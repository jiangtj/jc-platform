package com.jiangtj.platform.common.aop;

import com.jiangtj.platform.common.aop.anno.AnnoM;
import com.jiangtj.platform.common.aop.anno.AnnoT;
import com.jiangtj.platform.common.aop.anno.AnnoTM;

@AnnoT
@AnnoTM
public class AnnotatedClass {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
