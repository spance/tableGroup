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

import static wong.spance.html.css.SyntaxError.SyntaxErrorType;
import static wong.spance.html.css.SyntaxError.SyntaxErrorType.EXPRESSION_MISSING;
import static wong.spance.html.css.SyntaxError.SyntaxErrorType.INVALID_EXPRESSION_BEGIN;

/**
 * Created by spance on 14/3/31.
 */
public class Token {

    /*
        private final static Pattern SYMBOL_PATTERN = Pattern.compile("[._\\]\\[\\-~=#*'\"()]");
        private final static Pattern IDENTIFIER_PATTERN = Pattern.compile("[\\w\\d_-]");
    */
    private int position;
    private char[] context;
    private char _pair;
    private int _mark;

    public Token(int position, char[] context) {
        this.position = position;
        this.context = context;
        _mark = 0;
        _pair = 0;
    }

    public int getPosition() {
        return position;
    }

    public char getValue() {
        return context[position];
    }

    public boolean moveNext() {
        if (++position < context.length) {
            return true;
        }
        position--;
        return false;
    }

    public boolean hasNext() {
        return (position + 1) < context.length;
    }

    public String lookAhead(int step) {
        return new String(context, position, step);
    }

    // LETTER, A-Z[65-90], a-z[97-122]
    // DIGIT, 0-9[48-57]
    // LETTER_DIGIT,
    // IDENTIFIER,  _[95] -[45]
    // SYMBOL

    public boolean isDigit() {
        char code = getValue();
        return (code > 47 && code < 58);
    }

    public boolean isIdentifier() {
        char code = getValue();
        if ((code > 64 && code < 91) || (code > 96 && code < 123))
            return true;
        if (code > 47 && code < 58)
            return true;
        if (code == 45 || code == 95)
            return true;
        return false;
    }

    public boolean isSpace() {
        return Character.isSpaceChar(getValue());
    }

    public boolean isDelimiter() {
        return Character.isSpaceChar(getValue())
                || getValue() == '>'
                || getValue() == '+'
                || getValue() == ',';
    }

    public boolean isPairedChar() {
        return _pair == getValue();
    }

    public int pairBegin() {
        _pair = getValue();
        return position;
    }

    public int pairEnd() {
        _pair = 0;
        return position;
    }

    public boolean isPairing() {
        return _pair != 0;
    }

    public void markBegin() {
        _mark = position;
    }

    public boolean isMarking() {
        return _mark >= 0;
    }

    public String markEnd(boolean withCurrent) {
        int _markBegin = _mark;
        _mark = -1;
        int count = position - _markBegin;
        if (withCurrent)
            count++;
        return count > 0 ? new String(context, _markBegin, count) : null;
    }

    public boolean back() {
        if (position > 0) {
            position--;
            return true;
        }
        return false;
    }

    public Token skip() {
        return skip(null);
    }

    public Token skip(SyntaxErrorType type) {
        if (!moveNext()) {
            Assert.failure(type == null ? EXPRESSION_MISSING : type);
        }
        return this;
    }


    @Override
    public String toString() {
        return context[position] +
                " {position=" + position +
                '}';
    }

    public static Token parse(String pattern) {
        if (pattern != null) {
            char[] chars = pattern.trim().toCharArray();
            if (chars.length <= 0)
                Assert.failure(EXPRESSION_MISSING);
            return new Token(0, chars);
        }
        Assert.failure(INVALID_EXPRESSION_BEGIN);
        return null;
    }


}
