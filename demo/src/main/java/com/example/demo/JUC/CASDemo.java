package com.example.demo.JUC;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/18 17:13
 * @description
 *
 *
 * 1. CAS是什么? ==> comparAndSwap
 *      比较并交换
 * 2. CAS底层原理 UnSafe的理解
 *      1. 自旋锁
 *      2. UnSafe类
 * 3. CAS缺点
 */
public class CASDemo {

    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(5);
        atomicInteger.getAndIncrement();

        /**
         * @param o atomicInteger
         * @param offset 内存偏移量
         * @param delta 要增加的值
         *
         * public final int getAndAddInt(Object o, long offset, int delta) {
         *         int v;
         *         do {
         *             // 获取对象内存偏移量位置的值 volatile 修饰的值
         *             v = this.getIntVolatile(o, offset);
         *         } while(!this.weakCompareAndSetInt(o, offset, v, v + delta));
         *
         *         return v;
         *     }
         */
    }

    private static void CASFirst() {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        // do something ...

        // true	 current data value: 0  获得期望值 将expectValue更新为newValue
        System.out.println(atomicInteger.compareAndSet(5, 0) + "\t current data value: " + atomicInteger.get());

        // false	 current data value: 0  没有获得期望值
        System.out.println(atomicInteger.compareAndSet(5, 10) + "\t current data value: " + atomicInteger.get());
    }
}
