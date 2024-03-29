package com.jiangtj.platform.system;

import com.jiangtj.platform.sql.jooq.GenerateHelper;
import jakarta.annotation.Resource;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
public class GenerateTest {

    @Resource
    DataSourceProperties properties;

    @Test
    public void generate() throws Exception {
        GenerateHelper.init(properties);
        GenerationTool.generate(new Configuration()
            .withJdbc(GenerateHelper.getJdbc())
            .withGenerator(new Generator()
                    .withName("com.jiangtj.platform.sql.jooq.ExtendGenerator")
                    .withDatabase(GenerateHelper.getDatabase(".*"))
                    .withTarget(GenerateHelper.getTarget("com.jiangtj.platform.system.jooq"))
                    .withGenerate(new Generate()
                            .withPojos(true)
                            .withPojosAsJavaRecordClasses(true)
                            .withValidationAnnotations(true)
                            .withDaos(true)
                            .withSpringAnnotations(true)

                        // Possible values for generatedAnnotationType
                        // - DETECT_FROM_JDK
                        // - JAVAX_ANNOTATION_GENERATED
                        // - JAVAX_ANNOTATION_PROCESSING_GENERATED
                        // - ORG_JOOQ_GENERATED
//                    .withGeneratedAnnotation(true)
//                    .withGeneratedAnnotationType(GeneratedAnnotationType.DETECT_FROM_JDK)
//                    .withGeneratedAnnotationDate(true)
//                    .withGeneratedAnnotationJooqVersion(true)
//                    .withNullableAnnotation(true)
//                    .withNullableAnnotationType("javax.annotation.Nullable")
//                    .withNonnullAnnotation(true)
//                    .withNonnullAnnotationType("javax.annotation.Nonnull")
//                    .withJpaAnnotations(true)
//                    .withJpaVersion("2.2")
//                    .withValidationAnnotations(true)
//
//                    .withKotlinSetterJvmNameAnnotationsOnIsPrefix(true)
//                    .withConstructorPropertiesAnnotation(true)
//                    .withConstructorPropertiesAnnotationOnPojos(true)
//                    .withConstructorPropertiesAnnotationOnRecords(true)
                    )
            ));
    }

}
