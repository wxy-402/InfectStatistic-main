## 代码风格

 1. ###缩进

    - 缩进采用4个空格，禁止使用tab字符。

 2. ###变量命名

    - 1.代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。

    - 2.代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。

    - 3.方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
    
    - 4.中括号是数组类型的一部分，数组定义如下：String[] args;

    - 5.POJO类中布尔类型的变量，都不要加is，否则部分框架解析会引起序列化错误。
    
    - 6.杜绝完全不规范的缩写，避免望文不知义。


 3. ###每行最多字符数

    - 单行字符数限制不超过 120个，超出需要换行，换行时遵循如下原则：
    
        - 第二行相对第一行缩进 4个空格，从第三行开始，不再继续缩进，参考示例。
        
        - 运算符与下文一起换行。
        
        - 在多个参数超长，逗号后进行换行。
        
        - 在多个参数超长，逗号后进行换行。
        
 4. ###函数最大行数

    - 以80、200、500为规定界限，超过80行的函数即为超大函数，即进行拆分，规定最大行数为80-200行，拆分时遵循如下原则：
    
        - 每一个代码块都可以封装为一个函数
        
        - 每一个循环体都可以封装为一个函数
        
        - 每一个条件体都可以封装为一个函数

 5. ###函数、类命名

    - 1.类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO等。
    
  
    - 2.方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
    
    - 3.抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。
    
    - 4.杜绝完全不规范的缩写，避免望文不知义。

    - 5.如果使用到了设计模式，建议在类名中体现出具体模式。
    
    - 6.接口类中的方法和属性不要加任何修饰符号（public也不要加），保持代码的简洁性，并加上有效的Javadoc注释。尽量不要在接口里定义变量，如果一定要定义变量，肯定是与接口方法相关，并且是整个应用的基础常量。
    
  
    - 7.接口和实现类的命名有两套规则：
    
    - 8.对于Service和DAO类，基于SOA的理念，暴露出来的服务一定是接口，内部的实现类用Impl的后缀与接口区别。

    - 9.如果是形容能力的接口名称，取对应的形容词做接口名（通常是–able的形式）。
    
    - 10.枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开。

 6. ###常量

    - 1.不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。
  
    - 2.long或者Long初始赋值时，必须使用大写的L，不能是小写的l，小写容易跟数字1混淆，造成误解。
    
    - 3.不要使用一个常量类维护所有常量，应该按常量功能进行归类，分开维护。如：缓存相关的常量放在类：CacheConsts下；系统配置相关的常量放在类：ConfigConsts下。
    
    - 4.常量的复用层次有五层：跨应用共享常量、应用内共享常量、子工程内共享常量、包内共享常量、类内共享常量。
    
        - 跨应用共享常量：放置在二方库中，通常是client.jar中的constant目录下。
        
        - 应用内共享常量：放置在一方库的modules中的constant目录下。
    
        - 子工程内部共享常量：即在当前子工程的constant目录下。
        
        - 包内共享常量：即在当前包下单独的constant目录下。
       
        - 类内共享常量：直接在类内部private  static  final定义。
    
    - 5.如果变量值仅在一个范围内变化用Enum类。如果还带有名称之外的延伸属性，必须使用Enum类，下面正例中的数字就是延伸信息，表示星期几。
    


 7. ###空行规则

    - 1.大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
    
        - 左大括号前不换行。
      
        - 左大括号后换行。
      
        - 右大括号前换行。
      
        - 右大括号后还有else等代码则不换行；表示终止右大括号后必须换行。
    
    
    
    - 2.方法体内的执行语句组、变量的定义语句组、不同的业务逻辑之间或者不同的语义之间插入一个空行。相同业务逻辑和语义之间不需要插入空行。

 8. ###注释规则

    - 1.类、类属性、类方法的注释必须使用Javadoc规范，使用/**内容*/格式，不得使用//xxx方式。

    - 2.所有的抽象方法（包括接口中的方法）必须要用Javadoc注释、除了返回值、参数、异常说明外，还必须指出该方法做什么事情，实现什么功能。

    - 3.所有的类都必须添加创建者信息。
    
    - 4.方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/* */注释，注意与代码对齐。
    
    - 5.所有的枚举类型字段必须要有注释，说明每个数据项的用途。
    
    - 6.与其“半吊子”英文来注释，不如用中文注释把问题说清楚。专有名词与关键字保持英文原文即可。

    - 7.代码修改的同时，注释也要进行相应的修改，尤其是参数、返回值、异常、核心逻辑等的修改。
    
    - 8.注释掉的代码尽量要配合说明，而不是简单的注释掉。

    - 9.对于注释的要求：
    
        - 第一、能够准确反应设计思想和代码逻辑；
    
        - 第二、能够描述业务含义，使别的程序员能够迅速了解到代码背后的信息。完全没有注释的大段代码对于阅读者形同天书，注释是给自己看的，即使隔很长时间，也能清晰理解当时的思路；注释也是给继任者看的，使其能够快速接替自己的工作。
    
    - 10.好的命名、代码结构是自解释的，注释力求精简准确、表达到位。避免出现注释的一个极端：过多过滥的注释，代码的逻辑一旦修改，修改注释是相当大的负担。
    
  
    - 11.特殊注释标记，请注明标记人与标记时间。注意及时处理这些标记，通过标记扫描，经常清理此类标记。线上故障有时候就是来源于这些标记处的代码。
    

 9. ###操作符前后空格

    - 1.左括号和后一个字符之间不出现空格；同样，右括号和前一个字符之间也不出现空格。详见第5条下方正例提示。
    
    - 2.if/for/while/switch/do等保留字与左右括号之间都必须加空格。

    - 3.任何运算符左右必须加一个空格。
   
    - 4.方法参数在定义和传入时，多个参数逗号后边必须加空格。


 10. ###其他规则

    - 无

    