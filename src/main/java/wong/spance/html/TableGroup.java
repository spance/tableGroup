package wong.spance.html;

/*
 * Copyright 2010-2011 Spance Wong.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import wong.spance.html.element.Table;
import wong.spance.html.element.TableMeta;
import wong.spance.html.render.Modifier;
import wong.spance.html.render.ModifierProvider;
import wong.spance.html.span.CellSpan;
import wong.spance.html.stat.Statistics;
import wong.spance.html.store.DataStore;

import java.util.Map;

/**
 * Created by spance on 14/4/4.
 */
public interface TableGroup {

    /**
     * 提供DataStore的实现为指定的数据源，简单情况下用SimpleDataStore
     *
     * @param data
     */
    void apply(DataStore data);

    TableMeta getMeta();

    Table getTable();

    /**
     * 要执行单元格合并或者统计计算，则需要进行分组
     * 分组规则CellSpan，已实现RowSpan来提供Y方向分组
     * RowSpan提供链式语法，实现三种情况的分组
     * on(index) 基于index列字面值的分组
     * by(index1) - on(index2) 基于index1的字面值在index2上进行分组
     * cascade(index1) - by(index2) - on(index3) 限制在index1的分组下，基于index2的字面值在index3上进行分组
     *
     * @param cellSpans
     */
    void group(CellSpan... cellSpans);

    /**
     * 在分组后，可执行抽取简单的统计计算结果
     * 统计规则由Statistics的链式语法提供，在Statistics.Rule中承载
     * 若统计规则的选择面上出现非数字，那必然要异常
     *
     * @param rules
     * @return 返回统计结果的字典，键=分组点的值，值=分组选择面上的按rule计算的结果
     */
    Map<String, Number> statistics(Statistics.Rule... rules);

    /**
     * 对表格组件进行渲染输出
     *
     * @return
     */
    String render();

    /**
     * 由更易用的 DefaultModifiers 提供Modifier供渲染期使用
     *
     * @param provider
     * @return
     */
    String render(ModifierProvider provider);

    /**
     * 手动提供Modifier的实现，需要实现handler方法
     * Modifier定义为每个Element的渲染周期点进行修改调节
     * 提供三类抽象实现
     * BeforeRenderModifier 前置渲染调节器
     * AfterRenderModifier  后置渲染调节器
     * TextRenderModifier   文本内容渲染调节器
     *
     * @param modifiers
     * @return
     */
    String render(Modifier... modifiers);
}
