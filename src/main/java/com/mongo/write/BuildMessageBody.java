//package com.mongo.write;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//public class BuildMessageBody {
//
//    private static final Random random = new Random();
//
//    public static MessageBody buildMessageBody(int i, MessageBody parent) {
//        MessageBody messageBody = new MessageBody();
//        messageBody.setMessageId(i);
//        messageBody.setMessageTopic("message: " + i);
////        messageBody.setPrev(parent);
//
//        if(parent != null){
//            parent.setChildren(null);
//            parent.setArrayDate(null);
//            parent.setArrayDate2(null);
//            parent.setArrayDouble(null);
//            parent.setArrayInt(null);
//            parent.setPrev(null);
//            parent.setTags(null);
//        }
//
//        if(parent != null && random.nextInt(10) > 5){
//            parent.setDateField(null);
//        }
//
//        if(parent != null && random.nextInt(10) > 5){
//            parent.setDoubleField(null);
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setDoubleField(1.0);
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setArrayDouble(Arrays.asList(1.0,2.0));
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setDateField(new Date());
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setDateField2(new Date());
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setTags(Arrays.asList("1", "2", "3"));
//        }
//        if(random.nextInt(10) > 5){
//            messageBody.setChildren(buildChildren(i));
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setArrayDate(Arrays.asList(new Date(), new Date()));
//        }
//        if(random.nextInt(10) > 5){
//            messageBody.setArrayDate2(Arrays.asList(new Date(), new Date()));
//        }
//
//        messageBody.setArrayInt(Arrays.asList(1, 2));
//        return messageBody;
//    }
//
//    private static List<MessageBody> buildChildren(int i) {
//        MessageBody messageBody = new MessageBody();
//        if(random.nextInt(10) > 5){
//            messageBody.setMessageId(i + 100);
//        }
//        if(random.nextInt(10) > 5){
//            messageBody.setMessageTopic("message: " + (i +100));
//        }
//        if(random.nextInt(10) > 5){
//            messageBody.setDateField(new Date());
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setDateField2(new Date());
//        }
//
//        if(random.nextInt(10) > 5){
//            messageBody.setDoubleField(1.0);
//        }
//
//        messageBody.setDateField(new Date());
//        MessageBody messageBody2 = new MessageBody();
//        if(random.nextInt(10) > 5){
//            messageBody2.setMessageId(i + 100);
//        }
//        if(random.nextInt(10) > 5){
//            messageBody2.setMessageTopic("message: " + (i +100));
//        }
//        if(random.nextInt(10) > 5){
//            messageBody2.setDoubleField(1.0);
//        }
//        if(random.nextInt(10) > 5){
//            messageBody2.setDateField(new Date());
//        }
//        if(random.nextInt(10) > 5){
//            messageBody2.setDateField2(new Date());
//        }
//        return Arrays.asList(messageBody, messageBody2);
//    }
//
//}
