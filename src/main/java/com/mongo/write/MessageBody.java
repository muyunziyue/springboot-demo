package com.mongo.write;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class MessageBody {

    private Integer messageId;

    private String messageTopic;

    private ChildMessageBody prev;

    private List<ChildMessageBody> children;

    private List<String> tags;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date dateField;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd", timezone = "GMT+8")
    private Date dateField2;

    private Double doubleField;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    List<Date> arrayDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd", timezone = "GMT+8")
    List<Date> arrayDate2;

    List<Integer> arrayInt;

    List<Double> arrayDouble;

    private Integer s;

}
