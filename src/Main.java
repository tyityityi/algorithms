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
        int total = sc.nextInt();
        for(int i=0; i<total; i++){
            String s = sc.nextLine();
            isValid(s);
        }
    }
    private static void isValid(String s){
        //首字符
        if(!('A'<=s.charAt(0) && s.charAt(0)<='Z')&&
                ('a'<=s.charAt(0) && s.charAt(0)<='z')){
            System.out.println("Wrong");
            return;
        }
        boolean hasChar = false, hasDigi = false;
        for(int j=0; j<s.length(); j++){
            if(('A'<=s.charAt(j) && s.charAt(j)<='Z') &&
                    ('a'<=s.charAt(j) && s.charAt(j)<='z')){
                hasChar = true;
            } else if('0'<=s.charAt(j) && s.charAt(j)<='9'){
                hasDigi = true;
            } else {
                System.out.println("Wrong");
                return;
            }
        }
        if(hasChar && hasDigi){
            System.out.println("Accept");
            return;
        } else {
            System.out.println("Wrong");
            return;
        }

    }
}
