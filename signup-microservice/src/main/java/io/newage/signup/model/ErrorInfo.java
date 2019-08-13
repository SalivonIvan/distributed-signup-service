package io.newage.signup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorInfo implements Serializable {

    private String code;
    private String errorId;
    private String message;
    @Singular
    private List<String> variables;

}
