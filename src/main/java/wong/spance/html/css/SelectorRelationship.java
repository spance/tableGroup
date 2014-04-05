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

import static wong.spance.html.css.SyntaxError.SyntaxErrorType.INVALID_SYMBOL;

/**
 * Created by spance on 14/4/3.
 */
public enum SelectorRelationship {

    PROGENY(' '), CHILDREN('>'), NEXT_NEIGHBOUR('+');

    private char value;

    SelectorRelationship(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static SelectorRelationship get(String delimiter) {
        if (delimiter == null || delimiter.length() < 1)
            return PROGENY;
        if (delimiter.length() > 1)
            Assert.failure(INVALID_SYMBOL);
        else {
            char token = delimiter.charAt(0);
            switch (token) {
                case '+':
                    return NEXT_NEIGHBOUR;
                case '>':
                    return CHILDREN;
                case ',':
                    return null;
                default:
                    if (Character.isSpaceChar(token)) {
                        return PROGENY;
                    }
                    Assert.failure(SyntaxError.SyntaxErrorType.SYMBOL_ERROR);
            }
        }
        return null;
    }
}
