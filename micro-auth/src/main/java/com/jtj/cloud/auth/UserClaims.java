package com.jtj.cloud.auth;

import lombok.Builder;

import java.util.List;

@Builder
public record UserClaims(String id, List<String> roles) {

}
