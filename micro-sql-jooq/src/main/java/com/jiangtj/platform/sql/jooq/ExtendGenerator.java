package com.jiangtj.platform.sql.jooq;

import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.TableDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
public class ExtendGenerator extends JavaGenerator {

    @Override
    protected void generateTableClassFooter(TableDefinition table, JavaWriter out) {
        super.generateTableClassFooter(table, out);
        if (GenerateHelper.isGeneratePojoTypeRef())
            generatePojoTypeForTableClass(table, out);
    }

    @Override
    protected void generateDaoClassFooter(TableDefinition table, JavaWriter out) {
        super.generateDaoClassFooter(table, out);
        if (GenerateHelper.isGeneratePageFetch())
            generatePageQueryForDaoClass(table, out);
    }

    /**
     * 创建一个分页查询方法，示例如下（）
     * <code>
     *    public Class<POJO> getPojoType() {
     *         return POJO.class;
     *     }
     * </code>
     */
    public void generatePojoTypeForTableClass(TableDefinition table, JavaWriter out) {
        String className = out.ref(getStrategy().getFullJavaClassName(table, GeneratorStrategy.Mode.POJO));
        out.javadoc("The class holding pojos for this type");
        printNonnullAnnotation(out);
        out.println("public %s<%s> getPojoType() {", Class.class, className);
        out.println("return %s.class;", className);
        out.println("}");
    }

    /**
     * 创建一个分页查询方法，示例如下（由于无法修改默认实现，所以需要生成时添加）
     * <code>
     *    public Page<P> fetchPage(Pageable pageable, Condition... conditions) {
     *         return PageUtils.selectFrom(ctx(), getTable())
     *             .conditions(conditions)
     *             .pageable(pageable)
     *             .fetchPage(getType());
     *     }
     * </code>
     */
    public void generatePageQueryForDaoClass(TableDefinition table, JavaWriter out) {
        String pojo = out.ref(getStrategy().getFullJavaClassName(table, GeneratorStrategy.Mode.POJO));
        String condition = out.ref(Condition.class);
        String pageable = out.ref(Pageable.class);
        String pageUtils = out.ref(PageUtils.class);
        out.javadoc("Fetch pages with pageable and conditions.");
        printNonnullAnnotation(out);
        out.println("public %s<%s> fetchPage(%s pageable, %s... conditions) {", Page.class, pojo, pageable, condition);
        out.println("return %s.selectFrom(ctx(), getTable())", pageUtils);
        out.println(".conditions(conditions)");
        out.println(".pageable(pageable)");
        out.println(".fetchPage(getType());");
        out.println("}");
    }
}
