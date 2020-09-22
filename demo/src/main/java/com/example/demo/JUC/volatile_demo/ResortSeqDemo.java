package com.example.demo.JUC.volatile_demo;

class MyResort {
    int a = 0;
    boolean flag = false;

    public void method1() {
        a = 1;
        flag = true;
    }
    public void method2() {
        if (flag) {
            a = a + 5;
            if (a != 6) {
                System.out.println(Thread.currentThread().getName() + "\t finally a value is :" + a);
            }
        } else {
            System.out.println(Thread.currentThread().getName() + "\t a value is :" + a);
        }
    }
}


/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/17 16:31
 */
public class ResortSeqDemo {

    public static void main(String[] args) {
        MyResort resort = new MyResort();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                resort.method1();
                resort.method2();
            }, String.valueOf(i)).start();
        }

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t mission completed.");
    }
}
