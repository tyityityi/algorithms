# Java基础

## 基本知识

### 为什么说 Java 语言“编译与解释并存”？

**编译型语言**是指编译器针对特定的操作系统将源代码**一次性翻译**成可被该平台执行的机器码；

**解释型语言**是指解释器对源程序**逐行解释**成特定平台的机器码并立即执行。

Java 语言既具有编译型语言的特征，也具有解释型语言的特征，因为 Java 程序要经过**先编译，后解释**两个步骤：

- 由 Java 编写的程序需要先经过**JDK中的javac**编译，生成**字节码**（`\*.class` 文件）；

- 这种字节码必须由 Java **解释器**来解释执行。因此，我们可以认为 Java 语言编译与解释并存。

### JVM JDK JRE

#### **JDK** 

Java Development Kit 缩写，包括 **JRE**+编译器（javac）+工具（如 javadoc 和 jdb）。能**创建**和**编译**程序。

#### **JRE** 

Java 运行时**环境**。它是运行**已编译** Java 程序所需的所有内容的集合，包括 **JVM** (Java 虚拟机)，Java 类库，java 命令和其他的一些基础构件。但是，**它不能用于创建新程序。**

#### **JVM (Java 虚拟机)**

<img src="imgs/java%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B.png" alt="java执行流程" style="width:35%;" />

是运行 Java **字节码**的虚拟机。JVM 有针对**不同系统**的特定实现（Windows，Linux，macOS），目的是使用相同的字节码，它们都会给出相同的结果。

##### **字节码**

在 Java 中，JVM 可以理解的代码就叫做字节码（即扩展名为 **.class** 的文件），它不面向任何特定的处理器，**只面向JVM**。Java 语言通过字节码的方式，在一定程度上**解决了传统解释型语言执行效率低的问题**，同时又保留了**解释型语言可移植的特点**。所以 Java 程序运行时比较**高效**。

<img src="imgs/jdkjvm%E5%85%B3%E7%B3%BB.png" alt="jdkjvm关系" style="width:88%;" />

**.class --> 机器码** 这一步JVM 类加载器首先加载字节码文件，然后通过解释器逐行解释执行，这种方式的执行速度会相对比较慢。而且，有些方法和代码块是经常需要被调用的(也就是所谓的热点代码)，所以后面引进了 **JIT 编译器**，而 JIT 属于**运行时编译**。当 JIT 编译器完成**第一次编译后**，其会将**字节码对应的机器码保存**下来，下次可以直接使用。而我们知道，**机器码的运行效率肯定是高于 Java 解释器的**。这也解释了我们为什么经常会说 **Java 是编译与解释共存的语言。**

### 字符型常量char vs 字符串常量String

1. **形式** : **字符常量char**是**单引号**引起的一个字符，**字符串常量String**是**双引号**引起的 0 个或若干个字符

2. **含义** : **字符常量char**相当于一个**整型值( ASCII 值)**,可以参加表达式运算; 字符串常量String代表一个**地址值**(该字符串在内存中存放位置)

3. **占内存大小** ： **字符常**量只占 **2 个字节**; 字符串常量占若干个字节 (**注意： char 在 Java 中占两个字节**),

    > 字符封装类 `Character` 有一个成员常量 `Character.SIZE` 值为 16,单位是`bits`,该值除以 8(`1byte=8bits`)后就可以得到 2 个字节

### 关键字（标识符的一种）

**标识符**：为程序、类、变量、方法等取的**名字**

**关键字**：被赋予特殊含义的**标识符**

<img src="imgs/%E5%85%B3%E9%94%AE%E5%AD%97.png" alt="关键字" style="width:90%;" />

### 自增自减运算符(a++ vs++a)

- 当 **b = ++a** 时，先自增（自己增加 1），再赋值（赋值给 b）；++a 输出的是 a+1 的值

- 当 **b = a++** 时，先赋值(赋值给 b)，再自增（自己增加 1）；a++输出的是 a 值。用一句口诀就是：“符号在前就先加/减，符号在后就后加/减”。

### continue、break、和 return 的区别是什么？

在循环结构中，当循环条件不满足或者循环次数达到要求时，循环会正常结束。但是，有时候可能需要在循环的过程中，当发生了某种条件之后 ，**提前终止循环**，这就需要用到下面几个关键词：

- **continue** ：指跳出当前的这一次循环，继续下一次循环。

- **break** ：指跳出整个循环体，继续执行循环下面的语句。

- **return** **value**; ：return 一个特定值，用于有返回值函数的方法

### **泛型？常用的通配符？**

泛型generics提供了编译时**类型安全检测**机制，该机制允许程序员在编译时检测到非法的类型。泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。

**常用的通配符为： T，E，K，V，？**

- **？ 表示不确定的 java 类型**

- **`T<type>` 表示具体的一个 java 类型**

- **`K, V <key, value>` 分别代表 java 键值中的 Key Value**

- **E (element) 代表 Element**

#### 1、**泛型类**：

```java
//此处T可随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
//在实例化泛型类时，必须指定T的具体类型
public class Generic<T> {

    private T key;

    public Generic(T key) {
        this.key = key;
    }

    public T getKey() {
        return key;
    }
}
```

如何实例化泛型类：

```java
Generic<Integer> genericInteger = new Generic<Integer>(123456);                               
```

#### 2、**泛型接口：**

```java
public interface Generator<T> {
    public T method();
}
```

实现泛型接口，不指定类型：

```java
class GeneratorImpl<T> implements Generator<T>{
    @Override
    public T method() {
        return null;
    }
}
```

实现泛型接口，指定类型：

```java
class GeneratorImpl implements Generator<String>{
    @Override
    public String method() {
        return "hello";
    }
}
```

#### **3、泛型方法：**

```java
public static <E> void printArray(E[] inputArray) {
    for (E element : inputArray) {
        System.out.printf("%s ", element);
    }
    System.out.println();
}
```

使用：

```java
// 创建不同类型数组： Integer, Double 和 Character
Integer[] intArray = { 1, 2, 3 };
String[] stringArray = { "Hello", "World" };
printArray(intArray);
printArray(stringArray);
```

### 类型擦除

Java的泛型是**伪泛型**，这是因为Java在**编译期间**，所有的**泛型信息都会被擦掉** 。

### **== vs equals**

- **==** : 它的作用是判断两个对象的**地址**是不是相等。即判断两个对象是不是同一个对象。(**基本数据类型==比较的是值，引用数据类型==比较的是内存地址**)

- **equals()** : 它的作用也是判断两个对象是否相等，它**不能用于比较基本数据类型**的变量。

    equals()方法存在于Object类中，而Object类是所有类的直接或间接父类。

    `equals()` 方法存在两种使用情况：

    - **类没有覆盖 `equals()`方法** ：通过`equals()`比较该类的两个对象时，等价于通过“==”比较这两个对象的**内存地址**，使用的默认是 `Object`类`equals()`方法。

        ```java
        public boolean equals(Object obj) {
             return (this == obj);
        }
        ```

    - **类覆盖了 `equals()`方法** ：一般我们都覆盖 `equals()`方法来比较两个对象中的**属性**是否相等；若它们的属性相等，则返回 true(即，认为这两个对象相等)。

#### String中的equals()

- 当创建 `String` 类型的对象时，虚拟机会在常量池中查找有没有已经存在的值和要创建的值相同的对象，如果有就把它赋给当前引用。如果没有就在常量池中重新创建一个 `String` 对象。

- `String` 中的 `equals` 方法是被重写过的，因为 `Object` 的 `equals` 方法是比较的对象的**内存地址**，而 `String` 的 `equals` 方法比较的是**对象的值**。

    String类equals源码：

    ```java
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                  	//!!!!比较的是String每个char的值
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
    ```

例子：

```java
public class test1 {
    public static void main(String[] args) {
      	System.out.println(42 == 42.0); // true
      
      	String a = new String("ab"); // a 为一个引用，指向堆中的某个地址
        String b = new String("ab"); // b为另一个引用,对象的内容一样
        System.out.println(a == b); // false，非同一对象
        System.out.println(a.equals(b)); // true
      
      	String aa = "ab"; // 放在常量池中
        String bb = "ab"; // 从常量池中查找
        System.out.println(aa==bb);// true
      	System.out.println(aa==a);// false，a指向堆的地址，aa指向常量池的地址
      	
      	String abab1 = "abab";
      	String abab2 = aa + bb;
      	System.out.println(abab1==abab2);//false，因为abab2相当于new了一下，有新地址
      	final String aaa = "ab";
      	final String bbb = "ab";
      	String abab3 = aaa+bbb;
	      System.out.println(abab1==abab3);//true，因为aaa、bbb被final修饰即为常量，编译器做常量相加会做优化所以为true
      	
        
    }
}
```

### **hashCode()与 equals()**

当你把对象加入 **HashSet** 时，HashSet 会先计算**对象的** **hashcode** 值来判断对象加入的位置，同时也会与其他**已经加入的对象的 hashcode** 值作**比较**。

- 如果没有相同的 hashcode，HashSet 会假设对象没有重复出现。

- 如果有相同 hashcode 值的对象，这时会调用 equals() 方法来检查 **hashcode 相等的对象**是否**对象本身的值也相同**：
    - 如果两者相同，HashSet 就不会让其加入操作成功（重复出现）；
    - 如果不同的话，就会重新散列到其他位置。

这样我们就**减少了 equals 的次数(只用==)，相应就大大提高了执行速度。**

### **为什么重写equals时必须重写hashCode方法**

> `hashCode()`的默认行为是对堆上的对象产生独特值。如果没有重写 `hashCode()`，则该 class 的**两个对象**无论如何都**不会相等**（即使这两个对象**指向相同的数据**）

如果两个对象相等，则 hashcode 一定也是相同的。

两个对象相等,对两个对象分别调用 equals 方法都返回 true。

但是，如果两个对象有相同的 hashcode 值，它们也不一定是相等的 。**因此，equals 方法被覆盖过，则 `hashCode` 方法也必须被覆盖。**

## 基本数据类型

### 基本数据类型及对应的包装类型

| 基本类型  | 位数 | 字节 | 默认值  | 包装类型  |
| --------- | ---- | ---- | ------- | --------- |
| `int`     | 32   | 4    | 0       | Integer   |
| `short`   | 16   | 2    | 0       | Short     |
| `long`    | 64   | 8    | 0L      | Long      |
| `byte`    | 8    | 1    | 0       | Byte      |
| `char`    | 16   | 2    | 'u0000' | Character |
| `float`   | 32   | 4    | 0f      | Float     |
| `double`  | 64   | 8    | 0d      | Double    |
| `boolean` | 1    |      | false   | Boolean   |

另外，对于 `boolean`，官方文档未明确定义，它依赖于 JVM 厂商的具体实现。逻辑上理解是占用 1 位，但是实际中会考虑计算机高效存储因素。

**注意：**

1. Java 里使用 `long` 类型的数据一定要在数值后面加上 **L**，否则将作为整型解析。
2. `char a = 'h'`char :单引号，`String a = "hello"` :双引号;
3. 包装类型不赋值就是 `Null` ，而基本类型有默认值且不是 `Null`。

另外，这个问题建议还可以先从 JVM 层面来分析。

**基本数据类型**直接存放在 Java 虚拟机**栈**中的**局部变量表**中，而**包装类**型属于**对象**类型，我们知道对象实例都存在于**堆**中。相比于对象类型， 基本数据类型占用的空间非常小。

> 《深入理解 Java 虚拟机》 ：**局部变量**表主要存放了编译期可知的基本数据类型**（boolean、byte、char、short、int、float、long、double）**、**对象引用**（reference 类型，它不同于对象本身，可能是一个指向对象起始地址的引用指针，也可能是指向一个代表对象的句柄或其他与此对象相关的位置）。

### 自动装箱与拆箱

- **装箱**：将**基本类型**用它们对应的**引用类型**包装起来；
- **拆箱**：将包装类型转换为基本数据类型；

举例：

```java
Integer i = 10;  //装箱
int n = i;   //拆箱
```

装箱其实就是调用了 包装类的`valueOf()`方法，拆箱其实就是调用了 `xxxValue()`方法。

因此，

- `Integer i = 10` 等价于 `Integer i = Integer.valueOf(10)`
- `int n = i` 等价于 `int n = i.intValue()`;

### 8 种基本类型的包装类和常量池

两种浮点数类型的包装类 `Float`,`Double` 并没有实现常量池技术。

```JAVA
Float i11 = 333f;
Float i22 = 333f;
System.out.println(i11 == i22);// 输出 false

Double i3 = 1.2;
Double i4 = 1.2;
System.out.println(i3 == i4);// 输出 false
```

常量池技术：`Byte`,`Short`,`Integer`,`Long` 这 4 种包装类默认创建了数值 **[-128，127]** 的相应类型的**缓存数据**，`Character` 创建了数值在[0,127]范围的缓存数据，`Boolean` 直接返回 `True` Or `False`。

**Integer 缓存源码：**

```java
/**

*此方法将始终缓存-128 到 127（包括端点）范围内的值，并可以缓存此范围之外的其他值。

*/

public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
      return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);

}

private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];
}
```

**`Boolean` 缓存源码：**

```java
public static Boolean valueOf(boolean b) {
    return (b ? TRUE : FALSE);
}
```

**如果未超出缓存范围，会直接使用常量池中的对象**

```java
Integer i1 = 33;
Integer i2 = 33;
System.out.println(i1==i2);// 输出 true
System.out.println(i1.equals(i2));//true
```

**new Integer会新开辟一块内存，而不是从常量池里拿**

```java
Integer i1 = 40;
Integer i2 = new Integer(40);
System.out.println(i1==i2);//false
System.out.println(i1.equals(i2));//true
```

`Integer i1=40` 这一行代码会发生**装箱**，也就是说这行代码等价于 `Integer i1=Integer.valueOf(40)` ，直接使用的是**常量池中的对象**；而`Integer i2 = new Integer(40)` 会开辟一块新内存，创建新的对象。

**如果超出缓存范围，会去创建新的对象，所以未超出缓存范围的包装类可以用==来比较值；超出缓存的包装类==比较的是内存地址**（对象引用？）

```java
Integer i3 = 128;
Integer i4 = 128;
System.out.println(i3==i4);//false
System.out.println(i3.equals(i4));//true
```

#### Integer和int的比较：

实际比较的是**数值**，Integer会做**拆箱**的动作，来跟基本数据类型做比较，此时跟是否在缓存范围内或是否new都没关系 

```Java
Integer i3 = 128;
int i4 = 128;
System.out.println(i3==i4);//true
System.out.println(i3.equals(i4));//true
```

记住：**所有整型包装类对象之间值的比较，全部使用 equals 方法比较**。

> 《阿里巴巴Java开发手册终极版v1.3.0》
>
> 【强制】所有的相同类型的**包装类**对象之间值的比较，全部使用 equals 方法比较。
>
> 说明:对于 Integer var = ? 在-128 至 127 范围内的赋值，Integer 对象是在IntegerCache.cache 产生，会复用已有对象，这个区间内的 Integer 值可以直接使用==进行判断，但是这个区间之外的所有数据，都会在堆上产生，并不会复用已有对象，这是一个大坑， 推荐使用 equals 方法进行判断。