## 面向对象设计与构造第三次作业

------

### 第一部分：训练目标

通过对数学意义上的表达式结构进行建模，完成多项式的括号展开与函数调用、化简，进一步体会层次化设计的思想的应用和工程实现。

------

### 第二部分：预备知识

- 1、Java 基础语法与基本容器的使用。
- 2、扩展 BNF 描述的形式化表述。
- 3、正则表达式、递归下降或其他解析方法。

------

### 第三部分：题目描述

本次作业中需要完成的任务为：读入**一系列自定义函数的定义**以及一个包含幂函数、三角函数、自定义函数调用、求导算子的**表达式**，输出**恒等变形展开所有括号后**的表达式。

在本次作业中，**展开所有括号**的定义是：对原输入表达式 E做**恒等变形**，得到新表达式 E′。其中，E′ 中不再含有自定义函数，不再含有求导算子，且只包含**必要的括号**（必要括号的定义见**公测说明-正确性判定**）。

在本次作业中，**自定义函数** 指自定义递推函数和自定义普通函数。

------

### 第五部分：基本概念

#### 一、基本概念的声明

- **带符号整数** **支持前导 0**的**十**进制带符号整数（若为正数，则正号可以省略），无进制标识。如： `+02`、`-16`、`19260817`等。

- **因子**

  - **变量因子**

  - **幂函数**

  - **一般形式** 由自变量 x，指数符号 `^` 和指数组成，指数为一个**非负**带符号整数，如：`x ^ +2`,`x ^ 02`,`x ^ 2` 。

  - **省略形式** 当指数为 1 的时候，可以省略指数符号 `^` 和指数，如：`x` 。

  - **三角函数**

    - **一般形式** 类似于幂函数，由`sin(<因子>)`或`cos(<因子>)` 、指数符号`^`和指数组成，其中：
    - 指数为符号不是 `-` 的整数，如：`sin(x) ^ +2`。
    - **省略形式** 当指数为 1 的时候，可以采用省略形式，省略指数符号`^`和指数部分，如：`sin(x)`。
    - 本指导书范围内的“三角函数”**仅包含`sin`和`cos`**。

  - **自定义递推函数**

    - 自定义递推函数的**定义**形如

    ```tex
    f{n}(x, y) = 递推表达式
    f{0}(x, y) = 函数表达式
    f{1}(x, y) = 函数表达式
    ```

    三者顺序任意，以换行分隔。递推表达式和函数表达式的定义见“形式化表述”部分。

    定义中默认 n>1*n*>1。

    例如

    ```tex
    f{0}(y) = y
    f{1}(y) = 1
    f{n}(y) = 1*f{n-1}(sin(y)) - 4*f{n-2}(y^2) + 1
    ```

    ```tex
    f{0}(x, y) = x - y
    f{n}(x, y) = 0*f{n-1}(x, y) + 35*f{n-2}(x, y^2)
    f{1}(x, y) = x^3 + y
    ```

    - `f` 是递推函数的**函数名**。在本次作业中，保证函数名只使用`f`，且每次只有1个自定义递推函数。 `n` 、`0` 、 `1` 是递推函数的**序号**。

  - `x`、`y` 是递推函数的形参。在本次作业中，**形参个数为 1~2 个**。形参**只使用x，y**，且同一函数定义中不会出现重复使用的形参。对一个自定义递推函数的定义来说，`f{n}`、`f{n-1}`、`f{n-2}`、…、`f{1}`、`f{0}`等一系列函数的形参统一，不会出现同一系列中函数形参不同的情况。

    - 递推表达式是一个关于形参的表达式，**保证其中 `f{n-1}`和`f{n-2}` 各被调用且只被调用 1 次**，并且调用前需要 **和一个常数因子相乘**。**（新增）允许调用自定义普通函数**。函数表达式是一个关于形参的表达式。二者的一般形式见**形式化定义**。
    - 自定义递推函数的**调用**形如`f{常数因子}(因子, 因子)`，比如`f{3}(x^2)`，`f{5}(-1, sin(x^2)`)。
    - 大括号中的`常数因子`为函数调用时的**序号**，你需要根据自定义递推函数的定义找到序号对应的函数，才能计算。保证0≤0≤序号≤5≤5。
    - 小括号中的`因子`为函数调用时的**实参**，包含任意一种因子。

  - **自定义普通函数（新增）**

    - 自定义普通函数的**定义**形如 `g(x, y) = 函数表达式` ，比如 `g(y) = y^2`，`g(x, y) = sin(x)*cos(y^2)`，`h(x, y) = x + y` 。
    - `g`、`h` 是函数的**函数名**。在本次作业中，保证函数名**只使用 `g`，`h`**，且**不出现同名函数的重复定义**（因此每次最多只有 2 个自定义普通函数）。更具体的约束信息请看第六部分中的数据限制部分。
    - `x`、`y` 为函数的**形参**。在本次作业中，**形参个数为 1~2 个**。形参**只使用 `x`，`y`**，且同一函数定义中不会出现重复使用的形参。
    - 函数表达式为一个关于形参的表达式。为了限制难度，我们规定**求导因子不会出现在函数表达式中**。函数表达式的一般形式参见**形式化定义**。
    - 函数表达式中允许调用自定义普通函数，但保证不出现递归调用的情况，如

    ```
      g(x) = h(x) + 6
      h(x) = g(x) * sin(x)
    ```

    不合法。

    **不允许先调用函数，再进行声明**，也不允许调用未声明的函数，如

    ```
      g(x) = h(x, x) + 666
      h(x, y) = x*sin(y)
    ```

    不合法。

    - 自定义普通函数的**调用**形如 `g(因子, 因子)` ，比如 `g(x^2)`，`g(sin(x^2), cos(x))`，`h(1, 0)` 。
    - `因子` 为函数调用时的**实参**，包含任意一种因子。

  - **常数因子** 包含一个带符号整数，如：`233`。

  - **表达式因子** 用一对小括号包裹起来的表达式，可以带指数，且指数为一个**非负**带符号整数，例如 `(x^2 + 2*x + x)^2` 。表达式的定义将在表达式的相关设定中进行详细介绍。

- **项** 由乘法运算符连接若干因子组成，如 `x * 02`。此外，**在第一个因子之前，可以带一个正号或负号**，如 `+ x * 02`、`- +3 * x`。注意，**空串不属于合法的项**。

- **表达式** 由加法和减法运算符连接若干项组成，如： `-1 + x ^ 233 - x ^ 06 +x` 。此外，**在第一项之前，可以带一个正号或者负号，表示第一个项的正负**，如：`- -1 + x ^ 233`、`+ -2 + x ^ 19260817`。注意，**空串不属于合法的表达式**。

- **求导因子（新增）** 由`dx(表达式)`这种算符构成，含义为对表达式中的x变量求导，其中可以出现多次求导的情况，详见求导因子的形式化描述。为了限制难度，我们规定**求导算子不会出现在自定义函数的函数表达式和递推表达式中**。例如

  - `g(x)=x+dx(x)`属于不合文法的自定义函数

- **空白字符** 在本次作业中，空白字符仅包含空格 `<space>`（ascii 值 32）和水平制表符 `\t`（ascii 值 9）。其他的空白字符，均属于非法字符。

  对于空白字符，有以下几点规定：

  - 带符号整数内不允许包含空白字符，注意带符号整数本身的符号与整数之间也不允许包含空白字符。
  - 函数保留字内不允许包含空白字符，即 `sin`，`cos`，`f{序号}`，`dx`关键字内不可以含有空白字符。

#### 二、设定的形式化表述

- 表达式 →→ 空白项 [加减 空白项] 项 空白项 | 表达式 加减 空白项 项 空白项
- 项 →→ [加减 空白项] 因子 | 项 空白项 '*' 空白项 因子
- 因子 →→ 变量因子 | 常数因子 | 表达式因子 | 求导因子
- 变量因子 →→ 幂函数 | 三角函数 | 函数调用
- 函数调用 →→ 自定义递推函数**调用** | 自定义普通函数**调用**
- 常数因子 →→ 带符号的整数
- 表达式因子 →→ '(' 表达式 ')' [空白项 指数]
- 幂函数 →→ 自变量 [空白项 指数]
- 自变量 →→ 'x'
- 三角函数 →→ 'sin' 空白项 '(' 空白项 因子 空白项 ')' [空白项 指数] | 'cos' 空白项 '(' 空白项 因子 空白项 ')' [空白项 指数]
- 指数 →→ '^' 空白项 ['+'] 允许前导零的整数 **(注：指数一定不是负数)**
- 带符号的整数 →→ [加减] 允许前导零的整数
- 允许前导零的整数 →→ ('0'|'1'|'2'|…|'9'){'0'|'1'|'2'|…|'9'}
- 空白项 →→ {空白字符}
- 空白字符 →→ （空格） | `\t`
- 加减 →→ '+' | '-'
- 换行 →→ `\n`

**自定义递推函数相关(相关限制见“公测数据限制”)**

- 自定义递推函数定义 →→ 定义列表
- 定义列表 →→ 初始定义 换行 初始定义 换行 递推定义 | 初始定义 换行 递推定义 换行 初始定义 | 递推定义 换行 初始定义 换行 初始定义
- 初始定义 →→ 'f' '{' 初始序号 '}' 空白项 '(' 空白项 形参自变量 空白项 [',' 空白项 形参自变量 空白项] ')' 空白项 '=' 空白项 函数表达式
- 初始序号 →→ '0' | '1'
- 递推定义 →→ 'f{n}' 空白项 '(' 空白项 形参自变量 空白项 [',' 空白项 形参自变量 空白项] ')' 空白项 '=' 空白项 递推表达式
- 序号 →→ '0'|'1'|'2'|'3'|'4'|'5'
- 形参自变量 →→ 'x' | 'y'
- 自定义递推函数**调用** →→ 'f{' 序号 '}' 空白项 '(' 空白项 因子 空白项 [',' 空白项 因子 空白项] ')'
- 自定义递推函数调用n-1 →→ 'f{n-1}' 空白项 '(' 空白项 因子 空白项 [',' 空白项 因子 空白项] ')'**（注：本次作业中此处的因子不允许出现任何自定义递推函数调用，但允许出现自定义普通函数调用）**
- 自定义递推函数调用n-2 →→ 'f{n-2}' 空白项 '(' 空白项 因子 空白项 [',' 空白项 因子 空白项] ')'**（注：本次作业中此处的因子不允许出现任何自定义递推函数调用，但允许出现自定义普通函数调用）**
- 递推表达式 →→ 常数因子 空白项 * 空白项 自定义递推函数调用n-1 空白项 加减 空白项 常数因子 空白项 * 空白项 自定义递推函数调用n-2 [空白项 '+' 空白项 函数表达式]
- 函数表达式 →→ 表达式（将自变量扩展为形参自变量，且一定不含求导因子） **(注：本次作业中函数表达式不允许出现任何自定义递推函数调用，但允许出现自定义普通函数调用，保证不会出现递归调用的情况)**

**自定义普通函数相关(相关限制见“公测数据限制”)**

- 自定义普通函数**定义** →→ 自定义普通函数名 空白项 '(' 空白项 形参自变量 空白项 [',' 空白项 形参自变量 空白项] ')' 空白项 '=' 空白项 函数表达式
- 形参自变量 →→ 'x' | 'y'
- 自定义普通函数**调用** →→ 自定义普通函数名 空白项 '(' 空白项 因子 空白项 [',' 空白项 因子 空白项] ')'
- 自定义普通函数名 →→ 'g' | 'h'
- 函数表达式 →→ 表达式（将自变量扩展为形参自变量，且一定不含求导因子） **(注：本次作业中函数表达式不允许出现任何自定义递推函数调用，但允许出现自定义普通函数调用，保证不会出现递归调用的情况)**

**求导算子相关(相关限制见“公测数据限制”)**

- 求导因子 →→ 求导算子 空白项 '(' 空白项 表达式 空白项 ')'
- 求导算子 →→ 'dx'

形式化表述中`{}[]()|`符号的含义已在第一次作业指导书中说明，不再赘述。

式子的具体含义参照其数学含义。

若输入字符串能够由“表达式”推导得出，则输入字符串合法。

除了满足上述形式化表述之外，我们本次作业的输入数据的**额外限制**请参见**第六部分：输入/输出说明 的数据限制部分**。

#### 三、求导公式

**本次作业可能用到的求导公式有：**Ⅰ.当f(x)=c（c为常数）时，f′(x)=0

Ⅱ.当f(x)=xn（n≠0）时，f′(x)=nxn−1

Ⅲ.当f(x)=cos(x)时，f′(x)=−sin(x)

Ⅳ.当f(x)=sin(x)时，f′(x)=cos(x)

Ⅴ.链式法则：[f(g(x))]′=f′(g(x))g′(x)

Ⅵ.乘法法则：[f(x)g(x)]′=f′(x)g(x)+f(x)g′(x)

------

### 第六部分：输入/输出说明

#### 一、公测说明

##### 输入格式

本次作业的输入数据包含若干行：

- 第一行为一个整数 n (0≤n≤2) ，表示**自定义普通函数定义的个数**。
- 第 2 到第 n+1行，每行为一行字符串，表示一个自定义普通函数的定义。
- 第 n+2 行为一个整数 m(0≤m≤1)，表示 **自定义递推函数定义的个数**。
- 第 n+3到第 n+2+3m 行，每行一个字符串，每三行表示一组自定义递推函数的定义。
- 第 n+3+3m 行，一行字符串，表示待展开表达式。

##### 输出格式

输出展开括号之后，不再含有自定义函数，不再含有求导算子，且只包含**必要的括号**的表达式。（必要括号的定义见**公测说明-正确性判定**）。

##### 数据限制

- 输入表达式**一定满足**基本概念部分给出的**形式化描述**。
- **自定义函数定义**满足以下限制：
  - 不会出现重名函数，自定义递推函数的函数名一定为 `f`。
  - 函数表达式**与上次作业不同**，允许调用其他**已定义的**自定义**普通**函数，且不允许出现求导因子，下面是几个不合法的例子。
  - 函数定义时`g(x,y) = g(x,2)+y+1`，出现递归调用，不合法。
  - 函数定义时`h(x,y) = g(x,y)+y,g(x,y)=x+y`，h先定义，g后定义，h在定义时调用了未定义的函数g，不合法。
  - 函数定义时`g(x)=x+dx(x)`，包含了求导因子，不合法
  - 函数形参不能重复出现，即无需考虑 `f(x,x)=x^2+x` 这类情况
  - 函数定义式中出现的变量都必须在形参中有定义
- 对于规则 “指数 →→ ^ 空白项 ['+'] 允许前导零的整数”，我们本次要求**输入数据的指数不能超过 8**。
- 在表达式化简过程中，如果遇到了 `0^0`这种情况，默认`0^0` = 1。
- 为了避免待展开表达式或函数表达式过长。最后一行输入的待展开表达式的**有效长度**至多为 200 个字符，每个自定义普通函数**定义**的**有效长度**至多为 50 个字符，自定义递推函数定义时，每个定义的**有效长度**至多为 75 个字符。其中**有效长度**指的是去除掉所有**空白符**后剩余的字符总数。
- 根据文法可以注意到，整数的范围并不一定在`int`或`long`范围内。

#### 三、样例

| **#** | **输入**                                                     | **输出**          | **说明**                                                     |
| ----- | ------------------------------------------------------------ | ----------------- | ------------------------------------------------------------ |
| 1     | 0 <br />0 <br />dx((x+1)*(x+2))                              | 2*x+3             | 对(x+1)*(x+2)求导后，恒等变形展开为2*x+3                     |
| 2     | 1 <br />g(x)=x^2+1 <br />0 <br />g(x)+dx(g(x))               | x^2+2*x+1         | 先将g(x)替换为x^2+1，再对其求导得到2*x，最后合并             |
| 3     | 2 <br />g(x,y)=x*sin(y) <br />h(x)=cos(x)^2 <br />0 <br />g(x,h(x))+1 | x*sin(cos(x)^2)+1 | 自定义普通函数的嵌套调用：g(x,h(x)) = x*sin(cos(x)^2)        |
| 4     | 0 <br />1 <br />f{0}(x,y)=x+y <br />f{1}(x,y)=x-y <br />f{n}(x,y)=1\*f{n-1}(x\^2,y)-2*f{n-2}(x,y^2)+1 <br />f{2}(x,1) | x^2-2*x-2         | 自定义递推函数展开：f{2}(x,1) 由f{1}与f{0}迭代计算并合并简化 |
| 5     | 1 <br />g(x,y)=x\*y <br />1 <br />f{0}(x)=x^2 <br />f{1}(x)=1 <br />f{n}(x)=2\*f{n-1}(x\^2)+3*f{n-2}(x^3)+-1 <br />g(f{2}(x),dx(x^2)) | 6*x^7+2*x         | 既有自定义普通函数又有自定义递推函数，并含求导算子，最终全部展开 |
| 6     | 1 <br />g(x)=x+y <br />0 <br />x+1                           | Wrong Format      | 函数定义中出现了未在形参中声明的变量y，违反文法规则          |

------

### 第七部分：设计建议

- 在 Java程序中，不建议使用静态数组。推荐使用 `ArrayList` 、 `HashMap` 、 `HashSet`等容器来高效率管理数据对象。
- 在处理输入解析时，可以考虑采用**递归下降解析法**或是正则表达式作为工具。递归下降方法已在先导课程的作业中得到了详尽的介绍与充分的实践，**建议同学们继续沿用**这一方法。而对正则表达式相关的 API 可以了解 `Pattern` 和 `Matcher` 类。
- 这次作业新增的求导算子，可以这样考虑：
  - 对于层次化结构类编写求导方法，对应不同求导法则。
  - 注意求导因子可能出现的地方，特别地：**自定义函数定义时的函数表达式和递推表达式中不会出现求导因子**。

------

### 第八部分：提示与警示

#### 一、提示

- Java 内的原生整数类型有 `long` 和 `int`，长度分别为 64 位和 32 位，遇到整数过大的问题，可以使用BigInteger存储。
- 要善于利用java语言内置的工具（工具函数或者工具类等），不要重复造轮子！
- 我们鼓励大家通过 Baidu、Google、Stack Overflow 等方式自行学习和解决问题。比如: 程序运行异常的原因很容易通过搜索引擎了解明白。
- 请注意一个往年遇到比较多的问题，使用`Integer.parseInt(String input)`函数抛出了`java.lang.NumberFormatException`异常，一般是因为传入的`input`字符串其中含有非数字字符，以至于函数无法将之转换为数字。
- 如果还有更多的问题，请到讨论区提问。但是**请善用讨论区**，并在此之前认真阅读包括但不限于课程要求文档、指导书、搜索引擎结果等的内容。[关于如何提问](https://www.cnblogs.com/rocedu/p/5167941.html)。

#### 二、警示

- 如果在互测中发现其他人的代码疑似存在**抄袭**等行为，可向课程组举报，课程组感谢同学们为 OO 课程建设所作出的贡献。
