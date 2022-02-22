//定义一个接口
interface IPerson{
    firstName:string
    lastName:string
}
class Person{
    firstName:string
    lastName:string
    fullName:string
    //定义构造器
    constructor(firstName:string,lastName:string){
        this.firstName = firstName
        this.lastName = lastName
        this.fullName = this.firstName + '_' + this.lastName
    }
}
//定义一个函数

function fullName(person:IPerson){
    return person.firstName + '_' + person.lastName
}

//实例化对象
const person =new Person('诸葛','孔明');
console.log(fullName(person));
