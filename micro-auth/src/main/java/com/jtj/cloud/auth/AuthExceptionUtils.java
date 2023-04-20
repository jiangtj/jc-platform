package com.jtj.cloud.auth;

import com.jtj.cloud.common.BaseException;
import com.jtj.cloud.common.BaseExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class AuthExceptionUtils {

    /**
     * 401 未登录
     */
    public static BaseException unLogin() {
        BaseException exception = BaseExceptionUtils.unauthorized("缺少认证信息，请在header中携带token");
        exception.setTitle("未登录");
        return exception;
    }

    /**
     * 403 无权限
     */
    public static BaseException invalidToken(String msg, @Nullable Throwable cause) {
        BaseException exception = BaseExceptionUtils.from(HttpStatus.FORBIDDEN, msg, cause);
        exception.setTitle("Invalid Token");
        return exception;
    }

    /**
     * 403 No Permission
     */
    public static BaseException noPermission(String permission) {
        BaseException exception = BaseExceptionUtils.forbidden(String.format("Don't have permission<%s>", permission));
        exception.setTitle("No Permission");
        return exception;
    }

}
