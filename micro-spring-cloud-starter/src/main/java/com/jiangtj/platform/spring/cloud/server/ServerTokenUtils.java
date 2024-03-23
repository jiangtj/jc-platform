package com.jiangtj.platform.spring.cloud.server;

import com.jiangtj.platform.auth.context.AuthContext;
import org.springframework.lang.Nullable;

import java.util.Arrays;

public interface ServerTokenUtils {

    static boolean check(AuthContext ctx, @Nullable String target, String... source) {
        if (ctx instanceof ServerContextImpl sc) {
            if (!sc.hasAudience(target)) {
                return false;
            }

            if (source.length == 0) {
                return true;
            }

            return Arrays.stream(source)
                    .anyMatch(s -> s.equalsIgnoreCase(sc.getIssuer()));
        }
        return false;
    }

}
