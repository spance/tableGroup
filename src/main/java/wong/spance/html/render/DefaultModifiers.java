package wong.spance.html.render;

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
import wong.spance.html.element.HtmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spance on 14/4/4.
 */
public class DefaultModifiers implements ModifierProvider {

    private Map<ModifierPoint, List<Modifier>> modifiers;

    public DefaultModifiers() {
        modifiers = new HashMap<ModifierPoint, List<Modifier>>();
        putIntoList(ModifierPoint.BEFORE, new CellSpanModifier());
    }

    void putIntoList(ModifierPoint key, Modifier value) {
        List<Modifier> list = modifiers.get(key);
        if (list == null) {
            list = new ArrayList<Modifier>();
            modifiers.put(key, list);
        }
        list.add(value);
    }

    @Override
    public Map<ModifierPoint, List<Modifier>> getModifiers() {
        return modifiers;
    }

    /**
     * 根据pattern选择到的对象，用regex匹配，用replacement替换
     *
     * @param pattern
     * @param regex
     * @param replacement
     * @return
     */
    public DefaultModifiers replace(final String pattern, final String regex, final String replacement) {
        putIntoList(ModifierPoint.TEXT, new TextRenderModifier(pattern) {

            @Override
            protected String handler(HtmlElement element, RenderContext context) {
                return element.getText().replaceAll(regex, replacement);
            }
        });
        return this;
    }

    /**
     * 根据pattern选择到的对象，为其设置属性k-v
     *
     * @param pattern
     * @param key
     * @param value
     * @return
     */
    public DefaultModifiers setAttribute(final String pattern, final String key, final String value) {
        putIntoList(ModifierPoint.BEFORE, new BeforeRenderModifier(pattern) {

            @Override
            protected Boolean handler(HtmlElement element, RenderContext context) {
                element.setAttribute(key, value);
                return true;
            }
        });
        return this;
    }

    public DefaultModifiers setAttributes(final String pattern, final Map<String, String> attributes) {
        putIntoList(ModifierPoint.BEFORE, new BeforeRenderModifier(pattern) {

            @Override
            protected Boolean handler(HtmlElement element, RenderContext context) {
                element.getAttributes().putAll(attributes);
                return true;
            }
        });
        return this;
    }

    public DefaultModifiers setEmptyText(final String pattern, final String emptyText) {
        putIntoList(ModifierPoint.BEFORE_END, new BeforeEndRenderModifier(pattern) {

            @Override
            protected String handler(HtmlElement element, RenderContext context) {
                if (element.childrenSize() < 1) {
                    return emptyText;
                }
                return null;
            }
        });
        return this;
    }

    /**
     * 单元格合并
     */
    public static class CellSpanModifier extends BeforeRenderModifier {

        public CellSpanModifier() {
            super("tr td");
        }

        @Override
        public Boolean handler(HtmlElement element, RenderContext context) {
            Cell cell = (Cell) element;
            boolean display = !context.getMeta().getColumn(cell.getX()).isHidden();
            if (display && cell.getSpanRange().isStart(cell)) {
                int range = cell.getSpanRange().getRangeY();
                if (range > 0)
                    cell.setAttribute("rowspan", range + 1);
                return true;
            }
            return false;
        }
    }

}
