package com.example.demo.JUC.volatile_demo;

import java.util.concurrent.atomic.AtomicInteger;

public class MyData {

//    int number = 0;
    volatile int number = 0;

    public void addTo1() {
        this.number = 1;
    }

    //
    public void add() {
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();

    public void addMyAtomic() {
        atomicInteger.getAndIncrement();
    }
}