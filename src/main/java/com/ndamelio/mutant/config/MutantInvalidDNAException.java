package com.ndamelio.mutant.config;

public class MutantInvalidDNAException extends Exception {
    public MutantInvalidDNAException() {
        super();
    }

    public MutantInvalidDNAException(String message) {
        super(message);
    }

    public MutantInvalidDNAException(String message, Throwable cause) {
        super(message, cause);
    }

    public MutantInvalidDNAException(Throwable cause) {
        super(cause);
    }

    protected MutantInvalidDNAException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}