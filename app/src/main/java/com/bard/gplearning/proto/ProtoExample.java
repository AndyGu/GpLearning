package com.bard.gplearning.proto;

import android.util.Log;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

public class ProtoExample {

    public static void main(String[] args){
        MyPersonProto.Card card1 = MyPersonProto.Card.newBuilder().setCName("card1").build();
        MyPersonProto.Card card2 = MyPersonProto.Card.newBuilder().setCName("card2").build();

        List<MyPersonProto.Card> list = new ArrayList<>();
        list.add(card1);
        list.add(card2);


        MyPersonProto.Person person = MyPersonProto.Person.newBuilder()
                .setName("gp")
                .setId(123)
                .setBoo(false)
                .addAllCList(list)
                .build();

        //把对象序列化成字节字符串
        ByteString byteString = person.toByteString();

        //从字节数组中解析成对象
        try {
            MyPersonProto.Person person1 = MyPersonProto.Person.parseFrom(byteString);
            Log.e("testProto", "person1="+person1.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }


        byte[] byteArray = person.toByteArray();
        try {
            MyPersonProto.Person person2 = MyPersonProto.Person.parseFrom(byteArray);
            Log.e("testProto", "person2="+person2.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    //创建card对象
//    val card1 = MyPersonProto.Card.newBuilder().setCName("card1").build()
//    val card2 = MyPersonProto.Card.newBuilder().setCName("card2").build()
//
//    val cardList = listOf(card1, card2)
//
//    //创建person对象
//    val person = MyPersonProto.Person.newBuilder()
//            .setName("gp")
//            .setId(344)
//            .setBoo(false)
//            .addAllCList(cardList)  //添加集合
//            .build()
//
//
//
//    //把对象序列化成字节数组
//    val array = person.toByteArray()
//
//    //从字节数组中解析成对象
//    val newPerson = MyPersonProto.Person.parseFrom(array)
}
