package com.camon.dailylog.commons;

import lombok.Data;

import java.util.List;

/**
 * Created by jooyong on 2015-11-03.
 */
@Data
public class ErrorResponse {

    private String message;

    private String code;

    private List<FieldError> errors;

    @Data
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }

}
