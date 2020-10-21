package easy;

import java.util.Arrays;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/21 15:19
 */
public class _169_MajorityElement {
    public static void main(String[] args) {
        System.out.println(new _169_MajorityElement().majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}));
        System.out.println(new _169_MajorityElement().sort(new int[]{2, 2, 1, 1, 1, 2, 2}));
    }

    /**
     * 摩尔投票法
     * @param nums
     * @return
     */
    public int majorityElement(int[] nums) {
        int temp = 0, count = 0;
        for (int num : nums) {
            if (count == 0) {
                temp = num;
            }
            if (num == temp) {
                count++;
            } else {
                count--;
            }
        }
        return temp;
    }

    public int sort(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length/2];
    }
}
