package com.jiangtj.platform.system;

import com.jiangtj.platform.sql.jooq.GenerateService;
import jakarta.annotation.Resource;
import org.jooq.DSLContext;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GenerateTest {

    @Test
    public void generate(@Autowired GenerateService service) throws Exception {
        Configuration configuration = service.createConfiguration("com.jiangtj.platform.system.jooq");
        GenerationTool.generate(configuration);
    }

}
