package com.bard.gplearning.aidl.aidlbean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Person implements Parcelable {
    private int age;
    private String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    protected Person(Parcel in) {
        age = in.readInt();
        name = in.readString();


        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(0,
                Integer.MAX_VALUE, 60 , TimeUnit.SECONDS, queue);

        poolExecutor.execute(() -> System.out.println("任务1：" + Thread.currentThread()));

        poolExecutor.execute(() -> {

        });
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
