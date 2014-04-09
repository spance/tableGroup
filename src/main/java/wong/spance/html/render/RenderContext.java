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

import wong.spance.html.element.HtmlElement;
import wong.spance.html.element.TableMeta;

import java.util.*;

/**
 * 注意：这不是线程安全的实现
 */
public class RenderContext {

    private TableMeta meta;
    private String Br = System.getProperty("line.separator", "\n");
    private StringBuilder buffer;
    private Deque<PrintTransaction> transactions;
    private Map<ModifierPoint, List<Modifier>> modifierMap;


    public RenderContext(TableMeta meta) {
        this.meta = meta;
        buffer = new StringBuilder();
        transactions = new LinkedList<PrintTransaction>();
        modifierMap = new HashMap<ModifierPoint, List<Modifier>>();
    }


    public TableMeta getMeta() {
        return meta;
    }


    public String out() {
        return buffer.toString();
    }

    /**
     * 初始化modifiers，两个类同的方法
     *
     * @param modifiers
     */
    public void initModifiers(Modifier[] modifiers) {
        for (Modifier modifier : modifiers) {
            ModifierPoint key = modifier.getModifierPoint();
            List<Modifier> modifierList = modifierMap.get(key);
            if (modifierList == null) {
                modifierList = new ArrayList<Modifier>();
                modifierMap.put(key, modifierList);
            }
            modifierList.add(modifier);
        }
    }

    public void initModifiers(ModifierProvider provider) {
        modifierMap = provider.getModifiers();
    }

    /**
     * 查找位于前置调节点上的调节器
     * 遍历调节器，验证是否css选择器的目标，并对其执行前置调节。
     * 若某前置调节器返回false则停止对该对象的渲染。
     *
     * @param element
     * @return
     */
    public boolean handlerBeforeModifier(HtmlElement element) {
        List<Modifier> modifierList = modifierMap.get(ModifierPoint.BEFORE);
        if (modifierList != null && !modifierList.isEmpty()) {
            for (Modifier _modifier : modifierList) {
                BeforeRenderModifier modifier = (BeforeRenderModifier) _modifier;
                if (modifier.matches(element)) {
                    if (!modifier.handler(element, this))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * 查找位于后置调节点上的调节器
     * 遍历调节器，验证是否css选择器的目标，并对其执行后置调节得到附加值，输出到缓冲区
     *
     * @param element
     * @return
     */
    public void handlerAfterModifier(HtmlElement element) {
        List<Modifier> modifierList = modifierMap.get(ModifierPoint.AFTER);
        if (modifierList != null && !modifierList.isEmpty()) {
            for (Modifier _modifier : modifierList) {
                AfterRenderModifier modifier = (AfterRenderModifier) _modifier;
                if (modifier.matches(element)) {
                    String result = modifier.handler(element, this);
                    if (result != null) {
                        print(result);
                    }
                }
            }
        }
    }

    public void handlerBeforeEndModifier(HtmlElement element) {
        List<Modifier> modifierList = modifierMap.get(ModifierPoint.BEFORE_END);
        if (modifierList != null && !modifierList.isEmpty()) {
            for (Modifier _modifier : modifierList) {
                BeforeEndRenderModifier modifier = (BeforeEndRenderModifier) _modifier;
                if (modifier.matches(element)) {
                    String result = modifier.handler(element, this);
                    if (result != null) {
                        print(result);
                    }
                }
            }
        }
    }

    /**
     * 查找位于文本内容调节点上的调节器
     * 遍历调节器，验证是否css选择器的目标，并对其执行文本内容调节得到文本值，输出到缓冲区
     *
     * @param element
     * @return
     */
    public String handlerTextModifier(HtmlElement element) {
        List<Modifier> modifierList = modifierMap.get(ModifierPoint.TEXT);
        if (modifierList != null && !modifierList.isEmpty()) {
            for (Modifier _modifier : modifierList) {
                TextRenderModifier modifier = (TextRenderModifier) _modifier;
                if (modifier.matches(element)) {
                    String result = modifier.handler(element, this);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return element.getText();
    }


    public void print(String value) {
        if (value != null && value.length() > 0) {
            if (transactions.isEmpty()) {
                buffer.append(value);
            } else {
                transactions.peek().printing(value);
            }
        }
    }

    public void printf(String format, Object... value) {
        print(String.format(format, value));
    }

    /**
     * 启动一个打印事务
     * 一个打印事务，分为3段（头、体、尾），若无体输出，则事务失败，不输出或者备用消息
     *
     * @param value
     * @param indent
     */
    public void printBeginTransaction(CharSequence value, int indent) {
        if (indent > 0)
            value = autoIndent(transactions.size(), (indent & 0xff00) >>> 8, value);
        PrintTransaction transaction = new PrintTransaction();
        transactions.push(transaction);
        transaction.printBegin(value);
    }

    /**
     * 结束一个打印事务，把当前事务的内容输出到父级事务中，或主缓冲区中
     *
     * @param value
     * @param indent
     */
    public void printEndTransaction(CharSequence value, int indent) {
        if (transactions.isEmpty())
            throw new IllegalStateException("printBegin need first call!");
        PrintTransaction transaction = transactions.pop();
        if (indent > 0)
            value = autoIndent(transactions.size(), indent & 0x00ff, value);
        if (transactions.peek() != null) {
            transactions.peek().printing(transaction.printEnd(value));
        } else
            buffer.append(transaction.printEnd(value));
    }


    /**
     * 为了人类更容易看清楚，按嵌套事务数量进行缩进
     * 缩进标志位：
     * 缩S换...换E换 0xf111
     * 缩S...E换 0xf001
     *
     * @param size  8-bit int 无符号
     * @param value
     * @return
     */
    CharSequence autoIndent(int size, int indent, CharSequence value) {
        StringBuilder buf = new StringBuilder();
        if ((indent >>> 4) == 0xf)
            while (size-- > 0) {
                buf.append("  ");
            }
        else if ((indent >>> 4) == 1)
            buf.append(Br);
        if (value != null)
            buf.append(value);
        if ((indent & 0xf) == 1)
            buf.append(Br);
        return buf;
    }

    /**
     * 打印事务
     */
    class PrintTransaction {

        private StringBuilder _buffer = new StringBuilder();
        private boolean valid = false;

        void printBegin(CharSequence value) {
            _buffer.append(value);
            valid = false;
        }

        void printing(CharSequence value) {
            _buffer.append(value);
            valid = true;
        }

        /**
         * 事务失败后，选择性输出emptyText
         *
         * @param value
         * @return
         */
        CharSequence printEnd(CharSequence value) {
            if (valid) {
                _buffer.append(value);
                return _buffer;
            } else {
                return "";
            }
        }
    }
}
