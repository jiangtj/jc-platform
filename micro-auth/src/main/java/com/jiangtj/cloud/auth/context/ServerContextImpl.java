package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.UserClaims;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import io.jsonwebtoken.Claims;

public record ServerContextImpl(String token, Claims claims) implements AuthContext {
    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public UserClaims user() {
        throw BaseExceptionUtils.unauthorized("当前token用于服务间调用，不包含用户信息");
    }
}
