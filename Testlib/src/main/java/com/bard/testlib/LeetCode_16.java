package com.bard.testlib;

/**
 * 把空格替换成其他字符串（替换空格）
 *
 * 请实现一个函数，将一个字符串中的每个空格替换成“%20”。例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
 *
 * 我们首先想到原来的一个空格替换为三个字符，字符串长度会增加，
 * 因此，存在以下两种不同的情况：（1）允许创建新的字符串来完成替换。（2）不允许创建新的字符串，在原地完成替换。
 *
 * 时间复杂度为O(n^2)的解法：
 *      从头到尾遍历字符串，当遇到空格时，后面所有的字符都后移2个。
 *
 * 时间复杂度为O(n)的解法：
 *      可以先遍历一次字符串，这样可以统计出字符串中空格的总数，由此计算出替换之后字符串的长度，
 *      每替换一个空格，长度增加2，即替换之后的字符串长度为原来的长度+2*空格数目。
 *      接下来从字符串的尾部开始复制和替换，
 *      用两个指针P1和P2分别指向原始字符串和新字符串的末尾，然后向前移动P1，
 *      若指向的不是空格，则将其复制到P2位置，P2向前一步；
 *      若P1指向的是空格，则P1向前一步，P2之前插入%20，P2向前三步。
 *      这样，便可以完成替换，时间复杂度为O(n)
 *
 */
public class LeetCode_16 {

    public static void main(String[] args){
        System.out.println(replaceSpace1(new StringBuffer("we are family ")));
    }


    public static String replaceSpace(String s) {
        StringBuilder res = new StringBuilder();
        for(Character c : s.toCharArray())
        {
            if(c == ' ') res.append("%20");
            else res.append(c);
        }
        return res.toString();
    }


    // 方法二 :
    public static String replaceSpace1(StringBuffer str) {
        int spaceCount = 0; //spacenum为计算空格数
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ')
                spaceCount++;
        }
        int indexOld = str.length() - 1; //indexOld 为替换前的str尾部下标
        int newLength = str.length() + spaceCount * 2; //计算空格转换成 %20 之后的str长度
        int indexNew = newLength - 1; //indexOld 为把空格替换为 %20 后的str尾部下标
        str.setLength(newLength); //使 str的长度扩大到 替换成 %20之后的长度，防止下标越界

        //倒序的循环str【此时str长度已经是替换后的长度，但是内容没有被替换】
        //              indexOld indexNew
        //                 ←|           |
        // _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        for (; indexOld >= 0 && indexOld < newLength; --indexOld) {
            if (str.charAt(indexOld) == ' ') {
                str.setCharAt(indexNew--, '0');
                str.setCharAt(indexNew--, '2');
                str.setCharAt(indexNew--, '%');
            } else {
                str.setCharAt(indexNew--, str.charAt(indexOld));
            }
        }
        return str.toString();
    }
}
