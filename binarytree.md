# 二叉树

------

- ### 基础二叉树算法

#### 二叉树节点总数

```java
public static int count(TreeNode root){
		if (root == null) return 0;
  	return 1 + count(root.left) + count(root.right);
}
```

- ### LC[104.二叉树的最大深度](https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/)

```wiki
给定二叉树 [3,9,20,null,null,15,7]，
		3
   / \
  9  20
    /  \
   15   7
返回它的最大深度 3 
```

<u>**Solution**</u>:

```java
public static int maxDepth(TreeNode root){
  	if (root == null) return 0;
  	return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
}
```

- ###  LC[226.翻转二叉树](https://leetcode-cn.com/problems/invert-binary-tree/)

```wiki
翻转一棵二叉树。

示例：

输入：
     4
   /   \
  2     7
 / \   / \
1   3 6   9
输出：
     4
   /   \
  7     2
 / \   / \
9   6 3   1

```

<u>**Solution:**</u>

```java
public static TreeNode invertTree(TreeNode root) {
    if (root == null) return null;
  
		//交换左右子节点
    TreeNode tmp = root.left;
    root.left = root.right;
    root.right = tmp;
		//让左右子节点继续翻转他们的子节点
    invertTree(root.left);
    invertTree(root.right);

    return root;
    }
```

- ### LC[116.填充每个节点的下一个右侧节点指针](https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node/)

给定一个 完美二叉树 ，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下：

```java
class Node {
    int val;
    Node *left;
    Node *right;
    Node *next;
}
```

填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。

初始状态下，所有 next 指针都被设置为 NULL。

进阶：

你只能使用常量级额外空间。
使用递归解题也符合要求，本题中递归程序占用的栈空间不算做额外的空间复杂度。

示例：<img src="./imgs/116_sample.png" alt="img" style="zoom: 50%;" />

输入：root = [1,2,3,4,5,6,7]
输出：[1,#,2,3,#,4,5,6,7,#]
解释：给定二叉树如图 A 所示，你的函数应该填充它的每个 next 指针，以指向其下一个右侧节点，如图 B 所示。序列化的输出按层序遍历排列，同一层节点由 next 指针连接，'#' 标志着每一层的结束。

<u>**Solution**</u>:

```java
public static Node connect(Node root) {
    if (root == null) return null;
    connectTwoNodes(root.left, root.right);
    return root;
    }
public static void connectTwoNodes(Node leftNode, Node rightNode) {
    if (leftNode == null || rightNode == null) return;
		//连接两个传入节点
    leftNode.next = rightNode;
		//连接 两个传入节点 的 两个子节点
    connectTwoNodes(leftNode.left, leftNode.right);
    connectTwoNodes(rightNode.left, rightNode.right);
		//连接跨越副节点的两个子节点
    connectTwoNodes(leftNode.right, rightNode.left);
}
```

- ### LC[114. 二叉树展开为链表](https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list/)

给你二叉树的根结点 root ，请你将它展开为一个单链表：

展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
展开后的单链表应该与二叉树 先序遍历 顺序相同。


示例 1：<img src="https://assets.leetcode.com/uploads/2021/01/14/flaten.jpg" alt="img" style="zoom: 50%;" />

输入：root = [1,2,5,3,4,null,6]
输出：[1,null,2,null,3,null,4,null,5,null,6]

示例 2：

输入：root = []
输出：[]

示例 3：

输入：root = [0]
输出：[0]

<u>**Solution**</u>

1、将 `root` 的左子树和右子树拉平

2、将 `root` 的右子树接到左子树下方

3、然后将整个左子树作为右子树

<img src="./imgs/image-20210707180317323.png" alt="image-20210707180317323" style="zoom:50%;" />

```java
public static void flatten(TreeNode root) {
    if (root == null) return;

    flatten(root.left);
    flatten(root.right);
    
    //记录原始的右子节点
    TreeNode originRight = root.right;

    //将左子树作为右子数，左子数置为空
    root.right = root.left;
    root.left = null;

    //将原先的右子树接到当前右子树末端
    while (root.right != null){
        root = root.right;
    }
    root.right = originRight;
}
```

