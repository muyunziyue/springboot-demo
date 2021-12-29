package com.myjava.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @Author lidexiu
 * @Date 2021/12/16
 * @Description
 */
public class GsonTest {

    public static void main(String[] args) {


        String namesJson = "['xiaoqiang','chenrenxiang',null]";
        Gson gson = new Gson();
        // 反序列化
        String[] nameArray = gson.fromJson(namesJson, String[].class);
        // 序列化
        String s = gson.toJson(nameArray);
        JsonObject asJsonObject = JsonParser.parseString(namesJson).getAsJsonObject();
        JsonArray xx = asJsonObject.getAsJsonArray("xx");
        System.out.println(s);
    }
}
