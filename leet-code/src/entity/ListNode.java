package entity;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/13 13:29
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
