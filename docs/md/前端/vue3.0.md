# vue 3.0 教程



# 2. 创建 vue3 项目

## [#](http://huaxhe.gitee.io/vue3_study_docs/chapter3/02_创建vue3项目.html#_1-使用-vue-cli-创建)1) 使用 vue-cli 创建

[文档指南: (opens new window)](https://cli.vuejs.org/zh/guide/creating-a-project.html#vue-create)

```bash
## 安装或者升级
npm install -g @vue/cli
## 保证 vue cli 版本在 4.5.0 以上
vue --version
## 创建项目
vue create my-project
```

然后的步骤

- Please pick a preset - 选择 ***Manually select features\***
- Check the features needed for your project - 选择上 ***TypeScript\*** ，特别注意点空格是选择，点回车是下一步
- Choose a version of Vue.js that you want to start the project with - 选择 ***3.x (Preview)\***
- Use class-style component syntax - 直接回车
- Use Babel alongside TypeScript - 直接回车
- Pick a linter / formatter config - 直接回车
- Use history mode for router? - 直接回车
- Pick a linter / formatter config - 直接回车
- Pick additional lint features - 直接回车
- Where do you prefer placing config for Babel, ESLint, etc.? - 直接回车
- Save this as a preset for future projects? - 直接回车

## [#](http://huaxhe.gitee.io/vue3_study_docs/chapter3/02_创建vue3项目.html#_2-使用-vite-创建)2) 使用 vite 创建

- [文档指南:(opens new window)](https://v3.cn.vuejs.org/guide/installation.html)
- vite 是一个由原生 ESM 驱动的 Web 开发构建工具。在开发环境下基于浏览器原生 ES imports 开发，
- 它做到了***本地快速开发启动***, 在生产环境下基于 Rollup 打包。
	- 快速的冷启动，不需要等待打包操作；
	- 即时的热模块更新，替换性能和模块数量的解耦让更新飞起；
	- 真正的按需编译，不再等待整个应用编译完成，这是一个巨大的改变。

```bash
npm init @vitejs/app <project-name>
cd <project-name>
npm install
npm run dev
```

# 3. 基础API

- setup()
- createApp()
- ref 与 reactive()
- computed()
- readonly

## 3.1 创建项目

使用typeScript .

> vue create my-project

启动项目测试代码：

修改main.ts

```tsx
import { createApp } from 'vue'
import App from './App.vue'


createApp(App).mount('#app')

```

修改

App.vue

```vue
<template>
  <div>
    <h3> 我又变帅了</h3>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
export default defineComponent({
  name : 'App'
})

</script>
```

重新启动项目

访问 [http://localhost:8080](http://localhost:8080/)

结果如图

> ### 我又变帅了

## 3.2 setup

