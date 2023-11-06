package com.jiangtj.cloud.auth;

public interface KeyUtils {

    static String toKey(String name) {
        return name.toLowerCase()
            .replace("-", "")
            .replace("_", "")
            .replace(" ", "");
    }

}
