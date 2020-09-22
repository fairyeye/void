/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/8/14 17:58
 */
public class TestStringSub {

    public static void main(String[] args) {
//        String lot = "12345672020";
//        String substring = lot.substring(0, 7);
//        System.out.println(substring);

        addPrefix();
    }

    private static void addPrefix() {
        String str = "7836432";

        if (str.length() < 8 ) {
            str = 0 + str;
            System.out.println(str);
        }
    }
}
