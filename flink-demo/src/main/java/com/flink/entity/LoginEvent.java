package com.flink.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldx
 * @date 2022/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginEvent {
    private String userId;
    private String ipAddress;
    private String eventType;
    private Long timestamp;
}
