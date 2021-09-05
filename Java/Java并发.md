# 线程和进程

## 进程

进程是程序的一次执行过程，是系统运行程序的基本单位，因此进程是动态的。系统运行一个程序即是一个进程从创建，运行到消亡的过程。

在 Java 中，当我们启动 main 函数时其实就是启动了一个 JVM 的进程，而 main 函数所在的线程就是这个进程中的一个线程，也称主线程。

## 线程

线程与进程相似，但线程是一个比进程更小的执行单位。

Java 程序天生就是多线程程序，我们可以通过 JMX 来看一下一个普通的 Java 程序有哪些线程，代码如下。

```java
public class MultiThread {
    public static void main(String[] args) {
        // 获取 Java 线程管理 MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程 ID 和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
          	System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }
}
```

上述程序输出如下（输出内容可能不同，不用太纠结下面每个线程的作用，只用知道 main 线程执行 main 方法即可）：

```java
[5] Attach Listener //添加事件
[4] Signal Dispatcher // 分发处理给 JVM 信号的线程
[3] Finalizer //调用对象 finalize 方法的线程
[2] Reference Handler //清除 reference 线程
[1] main //main 主线程,程序入口
```

从上面的输出内容可以看出：**一个 Java 程序的运行是 main 线程和多个其他线程同时运行**。

### 线程与进程的区别

一个进程在其执行的过程中可以产生多个线程。与进程不同的是同类的多个**线程共享进程**的**堆**和**方法区(JDK1.8 之后的元空间)**资源，但每个线程有自己的**程序计数器**、**虚拟机栈**和**本地方法栈**，**线程和进程最大的不同在于基本上各进程是独立的，而各线程则不一定，因为同一进程中的线程极有可能会相互影响。线程执行开销小，但不利于资源的管理和保护；而进程正相反。**

### 线程私有的资源

#### 程序计数器

程序计数器私有主要是为了**线程切换后能恢复到正确的执行位置**。

- **程序计数器**：
    1. 字节码解释器通过改变程序计数器来依次读取指令，从而实现**代码的流程控制**，如：顺序执行、选择、循环、异常处理；
    2. 在多线程的情况下，**程序计数器用于记录当前线程执行的位置**，从而**当线程被切换回来的时候能够知道该线程上次运行到哪**儿了。

需要注意的是，如果执行的是 native 方法，那么程序计数器记录的是 undefined 地址，只有执行的是 Java 代码时程序计数器记录的才是下一条指令的地址。

#### 虚拟机栈和本地方法栈

为了**保证线程中的局部变量不被别的线程访问到**，虚拟机栈和本地方法栈是线程私有的。

- **虚拟机栈：** **每个 Java 方法**在执行的同时会创建一个**栈帧**用于存储**局部变量表、操作数栈、常量池引用**等信息。从**方法调用**直至执行完成的过程，就对应着一个栈帧在 Java 虚拟机栈中**入栈和出栈**的过程。
- **本地方法栈：** 和虚拟机栈所发挥的作用非常相似，区别是： **虚拟机栈为虚拟机执行 Java 方法 （也就是字节码）服务，而本地方法栈则为虚拟机使用到的 Native 方法服务。** 在 HotSpot 虚拟机中和 Java 虚拟机栈合二为一。

### 线程共享的资源

#### 堆和方法区

**堆和方法区是所有线程共享的资源**，其中**堆**是**进程中**最大的一块**内存**，主要用于存放**新创建的对象** (几乎所有对象都在这里分配内存)，**方法区**主要用于存放**已被加载的类信息、常量、静态变量、即时编译器编译后的代码**等数据。

# 并发（多线程）

## 并发与并行的区别

- **并发：** 同一时间段，多个任务都在(**交替)执行** (单位时间内不一定同时执行)；
- **并行：** 单位时间内，多个任务**同时**执行。

## 为什么要使用多线程

先从总体上来说：

- **从计算机底层来说：** 线程可以比作是轻量级的进程，是程序执行的最小单位,**线程间的切换和调度的成本远远小于进程**。另外，多核 CPU 时代意味着多个线程可以**同时运行**，这减少了线程上下文切换的开销。
- **从当代互联网发展趋势来说：** 现在的系统动不动就要求百万级甚至千万级的并发量，而多线程并发编程正是开发高并发系统的基础，利用好多线程机制可以大大提高系统整体的并发能力以及性能。

再深入到计算机底层来探讨：

- **单核时代**： 在单核时代多线程主要是为了提高单进程利用 CPU 和 IO 系统的效率。 假设只运行了一个 Java 进程的情况，当我们请求 IO 的时候，如果 Java 进程中只有一个线程，此线程被 IO 阻塞则整个进程被阻塞。CPU 和 IO 设备只有一个在运行，那么可以简单地说系统整体效率只有 50%。**当使用多线程的时候，一个线程被 IO 阻塞，其他线程还可以继续使用 CPU**。从而提高了 Java 进程利用系统资源的整体效率。
- **多核时代**: 多核时代多线程主要是为了提高进程利用多核 CPU 的能力。举个例子：假如我们要计算一个复杂的任务，我们只用一个线程的话，不论系统有几个 CPU 核心，都只会有一个 CPU 核心被利用到。而创建多个线程，这些**线程可以被映射到底层多个 CPU 上执行**，在任务中的多个线程没有资源竞争的情况下，任务执行的效率会有显著性的提高，约等于（单核时执行时间/CPU 核心数）。

## 多线程带来的问题

**内存泄漏、死锁、线程不安全**等等。

## 线程的生命周期和状态?

### 状态

Java 线程在运行的**生命周期**中的指定时刻只可能处于下面 6 种不同状态的其中一个**状态**

<img src="imgs/image-20210902181922497.png" alt="image-20210902181922497" style="width:80%;" />

### 生命周期

线程在生命周期中会在**不同状态之间切换**。Java 线程状态变迁如下图：

<img src="imgs/Java+%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81%E5%8F%98%E8%BF%81.png" alt="Java 线程状态变迁 " style="width:67%;" />

> 原图中 wait 到 runnable 状态的转换中，`join`实际上是`Thread`类的方法，但这里写成了`Object`

- 线程创建之后它将处于 **NEW（新建）** 状态，调用 `start()` 方法后开始运行，线程这时候处于 **READY（可运行）** 状态。可运行状态的线程获得了 CPU 时间片（timeslice）后就处于 **RUNNING（运行）** 状态；

> 在操作系统中层面线程有 READY 和 RUNNING 状态，而在 JVM 层面只能看到 RUNNABLE 状态，所以 Java 系统一般将这两个状态统称为 **RUNNABLE（运行中）** 状态 。JVM不区分这两种状态是因为现在的**时分**（time-sharing）**多任务**（multi-task）**操作系统**架构通常都是用所谓的“**时间分片**（time quantum or time slice）”方式进行**抢占式**（preemptive）轮转调度（round-robin式），一个线程一次最多只能在 CPU 上运行比如 10-20ms 的时间，时间片用后就要被切换下来放入调度队列的末尾等待再次调度。线程切换很快，这两种状态没有本质的区别。

- 当线程执行 `wait()`方法之后，线程进入 **WAITING（等待）** 状态。进入等待状态的线程需要依靠其他线程的**通知notify**才能够返回到运行状态；

- **TIME_WAITING(超时等待)** 状态相当于在等待状态的基础上增加了**超时限制**，比如通过 `sleep（long millis）`方法或 `wait（long millis）`方法可以将 Java 线程置于 TIMED WAITING 状态。当超时时间到达后 Java 线程将会返回到 RUNNABLE 状态。
- 当线程调用同步方法时，在**没有获取到锁**的情况下，线程将会进入到 **BLOCKED（阻塞）** 状态。

- 线程在执行 Runnable 的`run()`方法之后将会进入到 **TERMINATED（终止）** 状态。

### sleep() 方法和 wait() 方法区别和共同点

**共同点**：两者都可以**暂停线程的执行**。

**区别**：

- 两者最主要的区别在于：**`sleep()` 方法没有释放锁，而 `wait()` 方法释放了锁** 。
- `wait()` 通常被用于线程间交互/通信；`sleep() `通常被用于暂停执行。
- `wait()` 方法被调用后，**线程不会自动苏醒**(但也可以使用 `wait(long timeout)` **超时后线程会自动苏醒**)，需要**别的线程**调用**同一个对象上的 `notify() `或者 `notifyAll()` 方法**；`sleep() `方法执行完成后，线程会**自动苏醒**。或者。

### 调用 start() 方法时会执行 run() 方法，为什么不能直接调用 run() 方法？

**总结： 调用 `start()` 方法方可启动线程并使线程进入就绪状态，直接执行 `run()` 方法会当成一个main线程下的普通方法执行，不会以多线程的方式执行。**

new 一个 Thread，线程进入了新建NEW状态。调用 `start()`方法，会启动一个线程并使线程进入了就绪RUNNABLE状态，当分配到时间片后就可以开始运行了。 **`start()` 会执行线程的相应准备工作，然后自动执行 `run()` 方法的内容，这是真正的启动一个线程工作。** 但是，**直接执行 `run()` 方法，会把 `run()` 方法当成一个 main 线程下的普通方法去执行，并不会在某个线程中执行它，**所以这并不是多线程工作。

## 上下文切换

线程在执行过程中会有**自己的运行条件和状态**（也称上下文），比如上文所说到过的**程序计数器，栈信息**等。当出现如下情况的时候，**线程会从占用 CPU 状态中退出**。

- 主动让出 CPU，比如调用了 **`sleep()`, `wait()`** 等。
- **时间片用完**，因为操作系统要防止一个线程或者进程长时间占用CPU导致其他线程或者进程饿死。
- 调用了**阻塞类型的系统中断**，比如**请求 IO**，线程被阻塞。
- **被终止或结束运行**

这其中**前三种**都会发生**线程切换**，线程切换意味着需要**保存当前线程的上下文**，留待线程下次占用 CPU 的时候恢复现场。并加载下一个将要占用 CPU 的线程上下文。这就是所谓的 **上下文切换**。

**操作系统中每次上下文切换需要保存信息恢复信息，这将会占用 CPU，内存等系统资源进行处理，也就意味着效率会有一定损耗，如果频繁切换就会造成整体效率低下。**

## 线程死锁

线程死锁描述的是这样一种情况：多个线程同时被阻塞，它们中的一个或者全部都在等待某个资源被释放。由于线程被无限期地阻塞，因此程序不可能正常终止。

如下图所示，线程 A 持有资源 2，线程 B 持有资源 1，他们同时都想申请对方的资源，所以这两个线程就会互相等待而进入死锁状态。

<img src="imgs/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d342f323031392d34254536254144254242254539253934253831312e706e67.png" alt="线程死锁示意图 " style="width:30%;" />

```java
public class DeadLockDemo {
    private static Object resource1 = new Object();//资源 1
    private static Object resource2 = new Object();//资源 2

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (resource1) {
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource2");
                synchronized (resource2) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }, "线程 1").start();

        new Thread(() -> {
            synchronized (resource2) {
                System.out.println(Thread.currentThread() + "get resource2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource1");
                synchronized (resource1) {
                    System.out.println(Thread.currentThread() + "get resource1");
                }
            }
        }, "线程 2").start();
    }
}
```

Output

```java
Thread[线程 1,5,main]get resource1
Thread[线程 2,5,main]get resource2
Thread[线程 1,5,main]waiting get resource2
Thread[线程 2,5,main]waiting get resource1
```

线程 A 通过 `synchronized (resource1)` 获得 `resource1` 的monitor锁，然后通过`Thread.sleep(1000);`让线程 A 休眠 1s 为的是让线程 B 得到执行然后获取到 resource2 的monitor锁。线程 A 和线程 B 休眠结束了都开始企图请求获取对方的资源，然后这两个线程就会陷入互相等待的状态，这也就产生了死锁。

## 线程死锁的四个必要条件

上面的例子符合产生死锁的四个必要条件:

1. **互斥条件**：该资源任意一个时刻只由一个线程占用。
2. **请求与保持条件**：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
3. **不剥夺条件**: 线程已获得的资源在未使用完之前不能被其他线程强行剥夺，只有自己使用完毕后才释放资源。
4. **循环等待条件**: 若干进程之间形成一种头尾相接的循环等待资源关系。

## 避免线程死锁

**如何预防死锁？** 破坏死锁的产生的必要条件即可：

1. **破坏请求与保持条件** ：一次性申请所有的资源。
2. **破坏不剥夺条件** ：占用部分资源的线程进一步申请其他资源时，如果申请不到，可以主动释放它占有的资源。
3. **破坏循环等待条件** ：靠按序申请资源来预防。按某一顺序申请资源，释放资源则反序释放。破坏循环等待条件。

避免死锁就是在资源分配时，借助于算法（比如银行家算法）对资源分配进行计算评估，使其进入安全状态。

**安全状态**指的是系统能够按照某种**推进顺序**（P1、P2、P3.....Pn）来为每个**进程**分配所需资源，直到**满足每个进程对资源的最大需求**，使每个进程都可顺利完成。称<P1、P2、P3.....Pn>序列为安全序列。

### 上面死锁的一种解决方式：按顺序获取资源

我线程 2 的代码修改成下面这样(改变请求资源的顺序)就不会产生死锁了。

```java
        new Thread(() -> {
            synchronized (resource1) {//先请求r1
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource2");
                synchronized (resource2) {//再请求r2
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }, "线程 2").start();
```

Output

```java
Thread[线程 1,5,main]get resource1
Thread[线程 1,5,main]waiting get resource2
Thread[线程 1,5,main]get resource2
Thread[线程 2,5,main]get resource1
Thread[线程 2,5,main]waiting get resource2
Thread[线程 2,5,main]get resource2

Process finished with exit code 0
```

我们分析一下上面的代码为什么避免了死锁的发生?

线程 1 首先获得到 resource1 的监视器锁,这时候线程 2 就获取不到了。然后线程 1 再去获取 resource2 的监视器锁，可以获取到。然后线程 1 释放了对 resource1、resource2 的监视器锁的占用，线程 2 获取到就可以执行了。这样就破坏了**破坏循环等待条件**，因此避免了死锁。

# CAS

Synchronized属于**悲观锁**，悲观地认为程序中的并发情况严重，所以严防死守。CAS属于**乐观锁**，乐观地认为程序中的并发情况不那么严重，所以让线程不断去尝试更新。

CAS是英文单词Compare And Swap的缩写，翻译过来就是比较并替换。

CAS机制当中使用了3个基本操作数：内存地址V，旧的预期值A，要修改的新值B。

**更新一个变量的时候，只有当变量的预期值A和内存地址V当中的实际值相同时，才会将内存地址V对应的值修改为B。**

## CAS的缺点：

### 1.CPU可能开销较大

在并发量比较高的情况下，如果许**多线程反复尝试更新某一个变量**，却又一直**更新不成功**，循环往复，会给CPU带来很大的压力。

### 2.不能保证代码块的原子性

CAS机制所保证的只是**一个变量的原子性操作**，而**不能保证整个代码块的原子性**。比如需要保证3个变量共同进行原子性的更新，就不得不使用悲观锁了。

### 3.ABA问题。

CAS的核心思想是通过比对内存值与预期值是否一样而判断内存值是否被改过，但这个判断逻辑不严谨，**假如内存值原来是A，后来被一条线程改为B，最后又被改成了A，则CAS认为此内存值并没有发生改变**，但实际上是有被其他线程改过的，这种情况对依赖过程值的情景的运算结果影响很大。

### ABA问题处理：

思路：解决ABA最简单的方案就是给值加一个修改**版本号**，每次值变化，都会修改它版本号，CAS操作时都对比此版本号。

**AtomicMarkableReference**则是将一个boolean值作是否有更改的标记，本质就是它的版本号只有两个，true和false，修改的时候在这两个版本号之间来回切换，这样做并不能解决ABA的问题，只是会降低ABA问题发生的几率而已；

**AtomicStampedReference** 本质是有一个int 值作为版本号，每次更改前先取到这个int值的版本号，等到修改的时候，比较当前版本号与当前线程持有的版本号是否一致，如果一致，则进行修改，并将版本号+1（当然加多少或减多少都是可以自己定义的），在zookeeper中保持数据的一致性也是用的这种方式；



例子：

```java
private static AtomicStampedReference<Integer> atomicStampedRef =
        new AtomicStampedReference<>(1, 0);
public static void main(String[] args){
    Thread main = new Thread(() -> {
        System.out.println("操作线程" + Thread.currentThread() +",初始值 a = " + atomicStampedRef.getReference());
        int stamp = atomicStampedRef.getStamp(); //获取当前标识别
        try {
            Thread.sleep(1000); //等待1秒 ，以便让干扰线程执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean isCASSuccess = atomicStampedRef.compareAndSet(1,2,stamp,stamp +1);  //此时expectedReference未发生改变，但是stamp已经被修改了,所以CAS失败
        System.out.println("操作线程" + Thread.currentThread() +",CAS操作结果: " + isCASSuccess);
    },"主操作线程");
 
    Thread other = new Thread(() -> {
        Thread.yield(); // 确保thread-main 优先执行
atomicStampedRef.compareAndSet(1,2,atomicStampedRef.getStamp(),atomicStampedRef.getStamp() +1);
        System.out.println("操作线程" + Thread.currentThread() +",【increment】 ,值 = "+ atomicStampedRef.getReference());
        atomicStampedRef.compareAndSet(2,1,atomicStampedRef.getStamp(),atomicStampedRef.getStamp() +1);
        System.out.println("操作线程" + Thread.currentThread() +",【decrement】 ,值 = "+ atomicStampedRef.getReference());
    },"干扰线程");
 
    main.start();
    other.start();
}
```



# synchronized 关键字

<img src="https://snailclimb.gitee.io/javaguide/docs/java/multi-thread/images/interview-questions/synchronized%E5%85%B3%E9%94%AE%E5%AD%97.png" alt="img" style="width:70%;" />

**`synchronized` 关键字解决的是多个线程之间<u>访问资源</u>的同步性，`synchronized`关键字可以保证被它修饰的<u>方法或者代码块</u>在任意时刻只能有一个线程执行。**

另外，在 Java 早期版本中，`synchronized` 属于 **重量级锁**，效率低下:

因为**监视器锁monitor**是依赖于底层的**操作系统的 `Mutex Lock`** 来实现的，Java 的线程是映射到操作系统的原生线程之上的。如果要**挂起或者唤醒一个线程**，都需要**操作系统帮忙完成**，需要从**用户态**转换到**内核态**，这个状态之间的转换需要相对比较长的时间，时间成本相对较高。

庆幸的是在 Java 6 之后 Java 官方对从 JVM 层面对 `synchronized` 较大优化，如**自旋锁、适应性自旋锁、锁消除、锁粗化、偏向锁、轻量级锁**等技术来减少锁操作的开销。

## synchronized 关键字的使用

### **1.修饰实例方法:** 

作用于当前**对象实例**加锁，进入同步代码前要获得 **当前对象实例的锁**

```java
synchronized void method() {
    //业务代码
}
```

### **2.修饰静态方法:** 

也就是给**当前类**加锁，会**作用于类的所有<u>对象实例</u>** ，进入同步代码前要获得 **当前 class 的锁**。因为静态成员不属于任何一个实例对象，是类成员（ *static 表明这是该类的一个静态资源，不管 new 了多少个对象，只有一份*）。所以，如果一个线程 A 调用一个**<u>实例对象</u>的非静态 `synchronized` 方法**，而线程 B 需要调用这个**<u>实例对象所属类</u>的静态 `synchronized` 方法**，是允许的，不会发生互斥现象，**因为访问静态 `synchronized` 方法占用的锁是当前<u>类</u>的锁，而访问<u>非</u>静态 `synchronized` 方法占用的锁是当前<u>实例对象锁</u>**。

```java
synchronized static void method() {
    //业务代码
}
```

### **3.修饰代码块** ：

指定加锁<u>对象</u>，对给定对象/类加锁。`synchronized(this|object)` 表示进入同步代码库前要获得**给定对象的锁**。`synchronized(类.class)` 表示进入同步代码前要获得 **当前 class 的锁**

```java
synchronized(this) {
    //业务代码
}
```

**总结：**

- `synchronized` 关键字加到 `static` 静态方法和 `synchronized(class)` 代码块上都是是给 <u>**Class 类**</u>上锁，而不是给<u>**实例对象**</u>上锁。
- `synchronized` 关键字加到**实例方法**上是给**对象实例**上锁。
- 尽量不要使用 `synchronized(String a)` 因为 JVM 中，字符串**常量池**具有**缓存**功能！

### 4. 构造方法不可以使用 synchronized 关键字修饰

构造方法Constructor本身就属于**线程安全**的，不存在同步的构造方法一说。

## synchronized 关键字的底层原理

synchronized 关键字底层原理属于 **JVM** 层面。

**两者的本质都是对对象监视器 monitor (即锁)的获取。**

- `synchronized` **同步语句块**的实现使用的是 **`monitorenter` 和 `monitorexit`** 指令：
    - `monitorenter` 指令指向同步代码块的**开始位置**，将锁的计数器从0加1；
    - `monitorexit` 指令则指明同步代码块的**结束位置**，将锁的计数器从n(不一定是1？)置回1。

- `synchronized` **修饰的方法**并没有 `monitorenter` 指令和 `monitorexit` 指令，取得代之的确实是 **`ACC_SYNCHRONIZED`** 标识，该标识**指明了该方法是一个同步方法**。

### synchronized 同步语句块的情况

```java
public class SynchronizedDemo {
    public void method() {
        synchronized (this) {
            System.out.println("synchronized 代码块");
        }
    }
}
```

通过 JDK 自带的 `javap` 命令查看 `SynchronizedDemo` 类的相关字节码信息：首先切换到类的对应目录执行 `javac SynchronizedDemo.java` 命令生成编译后的 .class 文件，然后执行`javap -c -s -v -l SynchronizedDemo.class`。

<img src="imgs/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f73796e6368726f6e697a65642545352538352542332545392539342541452545352541442539372545352538452539462545372539302538362e706e67.png" alt="synchronized关键字原理" style="width:80%;" />

从上面我们可以看出：

**`synchronized` 同步语句块的实现使用的是 `monitorenter` 和 `monitorexit` 指令，其中 `3 monitorenter` 指令指向同步代码块的开始位置，`13 monitorexit` 指令则指明同步代码块的结束位置。`19的monitorexit`是保证同步代码块<u>抛出异常时释放锁</u>而存在的**

当执行 `monitorenter` 指令时，线程试图获取锁也就是获取 **对象监视器 `monitor`** 的持有权。

> 在 Java 虚拟机(HotSpot)中，Monitor 是基于 C++实现的，由[ObjectMonitor](https://github.com/openjdk-mirror/jdk7u-hotspot/blob/50bdefc3afe944ca74c3093e7448d6b889cd20d1/src/share/vm/runtime/objectMonitor.cpp)实现的。每个对象中都内置了一个 `ObjectMonitor`对象。
>
> 另外，`wait/notify`等方法也依赖于`monitor`对象，这就是为什么只有在**同步的块或者方法中才能调用`wait/notify`**等方法，否则会抛出`java.lang.IllegalMonitorStateException`的异常的原因。

在执行`monitorenter`时，会**尝试获取对象的锁**，如果**锁的计数器为 0** 则表示锁可以被获取，获取后**将锁计数器设为 1 也就是加 1。**

在执行 `monitorexit` 指令后，**将锁计数器设为 0**，表明**锁被释放**。如果获取对象锁失败，那当前线程就要阻塞等待，直到锁被另外一个线程释放为止。

### synchronized 修饰方法的的情况

```java
public class SynchronizedDemo2 {
    public synchronized void method() {
        System.out.println("synchronized 方法");
    }
}
```

<img src="https://camo.githubusercontent.com/78774e083f6db4eb50d52b9b7a730eadfcc205ab1a05f2438bbbc66936dc32ca/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f73796e6368726f6e697a6564254535253835254233254539253934254145254535254144253937254535253845253946254537253930253836322e706e67" alt="synchronized关键字原理" style="width:75%;" />

`synchronized` 修饰的方法并没有 `monitorenter` 指令和 `monitorexit` 指令，取得代之的是 `ACC_SYNCHRONIZED` 标识，该标识指明了该方法是一个同步方法。JVM 通过该 `ACC_SYNCHRONIZED` 访问标志来辨别一个方法是否声明为同步方法，从而执行相应的同步调用。

**不过两者的本质都是对对象监视器 monitor 的获取。**

## JDK1.6 之后的 synchronized 关键字底层的优化

![preview](imgs/v2-9db4211af1be81785f6cc51a58ae6054_r.jpg)

JDK1.6 对锁的实现引入了大量的优化，如**偏向锁、轻量级锁、自旋锁、适应性自旋锁、锁消除、锁粗化**等技术来减少锁操作的开销。

锁主要存在**四种状态**，依次是：**无锁、偏向锁、轻量级锁、重量级锁**，他们会随着竞争的激烈而逐渐升级。注意**锁只可以升级不可降级**，这种策略是为了提高获得锁和释放锁的效率。

### Java对象头(MarkWord存储锁类型)

在HotSpot虚拟机中, 对象在内存中的布局分为三块区域: 对象头, 示例数据和对其填充.

对象头中包含两部分: MarkWord 和 类型指针; 如果是数组对象的话, 对象头还有一部分是存储数组的长度.

| 长度     | 内容                   | 说明                             |
| -------- | ---------------------- | -------------------------------- |
| 32/64bit | MarkWord               | 存储对象的hashCode或**锁信息**等 |
| 32/64bit | Class Metadada Address | 存储对象**类型**数据的**指针**   |
| 32/64bit | Array Length           | 数组的长度(如果当前对象是数组)   |

如果是数组对象的话, 虚拟机用3个字宽(32/64bit + 32/64bit + 32/64bit)存储对象头; 如果是普通对象的话, 虚拟机用2字宽存储对象头(32/64bit + 32/64bit).

#### 类型指针

类型指针指向对象的类元数据, 虚拟机通过这个指针确定该对象是哪个类的实例.

多线程下synchronized的加锁就是对同一个对象的对象头中的MarkWord中的变量进行CAS操作.

#### MarkWord

**Mark Word用于存储对象自身的运行时数据, 如HashCode, GC分代年龄, 锁状态标志, 线程持有的锁, 偏向线程ID**等等.
占用内存大小与虚拟机位长一致(32位JVM -> MarkWord是32位, 64位JVM->MarkWord是64位).

### 优化后synchronized锁的分类

级别从低到高依次是:

1. 无锁状态
2. 偏向锁状态
3. 轻量级锁状态
4. 重量级锁状态

**锁可以升级, 但不能降级**. 即: 无锁 -> 偏向锁 -> 轻量级锁 -> 重量级锁是单向的.

每个锁状态对象头中的**MarkWord**的内容是什么. 以32位为例.

#### 无锁状态(New) 后三位001

| 25bit                      | 4bit                     | 1bit(是否是偏向锁) | 2bit(锁标志位) |
| -------------------------- | ------------------------ | ------------------ | -------------- |
| **对象**的IdentityHashCode | 对象分代年龄(JVM 分代GC) | **0**              | **01**         |

#### 偏向锁状态 后三位101

| 23bit          | 2bit          | 4bit         | 1bit(是否是偏向锁) | 2bit   |
| -------------- | ------------- | ------------ | ------------------ | ------ |
| **当前线程**ID | epoch批量撤销 | 对象分代年龄 | **1**              | **01** |

#### 轻量级锁(aka自旋/无锁/自适应自旋)状态 后两位00

| 30bit                                                  | 2bit |
| ------------------------------------------------------ | ---- |
| 指向**栈**中Lock Record的指针(可重入锁相关,上锁的次数) | 00   |

#### 重量级锁状态 后两位10

| 30bit                                      | 2bit |
| ------------------------------------------ | ---- |
| 指向互斥量(**重量级锁-底层的mutex**)的指针 | 10   |

GC标记信息 后两位11

| 30bit                 | 2bit |
| --------------------- | ---- |
| cms过程用到的标记信息 | 11   |

### 锁的升级(进化)

#### 1. 偏向锁 

**偏向锁适用于只有一个线程访问同步块的场景, 适合一个线程对一个锁的多次获取的情况**

偏向锁是针对于一个**线程**而言的, 线程**获得锁之后**就**不会再有解锁**等操作了, 这样可以省略很多开销. 

假如有**两个线程来竞争该锁**的话, 那么**偏向锁就失效**了, 进而**升级成轻量级锁**了.

为什么要这样做呢? 因为经验表明, 其实大部分情况下, 都会是**同一个线程进入同一块同步代码块**的. 这也是为什么会有偏向锁出现的原因.

在Jdk1.6中, 偏向锁的开关是**默认开启**的, 但是它会延迟4秒钟后才激活,等jvm启动完成才启动,4秒前是普通无锁对象;

**4秒内(偏向锁未启动)**如果有线程竞争,那么会升级成轻量级锁

**4秒后(偏向锁已启动)**如果没有线程获得此对象锁, 那么他是**匿名偏向锁对象**. 

为什么要延迟4秒因为**某些对象**一定会有很多线程**争抢**, 所以就没有必要启用偏向锁,直接用轻量级锁.

![image-20210905164602396](imgs/image-20210905164602396.png)

可以在**jvm调优中**使用-XX:BiasedLockingStartupDelay=0来关闭偏向锁的启动延迟, 也可以使用-XX:-UseBiasedLocking=false来关闭偏向锁, 那么程序会直接进入轻量级锁状态.

##### 偏向锁的加锁

当**一个线程访问同步块并获取锁**时, 会在**锁对象**的**对象头和栈帧**中的**锁记录里存储锁偏向的线程ID**, 以后该线程进入和退出同步块时**不需要进行CAS**操作来**加锁和解锁**, 只需要简单的测试锁对象的对象头的**MarkWord**里是否存储存着**指向当前线程的偏向锁(线程ID是当前线程)**

如果测试**成功**, 表示线程已经**获得锁**; 

如果测试**失败**, 则需要再测试一下**MarkWord**中**偏向锁的标识是否设置成1**(表示当前是偏向锁), **如果没有设置, 则使用CAS竞争锁**; 如果设置了, 则尝试**使用CAS将锁对象的对象头的偏向锁指向当前线程(即偏向锁的释放)**.

##### 偏向锁的撤销(撤销后升级成轻量级锁)

偏向锁使用了一种等到竞争出现才释放锁的机制, 所以当**其他线程尝试竞争偏向锁时, 持有偏向锁的线程才会释放锁**. 

偏向锁的释放需要等到**全局安全点**(在这个时间点上**没有正在执行的字节码**). 首先会**暂停持有偏向锁的线程**, 然后检查**持有偏向锁的线程是否存活**, 如果线程**不处于活动**状态, 则将锁对象的**对象头设置为无锁状态**; 如果**线程仍然活着**, 则锁对象的对象头中的**MarkWord**和栈中的锁记录**要么重新偏向于其它线程要么恢复到无锁状态**, mark work更新为轻量锁, 最后**唤醒暂停的线程(释放偏向锁的线程)**.变成无锁状态以后会升级成轻量级锁

#### 2. 轻量级锁

**轻量级锁适合锁执行体比较简单(即减少锁粒度或时间), 自旋一会儿就可以成功获取锁的情况**

当出现有**两个线程来竞争锁**的话, 那么**偏向锁就失效**了, 此时锁就会膨胀, **升级为轻量级锁**.

##### 轻量级锁加锁

线程在执行同步块之前, JVM会先在当前线程的栈帧中创建用户**存储锁记录Lock Record的空间**, 并将**对象头中的MarkWord复制到锁记录Lock Record**中. 然后线程尝试使用**CAS**将**对象头中的MarkWord替换为指向锁记录Lock Record的指针**. 如果成功, 当前线程**获得锁**; 如果失败, 表示**其它线程竞争锁**, **当前线程便尝试使用自旋来获取锁**, 之后再来的线程, 发现是轻量级锁, 就开始进行**自旋**.

##### 轻量级锁解锁

轻量级锁解锁时, 会使用**原子的CAS操作**将当前**线程的锁记录Lock Record替换回到对象头**, 如果成功, 表示没有竞争发生; **如果失败, 表示当前锁存在竞争, 锁就会膨胀成重量级锁.**

#### 偏向锁->轻量级锁->重量级锁

总结一下加锁解锁过程, 有线程A和线程B来竞争对象c的锁(如: synchronized(c){} ), 这时**线程A和线程B同时将对象c的MarkWord复制到自己的锁记录中**, 两者竞争去获取锁, 假设线程**A成功获取锁**, 并将**对象c的对象头中的线程ID(MarkWord中)修改为指向自己的锁记录Lock Record的指针**, 这时线程B仍旧通过**CAS**去获取对象c的锁, 因为对象c的MarkWord中的内容已经被线程A改了, 所以**获取失败**. 此时**为了提高获取锁的效率, 线程B会循环去获取锁**, 这个循环是有次数限制的, 如果在循环结束之前CAS操作成功, 那么线程B就获取到锁, 如果**循环结束依然获取不到锁, 则获取锁失败, 对象c的MarkWord中的记录会被修改为重量级锁,** 然后**线程B就会被挂起**, 之后有**线程C来获取锁时, 看到对象c的MarkWord中的是重量级锁的指针, 说明竞争激烈, 直接挂起.**

解锁时, 线程A尝试使用**CAS**将**对象c的MarkWord改回自己栈中复制的那个MarkWord**, 因为对象c中的MarkWord已经被指向为**重量级锁**了, 所以**CAS失败**, **线程A会释放锁并唤起等待的线程, 进行新一轮的竞争**.

### 锁的比较

| 锁       | 优点                                                         | 缺点                                                | 适用场景                               |
| -------- | ------------------------------------------------------------ | --------------------------------------------------- | -------------------------------------- |
| 偏向锁   | **加锁和解锁不需要额外的消耗, 和执行非同步代码方法的性能相差无几.** | **如果线程间存在锁竞争, 会带来额外的锁撤销的消耗.** | **适用于只有一个线程访问的同步场景**   |
| 轻量级锁 | **竞争的线程不会阻塞, 提高了程序的响应速度**                 | **如果始终得不到锁竞争的线程, 使用自旋会消耗CPU**   | **追求响应时间, 同步快执行速度非常快** |
| 重量级锁 | **线程竞争不使用自旋, 不会消耗CPU**                          | **线程堵塞, 响应时间缓慢**                          | **追求吞吐量, 同步快执行时间速度较长** |

## sychronized例子：双重校验锁的单例模式

下面我以一个常见的面试题为例讲解一下 `synchronized` 关键字的具体使用。

面试中面试官经常会说：“单例模式了解吗？来给我手写一下！给我解释一下**双重检验锁方式**实现单例模式的原理呗！”

**双重校验锁实现对象单例（线程安全）**

**双重检验锁** 第二次判断目的在于有可能其他线程获取过锁，已经初始化该变量。

```java
public class Singleton {

    private volatile static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
       //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (uniqueInstance == null) {//双重检验锁第一次判断
            //类对象加锁
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {//双重检验锁第二次判断
                    uniqueInstance = new Singleton();//分配内存空间，初始化，指向内存地址
                }
            }
        }
        return uniqueInstance;
    }
}
```

### 为什么是双重校验锁实现单例模式呢？

第一次校验：也就是第一个if（uniqueInstance==null），这个是为了代码提高代码执行效率，由于**单例模式只要一次创建实例即可**，所以当创建了一个实例之后，再次**调用getUniqueInstance方法就不必要进入同步代码块，不用竞争锁**，**直接返回前面创建的实例即可**。

第二次校验：也就是第二个if（uniqueInstance==null），这个校验是**防止二次创建实例**，假如有一种情况，当uniqueInstance还未被创建时，线程t1调用getUniqueInstance方法，由于第一次判断uniqueInstance==null，此时线程t1准备继续执行，但是由于资源被线程t2抢占了，此时t2页调用getUniqueInstance方法，同样的，由于uniqueInstance并没有实例化，t2同样可以通过第一个if，然后继续往下执行，同步代码块，第二个if也通过，然后t2线程创建了一个实例uniqueInstance。此时t2线程完成任务，资源又回到t1线程，t1此时也进入同步代码块，如果没有这个第二个if，那么，t1就也会创建一个uniqueInstance实例，那么，就会出现创建多个实例的情况，但是加上第二个if，就可以完全避免这个多线程导致多次创建实例的问题。

### 注意 `uniqueInstance` 采用 **`volatile`** 关键字修饰

防止**指令重排**导致一个**线程获得还没有初始化的实例**

 `uniqueInstance = new Singleton();` 这段代码其实是分为三步执行：

1. 为 `uniqueInstance` 分配内存空间
2. 初始化 `uniqueInstance`
3. 将 `uniqueInstance` 指向分配的内存地址

但是由于 JVM 具有指令重排的特性，执行顺序有可能变成 1->3->2。指令重排在单线程环境下不会出现问题，但是在多线程环境下**指令重排**会导致一个**线程获得还没有初始化的实例**。例如，线程 T1 执行了 1 和 3，此时 T2 调用 `getUniqueInstance`() 后发现 `uniqueInstance` 不为空，因此返回 `uniqueInstance`，但此时 `uniqueInstance` 还未被初始化。

使用 **`volatile` 可以禁止 JVM 的指令重排**，**Java线程内存模型确保所有线程看到这个变量的值是一致的**保证在多线程环境下也能正常运行。

# synchronized 和 ReentrantLock 的区别

## 两者都是可重入锁

**“可重入锁”** 指的是自己可以再次获取自己的内部锁。比如一个线程获得了某个对象的锁，此时这个对象锁还没有释放，当其再次想要获取这个对象的锁的时候还是可以获取的，如果不可锁重入的话，就会造成死锁。同一个线程每次获取锁，锁的计数器都自增 1，所以要等到锁的计数器下降为 0 时才能释放锁。

## synchronized 依赖于 JVM 而 ReentrantLock 依赖于 JDK

`synchronized` 是依赖于 JVM 实现的，在 JDK1.6  `synchronized` 关键字进行了很多优化，但是这些优化都是在虚拟机层面实现的，并没有直接暴露给我们。

`ReentrantLock` 是 JDK 层面实现的（也就是 API 层面，需要 **lock() 和 unlock()** 方法配合 **try/finally** 语句块来完成），所以我们可以通过查看它的源代码，来看它是如何实现的。

## ReentrantLock 比 synchronized 增加了一些高级功能

相比`synchronized`，`ReentrantLock`增加了一些高级功能。主要来说主要有三点：

- **等待可中断** : `ReentrantLock`提供了一种能够**中断等待锁的线程**的机制，通过 `lock.lockInterruptibly()` 来实现这个机制。也就是说正在等待的线程可以选择放弃等待，改为处理其他事情。
- **可实现公平锁** : `ReentrantLock`可以指定是**公平锁还是非公平锁**。而`synchronized`只能是**非公平锁**。所谓的公平锁就是先等待的线程先获得锁。`ReentrantLock`默认情况是非公平的，可以通过 `ReentrantLock`类的`ReentrantLock(boolean fair)`构造方法来制定是否是公平的。
- **可实现选择性通知（锁可以绑定多个条件）**: 
    - `synchronized`关键字与`wait()`和`notify()`/`notifyAll()`方法相结合可以实现**等待/通知机制**。
    - `ReentrantLock`类当然也可以实现，但是需要借助于`Condition`接口与`newCondition()`方法。

> `Condition`是 JDK1.5 之后才有的，它具有很好的灵活性，比如可以实现多路通知功能也就是在一个`Lock`对象中可以创建多个`Condition`实例（即对象监视器），**线程对象可以注册在指定的`Condition`中，从而可以有选择性的进行线程通知，在调度线程上更加灵活。 在使用`notify()/notifyAll()`方法进行通知时，被通知的线程是由 JVM 选择的，用`ReentrantLock`类结合`Condition`实例可以实现“选择性通知”** ，这个功能非常重要，而且是 Condition 接口默认提供的。而`synchronized`关键字就相当于整个 Lock 对象中只有一个`Condition`实例，所有的线程都注册在它一个身上。如果执行`notifyAll()`方法的话就会通知所有处于等待状态的线程这样会造成很大的效率问题，而`Condition`实例的`signalAll()`方法 只会唤醒注册在该`Condition`实例中的所有等待线程。

# volatile 关键字

## CPU 缓存模型

**内存缓存的是硬盘数据用于解决硬盘访问速度过慢的问题, CPU Cache 缓存的是内存数据用于解决 CPU 处理速度和内存不匹配的问题**

<img src="imgs/68747470733a2f2f67756964652d626c6f672d696d616765732e6f73732d636e2d7368656e7a68656e2e616c6979756e63732e636f6d2f323032302d382f33303361333030662d373064642d346565312d393937342d3366333361666663363537342e706e67.png" alt="CPU Cache" style="width:33%;" />

**CPU Cache 的工作方式：**

先复制一份数据到 CPU Cache 中，当 CPU 需要用到的时候就可以直接从 CPU Cache 中读取数据，当运算完成后，再将运算得到的数据写回 Main Memory 中。但是，这样存在 **内存缓存不一致性的问题** ！

比如我执行一个 i++操作的话，如果两个线程同时执行的话，假设两个线程从 CPU Cache 中读取的 i=1，两个线程做了 1++运算完之后再写回 Main Memory 之后 i=2，而正确结果应该是 i=3。

**CPU 为了解决内存缓存不一致性问题可以通过制定缓存一致协议或者其他手段来解决。**

## JMM(Java 内存模型)

在 JDK1.2 之前，Java 的内存模型实现总是从**主存**（即共享内存）读取变量，是不需要进行特别的注意的。而在当前的 Java 内存模型下，线程可以把变量保存**本地内存**（比如机器的寄存器）中，而不是直接在主存中进行读写。这就可能造成一个线程在主存中修改了一个变量的值，而另外一个线程还继续使用它在寄存器中的变量值的拷贝，造成**数据的不一致**。

<img src="imgs/68747470733a2f2f67756964652d626c6f672d696d616765732e6f73732d636e2d7368656e7a68656e2e616c6979756e63732e636f6d2f323032302d382f30616337653636332d376462382d346239352d386438652d3764326231373966363765382e706e67.png" alt="JMM(Java内存模型)" style="width:33%;" />

要解决这个问题，就需要把变量声明为 **`volatile`** ，这就指示 JVM，这个变量是**共享且不稳定的**，每次使用它都到主存中进行读取。

所以，**`volatile` 关键字 除了防止 JVM 的指令重排 ，还有一个重要的作用就是保证变量的可见性。**

<img src="imgs/68747470733a2f2f67756964652d626c6f672d696d616765732e6f73732d636e2d7368656e7a68656e2e616c6979756e63732e636f6d2f323032302d382f64343963353535372d313430622d346162662d616461642d3861616333633930333663662e706e67-20210905174918949.png" alt="volatile关键字的可见性" style="width:50%;" />

## 并发下的原子性, 可见性, 有序性如何保证?

1. **原子性** : 一次操作或者多次操作，要么所有的操作全部都得到执行并且不会收到任何因素的干扰而中断，要么所有的操作都执行，要么都不执行。

    **`synchronized` 可以保证代码片段的原子性, `volatile` 关键字不能保证操作的原子性**

2. **可见性** ：当一个变量对共享变量进行了修改，那么另外的线程都是立即可以看到修改后的最新值。

    **`synchronized, volatile` 关键字都可以保证共享变量的可见性。**

3. **有序性** ：代码在执行的过程中的先后顺序，Java 在编译器以及运行期间的优化，代码的执行顺序未必就是编写代码时候的顺序。

    **`volatile` 关键字可以禁止指令进行重排序优化。**

#  synchronized 关键字和 volatile 关键字的区别

`synchronized` 关键字和 `volatile` 关键字是两个**互补**的存在，而**不是对立**的存在！

- **`volatile` 关键字**是线程同步的**轻量级实现**，所以 **`volatile `性能肯定比`synchronized`关键字要好** 。但是 **`volatile` 关键字只能用于变量而 `synchronized` 关键字可以修饰方法以及代码块** 。
- **`volatile` 关键字能<u>保证数据的可见性</u>，但<u>不能保证数据的原子性</u>。`synchronized` 关键字两者都能保证。**
- **`volatile`关键字主要用于解决变量在多个线程之间的可见性，而 `synchronized` 关键字解决的是多个线程之间访问资源的同步性。**

# ThreadLocal

通常情况下，我们创建的变量是可以被任何一个线程访问并修改的。**如果想实现每一个线程都有自己的专属本地变量该如何解决呢？** JDK 中提供的`ThreadLocal`类正是为了解决这样的问题。 **`ThreadLocal`类主要解决的就是让每个线程绑定自己的值，可以将`ThreadLocal`类形象的比喻成存放数据的盒子，盒子中可以存储每个线程的私有数据。**

**如果你创建了一个`ThreadLocal`变量，那么访问这个变量的每个线程都会有这个变量的本地副本，这也是`ThreadLocal`变量名的由来。他们可以使用 `get（）` 和 `set（）` 方法来获取默认值或将其值更改为当前线程所存的副本的值，从而避免了线程安全问题。**

## ThreadLocal 示例

```java
import java.text.SimpleDateFormat;
import java.util.Random;

public class ThreadLocalExample implements Runnable{

     // SimpleDateFormat 不是线程安全的，所以每个线程都要有自己独立的副本
    private static final ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd HHmm"));

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalExample obj = new ThreadLocalExample();
        for(int i=0 ; i<10; i++){
            Thread t = new Thread(obj, ""+i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    @Override
    public void run() {
        System.out.println("Thread Name= "+Thread.currentThread().getName()+" default Formatter = "+formatter.get().toPattern());
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //formatter pattern is changed here by thread, but it won't reflect to other threads
        formatter.set(new SimpleDateFormat());

        System.out.println("Thread Name= "+Thread.currentThread().getName()+" formatter = "+formatter.get().toPattern());
    }

}
```

Output:

```java
Thread Name= 0 default Formatter = yyyyMMdd HHmm
Thread Name= 0 formatter = yy-M-d ah:mm
Thread Name= 1 default Formatter = yyyyMMdd HHmm
Thread Name= 2 default Formatter = yyyyMMdd HHmm
Thread Name= 1 formatter = yy-M-d ah:mm
Thread Name= 3 default Formatter = yyyyMMdd HHmm
Thread Name= 2 formatter = yy-M-d ah:mm
Thread Name= 4 default Formatter = yyyyMMdd HHmm
Thread Name= 3 formatter = yy-M-d ah:mm
Thread Name= 4 formatter = yy-M-d ah:mm
Thread Name= 5 default Formatter = yyyyMMdd HHmm
Thread Name= 5 formatter = yy-M-d ah:mm
Thread Name= 6 default Formatter = yyyyMMdd HHmm
Thread Name= 6 formatter = yy-M-d ah:mm
Thread Name= 7 default Formatter = yyyyMMdd HHmm
Thread Name= 7 formatter = yy-M-d ah:mm
Thread Name= 8 default Formatter = yyyyMMdd HHmm
Thread Name= 9 default Formatter = yyyyMMdd HHmm
Thread Name= 8 formatter = yy-M-d ah:mm
Thread Name= 9 formatter = yy-M-d ah:mm
```

从输出中可以看出，Thread-0 已经改变了 formatter 的值，但仍然是 thread-2 默认格式化程序与初始化值相同，其他线程也一样。

上面有一段代码用到了创建 `ThreadLocal` 变量的那段代码用到了 Java8 的知识，它等于下面这段代码，如果你写了下面这段代码的话，IDEA 会提示你转换为 Java8 的格式(IDEA 真的不错！)。因为 ThreadLocal 类在 Java 8 中扩展，使用一个新的方法`withInitial()`，将 Supplier 功能接口作为参数。

```java
private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>(){
    @Override
    protected SimpleDateFormat initialValue(){
        return new SimpleDateFormat("yyyyMMdd HHmm");
    }
};
```

## ThreadLocal 原理

从 `Thread`类源代码入手。

```java
public class Thread implements Runnable {
    ......
    //与此线程有关的ThreadLocal值。由ThreadLocal类维护
    ThreadLocal.ThreadLocalMap threadLocals = null;

    //与此线程有关的InheritableThreadLocal值。由InheritableThreadLocal类维护
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
    ......
}
```

从上面`Thread`类 源代码可以看出`Thread` 类中有一个 `threadLocals` 和 一个 `inheritableThreadLocals` 变量，它们都是 `ThreadLocalMap` 类型的变量,我们可以把 `ThreadLocalMap` 理解为`ThreadLocal` 类实现的定制化的 `HashMap`。默认情况下这两个变量都是 null，只有当前线程调用 `ThreadLocal` 类的 `set`或`get`方法时才创建它们，实际上调用这两个方法的时候，我们调用的是`ThreadLocalMap`类对应的 `get()`、`set()`方法。

### `ThreadLocal`类的`set()`方法

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}
ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```

通过上面这些内容，我们足以通过猜测得出结论：**最终的变量是放在了当前线程的 `ThreadLocalMap` 中，并不是存在 `ThreadLocal` 上，`ThreadLocal` 可以理解为只是`ThreadLocalMap`的封装，传递了变量值。** `ThrealLocal` 类中可以通过`Thread.currentThread()`获取到当前线程对象后，直接通过`getMap(Thread t)`可以访问到该线程的`ThreadLocalMap`对象。

**每个`Thread`中都具备一个`ThreadLocalMap`，而`ThreadLocalMap`可以存储以`ThreadLocal`为 key ，Object 对象为 value 的键值对。`ThreadLocalMap`是`ThreadLocal`的静态内部类。**

```java
ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
    //......
}
```

比如我们在同一个线程中声明了两个 `ThreadLocal` 对象的话，会使用 `Thread`内部都是使用仅有那个`ThreadLocalMap` 存放数据的，`ThreadLocalMap`的 key 就是 `ThreadLocal`对象，value 就是 `ThreadLocal` 对象调用`set`方法设置的值。

![ThreadLocal数据结构](imgs/threadlocal%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84.png)

## ThreadLocal 内存泄露问题

**`ThreadLocalMap`** 中使用的 **key 为 `ThreadLocal` 的弱引用, 而 value 是强引用。**如果 `ThreadLocal` 没有被外部强引用的情况下，在垃圾回收的时候，**key 会被清理掉，而 value 不会被清理掉**。这样一来，**`ThreadLocalMap` 中就会出现 key 为 null 的 Entry**。假如我们不做任何措施的话，value 永远无法被 GC 回收，这个时候就可能会产生内存泄露。

ThreadLocalMap 实现中已经考虑了这种情况，**在调用 `set()`、`get()`、`remove()` 方法的时候，会清理掉 key 为 null 的记录。**

**使用完 `ThreadLocal`方法后 最好手动调用`remove()`方法**

```java
static class Entry extends WeakReference<ThreadLocal<?>> {
    /** The value associated with this ThreadLocal. */
    Object value;

    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
}
```

**弱引用介绍：**

> 如果一个对象只具有弱引用，那就类似于**可有可无的生活用品**。弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它 所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于垃圾回收器是一个优先级很低的线程， 因此不一定会很快发现那些只具有弱引用的对象。
>
> 弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java 虚拟机就会把这个弱引用加入到与之关联的引用队列中。

# 线程池

> **池化技术相比大家已经屡见不鲜了，线程池、数据库连接池、Http 连接池等等都是对这个思想的应用。池化技术的思想主要是为了减少每次获取资源的消耗，提高对资源的利用率。**

**线程池**提供了一种**限制和管理资源**（包括执行一个任务）。 每个**线程池**还维护一些基本统计信息，例如已完成任务的数量。

## **使用线程池的好处**：

- **降低资源消耗**。通过**重复利用已创建的线程**降低线程创建和销毁造成的消耗。
- **提高响应速度**。当任务到达时，**任务可以不需要的等到线程创建**就能**立即执行**。
- **提高线程的可管理性**。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行**统一的分配，调优和监控**。

## 实现 Runnable 接口和 Callable 接口的区别

`Runnable`自 Java 1.0 以来一直存在，但`Callable`仅在 Java 1.5 中引入,目的就是为了来处理`Runnable`不支持的用例。

**`Runnable` 接口** **不会返回结果**或**抛出检查异常**，但是 **`Callable` 接口** 可以。所以，如果任务不需要返回结果或抛出异常推荐使用 **`Runnable` 接口** ，这样代码看起来会更加简洁。

```java
Runnable.java
@FunctionalInterface
public interface Runnable {
   /**
    * 被线程执行，没有返回值也无法抛出异常
    */
    public abstract void run();
}
Callable.java
@FunctionalInterface
public interface Callable<V> {
    /**
     * 计算结果，或在无法这样做时抛出异常。
     * @return 计算得出的结果
     * @throws 如果无法计算结果，则抛出异常
     */
    V call() throws Exception;
}
```

工具类 `Executors` 可以实现将 `Runnable` 对象转换成 `Callable` 对象。（`Executors.callable(Runnable task)` 或 `Executors.callable(Runnable task, Object result)`）。

##  execute()和 submit()方法的区别

1. **`execute()`方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功与否；**
2. **`submit()`方法用于提交需要返回值的任务。线程池会返回一个 `Future` 类型的对象，通过这个 `Future` 对象可以判断任务是否执行成功**，并且可以通过 **`Future` 的 `get()`方法来获取返回值**，`get()`方法会阻塞当前线程直到任务完成，而使用 `get(long timeout，TimeUnit unit)`方法则会阻塞当前线程一段时间后立即返回，这时候有可能任务没有执行完。

我们以 **`AbstractExecutorService` 接口** 中的一个 `submit` 方法为例子来看看源代码：

```java
public Future<?> submit(Runnable task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<Void> ftask = newTaskFor(task, null);
    execute(ftask);
    return ftask;
}
```

上面方法调用的 `newTaskFor` 方法返回了一个 `FutureTask` 对象。

```java
protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
    return new FutureTask<T>(runnable, value);
}
```

我们再来看看`execute()`方法：

```java
public void execute(Runnable command) {
  	...
    //没有返回值
}
```

## 如何创建线程池

《阿里巴巴 Java 开发手册》中强制线程池不允许使用 Executors 去创建，而是通过 **ThreadPoolExecutor** 的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险

> Executors 返回线程池对象的弊端如下：
>
> - **FixedThreadPool 和 SingleThreadExecutor** ： 允许请求的**队列长度**为 Integer.MAX_VALUE ，可能堆积大量的请求，从而导致 OOM。
> - **CachedThreadPool 和 ScheduledThreadPool** ： 允许创建的**线程数量**为 Integer.MAX_VALUE ，可能会创建大量线程，从而导致 OOM。

### **1. 通过构造方法ThreadPoolExecutor实现**(推荐)

![ThreadPoolExecutor构造方法](imgs/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f546872656164506f6f6c4578656375746f722545362539452538342545392538302541302545362539362542392545362542332539352e706e67.jpeg)

### **2. 通过 Executor 框架的工具类 Executors 来实现**

我们可以创建三种类型的 ThreadPoolExecutor：

- **FixedThreadPool** ： 该方法返回一个固定线程数量的线程池。该线程池中的线程数量始终不变。当有一个新的任务提交时，线程池中若有空闲线程，则立即执行。若没有，则新的任务会被暂存在一个任务队列中，待有线程空闲时，便处理在任务队列中的任务。
- **SingleThreadExecutor：** 方法返回一个只有一个线程的线程池。若多余一个任务被提交到该线程池，任务会被保存在一个任务队列中，待线程空闲，按先入先出的顺序执行队列中的任务。
- **CachedThreadPool：** 该方法返回一个可根据实际情况调整线程数量的线程池。线程池的线程数量不确定，但若有空闲线程可以复用，则会优先使用可复用的线程。若所有线程均在工作，又有新的任务提交，则会创建新的线程处理任务。所有线程在当前任务执行完毕后，将返回线程池进行复用。

**FixedThreadPool** 和 **SingleThreadExecutor** 允许请求的队列长度为 Integer.MAX_VALUE ，可能堆积大量的请求，从而导致 OOM。

**CachedThreadPool 和 ScheduledThreadPool** ： 允许创建的线程数量为 Integer.MAX_VALUE ，可能会创建大量线程，从而导致 OOM。

对应 Executors 工具类中的方法如图所示：

[![Executor框架的工具类](https://camo.githubusercontent.com/764b0fb2d22df745240850ae2d874e3bbb798494d5d32d6e3ca7eee44186c3b8/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4578656375746f722545362541312538362545362539452542362545372539412538342545352542372541352545352538352542372545372542312542422e706e67)](https://camo.githubusercontent.com/764b0fb2d22df745240850ae2d874e3bbb798494d5d32d6e3ca7eee44186c3b8/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4578656375746f722545362541312538362545362539452542362545372539412538342545352542372541352545352538352542372545372542312542422e706e67)

## ThreadPoolExecutor 类分析

`ThreadPoolExecutor` 类中提供的四个构造方法。我们来看最长的那个，其余三个都是在这个构造方法的基础上产生（其他几个构造方法说白点都是给定某些默认参数的构造方法比如默认制定拒绝策略是什么）.

```java
/**
 * 用给定的初始参数创建一个新的ThreadPoolExecutor。
 */
public ThreadPoolExecutor(int corePoolSize,
                      int maximumPoolSize,
                      long keepAliveTime,
                      TimeUnit unit,
                      BlockingQueue<Runnable> workQueue,
                      ThreadFactory threadFactory,
                      RejectedExecutionHandler handler) {
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
            throw new IllegalArgumentException();
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
}
```

### `ThreadPoolExecutor`构造函数参数(重要)

#### **`ThreadPoolExecutor` 3 个最重要的参数：corePoolSize, maximumPoolSize**, workQueue

- **`corePoolSize` :** 核心线程数定义了**最小可以同时运行的线程数量**。
- **`maximumPoolSize` :** 当队列中存放的**任务**达到**队列容量**的时候，当前可以**同时运行的线程数量变为最大线程数**。
- **`workQueue`:** 当新任务来的时候会先判断当前运行的线程数量**是否达到核心线程数**，如果达到的话，新任务就会被存放在队列中。

#### `ThreadPoolExecutor`其他常见参数:

1. **`keepAliveTime`**:当线程池中的线程数量大于 `corePoolSize` 的时候，如果这时没有新的任务提交，**核心线程外的线程不会立即销毁**，而是会**等待，超过了 `keepAliveTime`才会被回收销毁；**
2. **`unit`** : `keepAliveTime` 参数的时间单位。
3. **`threadFactory`** :executor 创建新线程的时候会用到。工厂模式?
4. **`handler`** :饱和策略。

### `ThreadPoolExecutor` 饱和策略

**`ThreadPoolExecutor` 饱和策略定义:**

如果当前**同时运行的线程数量**达到**最大线程数量**并且**队列也已经被放满了任务**时，`ThreadPoolTaskExecutor` 定义一些策略:

- **`ThreadPoolExecutor.AbortPolicy`：** **抛出 `RejectedExecutionException异常**来拒绝新任务的处理。
- **`ThreadPoolExecutor.CallerRunsPolicy`：** 调用**执行自己的线程运行任务**，也就是直接在调用`execute`方法的线程中运行(`run`)被拒绝的任务，如果执行程序已关闭，则会丢弃该任务。因此**这种策略会降低对于新任务提交速度，影响程序的整体性能**。如果应用程序可以承受此延迟并且你要求任何一个任务请求都要被执行的话，你可以选择这个策略。
- **`ThreadPoolExecutor.DiscardPolicy`：** **不处理新任务，直接丢弃掉**。
- **`ThreadPoolExecutor.DiscardOldestPolicy`：** 此策略将**丢弃最早的未处理的任务请求**。

举个例子： Spring 通过 `ThreadPoolTaskExecutor` 或者我们直接通过 `ThreadPoolExecutor` 的构造函数创建线程池的时候，

当我们不指定 `RejectedExecutionHandler` 饱和策略的话来配置线程池的时候**默认使用的是 `ThreadPoolExecutor.AbortPolicy`**。在默认情况下，`ThreadPoolExecutor` 将抛出 `RejectedExecutionException` 来拒绝新来的任务 ，这代表你将丢失对这个任务的处理。 

对于**可伸缩的应用程序，建议使用 `ThreadPoolExecutor.CallerRunsPolicy`**。

当**最大池被填满**时，此策略为我们提供**可伸缩队列**。（这个直接查看 `ThreadPoolExecutor` 的构造函数源码就可以看出，比较简单的原因，这里就不贴代码了）

## 一个简单的线程池 Demo

为了让大家更清楚上面的面试题中的一些概念，我写了一个简单的线程池 Demo。

首先创建一个 `Runnable` 接口的实现类（当然也可以是 `Callable` 接口，我们上面也说了两者的区别。）

```java
MyRunnable.java
import java.util.Date;

/**
 * 这是一个简单的Runnable类，需要大约5秒钟来执行其任务。
 * @author shuang.kou
 */
public class MyRunnable implements Runnable {

    private String command;

    public MyRunnable(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.command;
    }
}
```

编写测试程序，我们这里以阿里巴巴推荐的使用 `ThreadPoolExecutor` 构造函数自定义参数的方式来创建线程池。

```java
ThreadPoolExecutorDemo.java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;
    public static void main(String[] args) {

        //使用阿里巴巴推荐的创建线程池的方式
        //通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new MyRunnable("" + i);
            //执行Runnable
            executor.execute(worker);
        }
        //终止线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
```

可以看到我们上面的代码指定了：

1. `corePoolSize`: 核心线程数为 5。
2. `maximumPoolSize` ：最大线程数 10
3. `keepAliveTime` : 等待时间为 1L。
4. `unit`: 等待时间的单位为 TimeUnit.SECONDS。
5. `workQueue`：任务队列为 `ArrayBlockingQueue`，并且容量为 100;
6. `handler`:饱和策略为 `CallerRunsPolicy`。

**Output：**

线程12345分别领到任务各执行一遍, 后12345又分别领到任务执行了一遍,总共是10次任务

```java
pool-1-thread-3 Start. Time = Sun Apr 12 11:14:37 CST 2020
pool-1-thread-5 Start. Time = Sun Apr 12 11:14:37 CST 2020
pool-1-thread-2 Start. Time = Sun Apr 12 11:14:37 CST 2020
pool-1-thread-1 Start. Time = Sun Apr 12 11:14:37 CST 2020
pool-1-thread-4 Start. Time = Sun Apr 12 11:14:37 CST 2020
pool-1-thread-3 End. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-4 End. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-1 End. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-5 End. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-1 Start. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-2 End. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-5 Start. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-4 Start. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-3 Start. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-2 Start. Time = Sun Apr 12 11:14:42 CST 2020
pool-1-thread-1 End. Time = Sun Apr 12 11:14:47 CST 2020
pool-1-thread-4 End. Time = Sun Apr 12 11:14:47 CST 2020
pool-1-thread-5 End. Time = Sun Apr 12 11:14:47 CST 2020
pool-1-thread-3 End. Time = Sun Apr 12 11:14:47 CST 2020
pool-1-thread-2 End. Time = Sun Apr 12 11:14:47 CST 2020
```

## 通过上面的demo分析线程池原理

我们通过代码输出结果可以看出：**线程池首先会先执行 5 个任务，然后这些任务有任务被执行完的话，就会去拿新的任务执行。** 现在，我们就分析上面的输出内容来简单分析一下线程池原理。

通过下图可以更好的对上面这 3 步做一个展示

![图解线程池实现原理](imgs/%E5%9B%BE%E8%A7%A3%E7%BA%BF%E7%A8%8B%E6%B1%A0%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86.png)

我们在代码中模拟了 10 个任务，我们配置的核心线程数为 5 、等待队列容量为 100 ，所以每次只可能存在 5 个任务同时执行，剩下的 5 个任务会被放到等待队列中去。当前的5个任务中如果有任务被执行完了，线程池就会去拿新的任务执行。

**为了搞懂线程池的原理，我们需要首先分析一下 `execute`方法。** 在Demo 中我们使用 `executor.execute(worker)`来提交一个任务到线程池中去，这个方法非常重要，下面我们来看看它的源码：

```java
// 存放线程池的运行状态 (runState) 和线程池内有效线程的数量 (workerCount)
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

private static int workerCountOf(int c) {
    return c & CAPACITY;
}

private final BlockingQueue<Runnable> workQueue;

public void execute(Runnable command) {
    // 如果任务为null，则抛出异常。
    if (command == null)
        throw new NullPointerException();
    // ctl 中保存的线程池当前的一些状态信息
    int c = ctl.get();

    //  下面会涉及到 3 步 操作
    // 1.首先判断当前线程池中执行的任务数量是否小于 corePoolSize
    // 如果小于的话，通过addWorker(command, true)新建一个线程，并将任务(command)添加到该线程中；然后，启动该线程从而执行任务。
    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    // 2.如果当前执行的任务数量大于等于 corePoolSize 的时候就会走到这里
    // 通过 isRunning 方法判断线程池状态，线程池处于 RUNNING 状态才会被并且队列可以加入任务，该任务才会被加入进去
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        // 再次获取线程池状态，如果线程池状态不是 RUNNING 状态就需要从任务队列中移除任务，并尝试判断线程是否全部执行完毕。同时执行拒绝策略。
        if (!isRunning(recheck) && remove(command))
            reject(command);
            // 如果当前线程池为空就新创建一个线程并执行。
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    //3. 通过addWorker(command, false)新建一个线程，并将任务(command)添加到该线程中；然后，启动该线程从而执行任务。
    //如果addWorker(command, false)执行失败，则通过reject()执行相应的拒绝策略的内容。
    else if (!addWorker(command, false))
        reject(command);
}
```

# Atomic 原子类

Atomic 是指一个操作是不可中断的,具有原子/原子操作特征的类。即使是在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程干扰。

并发包 `java.util.concurrent` 的原子类都存放在`java.util.concurrent.atomic`下,如下图所示。

[![JUC原子类概览](https://camo.githubusercontent.com/4216264f4be1f619417bbe18da24985b7ffc5405991b5a2da88bc2ce0b499a8d/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4a55432545352538452539462545352541442539302545372542312542422545362541362538322545382541372538382e706e67)](https://camo.githubusercontent.com/4216264f4be1f619417bbe18da24985b7ffc5405991b5a2da88bc2ce0b499a8d/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4a55432545352538452539462545352541442539302545372542312542422545362541362538322545382541372538382e706e67)

## JUC 包中的原子类是哪 4 类?

### **基本类型**

使用原子的方式更新基本类型

- `AtomicInteger`：整形原子类
- `AtomicLong`：长整型原子类
- `AtomicBoolean`：布尔型原子类

### **数组类型**

使用原子的方式更新数组里的某个元素

- `AtomicIntegerArray`：整形数组原子类
- `AtomicLongArray`：长整形数组原子类
- `AtomicReferenceArray`：引用类型数组原子类

### **3. 引用类型**

- `AtomicReference`：引用类型原子类
- `AtomicStampedReference`：原子更新带有**版本号**的引用类型。该类将整数值与引用关联起来，可用于解决原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。
- `AtomicMarkableReference` ：原子更新带有**标记位**的引用类型

### 4. **对象的属性修改类型**

- `AtomicIntegerFieldUpdater`：原子更新整形字段的更新器
- `AtomicLongFieldUpdater`：原子更新长整形字段的更新器
- `AtomicReferenceFieldUpdater`：原子更新引用类型字段的更新器

## AtomicInteger 的使用

### **AtomicInteger 类常用方法**

```java
public final int get() //获取当前的值
public final int getAndSet(int newValue)//获取当前的值，并设置新的值
public final int getAndIncrement()//获取当前的值，并自增
public final int getAndDecrement() //获取当前的值，并自减
public final int getAndAdd(int delta) //获取当前的值，并加上预期的值
boolean compareAndSet(int expect, int update) //如果输入的数值等于预期值，则以原子方式将该值设置为输入值（update）
public final void lazySet(int newValue)//最终设置为newValue,使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。
```

### **AtomicInteger 类的使用示例**

使用 AtomicInteger 之后，**不用对 increment() 方法加锁也可以保证线程安全**。

```java
class AtomicIntegerTest {
    private AtomicInteger count = new AtomicInteger();
    //使用AtomicInteger之后，不需要对该方法加锁，也可以实现线程安全。
    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
```

### AtomicInteger 线程安全原理

**（1）AtomicInteger中维护了一个使用volatile修饰的变量value，保证可见性；**

**（2）AtomicInteger中的主要方法最终几乎都会调用到Unsafe的compareAndSwapInt()方法保证对变量修改的原子性。**

#### AtomicInteger的compareAndSet方法:

```java
// 获取Unsafe的实例
private static final Unsafe unsafe = Unsafe.getUnsafe();
// 标识value字段的偏移量
private static final long valueOffset;
// 静态代码块，通过unsafe获取value的偏移量
static {
    try {
        valueOffset = unsafe.objectFieldOffset
            (AtomicInteger.class.getDeclaredField("value"));
    } catch (Exception ex) { throw new Error(ex); }
}
// 存储int类型值的地方，使用volatile修饰
private volatile int value;

public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
}
// Unsafe中的方法,参数: 1操作的对象; 2对象中字段的偏移量；3原来的值，即期望的值；4要修改的值；
public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
```

AtomicInteger 类主要利用 CAS (compare and swap) + volatile 和 native本地方法来保证原子操作，从而避免 synchronized 的高开销，执行效率大为提升。

value 是一个 volatile 变量，在内存中可见，因此 JVM 可以保证任何时刻任何线程总能拿到该变量的**最新值**。

UnSafe 类的 objectFieldOffset() 方法是一个本地native方法，这个方法是用来拿到“原来的值”的内存地址，通过反射的方法获取value字段在类中的偏移量, 返回值是 valueOffset。

可以看到，这是一个native方法，底层是使用C/C++写的，主要是调用CPU的CAS指令来实现，它能够保证只有当对应偏移量处的字段值是期望值时才更新，即类似下面这样的两步操作：

```java
if(value == expect) {
    value = newValue;
}
```

通过CPU的CAS指令可以保证这两步操作是一个整体，也就不会出现多线程环境中可能比较的时候value值是a，而到真正赋值的时候value值可能已经变成b了的问题。

#### AtomicInteger的getAndIncrement()方法

```java
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}

// Unsafe中的方法
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
```

getAndIncrement()方法底层是调用的Unsafe的getAndAddInt()方法，这个方法有三个参数：

（1）操作的对象；

（2）对象中字段的偏移量；

（3）要增加的值；

查看Unsafe的getAndAddInt()方法的源码，可以看到它是先获取当前的值，然后再调用compareAndSwapInt()尝试更新对应偏移量处的值，如果成功了就跳出循环，如果不成功就再重新尝试，直到成功为止，这可不就是（CAS+自旋）的乐观锁机制么^^

AtomicInteger中的其它方法几乎都是类似的，最终会调用到Unsafe的compareAndSwapInt()来保证对value值更新的原子性。

关于 Atomic 原子类这部分更多内容可以查看我的这篇文章：并发编程面试必备：[JUC 中的 Atomic 原子类总结](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247484834&idx=1&sn=7d3835091af8125c13fc6db765f4c5bd&source=41#wechat_redirect)

# AQS

AQS 的全称为（`AbstractQueuedSynchronizer`），这个类在`java.util.concurrent.locks`包下面。

[![AQS类](https://camo.githubusercontent.com/a0295b9eee9b4b3047aa66b2cc972d4225062c9a618c121053ee4b66a5a1425a/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4151532545372542312542422e706e67)](https://camo.githubusercontent.com/a0295b9eee9b4b3047aa66b2cc972d4225062c9a618c121053ee4b66a5a1425a/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4151532545372542312542422e706e67)

AQS 是一个用来构建锁和同步器的框架，使用 AQS 能简单且高效地构造出应用广泛的大量的同步器，比如我们提到的 `ReentrantLock`，`Semaphore`，其他的诸如 `ReentrantReadWriteLock`，`SynchronousQueue`，`FutureTask` 等等皆是基于 AQS 的。当然，我们自己也能利用 AQS 非常轻松容易地构造出符合我们自己需求的同步器。

## AQS 原理分析

> 在面试中被问到并发知识的时候，大多都会被问到“请你说一下自己对于 AQS 原理的理解”。下面给大家一个示例供大家参考

### AQS 原理概览

AQS 核心思想是，

- 如果被请求的**共享资源空闲**，则将**当前请求资源的线程**设置为**有效的工作线程**，并且将**共享资源**设置为**锁定状态**。
- 如果被请求的**共享资源被占用**，那么就需要一套**线程阻塞等待**以及**被唤醒时锁分配的机制**，这个机制 AQS 是用 **CLH** 队列锁实现的，即将**暂时获取不到锁的线程加入到队列中**。

> CLH(Craig,Landin,and Hagersten)队列是一个虚拟的**双向队列**（虚拟的双向队列即**不存在队列实例，仅存在结点之间的关联关系**）。AQS 是将每条请求共享资源的**线程**封装成一个 **CLH 锁队列**的一个**结点**（Node）来实现锁的分配。

看个 AQS(AbstractQueuedSynchronizer)原理图：

[![AQS原理图](https://camo.githubusercontent.com/ac2a92061ff7f1176d98be55181fe6615a7137bab4337540f96fc5c2056ba652/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4151532545352538452539462545372539302538362545352539422542452e706e67)](https://camo.githubusercontent.com/ac2a92061ff7f1176d98be55181fe6615a7137bab4337540f96fc5c2056ba652/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4151532545352538452539462545372539302538362545352539422542452e706e67)

AQS 使用一个 int 成员变量来表示同步状态，通过内置的 FIFO 队列来完成获取资源线程的排队工作。AQS 使用 CAS 对该同步状态进行原子操作实现对其值的修改。

```java
private volatile int state;//共享变量，使用volatile修饰保证线程可见性
```

状态信息通过 protected 类型的 getState，setState，compareAndSetState 进行操作

```java
//返回同步状态的当前值
protected final int getState() {
    return state;
}
//设置同步状态的值
protected final void setState(int newState) {
    state = newState;
}
//原子地（CAS操作）将同步状态值设置为给定值update如果当前同步状态的值等于expect（期望值）
protected final boolean compareAndSetState(int expect, int update) {
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
}
```

#### 6.2.2. AQS 对资源的共享方式

**AQS 定义两种资源共享方式**

- Exclusive

    （独占）：只有一个线程能执行，如ReentrantLock

    ​	又可分为公平锁和非公平锁：

    - 公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
    - 非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的

- **Share**（共享）：多个线程可同时执行，如` CountDownLatch`、`Semaphore`、 `CyclicBarrier`、`ReadWriteLock` 我们都会在后面讲到。

`ReentrantReadWriteLock` 可以看成是组合式，因为 `ReentrantReadWriteLock` 也就是读写锁允许多个线程同时对某一资源进行读。

不同的自定义同步器争用共享资源的方式也不同。自定义同步器在实现时只需要实现共享资源 state 的获取与释放方式即可，至于具体线程等待队列的维护（如获取资源失败入队/唤醒出队等），AQS 已经在顶层实现好了。

#### 6.2.3. AQS 底层使用了模板方法模式

同步器的设计是基于模板方法模式的，如果需要自定义同步器一般的方式是这样（模板方法模式很经典的一个应用）：

1. 使用者继承 `AbstractQueuedSynchronizer` 并重写指定的方法。（这些重写方法很简单，无非是对于共享资源 state 的获取和释放）
2. 将 AQS 组合在自定义同步组件的实现中，并调用其模板方法，而这些模板方法会调用使用者重写的方法。

这和我们以往通过实现接口的方式有很大区别，这是模板方法模式很经典的一个运用。

**AQS 使用了模板方法模式，自定义同步器时需要重写下面几个 AQS 提供的模板方法：**

```
isHeldExclusively()//该线程是否正在独占资源。只有用到condition才需要去实现它。
tryAcquire(int)//独占方式。尝试获取资源，成功则返回true，失败则返回false。
tryRelease(int)//独占方式。尝试释放资源，成功则返回true，失败则返回false。
tryAcquireShared(int)//共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
tryReleaseShared(int)//共享方式。尝试释放资源，成功则返回true，失败则返回false。
```

默认情况下，每个方法都抛出 `UnsupportedOperationException`。 这些方法的实现必须是内部线程安全的，并且通常应该简短而不是阻塞。AQS 类中的其他方法都是 final ，所以无法被其他类使用，只有这几个方法可以被其他类使用。

以 ReentrantLock 为例，state 初始化为 0，表示未锁定状态。A 线程 lock()时，会调用 tryAcquire()独占该锁并将 state+1。此后，其他线程再 tryAcquire()时就会失败，直到 A 线程 unlock()到 state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A 线程自己是可以重复获取此锁的（state 会累加），这就是可重入的概念。但要注意，获取多少次就要释放多少次，这样才能保证 state 是能回到零态的。

再以 `CountDownLatch` 以例，任务分为 N 个子线程去执行，state 也初始化为 N（注意 N 要与线程个数一致）。这 N 个子线程是并行执行的，每个子线程执行完后` countDown()` 一次，state 会 CAS(Compare and Swap)减 1。等到所有子线程都执行完后(即 state=0)，会 unpark()主调用线程，然后主调用线程就会从 `await()` 函数返回，继续后余动作。

一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现`tryAcquire-tryRelease`、`tryAcquireShared-tryReleaseShared`中的一种即可。但 AQS 也支持自定义同步器同时实现独占和共享两种方式，如`ReentrantReadWriteLock`。

推荐两篇 AQS 原理和相关源码分析的文章：

- https://www.cnblogs.com/waterystone/p/4920797.html
- https://www.cnblogs.com/chengxiao/archive/2017/07/24/7141160.html

### 6.3. AQS 组件总结

- **`Semaphore`(信号量)-允许多个线程同时访问：** `synchronized` 和 `ReentrantLock` 都是一次只允许一个线程访问某个资源，`Semaphore`(信号量)可以指定多个线程同时访问某个资源。
- **`CountDownLatch `（倒计时器）：** `CountDownLatch` 是一个同步工具类，用来协调多个线程之间的同步。这个工具通常用来控制线程等待，它可以让某一个线程等待直到倒计时结束，再开始执行。
- **`CyclicBarrier`(循环栅栏)：** `CyclicBarrier` 和 `CountDownLatch` 非常类似，它也可以实现线程间的技术等待，但是它的功能比 `CountDownLatch` 更加复杂和强大。主要应用场景和 `CountDownLatch` 类似。`CyclicBarrier` 的字面意思是可循环使用（`Cyclic`）的屏障（`Barrier`）。它要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。`CyclicBarrier` 默认的构造方法是 `CyclicBarrier(int parties)`，其参数表示屏障拦截的线程数量，每个线程调用 `await()` 方法告诉 `CyclicBarrier` 我已经到达了屏障，然后当前线程被阻塞。

### 6.4. 用过 CountDownLatch 么？什么场景下用的？

`CountDownLatch` 的作用就是 允许 count 个线程阻塞在一个地方，直至所有线程的任务都执行完毕。之前在项目中，有一个使用多线程读取多个文件处理的场景，我用到了 `CountDownLatch` 。具体场景是下面这样的：

我们要读取处理 6 个文件，这 6 个任务都是没有执行顺序依赖的任务，但是我们需要返回给用户的时候将这几个文件的处理的结果进行统计整理。

为此我们定义了一个线程池和 count 为 6 的`CountDownLatch`对象 。使用线程池处理读取任务，每一个线程处理完之后就将 count-1，调用`CountDownLatch`对象的 `await()`方法，直到所有文件读取完之后，才会接着执行后面的逻辑。

伪代码是下面这样的：

```java
public class CountDownLatchExample1 {
    // 处理文件的数量
    private static final int threadCount = 6;

    public static void main(String[] args) throws InterruptedException {
        // 创建一个具有固定线程数量的线程池对象（推荐使用构造方法创建）
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int threadnum = i;
            threadPool.execute(() -> {
                try {
                    //处理文件的业务操作
                    //......
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //表示一个文件已经被完成
                    countDownLatch.countDown();
                }

            });
        }
        countDownLatch.await();
        threadPool.shutdown();
        System.out.println("finish");
    }
}
```

**有没有可以改进的地方呢？**

可以使用 `CompletableFuture` 类来改进！Java8 的 `CompletableFuture` 提供了很多对多线程友好的方法，使用它可以很方便地为我们编写多线程程序，什么异步、串行、并行或者等待所有线程执行完任务什么的都非常方便。

```
CompletableFuture<Void> task1 =
    CompletableFuture.supplyAsync(()->{
        //自定义业务操作
    });
......
CompletableFuture<Void> task6 =
    CompletableFuture.supplyAsync(()->{
    //自定义业务操作
    });
......
CompletableFuture<Void> headerFuture=CompletableFuture.allOf(task1,.....,task6);

try {
    headerFuture.join();
} catch (Exception ex) {
    //......
}
System.out.println("all done. ");
```

上面的代码还可以接续优化，当任务过多的时候，把每一个 task 都列出来不太现实，可以考虑通过循环来添加任务。

```
//文件夹位置
List<String> filePaths = Arrays.asList(...)
// 异步处理所有文件
List<CompletableFuture<String>> fileFutures = filePaths.stream()
    .map(filePath -> doSomeThing(filePath))
    .collect(Collectors.toList());
// 将他们合并起来
CompletableFuture<Void> allFutures = CompletableFuture.allOf(
    fileFutures.toArray(new CompletableFuture[fileFutures.size()])
);
```