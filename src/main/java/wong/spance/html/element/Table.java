package wong.spance.html.element;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spance on 14/3/29.
 */
public class Table extends HtmlElement {

    private final int columnCount;
    private final List<TableRow> rowsList;
    private final TBody tBody;

    public Table(int x) {
        super("table", new ArrayList<HtmlElement>());
        columnCount = x;
        addChild(new THead(x));
        tBody = new TBody();
        rowsList = (List<TableRow>) tBody.getChildren();
        addChild(tBody);
    }

    public Cell[][] getBody() {
        Cell[][] cells = new Cell[rowsList.size()][];
        for (int i = 0; i < rowsList.size(); i++) {
            cells[i] = rowsList.get(i).getChildren().toArray(new Cell[0]);
        }
        return cells;
    }

    public Cell getCell(int x, int y) {
        if (y >= rowsList.size())
            return null;
        TableRow row = rowsList.get(y);
        if (x >= row.childrenSize())
            return null;
        return (Cell) row.getChild(x);
    }

    public void appendRow(Object[] row) {
        if (row == null || row.length != columnCount) {
            throw new IllegalArgumentException();
        }
        TableRow _row = new TableRow(columnCount);
        _row.fill(row, rowsList.size(), this);
        tBody.addChild(_row);
    }

    public void setHead(List<? extends HtmlElement> columns) {
        TableRow headRow = (TableRow) getChild(0).getChild(0);
        for (HtmlElement column : columns) {
            headRow.addChild(column);
        }
    }
}
