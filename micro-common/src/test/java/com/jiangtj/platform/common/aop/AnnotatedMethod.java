package com.jiangtj.platform.common.aop;

import com.jiangtj.platform.common.aop.anno.AnnoM;
import com.jiangtj.platform.common.aop.anno.AnnoTM;

public class AnnotatedMethod {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
