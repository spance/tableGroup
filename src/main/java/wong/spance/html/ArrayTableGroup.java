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

import wong.spance.html.element.Cell;
import wong.spance.html.element.Table;
import wong.spance.html.element.TableMeta;
import wong.spance.html.merge.MergeContext;
import wong.spance.html.render.DefaultModifiers;
import wong.spance.html.render.Modifier;
import wong.spance.html.render.ModifierProvider;
import wong.spance.html.render.RenderContext;
import wong.spance.html.span.CellSpan;
import wong.spance.html.stat.Statistics;
import wong.spance.html.stat.StatisticsContext;
import wong.spance.html.store.DataStore;

import java.util.Map;

/**
 * 以数组为数据源，数组下标为选择方式的基本实现
 */
public class ArrayTableGroup implements TableGroup {

    protected final TableMeta meta;
    protected final Table table;

    public ArrayTableGroup(TableMeta meta) {
        this.meta = meta;
        table = new Table(meta.size());
        table.setHead(meta.getColumns());
    }

    @Override
    public void apply(DataStore data) {
        if (data == null || data.getRowCount() <= 0) {
            throw new IllegalArgumentException();
        }
        for (Object[] row : data) {
            if (row.length != meta.size()) {
                throw new IllegalStateException();
            }
            table.appendRow(row);
        }
    }

    @Override
    public TableMeta getMeta() {
        return meta;
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public void group(CellSpan... cellSpans) {
        MergeContext mergeContext = new MergeContext(meta, table);
        mergeContext.initContext(cellSpans);

        Cell[][] grid = table.getBody();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (!mergeContext.hasSpanDefinition(cell.getX())) {
                    continue;
                }
                mergeContext.doCellMerge(cell);
            }
        }
    }

    @Override
    public Map<String, Number> statistics(Statistics.Rule... rules) {
        StatisticsContext context = new StatisticsContext(table);
        context.initContext(rules);
        for (int i = 0; i < meta.size(); i++) {
            if (context.hasRule(i)) {
                context.doStat(i, table.getCell(i, 0));
            }
        }
        return context.calculate();
    }

    @Override
    public String render() {
        return render(new DefaultModifiers());
    }

    @Override
    public String render(ModifierProvider provider) {
        RenderContext context = new RenderContext(meta);
        context.initModifiers(provider);
        table.render(context);
        return context.out();
    }

    @Override
    public String render(Modifier... modifiers) {
        RenderContext context = new RenderContext(meta);
        context.initModifiers(modifiers);
        table.render(context);
        return context.out();
    }
}
