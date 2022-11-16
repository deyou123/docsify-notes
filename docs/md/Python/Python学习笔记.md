# Python 函数

## 定义函数

```py
# 定义函数
def greet_user():
    print("hello!")
greet_user()

# 传入参数
def greet_user1(username):
    print("Hello " + username.title()+"!")
greet_user1("tom")
```

## 传递实参

```py
def my_pets(petType,petName):
    print("I Have a " + petType+", It's name is " + petName.title() + "")
#多次调用函数    
my_pets("dog","小黑")
my_pets("dog","小黑")
# 使用循环多次调用
for i in range(10):
    my_pets('cat{}'.format(i),'小黑{}'.format(i))
    
    
def describe_pet(animal_type, pet_name): 
 """显示宠物的信息""" 
 print("\nI have a " + animal_type + ".") 
 print("My " + animal_type + "'s name is " + pet_name.title() + ".") 
 # 显示指定参数名，可以不用按照顺序输入参数
describe_pet(animal_type='hamster', pet_name='harry')
describe_pet(pet_name='harry',animal_type='hamster')

#使用默认值，默认值设置要放后面
def animal(name,animal_Type='Dog'):
    print('My {} is {}'.format(animal_Type,name))
animal('大黄')
animal('Tom','Cat')

# 等效的函数调用
animal('汤姆猫')
animal(name='汤姆猫')

animal('哈利', '仓鼠') 
animal(pet_name='哈利', animal_type='仓鼠') 
animal(animal_type='仓鼠', pet_name='哈利')

```



## 返回值

```java
# 返回值
# 这里 full_Name.title() 作用是将英文单词首字母变为大写
def get_full_name(first_Name,second_Name):
    ''' 返回整洁的姓名 '''
    full_Name = first_Name + '' + second_Name
    return full_Name.title()

person_full_name = get_full_name('李','小龙')
print(person_full_name)


# 参数可选 middle_name 显式指定
def get_formatted_name(first_name, last_name, middle_name=''): 
    """返回整洁的姓名""" 
    if middle_name: 
        full_name = first_name + ' ' + middle_name + ' ' + last_name 
    else: 
        full_name = first_name + ' ' + last_name 
    return full_name.title() 
musician = get_formatted_name('李', '龙') 
print(musician) 
musician = get_formatted_name('李', '龙', '云') 
print(musician)
```



```python
# 返回字典
def build_person(frist_name,second_name):
    person = {'first' : frist_name, 'last' : second_name}
    return person
person1 = build_person('李','龙')
print(person1)

# 可选参数年龄age
def build_person(first_name, last_name, age=''): 
    """返回一个字典，其中包含有关一个人的信息""" 
    person = {'first': first_name, 'last': last_name}     
    if age: 
        person['age'] = age 
    return person 

musician = build_person('jimi', 'hendrix', age=26) 
print(musician) 
musician = build_person('jimi', 'hendrix', 27) 
print(musician) 
```



### 结合while 综合练习

```py
def get_formatted_name(first_name, last_name): 
 """返回整洁的姓名""" 
 full_name = first_name + ' ' + last_name 
 return full_name.title()

while True: 
    print("\nPlease tell me your name:") 
    
    print("\nPlease tell me your name:") 
    print("(enter 'q' at any time to quit)") 
    
    f_name = input("First name: ") 
    if f_name == 'q': 
        break 
    
    l_name = input("Last name: ") 
    if l_name == 'q': 
        break
    
    formatted_name = get_formatted_name(f_name, l_name) 
    print("\nHello, " + formatted_name + "!")
```





## 传递列表



```py
def greet_users(usernames):
    for name in usernames:
        msg = 'Hello ,{}!'.format(name.title())
        print(msg)
usernames = ['hannah', 'ty', 'margot'] 
greet_users(usernames)
```

### 实际案例

```py
# 首先创建一个列表，其中包含一些要打印的设计
unprinted_designs = ['iphone case', 'robot pendant', 'dodecahedron'] 
completed_models = [] 
# 模拟打印每个设计，直到没有未打印的设计为止
# 打印每个设计后，都将其移到列表completed_models中
while unprinted_designs: 
    current_design = unprinted_designs.pop() 
    
    #模拟根据设计制作3D打印模型的过程
    print("Printing model: " + current_design) 
    completed_models.append(current_design) 
 
# 显示打印好的所有模型
print("\nThe following models have been printed:") 
for completed_model in completed_models: 
    print(completed_model)
```

优化

```py
def print_models(unprinted_designs, completed_models): 
    """ 
    模拟打印每个设计，直到没有未打印的设计为止
    打印每个设计后，都将其移到列表completed_models中
    """ 
    while unprinted_designs: 
        current_design = unprinted_designs.pop() 

        # 模拟根据设计制作3D打印模型的过程
        print("Printing model: " + current_design) 
        completed_models.append(current_design)

def show_completed_models(completed_models): 
    """显示打印好的所有模型""" 
    print("\nThe following models have been printed:") 
    for completed_model in completed_models: 
        print(completed_model)
        
unprinted_designs = ['iphone case', 'robot pendant', 'dodecahedron'] 
completed_models = [] 
print_models(unprinted_designs, completed_models) 
print('输出未打印设计表')
print(unprinted_designs)
show_completed_models(completed_models)

```

禁止函数修改列表

函数所做的任何修改都只影响副本，而丝毫不影响原件

要将列表的副本传递给函数，可以像下面这样做：

*function_name*(*list_name*[:])

修改上面代码

```py
print_models(unprinted_designs[:], completed_models)
```





## 传递任意数量实参

### 使用位置实参和任意数量实参 `*`

```py
# 传递任意参数，形参*toppings
def make_pizza(*toppings): 
    """打印顾客点的所有配料""" 
    print(toppings) 
    
make_pizza('pepperoni') 
make_pizza('mushrooms', 'green peppers', 'extra cheese')

# 修改输出格式
def make_pizza(*toppings): 
    """概述要制作的比萨""" 
    print("\nMaking a pizza with the following toppings:") 
    for topping in toppings: 
        print("- " + topping) 
 
make_pizza('pepperoni') 
make_pizza('mushrooms', 'green peppers', 'extra cheese')

# 再加个参数，定制披萨尺寸
def make_pizza(size, *toppings): 
    """概述要制作的比萨""" 
    print("\nMaking a " + str(size) + 
    "-inch pizza with the following toppings:") 
    for topping in toppings: 
        print("- " + topping) 
 
make_pizza(16, 'pepperoni') 
make_pizza(12, 'mushrooms', 'green peppers', 'extra cheese')
```

### 任意数量的关键字实参 `**`

```py

# 形参**user_info中的两个星号让Python创建一个名为user_info的空字典
def build_profile(first, last, **user_info): 
    """创建一个字典，其中包含我们知道的有关用户的一切""" 
    profile = {} 
    profile['first_name'] = first 
    profile['last_name'] = last 
    for key, value in user_info.items():
        profile[key] = value 
    return profile 
user_profile = build_profile('albert', 'einstein', 
                            location='princeton', 
                            field='physics') 
print(user_profile) 
# output {'first_name': 'albert', 'last_name': 'einstein', 'location': 'princeton', 'field': 'physics'}
```



## 函数模块存储

放入同一文件夹下

pizza.py

```py
def make_pizza(size, *toppings): 
    """概述要制作的比萨""" 
    print("\nMaking a " + str(size) + 
    "-inch pizza with the following toppings:") 
    for topping in toppings: 
        print("- " + topping) 
```

模块导入

making_pizzas.py

```py
#  导入整个模块
import pizza 
pizza.make_pizza(16, 'pepperoni') 
pizza.make_pizza(12, 'mushrooms', 'green peppers', 'extra cheese')
# 导入特定的函数
from pizza import make_pizza 
make_pizza(16, 'pepperoni') 
make_pizza(12, 'mushrooms', 'green peppers', 'extra cheese')

# 使用 as 给函数指定别名
from pizza import make_pizza as mp 
mp(16, 'pepperoni') 
mp(12, 'mushrooms', 'green peppers', 'extra cheese')

# 使用 as 给模块指定别名

import pizza as p 
p.make_pizza(16, 'pepperoni') 
p.make_pizza(12, 'mushrooms', 'green peppers', 'extra cheese')

# 导入模块中的所有函数
from pizza import * 
make_pizza(16, 'pepperoni') 
make_pizza(12, 'mushrooms', 'green peppers', 'extra cheese')
```

