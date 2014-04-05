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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static wong.spance.html.css.SyntaxError.SyntaxErrorType.*;

/**
 * E
 * E.class
 * E#id
 * E[foo]           <   has
 * E[foo="xxx"]     <   equals
 * E[foo~="xxx"]	<   like
 * E:first-child
 * E:last-child
 * E:nth-child(n)
 */
public class SelectorParser {

    private final String pattern;
    private final boolean reversed;
    private Token token;
    private Qualifier qualifier;

    public SelectorParser(String pattern) {
        this(pattern, false);
    }

    public SelectorParser(String pattern, boolean reversed) {
        this.pattern = pattern;
        this.reversed = reversed;
    }

    private List<Selector> lexicalAnalyzer() {
        List<Selector> selectors = new ArrayList<Selector>();
        Selector selector = Selector.newInstance();
        selectors.add(selector);
        token = Token.parse(pattern);
        do {
            switch (token.getValue()) {
                case '.':
                    checkLastQualifierValue(selector);
                    qualifier = new ClassNameQualifier();
                    qualifier.parse(token);
                    selector.addQualifier(qualifier);
                    break;
                case '#':
                    checkLastQualifierValue(selector);
                    qualifier = new IdQualifier();
                    qualifier.parse(token);
                    selector.addQualifier(qualifier);
                    break;
                case '[':
                    checkLastQualifierValue(selector);
                    qualifier = new AttributeQualifier();
                    qualifier.parse(token);
                    selector.addQualifier(qualifier);
                    break;
                case ':':
                    checkLastQualifierValue(selector);
                    qualifier = new PseudoClassQualifier();
                    qualifier.parse(token);
                    selector.addQualifier(qualifier);
                    break;
                default:
                    if (token.isIdentifier()) {
                        checkLastQualifierValue(selector);
                        Assert.assertTrue(SYMBOL_ERROR, selector.lastQualifier() == null);
                        qualifier = new TagNameQualifier();
                        qualifier.parse(token);
                        selector.addQualifier(qualifier);
                    } else if (token.isDelimiter()) {
                        Assert.assertTrue(INVALID_EXPRESSION_BEGIN, selector.lastQualifier() != null);
                        selector = Selector.newInstance(token, selector);
                        selectors.add(selector);
                    } else
                        Assert.failure(INVALID_SYMBOL);
            }
        } while (token.moveNext());

        return selectors;
    }

    private Selector syntaxAnalyzer(List<Selector> list) {
        if (reversed) {
            Collections.reverse(list);
        }
        Selector first = list.get(0);
        Selector last = first;
        for (int i = 0; i < list.size(); i++) {
            Selector selector = list.get(i);
            if (!reversed && last.getRelationship() == null && i + 1 < list.size())
                Assert.failure(UNSUPPORTED_SYNTAX, "暂不支持逗号声明的多表达式");
            if (reversed && selector.getRelationship() == null && i != 0)
                Assert.failure(UNSUPPORTED_SYNTAX, "暂不支持逗号声明的多表达式");
            if (i > 0) {
                // 反转时，同时转移关系
                if (reversed) {
                    last.setRelationship(selector.getRelationship());
                    if (i + 1 == list.size()) {
                        selector.setRelationship(null);
                    }
                }
                last.setNext(selector, reversed);
                last = selector;
            }

            for (Qualifier qualifier : last.getQualifiers()) {
                qualifier.syntaxAnalyzer();
            }
        }
        return first;
    }

    public Selector parse() throws SyntaxError {
        List<Selector> list;
        try {
            list = lexicalAnalyzer();
        } catch (Assert.AssertException e) {
            String cause = e.getMessage() == null ? e.type.name() : e.type + " , " + e.getMessage();
            if (e.type == EXPRESSION_MISSING && qualifier != null)
                cause = qualifier.getClass().getSimpleName().replace("Qualifier", "_") + cause;
            throw new SyntaxError(e, "Lexical-error : Symbol=%s cause %s", token, cause);
        }
        token = null;
        qualifier = null;
        Selector selector;
        try {
            selector = syntaxAnalyzer(list);
        } catch (Assert.AssertException e) {
            String cause = e.getMessage() == null ? e.type.name() : e.type + " -> " + e.getMessage();
            throw new SyntaxError(e, "Syntax-error : " + cause);
        }
        return selector;
    }

    private void checkLastQualifierValue(Selector selector) {
        if (selector != null && selector.lastQualifier() != null && selector.lastQualifier().getValue() == null)
            Assert.failure(EXPRESSION_MISSING);
    }
}
