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

import static wong.spance.html.css.SyntaxError.SyntaxErrorType.SYNTAX_ERROR;

/**
 * Created by spance on 14/4/3.
 */
public class TagNameQualifier extends Qualifier {

    public TagNameQualifier() {
        setQualifierType(QualifierType.TAG_NAME);
    }

    @Override
    public void parse(Token token) {
        token.markBegin();
        while (token.moveNext()) {
            if (!token.isIdentifier()) {
                token.back();
                break;
            }
        }
        setValue(token.markEnd(true));
    }

    @Override
    public void syntaxAnalyzer() throws SyntaxError {
        Assert.assertTrue(SYNTAX_ERROR, getValue(), getValue().matches("\\w+"));
    }

    @Override
    public HtmlElement matches(int selectorLevel, HtmlElement element) {
        if (selectorLevel == 0) {
            if (element.getTagName().equals(getValue()))
                return element;
            return null;
        } else {
            HtmlElement parent = element;
            do {
                if (parent.getTagName().equals(getValue()))
                    return parent;
            } while ((parent = parent.getParentElement()) != null);
            return null;
        }
    }

    @Override
    public String toString() {
        return getValue();
    }
}
