package com.jtj.cloud.sql;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.sql.Types;

/**
 * Configuration properties for dbc.
 */
@Data
@ConfigurationProperties(prefix = "db")
public class SQLPublicProperties {

    /**
     * URL of the database. database name, username, password and pooling options
     * without prefix jdbc: r2dbc:
     */
    private String url;

    /**
     * Login username of the database. Set if no username is specified in the url.
     */
    private String username;

    /**
     * Login password of the database. Set if no password is specified in the url.
     */
    private String password;
}
