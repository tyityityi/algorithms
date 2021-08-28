# 框架

BFS 算法都是用「队列」这种数据结构，每次将一个节点周围的所有节点加入队列。

BFS 相对 DFS 的最主要的区别是：**BFS 找到的路径一定是最短的，但代价就是空间复杂度可能比 DFS 大很多**

```java
// 计算从起点 start 到终点 target 的最近距离
int BFS(Node start, Node target) {
    Queue<Node> q; // 核心数据结构
    Set<Node> visited; // 避免走回头路


    q.offer(start); // 将起点加入队列
    visited.add(start);
    int step = 0; // 记录扩散的步数


    while (q not empty) {
        int size = q.size();
        /* 将当前队列中的所有节点向四周扩散 */
        for (int i = 0; i < size; i++) {
            Node cur = q.poll();
            /* 划重点：这里判断是否到达终点 */
            if (cur is target)
                return step;
            /* 将 cur 的相邻节点加入队列 */
            for (Node x : cur.adj())//泛指 cur 相邻的节点
                if (x not in visited) {
                    q.offer(x);
                    visited.add(x);
                }
        }
        /* 划重点：更新步数在这里 */
        step++;
    }
}
```

`cur.adj()` 泛指 `cur` 相邻的节点，比如说二维数组中，`cur` 上下左右四面的位置就是相邻节点；

`visited` 的主要作用是防止走回头路，大部分时候都是必须的，但是像一般的二叉树结构，没有子节点到父节点的指针，不会走回头路就不需要 `visited`。

# [752. 打开转盘锁](https://leetcode-cn.com/problems/open-the-lock/)

你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： `'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'` 。每个拨轮可以自由旋转：例如把 `'9'` 变为 `'0'`，`'0'` 变为 `'9'` 。每次旋转都只能旋转一个拨轮的一位数字。

锁的初始数字为 `'0000'` ，一个代表四个拨轮的数字的字符串。

列表 `deadends` 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。

字符串 `target` 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 `-1` 。

**示例 1:**

```
输入：deadends = ["0201","0101","0102","1212","2002"], target = "0202"
输出：6
解释：
可能的移动序列为 "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202"。
注意 "0000" -> "0001" -> "0002" -> "0102" -> "0202" 这样的序列是不能解锁的，
因为当拨动到 "0102" 时这个锁就会被锁定。
```

**示例 2:**

```
输入: deadends = ["8888"], target = "0009"
输出：1
解释：
把最后一位反向旋转一次即可 "0000" -> "0009"。
```

**示例 3:**

```
输入: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
输出：-1
解释：
无法旋转到目标数字且不被锁定。
```

**示例 4:**

```
输入: deadends = ["0000"], target = "8888"
输出：-1
```

函数签名：

```java
public int openLock(String[] deadends, String target);
```

## Solution

BFS：锁的每个位置可以上移+1或者下移-1（9上移变成0，0下移变成9），有四个位置，所以每一步都有八个可能的下一步

结束条件是等于target时返回当时的步数

visited用HashSet来存

将deadends加入visited，防止拨动到deanend

```java
class Solution {
    public int openLock(String[] deadends, String target) {
        Set<String> visited = new HashSet<>();
        for(int i=0; i<deadends.length; i++){
            visited.add(deadends[i]);
        }

        Queue<String> q = new LinkedList<>();
        if(!visited.contains("0000")){
            q.offer("0000");
            visited.add("0000");
        } else {//0000是在deadends里面被加入的
            return -1;
        }
        
        int step = 0;
        while(!q.isEmpty()){
            int size = q.size();
            for(int i=0; i<size; i++){
                String curr = q.poll();
                if(curr.equals(target))
                    return step;

                for(int j=0; j<4; j++){
                    String plus = plusOne(curr, j);
                    String minus = minusOne(curr, j);
                    if(!visited.contains(plus))
                        q.offer(plus);
                        visited.add(plus);
                    if(!visited.contains(minus))
                        q.offer(minus);
                        visited.add(minus);
                }
            }
            step++;
        }
        return -1;
    }
    private String plusOne(String s, int j){
        char[] ch = s.toCharArray();
        if(ch[j]=='9')
            ch[j] = '0';
        else 
            ch[j] += 1;
        return new String(ch);
    }
    private String minusOne(String s, int j){
        char[] ch = s.toCharArray();
        if(ch[j]=='0')
            ch[j] = '9';
        else 
            ch[j] -= 1;
        return new String(ch);
    }

}
```

