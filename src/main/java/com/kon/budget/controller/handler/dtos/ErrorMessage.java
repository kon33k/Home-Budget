package com.kon.budget.controller.handler.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {

    /*
    tworzy wiadomośc
     */

    private String errorCode;
    private String errorDescription;


    public static final class ErrorMessageBuilder {
        private String errorCode;
        private String errorDescription;

        private ErrorMessageBuilder() {
        }

        public static ErrorMessageBuilder anErrorMessage() {
            return new ErrorMessageBuilder();
        }

        public ErrorMessageBuilder withErrorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorMessageBuilder withErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }

        public ErrorMessage build() {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorCode(errorCode);
            errorMessage.setErrorDescription(errorDescription);
            return errorMessage;
        }
    }
}
