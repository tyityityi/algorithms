package algorithms;


import domain.Node;
import domain.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class binaryTree {


    /**
     * 二叉树的先序遍历
     * https://www.nowcoder.com/practice/501fb3ca49bb4474bf5fa87274e884b4?tpId=46&&tqId=29036&rp=1&ru=/ta/classic-code&qru=/ta/classic-code/question-ranking
     * 1. 访问根节点;
     * 2. 访问当前节点的左子树;
     * 3. 访问当前节点的右子树。
     *
     */
    //递归
    public ArrayList<Integer> preorderTraversalByRecursive (TreeNode root) {
        // write code here
        ArrayList<Integer> result = new ArrayList<Integer>();
        preTra(root, result);
        return result;

    }
    public void preTra (TreeNode node, ArrayList<Integer> result){
        if(node==null)
            return;
        result.add(node.val);
        preTra(node.left,result);
        preTra(node.right,result);
    }

    //迭代 用stack
    public ArrayList<Integer> preorderTraversalByIteration (TreeNode root) {
        // write code here
        ArrayList<Integer> result = new ArrayList<Integer>();
        Stack<TreeNode> stack = new Stack<TreeNode>();

        if(root==null)
            return result;

        stack.push(root);
        //！！！stack.empty, 不能用stack==null
        while(!stack.empty()){
            TreeNode node = stack.pop();
            result.add(node.val);
            if(node.right!=null)
                stack.push(node.right);
            if(node.left!=null)
                stack.push(node.left);
        }
        return result;
    }

    /**
     * 二叉树中序遍历
     * https://www.nowcoder.com/practice/1b25a41f25f241228abd7eb9b768ab9b?tpId=46&&tqId=29084&rp=1&ru=/ta/classic-code&qru=/ta/classic-code/question-ranking
     * 1. 访问左节点;
     * 2. 再访问根节点;
     * 3. 最后访问右节点。
     */
    //递归
    public ArrayList<Integer> inorderTraversalByRecur (TreeNode root) {
        // write code here
        ArrayList<Integer> res = new ArrayList<Integer>();
        inTra(root, res);
        return res;
    }

    public void inTra(TreeNode node, ArrayList<Integer> res){
        if(node==null)
            return;
        inTra(node.left, res);
        res.add(node.val);
        inTra(node.right, res);
    }

    //迭代
    public ArrayList<Integer> inorderTraversalByIter (TreeNode root) {
        // write code here
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(root==null)
            return res;
        Stack<TreeNode> stack = new Stack<TreeNode>();

        TreeNode node = root;
        while(!stack.isEmpty() || node!=null){
            while(node!=null){
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            res.add(node.val);
            node = node.right;
        }
        return res;
    }

    /**
     * 二叉树的后序遍历
     * https://blog.csdn.net/ibelieve8013/article/details/103059101
     * 左、右、根
     */
    public ArrayList<Integer> postorderTraversal (TreeNode root) {
        // write code here
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(root==null)
            return res;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(!stack.isEmpty()){
            TreeNode node = stack.pop();
            res.add(node.val);
            if(node.left!=null)
                stack.push(node.left);
            if(node.right!=null)
                stack.push(node.right);
        }
        Collections.reverse(res);
        return res;
    }
    //二叉树节点总数
    public static int count(TreeNode root){
        if (root == null) return 0;
        return 1 + count(root.left) + count(root.right);
    }

    /**
     * 剑指Offer55 二叉树最大深度
     * https://leetcode-cn.com/problems/er-cha-shu-de-shen-du-lcof
     * 输入一棵二叉树的根节点，求该树的深度。从根节点到叶节点依次经过的节点（含根、叶节点）形成树的一条路径，最长路径的长度为树的深度。
     *
     * 例如：
     *
     * 给定二叉树 [3,9,20,null,null,15,7]
     *
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     * 返回它的最大深度 3 。
     */
    public static int maxDepth(TreeNode root){
        if (root==null) return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right))+1;
    }

    //LC226 翻转二叉树
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) return null;

        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;

        invertTree(root.left);
        invertTree(root.right);

        return root;
    }

    //LC116 [填充每个节点的下一个右侧节点指针](https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node/)
    public static Node connect(Node root) {
        if (root == null) return null;
        connectTwoNodes(root.left, root.right);
        return root;
    }
    public static void connectTwoNodes(Node leftNode, Node rightNode) {
        if (leftNode == null || rightNode == null) return;

        leftNode.next = rightNode;

        connectTwoNodes(leftNode.left, leftNode.right);
        connectTwoNodes(rightNode.left, rightNode.right);

        connectTwoNodes(leftNode.right, rightNode.left);
    }

    //
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



}
