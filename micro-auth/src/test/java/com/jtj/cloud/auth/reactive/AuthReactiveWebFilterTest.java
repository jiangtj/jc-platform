package com.jtj.cloud.auth.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class AuthReactiveWebFilterTest {

    @Test
    void testBuilder() {
        assertThrows(NullPointerException.class, () -> new AuthReactiveWebFilter.builder().build());

        AuthReactiveWebFilter filter1 = new AuthReactiveWebFilter.builder()
            .filter(handler -> Mono.empty().then())
            .build();
        assertArrayEquals(new String[]{"/**"}, filter1.getIncludePatterns().toArray());
        assertTrue(filter1.getExcludePatterns().isEmpty());
        assertNull(filter1.filter(new AuthReactorHandler()).block());

        AuthReactiveWebFilter filter2 = new AuthReactiveWebFilter.builder()
            .include("/a","/b","/c/d")
            .exclude("/c","/d","/e/f")
            .filter(handler -> Mono.empty().then())
            .build();
        assertArrayEquals(new String[]{"/a","/b","/c/d"}, filter2.getIncludePatterns().toArray());
        assertArrayEquals(new String[]{"/c","/d","/e/f"}, filter2.getExcludePatterns().toArray());
    }
}