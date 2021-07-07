# 算法

## 排序

### 快速排序

  1. 选择第一个数作为pivot，比pivot小的放左边，比pivot大的放右边。
  2. 在左边的数里取第一个作为pivot..
     在右边的数里取第一个作为pivot..

<img src="./imgs/quicksort.gif" />

```java
public static int[] quickSort(int[] arr, int left, int right) {
  	if (left < right) {
        //获取中轴元素所处的位置
        //            System.out.println("Pivot:"+arr[left]);
        int mid = partition(arr, left, right);
        //            System.out.println(Arrays.toString(arr));
        //进行分割
        arr = quickSort(arr, left, mid - 1);
        arr = quickSort(arr, mid + 1, right);
    }
    return arr;
}

private static int partition(int[] arr, int left, int right) {
    //选取中轴元素
    int pivot = arr[left];
    int i = left + 1;
    int j = right;
    while (true) {
        // 向右找到第一个大于 pivot 的元素位置
        while (i <= j && arr[i] <= pivot) i++;
        // 向左找到第一个小于 pivot 的元素位置
        while (i <= j && arr[j] >= pivot ) j--;
      	if (i >= j)
        		break;
      	//交换两个元素的位置, 使得左边的元素不大于pivot, 右边的元素不小于pivot
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    arr[left] = arr[j];
    // 使中轴元素处于有序的位置
    arr[j] = pivot;
    return j;
}
```

时间复杂度：O(nlogn)
空间复杂度：O(logn)
非稳定排序
原地排序

### 归并排序

1. 通过递归的方式将大的数组一直分割，直到数组的大小为1，
2. 之后再把两个数组大小为1的合并成一个大小为2的，再把两个大小为2的合并成4的.....
    直到全部小的数组合并起来。

递归实现

```java
public static int[] recurMergeSort(int[] arr, int left, int right){

    if(left >= right)
     	 return arr;

    int mid = (right+left)/2;
    arr = recurMergeSort(arr, left, mid);
    arr = recurMergeSort(arr, mid+1, right);
    recurMerge(arr, left, mid, right);

    return arr;
}
public static void recurMerge(int[] arr, int left, int mid, int right){
  //+1是因为？？
    int[] temp = new int[right-left+1];
    int array1start = left;
    int array2start = mid+1;
    int i = 0;
    while(array1start<=mid && array2start<=right){
        if(arr[array1start]<arr[array2start]){
          	//i++和array1start++：先赋值，再+1
          	temp[i++] = arr[array1start++];
        } else{
          	temp[i++] = arr[array2start++];
        }
      }
    while(array1start<=mid) temp[i++]=arr[array1start++];
    while(array2start<=mid) temp[i++]=arr[array2start++];

    for(int a=0; a<i; a++){
      	arr[left++] = temp[a];
    }
}
```

迭代实现

```java
public static int[] iterMergeSort(int[] a) {
    // 子数组的大小分别为1，2，4，8...
    // 刚开始合并的数组大小是1，接着是2，接着4....
    for (int i = 1; i < a.length; i += i) {
        //进行数组进行划分
        int left = 0;
        int mid = left + i - 1;
        int right = mid + i;
        //进行合并，对数组大小为 i 的数组进行两两合并
        while (right < a.length) {
            // 合并函数和递归式的合并函数一样
            iterMerge(a, left, mid, right);
            left = right + 1;
            mid = left + i - 1;
            right = mid + i;
        }
        // 还有一些被遗漏的数组没合并，千万别忘了
        // 因为不可能每个字数组的大小都刚好为 i
        if (left < a.length && mid < a.length) {
            iterMerge(a, left, mid, a.length - 1);
        }
        System.out.println(Arrays.toString(a));

    }
    return a;
}

// 合并函数，把两个有序的数组合并起来
// arr[left..mif]表示一个数组，arr[mid+1 .. right]表示一个数组
private static void iterMerge(int[] a, int left, int mid, int right) {
    //先用一个临时数组把他们合并汇总起来
    int[] temp = new int[right - left + 1];
    int i = left;
    int j = mid + 1;
    int k = 0;
    while (i <= mid && j <= right) {
        if (a[i] < a[j]) {
            temp[k++] = a[i++];
        } else {
            temp[k++] = a[j++];
        }
    }
    while(i <= mid)
        temp[k++] = a[i++];
    while(j <= right)
        temp[k++] = a[j++];
    // 把临时数组复制到原数组
    for (i = 0; i < k; i++) {
        a[left++] = temp[i];
    }

```

时间复杂度：O(nlogn)

空间复杂度：O(1)

非稳定排序

原地排序

## 二叉树

### 基础二叉树算法

#### 二叉树节点总数

```java
public static int count(TreeNode root){
		if (root == null) return 0;
  	return 1 + count(root.left) + count(root.right);
}
```

#### LC[104.二叉树的最大深度](https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/)

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

####  LC[226.翻转二叉树](https://leetcode-cn.com/problems/invert-binary-tree/)

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

#### LC[116.填充每个节点的下一个右侧节点指针](https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node/)

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

示例：![img](imgs/116_sample.png)

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

#### LC[114. 二叉树展开为链表](https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list/)

给你二叉树的根结点 root ，请你将它展开为一个单链表：

展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
展开后的单链表应该与二叉树 先序遍历 顺序相同。


示例 1：![img](https://assets.leetcode.com/uploads/2021/01/14/flaten.jpg)

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

![image-20210707180317323](imgs/image-20210707180317323.png)

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

