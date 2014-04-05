介绍
=========

TableGroup专为分组式表格而生，把二维数据按照一定规则进行分组合并，生成一个高度自定义的HTML table组件。

一种典型的情况,从统计类SQL结果作为数据源，交给TableGroup按规则执行各种花样的分组合并单元格，然后输出，其间可以提取一些数据供图表等其它组件使用而不必发起更多次的查询。

##分析

先从几个问题的分析入手，逐步了解几种常见的分组表格问题。

假设有**数据源一**，结构如下：

<table>
<tr><th>列0</th><th>列2</th><th>列4</th><th>列5</th><th>列6</th></tr>
<tr><td>AA类</td><td>BB分类</td><td>CC子类</td><td>1.2</td><td>1</td></tr>
<tr><td>AA类</td><td>BB分类</td><td>CC子类</td><td>2.3</td><td>1</td></tr>
<tr><td>AA类</td><td>BB分类</td><td>CD子类</td><td>3.4</td><td>2</td></tr>
<tr><td>AA类</td><td>BC分类</td><td>CD子类</td><td>4.5</td><td>3</td></tr>
<tr><td>AB类</td><td>BC分类</td><td>CD子类</td><td>5.6</td><td>4</td></tr>
<tr><td>AB类</td><td>BC分类</td><td>CE子类</td><td>6.7</td><td>5</td></tr>
</table>

常规分组：按字面值对列0，列1，列2直接分组合并

<table>
<tr><th>列0</th><th>列1</th><th>列2</th><th>列3</th><th>列4</th></tr>
<tr><td rowspan=4>AA类</td><td rowspan=3>BB分类</td><td rowspan=2>CC子类</td><td>1.2</td><td>1</td></tr>
<tr><td>2.3</td><td>1</td></tr>
<tr><td rowspan=3>CD子类</td><td>3.4</td><td>2</td></tr>
<tr><td rowspan=3>BC分类</td><td>4.5</td><td>3</td></tr>
<tr><td rowspan=2>AB类</td><td>5.6</td><td>4</td></tr>
<tr><td>CE子类</td><td>6.7</td><td>5</td></tr>
</table>

**问题思考**：如果“分类”或“子类”中存在**字面相同但是不同记录**的情况（例如上面两个CC子类），那上面的合并结果显然不合要求。

首先，要求数据源增加2列“分类id”“子类id”（这两列可以设置hidden=true不让其显示）

数据源二：

<table>
<tr><th>列0</th><th>列1(分类id)</th><th>列2</th><th>列3(子类id)</th><th>列4</th><th>列5</th><th>列6</th></tr>
<tr><td>AA类</td><td>11</td><td>BB分类</td><td>110</td><td>CC子类</td><td>1.2</td><td>1</td></tr>
<tr><td>AA类</td><td>11</td><td>BB分类</td><td>111</td><td>CC子类</td><td>2.3</td><td>1</td></tr>
<tr><td>AA类</td><td>11</td><td>BB分类</td><td>112</td><td>CD子类</td><td>3.4</td><td>2</td></tr>
<tr><td>AA类</td><td>12</td><td>BC分类</td><td>112</td><td>CD子类</td><td>4.5</td><td>3</td></tr>
<tr><td>AB类</td><td>12</td><td>BC分类</td><td>112</td><td>CD子类</td><td>5.6</td><td>4</td></tr>
<tr><td>AB类</td><td>12</td><td>BC分类</td><td>113</td><td>CE子类</td><td>6.7</td><td>5</td></tr>
</table>

**TableGroup提供的分组办法：**
- 列0按照字面值分组
- 列2按照列1进行分组
- 列4按照列3进行分组

<table>
<tr><th>列0</th><th>列1(分类id)</th><th>列2</th><th>列3(子类id)</th><th>列4</th><th>列5</th><th>列6</th></tr>
<tr><td rowspan=4>AA类</td><td>11</td><td rowspan=3>BB分类</td><td>110</td><td>CC子类</td><td>1.2</td><td>1</td></tr>
<tr><td>11</td><td>111</td><td>CC子类</td><td>2.3</td><td>1</td></tr>
<tr><td>11</td><td>112</td><td rowspan=3>CD子类</td><td>3.4</td><td>2</td></tr>
<tr><td>12</td><td rowspan=3>BC分类</td><td>112</td><td>4.5</td><td>3</td></tr>
<tr><td rowspan=2>AB类</td><td>12</td><td>112</td><td>5.6</td><td>4</td></tr>
<tr><td>12</td><td>113</td><td>CE子类</td><td>6.7</td><td>5</td></tr>
</table>

解决了同名异体的分组合并问题。

**继续问题思考**：假设“分类”归属于“类”，“子类”归属于“分类”，则上面的合并结果又不合业务逻辑了。

**TableGroup提供的分组办法：**
- 列0按照字面值合并
- 列2按照列1进行分组，并约束在列0组下
- 列4按照列3进行分组，并约束在列2组下

<table>
<tr><th>列0</th><th>列1(分类id)</th><th>列2</th><th>列3(子类id)</th><th>列4</th><th>列5</th><th>列6</th></tr>
<tr><td rowspan=4>AA类</td><td>11</td><td rowspan=3>BB分类</td><td>110</td><td>CC子类</td><td>1.2</td><td>1</td></tr>
<tr><td>11</td><td>111</td><td>CC子类</td><td>2.3</td><td>1</td></tr>
<tr><td>11</td><td>112</td><td>CD子类</td><td>3.4</td><td>2</td></tr>
<tr><td>12</td><td>BC分类</td><td>112</td><td>CD子类</td><td>4.5</td><td>3</td></tr>
<tr><td rowspan=3>AB类</td><td>12</td><td rowspan=3>BC分类</td><td>112</td><td>CD子类</td><td>5.6</td><td>4</td></tr>
<tr><td>12</td><td>113</td><td rowspan=2>CE子类</td><td>6.7</td><td>5</td></tr>
<tr><td>12</td><td>113</td><td>7.8</td><td>6</td></tr>
</table>

TableGroup就是为了解决以上问题而设计的，提供了足够简便、足够强大的应用API接口。

##特点

- 这是非常高效的java实现，比使用大规模的统计报表引擎要易用和轻量的多
- 比使用各类页面模版化手段更要简单、易用、通用
- 在不手动操作html的情况下完成同等的高度灵活自定义的需求

##适合的场景

- 二维数据在多个列上进行分组合并
- 二维数据多个列的分组要约束在前一列(某一列)的分组中
- 对分组后的数据进行简单统计计算
- 对分组后的html对象在渲染输出时基于css选择器的拦截和修改

##主要部分

- HtmlElement的树型构建
- 节点的渲染器、拦截点、修改器和对应规则
- 分组规则的构建定义
- css选择器的语法分析和构建
- css选择器的倒序匹配器
- 简易的统计计算

##Demo

首先创建TableGroup对象，传入TableMeta和列的定义

```java
TableGroup builder = new ArrayTableGroup(TableMeta.newMeta()
                                      .addColumn("111")
                                      .addColumn("222")
                                      .addColumn("333"));
```

使用DataStore提供的数据, 简单情况下用SimpleDataStore

```java
builder.apply(new SimpleDataStore(new Object[][]{
                {"a1", "a2", "a3"},
                {"a1", "b2", "b3"},
                {"c1", "c2", "c3"}
        }));
```

分组规则CellSpan，已实现RowSpan来提供Y方向分组

RowSpan提供链式语法，实现三种情况的分组

- on(index) 基于index列字面值的分组
- by(1) - on(2) 基于1的字面值在2上进行分组
- cascade(1) - by(2) - on(3) 限制在1的分组下，基于2的字面值在3上进行分组


```java
builder.group(RowSpan.newRule().on(0),
              RowSpan.newRule().by(0).on(1),
              RowSpan.newRule().cascade(1).by(1).on(2) );
```

在分组后，还可执行抽取简单的统计计算结果

统计规则由Statistics的链式语法提供，在Statistics.Rule中承载

```java
Map<String, Number> map = builder.statistics(Statistics.groupBy(0).sum(2));
```

对表格组件进行默认方式渲染输出

```java
String html = builder.render();
```

也可以进行定制化渲染输出 DefaultModifiers 提供Modifier进行更复杂的属性变化替换等

```java
String html = builder.render(new DefaultModifiers()
                        .setAttribute("table", "class", "table-class")
                        .setAttribute("table th", "class", "head")
                        .setAttribute("tbody tr", "style", "kkkk")
                        .replace("tr:last-child td:nth-child(1)", "(\\d+)", "XXX$1ttt")
                        //  按照css选择器选择节点，执行对应的调节器
        );
```

一段渲染好的结果

```html
<table class="table-class">         <!-- setAttribute(前置调节器)的作用 -->
  <thead>
    <th class="head">111</th>
    <th class="head">222</th>
    <th class="head">333</th>
  </thead>
  <tbody>
    <tr style="kkkk">               <!-- setAttribute(前置调节器)的作用 -->
      <td rowspan="2">s0</td>       <!-- 合并(CellSpanModifier调节器)的作用 -->
      <td rowspan="3">b2</td>
      <td>2</td>
    </tr>
    <tr style="kkkk">
      <td>3</td>
    </tr>
    <tr style="kkkk">
      <td rowspan="3">s1</td>
      <td>4</td>
    </tr>
    <tr style="kkkk">
      <td>b </td>
      <td>5</td>
    </tr>
    <tr style="kkkk">
      <td>bB XXX22ttt--</td>        <!-- TextRenderModifier(文本调节器)替换的作用 -->
      <td>5</td>
    </tr>
  </tbody>
</table>
```
##Test

表格生成相关测试 请参见ArrayTableGroupTest

css选择器解析测试 请参见SelectorParserTest

选择器匹配器测试 请参见SelectorMatcherTest


> SelectorMatcherTest测试结果，总匹配283万次，用时2秒，平均每秒141万次

> total loop count: 2830278 , matched: 4448 , miss: 2825830 , error: 0

> used: 2.00s , speed: 1415139.00/s , avg match: 0.00071ms/per

**如有其它问题或需求欢迎讨论。**




License
----

MIT

**Free Software, Hell Yeah!**