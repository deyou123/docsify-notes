var Person = /** @class */ (function () {
    //定义构造器
    function Person(firstName, lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = this.firstName + '_' + this.lastName;
    }
    return Person;
}());
//定义一个函数
function fullName(person) {
    return person.firstName + '_' + person.lastName;
}
//实例化对象
var person = new Person('诸葛', '孔明');
console.log(fullName(person));
