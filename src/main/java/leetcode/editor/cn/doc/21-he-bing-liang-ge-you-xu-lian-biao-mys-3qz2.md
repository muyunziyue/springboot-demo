### 解题思路
此处撰写解题思路

### 代码

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(0);
        ListNode first = head;
        while (l1!=null && l2!=null){
            if(l1.val > l2.val){
                first.next = l2;
                first = first.next;
                l2 = l2.next;
            }else {
                first.next = l1;
                first = first.next;
                l1 = l1.next;
            }
        }
        if(l1 != null){
            first.next = l1;
        }
        if(l2 != null){
            first.next = l2;
        }
        return head.next;
    }
}
```