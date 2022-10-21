package com.bard.testlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给你一个包含 n 个整数的数组 nums，
 * 判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？
 * 请你找出所有和为 0 且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * <p>
 * 示例 1：
 * 输入：nums = [-1,0,1,2,-1,-4]
 * 输出：[[-1,-1,2],[-1,0,1]]
 * <p>
 * 示例 2：
 * 输入：nums = []
 * 输出：[]
 * <p>
 * 示例 3：
 * 输入：nums = [0]
 * 输出：[]
 */
public class LeetCode3 {

    public static void main(String[] args) {

    }


    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int k = 0; k < nums.length - 2; k++) {
            if (nums[k] > 0) break;
            if (k > 0 && nums[k] == nums[k - 1]) continue;
            int i = k + 1, j = nums.length - 1;
            while (i < j) {
                int sum = nums[k] + nums[i] + nums[j];
                if (sum < 0) {
                    while (i < j && nums[i] == nums[++i]) ;
                } else if (sum > 0) {
                    while (i < j && nums[j] == nums[--j]) ;
                } else {
                    res.add(new ArrayList<Integer>(Arrays.asList(nums[k], nums[i], nums[j])));
                    while (i < j && nums[i] == nums[++i]) ;
                    while (i < j && nums[j] == nums[--j]) ;
                }
            }
        }
        return res;
    }

    public static List<List<Integer>> find(int[] array) {
        List<List<Integer>> lists = new ArrayList<>();
        //排序
        Arrays.sort(array);
        //双指针
        int len = array.length;

        for (int i = 0; i < len; ++i) {
            //i是第一个数，因为已经排序，所以当i大于0，三数和不可能为0，循环中断
            if (array[i] > 0) return lists;

            //如果i的后一位的数 与i位相同，则跳过【如果是[0,0,0]呢? -> 特殊情况处理】
            if (i > 0 && array[i] == array[i - 1]) continue;

            int curr = array[i]; //记录当前值
            int L = i + 1, R = len - 1; //记录左下标L从i右开始、右下标数组最右侧开始
            while (L < R) { //保证左指针在左，右指针在右
                int tmp = curr + array[L] + array[R]; //计算3数和
                if (tmp == 0) { //如果为0，成功，记录3数值
                    List<Integer> list = new ArrayList<>();
                    list.add(curr);
                    list.add(array[L]);
                    list.add(array[R]);
                    lists.add(list);
                    while (L < R && array[L + 1] == array[L]) ++L; //查看左指针右移一位 指针所指的数 是否变化，没变则继续右移
                    while (L < R && array[R - 1] == array[R]) --R; //查看右指针左移一位 指针所指的数 是否变化，没变则继续左移
                    ++L; //这次左指针右移一位，前面的判断已经保证数字会变动了
                    --R; //同理，因为i不动，所以不能只懂左下标，右下标也要动，不然不会得到0
                } else if (tmp < 0) { //如果3数和小于0，就左下标右移一位
                    ++L;
                } else { //如果3数和大于0，就右下标左移一位
                    --R;
                }
            }
        }
        return lists;
    }
}
