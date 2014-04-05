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

import static wong.spance.html.css.SyntaxError.SyntaxErrorType.EXPRESSION_MISSING;

/**
 * Created by spance on 14/3/31.
 */
public abstract class Qualifier {

    private String value;
    private QualifierType qualifierType;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        if (value == null)
            Assert.failure(EXPRESSION_MISSING);
    }

    public QualifierType getQualifierType() {
        return qualifierType;
    }

    protected void setQualifierType(QualifierType qualifierType) {
        this.qualifierType = qualifierType;
    }

    /**
     * 进行词法分析
     * 此处仅提供首位跳过的基本实现
     *
     * @param token
     */
    protected void parse(Token token) {
        token.skip().markBegin();
        if (!token.isIdentifier()) {
            Assert.failure(EXPRESSION_MISSING);
        }
        while (token.moveNext()) {
            if (!token.isIdentifier()) {
                token.back();
                break;
            }
        }
        setValue(token.markEnd(true));
    }

    @Override
    public abstract String toString();

    public abstract HtmlElement matches(int selectorLevel, HtmlElement element);

    public abstract void syntaxAnalyzer() throws SyntaxError;

    public static enum QualifierType {
        TAG_NAME, CLASS_NAME, ID, ATTRIBUTE, PSEUDO_CLASS
    }

}
