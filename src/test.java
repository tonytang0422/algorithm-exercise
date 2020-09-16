import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Solution {
    public int reverse(int x) {
    /* 排除-2147483648的结果之后取数字的绝对值，flag表示正负
    * 然后使用字符串得到数字长度以及倒过来的数字并进行判断，如果满足范围就转回数字*/
        boolean flag = false;
        int length, revNum;
        String num , revString;
        StringBuilder rev = new StringBuilder("");
        if (x < 0){
            flag = true;
            if (x == -2147483648) return 0;
            else x = -x;
        }
        num = Integer.toString(x);
        length = num.length();
        for (int i = 0;i < length;i++){
            rev.append(num.charAt(length - i - 1));
        }
        revString = rev.toString();
        if (length == 10 && (revString.compareTo("2147483648") > 0)){
            return 0;
        }
        revNum = Integer.parseInt(revString);
        if (flag) revNum = -revNum;
        return revNum;
    } //  leet_code #7(2020.7.29)
    public int integerBreak(int n) {
    /*按照分解份数来进行循环，分解份数最多取n/2，此时分解的结果都是2*/
        if (n == 2 || n == 3) return n-1;
        if (n == 4) return n;
        int divide, quotient, remainder, ans;
        int maxAns = 1;
        for (divide=2;divide<=n/2;divide++){
            ans = 1;
            quotient = n / divide;
            remainder = n % divide;
            ans = ans * (int)Math.pow(quotient+1, remainder) * (int)Math.pow(quotient, divide-remainder);
            maxAns = Math.max(maxAns, ans);
        }
        return maxAns;
    } //  leet_code #343(2020.7.30 daily)
    public int findMagicIndex(@NotNull int[] nums) {
        /*使用跳跃索引，在顺序搜索的基础上节省了时间*/
        for(int i=0;i < nums.length;){
            if (nums[i] == i) return i;
            i=Math.max(nums[i],i+1); // 如果i大就看max什么时候追上，如果数字大，因为满足递增所以中间的部分一定不会相等
        }
        return -1;
    } //  leet_code #面试题08.03魔术索引(2020.7.31 daily)
    public int numTrees(int n) {
        /*动态规划练习，左子树情况数乘以右子树情况数*/
        int[] dp = new int[n+1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i=2;i<=n;i++){
            for(int j=1;j<=i;j++){
                dp[i] += dp[j-1] * dp[i-j];
            }
        }
        return dp[n];
    } //  leet_code #96
    public int largest1BorderedSquare(@NotNull int[][] grid) {
        /*dp练习，我的解法偏死算，可以参考Ripple的思路，Ripple的思路附在本题下面*/
        int length = grid.length;
        int width = grid[0].length;
        int elementLength = 0;
        int countLength;
        for (int col = 0; col < width; col++){
            for (int row = 0; row < length; row++){
                if (grid[row][col]==0) continue;
                countLength = Math.min(length-row, width-col);
                if (countLength < elementLength) continue;
                for (int i = countLength; i >= 1; i--){
                    boolean flag=true;
                    for (int j=0; j < i; j++){
                        if (grid[row+j][col]==0||grid[row][col+j]==0||grid[row+i-1-j][col+i-1]==0||
                                grid[row+i-1][col+i-1-j]==0){
                            flag=false;
                            break;
                        }
                    }
                    if (flag) elementLength=Math.max(elementLength, i);
                }
            }
        }
        return elementLength*elementLength;
    } //  leet_code #1139
    public int largest1BorderedSquare1(@NotNull int[][] grid) {
        /*Ripple的解法，使用dp思想*/
        int m = grid.length, n = grid[0].length;
        int[][][] dp = new int[m+1][n+1][2];
        // 0 up 1 left;
        int ans = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (grid[i-1][j-1] == 0) continue; // 如果是0则不要继续了
                dp[i][j][0] = dp[i-1][j][0] + 1; // 求出up情况下连续的个数
                dp[i][j][1] = dp[i][j-1][1] + 1; // 求出left情况下连续的个数
                int min = Math.min(dp[i][j][0], dp[i][j][1]); // 拿出两者较小的长度，因为四条边都要相等。
                for (int k = 0; k < min; k++) {//拿出后并不一定就是min这个长度，有可能另外两条边比较短，没有min长，所以要一个一个判断。
                    // 判断另外的两条边是否都比当前长度大。
                    if (dp[i-k][j][1] >= k + 1 && dp[i][j-k][0] >= k + 1) ans = Math.max(ans, k + 1);
                }
            }
        }
        return ans * ans;
    } //  leet_code #1139 解法参考
    public List<Integer> inorderTraversal(TreeNode root) {
        /*使用栈递归解决*/
        List<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        while (root!=null || !stack.isEmpty()){
            while(root != null){
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            list.add(root.val);
            root = root.right;
        }
        return list;
    } // leet_code #94
    private boolean dfs(@NotNull char[][] board, boolean[][] row, boolean[][] col, boolean[][] block, int i, int j){
        /*给数组打上标签，对于空格来说先递归任意填入符合条件的数字，如果之后验证下来不符合条件，再回溯回来取消相应的标签*/
        while(board[i][j] != '.'){
            if(++j >= 9){
                i++;
                j = 0;
            }
            if(i >= 9){
                return true;
            }
        }
        for(int num=0; num<9; num++){
            int blockIndex = i / 3 * 3 + j / 3;
            if (!row[i][num] && !col[j][num] && !block[blockIndex][num]){
                board[i][j] = (char)('1'+num);
                row[i][num] = true;
                col[j][num] = true;
                block[blockIndex][num] = true;
                if (dfs(board, row, col, block, i, j)) {
                    return true;
                } else {
                    row[i][num] = false;
                    col[j][num] = false;
                    block[blockIndex][num] = false;
                    board[i][j] = '.';
                }
            }
        }
        return false;
    } // leet_code #37
    public void solveSudoku(char[][] board) {
        /*创建3个数组分别表示行，列，每个九宫格的数字情况，i/3*3即抹去了除以3的余数*/
        boolean[][] row = new boolean[9][9];
        boolean[][] col = new boolean[9][9];
        boolean[][] block = new boolean[9][9];
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(board[i][j] != '.'){
                    int num = board[i][j] - '1';
                    row[i][num] = true;
                    col[j][num] = true;
                    block[i/3*3+j/3][num] = true;
                }
            }
        }
        dfs(board, row, col, block, 0, 0);
    } // leet_code #37
    public TreeNode invertTree(TreeNode root) {
        /*遍历二叉树的同时交换左右子树（采用先序遍历）*/
        TreeNode node;
        if (root == null) return null;
        node = root.right;
        root.right = invertTree(root.left);
        root.left = invertTree(node);
        return root;
    } // leet_code #226
}

public class test {
    private static void printBoard(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args){
        char[][] board = new char[][]{
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        Solution solution = new Solution();
        test.printBoard(board);
        System.out.println("-----");
        solution.solveSudoku(board);
        test.printBoard(board);
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    @Contract(pure = true)
    TreeNode(int x) {val = x;}
} // 树结构 leet_code #94 #226