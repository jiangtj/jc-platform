package com.jiangtj.platform.spring.boot.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface SpelUtils {

    static EvaluationContext getMethodContext(Method method, Object[] args) {
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        context.setVariable("args", args);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            context.setVariable(parameter.getName(), arg);
        }
        return context;
    }

}
