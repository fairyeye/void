package easy;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/21 14:39
 */
public class _136_SingleNumber {
    public static void main(String[] args) {
        System.out.println(new _136_SingleNumber().singleNumber(new int[]{4, 1, 2, 1, 2}));
    }

    public int singleNumber(int[] nums) {
        int temp = 0;
        for (int num : nums) {
            temp ^= num;
        }
        return temp;
    }
}
