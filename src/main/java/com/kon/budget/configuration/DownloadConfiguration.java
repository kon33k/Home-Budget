package com.kon.budget.configuration;

import com.kon.budget.enums.DownloadSeparatorEnum;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "download.files")
@Setter
public class DownloadConfiguration {

    private String separator;
    private String assetsFilename;
    private String expensesFilename;

    public String getSeparator() {
        return separator != null
                ? DownloadSeparatorEnum.valueOf(separator.toUpperCase()).getSign()
                : DownloadSeparatorEnum.SEMICOLON.getSign();
    }

    public String getAssetsFilename() {
        return assetsFilename != null ? assetsFilename : "assetsFilename";
    }

    public String getExpensesFilename() {
        return expensesFilename != null ? expensesFilename : "expensesFilename";
    }
}
