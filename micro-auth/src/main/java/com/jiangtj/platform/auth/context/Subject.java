package com.jiangtj.platform.auth.context;

import lombok.Data;

@Data
public class Subject {
    String id;
    String name;
    String displayName;
    String avatar;
}
