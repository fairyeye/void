//给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
//
// 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
//
//
//
// 示例:
//
// 给定 nums = [2, 7, 11, 15], target = 9
//
//因为 nums[0] + nums[1] = 2 + 7 = 9
//所以返回 [0, 1]
//
// Related Topics 数组 哈希表

//        测试用例:[2,7,11,15,22,24]
//        13
//        测试结果:[0,2]
//        期望结果:[0,2]
package easy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/12 10:56
 */
public class TwoSum {
    public static void main(String[] args) {
        int[] ints = TwoSum.twoSum(new int[]{2, 7, 11, 15}, 26);
        System.out.println(Arrays.toString(ints));
    }

    public static int[] twoSum(int[] nums, int target) {
        return secondMethod(nums, target);
    }

    /**
     * 
     * @param nums
     * @param target
     * @return
     */
    private static int[] secondMethod(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.get(target - nums[i]) != null) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * 暴力解法 循环
     *
     * @param nums
     * @param target
     * @return
     */
    private static int[] firstMethod(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}
