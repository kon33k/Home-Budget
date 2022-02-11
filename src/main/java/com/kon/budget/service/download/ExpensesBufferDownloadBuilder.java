package com.kon.budget.service.download;

import com.kon.budget.service.dtos.ExpensesDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesBufferDownloadBuilder {

    public StringBuffer prepareBuffer(List<ExpensesDto> dtos,
                                      String separator) {
        StringBuffer stringBuffer = new StringBuffer(
                "Amount" + separator + "Category" + separator + "Date");
        dtos.forEach(asset ->
                stringBuffer
                        .append("\n")
                        .append(asset.getAmount())
                        .append(separator)
                        .append(asset.getCategory())
                        .append(separator)
                        .append(asset.getPurchaseData())
        );

        return stringBuffer;
    }
}
