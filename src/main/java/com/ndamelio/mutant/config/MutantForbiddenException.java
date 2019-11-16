package com.ndamelio.mutant.config;

public class MutantForbiddenException extends Exception{

    public MutantForbiddenException() {
        super();
    }

    public MutantForbiddenException(String message) {
        super(message);
    }

    public MutantForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public MutantForbiddenException(Throwable cause) {
        super(cause);
    }

    protected MutantForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}