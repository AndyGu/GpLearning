package com.bard.testlib;

/**
 *
 */
public class LeetCode_sparsearray {

    public static void main(String[] args){
        System.out.println((~0)+" - "+ (1>>>1) + " - "+
                (binarySearch(new int[]{2,4,6,8,10}, 5, 12)));
    }


    /**
     * SparseArray的put算法
     */
//    public void put(int key, E value) {
//        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
//
//        //如果i>=0，说明找到了key的位置，说明mKeys里的i位置就是key，所以mValue的i位置更新一下就可以
//        if (i >= 0) {
//            mValues[i] = value;
//        } else {
//
//            //没在mKeys里找到key，就把lo取反之后返回，这样保证了
//            // 1. 返回的值必为负数，便于外层判断
//            // 2. 对返回的值再次取反，能得到lo，而这个lo就是二分法找出的、value应该插入的位置
//
//            i = ~i;
//
//            if (i < mSize && mValues[i] == DELETED) {
//                mKeys[i] = key;
//                mValues[i] = value;
//                return;
//            }
//
//            if (mGarbage && mSize >= mKeys.length) {
//                gc();
//
//                // Search again because indices may have changed.
//                i = ~ContainerHelpers.binarySearch(mKeys, mSize, key);
//            }
//
//            mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, key);
//            mValues = GrowingArrayUtils.insert(mValues, mSize, i, value);
//            mSize++;
//        }
//    }



    static int binarySearch(int[] array, int size, int value) {
        int lo = 0;
        int hi = size - 1;

        while (lo <= hi) {
            final int mid = (lo + hi) >>> 1; //除以2
            final int midVal = array[mid];

            System.out.println("binarySearch lo="+lo+ " hi="+hi + " mid="+mid+" midVal="+midVal);

            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;  // value found
            }
        }

        return ~lo;  // value not present
    }


}
