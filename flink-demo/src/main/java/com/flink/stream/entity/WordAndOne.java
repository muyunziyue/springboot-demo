package com.flink.stream.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldx
 * @date 2022/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordAndOne {
    /**
     * word
     */
    public String word;
    /**
     * count
     */
    public Integer count;
}
