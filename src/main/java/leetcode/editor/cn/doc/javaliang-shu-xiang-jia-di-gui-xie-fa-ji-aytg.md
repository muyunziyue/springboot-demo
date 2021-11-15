首先，为什么可以用递归？
如示例1所示，2 和 5 相加后，4 6 的相加方法其实可以和2 5 相加一样，只是需要考虑进位，
那好办啊！我们将进位单独记录出来，这样递归就没难度了

要考虑两点：
1.递归函数交付给上层什么？
  没错，交付的是两节点相加后的值（sum%10）
2.递归函数的终止条件是什么？ 
  当指针都为Null 且 进位carry为0时 return null

代码及其注释如下：（如果没看懂，可以直接评论，leetcode是我家！）
```
class Solution {
    int carry = 0;//记录进位
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //定义终止条件，当l1,l2指针都为null时且进位为0 ->null
        if(l1==null&&l2==null&&carry == 0) return null;

        //当有一条链表为null 且 进位为0时，next指针直接指向另外一条链表返回
        if(l1!=null&&l2==null&&carry==0) return l1;
        else if(l1==null&&l2!=null&&carry==0) return l2;

        //sum = 两链表指针位置上的数字加上进位
        int sum = (l1==null?0:l1.val)+(l2==null?0:l2.val)+carry;
        //计算进位
        carry = sum/10;
        //计算链表的value
        int value = sum % 10;
        ListNode node = new ListNode(value);

        //递归算出这个node的next指向
        node.next = addTwoNumbers((l1==null?null:l1.next),(l2==null?null:l2.next));

        return node;
    }
}
```

