package com.myjava.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author lidexiu
 * @Date 2021/12/6
 * @Description
 */
public class OutputStreamTest {
    public static void main(String[] args) throws IOException {
//        try(OutputStream os = new FileOutputStream("/home/gooagoo/output/output.txt")){
//            os.write("Hello World".getBytes(StandardCharsets.UTF_8));
//            os.flush();
//        }

        byteArrayOutputStreamTest();

    }

    private static void byteArrayOutputStreamTest() throws IOException{
        byte[] data;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            bos.write("Hello 中国".getBytes(StandardCharsets.UTF_8));

            data = bos.toByteArray();
        }

        System.out.println(new String(data, StandardCharsets.UTF_8));
    }
}
