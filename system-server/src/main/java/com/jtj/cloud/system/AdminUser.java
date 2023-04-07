package com.jtj.cloud.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtj.cloud.common.reactive.db.LogicalDelete;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@LogicalDelete
public class AdminUser {
    @Id
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Integer isDeleted;
}
