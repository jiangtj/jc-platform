package com.jiangtj.platform.auth.context;

import org.springframework.http.HttpRequest;
import org.springframework.lang.Nullable;

/**
 * 授权上下文转换器接口
 */
public interface AuthContextConverter {

    /**
     * 将HttpRequest转换为AuthContext对象。
     *
     * @param request 要转换的HttpRequest对象
     * @return 转换后的AuthContext对象，如果转换失败则返回null
     */
    @Nullable
    AuthContext convert(HttpRequest request);

}
