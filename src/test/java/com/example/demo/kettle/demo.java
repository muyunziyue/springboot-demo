package com.example.demo.kettle;

/**
 * @Author lidexiu
 * @Date 2021/9/25
 * @Description
 */
public class demo {
    public static void main(String[] args) {
        String[] line = new String[]{"日期,排名,商品信息,店铺名称,商品链接,图片链接,商品ID,商品价格,访客人数,搜索人数,成交金额,支付件数,支付转化率,UV价值", "2021/8/24,1,雅塑奥利司他胶囊体验装减肥瘦身燃脂排油丸男女瘦身药减脂肪暴瘦,阿里健康大药房,https://detail.tmall.com/item.htm?id=607834170578,https://img.alicdn.com/bao/uploaded/i2/2206581099089/O1CN015gMfcC2H0oXgXTHT0_!!2206581099089-0-scmitem8000.jpg,\"\t607834170578\",2,1001,437,5275,0,0,5.27"};
        String[] targetFields = new String[]{"日期", "排名", "商品信息", "店铺名称", "商品链接", "图片链接", "商品ID", "商品价格", "访客人数", "搜索人数", "成交金额", "支付件数", "支付转化率", "UV价值"};
        String[] sourceFields = new String[100];
        long lineCount = 0;
        for (int j = 0; j < line.length; j++) {
            String[] split = line[j].split(",");
            lineCount++;

            if (lineCount == 1) {
                for (int i = 0; i < split.length; i++) {
                    sourceFields[i] = split[i];
                }

            }
            for (int i1 = 0; i1 < targetFields.length; i1++) {
                for (int i = 0; i < sourceFields.length; i++) {
                    if (targetFields[i1].equals(sourceFields[i])) {
                        // 赋予目标字段
//                    get(Fields.Out, targetFields.get(i)).setValue(r, split[i1]);
                        System.out.println(targetFields[i1] + "::" + split[i]);
                    }
                }

            }

//        sourceFields.forEach(System.out::println);


        }
    }
}
