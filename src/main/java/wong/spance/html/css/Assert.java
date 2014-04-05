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
import static wong.spance.html.css.SyntaxError.SyntaxErrorType.NOT_EQUALS;
import static wong.spance.html.css.SyntaxError.SyntaxErrorType.UNKNOWN_ERROR;

/**
 * Created by spance on 14/3/31.
 */
public final class Assert {


    public static void notNull(SyntaxErrorType type, Object obj) {
        notNull(type, null, obj);
    }

    public static void notNull(SyntaxErrorType type, String msg, Object obj) {
        if (obj == null)
            throw new AssertException(type, msg);
    }

    public static void assertTrue(SyntaxErrorType type, boolean condition) {
        assertTrue(type, null, condition);
    }

    public static void assertTrue(SyntaxErrorType type, String msg, boolean condition) {
        if (!condition)
            throw new AssertException(type, msg);
    }

    public static void equals(Object s1, Object s2) {
        equals(null, s1, s2);
    }

    public static void equals(SyntaxErrorType type, Object s1, Object s2) {
        if (s1 == null || s2 == null || !s1.equals(s2))
            throw new AssertException(type != null ? type : NOT_EQUALS);
    }

    public static void failure(SyntaxErrorType type) {
        throw new AssertException(type);
    }

    public static void failure(SyntaxErrorType type, String message) {
        throw new AssertException(type, message);
    }

    public static class AssertException extends RuntimeException {

        public final SyntaxErrorType type;

        public AssertException(SyntaxErrorType type) {
            this(type, null);
        }

        public AssertException(SyntaxErrorType type, String message) {
            super(message);
            this.type = type == null ? UNKNOWN_ERROR : type;
        }
    }
}
