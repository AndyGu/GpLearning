package com.bard.gplearning.lifecycle

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.TextView

fun main() {

    val p: Person = Man("andy", 12)

//    val p1: ArrayList<Person> = ArrayList<Man>() //报错
//    Required: kotlin.collections.ArrayList<Person>
//    Found: kotlin.collections.ArrayList<Man>

//    ArrayList<Person> 并不是 ArrayList<Man>的父类，他们都是List接口的子类，相互之间并没有继承关系所以编译器并不能匹配正确的引用类型


    val p2: List<Person> = ArrayList<Man>() //正常
    val p3: List<Person> = listOf<Man>() //正常

//    这是因为List接口在kotlin中它的泛型类型已经被协变了：
//    public interface List<out E> : Collection<E> {...}

//    java为了解决这个问题提供了<? extends T>通配符，而在kotlin中将这个通配符用out关键字来替代
//    out 修饰 Person表明p1这个集合对象中存的可以是Person及其子类的对象：

    val p4: ArrayList<out Person> = ArrayList<Man>() //正常

//    1.Man 是 Person 的子类
//    2.同时ArrayList<Man> 又是 ArrayList<out Person>的子类
//    那么我们就可以说 ArrayList 在 Person 这个类型上是协变(covariance)的。


//    一个 泛型类 或者 泛型接口 中的方法 接收入参的位置称为in位置，返回值输出数据的位置称为out位置
//    interface MyClass<T>{
//        fun method(param: T【in位置】): T【out位置】
//    }


    //继续实验，声明一个泛型类MyClass，并向外部提供set get方法

    val data = MyClass<Man>()
    data.set(Man("Jack", 33))

//    test(data) //报错，因为test方法接收 MyClass<Person>，data是MyClass<Man>类型 Man是Person子类，但是 MyClass<Man>不是MyClass<Person>子类
//    如果不报错，在test方法里一设置，那么再调用data.get()，效果岂不是传入Man获取到Woman？而get返回的泛型应该与传入的泛型相同，就矛盾了
    data.get()

//    换个思维，之所以会出现类转换异常，就是因为test方法中给set了一个Woman对象导致了问题，
//    如果说 MyClass在泛型T上是只读的即没有set方法那么就不会因为set一个Woman对象导致类型转换异常。

//    来了！！！：
//    kotlin为了实现这个《只能读不能写》的功能而提供了out关键字，即这个泛型T只能出现在out位置不能出现在in位置：
//    例如 MyClass2

//    特别要注意的是由于data《只读不写》，所以在声明时需要val修饰，
//    如果用var修饰则表明data是可写的，它仍然处在in位置上，除非用private var修饰，
//    这样写就表明 data不暴露给外部，它仍然是不可写的即不处在in位置上：
//    例如 MyClass3


//    至此，我们就可以说 MyClass3在 泛型T上 是协变的。

//    val data3 = MyClass3<Person>(Man("aaa", 21)) //不报错
    val data3 = MyClass3<Man>(Man("aaa", 21))
    test3(data3) //不报错，test3接收 MyClass3<Person>类型，传入MyClass3<Man>类型，因为使用了协变，无法set设置 data3的变量，所以不报错
    data3.get() //get的还是原汁原味的data3

//    再来看一种特殊情况：真的需要泛型出现在in位置上怎么办？

//    public interface List<out E> : Collection<E> {
//        override val size: Int
//        override fun isEmpty(): Boolean
//        override fun contains(element: @UnsafeVariance E): Boolean
//        override fun iterator(): Iterator<E>
//    }
//    看这里：contains方法，E出现在了in位置
//    本身这样不合理，但是contains方法只做了判断，也没写入数据，所以，针对这种特殊情况
//    引入了 @UnsafeVariance 注解
//    例如 MyClass4


//    讲完了《把子类当父类用》《泛型不出现在in位置》《可读不可写》的协变
//    再来看看逆变
//    val p5 : ArrayList<Man> = arrayListOf<Person>() //报错
    val p6: Man = Person("jack", 12) as Man  //正常


//    逆变和协变是相反的，但其实道理是一样的，
//    之所以第一句编译报错就是因为 ArrayList<Man> 并不是 ArrayList<Person> 的子类 无法进行类型强转。
//    同样的，java为了解决这个问题提供了<? super T>通配符，而在kotlin中将这个通配符用in关键字来替代，
//    in修饰 Man表明 p7这个集合对象中存的可以是Man及其父类的对象：《规定下界》

    val p7: ArrayList<in Man> = arrayListOf<Person>() //正常

//    1.Person 是 Man的父类
//    2.同时ArrayList<Person> 又是 ArrayList<in man> 的父类，
//    那么我们就可以说ArrayList在Man这个类型上是逆变(contravariant)的。

//    继续实验，声明一个泛型接口 MyClass5

    val data5 = object : MyClass5<Person>{
        override fun show(d: Person?): Person? {
            Log.e("show", "${d?.name}---${d?.age}")
            return Woman(d?.name ?: "null", d?.age ?: 0)
        }
    }

//    test5(data5) //报错 type mismatch
//    Required: MyClass5<Man>
//    Found: ``

//    test5接收 MyClass5<Man>，传入 MyClass5<Person>，报错。
//    Person是Man的父类，但是 MyClass5<Person> 并不是 MyClass5<Man>的父类

//    如果不报错，那么show方法中实现，其实实际返回的是Woman，就和test5方法的要求相违背，就矛盾了

//    换个角度想，之所以出错是因为 show方法要去返回一个Person对象导致了问题，
//    如果说MyClass5在反省T上是只写的，即 不允许泛型T出现在out位置上，
//    那么就不会因为show方法返回一个 Woman对象而导致的类型转换问题

//    kotlin为实现这个《只能写不能读》的功能而提供了in关键字，即这个泛型T只能出现在in位置不能出现在out位置


    val data6 = object : MyClass6<Person>{
        override fun show(d: Person?) {
            Log.e("show", "可以各种set，但是没有返回值 ${d?.name}---${d?.age}")
        }
    }

    test6(data6) //正常

//    可以想象，逆变中也可以使用 @UnsafeVariance 注解来强行让泛型T作为输出位置，但这样做很危险，易出错

    val data7 = object : MyClass7<Person>{
        override fun show(d: Person?): Person? {
            Log.e("show", "${d?.name}---${d?.age}")
            return Woman(d?.name ?: "null", d?.age ?: 0)
        }
    }
    test7(data7) //编译不报错，运行时报错


    /**
     * Java                 Kotlin          边界          场景
     * <? extends T>        out【协变】     上边界         取值
     * <? super T>          in【逆变】      下边界         存值
     * <T>                  <T>【不变】       -          存值/取值
     */

    var textViews1 : List<*>
    var textViews2 : List<out Any>
//    等同java中的：
//      List<?> textViews;
//      List<? extends Object> textViews;





//    interface Counter<out T: Number>{
//        fun count(): T
//    }

//var counter: Counter<*> = ...
//等价于
//var counter: Counter<out Number> = ...


//    Java: 泛型声明的时候，可以通过extends设置上界
//    注意这里是类型声明时，不是声明变量时 或者 方法参数中的使用时 带问号的那个东西
//    这个上界是可以设置成多重的，用&符号连接
//
//    class Monster<T extends Animal & Food>{
//    }
//
//
//    kotlin：
//    1. extends变成了冒号 ：
//    2. 当多个上界时，从尖括号里拿出来，并使用where关键字
//
//    class Monster<T: Animal>{
//    }
//
//    class Monster<T> where T: Animal, T: Food{
//    }

}

open class Person(val name: String, val age : Int){}

class Man(val mName: String, val mAge: Int, val male: String = "man") : Person(mName, mAge){}

class Woman(val wName: String, val wAge: Int, val female: String = "woman"): Person(wName, wAge){}


class MyClass<T>{
    private var data: T? = null

    fun set(t: T?){
        data = t
    }

    fun get():T? = data
}

fun test(data: MyClass<Person>){
    data.set(Woman("Anna", 22))
}


class MyClass2<out T>(val data1: T?){
    private var data2: T? = null

//    fun set(t: T?){ //报错
//        data2 = t
//    }

    fun get():T? = data1

    fun get2():T? = data2
}


fun test3(data3: MyClass3<Person>){
    data3.set(Woman("Anna", 22))
}

class MyClass3<out T>(private var data: T?){
    fun get():T? = data

//    fun set(t: T?){ //报错，泛型不能出现在in位置，可以有set方法，但是无法写入数据
//    }

    fun set(person: Person?){
//        data = person //报错，data是T泛型啊，这里可以有set方法，但是无法写入数据
    }
}


class MyClass4<out T>(private var data: T?){
    fun get():T? {
        return data
    }

    fun set(t: @UnsafeVariance T?){
        data = t //很危险！！！
    }
}




interface MyClass5<T> {
    fun show(d: T?): T?
}

fun test5(d: MyClass5<Man>){
    val result = d.show(Man("simon", 42))
}



interface MyClass6<in T> {
    fun show(d: T?)
}

fun test6(d: MyClass6<Man>){
    d.show(Man("simon", 42))
}




interface MyClass7<in T>{
    fun show(d: T?): @UnsafeVariance T?
}

fun test7(d: MyClass7<Man>) {
    val result = d.show(Man("sim", 12)) //运行时报类转换异常
}











