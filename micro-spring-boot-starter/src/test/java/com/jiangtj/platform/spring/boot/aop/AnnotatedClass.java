package com.jiangtj.platform.spring.boot.aop;

import com.jiangtj.platform.spring.boot.aop.anno.AnnoM;
import com.jiangtj.platform.spring.boot.aop.anno.AnnoT;
import com.jiangtj.platform.spring.boot.aop.anno.AnnoTM;

@AnnoT
@AnnoTM
public class AnnotatedClass {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
