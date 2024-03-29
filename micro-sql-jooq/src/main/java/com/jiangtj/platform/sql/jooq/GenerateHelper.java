package com.jiangtj.platform.sql.jooq;

import lombok.Getter;
import lombok.Setter;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DatabaseDriver;

public class GenerateHelper {

    static String url = null;
    @Setter
    static String username = null;
    @Setter
    static String password = null;
    @Setter
    static String driver = null;
    @Setter
    @Getter
    static boolean isGeneratePojoTypeRef = false;
    @Setter
    @Getter
    static boolean isGeneratePageFetch = true;

    public static void init(DataSourceProperties properties) {
        init(properties.getUrl(), properties.getUsername(), properties.getPassword());
        driver = properties.getDriverClassName();
    }

    public static void init(String url, String username, String password) {
        GenerateHelper.url = url;
        GenerateHelper.username = username;
        GenerateHelper.password = password;
        GenerateHelper.driver = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
    }

    public static String getSchemaFromUrl(String url) {
        return url.split("//")[1].split("/")[1].split("\\?")[0];
    }

    public static Jdbc getJdbc() {
        return new Jdbc()
            .withDriver(driver)
            .withUrl(url)
            .withUser(username)
            .withPassword(password);
    }

    public static Database getDatabase(String pattern) {
        return new Database()
            .withIncludes(pattern)
            .withInputSchema(getSchemaFromUrl(url));
    }

    public static Target getTarget(String packageName) {
        return new Target()
            .withPackageName(packageName)
            .withDirectory("src/main/java");
    }
}
