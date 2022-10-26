package com.bard.testlib;

public class LeetCode_1_2 {


    public static void main(String[] args){
        int[][] array = {{1,  2,  8,  9},
                         {2,  4,  9,  12},
                         {4,  7,  10,  13},
                         {6,  8,  11,  15}};
        System.out.println(find1(array, 7));

        System.out.println(find2(new int[]{ 3, 4, 0, 1, 2}));

        System.out.println("~5="+(~5));
    }

    /**
     * 查找在特殊二维数组中，是否含有target
     *
     * 1. 每个一维数组的长度相同
     * 2. 数组每一行都从左到右递增
     * 3. 数组每一列都从上到下递增
     *
     * 例
     *  1  2  8  9
     *  2  4  9  12
     *  4  7  10 13
     *  6  8  11 15
     *  从左上不方便，因为这样一来，array[0][0]右边和下面的值都比它要大，当array[0][0] < target的时候要做的判断有点多
     *  不如从右上，array[0][array[0].length - 1]，比target小就下移，比target大就左移
     */
    private static boolean find1(int[][] array, int target){
        //array.length 是array的行数
        //array[0].length 是array的列数
        if(array == null || array.length == 0){
            return false;
        }
        int x, y;
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
     * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
     * 输入一个非减排序的数组的一个旋转，输出旋转数组的最小元素。
     * 例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，该数组的最小值为1。
     * NOTE：给出的所有元素都大于0，若数组大小为0，请返回-1。
     *
     *三种情况：
     *   （1）数组没有发生旋转，即把前面0个元素搬到末尾，也就是排序数组本身，第一个就是最小值
     *      例如：12345 -> 12345
     *   （2）一般情况二分查找，当high-low=1时，high就是最小值
     *      例如：12345 -> 34512
     *   （3）如果首尾元素和中间元素都相等时，只能查找，也并到第二种情况中
     *      例如：12222 -> 22212
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

        //第二种情况，因为是排序数列？，因此用二分查找，时间复杂度为O(logn)
        while(low < high) {
            int mid = (low + high) / 2 ;
            //当首或尾和中间元素相等时，因为没法比较谁大谁小，因此用不了折半查找，只能用顺序查找
            if(array[low] == array[mid] || array[mid] == array[high]) {
                return orderFind(array);
            }

            //如果左比中小，说明最小出在右边！！！所以最小值往右缩，左边舍弃
            if(array[low] < array[mid]) {
                low = mid;
            }
            //如果右比中大，说明最小出在左边！！！所以最大值往左缩，右边舍弃
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
