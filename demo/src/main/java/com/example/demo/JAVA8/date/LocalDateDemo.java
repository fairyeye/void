package com.example.demo.JAVA8.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/9 13:58
 */
public class LocalDateDemo {


    public static void main(String[] args) {
//        LocalDateTime localDateTime = LocalDateTime.now().plusDays(-1L);
//
//        LocalDateTime dateTime = localDateTime.plusDays(-1L);
//        dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        System.out.println(localDateTime);
//        System.out.println(dateTime);

//        System.out.println(LocalDateTime.now().plusDays(-1L).withNano(0));
//        System.out.println(LocalDateTime.now().plusDays(-1L).truncatedTo(ChronoUnit.SECONDS));


//        LocalDateTime dateTime = LocalDateTime.now().plusHours(-2L).truncatedTo(ChronoUnit.SECONDS);
//        String time = dateTime.toString().replace("T", " ");
//        System.out.println(time);

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println(localDate); // 2020-10-09
        System.out.println(localTime); // 18:29:47.712227100
        System.out.println(localDateTime); // 2020-10-09T18:29:47.712227100

        // 去掉毫秒
        System.out.println(localTime.withNano(0)); // 18:29:47
        System.out.println(localDateTime.withNano(0)); // 2020-10-09T18:29:47
        System.out.println(localTime.truncatedTo(ChronoUnit.SECONDS)); // 18:29:47
        System.out.println(localDateTime.truncatedTo(ChronoUnit.SECONDS)); // 2020-10-09T18:29:47

        // 获取加减一天的日期
        System.out.println(localDateTime.plusDays(1L)); // 2020-10-10T18:33:47.376192400
        System.out.println(localDateTime.plusDays(-1L)); // 2020-10-08T18:33:47.376192400
    }

}
