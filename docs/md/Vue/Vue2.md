# Vue 安装

看官网





# 创建第一个Vue2项目

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>
</head>
<body>
    <div id="app" >
        {{message}}

    </div>
    <script >
        const app = new Vue({
    		el: '#app',
    			data: {
       		message: 'Hello Vue'
   			 },
		});
    </script>
</body>
</html>
```

