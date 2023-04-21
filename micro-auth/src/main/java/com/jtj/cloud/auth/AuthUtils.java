package com.jtj.cloud.auth;

public interface AuthUtils {

    static String toKey(String name) {
        return name.toLowerCase()
            .replace("-", "")
            .replace("_", "")
            .replace(" ", "");
    }

}
