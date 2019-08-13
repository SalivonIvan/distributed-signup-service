package io.newage.signup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorConstant {

    ERR_UNKNOWN("EC", "error.unknown.0001", "Process failed."),
    ERR_VALIDATION("EV", "error.validation.0002", "Validation failed for model.");

    private String code;
    private String errorId;
    private String message;

}
