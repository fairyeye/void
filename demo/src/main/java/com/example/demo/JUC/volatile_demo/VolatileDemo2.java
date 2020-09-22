package com.example.demo.JUC.volatile_demo;

import java.util.concurrent.TimeUnit;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/15 16:25
 * @desc 验证volatile的可见性
 */
public class VolatileDemo2 {
    public static void main(String[] args) {
        MyData myData = new MyData();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTo1();
            System.out.println(Thread.currentThread().getName() + "\t updated number value:" + myData.number);
        }, "A").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            while (myData.number == 0) {
            // B 线程一直在这里等待循环 直到number不等于0
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(myData.number);
            }
        }, "B").start();

        System.out.println(Thread.currentThread().getName() + "\t mission is over, main get number value :" + myData.number);
    }
}
