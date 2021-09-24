//https://www.cnblogs.com/zzq-include/p/13527151.html
//wait notify notifyall https://www.jianshu.com/p/25e243850bd2?appinstall=0
//https://blog.csdn.net/meism5/article/details/90238268
public class ThreeThreadsPrint0to10 {
    volatile int i = 0;

    public static void main(String[] args) {
        ThreeThreadsPrint0to10 threeThreadsPrint0to10 = new ThreeThreadsPrint0to10();
        int[] a = new int[]{1,2,3,4,5,6,7,8,9,10};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(true){
                    synchronized (this){
                        notifyAll();
                        if(threeThreadsPrint0to10.i >= a.length) {
                            System.out.println("线程"+Thread.currentThread().getName()+"return");
                            //notifyAll();
                            return;
                        }
                        int currThreadNo = Integer.parseInt(Thread.currentThread().getName());
                        if(threeThreadsPrint0to10.i % 5 == currThreadNo){
                            System.out.println("线程"+Thread.currentThread().getName()+": "+a[threeThreadsPrint0to10.i]);
                            threeThreadsPrint0to10.i++;
                        }
                        if(threeThreadsPrint0to10.i >= a.length){
                            System.out.println("线程"+Thread.currentThread().getName()+"return");
                            //notifyAll();
                            return;
                        } else {
                            try {
                                //notifyAll();//将剩余线程唤醒进入runnable状态
                                wait();//阻塞自己,让处于runnable状态的线程竞争锁
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
        new Thread(runnable, "0").start();
        new Thread(runnable, "1").start();
        new Thread(runnable, "2").start();
        new Thread(runnable, "3").start();
        new Thread(runnable, "4").start();


    }
}
