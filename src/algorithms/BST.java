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

    /**
     * [LC98. 验证二叉搜索树的合法性](https://leetcode-cn.com/problems/validate-binary-search-tree/)
     */
    public boolean isValidBST(TreeNode root) {
        return isValid(root, null, null);
    }

    /* 限定以 root 为根的子树节点必须满足 max.val > root.val > min.val */
    public boolean isValid(TreeNode root, TreeNode min, TreeNode max){
        if(root==null)
            return true;

        if(min!=null && root.val<=min.val)
            return false;
        if(max!=null && root.val>=max.val)
            return false;

        // 限定左子树的最大值是 root.val，右子树的最小值是 root.val
        return isValid(root.left, min, root)
                && isValid(root.right, root, max);
    }

    /**
     * [LC700. 二叉搜索树中的搜索](https://leetcode-cn.com/problems/search-in-a-binary-search-tree/)
     */
    public TreeNode searchBST(TreeNode root, int val) {
        if (root==null)
            return null;
        if (val==root.val)
            return root;

        if (val<root.val)
            return searchBST(root.left, val);
        else
            return searchBST(root.right, val);
    }

    /**
     * [LC701. 二叉搜索树中的插入操作](https://leetcode-cn.com/problems/insert-into-a-binary-search-tree/)
     */
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if(root==null)
            return new TreeNode(val);

        //if (root.val == val)
        //BST 中一般不会插入已存在元素
        if(val<root.val)
            root.left = insertIntoBST(root.left, val);
        if(val>root.val)
            root.right = insertIntoBST(root.right, val);
        return root;
    }

    /**
     * [LC450. 删除二叉搜索树中的节点](https://leetcode-cn.com/problems/delete-node-in-a-bst/)
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root==null)
            return null;
        if(key==root.val){
            //恰好是末端节点，两个子节点都为空，那么它可以当场去世了
            if(root.left==null && root.right==null)
                return null;
                //只有一个非空子节点，那么它要让这个孩子接替自己的位置。
            else if(root.left==null)
                return root.right;
            else if(root.right==null)
                return root.left;
                //有两个子节点, 必须找到左子树中最大的那个节点，或者右子树中最小的那个节点 来接替自己。
            else {
                //情况三解法1: 左子树中最大的那个节点
                TreeNode maxInLeft = root.left;
                while(maxInLeft.right!=null)
                    maxInLeft = maxInLeft.right;
                //交换root(要删的node)和maxInleft(替换的node)位置，使要删除的node的位置的情况变成情况1
                root.val = maxInLeft.val;
                //去maxInleft的位置删除node
                root.left = deleteNode(root.left, maxInLeft.val);

                /**
                 //情况三解法2: 左子树中最大的那个节点
                 TreeNode maxInRight = root.right;
                 while(maxInRight.left!=null)
                    maxInRight = maxInRight.left;
                 //交换root(要删的node)和maxInleft(替换的node)位置，使要删除的node的位置的情况变成情况1
                 root.val = maxInRight.val;
                 //去maxInleft的位置删除node
                 root.right = deleteNode(root.right, maxInRight.val);
                 */
            }
        } else if(key<root.val){
            root.left = deleteNode(root.left, key);
        } else {
            root.right = deleteNode(root.right, key);
        }
        return root;
    }






}
