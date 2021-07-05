package algorithms;

import domain.ListNode;

import java.util.ArrayList;
import java.util.Stack;

public class list {

    /**
     *
     * 剑指offer 24 反转链表
     * https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/
     *
     * 定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。
     *
     *  
     *
     * 示例:
     *
     * 输入: 1->2->3->4->5->NULL
     * 输出: 5->4->3->2->1->NULL
     *
     */
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while(curr!=null){
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        //易错点！！！！！！
        return prev;
    }

    /**
     * 剑指 Offer 06. 从尾到头打印链表
     * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
     *
     * 示例 1：
     *
     * 输入：head = [1,3,2]
     * 输出：[2,3,1]
     *
     */
    //1. 递归：
    //声明一个可被递归函数操作的全局变量
    ArrayList<Integer> temporalResult = new ArrayList<Integer>();
    public int[] recurReversePrint(ListNode head) {
        recurToLast(head);
        int[] result = new int[temporalResult.size()];
        for(int i=0; i<temporalResult.size(); i++){
            result[i] = temporalResult.get(i);
        }
        return result;

    }
    void recurToLast(ListNode temp){
        if(temp==null) return;
        recurToLast(temp.next);
        temporalResult.add(temp.val);
    }

    //2. 辅助栈法:
    public int[] reversePrintByStack(ListNode head) {
        Stack<Integer> supStack = new Stack<Integer>();
        while (head != null) {
            supStack.push(head.val);
            head = head.next;
        }
        int[] result = new int[supStack.size()];
        //这里不能写i<supStack.size()，因为supStack.size()会随着pop 减小
        //要用定值
        for (int i = 0; i < result.length; i++) {
            result[i] = supStack.pop();
        }
        return result;
    }

    /**
     * 剑指25 合并两个排序链表
     * https://leetcode-cn.com/problems/he-bing-liang-ge-pai-xu-de-lian-biao-lcof
     * 输入两个递增排序的链表，合并这两个链表并使新链表中的节点仍然是递增排序的。
     *
     * 示例1：
     *
     * 输入：1->2->4, 1->3->4
     * 输出：1->1->2->3->4->4
     *
     */
    //1. 迭代iteration
    public ListNode mergeTwoListsByIteration(ListNode l1, ListNode l2) {
        ListNode fakeHead = new ListNode(-999);
        ListNode curr = fakeHead;
        while(l1!=null && l2!=null){
            if(l1.val > l2.val){
                curr.next = l2;
                l2 = l2.next;
            } else{
                curr.next = l1;
                l1 = l1.next;
            }
            curr = curr.next;
        }

        //l1为null，即l2不为null
        if(l1 == null){
            curr.next = l2;
        }
        //反之亦然
        if(l2 == null){
            curr.next = l1;
        }

        return fakeHead.next;
    }

    //2. 递归
    //规律：递归寻找当前val小的list的next
    //出口：其中一个list被遍历完，直接return剩下的那个list
    public ListNode mergeTwoListsByRecursive(ListNode l1, ListNode l2) {
        if(l1==null) return l2;
        if(l2==null) return l1;

        if(l1.val < l2.val){
            l1.next = mergeTwoListsByIteration(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoListsByIteration(l1, l2.next);
            return l2;
        }
    }

}
