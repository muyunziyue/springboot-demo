### 解题思路
我感觉我的代码比官方那个答案要舒服一些
因为其实flag, l1, l2都可以列入循环判断中
以链表1为基准，将链表2的对应值加到链表1上，再加上进位值flag
l1空了且有进位就新增结点，只有当进位flag为0，l1，l2都为空时才停止循环

### 代码

```c
/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     struct ListNode *next;
 * };
 */


struct ListNode* addTwoNumbers(struct ListNode* l1, struct ListNode* l2){
    // 两数相加
	int flag = 0;
	struct ListNode* head = l1;
	struct ListNode* p = (struct ListNode*)malloc(sizeof(struct ListNode));
	p->next = l1;
	
	while(flag || l1 || l2){
		if(l1 != NULL){
			l1->val += flag;
		}
		else{
			l1 = (struct ListNode*)malloc(sizeof(struct ListNode));
			l1->val = flag;
			l1->next = NULL;
		}
		
		if(l2 != NULL){
			l1->val += l2->val;
			l2 = l2->next;
		}
		
		flag = l1->val / 10;
		l1->val %= 10;
		
		p->next = l1;
		p = p->next;
		l1 = l1->next;
	}
	
	return head;
}
```