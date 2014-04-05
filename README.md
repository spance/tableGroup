说明
=========

TableGroup专为分组式表格而生，适用于把一个二维数据按照一定规则进行分组合并生成html table，通过简单的分组规则生成一个高度自定义的table组件。

一种典型的情况就是从统计类SQL结果作为数据源，交给TableGroup按规则执行各种分组合并单元格，然后输出，其间可以提取一些数据供图表等其它组件使用而不必发起更多次的查询。

##特点

- 这是非常高效的java实现，比使用大规模的统计报表引擎要易用和轻量的多
- 比使用各类页面模版化手段更要简单、易用、通用
- 在不手动操作html的情况下完成同等的高度灵活自定义的需求

##适合的场景

- 二维数据在多个列上进行分组合并
- 二维数据多个列的分组要约束在前一列(某一列)的分组中
- 对分组后的数据进行简单统计计算
- 对分组后的html对象在渲染输出时基于css选择器的拦截和修改

##主要的实现

- HtmlElement的树型构建
- 节点的渲染器、拦截点、修改器和对应规则
- 分组规则的构建和定义
- css选择器的语法分析和构建
- css选择器的倒序匹配器
- 简易的统计计算

##效果

![image](https://github.com/spance/tableGroup/raw/master/tableGroup-demo.png)

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

要执行单元格合并或者统计计算，则需要进行分组

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

在分组后，可执行抽取简单的统计计算结果

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