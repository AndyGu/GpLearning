package com.bard.testlib;

public class LeetCode {


    public static void main(String[] args){
        int[][] array = {{1,  2,  8,  9},
                         {2,  4,  9,  12},
                         {4,  7,  10,  13},
                         {6,  8,  11,  15}};
        System.out.println(find1(array, 7));

        System.out.println(find2(new int[]{ 3, 4, 0, 1, 2}));
    }



    /**
     * 查找特殊二维数组中的数
     * 例
     *  1  2  8  9
     *  2  4  9  12
     *  4  7  10 13
     *  6  8  11 15
     *  从左上不方便，因为这样一来要重置y，也要重置for循环的判断，不如从右上
     */
    private static boolean find1(int[][] array, int target){

        if(array == null || array.length == 0){
            return false;
        }
        int x, y;
//        for(x=0, y=0; x<array.length && y<array[0].length; ){
//            if(array[x][y] == target){
//                return true;
//            }
//            else if(array[x][y] > target){
//                x++;
//                y=0;
//            }
//            else{
//                y++;
//            }
//        }

        for(x=0, y=array[0].length-1; x<array.length && y>=0; ){
            if(array[x][y] == target){
                return true;
            }
            else if(array[x][y] > target){
                y--;
            }
            else{
                x++;
            }
        }
        return false;
    }


    /**
     * 查找旋转数组中的最小数字
     *
     *三种情况：
     *   （1）数组没有发生旋转，即把前面0个元素搬到末尾，也就是排序数组本身，第一个就是最小值
     *      例如：12345 -> 12345
     *   （2）一般情况二分查找，当high-low=1时，high就是最小值
     *      例如：12345 -> 34512
     *   （3）如果首尾元素和中间元素都相等时，只能顺序查找
     *      例如：01111 -> 11101
     *
     *  这一题的分情况比较难
     */
    private static int find2(int[] array){
        int len = array.length;
        int low = 0, high = len-1;

        //如果长度为0，返回-1
        if(len == 0) {
            return -1;
        }

        //如果长度为1，直接返回该值
        if(len == 1) {
            return array[0];
        }

        //第一种情况，数组没有发生旋转，即数组本身,最小值就是第一个元素
        if(array[low] < array[high]) {
            return array[0];
        }


        //第二种情况，因为是排序数列，因此用二分查找，时间复杂度为O(logn)
        while(low < high) {
            int mid = (low + high) / 2 ;
            //当首或尾和中间元素相等时，因为没法比较谁大谁小，因此用不了折半查找，只能用顺序查找
            if(array[low] == array[mid] || array[mid] == array[high]) {
                return orderFind(array);
            }

            //如果左比中小，说明最小出在右边，所以最小值往右缩，左边舍弃
            if(array[low] < array[mid]) {
                low = mid;
            }
            //如果右比中大，说明最小出在左边，所以最大值往左缩，右边舍弃
            else if(array[mid] < array[high]) {
                high = mid;
            }
            //这个判断比较重要，如果high low碰面/相邻，说明左边是最大，右边是最小，取右边的值
            if(high-low == 1) {
                return array[high];
            }
        }
        return -1;
    }

    //顺序查找的方法
    private static int orderFind(int[] array) {
        int min = array[0];
        for(int i=0;i<array.length;i++) {
            if(array[i]<min) {
                min = array[min];
            }
        }
        return min;
    }
}
