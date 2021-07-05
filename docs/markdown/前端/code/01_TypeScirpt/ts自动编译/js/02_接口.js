(function () {
    //输出姓名
    function fullName(person) {
        return person.firstName + '_' + person.lastName;
    }
    var person = {
        firstName: '东方',
        lastname: '不败'
    };
    console.log(person);
})();
