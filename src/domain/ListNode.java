package domain;

public class ListNode {
    public ListNode next;
    public int val;

    public ListNode(int x) {
        val = x;
    }

    public int getVal(){
        return this.val;
    }

    private static void printList(ListNode head){
        StringBuilder sb = new StringBuilder();
        while(head!=null){
            sb.append(head.val+"");
            sb.append(",");
            head = head.next;
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
    }
}
