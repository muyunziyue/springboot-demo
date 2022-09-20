package com.flink.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldx
 * @date 2022/9/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    String key;
    Integer count;
}
