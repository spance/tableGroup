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
public class TableMeta {

    private List<TableColumn> columns;

    public TableMeta() {
        columns = new ArrayList<TableColumn>();
    }

    public int size() {
        return columns.size();
    }

    public List<TableColumn> getColumns() {
        return columns;
    }

    public TableColumn getColumn(int index) {
        return columns.get(index);
    }

    public TableMeta addColumn(String header) {
        return addColumn(header, false);
    }

    public TableMeta addColumn(String header, boolean hidden) {
        columns.add(new TableColumn(columns.size(), header, hidden));
        return this;
    }

    public static TableMeta newMeta() {
        return new TableMeta();
    }

}
