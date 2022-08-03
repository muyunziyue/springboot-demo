package com.example.demo.utils.alibaba.easyexcel;
import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ldx
 * @date 2022/7/28
 */
public class ExportExcelDemo {
    public static void main(String[] args) {
        String path = "/home/data/output/export-excel.xlsx";

        EasyExcel.write(path, Student.class).sheet("学生信息").doWrite(getData());
    }

    private static List<Student> getData(){
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setName("张三");
        student1.setAge(0);
        student1.setBirthday(new Date());
        studentList.add(student1);

        Student student2 = new Student();
        student2.setName("李四");
        student2.setAge(20);
        student2.setBirthday(new Date());
        studentList.add(student2);

        return studentList;
    }
}
