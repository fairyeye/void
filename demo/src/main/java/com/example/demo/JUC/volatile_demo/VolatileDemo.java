package com.example.demo.JUC.volatile_demo;

import java.util.concurrent.TimeUnit;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/15 16:25
 * @desc
 */
public class VolatileDemo {
    public static void main(String[] args) {

    }

    /**
     * 2.验证volatile不保证原子性
     *  2.1
     */
    private static void atomicVerify() {
        MyData myData = new MyData();

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    // 如果myData.add()添加了 synchronized 则可以保证 mydata.number 结果为20000 否则 number总是小于20000
                    myData.add();
                    myData.addMyAtomic();
                }
            }, String.valueOf(i)).start();
        }

        // 如果存活数量大于2 说明上面的线程还在执行
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t int type, finally number value is:" + myData.number);
        System.out.println(Thread.currentThread().getName() + "\t AtomicInteger type, finally number value is:" + myData.atomicInteger);
    }

    /**
     * 1.验证volatile可见性
     *  结果：volatile保证可见性
     * 在number 没有加volatile关键字修饰前 没有可见性
     */
    private static void seeOkByVolatile() {
        MyData myData = new MyData();

        // 声明一个线程A 然后对number进行更新
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

        int n = 0;

        while (myData.number == 0) {
            // 如果不加volatile关键字 while判断会进入死循环 因为main线程不会知道线程A更新后的number 由此可证：volatile可见性
            // main 线程一直在这里等待循环 直到number不等于0
            // TIPS: 如果使用print/sleep 会刷新主线程中number的值 可以获取到线程A更新后的number
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(myData.number);
            n = myData.number;
            System.out.println(n);
        }
        System.out.println(Thread.currentThread().getName() + "\t mission is over, main get number value :" + myData.number + "\t n is:" + n);
    }
}
