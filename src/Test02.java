import java.io.*;
import java.util.*;
public class Test02 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(new BufferedInputStream(System.in));
        int t = sc.nextInt();
        for(int i=0; i<t; i++){
            int n = sc.nextInt();
            if(n==0){
                System.out.println(0);
                continue;
            }
            sc.nextLine();
            String s = sc.nextLine();
            char[] ch = s.toCharArray();

            Stack<Character> stack = new Stack<>();
            for(int j=0; j<ch.length; j++){
                if(ch[j]=='(')
                    stack.push('(');
                if(ch[j]==')' && !stack.isEmpty())
                    stack.pop();
            }
            System.out.println(stack.size());
        }
    }
}
