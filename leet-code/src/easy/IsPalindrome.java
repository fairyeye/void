package easy;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/14 13:27
 */
public class IsPalindrome {
    public static void main(String[] args) {
        System.out.println(isPalindrome(10));
    }
    public static boolean isPalindrome(int x) {

        if (x == 0) return true;
        if (x < 0) return false;

        int target = 0;
        int ori = x;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;

            target = target * 10 + pop;
        }
        if (target != ori) return false;

        return true;
    }
}
