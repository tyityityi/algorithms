# Table of Contents

------

## 算法

要求我们的算法复杂度是 `O(NlogN)`，你想想怎么才能搞出一个对数级别的复杂度呢？

肯定得用到 [二分搜索]() 或者<u>二叉树</u>相关的数据结构，比如 `TreeMap`，`PriorityQueue` 之类的对吧。

再比如，有时候题目要求你的算法时间复杂度是 `O(MN)`，这可以联想到什么？

可以大胆猜测，常规解法是用 [回溯算法]() 暴力穷举，但是更好的解法是动态规划，而且是一个二维动态规划，需要一个 `M * N` 的二维 `dp` 数组，所以产生了这样一个时间复杂度。

##### [Sorting 排序](./排序.md)

##### [Binary Tree 二叉树算法](./二叉树.md) 

##### [Binary Search Tree 二叉搜索树](./二叉搜索树.md)

##### [DFS 回溯算法](./回溯算法.md)

##### [Binary Search二分查找](./二分查找.md)

##### [双指针](./双指针.md)

子串匹配问题：子串一定是连续的

##### [Dynamic Programming 动态规划](./动态规划.md)

子序列问题：子序列不一定是连续的

[链表](./链表.md)

## 计算机基础

##### [Data Structure 数据结构面试/笔试题](./数据结构面试笔试题.md)

##### [计算机网络](./计算机网络.md)

[操作系统](./操作系统.md)

## 数据库

[MySQL](./数据库(mysql).md)

## Java

[基础](./Java基础)



With Respect to

labuladong的算法小抄：https://labuladong.gitbook.io/algo/

CS-Notes：http://www.cyc2018.xyz/

## Others

1. 单引号代表char 双引号代表String！

2. 字符的ASCII可以用String.charAt()或者String.codePointAt()来计算；

3. Java的comparator中

    ```java
    @Override
     public int compare(A o1, A o2) {
         //升序
         //return o1.a - o2.a;
         //降序
         return o2.a - o1.a;
     }
    ```

    这里o1表示位于前面的对象，o2表示后面的对象

    - 返回-1（或负数），表示不需要交换01和02的位置，o1排在o2前面，asc
    - 返回1（或正数），表示需要交换01和02的位置，o1排在o2后面，desc

    为什么`return o2.a - o1.a;`就是降序了：

    首先o2是第二个元素，o1是第一个元素。无非就以下这些情况：
    ①： **`o2.a > o1.a`** : 那么此时**返回正数**，表示需要调整o1,o2的顺序，也就是需要把o2放到o1前面，这不就是降序了么。

    ②：**`o2.a < o1.a`** : 那么此时返回负数，表示不需要调整，也就是此时o1 比 o2大， 不还是降序么。

4. 当然，也可以用这种方法来**升序**，可以避免comparator中Integer边界值相减出bug的问题

    ```java
    //升序的情况下p1<p2（前面的小于后面的），但是这里！！！返回-1！！！
    Arrays.sort(points, (p1, p2) -> p1[1] < p2[1] ? -1 : 1);
    
    ```

    
