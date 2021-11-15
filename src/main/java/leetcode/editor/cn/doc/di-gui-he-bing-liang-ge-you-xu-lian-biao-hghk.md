![图解每日一练.jpg](https://pic.leetcode-cn.com/1615817903-fzmpwZ-%E5%9B%BE%E8%A7%A3%E6%AF%8F%E6%97%A5%E4%B8%80%E7%BB%83.jpg)

---

### 🧠 解题思路

根据题意，我们知道我们的任务是要将两个升序链表合并为一个升序的新链表！

这就好比，军训的时候，有两个小组，每一组都是按照身高，从左往右依次站立的。

这时候，教官让我们两个小组，合并为一个小组，并且也要按照身高来站立。

这个时候，是不是感觉题目，有一点温度了，人间，又充满阳光了？

跟着你的感觉，我们来想象一下如何正确的合并成一个小组，流程如下：

1. 首先，我们给小组命名，一组为 *A*，一组为 *B*，新组合的的 *C* 组。
2. 对比 *A* 组和 *B* 组现在站在最前面的人的身高，矮的先出来，站在 *C* 组第一位。
3. 然后再次对比两组开头的人的身高，矮的又站出来，站在 *C* 组第二位。
4. 就这样周而复始，最终，*AB* 两组的人，全部站到了 *C* 组，我们的任务也就完成了。

而我们实现该逻辑的方法，就是：递归！

---

### 🎨 图解演示

 ![1.jpg](https://pic.leetcode-cn.com/1617028213-JmCYyn-1.jpg) ![2.jpg](https://pic.leetcode-cn.com/1617028216-fhhpZH-2.jpg) ![3.jpg](https://pic.leetcode-cn.com/1617028218-MUzRYE-3.jpg) ![4.jpg](https://pic.leetcode-cn.com/1617028221-eVKFnH-4.jpg) ![5.jpg](https://pic.leetcode-cn.com/1617028223-LyoxrM-5.jpg) ![6.jpg](https://pic.leetcode-cn.com/1617028226-LDOQnH-6.jpg) ![7.jpg](https://pic.leetcode-cn.com/1617028229-VIdkaj-7.jpg) ![8.jpg](https://pic.leetcode-cn.com/1617028231-xwKAFI-8.jpg) 

---

### 🍭 示例代码

```Javascript []
var mergeTwoLists = function(l1, l2) {
    if(!l1) return l2;
    if(!l2) return l1;
    if (l1.val < l2.val) {
        l1.next = mergeTwoLists(l1.next, l2);
        return l1;
    } else {
        l2.next = mergeTwoLists(l1, l2.next);
        return l2;
    }
};
```
```Java []
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }

    }
}
```
```Python []
class Solution:
    def mergeTwoLists(self, l1, l2):
        if l1 is None:
            return l2
        elif l2 is None:
            return l1
        elif l1.val < l2.val:
            l1.next = self.mergeTwoLists(l1.next, l2)
            return l1
        else:
            l2.next = self.mergeTwoLists(l1, l2.next)
            return l2
```
```C []
class Solution {
public:
    ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
        if (l1 == nullptr) {
            return l2;
        } else if (l2 == nullptr) {
            return l1;
        } else if (l1->val < l2->val) {
            l1->next = mergeTwoLists(l1->next, l2);
            return l1;
        } else {
            l2->next = mergeTwoLists(l1, l2->next);
            return l2;
        }
    }
};
```

---

### 转身挥手

嘿，少年，做图不易，留下个赞或评论再走吧！谢啦~ 💐

差点忘了，祝你牛年大吉 🐮 ，AC 和 Offer 📑 多多益善~

⛲⛲⛲ 期待下次再见~ 