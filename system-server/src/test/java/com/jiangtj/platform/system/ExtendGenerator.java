package com.jiangtj.platform.system;

import lombok.extern.slf4j.Slf4j;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.TableDefinition;

@Slf4j
public class ExtendGenerator extends JavaGenerator {

    @Override
    protected void generateTableClassFooter(TableDefinition table, JavaWriter out) {
        super.generateTableClassFooter(table, out);
        generatePojoTypeForTableClass(table, out);
    }

    public void generatePojoTypeForTableClass(TableDefinition table, JavaWriter out) {
        String className = out.ref(getStrategy().getFullJavaClassName(table, GeneratorStrategy.Mode.POJO));
        out.javadoc("The class holding pojos for this type");
        printNonnullAnnotation(out);
        out.println("public %s<%s> getPojoType() {", Class.class, className);
        out.println("return %s.class;", className);
        out.println("}");
    }
}
