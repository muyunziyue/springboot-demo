package com.flink.project.zeye;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Student> stuList = new ArrayList();
        stuList.add(new Student("xx",5L));
        stuList.add(new Student("test",7L));
        stuList.add(new Student("test",7L));
        stuList.add(new Student("ss",3L));
        stuList.add(new Student("aa",8L));

        Collections.sort(stuList);  // 调用排序方法
        for (Student student : stuList) {
            System.out.println(student.getAge());
         }
    }

    public static class Student implements Comparable<Student> {
        private String name;
        private Long age;

        public Student(String name, Long age) {
            this.name = name;
            this.age = age;
        }

        public Student(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getAge() {
            return age;
        }

        public void setAge(Long age) {
            this.age = age;
        }

        @Override
        public int compareTo(Student student) {
            return (this.age > student.age) ? -1 : ((this.age == student.age) ? 0 : 1);

        }
    }
}
