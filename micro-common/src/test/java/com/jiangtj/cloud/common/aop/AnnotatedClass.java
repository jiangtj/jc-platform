package com.jiangtj.cloud.common.aop;

import com.jiangtj.cloud.common.aop.anno.AnnoM;
import com.jiangtj.cloud.common.aop.anno.AnnoT;
import com.jiangtj.cloud.common.aop.anno.AnnoTM;

@AnnoT
@AnnoTM
public class AnnotatedClass {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
