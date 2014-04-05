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

import wong.spance.html.render.RenderContext;

/**
 * Created by spance on 14/3/29.
 */
public class TableColumn extends HtmlElement {

    private int x;
    private boolean hidden;

    public TableColumn(int x, String text, boolean hidden) {
        super("th", null);
        this.x = x;
        setText(text);
        this.hidden = hidden;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public void render(RenderContext context) {
        if (!hidden) {
            super.render(context);
        }
    }
}
