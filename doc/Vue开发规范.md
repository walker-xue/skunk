## 前言

好的命名规范可以让不用为取名字而苦恼，同时找bug时更快的定位到位置。

- 与它的功能相配套
- 不与其他业务组件命名冲突，容易区分
- 不一定好听酷炫，但实用，开发者根据命名就会知道组件的实用场景
- 易写易记、短小却精炼、不繁琐

### BEM

- .nav某一块展示/功能区域 (div)
- .nav__item这块展示/功能区域（div）里面的某个元素，比如: nav__item
- .nav__item--hide/ .nav__item--open 某个元素或者某个块的状态

> 注：不要使用**敏感**词汇。例如：给元素绑定class为advertisement样式，浏览器开启去广告模式后，可能直接把这个div给删掉。

## 函数与属性命名规则

1. 把函数未执行归咎于代码逻辑问题
2. 使用正常的时态，特别是代码中状态的变量或者函数的命名。
```
比如 onXxxxStarted 表示xxx已经启动了，isConnecting表示正在连接。
正确的时态可以给使用者传递准确的信息。	
```
### 函数与属性的命名区别

1. 函数，建议使用动宾结构，动宾结构就是 doSomething，这样的函数命名含义明确。
比如：openFile, setName, addNumber...
2. 属性，建议使用定语+名词。
比如: fileName, maxLength, textSize
3. 不要单词+拼音混合使用
比如：useJiFen，huKouNumber.. 缺乏美感不说，可读性大幅度降低。
4. 谨慎使用缩写
除非是约定俗成已经被广泛使用的缩写，否则老老实实用完整拼写。
典型的反面例子: count->cnt, manager->mgr password->pwd button->btn。
无论我们使用eclipse 或者intellij, 都有很好的自动完成功能，名字长一点没关系的，可读性更重要。
5. 命名的语义话（动词、名词的区分）

### 变量命名规则
变量使用主要集中在 data 和 methods 中，其中 data 中更多的是名词、状态布尔两种类型。

1. 名词太多，大致分为复数、后缀加Arr、加Obj之类作为约定规则
2. 状态布尔型可以使用is表示是不是，has表示有没有，can表示能不能
3. 单词本身的形式（过去式、进行时、将来时）：had开头、ing、ed结尾等

> - 表示是不是，用is + ... ：如 isEmpty
> - 表示有没有，用 has + ... ： 如 hasClass
> - 表示能不能，用 can + ... ：如 canSubmit

**Methods 中 handle + 以下**：
> *添加（add）、移除（emove）、删除（delete）、插入（insert）、开始\停止（start|begin\stop）、结束（end）、发送（send）、接受（receive），取出（get）、设置（set）、释放（release）、向上\向下（up\down）、显示\隐藏（show\hide）、打开\关闭（open\close）、增加（increment）、减少（decrement）、锁\解锁（lock\unlock）、下一个\前一个（next\previous）、创建\销毁（create\destory）、最小\最大（min\max）、可见\不可见（visible\invisible）、出栈\入栈（pop\push）、储存（store）、查询（query）*

结合业务：
表单操作提交：submit\add\delete\update\search\reset\check
上传附件：upload
关闭\打开弹窗：openWin/closeWin


## 组件命名规范

>Ant.design 的 React 组件是下面这样的时候，我感觉到一种自由的味道。首先，组件名可以使用原生 HTML 标签名，意味着再也不用较劲脑汁去规避原生 HTML 标签了。另外，这些组件都使用了首字母大写标签名，使它们很容易地与原生小写的 HTML 标签区分。

--------
组件文件名应该始终以单词**大写开头**，组件名也是以单词**大写开头**，当多个单词拼写成的组件时，单词开头大写，采用**驼峰式命名规则**。*一般是多个单词全拼，减少简写的情况，这样增加可读性。*

组件应该都放到components文件夹下，单个页面独立一个文件夹，用来放相对应的vue文件以及页面相关的样式文件，样式少可直接写到页面组件里边，这样更符合组件化的思想。

公用组件应该统一放到public文件下。
> 例如：
> 1. 公共的样式文件应该提取出来放在统一的文件中来统一管理
> 2. 项目级组件一般放到公共文件夹public下供业务组件使用

### 基础组件

应用特定样式和约定的基础组件 (也就是展示类的、无逻辑的或无状态的组件) 应该全部以一个特定的前缀开头，比如 Base、App 或 V。
```
**反例**
	components/
	|- button.vue
	|- loading.vue
	|- slide.vue
**正例**
	components/
	|- BaseButton.vue
	|- BaseLoading.vue
	|- BaseSlide.vue
```

### 单个活跃组件

单个活跃实例的组件应该以 The 前缀命名，以示其唯一性。这不意味着组件只可用于一个单页面，而是每个页面只使用一次。
这些组件永远不接受任何 prop，因为它们是为你的应用定制的，而不是它们在你的应用中的上下文。如果你发现有必要添加 prop，那就表明这实际上是一个可复用的组件，只是目前在每个页面里只使用一次。
```
**反例**
	components/
	|- SaleManage.vue
	|- ImportExcel.vue
**正例**
	components/
	|- TheSaleManage.vue
	|- TheImportExcel.vue
```
### 紧密耦合组件

与父组件紧密耦合的子组件应该以父组件的命名为前缀.如果一个组件只在其父组件某个场景下有意义,这层关系应该体现在组件名上,因为编辑器通常按照首字母顺序组织文件.
```
**反例**
	components/
	|- SearchBox.vue
	|- SearchItem.vue
	|- SearchButton.vue
**正例**
	components/
	|- SearchBox.vue
	|- SearchBoxItem.vue
	|- SearchBoxButton.vue
```
### 组件名中的单词顺序

组件名应该以高级别的单词开头，以描述性的修饰词结尾。
```
**反例**
	components/
	|- ClearSearchButton.vue
	|- ExcludeFromSearchInput.vue
	|- LaunchOnStartupCheckbox.vue
	|- RunSearchButton.vue
	|- SearchInput.vue
	|- TermsCheckbox.vue
**正例**
	components/
	|- SearchButtonClear.vue
	|- SearchButtonRun.vue
	|- SearchInputQuery.vue
	|- SearchInputExcludeGlob.vue
	|- SettingsCheckboxTerms.vue
	|- SettingsCheckboxLaunchOnStartup.vue
```

### 应使用完整单词

编辑器中的自动补全已经相当友好,让书写长的组件名的代价已经可以微乎其微,同样的效率更易于理解,何乐而不为?
```
**反例**
	components/
	|- soManage.vue
	|- woManage.vue
**正例**
	components/
	|- SaleOrderManage.vue
	|- WorkOrderManage.vue
```
### PROP定义与书写

#### 书写格式

在声明时始终采用(camelCase),在模板和 JSX 中应该始终使用( kebab-case)。
单纯的遵循每个语言的约定。在 JavaScript 中更自然的是 camelCase。而在 HTML 中则是 kebab-case。

```
**反例**
props: {
	'greeting-text': String
}
<WelcomeMessage greetingText="hi"/>

**正例**
props: {
	greetingText: String
}
<WelcomeMessage greeting-text="hi"/>
```
#### 定义规则

当定义组件时，应该对传入组件的props进行严格的定义，至少指定类型，同时设定默认值。这样可以好处是方便他人使用。

```vue
props:{
	modelflag:{
		type:Number,
		default:0
	}
}
```
### 组件结构

组件结构一个遵循从上往下是template，script，style的结构

```vue
<template>
	<div></div>
</template>

<script>
</script>

<style>
</style>
```

### 组件样式

单个组件样式一般可直接写到组件下style标签下，为了防止样式污染，可添加scoped 属性，
也可以通过设置作用域来防止样式污染，写样式的时候尽量少写元素选择器，为了提高代码查找速度，可以用类选择器。

```vue
<template>
	<div class="homePage">
		<div class="list"></div>
		<div class="banner"></div>
	</div>
</template>

<script>
</script>

<style lang="scss" scoped>
 .homePage{
	 .list{
	 }
	 .banner{
	 }
 }
</style>

```

### 多属性格式编写

当组件定义很多的props值时，应该每个特性属性一行。

```html
<SelectExportItem
	v-bind="$attrs"
	:selectItemList="selectItemList"
	@hideSelectItem="$emit('hideSelectItem')"
	@exportReport="exportReport">
</SelectExportItem>
```

### v-for指令增加 key

为 v-for指令增加 key 值，这样提高查找速度。

```
<ul>
	<li v-for="todo in todos" :key="todo.id">
		{{ todo.text }}
	</li>
</ul>
```

### 全局变量推荐使用 Vuex状态管理

当页面用到一些共享的状态的时，建议使用vuex

### Axios统一处理接口

使用Axios进行接口请求，然后对项目中的接口做统一拦截处理，封装适合项目使用的 get\post 方法。

```js
import Vue from 'vue'
import axios from 'axios'
import { encode, decode,toEncode,toDecode} from './base64.js'
import Qs from 'qs';
import router from './router'

let timeout = 30000;

let instance = axios.create({
	 //baseURL: 'https://vuedomain.com/api/',
	 timeout: timeout,
	 responseType: 'json', // default,
	 //headers: {'apikey': 'foobar'},
	 transformRequest: function(data, headers) {
		 // 为了避免qs格式化时对内层对象的格式化先把内层的对象转为
		 // 由于使用的form-data传数据所以要格式化
		 if(!(data instanceof FormData)) {
		 if (typeof data === "string") {
			headers.post['Content-Type'] = "application/json";
		 } else {
			 headers.post['Content-Type'] = "application/x-www-form-urlencoded";
			 for(let key in data) {
				if(data[key] === undefined) {
					data[key] = null;
				}
			 }
			 data = Qs.stringify(data);
			 }
		 }
		 return data;
	 }
	});
	instance.interceptors.request.use(function(config) {
	 // Do something before request is sent 
	 //header 添加Request-Token
	 //配置请求头tocken	 
	 //配置get请求	 
	 return config;
	}, function(error) {
		// Do something with request error 
		return Promise.reject(error);
	})
	// Add a response interceptor 
	instance.interceptors.response.use(function(response) {
		// Do something with response data 
	 
		if (response.data.status===400001) { //access tocken过期30天
			vm.$router.push({path:'/login'})
		}
		return response;
	}, function(error) {
	 // Do something with response error  
	return Promise.reject(error);
});
export default instance;
```
