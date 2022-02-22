//类型注解： 一种轻量的的为函数添加的约束
(function () {
    function showMsg(str) {
        return '小姐姐,' + str;
    }
    var msg = '加下微信好吗？';
    //var msg = [10,20,30];
    console.log(showMsg(msg));
})();
