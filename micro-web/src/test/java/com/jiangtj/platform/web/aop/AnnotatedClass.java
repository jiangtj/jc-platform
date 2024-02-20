package com.jiangtj.platform.web.aop;

import com.jiangtj.platform.web.aop.anno.AnnoM;
import com.jiangtj.platform.web.aop.anno.AnnoT;
import com.jiangtj.platform.web.aop.anno.AnnoTM;

@AnnoT
@AnnoTM
public class AnnotatedClass {

    @AnnoM
    public void doSome1() {}

    @AnnoTM
    public void doSome2() {}

}
