import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import static java.util.Collections.swap;

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
    @Contract("null -> null")
    private TreeNode invertTree(TreeNode root) {
        /*遍历二叉树的同时交换左右子树（采用先序遍历）*/
        TreeNode node;
        if (root == null) return null;
        node = root.right;
        root.right = invertTree(root.left);
        root.left = invertTree(node);
        return root;
    } // leet_code #226
    int[] findRedundantDirectedConnection(@NotNull int[][] edges) {
        /*加上一个节点之后不构成树的情况有两种
        * a.形成了环
        * b.有一个节点存在两个父节点
        * 只满足a的话删除环上最后一条边
        * 只满足b的话删除后面的一条边
        * 同时满足a和b的话删除环上指向重复子节点的边
        */
        List<Integer> father = new ArrayList<>(1000);
        List<Integer> son = new ArrayList<>(1000);
        List<Integer> loop = new ArrayList<>(1000);
        int front, back, findloop;
        boolean secondSon = false;
        int[] ans = new int[2];
        for (int i=0; i<edges.length; i++){
            front = edges[i][0];
            back = edges[i][1];
            if (son.contains(front) && father.contains(back)){
                if (!secondSon) ans = edges[i];
                loop.add(front);
                loop.add(back);
                int j=0;
                int jfront, jback;
                findloop = front;
                while (true){
                    jfront = edges[j][0];
                    jback = edges[j][1];
                    if (jback == findloop){
                        findloop = jfront;
                        if (findloop == back){
                            break;
                        }
                        else {
                            loop.add(findloop);
                            j = 0;
                        }
                    }
                    else {
                        j++;
                        if (j>i) {
                            break;
                        } // 判断是不是没找到环
                    }
                } // 寻找闭环元素
                if (j>i) {
                    if (son.contains(back)){
                        ans = edges[i];
                        secondSon = true;
                    }
                    father.add(front);
                    son.add(back);
                    loop.clear();
                    continue;
                } // 没找到环
                for (int[] edge : edges) {
                    front = edge[0];
                    back = edge[1];
                    if (!loop.contains(front) && loop.contains(back)) {
                        ans = edge;
                        break;
                    }
                }
                for (int[] edge : edges) {
                    front = edge[0];
                    back = edge[1];
                    if (loop.contains(front) && back == ans[1]) return edge;
                }
                break;
            }
            else if (son.contains(back)){
                ans = edges[i];
                secondSon = true;
            }
            else{
                father.add(front);
                son.add(back);
            }
        }
        return ans;
    } // leet_code #685
    private void backtrack47(List<List<Integer>> ans, List<Integer> output, int n, int begin){
        if (begin == n && !ans.contains(output)){
            ans.add(new ArrayList<Integer>(output));
        }
        for (int i = begin; i < n; i++){
            if (begin!=i && output.get(i).equals(output.get(begin))) continue;
            swap(output, begin, i);
            backtrack47(ans, output, n, begin+1);
            swap(output, begin, i);
        }
    } // leet_code #47
    List<List<Integer>> permuteUnique(@NotNull int[] nums) {
        /*用递归的方法，先让output与输入值相同，然后依次交换各个位置与之后每个位置的值（非最优解）*/
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> output = new ArrayList<>();
        for (int num : nums){
            output.add(num);
        }
        int n = nums.length;
        backtrack47(ans, output, n, 0);
        return ans;
    } // leet_code #47
    private int ans404 = 0; // leet_code #404
    private void LDR(TreeNode root){
        if (root == null) return;
        LDR (root.left);
        if (root.left!=null && root.left.left==null && root.left.right==null) ans404 += root.left.val;
        LDR (root.right);
    } // leet_code #404
    public int sumOfLeftLeaves(TreeNode root) {
        /*遍历二叉树，使用全局变量的问题要如何解决需要思考一下*/
        LDR(root);
        return ans404;
    } // leet_code #404
    private void backtrack78(List<List<Integer>> ans, List<Integer> output, List<Integer> output0, int n, int i){
        if(i == n) {
            ans.add(new ArrayList<Integer>(output));
            return;
        }
        output.add(output0.get(i));
        backtrack78(ans, output, output0, n, i+1);
        output.remove(output0.get(i));
        backtrack78(ans, output, output0, n, i+1);
    } // leet_code #78
    public List<List<Integer>> subsets(@NotNull int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> output = new ArrayList<>(1000);
        List<Integer> output0 = new ArrayList<>();
        for (int num : nums){
            output0.add(num);
        }
        int n = nums.length;
        backtrack78(ans, output, output0, n, 0);
        return ans;
    } // leet_code #78
}

public class test {
    public static void main(String[] args){
        List<List<Integer>> n;
        int[] x;
        Solution test = new Solution();
        x = new int[]{1, 1, 2};
        n = test.permuteUnique(x);
        System.out.print('[');
        for (List<Integer> i : n){
            System.out.print('[');
            for (int j : i ) System.out.print(j + ", ");
            System.out.println(']');
        }
        System.out.print(']');
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    @Contract(pure = true)
    TreeNode(int x) {val = x;}
} // 二叉树结构 leet_code #94 #226 #404