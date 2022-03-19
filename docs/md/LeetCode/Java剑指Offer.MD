

# 第一天

## [剑指 Offer 09 用两个栈实现队列](https://leetcode-cn.com/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof/)



```java
   //leetcode submit region begin(Prohibit modification and deletion)
class CQueue {
    Deque<Integer> stack1;
    Deque<Integer> stack2;
    public CQueue() {
        stack1 = new LinkedList<Integer>();
        stack2 = new LinkedList<Integer>();

    }
    
    public void appendTail(int value) {
        stack1.push( value );

    }
    
    public int deleteHead() {
        //使用stack2 为空，则把栈1数据全部移入栈二，
        if (stack2.isEmpty()){
            while (!stack1.isEmpty()){
                stack2.push( stack1.pop() );
            }
        }
        //如果栈二数据依然为空，返回-1.
        if(stack2.isEmpty()){
            return -1;
        }
        //返回栈顶元素
        return stack2.pop();
    }
}
```

## [剑指 Offer 30 包含min函数的栈](https://leetcode-cn.com/problems/bao-han-minhan-shu-de-zhan-lcof/)

```java
class MinStack {
    Stack<Integer> stackA,stackB;
    /** initialize your data structure here. */
    public MinStack() {
        //数据栈
        stackA = new Stack<>();
        //辅助栈
        stackB = new Stack<>();
    }
    
    public void push(int x) {
        stackA.push( x );
        //
        if(stackB.isEmpty() || stackB.peek() >= x){
            stackB.push( x );
        }
    }
    
    public void pop() {
        //如果数据栈顶元素刚好是最小元素，要把辅助栈的最小元素弹出
        if(stackA.pop().equals( stackB.peek() )){
            stackB.pop();
        }
    }
    
    public int top() {
        return stackA.peek();
    }
    
    public int min() {
        return stackB.peek();
    }
}
```



# 第二天

## [剑指 Offer 06. 从尾到头打印链表](https://leetcode-cn.com/problems/cong-wei-dao-tou-da-yin-lian-biao-lcof/)

```java
 
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */

class Solution {
    public int[] reversePrint(ListNode head) {
        Stack<ListNode> stack = new Stack<>();
        ListNode temp = head;

        while (temp != null){
            stack.push( temp );
            temp = temp.next;
        }


        int size = stack.size();
        int[] printArr = new int[size];
        for (int i = 0; i < size; i++) {

            printArr[i] = stack.pop().val;
        }
        return printArr;
    }
}

```



## [剑指 Offer 24. 反转链表](https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/)

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        //先准备一个前节点
        ListNode pre = null;

        //当前指针指向当前节点
        ListNode curr = head;

        while (curr != null){
            //创建next节点
            ListNode next = curr.next;

            //curr节点指向前一个节点
            curr.next = pre;

            //节点交换，前一个节点存储当前节点
            pre = curr;
            //指针移动到下一个节点
            curr = next;


        }
        return pre;

    }
}
```



## [剑指 Offer 35. 复杂链表的复制](https://leetcode-cn.com/problems/fu-za-lian-biao-de-fu-zhi-lcof/)

```java
class Solution { //HashMap实现
    public Node copyRandomList(Node head) {
        HashMap<Node,Node> map = new HashMap<>(); //创建HashMap集合
        Node cur=head;
        //复制结点值
        while(cur!=null){
            //存储put:<key,value1>
            map.put(cur,new Node(cur.val)); //顺序遍历，存储老结点和新结点(先存储新创建的结点值)
            cur=cur.next;
        }
        //复制结点指向
        cur = head;
        while(cur!=null){
            //得到get:<key>.value2,3
            map.get(cur).next = map.get(cur.next); //新结点next指向同旧结点的next指向
            map.get(cur).random = map.get(cur.random); //新结点random指向同旧结点的random指向
            cur = cur.next;
        }

        //返回复制的链表
        return map.get(head);


    }
}
```

参考博客https://blog.csdn.net/weixin_46443403/article/details/123054403?spm=1001.2014.3001.5502

# 第三天

## [剑指 Offer 05. 替换空格](https://leetcode-cn.com/problems/ti-huan-kong-ge-lcof/)

方法一

```java
class Solution {
    public String replaceSpace(String s) {
        StringBuilder stringBuilder = new StringBuilder();	
		String replace = s.replace( " ", "%20" );
        return replace;

    }
}
```

方法二

```java
class Solution {
    public String replaceSpace(String s) {
      StringBuilder stringBuilder = new StringBuilder();
      
      for (int i = 0; i < s.length() ; i++) {
         if(s.charAt(i)== ' '){
            stringBuilder.append("%20");
         }else {
            stringBuilder.append(s.charAt(i));
         }
      }
      return stringBuilder.toString();
   }
}
```

## [剑指 Offer 58 - II. 左旋转字符串](https://leetcode-cn.com/problems/zuo-xuan-zhuan-zi-fu-chuan-lcof/)

方法一

```java
class Solution {
    public String reverseLeftWords(String s, int n) {
        return s.substring( n,s.length() ) + s.substring( 0, n );
}
```

方法二

```java
class Solution {
    public String reverseLeftWords(String s, int n) {
        StringBuilder res = new StringBuilder();
        for (int i = n; i < s.length()  ; i++) {
            res.append( s.charAt( i ) );
        }

        for (int i = 0; i < n; i++) {
            res.append( s.charAt( i ) );
        }
        return res.toString();
    }
}
```

# 第4天

## [剑指 Offer 53 - I. 在排序数组中查找数字 I](https://leetcode-cn.com/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/)

方法一 暴力算法

时间复杂度O(1)

```java
class Solution {
    public int search(int[] nums, int target) {
        int count = 0;
        for(int i = 0; i < nums.length; i++){
            if(nums[i] == target){
                count++;
            }
        }
        return count;
    }
}
```

