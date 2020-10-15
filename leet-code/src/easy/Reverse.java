package easy;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/13 16:14
 *
 *
 * 整数反转
 */
public class Reverse {
    public static void main(String[] args) {
        System.out.println(reverseTwo(1777777777));
    }

    private static int reverseTwo(int x) {

        int target = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;

            // Integer.MAX_VALUE = 2147483647 if target == 214748364 and last > 7 out
            if (target > Integer.MAX_VALUE / 10 || (target == Integer.MAX_VALUE / 10 && pop > 7)) return 0;
            if (target < Integer.MIN_VALUE / 10 || (target == Integer.MIN_VALUE / 10 && pop < -8)) return 0;

            target = target * 10 + pop;
        }
        return target;
    }

    /**
     * 暴力解法
     *
     * @param x
     * @return
     */
    public static int reverseOne(int x) {
        String str = x + "";
        String head = "";
        String result = "";
        if (str.startsWith("-")) {
            str = str.replace("-", "");
            head = "-";
        }
        for (int i = 0; i < str.length(); i++) {
            result = str.toCharArray()[i] + result;
        }
        result = head + result;
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {
            return 0;
        }
    }
}
