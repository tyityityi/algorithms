## DFS 回溯算法

[toc]

------



## 回溯算法框架

**解决一个回溯问题，实际上就是一个决策树的遍历过程**。你只需要思考 3 个问题：

1、路径：也就是已经做出的选择。

2、选择列表：也就是你当前可以做的选择。

3、结束条件：也就是到达决策树底层，无法再做选择的条件。

```python
result = []
def backtrack(路径, 选择列表):
    if 满足结束条件:
        result.add(路径)
        return

    for 选择 in 选择列表:
        # 做选择（前序遍历位置！）
        将该选择从选择列表移除
        路径.add(选择)
        backtrack(路径, 选择列表)
        # 撤销选择（后序遍历位置！）
        路径.remove(选择)
        将该选择再加入选择列表
```

## [LC46. 数字全排列](https://leetcode-cn.com/problems/permutations/)

给定一个**不含重复数字**的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。

示例 1：输入：nums = [1,2,3]
输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

示例 2：输入：nums = [0,1]
输出：[[0,1],[1,0]]

示例 3：输入：nums = [1]
输出：[[1]]

**提示：**

- `1 <= nums.length <= 6`
- `-10 <= nums[i] <= 10`
- `nums` 中的所有整数 **互不相同**

函数签名：

```java
public List<List<Integer>> permute(int[] nums);
```

### <u>**思路**</u>

构造决策树，前序遍历位置做选择，后序遍历位置撤销选择

<img src="../imgs/image-20210716142708055.png" alt="image-20210716142708055" style="width:80%;" />

关键点在于用`contains` 方法排除已经选择的数字。

### <u>**Solution**</u>

```java
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
```

对链表使用 `contains` 方法需要 O(N) 的时间复杂度

必须说明的是，不管怎么优化，都符合回溯框架，而且时间复杂度都不可能低于 O(N!)，因为穷举整棵决策树是无法避免的。**这也是回溯算法的一个特点，不像动态规划存在重叠子问题可以优化，回溯算法就是纯暴力穷举，复杂度一般都很高**。

## [剑指 Offer 38. 字符串的全排列](https://leetcode-cn.com/problems/zi-fu-chuan-de-pai-lie-lcof/)

输入一个字符串，打印出该字符串中字符的所有排列。

你可以以任意顺序返回这个字符串数组，但里面不能有重复元素。

**示例:**

```
输入：s = "abc"
输出：["abc","acb","bac","bca","cab","cba"]
```

函数签名

```java
public String[] permutation(String s);
```

###  Solution

与<u>LC46数字全排列</u>的区别在于给定的字符串中可能有重复的字符，用Set来接收结果；

字符不能用List<Character>来记录subRes，要用StringBuilder；

StringBuilder没有contains()方法，要加一个boolean[] visited来记录已用过的字符；

```java
Set<String> res = new HashSet<>();//避免重复
    public String[] permutation(String s) {
        dfs(s, new StringBuilder(""), new boolean[s.length()]);
        return res.toArray(new String[res.size()]);
    }
    public void dfs(String s, StringBuilder subRes, boolean[] visited){
        //结束条件
        if(subRes.length()==s.length()){
            res.add(subRes.toString());
            return;
        }

        for(int i=0; i<s.length(); i++){
            if(visited[i]) continue;
            //做选择
            subRes.append(s.charAt(i));
            visited[i] = true;
            //进入下一层决策树
            dfs(s, subRes, visited);
            //撤销选择
            subRes.deleteCharAt(subRes.length()-1);
            visited[i] = false;
        }
    }
```

时间复杂度：O(n×n!)，其中 n 为给定字符串的长度。这些字符的全部排列有 O(n!) 个，每个排列平均需要 O(n) 的时间来生成。

空间复杂度：O(n)。我们需要 O(n) 的栈空间进行回溯，注意返回值不计入空间复杂度。

## [LC78. 子集](https://leetcode-cn.com/problems/subsets/)

给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。

解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。

示例 1：

输入：nums = [1,2,3]
输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
示例 2：

输入：nums = [0]
输出：[[],[0]]

函数签名：

```java
public List<List<Integer>> subsets(int[] nums);
```

### <u>**Solution**</u>

<img src="../imgs/image-20210717180407833.png" alt="image-20210717180407833" style="width:50%;" />

与全排列不同，寻找子集不必到决策树**最底层**（即无结束条件）再加入结果，而是在决策树的**每一层都要加入结果**

关键点在于要用 `startIdx` 参数排除已选择的数字

```java
		List<List<Integer>> results = new LinkedList<>();
    public List<List<Integer>> subsets(int[] nums) {
        LinkedList<Integer> result = new LinkedList<>();
        dfsBuildSubsets(nums, 0, result);
        return results;
    }
    public void dfsBuildSubsets(int[] nums, int startIdx, LinkedList<Integer> result){
        results.add(new LinkedList(result));
        for(int i=startIdx; i<nums.length; i++){
            result.addLast(nums[i]);
            dfsBuildSubsets(nums, i+1, result);
            result.removeLast();
        }
    }
```

## [LC77. 组合](https://leetcode-cn.com/problems/combinations/)

给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。

示例:

输入: n = 4, k = 2
输出:
[
  [2,4],
  [3,4],
  [2,3],
  [1,2],
  [1,3],
  [1,4],
]

函数签名：

```java
public List<List<Integer>> combine(int n, int k);
```

### <u>**Solution**</u>

<img src="../imgs/image-20210717183853438.png" alt="image-20210717183853438" style="width:67%;" />

这就是典型的回溯算法，`k` 限制了树的高度，`n` 限制了树的宽度，到达树的底部（即result.size==k）才加入results；

关键点在于要用 `startIdx` 参数排除已选择的数字。

```java
		List<List<Integer>> resultsOfCombine = new LinkedList<>();
    public List<List<Integer>> combine(int n, int k) {
        // if(n<=0 || k<=0)
        //     return null;
        LinkedList<Integer> result = new LinkedList<>();
        dfsCombine(n, k, 1, result);
        return resultsOfCombine;
    }
    public void dfsCombine(int n, int k, int startIdxOfN, LinkedList<Integer> result){
        // 到达树的底部(结束条件)
        if(result.size()==k){
            resultsOfCombine.add(new LinkedList(result));
            return;
        }
        for(int i=startIdxOfN; i<=n; i++){
            result.addLast(i);
            dfsCombine(n, k, i+1, result);
            result.removeLast();
        }
    }
```

## [剑指 Offer 12. 矩阵中的路径](https://leetcode-cn.com/problems/ju-zhen-zhong-de-lu-jing-lcof/)

给定一个 `m x n` 二维字符网格 `board` 和一个字符串单词 `word` 。如果 `word` 存在于网格中，返回 `true` ；否则，返回 `false` 。

单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母**不允许被重复使用**。

例如，在下面的 3×4 的矩阵中包含单词 "ABCCED"（单词中的字母已标出）。

<img src="imgs/word2.jpg" alt="img" style="width:15%;" />

**示例 1：**

```
输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
输出：true
```

**示例 2：**

```
输入：board = [["a","b"],["c","d"]], word = "abcd"
输出：false
```

**提示：**

- `1 <= board.length <= 200`
- `1 <= board[i].length <= 200`
- `board` 和 `word` 仅由大小写英文字母组成

函数签名

```java
public boolean exist(char[][] board, String word);
```

### Solution

O(MN)遍历矩阵中每个值找开头char相等的，dfs找剩下的

不能走回头路，用一个visited矩阵记录已访问的元素，（也可将board临时赋值成‘/’，用来标记已访问的元素，省下了bool[][] visited的空间，撤销选择的时候再把字符替换回去，省下O(MN)的空间）

```java
public boolean exist(char[][] board, String word){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                if(dfs(board, i, j, word, 0, new boolean[board.length][board[0].length]))
                    return true;
            }
        }
        return false;
    }
    public boolean dfs(char[][] board, int i, int j, String word, int curr, boolean[][] visited){
        if(i<0 || i>=board.length || j<0 || j>=board[0].length) return false;//数组越界
        if(board[i][j]!=word.charAt(curr)) return false;//char不相等
        if(visited[i][j]) return false;//已访问过，不可再次访问
        if(curr==word.length()-1) return true;//结束条件
        visited[i][j] = true;//防止重新访问
        boolean res = dfs(board, i+1, j, word, curr+1, visited) ||
                    dfs(board, i, j+1, word, curr+1, visited) ||
                    dfs(board, i-1, j, word, curr+1, visited) ||
                    dfs(board, i, j-1, word, curr+1, visited);
        if(!res)
            visited[i][j] = false;//撤销选择
        return res;
    }
```

- 时间复杂度 O(3^KMN)： 矩阵中共有 MN 个起点，时间复杂度为 O(MN) ; 最差情况下，需要遍历矩阵中长度为 K 字符串的所有方案，时间复杂度为 O(3^K):
    - 方案数计算： 设字符串长度为 K ，搜索中每个字符有上、下、左、右四个方向可以选择，舍弃回头（上个字符）的方向，剩下 3 种选择，因此方案数的复杂度为 O(3^K)。
- 空间复杂度 O(K)或O(MN) ： 
    - 搜索过程中的递归深度不超过 K ，因此系统因函数调用累计使用的栈空间占用 O(K) （因为函数返回后，系统调用的栈空间会释放）。
    - visited占用O(MN)，当然这是可以被替代的。board临时赋值成‘/’，用来标记已访问的元素，省下了bool[][] visited的空间，
    - 最坏情况下 K = MN ，递归深度为 MN ，此时系统栈使用 O(MN) 的额外空间。

## [剑指 Offer 13. 机器人的运动范围](https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/)

地上有一个m行n列的方格，从坐标 `[0,0]` 到坐标 `[m-1,n-1]` 。一个机器人从坐标 `[0, 0] `的格子开始移动，它每次可以向左、右、上、下移动一格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子。例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但它不能进入方格 [35, 38]，因为3+5+3+8=19。请问该机器人能够到达多少个格子？

**示例 1：**

```
输入：m = 2, n = 3, k = 1
输出：3
```

**示例 2：**

```
输入：m = 3, n = 1, k = 0
输出：1
```

**提示：**

- `1 <= n,m <= 100`
- `0 <= k <= 20`

函数签名

```java
public int movingCount(int m, int n, int k);
```

### 思路

dfs或bfs，从左上(0,0)出发往右下走，不走回头路(因为回头路没意义)，用visited记录已到达的位置。

注意 **不可用两个for循环遍历 m x n的方格**，因为会遍历到**不可达解**。

<img src="imgs/image-20210903140526418-0649127.png" alt="image-20210903140526418" style="width:60%;" />

### Solution - DFS

```java
public int movingCount(int m, int n, int k) {
        return dfs(0, 0, m, n, k, new boolean[m][n]);
    }
    public int dfs(int i, int j, int m, int n, int k, boolean[][] visited){
        if(i<0 || i>=m || j<0 || j>=n) return 0;//数组越界
        if(visited[i][j]) return 0;//因为是从左上向右下搜索，避免重复
        if(!isValid(i,j,k)) return 0;//是否大于k
        visited[i][j] = true;
        //往下走或往右走
        return 1 + dfs(i+1, j, m, n, k, visited) + dfs(i, j+1, m, n, k, visited);
    }
    public boolean isValid(int i, int j, int k){
        //百位 + 十位 + 个位
        return (i/100 + (i/10)%10 + i%10 + j/100 + (j/10)%10 + j%10) <= k;
    }
```

时间复杂度 O(MN) ： 最差情况下，机器人遍历矩阵所有单元格，此时时间复杂度为 O(MN) 。
空间复杂度 O(MN) ： visited 内存储矩阵所有单元格的索引，使用 O(MN)的额外空间。



## [LC51. N 皇后](https://leetcode-cn.com/problems/n-queens/)

n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击（皇后彼此不能相互攻击，也就是说：**任何两个皇后都不能处于同一条横行、纵行或斜线上**）。

给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案。

每一种解法包含一个不同的 n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。 

示例 1：

输入：n = 4

<img src="https://assets.leetcode.com/uploads/2020/11/13/queens.jpg" alt="img" style="width:67%;" />

输出：[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
解释：如上图所示，4 皇后问题存在两个不同的解法。
示例 2：

输入：n = 1
输出：[["Q"]]


提示：

1 <= n <= 9
函数签名：

```java
public List<List<String>> solveNQueens(int n);
```

### <u>**Solution**</u>

直接套框架

```java
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
        // 触发结束条件: board的取值为[0,n-1],当row=n时，已经取不到board[n]
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
```

## [LC37. 解数独](https://leetcode-cn.com/problems/sudoku-solver/)

编写一个程序，通过填充空格来解决数独问题。

数独的解法需 遵循如下规则：

数字 1-9 在每一行只能出现一次。
数字 1-9 在每一列只能出现一次。
数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。（请参考示例图）
数独部分空格内已填入了数字，空白格用 '.' 表示。

 

示例：

<img src="https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2021/04/12/250px-sudoku-by-l2g-20050714svg.png" alt="img" style="width:50%;" />

输入：board = [["5","3",".",".","7",".",".",".","."],

​						["6",".",".","1","9","5",".",".","."],

​						[".","9","8",".",".",".",".","6","."],

​						["8",".",".",".","6",".",".",".","3"],

​						["4",".",".","8",".","3",".",".","1"],

​						["7",".",".",".","2",".",".",".","6"],

​						[".","6",".",".",".",".","2","8","."],

​						[".",".",".","4","1","9",".",".","5"],

​						[".",".",".",".","8",".",".","7","9"]]
输出：[["5","3","4","6","7","8","9","1","2"],

​			["6","7","2","1","9","5","3","4","8"],

​			["1","9","8","3","4","2","5","6","7"],

​			["8","5","9","7","6","1","4","2","3"],

​			["4","2","6","8","5","3","7","9","1"],

​			["7","1","3","9","2","4","8","5","6"],

​			["9","6","1","5","3","7","2","8","4"],

​			["2","8","7","4","1","9","6","3","5"],

​			["3","4","5","2","8","6","1","7","9"]]
解释：输入的数独如上图所示，唯一有效的解决方案如下所示：

<img src="https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2021/04/12/250px-sudoku-by-l2g-20050714_solutionsvg.png" alt="img" style="width:50%;" />


提示：

board.length == 9
board[i].length == 9
board[i][j] 是一位数字或者 '.'
题目数据 保证 输入数独仅有一个解

函数签名

```java
public void solveSudoku(char[][] board);
输入是一个9x9的棋盘，空白格子用点号字符 . 表示，算法需要在原地修改棋盘，将空白格子填上数字，得到一个可行解。
```

### <u>**思路**</u>

同一行内，row不变，col++：从1到9逐一试`board[row][col]`；

**当** **`col`** **到达超过每一行的最后一个索引(`col==9`)时，转为增加** **`row`** **开始穷举下一行，并且在穷举之前添加一个判断(isValid())，跳过不满足条件的数字**

什么时候结束递归？**显然** **`row == 9`** **的时候就说明穷举完了最后一行，完成了所有的穷举，就是 base case**。

### **<u>Solution</u>**

```java
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
```

对于这种时间复杂度的计算，我们只能给出一个最坏情况，也就是 O(9^M)，其中 `M` 是棋盘中空着的格子数量。你想嘛，对每个空格子穷举 9 个数，结果就是指数级的。

## [LC698. 划分为k个相等的子集](https://leetcode-cn.com/problems/partition-to-k-equal-sum-subsets/)

给定一个整数数组  nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。

示例 1：

输入： nums = [4, 3, 2, 3, 5, 2, 1], k = 4
输出： True
说明： 有可能将其分成 4 个子集（5），（1,4），（2,3），（2,3）等于总和。

函数签名：

```java
public boolean canPartitionKSubsets(int[] nums, int k);
```

### <u>**思路**</u>

**算出总数除以k得出每个子集的元素和，回溯地将每个桶装满。**

1. **视角一，如果我们切换到这** **`n`** **个数字的视角，每个数字都要选择进入到** **`k`** **个桶中的某一个**。

    **复杂度**：n` 个数字，每个数字有 `k` 个桶可供选择，所以组合出的结果个数为 `k^n`，时间复杂度也就是 `**O(k^n)**

2. **视角二，如果我们切换到这** **`k`** **个桶的视角，对于每个桶，都要遍历** **`nums`** **中的** **`n`** **个数字，然后选择是否将当前遍历到的数字装进自己这个桶里**。

    **复杂度**：每个桶要遍历 `n` 个数字，选择「装入」或「不装入」，组合的结果有 `2^n` 种；而我们有 `k` 个桶，所以总的时间复杂度为 **`O(k*2^n)`**。

通俗来说，我们应该尽量「少量多次」，就是说宁可多做几次选择，也不要给太大的选择空间；宁可「**二选一」选 `k` 次**，也**不**要 **「`k` 选一」选一次**。

### <u>**Solution**</u>

视角二：对于每个桶选择nums中装入哪些数字

```java
		public boolean canPartitionKSubsets(int[] nums, int k) {
        if(k>nums.length)
            return false;
        
        int sum = 0;
        for(int x: nums)
            sum += x;
        if(sum%k != 0)
            return false;
        int bucketTargetSum = sum/k;

        boolean[] used = new boolean[nums.length];
        
        // k 号桶初始什么都没装，从 nums[0] 开始做选择
        return dfsPartitionKSubsets(k, 0, bucketTargetSum, nums, 0, used);
    }
    public boolean dfsPartitionKSubsets(int k, int bucketSum, int bucketTargetSum, int[] nums, int numsStartIdx, boolean[] used){
        if(k==0)
            // 所有桶都被装满了，而且 nums 一定全部用完了, 因为 target == sum / k
            return true;
        if(bucketSum==bucketTargetSum)
            // 装满了当前桶，递归穷举下一个桶的选择
            // 让下一个桶从 nums[0] 开始选数字
            return dfsPartitionKSubsets(k-1, 0, bucketTargetSum, nums, 0, used);
        // 从 start 开始向后探查有效的 nums[i] 装入当前桶
        for(int i=numsStartIdx; i<nums.length; i++){
            // 剪枝
            if(used[i])
                // nums[i] 已经被装入别的桶中
                continue;
            if(bucketSum+nums[i]>bucketTargetSum)
                // 当前桶装不下 nums[i]
                continue;
            // 做选择，将 nums[i] 装入当前桶中
            used[i] = true;
            bucketSum += nums[i];
            // 递归穷举下一个数字是否装入当前桶, 前面的数字都已经判断过，所以这里的numsStartIdx=i+1而不是i
            if(dfsPartitionKSubsets(k, bucketSum, bucketTargetSum, nums, i+1, used))
                return true;
            // 撤销选择
            bucketSum -= nums[i];
            used[i] = false;
        }
        // 穷举了所有数字，都无法装满当前桶
        return false;
    }
```

## [LC22. 括号生成](https://leetcode-cn.com/problems/generate-parentheses/)

数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 **有效(合法?)的** 括号组合。

示例 1：

输入：n = 3
输出：["((()))","(()())","(())()","()(())","()()()"]
示例 2：

输入：n = 1
输出：["()"]

函数签名：

```java
public List<String> generateParenthesis(int n);
```

### <u>**思路**</u>

有关括号问题，你只要记住以下性质，思路就很容易想出来：

**1、一个「合法」括号组合的左括号数量一定等于右括号数量**。

**2、对于一个「合法」的括号字符串组合** **`p`**，必然对于任何 **`0 <= i < len(p)`** **都有：子串** **`p[0..i]`** **中<u>左括号的数量都大于或等于右括号的数量</u>**。

算法输入一个整数 `n`，让你计算 **`n`** **对儿括号**能组成几种合法的括号组合，可以改写成如下问题：**现在有** **`2n`** **个位置，每个位置可以放置字符** **`(`** **或者** **`)`**，组成的所有括号组合中，有多少个是合法的**？

所以思路为：

1、得到全部 `2^(2n)` 种组合；

2、根据我们刚才总结出的**合法**括号组合的性质**筛选出合法的组合**：不是简单的记录穷举位置 `i`，而是**用** **`left`** **记录还可以使用多少个左括号，用** **`right`** **记录还可以使用多少个右括号**，两者初始值都为n，且**left<right**(因为括号都是先左后右), **left和right都>=0**；

3、 当left和right都==0时，满足结束条件，可加入结果集

### <u>**Solution**</u>

```java
		LinkedList<String> resultsOfParenthesis = new LinkedList<>();
    public List<String> generateParenthesis(int n) {
        // 回溯过程中的路径
        StringBuilder result = new StringBuilder();
        // 可用的左括号和右括号数量初始化为 n
        dfsGenerateParenthesis(n, n, result);
        return resultsOfParenthesis;
    }
    // 可用的左括号数量为 left 个，可用的右括号数量为 rgiht 个
    public void dfsGenerateParenthesis(int left, int right, StringBuilder result){
        //不合法条件
        if(left>right)// 若左括号剩下的多，说明不合法
            return;
        if(left<0 || right<0)// 数量小于 0 肯定是不合法的
            return;
        //合法条件
        if(left==0 && right==0){// 当所有括号都恰好用完时，得到一个合法的括号组合
            resultsOfParenthesis.add(result.toString());
            return;
        }

        // 尝试放一个左括号
        result.append("(");// 选择
        dfsGenerateParenthesis(left-1, right, result);
        //StringBuilder的length有括号，array的没有
        result.deleteCharAt(result.length()-1);// 撤消选择

        result.append(")");// 选择
        dfsGenerateParenthesis(left, right-1, result);
        //StringBuilder的length有括号，array的没有
        result.deleteCharAt(result.length()-1);// 撤消选择
    }
```

**对于** **`backtrack`** **函数，状态有三个，分别是** **`left, right, result`**，这三个变量的所有组合个数就是 `dfs` 函数的状态个数（调用次数）。

`left` 和 `right` 的组合好办，他俩取值就是 0~n 嘛，组合起来也就 `n^2` 种而已；这个 result 的长度虽然取在 0~2n，但对于每一个长度，它还有指数级的括号组合，这个是不好算的。

说了这么多，就是想让大家知道这个算法的复杂度是**指数级**，而且不好算，这里就不具体展开了，是 (4^n)/sqrt(n)

## [LC200. 岛屿数量 相关题目还有463 695 827](https://leetcode-cn.com/problems/number-of-islands/)

给你一个由 `'1'`（陆地）和 `'0'`（水）组成的的二维网格，请你计算网格中岛屿的数量。

岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。

此外，你可以假设该网格的四条边均被水包围。

**示例 1：**

```
输入：grid = [
  ["1","1","1","1","0"],
  ["1","1","0","1","0"],
  ["1","1","0","0","0"],
  ["0","0","0","0","0"]
]
输出：1
```

**示例 2：**

```
输入：grid = [
  ["1","1","0","0","0"],
  ["1","1","0","0","0"],
  ["0","0","1","0","0"],
  ["0","0","0","1","1"]
]
输出：3
```

**提示：**

- `m == grid.length`
- `n == grid[i].length`
- `1 <= m, n <= 300`
- `grid[i][j]` 的值为 `'0'` 或 `'1'`

函数签名

```java
public int numIslands(char[][] grid);
```

### 思路

dfs或bfs;https://leetcode-cn.com/problems/number-of-islands/solution/dao-yu-shu-liang-by-leetcode/

dfs:

为了求出岛屿的数量，我们可以扫描整个二维网格。如果一个位置为 11，则以其为起始节点开始进行深度优先搜索。在深度优先搜索的过程中，每个搜索到的 11 都会被重新标记为 00。

最终岛屿的数量就是我们进行深度优先搜索的次数。

### Solution

```java
class Solution {
    public int numIslands(char[][] grid) {
        int count = 0;
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[0].length; j++){
                if(grid[i][j]=='1'){
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }
    public void dfs(char[][] grid, int i, int j){
        if(i<0 || i>=grid.length || j<0 || j>=grid[0].length) return;
        if(grid[i][j]=='0') return;

        grid[i][j] = '0';
        dfs(grid, i+1, j);
        dfs(grid, i, j+1);
        dfs(grid, i-1, j);
        dfs(grid, i, j-1);
    }
}
```

时间复杂度：O(MN)，其中 M 和 N 分别为行数和列数。

空间复杂度：O(MN)，在最坏情况下，整个网格均为陆地，深度优先搜索的深度达到 MN。

