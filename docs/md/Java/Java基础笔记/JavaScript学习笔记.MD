#                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                JavaScript 学习笔记

# var 和 const 和 let 的区别

let一般应用于基本数据类型；const 一般应用于引用数据类型，也就是函数对象等。

使用优先级 let > const > var

var 是全局作用域

let 和 const 都是块级作用域。

let 可以先声明，不用赋值，

const  声明的时候必须赋值。



# 数组



数组中slice()方法的使用 数组切割

```javascript
let fruits = ["Banana", "Orange", "Lemon", "Apple", "Mango"];
let citrus = fruits.slice(1,3);//从下标1开始，到3结束，不包括3。也就是输出第一个和第二个元素。
console.log(citrus);
citrus = fruits.slice(3);//从下标3开始，包括下标3，输出之后的。
console.log(citrus);
```

# 函数

1. 推荐使用表示式命名函数

```javascript
const add = (parm1,parm2)=>{
    return parm1 + parm2;
};
```

2. 函数自调用

```javascript
(function () {
  console.log('hello');
})();

((a,b)=>{
    console.log(a+b);
})(10,11);

//自调用函数返回结果赋值给a;
const n = ((a,b)=>{
    return a + b;
})(10,11);

console.log(n);

```

# 对象

推荐通过对象字面量创建对象

```javascript
const student = {
  name: 'zhangsan',
  age: 18,
  gender: 'male',
  sayHi() {
    console.log('hi,my name is ' + this.name);
  },
};

console.log(student);
```

自定义构造函数

```javascript
function Student(name, age, gender) {
  this.name = name;
  this.age = age;
  this.gender = gender;
  this.sayHi = function () {
    console.log('hi,my name is ' + this.name);
  };
}
var s1 = new Student('zhangsan', 18, 'male');
```

前面例子原型的写法：（解决类创建一个对象，对象会重新创建一个sayhi 方法的问题。）

```js
function Student(name, age, gender) {
  this.name = name;
  this.age = age;
  this.gender = gender;
}

Student.prototype.sayHi = function () {
  console.log('hi');
};

var s1 = new Student('zhangsan', 18, 'male');
s1.sayHi(); // 打印 hi
var s2 = new Student('lisi', 18, 'male');
s2.sayHi(); // 打印 hi
console.log(s1.sayHi == s2.sayHi); // 结果为 true
```

删除对象属性

```javascript
delete student.name;
```

# BOM 

浏览器对象模型（Browser Object Model )指的是由Web 浏览器暴露的所有的对象组成的表示模型。

# DOM 

文档对象模型（Document Objce Model).

处理可扩展置标记语言的标准编程接口。它是一种与平台和语言无关的应用程序接口。动态的访问程序和脚本更新其内容。

DOM分为HTML DOM和XML DOM两种。它们分别定义了访问和操作HTML/XML文档的标准方法

HTML 超文本标记语言。

XML 可扩展标记语言。

# BOM

window 是浏览器的顶级对象。当调用window 下的属性和方法时。可以省略window。

对话框：

* alter()
* prompt() 显示可供用户输出的对话框。
* confirm: 显示带有一段消息以及确认按钮和取消按钮的对话框。

页面加载事件。

* onload 

  ```javascript
  window.onload = function(){
      //当页面加载完成时执行。
  }
  ```

* onunload 

  ```javascript
  window.onunlaod = function (){
      //当用户退出页面时执行
  }
  ```

* 浏览器尺寸

  ```javascript
  var width = window.innerWidth;
  document.documentElement.clientWidth;
  document.body.clientWidth;
  
  var height = window.innerHeight;
  document.documentElement.clientHeight;
  document.body.clientHeight;
  ```

* 定时器

  setTimeout(): 在指定毫秒数到达之后执行的函数，只能执行一次。

  clearTimeout(); 取消上面方法设置的timeout.

  ```javascript
  var timeId = setTimeout(()=>{
      console.log('Hello shiyanlou');
  },2000);
  //取消定时器
  clearTimeout(timeId);
  ```

  setInterval() : 周期调用函数。

  ```javascript
  let timerId = setInterval(() => {
      var date = new Date();
      console.log(date.toLocaleDateString());
  }, 2000);
  //取消周期 定时器
  clearTimeout(timerId);
  ```

  # DOM

  文档对象模型（Document Object Model，简称 DOM），是 W3C 组织推荐的处理可扩展标志语言的标准编程接口。DOM 定义了访问 HTML 和 XML 文档的标准。我们这里主要学习 HTML DOM。DOM 可以把 HTML 看做是文档树，通过 DOM 提供的 API 可以对树上的节点进行操作

**改变 HTML 的输出流**

```javascript
document.write('新设置的内容<p>标签也可以生成</P>');
```

**改变样式颜色**

```javaScript
<p id="syl" style="color: red;">实验楼</p>
<script>
    document.getElementById('syl').style.color = 'green';
</script>
```

HTML 文档中，所有元素都是节点

获取节点

1. 通过id

   ```javascript
   <img id="image" src="https://www.baidu.com/img/bd_logo1.png" />
       <script>
       document.getElementById('image').src =
       'https://static.shiyanlou.com/img/shiyanlou_logo.svg';
   </script>
   ```

2. 通过标签名

   ```javascript
   <input type="text" />
       <input type="text" />
           <script>
           document.getElementsByTagName('input')[0].value = 'hello'; // 下标为 [0] 表示选取第 1 个 input 标签
   document.getElementsByTagName('input')[1].value = 'shiyanlou'; // 下标为 [1] 表示选取第 2 个 input 标签
   </script>
   ```

3. 通过类名

   ```javascript
   document.getElementsByClassName('name'); // 返回包含 class = "name" 的所有元素的一个列表。
   ```

   # Dom 节点操作

   #### DOM 节点的操作

   1. 创建节点

      - 创建元素节点：使用 `createElement()` 方法。比如：

      ```javascript
      var par = document.createElement('p');
      ```

      - 创建属性节点：使用 `createAttribute()` 方法。
      - 创建文本节点：使用 `createTextNode()` 方法。

   2. 插入子节点

      - `appendChild ()` 方法向节点添加最后一个子节点。
      - `insertBefore` (插入的新的子节点，指定的子节点) 方法在指定的子节点前面插入新的子节点。如果第二个参数没写或者为 null，则默认插入到后面。

   3. 删除节点：使用 `removeChild()` 方法。写法为：

   ```javascript
   父节点.removeChild(子节点);
   node.parentNode.removeChild(node); // 如果不知道父节点是什么，可以这样写
   ```

   1. 替换子节点：使用 `replaceChild()` 方法。语法为：

   ```javascript
   node.replaceChild(newnode, oldnode);
   ```

   1. 设置节点的属性：
      - 获取：`getAttribute(name)`
      - 设置：`setAttribute(name, value)`
      - 删除：`removeAttribute(name)`

   # DOM 事件

   #### 事件的定义

   在什么时候执行什么事。

   #### 事件三要素

   事件由：事件源 + 事件类型 + 事件处理程序组成。

   - 事件源：触发事件的元素。
   - 事件类型：事件的触发方式（比如鼠标点击或键盘点击）。
   - 事件处理程序：事件触发后要执行的代码（函数形式，匿名函数）。

   #### 常用的事件

   | 事件名      | 说明                                 |
   | ----------- | ------------------------------------ |
   | onclick     | 鼠标单击                             |
   | ondblclick  | 鼠标双击                             |
   | onkeyup     | 按下并释放键盘上的一个键时触发       |
   | onchange    | 文本内容或下拉菜单中的选项发生改变   |
   | onfocus     | 获得焦点，表示文本框等获得鼠标光标。 |
   | onblur      | 失去焦点，表示文本框等失去鼠标光标。 |
   | onmouseover | 鼠标悬停，即鼠标停留在图片等的上方   |
   | onmouseout  | 鼠标移出，即离开图片等所在的区域     |
   | onload      | 网页文档加载事件                     |
   | onunload    | 关闭网页时                           |
   | onsubmit    | 表单提交事件                         |
   | onreset     | 重置表单时                           |

   例子 1 ：鼠标单击事件：

   ```html
   <p onclick="this.innerHTML = '我爱学习，身体好好!'">请点击该文本</p>
   ```

   例子 2 ：鼠标双击事件：

   ```html
   <!DOCTYPE html>
   <html>
     <head>
       <meta charset="UTF-8" />
       <title></title>
     </head>
     <body>
       <h1 ondblclick="changetext(this)">请点击该文本</h1>
       <script>
         function changetext(id) {
           id.innerHTML = '我爱学习，身体棒棒!';
         }
       </script>
     </body>
   </html>
   ```

   例子 3 ：鼠标移除悬停：

   ```html
   <!DOCTYPE html>
   <html>
     <head>
       <meta charset="UTF-8" />
       <title></title>
     </head>
     <body>
       <div
         onmouseover="mOver(this)"
         onmouseout="mOut(this)"
         style="background-color:deepskyblue;width:200px;height:100px;"
       >
         把鼠标移到上面
       </div>
       <script>
         function mOver(obj) {
           obj.innerHTML = '你把鼠标移到了上面 ';
         }
   
         function mOut(obj) {
           obj.innerHTML = '你把鼠标移开了';
         }
       </script>
     </body>
   </html>
   ```

   注：大家可以把上述例子运行一下感受一下事件的魅力，并且可以自己尝试着写一些其他的简单事件。

   通过鼠标点击更改导航栏的样式，来看看最终的效果：

   ![1](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1547453308407.png)

   参考源码：

   ```html
   <!DOCTYPE html>
   <html lang="en">
     <head>
       <meta charset="UTF-8" />
       <title>Title</title>
       <style>
         #list li {
           list-style-type: none;
           width: 100px;
           height: 50px;
           line-height: 50px;
           background-color: beige;
           text-align: center;
           float: left;
         }
   
         #list li.current {
           background-color: red;
         }
   
         #list li a {
           text-decoration: none;
         }
       </style>
     </head>
   
     <body>
       <div id="menu">
         <ul id="list">
           <li class="current">
             <a href="javascript:void(0)">首页</a>
           </li>
           <li>
             <a href="javascript:void(0)">HTML</a>
           </li>
           <li>
             <a href="javascript:void(0)">CSS</a>
           </li>
           <li>
             <a href="javascript:void(0)">JavaScript</a>
           </li>
           <li>
             <a href="javascript:void(0)">关于</a>
           </li>
           <li>
             <a href="javascript:void(0)">帮助</a>
           </li>
         </ul>
       </div>
   
       <script>
         // 获取所有的 li 标签
         var liObjs = document.getElementById('list').getElementsByTagName('li');
         // 循环遍历，找到每个 li 中的 a，注册点击事件
         for (var i = 0; i < liObjs.length; i++) {
           // 每个 li 中的 a
           var aObj = liObjs[i].firstElementChild;
   
           aObj.onclick = function () {
             // 把这个 a 所在的 li 的所有的兄弟元素的类样式全部移除
             for (var j = 0; j < liObjs.length; j++) {
               liObjs[j].removeAttribute('class');
             }
             //当前点击的 a 的父级元素 li(点击的这个 a 所在的父级元素 li)，设置背景颜色
             this.parentNode.className = 'current';
           };
         }
       </script>
     </body>
   </html>
   ```

面对对象编程



构造函数、实例、原型三者之间的关系



我们之前提到过：每一个函数都有一个 `prototype` 属性，指向另一个对象。让我们用代码验证一下，在编辑器中输入以下代码：

```html
<script type="text/javascript">
  function F() {}
  console.log(F.prototype);
</script>
```

上述代码在浏览器中打印结果为 Object，验证了我们所说的 `prototype` 属性，指向另一个对象。

构造函数的 `prototype` 对象默认都有一个 `constructor` 属性，指向 `prototype` 对象所在函数。在控制台中运行下面的代码：

```js
function F() {}
var a = new F();
console.log(a.__proto__ === F.prototype); // 结果为 true
```

实例对象可以直接访问原型对象成员，所有实例都直接或间接继承了原型对象的成员。

总结：每个构造函数都有一个原型对象，原型对象包含一个指向构造函数的指针 `constructor`，而实例都包含一个指向原型对象的内部指针`__proto__`。

原型链

我们说过所有的对象都有原型，而原型也是对象，也就是说原型也有原型，那么如此下去，也就组成了我们的原型链。

#### 属性搜索原则

属性搜索原则，也就是属性的查找顺序，在访问对象的成员的时候，会遵循以下原则：

- 首先从对象实例本身开始找，如果找到了这个属性或者方法，则返回。
- 如果对象实例本身没有找到，就从它的原型中去找，如果找到了，则返回。
- 如果对象实例的原型中也没找到，则从它的原型的原型中去找，如果找到了，则返回。
- 一直按着原型链查找下去，找到就返回，如果在原型链的末端还没有找到的话，那么如果查找的是属性则返回 undefined，如果查找的是方法则返回 `xxx is not a function`。

#### 更简单的原型语法

在前面的例子中，我们是使用 `xxx.prototype.` 然后加上属性名或者方法名来写原型，但是每添加一个属性或者方法就写一次显得有点麻烦，因此我们可以用一个包含所有属性和方法的对象字面量来重写整个原型对象：

```js
function Student(name, age, gender) {
  this.name = name;
  this.age = age;
  this.gender = gender;
}
Student.prototype = {
  hobby: 'study',
  sayHi: function () {
    console.log('hi');
  },
};

var s1 = new Student('wangwu', 18, 'male');
console.log(Student.prototype.constructor === Student); // 结果为 false
```

但是这样写也有一个问题，那就是原型对象丢失了 `constructor` 成员。所以为了保持 constructor 成员的指向正确，建议的写法是：

```js
function Student(name, age, gender) {
  this.name = name;
  this.age = age;
  this.gender = gender;
}
Student.prototype = {
  constructor: Student, // 手动将 constructor 指向正确的构造函数
  hobby: 'study',
  sayHi: function () {
    console.log('hi');
  },
};

var s1 = new Student('wangwu', 18, 'male');
console.log(Student.prototype.constructor === Student); // 结果为 true
```

srguments 对象

在函数代码中，使用特殊对象 `arguments`，无需明确指出参数名，我们就能访问它们。第一个参数是 `arguments[0]`，第二个参数是 `arguments[1]`，以此类推。比如：

```js
function foo() {
  console.log(arguments[0]);
  console.log(arguments[1]);
}
foo(2, 3); // 打印 2 3
```

还可以用 `arguments` 对象检测函数的参数个数，引用属性 `arguments.length` 即可。来看一个遍历参数求和的例子：

```js
function add() {
  var sum = 0;
  for (var i = 0; i < arguments.length; i++) {
    sum += arguments[i];
  }
  return sum;
}
add(); // 0
add(1); // 1
add(1, 2); // 3
add(1, 2, 3); // 6
```

用 `Function()` 对象创建函数的语法如下：

```js
var function_name = new Function(arg1, arg2, ..., argN, function_body)
```

注：每个参数都必须是字符串，`function_body` 是函数主体，也就是要执行的代码。

例子：

```js
var add = new Function('a', 'b', 'console.log(a+b);');
add(2, 5); // 打印 7
```

再看一个例子:

```js
var add = new Function('a', 'b', 'console.log(a+b);');
var doAdd = add;
doAdd(2, 5); // 打印 7
add(2, 5); // 打印 7
```

在上述例子中，变量 `add` 被定义为函数，然后 `doAdd` 被声明为指向同一个函数的指针。用这两个变量都可以执行该函数的代码，并输出相同的结果。因此，函数名只是指向函数的变量，那么我们可以把函数作为参数传递给另一个函数，比如下面的例子：

```js
function addF(foo, b, c) {
  foo(b, c);
}
var add = new Function('a', 'b', 'console.log(a+b);');
addF(add, 2, 5); // 打印 7
```

#### Function 对象的 length 属性

函数属于引用类型，所以它们也有属性和方法。`length` 属性声明了函数期望的参数个数。

例子：

```js
var add = new Function('a', 'b', 'console.log(a+b);');
console.log(add.length); // 打印 2
```

#### Function 对象的方法

`Function()` 对象也有与所有对象共享的 `valueOf()` 方法和 `toString()` 方法。这两个方法返回的都是函数的源代码。

例子：

```js
var add = new Function('a', 'b', 'console.log(a+b);');
add.valueOf();
add.toString();
```

![1](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1547866078578.png)

