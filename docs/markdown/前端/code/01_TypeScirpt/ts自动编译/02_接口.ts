(()=>{
    interface IPerson{
        firstName: string //姓氏
        lastName: string //名字
    }
    //输出姓名
    function fullName(person:IPerson){
        return person.firstName + '_' + person.lastName;
    }

    const person = {
        firstName:'东方',
        lastname: '不败'
    }
    console.log(person)
})()