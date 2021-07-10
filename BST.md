# Binary Search Tree 二叉搜索树

------

- ## [LC230. 二叉搜索树中第K小的元素](https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/)

    给定一个二叉搜索树的根节点 root ，和一个整数 k ，请你设计一个算法查找其中第 k 个最小元素（从 1 开始计数）。

     

    示例 1：

    <img src="https://assets.leetcode.com/uploads/2021/01/28/kthtree1.jpg" alt="img" style="width:20%;" />

    输入：root = [3,1,4,null,2], k = 1
    输出：1
    示例 2：

    <img src="https://assets.leetcode.com/uploads/2021/01/28/kthtree2.jpg" alt="img" style="width:35%;" />


    输入：root = [5,3,6,2,4,null,null,1], k = 3
    输出：3

    


    提示：

    树中的节点数为 n 。
    1 <= k <= n <= 104
    0 <= Node.val <= 104

    签名函数：

    ```java
    public int kthSmallest(TreeNode root, int k);
    ```

    

    ### <u>**思路**</u>

    BST的**中序遍历** (访问顺序**1left 2root 3right**) = **升序**遍历

    ### <u>**Solution**</u>

    ```Java
    int res = -1;
    int rank = 0;
    
    public int kthSmallest(TreeNode root, int k){
      	if (root==null)
        		return -1;
    
        kthSmallest(root.left, k);
    
        //中序遍历位置 
        rank++;
        if (rank==k) {
            res = root.val;
            //return可避免遍历兄弟右子树
            return res;
        }
    
        kthSmallest(root.right, k);
        return res;
    }
    ```

- ## [LC538==LC1038. 把二叉搜索树转换为累加树](https://leetcode-cn.com/problems/convert-bst-to-greater-tree/)

    给出二叉 搜索 树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），使每个节点 node 的**新值**等于**原树中**<u>大于或等于 node.val 的值之和</u>。

    示例 1：

    <img src="https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2019/05/03/tree.png" alt="img" style="width:50%;" />

    输入：[4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
    输出：[30,36,21,36,35,26,15,null,null,null,33,null,null,null,8]

    函数签名：

    ```java
    public TreeNode bstToGst(TreeNode root);
    ```

    

    ### <u>**思路**</u>

    BST的**中序遍历** (访问顺序倒过来**1right 2root 3left**) = **降序**遍历

    ### <u>**Solution**</u>

    ```java
    int sum = 0;
    public TreeNode bstToGst(TreeNode root) {
        if (root==null)
          	return null;
    
        bstToGst(root.right);
    
        //中序遍历位置
        sum += root.val;
        root.val = sum;
    
        bstToGst(root.left);
        return root;
    }
    ```

    