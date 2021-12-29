package com.example.demo.other;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Author lidexiu
 * @Date 2021/11/19
 * @Description
 */
public class URLEncodeTest {

    public static void main(String[] args) throws UnsupportedEncodingException {

        String str = "你好啊，世界";
        byte[] bytes = str.getBytes();

        System.out.println(bytes);

        String encode = URLEncoder.encode(str, StandardCharsets.UTF_8.toString());

        System.out.println(encode);

        String decode = URLDecoder.decode(encode, StandardCharsets.UTF_8.toString());
        System.out.println(decode);
    }
}
