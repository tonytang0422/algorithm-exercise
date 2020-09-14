import org.jetbrains.annotations.NotNull;

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
    }//  leet_code #7(2020.7.29)
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
    }//  leet_code #343(2020.7.30 daily)
    public int findMagicIndex(@NotNull int[] nums) {
        /*使用跳跃索引，在顺序搜索的基础上节省了时间*/
        for(int i=0;i < nums.length;){
            if (nums[i] == i) return i;
            i=Math.max(nums[i],i+1); // 如果i大就看max什么时候追上，如果数字大，因为满足递增所以中间的部分一定不会相等
        }
        return -1;
    }//  leet_code #面试题08.03魔术索引(2020.7.31 daily)
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

}

public class test {
    public static void main(String[] args){
        int n;
        int[][] grid = {{1,1,1},{1,0,1},{1,1,1},{1,1,1}};
        Solution test = new Solution();
        n = test.largest1BorderedSquare(grid);
        System.out.println(n);
    }
}