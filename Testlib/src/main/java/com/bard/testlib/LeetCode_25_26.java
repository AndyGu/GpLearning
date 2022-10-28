package com.bard.testlib;

import java.util.ArrayList;
import java.util.List;


public class LeetCode_25_26 {


    public static void main(String[] args){
        Node node = new Node(1);
        node.next = new Node(2);
        node.next.next = new Node(3);
        node.next.next.next = new Node(4);
        List<Integer> list = printNode(node);
        for (Integer i: list) {
            System.out.println("i="+i);
        }


        System.out.println(findKNode(node, 2));
    }


    /**
     * 从尾到头打印链表
     * 输入一个链表，按链表值从尾到头的顺序返回一个ArrayList。
     */
    public static List<Integer> printNode(Node node){
        List<Integer> list = new ArrayList<>();
        if(node == null){
            return list;
        }

        while(node != null){
            list.add(0, node.value);
            node = node.next;
        }
        return list;
    }


    /**
     * 找到链表中倒数第K个节点
     *
     * 思路：双指针，指针中间保持k步距离，第一个指针指到null了，第二个指针就到了
     *
     * k=3
     * 1,   2,   3,   4,   5,   6,   7,   8
     * fast
     * slow
     *
     *      fast
     * slow
     *
     *           fast
     * slow
     *
     *               fast
     *      slow
     *                                      fast
     *                         slow
     */
    public static Node findKNode(Node node, int k){
        Node fast = node;
        Node slow = node;
        int i = 0;
        while(fast != null){
            i++;
            fast = fast.next;

            if(i > 2){
                slow = slow.next;
            }
        }

        if(i < 2){
            return null;
        }else{
            return slow;
        }
    }

    static class Node {
        public Node(int value) {
            this.value = value;
        }

        int value;
        Node next;

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
}
