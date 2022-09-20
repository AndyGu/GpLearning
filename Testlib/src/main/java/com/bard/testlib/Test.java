package com.bard.testlib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class Test implements Runnable{

    static int i = 0;

    boolean flag;

    public Test(boolean flag) {
        this.flag = flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * 修饰实例方法
     */
    public static synchronized void increase(){
        System.out.println("测试:"+Thread.currentThread().getName()+",i="+i++);
    }

    public static synchronized void increase2(){
        System.out.println("测试2:"+Thread.currentThread().getName()+",i="+i++);
    }

    @Override
    public synchronized void run() {
        for(int j = 0;j < 50; j++){
            if(flag){
                increase();
            }else{
//                increase2();
                System.out.println("测试2:"+Thread.currentThread().getName()+",i="+i++);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread t1 = new Thread(new Test(true));
//        Thread t2 = new Thread(new Test(false));
//        t1.setName("线程1");
//        t1.start();
//        t1.join();
//        t2.setName("线程2");
//        t2.start();
//        t2.join();
//        System.out.println(i);

//        Test test = new Test(true);
//        Thread t1 = new Thread(test);
//        Thread t2 = new Thread(test);
//        t1.setName("线程1");
////        t1.join();
//        t2.setName("线程2");
////        test.setFlag(false);
//
//        t1.start();
//        test.setFlag(false);
//        t2.start();
////        t2.join();
//        System.out.println(i);


        final MyTest myTest = new MyTest();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
//                myTest.test2();
                MyTest.test3();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                MyTest.test1();
            }
        });

//        thread1.start();
//        thread2.start();

        System.out.println("result="+(-10 >>> 16));


        Person p = new Person(1L, "陈俊生", 100);
        System.out.println("person Serializable:" + p);
        try {
            FileOutputStream fos = new FileOutputStream("Person.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(p);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


class MyTest{
    static int i = 100;

    public static synchronized void test1(){
        for(int j = 0;j < 50; j++){
            System.out.println("mytest111:"+Thread.currentThread().getName()+",i="+i++);
        }
    }

    public synchronized void test2(){
        for(int j = 0;j < 50; j++){
            System.out.println("mytest222:"+Thread.currentThread().getName()+",i="+i++);
        }
    }

    public static synchronized void test3(){
        for(int j = 0;j < 50; j++){
            System.out.println("mytest333:"+Thread.currentThread().getName()+",i="+i++);
        }
    }
}