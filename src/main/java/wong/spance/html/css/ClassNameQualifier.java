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

/**
 * Created by spance on 14/4/3.
 */
public class ClassNameQualifier extends Qualifier {

    public ClassNameQualifier() {
        setQualifierType(QualifierType.CLASS_NAME);
    }

    @Override
    public void syntaxAnalyzer() throws SyntaxError {
    }

    @Override
    public String toString() {
        return '.' + getValue();
    }

    @Override
    public HtmlElement matches(int selectorLevel, HtmlElement element) {
        String cls;
        if (selectorLevel == 0) {
            cls = element.getClassName();
            if (cls != null && cls.contains(getValue())) {
                return element;
            }
            return null;
        } else {
            HtmlElement parent = element;
            do {
                cls = parent.getClassName();
                if (cls != null && cls.contains(getValue())) {
                    return parent;
                }
            } while ((parent = parent.getParentElement()) != null);
            return null;
        }
    }
}
