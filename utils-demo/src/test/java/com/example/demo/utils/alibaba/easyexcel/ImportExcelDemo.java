package com.example.demo.utils.alibaba.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 * @author ldx
 * @date 2022/7/28
 */
public class ImportExcelDemo {
    public static void main(String[] args) {
        String path = "/home/data/output/export-excel.xlsx";
        EasyExcel.read(path, Student.class, new ExcelListener()).sheet().doRead();
        EasyExcel.read(path, Student.class, new ExcelListener()).doReadAll();
    }

    //方法1：通过自定义的listener读取excel

}
