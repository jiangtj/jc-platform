package com.jtj.cloud.apigateway;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtj.cloud.common.reactive.db.LogicalDelete;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created At 2020/10/12.
 */
@Data
@LogicalDelete
public class AdminUser {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modifyTime;
    
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Integer isDeleted;
}
