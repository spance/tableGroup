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

import wong.spance.html.span.SpanRange;

/**
 * Created by spance on 14/3/29.
 */
public class Cell extends HtmlElement {

    private final Table owner;
    private SpanRange spanRange;

    private final int x;
    private final int y;

    public Cell(Table table, int x, int y, String text) {
        super("td", null);
        setText(text);
        this.x = x;
        this.y = y;
        owner = table;
        spanRange = new SpanRange(x, y);
    }

    public Cell up() {
        return owner.getCell(x, y - 1);
    }

    public Cell down() {
        return owner.getCell(x, y + 1);
    }

    public Cell left() {
        return owner.getCell(x - 1, y);
    }

    public Cell right() {
        return owner.getCell(x + 1, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public SpanRange getSpanRange() {
        return spanRange;
    }

    public void setSpanRange(SpanRange spanRange) {
        spanRange.setEnd(x, y);
        this.spanRange = spanRange;
    }

    @Override
    public String toString() {
        return "Cell{(" + x + "," + y + "), " + getText() + '}';
    }
}
