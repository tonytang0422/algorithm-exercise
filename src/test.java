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
    public int findMagicIndex(int[] nums) {
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

}

public class test {
    public static void main(String[] args){
        int n, x;
        Solution test = new Solution();
        x = 10;
        n = test.integerBreak(x);
        System.out.println(n);
    }
}