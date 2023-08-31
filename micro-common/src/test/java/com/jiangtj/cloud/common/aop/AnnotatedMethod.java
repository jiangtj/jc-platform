package com.jiangtj.cloud.common.aop;

import com.jiangtj.cloud.common.aop.anno.AnnoM;
import com.jiangtj.cloud.common.aop.anno.AnnoTM;

public class AnnotatedMethod {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
