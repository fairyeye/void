import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/9/11 10:54
 */
public class IfElesIf {
    public static void main(String[] args) throws ParseException {
//        String a = "";
//        String b = "";
//        if (a.equals("")) {
//            System.out.println("a is null");
//        } else if (b.equals("")) {
//            System.out.println("b is null");
//        }
//        String a = "Deadlock found when trying to get lock; try restarting transaction";
//        if (a.toUpperCase().contains("DEADLOCK")) {
//            System.out.println(123);
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date parse = simpleDateFormat.parse("20201001");
    }
}
