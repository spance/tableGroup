package wong.spance.html.span;

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

import wong.spance.html.element.TableColumn;
import wong.spance.html.merge.MergeContext;

import java.util.Map;

/**
 * Created by spance on 14/3/29.
 */
public class SpanDefinition {
    private char type;
    private int by;
    private int on;
    private Integer cascadeIndex;
    private Map<TableColumn, SpanDefinition> configMap;
    private TableColumn cascadeColumn;

    public SpanDefinition(char type) {
        this.type = type;
    }

    public SpanDefinition(char type, int by) {
        this.type = type;
        this.by = by;
    }

    public SpanDefinition(char type, int by, int on) {
        this.type = type;
        this.by = by;
        this.on = on;
    }

    public SpanDefinition(char type, int by, int on, int cascadeIndex) {
        this.type = type;
        this.by = by;
        this.on = on;
        this.cascadeIndex = cascadeIndex;
    }

    public int getBy() {
        return by;
    }

    public int getOn() {
        return on;
    }

    public void ref(Map<TableColumn, SpanDefinition> configMap, TableColumn cascadeColumn) {
        this.configMap = configMap;
        this.cascadeColumn = cascadeColumn;
    }

    public Integer getCascadeIndex() {
        return cascadeIndex;
    }

    public SpanDefinition getCascade() {
        return cascadeColumn == null ? null : configMap.get(cascadeColumn);
    }

    public Object validation(int x, MergeContext context) {
        if (by < 0 || by >= x || on < 0 || on >= x)
            return "by或on的列索引值错误！";
        if (cascadeIndex != null) {
            if (cascadeIndex < 0 || cascadeIndex >= x)
                return "cascadeIndex的列索引值错误！";
            if (context.get(cascadeIndex) == null)
                return "cascadeIndex的列索引值无法找到！";
        }
        return true;
    }
}
