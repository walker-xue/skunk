package com.skunk.core.exception;

import java.io.IOException;

/**
 * 网络主机异常.
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
public final class HostException extends RuntimeException {

    private static final long serialVersionUID = 3589264847881174997L;

    public HostException(final IOException cause) {
        super(cause);
    }
}
