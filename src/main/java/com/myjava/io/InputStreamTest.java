package com.myjava.io;

import java.io.*;

/**
 * @Author lidexiu
 * @Date 2021/12/6
 * @Description
 */
public class InputStreamTest {
    public static void main(String[] args) throws IOException {
        getFileByInputStreamTwo();
    }

    private static void getFileByInputStreamOne() throws IOException {
        String path = InputStreamTest.class.getResource("/").getPath();
        System.out.println(path);
        File file = new File(path + "test.log1");
        System.out.println(file.getCanonicalPath());
        try(InputStream inputStream = new FileInputStream(file)){
            int n = 0;
            byte[] bytes = new byte[128];
            for (int i =0;(n=inputStream.read())!=-1;i++){
                bytes[i] = (byte) i;
                System.out.println(n + "=" + (char)n);
            }
            String str = new String(bytes);
            System.out.println(str);
        }
    }

    private static void getFileByInputStreamTwo() throws IOException {
        String path = InputStreamTest.class.getResource("/").getPath();
        File file = new File(String.join("",path, "test.log1"));
        System.out.println(file.toString());
        StringBuilder sb = new StringBuilder();
        try(InputStream inputStream = new FileInputStream(file)){
            byte[] bytes = new byte[1024];
            int n = 0;
            while ((n=inputStream.read(bytes))!= -1){
                sb.append(new String(bytes, 0, n, "GBK"));
            }
        }
        System.out.println(sb.toString());
    }
}
