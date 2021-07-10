package algorithms;

import domain.TreeNode;

public class BST {
    /**
     * [LC230. 二叉搜索树中第K小的元素](https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/)
     */
    int res = -1;
    int rank = 0;

    public int kthSmallest(TreeNode root, int k) {
        inorderToFindKthSmallest(root, k);
        return res;
    }


    public void inorderToFindKthSmallest(TreeNode root, int k){
        if (root==null)
            return;

        inorderToFindKthSmallest(root.left, k);

        //中序遍历位置
        rank++;
        if (rank==k) {
            res = root.val;
            return;
        }

        inorderToFindKthSmallest(root.right, k);
    }

    /**
     * [LC538==LC1038. 把二叉搜索树转换为累加树](https://leetcode-cn.com/problems/convert-bst-to-greater-tree/)
     */
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
}
