# DFS 回溯算法

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

## [LC46. 全排列](https://leetcode-cn.com/problems/permutations/)

给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。

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

<img src="imgs/image-20210716142708055.png" alt="image-20210716142708055" style="width:80%;" />

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

<img src="imgs/image-20210717180407833.png" alt="image-20210717180407833" style="width:50%;" />

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

<img src="imgs/image-20210717183853438.png" alt="image-20210717183853438" style="width:67%;" />

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



## [LC51. N 皇后](https://leetcode-cn.com/problems/n-queens/)

n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击（皇后彼此不能相互攻击，也就是说：任何两个皇后都不能处于同一条横行、纵行或斜线上）。

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
            // 递归穷举下一个数字是否装入当前桶, 前面的数字都已经判断过，所以这里的numsStartIdx=i+1而不是0
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

数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。

 

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

2、根据我们刚才总结出的**合法**括号组合的性质**筛选出合法的组合**：不是简单的记录穷举位置 `i`，而是**用** **`left`** **记录还可以使用多少个左括号，用** **`right`** **记录还可以使用多少个右括号**，两者初始值都为n，且**left<right**(因为括号都是先左后右), l**eft和right都>=0**；

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

**对于** **`backtrack`** **函数，状态有三个，分别是** **`left, right, track`**，这三个变量的所有组合个数就是 `backtrack` 函数的状态个数（调用次数）。

`left` 和 `right` 的组合好办，他俩取值就是 0~n 嘛，组合起来也就 `n^2` 种而已；这个 `track` 的长度虽然取在 0~2n，但对于每一个长度，它还有指数级的括号组合，这个是不好算的。

说了这么多，就是想让大家知道这个算法的复杂度是**指数级**，而且不好算，这里就不具体展开了，是 $\frac{4^{n}}{\sqrt{n}}$