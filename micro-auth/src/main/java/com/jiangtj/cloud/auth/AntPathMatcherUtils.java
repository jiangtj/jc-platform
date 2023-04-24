package com.jiangtj.cloud.auth;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;

public interface AntPathMatcherUtils {

    AntPathMatcher matcher = new AntPathMatcher();

    static boolean match(String path, List<String> withPatterns, List<String> withoutPatterns){

        if (CollectionUtils.isEmpty(withPatterns)) {
            return false;
        }

        boolean flag = false;
        for (String ex: withPatterns) {
            if (matcher.match(ex, path)) {
                flag = true;
                break;
            }
        }
        if (!flag) return false;

        if (!CollectionUtils.isEmpty(withoutPatterns)) {
            for (String ex: withoutPatterns) {
                if (matcher.match(ex, path)) {
                    return false;
                }
            }
        }
        return true;
    }

}
