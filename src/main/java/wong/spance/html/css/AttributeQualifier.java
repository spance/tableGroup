package wong.spance.html.css;

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

import static wong.spance.html.css.SyntaxError.SyntaxErrorType.*;

/**
 * Created by spance on 14/4/3.
 */
public class AttributeQualifier extends Qualifier {

    private String key;
    private String operation;

    public AttributeQualifier() {
        setQualifierType(QualifierType.ATTRIBUTE);
    }

    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public void parse(Token token) {
        token.skip().markBegin();
        boolean valueBegin = false;
        do {
            if (token.isPairing() && !token.isPairedChar()) {
                continue;
            }
            switch (token.getValue()) {
                case '~':
                    key = token.markEnd(false);
                    Assert.equals(SYMBOL_ERROR, token.lookAhead(2), "~=");
                    operation = "~=";
                    token.moveNext();
                    valueBegin = true;
                    break;
                case '=':
                    key = token.markEnd(false);
                    operation = "=";
                    valueBegin = true;
                    break;
                case ']':
                    if (key == null)
                        key = token.markEnd(false);
                    else if (getValue() == null) {
                        Assert.assertTrue(EXPRESSION_MISSING, token.isMarking());
                        setValue(token.markEnd(false));
                    }
                    return;
                case '\'':
                case '"':
                    if (token.isPairing()) {
                        setValue(token.markEnd(false));
                        token.pairEnd();
                    } else {
                        if (!token.isMarking()) {
                            token.pairBegin();
                            token.skip().markBegin();
                            token.back();
                        } else
                            Assert.failure(SYMBOL_ERROR);
                    }

                    break;
                default:
                    if (!token.isIdentifier()) {
                        Assert.failure(SYMBOL_ERROR);
                    } else {
                        if (valueBegin && !token.isMarking()) {
                            token.markBegin();
                        }
                    }
            }
        } while (token.moveNext());
        if (token.isPairing())
            Assert.failure(PAIR_MISSING);
    }

    @Override
    public void syntaxAnalyzer() throws SyntaxError {
        Assert.assertTrue(SYNTAX_ERROR, key != null);
        Assert.assertTrue(SYNTAX_ERROR, key, key.matches("\\w+"));
        if (operation != null) {
            Assert.assertTrue(UNSUPPORTED_SYNTAX, operation, operation.matches("(~=|=)"));
        } else
            Assert.assertTrue(SYNTAX_ERROR, getValue() == null);
    }

    @Override
    public HtmlElement matches(int selectorLevel, HtmlElement element) {
        if (selectorLevel == 0) {
            String attr = key.equals("text") ? element.getText() : element.getAttribute(key);
            if (attr == null)
                return null;
            if (getValue() != null) {
                if (getOperation().startsWith("~") && !attr.contains(getValue())) {
                    return null;
                } else if (!getValue().equals(attr)) {
                    return null;
                }
            }
            return element;
        } else {
            HtmlElement parent = element;
            do {
                String attr = key.equals("text") ? parent.getText() : parent.getAttribute(key);
                if (attr == null)
                    continue;
                if (getValue() != null) {
                    if (getOperation().startsWith("~")) {
                        if (attr.contains(getValue())) {
                            return parent;
                        }
                    } else if (getValue().equals(attr)) {
                        return parent;
                    }
                }
                return parent;
            } while ((parent = parent.getParentElement()) != null);
            return null;
        }
    }

    @Override
    public String toString() {
        return '[' + key
                + (operation == null ? "" : operation)
                + (getValue() == null ? "" : getValue())
                + ']';
    }
}
