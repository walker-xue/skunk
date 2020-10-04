package com.github.skunk.core.validation.exception;

/**
 *
 */
public class ValidateIndexOutOfBoundsException extends IndexOutOfBoundsException {

    private static final long serialVersionUID = 5960395642045730583L;

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with no detail message.
     */
    public ValidateIndexOutOfBoundsException() {
        super();
    }

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with the specified
     * detail message.
     *
     * @param s
     *     the detail message.
     */
    public ValidateIndexOutOfBoundsException(String s) {
        super(s);
    }
}
