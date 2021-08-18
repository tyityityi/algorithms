import java.io.BufferedInputStream;
import java.util.*;

public class Main {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(new BufferedInputStream(System.in));
//        while(sc.hasNext()){
//            System.out.println(sc.nextInt());
//            //跳到下一行
//            sc.nextLine();
//            //String转Int[]
//            int[] arr=Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
//            for(int i=0; i<arr.length; i++){
//                System.out.print(arr[i]+" ");
//            }
//        }
        Scanner sc = new Scanner(new BufferedInputStream(System.in));
        while(sc.hasNext()){
            sc.nextInt();
            //跳到下一行
            sc.nextLine();
            //String转Int[]
            int[] arr=Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            Arrays.sort(arr);
        }
    }
}
