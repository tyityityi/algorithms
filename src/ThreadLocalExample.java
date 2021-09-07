import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadLocalExample implements Runnable{

    private static final ThreadLocal threadNo = new ThreadLocal(){
        protected String initialValue(){
            return "这是重写initial value后的default initial value";
        }
    };

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalExample obj = new ThreadLocalExample();
        for(int i=0 ; i<3; i++){
            Thread t = new Thread(obj, ""+i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    @Override
    public void run() {
        System.out.println("Thread "+Thread.currentThread().getName()+": "+threadNo.get());
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //threadNo会为每个线程提供独立的本地空间
        threadNo.set("这是Thread "+Thread.currentThread().getName()+"修改后的value");

        System.out.println("Thread "+Thread.currentThread().getName()+": "+threadNo.get());
    }

}