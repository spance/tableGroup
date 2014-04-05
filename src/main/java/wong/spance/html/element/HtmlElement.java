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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spance on 14/3/29.
 */
public abstract class HtmlElement {

    private String tagName;
    private String text;
    private HtmlElement parentElement;
    private List<? extends HtmlElement> children;
    private Map<String, String> attributes;

    protected HtmlElement(String tagName) {
        this(tagName, new ArrayList<HtmlElement>());
    }

    protected HtmlElement(String tagName, List<? extends HtmlElement> children) {
        this.tagName = tagName;
        this.children = children;
        attributes = new HashMap<String, String>();
    }

    public final String getTagName() {
        return tagName;
    }

    public final String getId() {
        return attributes.get("id");
    }

    public final void setId(String id) {
        attributes.put("id", id);
    }

    public final String getClassName() {
        return attributes.get("class");
    }

    public final void setClassName(String className) {
        attributes.put("class", className);
    }

    public final String getText() {
        return text;
    }

    public final void setText(String text) {
        this.text = text;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public final void setAttribute(String key, Object value) {
        attributes.put(key, value.toString());
    }

    public final HtmlElement getParentElement() {
        return parentElement;
    }

    public final void setParentElement(HtmlElement parentElement) {
        this.parentElement = parentElement;
    }

    public final List<? extends HtmlElement> getChildren() {
        return children;
    }

    public final int childrenSize() {
        return children == null ? 0 : children.size();
    }

    @SuppressWarnings("unchecked")
    public final void addChild(HtmlElement child) {
        if (child.getParentElement() == null)
            child.setParentElement(this);
        ((List<HtmlElement>) children).add(child);
    }

    public final void clearChildren() {
        if (children != null)
            children.clear();
    }

    public final HtmlElement getChild(int index) {
        if (children == null)
            return null;
        else {
            index = index >= 0 ? index : children.size() + index;
            if (index >= children.size() || index < 0)
                return null;
            return children.get(index);
        }
    }

    public final String htmlStartTag() {
        StringBuilder str = new StringBuilder();
        str.append('<').append(tagName);
        if (!attributes.isEmpty()) {
            for (String attr : attributes.keySet()) {
                str.append(" ").append(attr).append("=\"").append(attributes.get(attr)).append("\"");
            }
        }
        str.append('>');
        return str.toString();
    }

    public final String htmlEndTag() {
        return "</" + tagName + ">";
    }

    public String getXPath() {
        StringBuilder buffer = new StringBuilder();
        HtmlElement element = this;
        do {
            StringBuilder buf = new StringBuilder("/");
            buf.append(element.getTagName());
            if (element.getId() != null)
                buf.append('#').append(element.getId());
            if (element.getClassName() != null)
                buf.append('.').append(element.getClassName());
            buffer.insert(0, buf);
        } while ((element = element.parentElement) != null);
        return buffer.toString();
    }

    public void render(RenderContext context) {
        if (!context.handlerBeforeModifier(this)) {
            return;
        }
        int indent = getText() != null ? 0xf001 : 0xf1f1;
        context.printBeginTransaction(htmlStartTag(), indent);
        context.print(context.handlerTextModifier(this));
        if (children != null) {
            for (HtmlElement element : children) {
                element.render(context);
            }
        }
        context.printEndTransaction(htmlEndTag(), indent);
        context.handlerAfterModifier(this);
    }

    @Override
    public String toString() {
        return "<" + tagName + ">=" + text;
    }
}
