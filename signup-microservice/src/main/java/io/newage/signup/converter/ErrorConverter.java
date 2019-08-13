package io.newage.signup.converter;

import io.newage.signup.model.ErrorConstant;
import io.newage.signup.model.ErrorInfo;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;

@Converter
public class ErrorConverter {

    private ErrorConverter() {
    }

    @Converter
    public static ErrorInfo toErrorInfo(ErrorConstant errorConstant, Exchange exchange) {
        return ErrorInfo.builder()
                .code(errorConstant.getCode())
                .errorId(errorConstant.getErrorId())
                .message(errorConstant.getMessage())
                .variable(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class).getMessage())
                .build();
    }
}
