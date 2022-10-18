package com.bard.testlib;

import java.util.Random;

public class LeetCode2 {

    static Node chain1;
    static Node chain2;

    static class Node {
        int val;
        Node next;

        public Node() {
        }

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }


    private static void init() {
        Random random = new Random();
        chain1 = new Node(random.nextInt(30));
        chain2 = new Node(random.nextInt(30));

        Node temp1 = chain1;
        Node temp2 = chain2;
        for (int i = 0; i < 5; i++) {
            Node node1 = new Node(random.nextInt(30));
            temp1.next = node1;
            temp1 = node1;

            Node node2 = new Node(random.nextInt(30));
            temp2.next = node2;
            temp2 = node2;
        }
    }


    private static void show(Node chain1, Node chain2) {
        Node p = chain1;
        Node q = chain2;
        while (p != null) {
            System.out.print(p.val + " ");
            p = p.next;
        }
        System.out.println();

        while (q != null) {
            System.out.print(q.val + " ");
            q = q.next;
        }
        System.out.println();
    }



    private static Node merge_sort(Node head) {
//        System.out.println("merge_sort");
        if (head == null || head.next == null) return head;
        Node fast = head; //头结点 赋给 fast
        Node slow = new Node(0, head); //在头结点前 新建一个 慢节点，并连接

        // 1 2
        while (fast != null && fast.next != null){
            fast = fast.next.next; //快节点 两步往前走
            slow = slow.next; //慢节点 一步往前走
        }
        // 经历过这个while循环
        // 如果链表偶数项，fast为null，slow为中间线-1
        // 【确实是线，不是项，因为有偶数项，准确说中间项有俩，或者说只有中间线，再或者说是前一半的最后一项】
        // 如果链表奇数项，fast为最后项，slow为中间项-1

        //slow所在的位置是右边头节点的前一个
        Node right_head = slow.next;
        slow.next = null; //断开连接，这样就变成了两个链表，right_head引领右半边链表，head引领左半边链表
        Node left_node = merge_sort(head);
        Node right_node = merge_sort(right_head);
        Node merge = merge(left_node, right_node); //当递归到一个链表只有一个元素的时候，它就是有序的，就可以调用merge算法了
        return merge;
    }



    public static Node sortList(Node head) {
        if(head == null || head.next == null) return head;
        Node slow = head, fast = head.next;
        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        Node tmp = slow.next;
        slow.next = null;
        Node left = sortList(head);
        Node right = sortList(tmp);

        Node dum = new Node(0, head);
        Node res = dum;
        while(left != null && right != null){
            if(left.val > right.val){
                dum.next = right;
                right = right.next;
            }else{
                dum.next = left;
                left = left.next;
            }
            dum = dum.next;
        }
        dum.next = left != null ? left : right;
        return res.next;
    }
    private static Node merge(Node chain1, Node chain2) {
//        System.out.println("merge");
        //合并两个有序链表
        Node dum_node = new Node();
        Node first_node = dum_node;
        Node p = chain1;
        Node q = chain2;
        while (p != null && q != null) {
            // 1.把p赋给新节点的下一个 2.p指针后移
            if (p.val <= q.val) {
                dum_node.next = p;
                p = p.next;
            } else {
                // 1.把q赋给新节点的下一个 2.q指针后移
                dum_node.next = q;
                q = q.next;
            }
            //把新节点的指针向后移，使dum_node始终指向最后一个Node，应对下个循环添加新Node
            dum_node = dum_node.next;
        }
        dum_node.next = p != null ? p : q;
        return first_node.next;
    }


    /**
     * 时间复杂度O(nlogn)
     */
    public static void main(String[] args) {
        init();
        show(chain1, chain2);
        chain1 = merge_sort(chain1);
        chain2 = merge_sort(chain2);
        show(chain1, chain2);
        show(merge(chain1, chain2), null);
    }


}
