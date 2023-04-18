package com.jtj.cloud.auth.reactive;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthReactiveWebFilterTest {

    @Test
    void testBuilder() {
        assertThrows(NullPointerException.class, () -> AuthReactiveWebFilter.builder().build());

        AuthReactiveWebFilter filter1 = AuthReactiveWebFilter.builder()
            .filter(handler -> handler)
            .build();
        assertArrayEquals(new String[]{"/**"}, filter1.getIncludePatterns().toArray());
        assertEquals(1, filter1.getExcludePatterns().size());

        AuthReactiveWebFilter filter2 = AuthReactiveWebFilter.builder()
            .include("/a","/b","/c/d")
            .exclude("/c","/d","/e/f")
            .filter(handler -> handler)
            .build();
        assertArrayEquals(new String[]{"/a","/b","/c/d"}, filter2.getIncludePatterns().toArray());
        assertArrayEquals(new String[]{"/c","/d","/e/f","/actuator/**"}, filter2.getExcludePatterns().toArray());

        assertEquals(0, AuthReactiveWebFilter.builder()
            .includeActuator()
            .filter(handler -> handler)
            .build()
            .getExcludePatterns()
            .size());

        assertEquals(1, AuthReactiveWebFilter.builder()
            .exclude("/actuator/**")
            .filter(handler -> handler)
            .build()
            .getExcludePatterns()
            .size());
    }
}