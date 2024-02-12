package com.jiangtj.platform.sql.jooq;

import jakarta.annotation.Resource;
import org.jooq.meta.jaxb.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public class GenerateService {
    @Resource
    DataSourceProperties properties;

    public Configuration createConfiguration(String packageName) {
        String url = properties.getUrl();
        String defaultSchema = url.split("//")[1].split("/")[1].split("\\?")[0];
        return createConfiguration(defaultSchema, packageName);
    }

    public Configuration createConfiguration(String schema, String packageName) {
        String url = properties.getUrl();
        String username = properties.getUsername();
        String password = properties.getPassword();
        return new Configuration()

                // Configure the database connection here
                .withJdbc(new Jdbc()
                        .withDriver(properties.getDriverClassName())
                        .withUrl(url)
                        .withUser(username)
                        .withPassword(password)
                )
                .withGenerator(new Generator()
                                .withDatabase(new Database()
                                        .withIncludes(".*")
                                        .withInputSchema(schema)
                                )
                                .withTarget(new Target()
                                        .withPackageName(packageName)
                                        .withDirectory("src/main/java")
                                )
                                .withGenerate(new Generate()

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

                                                .withPojos(true)
                                                .withPojosAsJavaRecordClasses(true)
                                )
                );
    }

}
