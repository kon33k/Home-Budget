package com.kon.budget.service.download;

import com.kon.budget.configuration.DownloadConfiguration;
import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.DownloadSpecificationEnum;
import com.kon.budget.enums.FilterParameterCalendarEnum;
import com.kon.budget.service.AssetsService;
import com.kon.budget.service.ExpensesService;
import com.kon.budget.service.dtos.AssetDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DownloadServiceTest {

    @Mock
    private AssetsService assetsService;
    @Mock
    private ExpensesService expensesService;

    private ResponsePrepareService responsePrepareService = new ResponsePrepareService();
    private AssetBufferDownloadBuilder assetBufferDownloadBuilder = new AssetBufferDownloadBuilder();
    private ExpensesBufferDownloadBuilder expensesBufferDownloadBuilder = new ExpensesBufferDownloadBuilder();
    private DownloadConfiguration downloadConfiguration = new DownloadConfiguration();

    private DownloadService downloadService;


    @BeforeEach
    public void setup() {
        downloadService = new DownloadService(
                assetsService,
                expensesService,
                responsePrepareService,
                assetBufferDownloadBuilder,
                expensesBufferDownloadBuilder,
                downloadConfiguration
        );
    }

    @ParameterizedTest
    @MethodSource
    void shouldCheckIfThereIsFileWithHeadersInResponse(
            DownloadSpecificationEnum specificationEnum,
            String expectedHeader
    ) throws UnsupportedEncodingException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        downloadService.getFileToDownload(response, specificationEnum, null);

        // then
        assertThat(response.getContentAsString()).contains(expectedHeader);

    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    void shouldCheckCorrectDataAreMappedToCsvFile(
            String testName,
            ParameterTestData testData
    ) throws UnsupportedEncodingException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String expectedHeader = "Amount;Category;Date;Description";

        setMockWhenAssetsServiceIsCall(testData.dtos, testData.filter);

        // when
        downloadService.getFileToDownload(
                response,
                DownloadSpecificationEnum.ASSETS,
                testData.filter
        );

        // then
        assertThat(response.getContentAsString()).contains(expectedHeader);
        assertThat(response.getContentAsString()).contains(testData.expectedCsvData);

    }

    private void setMockWhenAssetsServiceIsCall(List<AssetDto> dtos,
                                                Map<String, String> filter) {
        if (Objects.isNull(filter)) {
            when(assetsService.getAllAssets()).thenReturn(dtos);
        } else {
            when(assetsService.getFilteredAssets(filter)).thenReturn(dtos);
        }
    }

    private static Stream<Arguments> shouldCheckIfThereIsFileWithHeadersInResponse() {
        return Stream.of(
                Arguments.of(
                        DownloadSpecificationEnum.ASSETS,
                        "Amount;Category;Date;Description"
                ),
                Arguments.of(
                        DownloadSpecificationEnum.EXPENSES,
                        "Amount;Category;Date"
                )
        );
    }

    private static Stream<Arguments> shouldCheckCorrectDataAreMappedToCsvFile() {
        var date = LocalDateTime.parse("2020-02-02T02:02:02.000000001");
        return Stream.of(
                Arguments.of(
                        "all assets",
                        new ParameterTestData(
                                null,
                                asList(
                                        AssetDto.builder()
                                                .amount(BigDecimal.ONE)
                                                .category(AssetCategory.RENT)
                                                .incomeDate(date)
                                                .description("some desc")
                                                .build()
                                ),
                                asList("1;RENT;" + date.toString() + ";some desc")
                        )
                ),
                Arguments.of(
                        "assets with filter",
                        new ParameterTestData(
                                new HashMap<>() {{
                                    put(FilterParameterCalendarEnum.FROM_DATE.getKey(), "2020-02-02");
                                    put(FilterParameterCalendarEnum.TO_DATE.getKey(), "2020-02-02");
                                }},
                                asList(
                                        AssetDto.builder()
                                                .amount(BigDecimal.TEN)
                                                .category(AssetCategory.RENT)
                                                .incomeDate(date)
                                                .description("some desc")
                                                .build()
                                ),
                                asList("10;RENT;" + date + ";some desc")
                        )
                )
        );
    }

    private static class ParameterTestData {
        public Map<String, String> filter;
        public List<AssetDto> dtos;
        public List<String> expectedCsvData;

        public ParameterTestData(Map<String, String> filter,
                                 List<AssetDto> dtos,
                                 List<String> expectedCsvData) {
            this.filter = filter;
            this.dtos = dtos;
            this.expectedCsvData = expectedCsvData;
        }
    }
}
