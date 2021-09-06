import java.util.*;
import java.io.*;

public class Test03 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(new BufferedInputStream(System.in));
        String[] nkm = sc.nextLine().split(" ");
        int n = Integer.parseInt(nkm[0]);
        long k = Integer.parseInt(nkm[1]);
        long m = Integer.parseInt(nkm[2]);
        String[] wStr = sc.nextLine().split(" ");
        Long[] w = new Long[n];
        for(int i=0; i<n; i++){
            w[i] = Long.parseLong(wStr[i]);
        }
        Arrays.sort(w, (o1,o2)->(int)(o2-o1));
        System.out.println(solve(n, k, m, w));
    }
    private static long solve(int n, long k, long m, Long[] w){
        long ans = 0;
        if(k==0 || w[1]<m){//惩罚为0或只有一个数大于阈值,全部都能取到
            for(int i=0; i<n; i++){
                ans += w[i];
            }
            return ans;
        }

        List<Long> bigW = new ArrayList<>();
        List<Long> smallW = new ArrayList<>();
        for(int i=0; i<n; i++){
            if(w[i]>m)
                bigW.add(w[i]);
            else
                smallW.add(w[i]);
        }
        //最后一个取最大的
        ans += bigW.get(0);
        bigW.remove(0);
        n--;
        //取mod个small中最大的元素将最后一组变成[小,小..大]
        long mod = n%(k+1);
        while(mod>0 && smallW.size()>0){
            ans += smallW.get(0);
            smallW.remove(0);
            mod--;
        }
        //k+1个为一组，n=10-1,k=2时，取三次（1、4、7，第10个就能取超过阈值的数），比较k+1(或者剩余的全部，取小)个small和1个big，谁大取谁
        for(int i=0; i<n/(k+1); i++){
            long big = 0l;
            long small = 0l;
            if(!bigW.isEmpty()) big = bigW.get(0);
            if(!smallW.isEmpty()){
                for(int j=0; j<Math.min(smallW.size(),k+1); j++){
                    small += smallW.get(j);
                }
            }

            if(big > small){
                ans += big;
                bigW.remove(0);
            } else {
                ans += small;
                for(int j=0; j<Math.min(smallW.size(),k+1); j++){
                    smallW.remove(0);
                }
            }
        }
        return ans;
    }
}
