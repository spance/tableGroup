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
public class PseudoClassQualifier extends Qualifier {

    private Integer nthValue;

    public Integer getNthValue() {
        return nthValue;
    }

    public PseudoClassQualifier() {
        setQualifierType(QualifierType.PSEUDO_CLASS);
    }

    @Override
    public void parse(Token token) {
        token.skip().markBegin();
        if (!token.isIdentifier()) {
            Assert.failure(EXPRESSION_MISSING);
        }
        int beginPairing = 0, endPairing = 0;
        _outer:
        while (token.moveNext()) {
            switch (token.getValue()) {
                case '(':
                    if (token.isPairing()) {
                        Assert.failure(SYMBOL_ERROR);
                    }
                    beginPairing = token.pairBegin();
                    setValue(token.markEnd(false));
                    break;
                case ')':
                    if (token.isPairing()) {
                        endPairing = token.pairEnd();
                        if (endPairing - beginPairing == 1) {
                            Assert.failure(EXPRESSION_MISSING);
                        }
                        return;
                    }
                    Assert.failure(SYMBOL_ERROR);
                    break;
                default:
                    if (token.isPairing()) {
                        if (!token.isDigit()) {
                            Assert.failure(INVALID_SYMBOL);
                        } else {
                            nthValue = (nthValue == null ? 0 : nthValue * 10) + token.getValue() - 48;
                        }
                    }
                    if (!token.isIdentifier()) {
                        token.back();
                        break _outer;
                    }
            }
        }
        if (token.isPairing()) {
            Assert.failure(PAIR_MISSING);
        }
        setValue(token.markEnd(true));
    }

    @Override
    public void syntaxAnalyzer() throws SyntaxError {
        Assert.assertTrue(UNSUPPORTED_SYNTAX, getValue(), getValue().matches("(first-child|last-child|nth-child)"));
        if (getValue().startsWith("nth")) {
            Assert.assertTrue(EXPRESSION_MISSING, "nth-child(n) required n", getNthValue() != null);
        }
        if (getValue().startsWith("first")) {
            nthValue = 0;
        } else if (getValue().startsWith("last")) {
            nthValue = -1;
        }
    }

    @Override
    public HtmlElement matches(int selectorLevel, HtmlElement element) {
        if (selectorLevel == 0) {
            if (element.getParentElement() == null)
                return null;
            if (element.getParentElement().getChild(nthValue).equals(element))
                return element;
            return null;
        } else {
            HtmlElement parent = element;
            do {
                if (parent.getParentElement() == null)
                    return null;
                if (parent.getParentElement().getChild(nthValue).equals(parent))
                    return parent;
            } while ((parent = parent.getParentElement()) != null);
            return null;
        }
    }

    @Override
    public String toString() {
        if (nthValue == null || nthValue <= 0)
            return ':' + getValue();
        else
            return ':' + getValue() + '(' + nthValue + ')';
    }
}
