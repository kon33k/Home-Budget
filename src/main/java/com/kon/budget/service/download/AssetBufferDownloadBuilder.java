package com.kon.budget.service.download;

import com.kon.budget.service.dtos.AssetDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetBufferDownloadBuilder {


    public StringBuffer prepareBuffer(List<AssetDto> dtos,
                                      String separator) {
        StringBuffer stringBuffer = new StringBuffer(
                "Amount" + separator + "Category" + separator + "Date" + separator + "Description");
        dtos.forEach(asset ->
                stringBuffer
                        .append("\n")
                        .append(asset.getAmount())
                        .append(separator)
                        .append(asset.getCategory())
                        .append(separator)
                        .append(asset.getIncomeDate())
                        .append(separator)
                        .append(asset.getDescription())
        );

        return stringBuffer;
    }
}
