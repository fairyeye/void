package easy;

import java.util.Arrays;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/21 15:57
 */
public class _88_Merge {
    public static void main(String[] args) {
        new _88_Merge().merge(new int[]{0}, 0, new int[]{1}, 0);
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {

        int[] nums1_cp = new int[m];

        System.arraycopy(nums1, 0, nums1_cp, 0, m);

        int p1 = 0;
        int p2 = 0;
        int p = 0;

        while (p1 < m && p2 < n) {
            nums1[p++] = nums1_cp[p1] > nums2[p2] ? nums2[p2++] :  nums1_cp[p1++];
        }
        if (p1 < m) {
            System.arraycopy(nums1_cp, p1, nums1, p1 + p2, m + n - p1 - p2);
        }
        if (p2 < n) {
            System.arraycopy(nums2, p2, nums1, p1 + p2, m + n - p1 - p2);
        }
        System.out.println(Arrays.toString(nums1));
    }
}
