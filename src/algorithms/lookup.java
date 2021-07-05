package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class lookup {
    /**
     * LC76 矩阵置0
     * https://www.nowcoder.com/practice/9ff9256075a1498fb165b583d951ebd4?tpId=46&&tqId=29105&rp=1&ru=/ta/classic-code&qru=/ta/classic-code/question-ranking
     * 给定一个m*n的矩阵，如果有一个元素是0，就把该元素所在的行和列上的元素全置为0，要求使用原地算法。
     *
     * 你的算法有使用额外的空间吗？
     * 一种比较直接的算法是利用O(m,n)的空间，但是这不是一个好的解法
     * 使用简单的改进可以在O(m+n)的空间解决这个问题，但是还不是最佳的解法
     *
     * 你能在常量级的空间复杂度内解决这个问题吗？
     */
    public static void testSetZeros(){
        int[][] matrix = {
                {1, 2, 0, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {0, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}
        };
        System.out.println("Origins: ");
        printMatrix(matrix);

        setZeros(matrix);
        System.out.println("Results: ");
        printMatrix(matrix);
    }
    //打印矩阵
    private static void printMatrix(int[][] arr){
        for (int i = 0; i <arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }

    private static void setZeros(int[][] matrix){
        ArrayList<Integer> indexesOfRowContainsZero = new ArrayList<>();
        ArrayList<Integer> indexesOfColumnContainsZero = new ArrayList<>();
        //获取含0的列、行的index
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                if (matrix[i][j]==0){
                    indexesOfRowContainsZero.add(i);
                    indexesOfColumnContainsZero.add(j);
                }
            }
        }
        //把含0的行、列全部置为0
        for(Integer indexOfRowContainsZero: indexesOfRowContainsZero){
            for(int j = 0; j < matrix[indexOfRowContainsZero].length; j++){
                matrix[indexOfRowContainsZero][j] = 0;
            }
        }
        for(Integer indexOfColumnContainsZero: indexesOfColumnContainsZero){
            for(int i = 0; i < matrix.length; i++){
                matrix[i][indexOfColumnContainsZero] = 0;
            }
        }
    }

    /**
     * LC138 装最多水的容器(双指针问题)
     * https://www.nowcoder.com/practice/c97c1400a425438fb130f54fdcef0c57?tpId=46&&tqId=29167&rp=1&ru=/ta/classic-code&qru=/ta/classic-code/question-ranking
     *
     * 给定n个非负整数a1，a2，…，an，其中每个数字表示坐标(i, ai)处的一个点。以（i，ai）和（i，0）（i=1,2,3...n）为端点画出n条直线。你可以从中选择两条线与x轴一起构成一个容器，最大的容器能装多少水？
     * 例如：
     * 输入 [1,9,6,4,3,7]
     * 输出: 最大装水量 28 (9和7组成容器，中间有4格，容器高=min(9,7)
     */

    public static void testMaxArea(){
        //int[] height = {1,2,3,30,4,5,6,7,100,8,9,10,11,12};
        int[] height = {1,9,6,4,3,7};
        System.out.println("Origins: ");
        for(int i = 0; i < height.length; i++){
            System.out.print(height[i]+" ");
        }
        System.out.println();
        int result = maxArea(height);
        System.out.println("Result: "+result);

    }

    private static int maxArea (int[] height) {
        if(height.length<=1)
            return 0;

        int l = 0;
        int r = height.length-1;
        int area = 0;

        while (l < r){
            int h = Math.min(height[l], height[r]);
            area = Math.max(area, h*(r-l));
            //r高，则r不变，l向前进一位，直到遍历完成
            if (height[l] < height[r])
                l++;
            else
                r--;
        }
        return area;
    }




    /**
     * LC23 最长的连续元素序列长度
     * https://www.nowcoder.com/practice/57d83a2501164168841c158a7535b458?tpId=46&&tqId=29052&rp=1&ru=/ta/classic-code&qru=/ta/classic-code/question-ranking
     * 给定一个无序的整数类型数组，求最长的连续元素序列的长度。
     * 例如：
     * 给出的数组为[1000, 4, 2000, 1, 3, 96, 97, 98, 2],
     * 最长的连续元素序列为[1, 2, 3, 4]. 返回这个序列的长度：4
     * 你需要给出时间复杂度在O（n）之内的算法
     */
    public static void testLongestConsecutive(){
        int[] num = {0,0};
        System.out.println("Origins: ");
        for(int i = 0; i < num.length; i++){
            System.out.print(num[i]+" ");
        }
        System.out.println();
        System.out.println("Result: ");
        System.out.println(longestConsecutive(num));
    }
    private static int longestConsecutive (int[] num) {
        // write code here

        Arrays.sort(num);
        int count = 1;
        int result = 1;
        for(int i = 0; i < num.length-1; i++){
            if(num[i] == num[i+1])
                continue;
            else if(num[i] + 1 == num[i+1]){
                count++;
                result = Math.max(count, result);
            } else
                count = 1;
        }
        return result;
    }

    /**
     * LC70 单词搜索
     * https://www.nowcoder.com/practice/14bcbcb7ae3c40c9bdbc5a0861361c29?tpId=46&&tqId=29099&rp=1&ru=/ta/classic-code&qru=/ta/classic-code/question-ranking
     * 给出一个二维字符数组和一个单词，判断单词是否在数组中出现，
     * 单词由相邻单元格的字母连接而成，相邻单元指的是上下左右相邻。同一单元格的字母不能多次使用。
     * 例如：
     * 给出的字符数组=
     * [
     *   ["XYZE"],
     *   ["SFZS"],
     *   ["XDEE"]
     * ]
     * 单词 ="XYZZED", -> 返回 true,
     * 单词 ="SEE", ->返回 true,
     * 单词 ="XYZY", -> 返回 fXlse.
     *
     */
    public static void testExist(){
        char[][] board = {
                {'a', 'b'}
        };
        boolean result = exist(board,"ba");
        System.out.println(result);
    }

    private static boolean exist(char[][] board, String word) {
        char[] words = word.toCharArray();
        for(int x=0; x<board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (dfsForExist(board, x, y, words, 0))
                    return true;
            }
        }
        return false;
    }
    private static boolean dfsForExist(char[][] board, int x, int y, char[] words, int idx){
        if(idx >= words.length)
            return true;
        if(x < 0
                || x >= board.length
                || y < 0
                || y >= board[0].length
                || board[x][y] != words[idx]
        )
            return false;

        char temp = board[x][y];
        board[x][y] = '@';

        boolean result = dfsForExist(board, x+1, y, words, idx+1)
                || dfsForExist(board, x-1, y, words, idx+1)
                || dfsForExist(board, x, y+1, words, idx+1)
                || dfsForExist(board, x, y-1, words, idx+1);

        board[x][y] = temp;

        return result;
    }

    /**
     * LC6 Z字变换
     * https://leetcode-cn.com/problems/zigzag-conversion/
     * 将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。
     *
     * 比如输入字符串为 "PAYPALISHIRING" 行数为 3 时，排列如下：
     *
     * P   A   H   N
     * A P L S I I G
     * Y   I   R
     * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："PAHNAPLSIIGYIR"。
     *
     * 请你实现这个将字符串进行指定行数变换的函数：
     *
     * string convert(string s, int numRows);
     *
     *
     * 示例 1：
     *
     * 输入：s = "PAYPALISHIRING", numRows = 3
     * 输出："PAHNAPLSIIGYIR"
     * 示例 2：
     * 输入：s = "PAYPALISHIRING", numRows = 4
     * 输出："PINALSIGYAHRPI"
     * 解释：
     * P     I    N
     * A   L S  I G
     * Y A   H R
     * P     I
     * 示例 3：
     *
     * 输入：s = "A", numRows = 1
     * 输出："A"
     *
     *
     * 提示：
     *
     * 1 <= s.length <= 1000
     * s 由英文字母（小写和大写）、',' 和 '.' 组成
     * 1 <= numRows <= 1000
     */
    public static void testConvertZtoString(){
        String s = "PAYPALISHIRING";
        int numRows = 3;
        System.out.println("Origin: "+ s + ", numRows: "+ numRows);
        String res = convertZtoString(s, numRows);
        System.out.println("Result: "+ res);
    }

    private static String convertZtoString(String s, int numRows) {
        if(numRows < 2) return s;
        List<StringBuilder> resPerRow = new ArrayList<StringBuilder>();
        for(int i=0;i<numRows;i++){
            resPerRow.add(new StringBuilder());
        }

        int idx = 0;
        int flag = -1;

        for (char c: s.toCharArray()) {
            resPerRow.get(idx).append(c);
            if(idx==0 || idx==numRows-1)
                flag = -flag;
            idx += flag;
        }

        StringBuilder result = new StringBuilder();
        for(StringBuilder ResPerRow: resPerRow) result.append(ResPerRow);
        return result.toString();
    }

}
