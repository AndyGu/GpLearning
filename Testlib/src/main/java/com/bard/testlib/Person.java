package com.bard.testlib;

public class Person {
    public Long id;
    public String name;
    public int age = 1;

    public Person(Long id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String tSoString() {
        return id.toString() + "--" + name.toString() + "age:" + age;
    }

}
