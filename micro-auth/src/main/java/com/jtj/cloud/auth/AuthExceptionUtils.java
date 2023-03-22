package com.jtj.cloud.auth;

import com.jtj.cloud.common.BaseException;
import com.jtj.cloud.common.BaseExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class AuthExceptionUtils {

    /**
     * 400
     */
    public static BaseException invalidToken(String msg, @Nullable Throwable cause) {
        BaseException exception = BaseExceptionUtils.from(HttpStatus.UNAUTHORIZED, msg, cause);
        exception.setTitle("Invalid Token");
        return exception;
    }

}
