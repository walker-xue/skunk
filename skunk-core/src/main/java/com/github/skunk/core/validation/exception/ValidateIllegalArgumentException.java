package com.github.skunk.core.validation.exception;

public class ValidateIllegalArgumentException extends IllegalArgumentException {

    /**
     * Constructs an <code>IllegalArgumentException</code> with no
     * detail message.
     */
    public ValidateIllegalArgumentException() {
        super();
    }

    /**
     * Constructs an <code>IllegalArgumentException</code> with the
     * specified detail message.
     *
     * @param s
     *     the detail message.
     */
    public ValidateIllegalArgumentException(String s) {
        super(s);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param message
     *     the detail message (which is saved for later retrieval
     *     by the {@link Throwable#getMessage()} method).
     * @param cause
     *     the cause (which is saved for later retrieval by the
     *     {@link Throwable#getCause()} method).  (A <tt>null</tt> value
     *     is permitted, and indicates that the cause is nonexistent or
     *     unknown.)
     * @since 1.5
     */
    public ValidateIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param cause
     *     the cause (which is saved for later retrieval by the
     *     {@link Throwable#getCause()} method).  (A <tt>null</tt> value is
     *     permitted, and indicates that the cause is nonexistent or
     *     unknown.)
     * @since 1.5
     */
    public ValidateIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = -5365630128856068164L;

}
