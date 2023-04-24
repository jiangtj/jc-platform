package com.jiangtj.cloud.auth;

import lombok.Builder;

import java.util.List;

@Builder
public record UserClaims(String id, List<String> roles) {

}
