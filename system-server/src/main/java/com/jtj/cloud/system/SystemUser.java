package com.jtj.cloud.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtj.cloud.sql.reactive.db.LogicalDelete;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@LogicalDelete
public class SystemUser {
    @Id
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Integer isDeleted;
}
