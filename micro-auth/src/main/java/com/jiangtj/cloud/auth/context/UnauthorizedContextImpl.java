package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.UserClaims;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import io.jsonwebtoken.Claims;

public class UnauthorizedContextImpl implements AuthContext {

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public UserClaims user() {
        throw BaseExceptionUtils.unauthorized("未授权");
    }

    @Override
    public String token() {
        throw BaseExceptionUtils.unauthorized("未授权");
    }

    @Override
    public Claims claims() {
        throw BaseExceptionUtils.unauthorized("未授权");
    }
}
