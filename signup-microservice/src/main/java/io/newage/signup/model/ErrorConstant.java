package io.newage.signup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorConstant {

    ERR_VALIDATION("EV", "error.validation.0001", "Validation failed for model.");

    private String code;
    private String errorId;
    private String message;

}
