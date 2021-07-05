package algorithms;

public class recursive {

    /**
     * n的阶乘
     * 规律：n!=1×2×3×...×(n-1)×n
     * 出口：n==1或n==0 return 1
     *
     * @param
     * @return
     */
    public static void testFactorized(int n){
        System.out.println(factorized(n));
    }

    private static int factorized(int n){
        if(n==0||n==1) return 1;
        return n*factorized(n-1);

    }

    /**
     * 斐波那契数 求第n个数字的值
     * 1，1，2，3，5，8，13，21，34
     * 1  2  3 4  5  6  7  8   9
     * 规律：n = (n-1)+(n-2)
     * 出口：n==1,n==2时 return 1
     * @param
     * @return
     */
    public static void testFib(int n){
        System.out.println(fib(n));
    }

    private static int fib(int n){
        if(n==1 || n==2) return 1;
        return fib(n-1)+fib(n-2);
    }
}
