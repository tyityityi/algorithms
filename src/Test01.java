import java.util.*;
import java.io.*;

public class Test01 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(new BufferedInputStream(System.in));
        int t = sc.nextInt();
        for(int i=0; i<t; i++){
            int n = sc.nextInt();
            if(n%2==0)
                System.out.println(n/2);
            else
                System.out.println((n+1)/2);
        }
    }
}

