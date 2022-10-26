package com.bard.testlib;

/**
 * 判断一个数组中是否 连续的 包含另一个数组
 */
public class LeetCode_find_array_in_array {
    public static void main(String[] args) {
        System.out.println(find(new int[]{1, 3, 9, 4, 5}, new int[]{3, 4}));
    }


    public static boolean find(int[] a, int[] b) {
        boolean flag = false;
        int index = -1; //如果有a中有b，记录从下标index开始相同的
        if (a.length > b.length) { //假定 a数组是大数组，另一种情况没有写
            for (int i = 0, j = 0; i <= a.length - b.length && j < b.length; ) {

                if (a[i] == b[j]) {
                    if (j==0) { //说明这个相同值是b第一个与a相同的值
                        index = i;
                    }

                    flag = true;
                    i++;
                    j++;
                } else {
                    if (flag) {
                        flag = false;
                        j = 0;
                    } else {
//                        flag = false;
                        i++;
                    }
                }
            }
        }

        System.out.println("index=" + index);
        return flag;
    }


    public static boolean find2(int[] a, int[] b) {
        boolean flag = false;
        if (a.length > b.length) {
            for (int i = 0, j = 0; i < a.length && j < b.length; ) {
                if (a[i] == b[j]) {
                    flag = true;
                    i++;
                    j++;
                } else {
                    if (flag) {
                        flag = false;
                        j = 0;
                        continue;
                    } else {
                        flag = false;
                        i++;
                    }
                }
            }
        }
        return flag;
    }
}
