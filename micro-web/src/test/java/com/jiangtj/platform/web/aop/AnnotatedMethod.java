package com.jiangtj.platform.web.aop;

import com.jiangtj.platform.web.aop.anno.AnnoM;
import com.jiangtj.platform.web.aop.anno.AnnoTM;

public class AnnotatedMethod {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
