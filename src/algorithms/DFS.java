package algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DFS {
    /**
     * [LC46. 全排列](https://leetcode-cn.com/problems/permutations/)
     */
    List<List<Integer>> res = new LinkedList<>();
    public List<List<Integer>> permute(int[] nums) {
        // 记录「路径」
        LinkedList<Integer> track = new LinkedList<>();
        dfsPermute(nums, track);
        return res;
    }

    // 路径：记录在 track 中
    // 选择列表：nums 中不存在于 track 的那些元素
    // 结束条件：nums 中的元素全都在 track 中出现
    public void dfsPermute(int[] nums, LinkedList<Integer> track){
        // 触发结束条件
        if(track.size()==nums.length){
            //复制LinkedList中的元素到新的LinkedList
            //！！如果直接add(track)会添加track的引用！！
            res.add(new LinkedList(track));
            return;
        }

        for(int i=0; i<nums.length; i++){
            // 排除不合法的选择
            if(track.contains(nums[i]))
                continue;
            // 做选择
            track.add(nums[i]);
            // 进入下一层决策树
            dfsPermute(nums, track);
            // 取消选择
            track.removeLast();
        }
    }
    /**
     * [LC51. N 皇后](https://leetcode-cn.com/problems/n-queens/)
     */
    List<List<String>> results = new LinkedList<>();
    public List<List<String>> solveNQueens(int n) {
        // '.' 表示空，'Q' 表示皇后，初始化空棋盘。
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            char[] row = new char[n];
            Arrays.fill(row, '.');
            board[i] = row;
        }
        //从第一行开始自上而下进行选择
        dfsSolveNQueens(board, 0);
        return results;
    }

    // 路径：board 中小于 row 的那些行都已经成功放置了皇后
    // 选择列表：第 row 行的所有列都是放置皇后的选择
    // 结束条件：row 超过 board 的最后一行
    public void dfsSolveNQueens(char[][] board, int row){
        int totalRow = board.length;
        // 触发结束条件: row的取值为[0,n-1],当row=n时，board[n]会报错
        if(row==totalRow){
            List<String> result = new LinkedList<>();
            //将结果逐行转化为List<String>并加入results
            for(int i=0; i<totalRow; i++){
                String resRow = String.valueOf(board[i]);
                result.add(resRow);
            }
            results.add(result);
            return;
        }

        int totalCol = board[0].length;
        for(int col=0; col<totalCol; col++){
            //排除不合法选择
            if(!isValidNQueens(board, row, col))
                continue;
            //做选择
            board[row][col] = 'Q';
            //进入下一层决策树
            dfsSolveNQueens(board, row+1);
            //撤销选择
            board[row][col] = '.';
        }
    }
    public boolean isValidNQueens(char[][] board, int row, int col){
        //因为是自上而下做选择，所以只需检查当前row上方是否有冲突
        //检查不同行 同列的位置是否有皇后
        for(int i=0; i<row; i++){
            if(board[i][col]=='Q')
                return false;
        }
        //检查右上方的斜线是否有皇后
        for(int i=row-1, j=col+1; i>=0 && j<board[0].length; i--, j++){
            if(board[i][j]=='Q')
                return false;
        }
        //检查左上方的斜线是否有皇后
        for(int i=row-1, j=col-1; i>=0&&j>=0; i--, j--){
            if(board[i][j]=='Q')
                return false;
        }

        return true;
    }
    /**
     * [LC37. 解数独](https://leetcode-cn.com/problems/sudoku-solver/)
     */
    public void solveSudoku(char[][] board) {
        dfsSolveSudoku(board, 0, 0);
    }
    public boolean dfsSolveSudoku(char[][] board, int row, int col){
        //穷举完了最后一行，完成了所有的穷举，就是 base case。
        if(row==9)
            return true;
        //穷举完最后一列，转为增加row开始穷举下一行row+1的第一列0
        if(col==9)
            return dfsSolveSudoku(board, row+1, 0);
        //如果这个位置题目有提供数字，则不做选择，转而判断下一个数字
        if(board[row][col]!='.')
            return dfsSolveSudoku(board, row, col+1);

        for(char num='1'; num<='9'; num++){
            // 如果遇到不合法的数字,就跳过
            if(!isValidSudoku(board, row, col, num))
                continue;
            board[row][col] = num;
            // 如果找到一个可行解，立即结束
            if(dfsSolveSudoku(board, row, col+1))
                return true;
            board[row][col] = '.';
        }
        //穷举完如果没找到可行解，则此题无解
        return false;
    }
    // 判断 board[i][j] 是否可以填入 n
    public boolean isValidSudoku(char[][]board, int row, int col, char num){
        for(int i=0; i<9; i++){
            //固定col，轮询row,找同一列中是否已经存在数num
            if(board[i][col]==num)
                return false;
            //固定row，轮询col,找同一行中是否已经存在数num
            if(board[row][i]==num)
                return false;
            // (n/3)返回n➗3后的整数部分
            // (n/3)*3返回0，3，6，9......
            //如果row=4，col=4， 当i=0时，下面方法判断board[3][3];
            //                  当i=1时，下面方法判断board[3][4];
            //                  当i=2时，下面方法判断board[3][5];
            //                  当i=3时，下面方法判断board[4][3];
            //                  当i=4时，下面方法判断board[4][4];
            //                  当i=1时，下面方法判断board[4][5]...
            // 判断 3 x 3 方框是否存在重复
            if(board[(row/3)*3 + i/3][(col/3)*3 + i%3]==num)
                return false;
        }
        return true;
    }















}
