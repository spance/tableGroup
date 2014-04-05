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

/**
 * Created by spance on 14/3/31.
 */
public class SyntaxError extends RuntimeException {

    public SyntaxError() {
    }

    public SyntaxError(String message) {
        super(message);
    }

    public SyntaxError(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

    public SyntaxError(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxError(Throwable cause) {
        super(cause);
    }

    /**
     * Created by spance on 14/3/31.
     */
    public static enum SyntaxErrorType {
        INVALID_SYMBOL,
        EXPRESSION_MISSING,
        INVALID_EXPRESSION_BEGIN,
        SYMBOL_ERROR,
        SYNTAX_ERROR,
        EXPECT_ERROR,
        PAIR_MISSING,
        NULL_ERROR,
        NOT_EQUALS,
        UNSUPPORTED_SYNTAX,
        UNKNOWN_ERROR
    }
}
