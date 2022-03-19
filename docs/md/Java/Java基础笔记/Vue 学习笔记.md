# Vue 2.0 学习笔记

学习 vue 需要的知识

* html
* css
* javascript
* node

# 0.Vue 2.0 项目的创建

1.安装2.9.6 Vue 

```
npm install -g vue-cli
```

2. 创建 vue 项目

```html
vue init webpack <项目名> # sylApp 这里是项目名全部小写。
cd <项目名>
npm run start             # 启动项目
```



## 1. 什么是Vue .js



​	是一套用于构建用户界面的渐进式框架。

​	下载地址： https://cn.vuejs.org/js/vue.js

​						https://cn.vuejs.org/js/vue.min.js

​	比较稳定的cdn 

```html
//开发环境的cdn
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
//生产环境的cdn
<script src="https://cdn.jsdelivr.net/npm/vue"></script>

```

# 1. 基本语法



## 第一个vue 实例

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <title>Document</title>
</head>

<body>
    <div id="app">{{msg}}</div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: 'hello Vue 3',
            },
        });
    </script>
</body>

</html>
```

双括号实现数值绑定。

## 实现数值绑定，双大括号表达式

```html
<p>{{msg}}</p>
```

## 数据双向绑定

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>

<body>
    <div id="app">
        <input type="text" v-model="msg"></input>
        <p>{{msg}}</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: 'hello',
            },
        });
    </script>
</body>

</html>
```

* `<p>{{msg}}</p>` 随着msg 的值而变化。

* `<p v-once>msg：{{msg}}</p>` 插入值只能改变一次。

## 渲染 html  v-html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>
<body>
    <div id ="app" v-html="msg">
       <p>{{msg}}</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: '<p>hello shiyanlou！</p>',
            },
        });
    </script>
</body>
</html>
```

显示结果

![image-20210319202850515](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210319202850515.png)

修改代码

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>
<body>
    <div id ="app" v-html="msg">
      
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: '<p>hello shiyanlou！</p>',
            },
        });
    </script>
</body>
</html>
```

## 特性绑定 v-bind 

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>
<body>
    <div id ="app" >
        <input type="checkbox" v-bind:checked ="isChecked"/>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: '<p>hello shiyanlou！</p>',
                isChecked: true
            }
        });
    </script>
</body>
</html>
```

## JavaScript 表达式

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>
<body>
    <div id ="app" >
        <!--运算符-->
        <p>num + 2 = {{num+2}}</p>
        <!--三运算符-->
        <p>Are you Ok? {{Ok? 'I am Ok!':'No'}}</p>
        <!--对象方法直接调用-->
        <p>名字倒过来写：{{name.split('').reverse().join('')}}</p>
        <!--属性值运算符 执行后class = "clo12"-->
        <p v-bind:class="'col'+colNum">Hello  </p>
        <p></p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                num: 20,
                Ok: true,
                name: '大宝',
                colNum: 12

            }
        });
    </script>
</body>
</html>
```

指令（directives)带有v-前缀的特殊属性。

v-bind 

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>

<body>
    <!-- 指令参数 -->
    <div id="app">
        <a v-bind:href="url">百度一下</a>

    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                url: 'http://www.baidu.com'
                
            }
        });
    </script>
</body>

</html>
```

####  v-on  监听DOM 事件

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>

<body>
    <!-- 指令参数 -->
    <div id="app">
        <p>我叫：{{name}}
            <button v-on:click="handleClick">点我</button>

    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                name: '嫦娥',


            },
            methods: {
                handleClick: function() {
                    this.name = this.name.split('').reverse().join('');
                },
            }
        });
    </script>
</body>

</html>
```

#### 动态事件绑定

```html
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title>
        <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    </head>

    <body>
        <!-- 指令参数 -->
        <div id="app">
            <p>我叫：{{name}}
                <button v-on:[event]="handleClick" >点我</button>
        </div>
        <script>
            var app = new Vue({
                el: '#app',
                data: {
                    name: '嫦娥',
                    event: 'click',
                },
                methods:{
                    handleClick: function(){
                        this.name = this.name.split('').reverse().join('');
                    },
                }
            });
        </script>
    </body>
</html>
```

## 修饰符

* 事件修饰符
* 按键修饰符
* 系统修饰符

#### .prevent 提交按钮阻止页面跳转

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue</title>
    <!-- 通过cdn方式引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <!-- 指令 修饰符-->
    <div id="app">
      <form action="/" v-on:submit.prevent="submit">
        <button type="submit">提交</button>
      </form>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {},
        methods: {
          submit: function () {
            console.log('成功提交！');
          },
        },
      });
    </script>
  </body>
</html>
```

## 指令缩写

上面例子中我们使用了 `v-bind` 绑定属性

```html
<a v-bind:href="url">实验楼</a>
```

我们可以简写为：

```html
<a :href="url">实验楼</a>
```

同样的使用 `v-bind` 绑定的其他属性也可以简写：

```html
v-bind:class="className" 简写为 :class="className" v-bind:value="myValue" 简写为
:value
```



上面 `v-bind` 指令提供简写，同样 `v-on` 指令也提供简写，但是与 `v-bind` 有一些差异，`v-on:` 使用 @ 简写。

```html
<!-- 完整语法 -->
<button v-on:click="handleClick">点我</button>
<!-- 缩写 -->
<button @click="handleClick">点我</button>
```

​	

# 2.计算属性和侦听属性与过滤器

计算机属性的基本使用

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <p>我名字正着写：{{name}}</p>
      <!-- reverseName 计算属性  可以像绑定普通属性一样在模板中绑定计算属性-->
      <p>计算出我名字倒着写：{{reverseName}}</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          name: '实验楼',
        },
        computed: {
          //reverseName 是一个计算属性
          reverseName: function () {
            return this.name.split('').reverse().join('');
          },
        },
      });
    </script>
  </body>
</html>
```

计算机加载是惰性的，如下的代码只有刷新才修改数值

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">{{now}}</div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {},
            computed: {
                now: function() {
                    return Date.now();
                },
            },
        });
    </script>
</body>

</html>
```

计算属性的getter 和 setter

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <p>firstName:{{firstName}}</p>
        <p>lastName:{{lastName}}</p>
        <p>全名是:{{fullName}}</p>
        <button v-on:click="changeName">改姓</button>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                firstName: '王',
                lastName: '花花',
            },
            methods: {
                //changeName 定义一个方法改变 计算属性 fullName 的值
                changeName: function() {
                    //修改计算属性 fullName 等于李花花
                    this.fullName = '李花花';
                    //上面一句等于触发了 fullName 属性的 setter
                },
            },
            computed: {
                fullName: {
                    //getter
                    get: function() {
                        return this.firstName + this.lastName;
                    },
                    //setter  直接改变计算属性 fullName的值就可以触发setter this.fullName='XX'
                    set: function(newName) {
                        var name = newName;
                        this.firstName = name.slice(0, 1); //取新值的第一个字符
                        this.lastName = name.slice(1); //从新值的第二个字符开始取值
                    },
                },
            },
        });
    </script>
</body>

</html>
```

## 过滤器的使用

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <!-- toUpperCase   getString  为自定义的过滤器-->
      <p>小写转换大写：过滤前：{{msg}} 过滤后： {{msg|toUpperCase}}</p>
      <p>去除数字：过滤前：{{msg2}} 过滤后： {{msg2|getString}}</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          msg: 'hello',
          msg2: '1s2y3l',
        },
        // filters 过滤器选项
        filters: {
          //toUpperCase 定义一个字符串转大写的过滤器
          toUpperCase: function (val) {
            return val.toUpperCase();
          },
          //getString 定义一个获取去除数字的过滤器
          getString: function (val) {
            let newVal = '';
            val.split('').map(function (item) {
              if (9 >= item && item >= 0) {
                return;
              } else {
                return (newVal += item);
              }
            });
            return newVal;
          },
        },
      });
    </script>
  </body>
</html>
```

过滤器应用场景

商品打折

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <!-- joint  为自定义的过滤器-->
      <p>不要￥899，只要{{price|joint}}</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          //后台价格数据
          price: 199,
        },
        // filters 过滤器选项
        filters: {
          //joint 定义￥拼接过滤器
          joint: function (price) {
            return '￥' + price;
          },
        },
      });
    </script>
  </body>
</html>
```

简易的购物车功能

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <p>商品单价：{{price|joint}}</p>
        <input type="number" v-model="goodsNums"></input>

        <p>商品总价：{{allPrice|joint}}</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                //商品数量
                goodsNums: 0,
                //商品价格
                price: 199,
            },
            computed: {
                allPrice: function() {
                    return this.price * this.goodsNums;
                },
            },
            //过滤器
            filters: {
                //字符拼接，添加金钱符
                joint: function(val) {
                    return '￥' + val;
                }
            }
        });
    </script>
</body>

</html>
```



# 3. 类与style 绑定

```html
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title>
        <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
    </head>

    <body>
        <style>
            .active {
                color: rgb(180, 156, 76);
                font-size: 22px;
            }
        </style>
        <div id="app">
            <span class="active">改变字体的颜色</span><br>
            <!-- p标签 使用 v-bind:class 标签无法生效 -->
            <span v-bind:class="{'active':isActive}">test</span>
            <!-- 缩写形式 -->
            <span :class="{'active':isActive}">syl</span>
        </div>
        <script>
            var app = new Vue({
                el: '#app',
                data: {
                    isActive: true,
                },
            });
        </script>
    </body>

</html>
```

### 多个属性动态切换class

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
    <style>
      .active {
        color: pink;
        font-size: 22px;
      }
      .red-bg {
        background: red;
      }
    </style>
  </head>
  <body>
    <div id="app">
      <!-- 数组语法绑定 class   当isActive为true时，active就成成span标签的class -->
      <span class="static" v-bind:class="{'active':isActive,'red-bg':isRed}"
        >syl</span
      >
      <!-- isActive 为true 渲染后 <span class="static active red-bg">syl</span> -->
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          isActive: true,
          isRed: true,
        },
      });
    </script>
  </body>
</html>
```

### 使用数组多属性动态切换class

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <style>
        .active {
            color: rgb(180, 156, 76);
            font-size: 22px;
        }
        
        .red-bg {
            background: red;
        }
    </style>
    <div id="app">
        <span class="active">改变字体的颜色</span><br>
        <!-- p标签 使用 v-bind:class 标签无法生效 -->
        <span v-bind:class="{'active':isActive,'red-bg':isRed}">test</span>
        <!-- 缩写形式 -->

        <!-- 使用数组 -->
        <span v-bind:class="[activeClass,bgColorClass]">syl</span>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                isActive: true,
                isRed: true,
                //style 样式绑定
                activeClass: 'active',
                bgColorClass: 'red-bg',

            },
        });
    </script>
</body>

</html>
```

对象绑定

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <style>
        .active {
            color: rgb(180, 156, 76);
            font-size: 22px;
        }
        
        .red-bg {
            background: red;
        }
    </style>
    <div id="app">
        <span class="active">改变字体的颜色</span><br>
        <!-- p标签 使用 v-bind:class 标签无法生效 -->
        <span v-bind:class="{'active':isActive,'red-bg':isRed}">test</span>
        <!-- 缩写形式 -->

        <!-- 使用数组 -->
        <span v-bind:class="[activeClass,bgColorClass]">syl</span>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                isActive: true,
                isRed: true,
                //style 样式绑定
                activeClass: 'active',
                bgColorClass: 'red-bg',

            },
        });
    </script>
</body>

</html>
```

使用对象绑定style

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <p v-bind:style="{fontSize:size,backgroundColor:bgColor}">你好，实验楼</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          size: '26px',
          bgColor: 'pink',
        },
      });
    </script>
  </body>
</html>
```

直接绑定一个样式对象通常更好，这会让模板更清晰

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <p v-bind:style="styleObject">你好，实验楼</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          styleObject: {
            fontSize: '26px',
            backgroundColor: 'pink',
          },
        },
      });
    </script>
  </body>
</html>
```

数组语法将多个样式应用到同一个元素上

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <p v-bind:style="[styleObject1,styleObject2]">你好，实验楼</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          //样式一
          styleObject1: {
            fontSize: '26px',
            backgroundColor: 'pink',
          },
          //样式二
          styleObject2: {
            marginTop: '200px',
            textAlign: 'center',
          },
        },
      });
    </script>
  </body>
</html>
```

动态改变样式

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
    <style>
      .active {
        color: red;
      }
    </style>
  </head>
  <body>
    <div id="app">
      <p
        v-bind:style="{fontSize:fontSize+'px'}"
        v-bind:class="{active:isActive}"
      >
        你好，实验楼
      </p>
      <button @click="handleClick">变大变色</button>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          fontSize: 20,
          isActive: true,
        },
        methods: {
          handleClick: function () {
            this.fontSize += 2;
            this.isActive = !this.isActive; //取反
          },
        },
      });
    </script>
  </body>
</html>
```

# 3. 条件与循环渲染

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=
    , initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <p v-if="display">当dispaly 等于true 时我才出现</p><br/>

        <p>v-if 测试</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                display: false,
            },
        });
    </script>
</body>

</html>
```

v-if 和 v-else 组合使用

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <p v-if="display">你好，实验楼</p>
      <p v-else>SYL</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          display: true,
        },
      });
    </script>
  </body>
</html>
```

v-show 始终保留在DOM 中。不同点：v-if显示隐藏是将dom元素整个添加或删除，而v-show隐藏则是为该元素添加css--display:none，dom元素还在。

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <p v-show="show">你好，实验楼</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          show: true,
        },
      });
    </script>
  </body>
</html>
```

将菜单数组循环成一个完整的菜单



```html
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=
                                       , initial-scale=1.0">
        <title>Document</title>
        <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
    </head>
    <style>
        * {
            padding: 0;
            margin: 0;
        }

        ul {
            width: 100%;
            height: 40px;
            list-style: none;
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: center;
            background: yellowgreen;
        }

        ul li {
            width: 20%;
            height: 100%;
            color: white;
            line-height: 40px;
            text-align: center;
            text-transform: uppercase;
        }
    </style>
    </style>

<body>
    <div id="app">
        <ul>
            <li v-for='item in items'>{{item}}</li>
        </ul>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                items: ['小鸡炖蘑菇', '红烧狮子头', '炸鸡腿', '清蒸鲤鱼', '红烧排骨', '油焖大虾'],
            },
        });
    </script>
</body>

</html>
```

# 4. 数组更新方法

一些操作数组的方法，编译会检测，从而会促使视图更新。

### 变异方法

- `push()`
- `pop()`
- `shift()`
- `unshift()`
- `splice()`
- `sort()`
- `reverse()`

上面这些数组操作方法，会直接改变原始数组称为变异方法，会促使视图自动更新。

### 替换数组

学了 JavaScript 标准对象库，都知道有些数组方法是不直接改变原数组的，这里称他们为非变异方法，例如：filter()、slice()、concat()，他们都是返回一个新数组，那么，在 Vue 中使用到这些方法，怎么样才能促使视图更新呢？我们就必须使用数组替换法，将非变异方法返回的新数组直接赋值给的旧数组

```js
this.nav = this.nav.slice(1, 4);
```

### 注意

由于 JavaScript 的限制，Vue 不能检测以下变动的数组：

1. 当你利用索引直接设置一个项时，例如：`vm.items[indexOfItem] = newValue`
2. 当你修改数组的长度时，例如：`vm.items.length = newLength`

例子：

```js
var app = new Vue({
  data: {
    items: ['a', 'b', 'c'],
  },
});
app.items[1] = 'x'; // 不是响应性的
app.items.length = 2; // 不是响应性的
```

上去直接这样改值操作是没有问题的，但是不是响应式的，并不能触发视图更新，需要用其他方法代替。

例如这样的操作 `app.items[indexOfItem] = newValue` ，可以用以下两种代替。

```js
// Vue.set
Vue.set(vm.items, indexOfItem, newValue);
// Array.prototype.splice
vm.items.splice(indexOfItem, 1, newValue);
```

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <style>
      * {
        padding: 0;
        margin: 0;
      }
      a {
        text-decoration: none;
        color: #fff;
      }
      ul {
        list-style: none;
      }
      nav,
      ul {
        width: 100%;
        display: flex; /* 开启弹性盒模型 布局方式*/
        flex-direction: row;
        justify-content: center;
        background: yellowgreen;
      }
      nav > ul > li {
        width: 20%;
        height: 100%;
        text-align: center;
        line-height: 50px;
      }
      nav > ul > li:hover {
        box-shadow: 1px 0px 10px #fff;
      }
      nav > ul > li > ul {
        display: flex;
        flex-direction: column;
      }
      nav > ul > li > ul > li {
        box-shadow: 1px 0px 10px #fff;
      }
      nav > ul > li > a {
        text-transform: uppercase;
      }
    </style>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <nav>
        <ul>
          <!-- 循环渲染一级菜单 -->
          <!-- 鼠标移入触发currentIndex(index)函数，更正current是当前菜单的index, 鼠标移出重置current为空  事件回调方法在methods中实现-->
          <li
            v-for="(nav,index) in navbar"
            :key="index"
            @mouseover="currentIndex(index)"
            @mouseout="changeIndex"
          >
            <!-- nav.name 一级菜单名字 -->
            <a href="javascript:;">{{nav.name}}</a>
            <!-- 如果nav.child存在，说明有子菜单，再次循环渲染子菜单 -->
            <!-- 子菜单v-show  如果当前菜单的 index 等于 鼠标移入那个菜单的下标我们就展示出子菜单-->
            <ul v-if="nav.child" v-show="current===index">
              <li v-for="item in nav.child">
                <a href="javascript:;">{{item}}</a>
              </li>
            </ul>
          </li>
        </ul>
      </nav>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          //navbar 模拟后台获取到的菜单列表
          navbar: [
            {
              name: 'home',
              child: ['homeItem', 'homeItem'],
            },
            {
              name: 'contact',
              child: ['contactItem', 'contactItem'],
            },
            {
              name: 'about',
            },
          ],
          //current 当前鼠标在那个菜单上 ，初始时没有值
          current: null,
        },
        methods: {
          //更正 当前鼠标移入的是哪个菜单的 index
          currentIndex: function (index) {
            this.current = index;
          },
          //鼠标移出 重置current 值
          changeIndex: function () {
            this.current = null;
          },
        },
      });
    </script>
  </body>
</html>
```

# 5. 事件处理

在平常开发中，对 DOM 的操作很常见，然而 Vue 中是虚拟 DOM 不太提倡直接进行原生 DOM 操作，降低性能。在 Vue 中可以使用 `v-on` 指令来操作 DOM 事件。

#### 知识点

- 事件绑定指令 `v-on`
- 事件处理方法
- 内联方法
- 修饰符

实现计数器

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=，d, initial-scale=1.0">
    <title>Document</title>
</head>
<script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.9/vue.js"></script>
    <style>
        button {
            width: 150px;
            height: auto;
            background: chartreuse;
            outline: auto;
            color: f0f;
        }
    </style>
<body>
    <div id="app">
        <!-- 简写形式 -->
        <button @click="counter+=1">点击</button>
        <!-- 常规写法 -->
        <button v-on:click="counter+=1">点击</button>
        <p>你点击了{{counter}}</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                counter: 0,
            },
        });
    </script>
</body>

</html>
```

内联处理器的方法

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
    <style>
        button {
            width: 150px;
            height: 40px;
            border-radius: 10px;
            background: green;
            outline: none;
            color: #fff;
        }
    </style>
</head>

<body>
    <div id="app">
        <!-- 绑定点击监听 共用say 方法-->
        <button v-on:click="say('实验楼')">实验楼</button>
        <button v-on:click="say('小楼')">小楼</button>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {},
            methods: {
                //声明事件点击监听 say方法
                say: function(name) {
                    /* es6 的新语法特性 */
                    alert(`我是${name}`);
                },
            },
        });
    </script>
</body>

</html>
```

阻止冒泡行为.stop

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
    <style>
        /* 居中 */
        
        .super,
        .child {
            position: absolute;
            top: 0;
            left: 0;
            bottom: 0;
            right: 0;
            margin: auto;
        }
        
        .super {
            width: 300px;
            height: 300px;
            background: pink;
        }
        
        .super .child {
            width: 100px;
            height: 100px;
            background: green;
        }
    </style>
</head>

<body>
    <div id="app">
        <div class="super" v-on:click="handleClick('super')">
            父
            <div class="child" v-on:click="handleClick('child')">
                子
            </div>
        </div>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {},
            methods: {
                //声明事件点击监听 handleClick
                handleClick: function(name) {
                    alert(`我是${name}`);
                },
            },
        });
    </script>
</body>

</html>
```

未添加 `.stop`修饰符，事件会触发冒泡行为，点击子元素也会触发父元素的相同事件

修改代码

```html
<div id="app">
  <div class="super" v-on:click.stop="handleClick('super')">
    父
    <div class="child" v-on:click.stop="handleClick('child')">
      子
    </div>
  </div>
</div>
```

在监听键盘事件时，我们经常需要检查详细的按键。Vue 允许为 `v-on` 在监听键盘事件时添加按键修饰符：

```
.enter
.tab
```

`.delete` (捕获“删除”和“退格”键)

```
.esc
.space
.up
.down
.left
.right
```

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <input
        type="text"
        v-on:keyup.enter="alert('你按了enter,确定输入完毕？')"
      />
    </div>
    <script>
      var app = new Vue({
        el: '#app',
      });
    </script>
  </body>
</html>
```

系统特定的组合功能键，如 `ctrl+c` 、`ctrl+v` 。

可以用如下修饰符来实现仅在按下相应按键时才触发鼠标或键盘事件的监听器。

- `.ctrl`
- `.alt`
- `.shift`
- `.meta`

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <button @click.ctrl="alert('你不单按鼠标键盘，按其他键也可以触发')">A</button>
        <button @click.ctrl.exact="alert('鼠标点击和ctrl 同时按才会触发')">A</button>
        <button @click.exact="alert('没有任何系统修饰符才会触发')">A</button>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: 'hello',
            }
        })
    </script>
</body>

</html>
```

鼠标按钮修饰符

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <button @click.left="alert('你按了鼠标左击键')">按钮</button>
      <button @click.middle="alert('你按了鼠标滚轮')">按钮</button>
      <button @click.right="alert('你按了鼠标右击键')">按钮</button>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
      });
    </script>
  </body>
</html>
```

# 6. 表单处理

v-model 数据的双向绑定

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>syl-vue-test</title>
    <!-- 引入 vue.js -->
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <input v-model="msg" placeholder="请输入..." />
      <p>输入的是: {{ msg }}</p>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data: {
          msg: '',
        },
      });
    </script>
  </body>
</html>
```

单选

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <!-- 将单选按钮绑定到同一个picked -->
        <input type="radio" id="one" value="One" v-model="picked" />
        <label for="one">One</label>
        <br />
        <input type="radio" id="two" value="Two" v-model="picked" />
        <label for="two">Two</label>
        <br />
        <span>Picked: {{ picked }}</span>
    </div>
    <script>
        var vue = new Vue({
            el: '#app',
            data: {
                picked: '',
            }

        });
    </script>
</body>

</html>
```

复选

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <!-- 将单选按钮绑定到同一个picked -->
        <input type="radio" id="one" value="One" v-model="picked" />
        <label for="one">One</label>
        <br />
        <input type="radio" id="two" value="Two" v-model="picked" />
        <label for="two">Two</label>
        <br />
        <span>Picked: {{ picked }}</span>
    </div>
    <script>
        var vue = new Vue({
            el: '#app',
            data: {
                picked: '',
            }

        });
    </script>
</body>

</html>
```

复选绑定同一数组中

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <input type="checkbox" id="syl1" value="syl1" v-model="checkedNames" />
      <label for="syl1">syl1</label>
      <input type="checkbox" id="syl2" value="syl2" v-model="checkedNames" />
      <label for="syl2">syl2</label>
      <input type="checkbox" id="syl3" value="syl3" v-model="checkedNames" />
      <label for="syl3">syl3</label>
      <br />
      <span>Checked names: {{ checkedNames }}</span>
    </div>
    <script>
      var vue = new Vue({
        el: '#app',
        data() {
          return {
            checkedNames: [],
          };
        },
      });
    </script>
  </body>
</html>
```

选择框

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <!-- select标签是绑定  数据项 selected -->
      <select v-model="selected">
        <option disabled value="">请选择</option>
        <option>A</option>
        <option>B</option>
        <option>C</option>
      </select>
      <span>Selected: {{ selected }}</span>
    </div>
    <script>
      var vue = new Vue({
        el: '#app',
        data() {
          return {
            selected: '',
          };
        },
      });
    </script>
  </body>
</html>
```

###### 选择框

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>

    <div id="app">
        <select v-model="selected">
            <option disabled value="">请选择</option>
            <option>A</option>
            <option>B</option>
            <option>C</option>
        </select>
        <span>Selected: {{ selected }}</span>
    </div>

    <script>
        var vue = new Vue({
            el: '#app',
            data: {
                selected: '',
            },
        });
    </script>
</body>

</html>
```

checkbox 和 radio 使用 `checked` 属性，所以直接给元素 value 值，当选中时 data 中声明的绑定项的值就为元素 value。

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>
  <body>
    <div id="app">
      <!-- 当选中时，`picked` 为字符串 "a" -->
      <input type="radio" v-model="picked" value="a" />
      {{picked}}
    </div>
    <script>
      var vue = new Vue({
        el: '#app',
        data() {
          return {
            picked: '',
          };
        },
      });
    </script>
  </body>
</html>
```

日常开发中使用复选框

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>

  <body>
    <div id="app">
      <input
        type="checkbox"
        v-model="toggle"
        true-value="yes"
        false-value="no"
      />
      <p>toggle:{{toggle}}</p>
    </div>
    <script>
      //通过true-value="yes" false-value="no"属性控制，选中时toggle值为yes,未选中时为no
      var vue = new Vue({
        el: '#app',
        data() {
          return {
            toggle: '',
          };
        },
      });
    </script>
  </body>
</html>
```

选择框

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>

    <div id="app">
        <select v-model="selected">
            <option disabled value="">请选择</option>
            <option>A</option>
            <option>B</option>
            <option>C</option>
        </select>
        <span>Selected: {{ selected }}</span>
    </div>

    <script>
        var vue = new Vue({
            el: '#app',
            data: {
                selected: '',
            },
        });
    </script>
</body>

</html>
```

### 修饰符

v-model 的三种修饰符

###### .lazy   懒加载

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <input v-model.lazy="msg" />
        <p>{{msg}}</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: 'hello lazy1',
            },
        });
    </script>
</body>

</html>
```



###### .number  指定数据类型

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <!-- 没有使用。number 修饰符， -->
        <input v-model="number1" type="number" />
        <!-- 使用typeof进行类型检测 -->
        <p>{{typeof(number1)}}</p>
        <!-- 使用。number  -->
        <input v-model.number="number2" type="number" />
        <p>{{typeof(number2)}}</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                number1: '',
                number2: '',
            },
        });
    </script>
</body>

</html>
```



###### .trim   首位去除空格

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <input v-model.trim="msg" />
        <p>你好{{msg}}我是</p>
    </div>
    <script>
        var app = new Vue({
            el: '#app',
            data: {
                msg: '',
            }
        })
    </script>
</body>

</html>
```





### 综合练习

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
    <style>
      * {
        padding: 0;
        margin: 0;
      }
      html,
      body {
        width: 100%;
        height: 100%;
        overflow: hidden;
      }
      #app {
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        margin: auto;
        width: 400px;
        height: 400px;
      }
    </style>
  </head>

  <body>
    <div id="app">
      <form class="app-form">
        <span>name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
        <input type="text" v-model="username" />
        <br />
        <span>password:</span><input type="password" v-model="password" />
        <br />
        <span
          >sex:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span
        >
        <input type="radio" id="man" value="man" v-model="sex" />
        <label for="man">man</label>
        <input type="radio" id="woman" value="woman" v-model="sex" />
        <label for="woman">women</label>
        <br />
        <span>hobby:</span>
        <input type="checkbox" id="game" value="game" v-model="hobby" />
        <label for="game">game</label>
        <input
          type="checkbox"
          id="basketball"
          value="basketball"
          v-model="hobby"
        />
        <label for="basketball">basketball</label>
        <input type="checkbox" id="study" value="study" v-model="hobby" />
        <label for="study">study</label>
        <br />
        <br />
        <p>名字：{{username}}</p>
        <p>密码：{{password}}</p>
        <p>性别:{{sex}}</p>
        <p>爱好：{{hobby}}</p>
      </form>
    </div>
    <script>
      var vue = new Vue({
        el: '#app',
        data() {
          return {
            username: '',
            password: '',
            sex: 'man', //性别单选默认勾选男
            hobby: [],
          };
        },
      });
    </script>
  </body>
</html>
```

# 7. 组件

组件注册： 分为全局注册和局部注册

全局注册

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <my-component-name></my-component-name>
        <my-component-name2></my-component-name2>
    </div>
    <script>
        /* 注册全局组件1 */
        Vue.component('my-component-name', {
            template: '<h1>全局注册组件<h1>',
            /* ... */
        });
        /* 祖册全局 */
        Vue.component('my-component-name2', {
            template: '<h1>全局注册组件2<h1>',
            /* ... */
        });

        var app = new Vue({
            el: '#app',


        });
    </script>
</body>

</html>
```



局部注册

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <component1></component1>
        <component2></component2>
    </div>

    <script>
        var component1 = {
            template: '<h1>局部组件</h1>',
        };

        var component2 = {
            template: '<h1>局部组件2</h1>',
        };

        var app = new Vue({
            el: '#app',
            components: {
                'component1': component1,
                'component2': component2,
            }

        });
    </script>
</body>

</html>
```

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <component1></component1>
        <!-- 不显示组件2 -->
        <component2></component2>
    </div>
    <div id="app2">
        <!-- 不显示组件1 -->
        <component1></component1>

        <component2></component2>
    </div>

    <script>
        
        var component1 = {
            template: '<h1>局部组件</h1>',
        };

        var component2 = {
            template: '<h1>局部组件2</h1>',
        };

        var app = new Vue({
            el: '#app',
            components: {
                'component1': component1,

            }

        });

        var app2 = new Vue({
            el: '#app2',
            components: {
                'component2': component2,

            }

        });
    </script>
</body>

</html>
```

在父级 components 对象中声明，局部组件只有它的父级才能调用

# 组件复用

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <p>hello</p>
        <btn-demo>{{counter}}</btn-demo>
        <btn-demo>{{counter}}</btn-demo>
        <btn-demo>{{counter}}</btn-demo>

    </div>


    <script>
        Vue.component('btn-demo', {
            data() {
                return {
                    counter: 0,
                }
            },
            template: ' <button @click="counter++">{{counter}}</button>',
        });
        var app = new Vue({
            el: '#app',
        });
    </script>
</body>

</html>
```

# 组件间的通信

父子组件props

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <component-title post-title="hello"></component-title>
        <component-title post-title="hello1"></component-title>
        <component-title post-title="hello2"></component-title>
        <component-title post-title="hello3"></component-title>

    </div>


    <script>
        Vue.component('component-title', {
            //props 以字符串数组的形式
            /* 使用组件时，属性值转为驼峰命名key-case */
            props: ['postTitle'],

            template: '<p>{{postTitle}}</p>',
        });
        var app = new Vue({
            el: '#app',
        });
    </script>
</body>

</html>
```

props 希望每个props 有特定的类型

```html
props:{
	title: String,
	id: Number,
	
}
```

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <component-A id="1" title="汽车" content="新闻">
        </component-A>

    </div>


    <script>
        //注册组件
        //组件名在使用时使用key-case 方式 component-A，否则不显示数据
        
        Vue.component('componentA', {
            props: {
                id: Number,
                title: String,
                content: String,
            },
            template: `<div><p>id:{{id}}</p>
                <p>title:{{title}}</p>
                <p>content:{{content}}</p>
                </div>`,
        });
        var app = new Vue({
            el: '#app',
        });
    </script>
</body>

</html>
```

子父组件的通信

子组件向父组件通信

大致语法`this.$emit('自定义事件名'，参数)`

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.12/vue.js"></script>
</head>

<body>
    <div id="app">
        <button>点击我</button>
        <!-- 2. 调用子组件指定的方法名，将消息传入父组件方法中，并执行父组件方法， -->
        <child-component v-on:send-msg="getMsg"></child-component>
    </div>
    <script>
        Vue.component('child-component', {
            //1. 定义一个点击事件，指定方法名send-msg,传递数据'我是子组件请求通信。
            //
            template: `<button v-on:click="$emit('send-msg','我是子组件，请求与您通信')">点击我</button>`,

        });
        var app = new Vue({
            el: '#app',
            methods: {
                getMsg: function(msg) {
                    //弹出子组件弹出信息
                    alert(msg);
                },
            },
        });
    </script>
</body>

</html>
```

#### 动态绑定组件

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
</head>

<body>
    <div id="app">
        <!-- 使用v-bind简写模式 动态绑定 props 值 -->
        <child-component :name="name" :age="age" :height="height"></child-component>
        <child-component :name="name+'2'" :age="age+1" :height="height"></child-component>
    </div>
    <script>
        //定义一个子组件
        Vue.component('child-component', {
            //使用属性类型检测
            props: {
                name: String,
                age: Number,
                height: String,
            },
            template: `
            <ul>
                <li>{{name}}</li>
                <li>{{age}}</li>
                <li>{{height}}</li>
            </ul>
            `,
        });
        var app = new Vue({
            el: '#app',
            data() {
                return {
                    name: 'syl',
                    age: 20,
                    height: '180cm',
                };
            },
        });
    </script>
</body>

</html>
```

# 生命周期函数

了解真个实例的生命周期，有哪些钩子函数

# 

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>vue</title>
    <script src="https://labfile.oss.aliyuncs.com/courses/1262/vue.min.js"></script>
  </head>

  <body>
    <div id="app">
      <button @click="handleClick">{{name}}</button>
    </div>
    <script>
      var app = new Vue({
        el: '#app',
        data() {
          return {
            name: 'syl',
          };
        },
        methods: {
          handleClick: function () {
            this.name = 'syl syl';
          },
        },
        beforeCreate() {
          alert(
            '在实例初始化之后，数据观测 (data observer) 和 event/watcher 事件配置之前被调用'
          );
        },
        created() {
          alert(
            '在实例创建完成后被立即调用,挂载阶段还没开始，$el 属性目前不可见'
          );
        },
        beforeMount() {
          alert('在挂载开始之前被调用：相关的 render 函数首次被调用');
        },
        mounted() {
          alert('el 被新创建的 vm.$el 替换，并挂载到实例上去之后调用该钩子');
        },
        beforeUpdate() {
          alert('数据更新时调用');
        },
        updated() {
          alert('组件 DOM 已经更新');
        },
        beforeDestroy() {},
        destroyed() {},
      });
    </script>
  </body>
</html>
```

# 过渡与动画

# vue 进阶

```javascript
import Vue from 'vue';
import App from './App';
import router from './router';

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
});
```

vue 2.0 

setp  1. 安装 node

setp 2 npm 安装vue-cli

`npm install -g vue-cli`

查看安装成功

vue -V

创建应用

```html
vue init webpack <项目名> # sylApp 这里是项目名全部小写。
cd <项目名>
npm run start             # 启动项目
```

src 目录下的App.vue

```html

<template>
   <!--组件中template html 表达式-->
  <div id="app">
    <img src="./assets/logo.png">
    <router-view/>
  </div>
</template>

<script>
    //javaScript 选项组件
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>

```

* 前段路由管理 vue -router

* 前端数据状态处理vuex

* 服务端渲染  nuxt.js

# vue 3 基础入门

## vue 的安装



### 1. 下载安装 node.js

node.js 是一个 js 运行环境，Vue 工程需要建立在 node.js 的基础上。

npm 是 node.js 的包管理器，npm（node package manager）

通过 npm 完成一些环境的安装

https://nodejs.org/en/download/

下载 LTS 版，长期维护的。

下载之后直接安装。

默认安装路径  `c:\Program Files\nodejs`,可以自行安装修改

安装成功之后，如何检测是否成功？

```
node -v
```



### 2. 配置环境变量

找到你的 node.js 安装路径`c:\Program Files\nodejs`，新建两个文件夹 “node_global”、“node_cache”

在 CMD 中输⼊命令

```
npm config set prefix "c:\Program Files\nodejs\node_global"
npm config set cache "c:\Program Files\nodejs\node_cache"
```

在我的电脑---》高级环境配置--〉设置环境变量

```
NODE_PATH = c:\Program Files\nodejs
PATH =%NODE_PATH%\;%NODE_PATH%\node_modules;%NODE_PATH%\node_global;
```

### 3. npm 安装淘宝镜像

CMD 执行

```
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

国内镜像访问较快

### 4.淘宝镜像安装失败可以执行如下命令

```
npm config set registry https://registry.npm.taobao.org
```

直接替换npm 源

### 5.npm 安装 **Vue**

CMD 执行 淘宝镜像安装成功使用cnpm,不成功可以直接使用npm

```
cnpm install -g @vue/cli
npm install -g @vue/cli
```

安装成功之后，如何检测是否成功？

```
vue -V
```

# 6. 在线创建工程

1. 在线创建

```
vue ui
```

也可以使用 cmd 创建

2.  创建一个新的项目

`vue create <项目名>`

3. 启动项目

> cd <项目路径>
>
> npm run serve   
>
> 

router 访问.vue 文件用的

vuex 实现数据同步

Linter  格式检查

vue 是一个单页面应用（SPA）sinlge Page Application，

真整个工程只有一个页面，各个页面的跳转，动态更新父页面的应用。

# Element UI

前段框架

# axios

负责数据和视图的绑定

使用RESTFUL  



前段页面展示



解决跨域问题。





