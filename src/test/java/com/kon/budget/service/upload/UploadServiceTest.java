package com.kon.budget.service.upload;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.enums.ExpensesCategory;
import com.kon.budget.service.AssetsService;
import com.kon.budget.service.ExpensesService;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.service.dtos.ExpensesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {


    @Mock
    private AssetsService assetsService;

    @Mock
    private ExpensesService expensesService;

    private final ParseAssetsService parseAssetsService = new ParseAssetsService();
    private final ParseExpensesService parseExpensesService = new ParseExpensesService();
    private UploadService uploadService;

    @BeforeEach
    public void setup() {
        uploadService = new UploadService(assetsService,expensesService, parseAssetsService, parseExpensesService);
    }

    @Test
    void shouldCorrectParseCsvToDtoListAndCallSetAssetFromService() throws IOException {
        //given
        var file = mock(MultipartFile.class);
        var stringCsv = "Amount;Category;Date;Description\n"
                + "200;RENT;2022-02-02;test description";
        var byteCsv = stringCsv.getBytes(StandardCharsets.UTF_8);
        var importCsv = new ByteArrayInputStream(byteCsv);
        when(file.getInputStream()).thenReturn(importCsv);

        var dtos = asList(
                AssetDto.builder()
                            .amount(new BigDecimal(200))
                            .category(AssetCategory.RENT)
                            .incomeDate(LocalDateTime.parse("2022-02-02T00:00:00.000000001"))
                            .description("test description")
                            .build()
        );

        //when
        uploadService.uploadFile(file);

        //then
        Mockito.verify(assetsService, Mockito.times(1)).setAsset(dtos);

    }

    @Test
    void shouldCorrectParseCsvToDtoListAndCallSetExpensesFromService() throws IOException {
        //given
        var file = mock(MultipartFile.class);
        var stringCsv = "Amount;Category;Date\n"
                + "200;FUN;2022-02-02";
        var byteCsv = stringCsv.getBytes(StandardCharsets.UTF_8);
        var importCsv = new ByteArrayInputStream(byteCsv);
        when(file.getInputStream()).thenReturn(importCsv);

        var dtos = asList(
                ExpensesDto.builder()
                        .amount(new BigDecimal(200))
                        .category(ExpensesCategory.FUN)
                        .purchaseData(LocalDateTime.parse("2022-02-02T00:00:00.000000001"))
                        .build()
        );

        //when
        uploadService.uploadFile(file);

        //then
        Mockito.verify(expensesService, Mockito.times(1)).setExpenses(dtos);

    }
}
