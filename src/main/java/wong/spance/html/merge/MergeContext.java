package wong.spance.html.merge;

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
import wong.spance.html.element.TableColumn;
import wong.spance.html.element.TableMeta;
import wong.spance.html.span.CellSpan;
import wong.spance.html.span.SpanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spance on 14/3/29.
 */
public class MergeContext {

    private final TableMeta meta;
    private final Table grid;
    private final CellComparator cellComparator;
    private final Map<TableColumn, SpanDefinition> definitionMap;

    public MergeContext(TableMeta meta, Table grid) {
        this(meta, grid, new BasicCellComparator());
    }

    public MergeContext(TableMeta meta, Table grid, CellComparator cellComparator) {
        this.grid = grid;
        this.meta = meta;
        this.cellComparator = cellComparator;
        definitionMap = new HashMap<TableColumn, SpanDefinition>();
    }

    public SpanDefinition get(int x) {
        return definitionMap.get(meta.getColumn(x));
    }

    public boolean hasSpanDefinition(int x) {
        return definitionMap.containsKey(meta.getColumn(x));
    }

    public void initContext(CellSpan[] cellSpans) {
        for (CellSpan cellSpan : cellSpans) {
            SpanDefinition definition = cellSpan.getDefinition();
            Object valid = definition.validation(meta.size(), this);
            if (valid instanceof Boolean) {
                if (definition.getCascadeIndex() != null)
                    definition.ref(definitionMap, meta.getColumn(definition.getCascadeIndex()));
                definitionMap.put(meta.getColumn(definition.getOn()), definition);
            } else
                throw new IllegalStateException(valid.toString());
        }
    }

    public void doCellMerge(Cell cell) {
        SpanDefinition spanDefinition = get(cell.getX());
        SpanDefinition cascade = spanDefinition.getCascade();
        Cell pointer = cell;
        if (cascade == null) {
            while (doCompare(spanDefinition, pointer)) {
                pointer.down().setSpanRange(pointer.getSpanRange());
                pointer = pointer.down();
            }
        } else {
            int ccX = cascade.getOn();
            Cell cascadeCell = grid.getCell(ccX, cell.getY());
            while (doCompare(spanDefinition, pointer) &&
                    cascadeCell.getSpanRange().inRangeY(pointer.down())) {
                pointer.down().setSpanRange(pointer.getSpanRange());
                pointer = pointer.down();
            }
        }
    }

    private boolean doCompare(SpanDefinition config, Cell pointer) {
        Cell cellBy = grid.getCell(config.getBy(), pointer.getY()), cellByNext = cellBy.down();
        if (cellByNext == null)
            return false;
        return cellComparator.compare(cellBy, cellByNext);
    }
}
