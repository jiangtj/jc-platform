package com.jiangtj.platform.spring.cloud.jwt;

import io.jsonwebtoken.Locator;

import java.security.Key;

public interface AuthKeyLocator extends Locator<Key> {
}
