package com.jtj.cloud.auth;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AntPathMatcherUtilsTest {

    @Test
    void match() {
        String path = "/a/b/c";
        assertTrue(matchWith(path, "/**"));
        assertTrue(matchWith(path, "/a/**"));
        assertTrue(matchWith(path, "/a/b/c", "/b"));
        assertTrue(matchWith(path, "/b", "/a/b/c"));
        assertFalse(matchWith(path));
        assertFalse(matchWith(path, "/b/**"));
        assertFalse(matchWith(path, "/a/b"));
        assertFalse(matchWith(path, "/a/b/c/d"));

        assertFalse(matchWithout(path, "/**"));
        assertFalse(matchWithout(path, "/a/**"));
        assertFalse(matchWithout(path, "/a/b/c", "/b"));
        assertFalse(matchWithout(path, "/b", "/a/b/c"));
        assertTrue(matchWithout(path));
        assertTrue(matchWithout(path, "/b/**"));
        assertTrue(matchWithout(path, "/a/b"));
        assertTrue(matchWithout(path, "/a/b/c/d"));
    }

    boolean matchWith(String path, String... patterns) {
        return AntPathMatcherUtils.match(path, Arrays.asList(patterns), Collections.emptyList());
    }

    boolean matchWithout(String path, String... patterns) {
        return AntPathMatcherUtils.match(path, Collections.singletonList("/**"), Arrays.asList(patterns));
    }
}