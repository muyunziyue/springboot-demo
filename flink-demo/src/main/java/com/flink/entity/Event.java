package com.flink.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldx
 * @date 2022/10/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    public String id;
    public String user;
    public String url;
    public Long timestamp;

}
