//懒加载+双重校验锁的单例模式
public class Singleton {
    private static volatile Singleton uniqueInstance;
    private Singleton(){

    }

    public Singleton getUniqueInstance(){
        if(uniqueInstance==null){
            synchronized (this){
                if (uniqueInstance==null){
                    Singleton singleton = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
