I hope I could code for fun, unfortunately I code to make a living -:) 

# Table of Contents

------

## 算法

要求我们的算法复杂度是 `O(NlogN)`，你想想怎么才能搞出一个对数级别的复杂度呢？

肯定得用到 [二分搜索]() 或者<u>二叉树</u>相关的数据结构，比如 `TreeMap`，`PriorityQueue` 之类的对吧。

再比如，有时候题目要求你的算法时间复杂度是 `O(MN)`，这可以联想到什么？

可以大胆猜测，常规解法是用 [回溯算法]() 暴力穷举，但是更好的解法是动态规划，而且是一个二维动态规划，需要一个 `M * N` 的二维 `dp` 数组，所以产生了这样一个时间复杂度。

### [排序&区间问题](./算法/排序&区间问题.md)

### [Binary Tree 二叉树算法](./算法/二叉树.md) 

### [Binary Search Tree 二叉搜索树](./二叉搜索树.md)

### [DFS 搜索回溯算法](./算法/DFS搜索回溯算法.md)

### [BFS 广度优先算法](./算法/BFS广度优先算法.md)

### [Binary Search二分查找](./算法/二分查找.md)

### [双指针](./算法/双指针.md)

子串匹配问题：子串一定是连续的

### [Dynamic Programming 动态规划](./算法/动态规划.md)

子序列问题：子序列不一定是连续的

### [分治](./算法/分治.md)

### [链表](./算法/链表.md)

### [数组](./算法/数组.md)

### [栈、队列、堆](./算法/栈队列堆.md)

### [缓存淘汰算法(LRU、LFU)](./算法/缓存淘汰算法.md)

### [位运算](./算法/位运算.md)

### [笔试题汇总](./算法/笔试题.md)

### [智力题场景题](./算法/智力题场景题.md)

## 计算机基础

### [Data Structure 数据结构面试/笔试题](./数据结构面试笔试题.md)

### [计算机网络](./计算机基础/计算机网络.md)

### [操作系统](./计算机基础/操作系统.md)



## 数据库

### [MySQL](./数据库/数据库(mysql).md)

### [Redis](./数据库./Redis.md)

## Java

### [基础](./Java/Java基础.md)

### [容器](./Java/Java容器.md)

### [并发](./Java/Java并发.md)

### [JVM](./Java/JVM.md)

### [Spring](./Java/Spring.md)

## 设计模式

代理模式！





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
    //pq降序
    PriorityQueue<int[]> maxHeap = new PriorityQueue<int[]>((o1,o2)->o2[1]-o1[1]);
    
    ```
    
    
    
5. **这里补充一点比较重要，但是容易被忽视掉的知识点：**

    - java 中的 `length`属性是针对数组说的,比如说你声明了一个数组,想知道这个数组的长度则用到了 length 这个属性.
    - java 中的 `length()` 方法是针对字符串说的,如果想看这个字符串的长度则用到 `length()` 这个方法.
    - java 中的 `size()` 方法是针对泛型集合说的,如果想看这个泛型有多少个元素,就调用此方法来查看!

6. 获取一个int的个位  十位 百位 千位

    ```java
    
        public static void takePlace(){
            int intNum = 2108;
            int thousand = intNum/1000;//千
            int hundred = intNum/100%10;//百
            int ten = intNum/10%10;//十
            int a= intNum%10;//个
        }
    ```

    

7. List转Array

    ```java
    //方法1,toArray()方法传入参数是泛型T, 但泛型必须是引用类型(Integer),不能是基本类型(int)
    String[] strs = list.toArray(new String[list.size()]);
    //方法2
    for(int i=0; i<list.size(); i++){
    		strs[i] = list.get(i);
    }
    ```

8. ###  RPC框架 vs http请求访问远程服务

    成熟的 RPC框架(Dubbo)还提供好了“**服务自动注册与发现**”、"智能**负载均衡**"、“可视化的**服务治理和运维**”、“运行期**流量调度**”等等功能，这些也算是选择 RPC 进行服务注册和发现的一方面原因吧！

9. 八股文学习

    JAVA方面首先得会基础(随便看什么书，培训班教程也可以)，并发的话如果只是应付面试极客时间有一个课程叫做JAVA并发(好像是这么个名字，忘了)，jvm就那本经典的深入理解JAVA虚拟机(重点是内存管理，垃圾回收，常用监测工具，类文件结构，类加载机制)，然后有时间可以看看effective JAVA这本书，可能要很多面试会用到的知识。最后知乎或者博客搜一搜hashMap,concurrenthashMap,ArrayList,LinkedList等到源码分析，有闲心可以自己写一个hashMap,Lru,Lfu(有些面试可能会让你写)

    JAVA框架没啥说的，我就学了spring和netty,我没看spring源码,主要是觉得它太重了不想学。学的时候主要注意一些设计思想和设计模式。推荐github JAVAguide项目，有这方面的内容。netty我是看的李林峰的Netty权威指南。

    中间件，mysql强烈推荐极客时间的mysql实战45讲。当然前提是自己先得有一些数据库的基本知识。redis我以前刚学看了黑马的redis教程，还行。然后如果要对其原理了解的话，还要redis设计与实现这本书，重点是看一下数据结构和对象这一章节。然后有个比较重要的问题是redis和mysql双写一致性问题。可以去知乎搜博客。以及用redis如何写分布式锁；redis如何实现简单消息队列，有什么缺陷。zookeeper如果你项目没用的话，看看网上的教程简单了解下就行了，然后知道怎么用它写分布式锁或者分布式id. 消息队列分理论和具体中间件选型，理论大致有：为什么用消息队列，消息队列的推拉模式 ，如何保证消息不丢，如何处理重复消息，如何处理消息堆积等。然后具体我学的是kafka,看了极客时间的有一个课程。

    计网和操作系统，如果是科班的强烈推荐公众号“小林code”里的操作系统和计网两本电子书，(小林code打钱)，真的是总结的很好。不是科班或者计网和操作系统学的不够好，那可以先买个考研的王道教程看一看重点章节。

    设计模式，我发现一个很好的网站学习设计模式，百度搜索“设计模式与重构”，应该能搜到，是个ui很好看的国外网站，每一种设计模式都有具体的情景，框架图，优缺点分析，然后有各种语言实现的小demo.不过设计模式不能单学理论，比如你学一个框架的时候，就要知道哪部分用了什么设计模式，为什么这么设计，比如netty的reactor和责任链，spring的动态代理等等。个人感觉单例模式比较容易问道，有5中不同特点的实现方法(JAVA)，可以知乎或者博客搜索学习。

    至于分布式嘛，其实比较玄学，对于本科生来说其实基本难有高并发的实战经验。要应付面试造火箭，那么总体来说就是掌握分布式基本理论（CAP,BASE等），分布式id，分布式事务、分布式锁、分布式算法(Raft)、分布式缓存。上述知识我一方面看的极客时间的分布式教程，一方面是积累的各种博客。然后，分布式架构相关的，可以从RPC入手，学习其核心原理、以及服务治理相关内容，我看了李林峰的分布式服务框架这本书以及极客时间的RPC相关的一门课程。

    分布式如果不准备停留在吹牛逼的层面，时间足够，那么推荐MIT的6.824，照着课程和论文手撸K-V数据库和RAFT协议，很有帮助。

    作者：scufish
    链接：https://leetcode-cn.com/circle/discuss/JlrHm3/
    来源：力扣（LeetCode）
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。





With Respect to

剑指offer

剑指offer题解 Java版：https://github.com/CyC2018/CS-Notes/blob/master/notes/%E5%89%91%E6%8C%87%20Offer%20%E9%A2%98%E8%A7%A3%20-%20%E7%9B%AE%E5%BD%95.md

labuladong的算法小抄：https://labuladong.gitbook.io/algo/

JavaGuide：https://github.com/Snailclimb/JavaGuide

架构风清扬：https://www.bilibili.com/video/BV1k74118721
