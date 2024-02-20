package com.jiangtj.platform.spring.boot.aop;

import com.jiangtj.platform.spring.boot.aop.anno.AnnoM;
import com.jiangtj.platform.spring.boot.aop.anno.AnnoTM;

public class AnnotatedMethod {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
