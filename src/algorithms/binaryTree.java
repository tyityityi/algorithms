package algorithms;


import domain.Node;
import domain.TreeNode;

import java.util.*;

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

    //递归
    public ArrayList<Integer> postorderTraversalByRecursive (TreeNode root) {
        // write code here
        ArrayList<Integer> result = new ArrayList<Integer>();
        postTra(root, result);
        return result;
    }
    public void postTra(TreeNode node, ArrayList<Integer> res){
        if(node==null)
            return;
        postTra(node.left, res);
        postTra(node.right, res);
        res.add(node.val);
    }

    //迭代
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

    //[LC116 填充每个节点的下一个右侧节点指针](https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node/)
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

    //[LC114. 二叉树展开为链表](https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list/)
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

    //[LC654. 最大二叉树](https://leetcode-cn.com/problems/maximum-binary-tree/)
    public static TreeNode constructMaximumBinaryTree(int[] nums) {
        return build(nums, 0, nums.length-1);
    }

    public static TreeNode build(int[] nums, int lo, int hi ){
        if (!(lo <= hi))
            return null;

        //找到数组中的最大value及其index
        int maxVal = Integer.MIN_VALUE;
        int maxValIdx = -1;
        for (int i=lo; i<=hi; i++){
            if (nums[i]>maxVal){
                maxVal = nums[i];
                maxValIdx = i;
            }
        }

        //构造树的根
        TreeNode root = new TreeNode(maxVal);
        //define树的左子树
        root.left = build(nums, lo, maxValIdx-1);
        //define树的右子树
        root.right = build(nums, maxValIdx+1, hi);

        return root;
    }

    //[105. 从前序与中序遍历序列构造二叉树](https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)
    public static TreeNode buildTreeFromInAndPre(int[] preorder, int[] inorder) {
        return buildFromInAndPre(preorder, 0, preorder.length-1,
                inorder, 0, inorder.length-1);
    }

    public static TreeNode buildFromInAndPre(int[] preorder, int preStart, int preEnd,
                          int[] inorder, int inStart, int inEnd){
        if (!(preStart <= preEnd))
            return null;

        //遍历inorder[] 找root的值在inorder中的位置
        int rootVal = preorder[preStart];
        int rootValInIdx = -1;
        for (int i=inStart; i<=inEnd; i++){
            if (inorder[i]==rootVal){
                rootValInIdx = i;
                break;
            }
        }

        //root.left的元素总数
        int leftSize = rootValInIdx - inStart;

        //构造左右子树
        TreeNode root = new TreeNode(rootVal);
        root.left = buildFromInAndPre(preorder, preStart+1, preStart+1+(leftSize-1),
                inorder, inStart, rootValInIdx - 1);
        root.right = buildFromInAndPre(preorder, preStart+1+leftSize, preEnd,
                inorder, rootValInIdx+1, inEnd);

        return root;
    }

    //[LC106. 从中序与后序遍历序列构造二叉树](https://leetcode-cn.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)
    public TreeNode buildTreeFromInAndPost(int[] inorder, int[] postorder) {
        return buildFromInAndPost(postorder, 0, postorder.length-1,
                inorder, 0, inorder.length-1);
    }
    public TreeNode buildFromInAndPost(int[] postorder, int postStart, int postEnd,
                          int[] inorder, int inStart, int inEnd){
        if (!(postStart <= postEnd))
            return null;

        int rootVal = postorder[postEnd];
        int rootValInIdx = -1;
        for (int i=inStart; i<=inEnd; i++){
            if (inorder[i]==rootVal){
                rootValInIdx = i;
                break;
            }
        }

        int leftSize = rootValInIdx - inStart;

        TreeNode root = new TreeNode(rootVal);
        root.left = buildFromInAndPost(postorder, postStart, postStart+leftSize-1,
                inorder, inStart, rootValInIdx-1);
        root.right = buildFromInAndPost(postorder, postStart+leftSize, postEnd-1,
                inorder, rootValInIdx+1, inEnd);
        return root;
    }

    //[LC652. 寻找重复的子树](https://leetcode-cn.com/problems/find-duplicate-subtrees/)
    public static List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        //！！！注意list的创建方式！
        List<TreeNode> res = new ArrayList<>();
        //存子树的样子及其出现频率
        Map<String, Integer> nodesFreq = new HashMap<>();
        postTraverse(root, res, nodesFreq);
        return res;
    }

    public static String postTraverse(TreeNode root, List<TreeNode> res, Map<String, Integer> nodesFreq){
        if (root == null)
            return "#";

        String left = postTraverse(root.left, res, nodesFreq);
        String right = postTraverse(root.right, res, nodesFreq);
        String subtree = root.val + "," + left + ","+ right;

        //此子树未出现过
        if (nodesFreq.get(subtree)==null){
            nodesFreq.put(subtree, 1);
            return subtree;
        }


        Integer freq = nodesFreq.get(subtree);

        if (freq==1){
            //此子树只出现过一次，添加到结果集
            res.add(root);
            //更新此子树的出现频率为2（相当于replace方法）
            nodesFreq.put(subtree, freq+1);
        } else {
            //此子树出现超过一次，不添加到结果集
            nodesFreq.put(subtree, freq+1);
        }

        return subtree;
    }

    /**
     * [LC297. 二叉树的序列化与反序列化](https://leetcode-cn.com/problems/serialize-and-deserialize-binary-tree/)
     */
    //后序遍历实现

    // Encodes a tree to a single string.
    StringBuilder sb = new StringBuilder();
    public String serialize(TreeNode root) {
        serializeTraverse(root);
        return sb.toString();
    }
    public void serializeTraverse(TreeNode root){
        if(root==null){
            sb.append("#").append(",");
            return;
        }
        //后序遍历
        serializeTraverse(root.left);
        serializeTraverse(root.right);
        sb.append(root.val).append(",");
        return;
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        //这里也可用ArrayList，因为两种List在列表末尾增加一个元素所花的开销都是固定的
        //用LinkedList是因为它有removeLast()方法，ArrayList则需要remove(nodes.size())
        LinkedList<Integer> nodes = new LinkedList<>();
        for(String val: data.split(",")){
            if(val.equals("#")){
                //Null的情况
                nodes.addLast(Integer.MIN_VALUE);
            } else{
                nodes.addLast(Integer.parseInt(val));
            }
        }
        return deserializeBuild(nodes);
    }
    public TreeNode deserializeBuild(LinkedList<Integer> nodes){
        if(nodes.isEmpty())
            return null;
        // 列表最右侧是根节点
        Integer val = nodes.removeLast();
        if(val==Integer.MIN_VALUE)
            return null;
        TreeNode root = new TreeNode(val);
        //这里是先right再left，因为后序遍历是从后往前的顺序
        root.right = deserializeBuild(nodes);
        root.left = deserializeBuild(nodes);
        return root;
    }

    /**
     * [LC222. 完全二叉树的节点个数](https://leetcode-cn.com/problems/count-complete-tree-nodes/) O(logN*logN)
     */
    public int countNodes(TreeNode root) {
        if(root==null)
            return 0;

        //root的左右子树一定会有一棵满二叉树
        //满二叉树的节点计算方法
        TreeNode l = root;
        TreeNode r = root;
        int hl = 1;
        int hr = 1;
        while(l.left!=null){
            l = l.left;
            hl += 1;
        }
        while(r.right!=null){
            r = r.right;
            hr += 1;
        }
        if(hl==hr){
            //满二叉树的节点总数就是 2^h - 1
            //Math.pow返回double类型
            return (int)Math.pow(2, hl) - 1;
        }

        //普通二叉树的节点计算方法
        //这两个递归只有一个会真的递归下去，另一个一定会触发 hl == hr (因为是满二叉树)而立即返回，不会递归下去。
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    /**
     * [LC236. 二叉树的最近公共祖先](https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/)
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root==null)
            return null;
        //即使其中一点不存在于以`root`为根的树中，也应该返回`root`节点本身。
        if(root==p || root==q)
            return root;

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        //后序遍历位置
        //情况 1，如果`p`和`q`都**在**以`root`为根的树中，返回**root**
        if(left!=null && right!=null)
            return root;
        //情况 2，如果`p`和`q`都**不在**以`root`为根的树中，返回**null**
        if(left==null && right==null)
            return null;
        //情况 3，返回不为空的那个子树
        return left!=null? left: right;
    }
}
