package com.skunk.core.validation.exception;

public class ValidateNullPointerException extends NullPointerException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code NullPointerException} with no detail message.
     */
    public ValidateNullPointerException() {
        super();
    }

    /**
     * Constructs a {@code NullPointerException} with the specified
     * detail message.
     *
     * @param s
     *     the detail message.
     */
    public ValidateNullPointerException(String s) {
        super(s);
    }
}
