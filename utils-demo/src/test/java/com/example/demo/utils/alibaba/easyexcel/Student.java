package com.example.demo.utils.alibaba.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ldx
 * @date 2022/7/28
 */
@Data
public class Student {
//    表名	ods_org_capacity
//    表注释	产能数据表
//    序号	字段名	字段注释	数据类型	备注
    @ExcelProperty({"表名", "表注释", "序号"})
    private String index;

    @ExcelProperty({"ods_org_capacity", "产能数据表", "字段名"})
    private String name;
    @ExcelProperty({ "", "", "字段注释"})
    private Integer age;
    @DateTimeFormat("yyyyMMdd")
    @ExcelProperty({"", "","数据类型"})
    private Date birthday;

}
