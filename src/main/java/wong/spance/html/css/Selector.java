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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static wong.spance.html.css.SyntaxError.SyntaxErrorType.INVALID_SYMBOL;

/**
 * Created by spance on 14/3/31.
 */
public final class Selector {

    private final LinkedList<Qualifier> qualifiers;
    private SelectorRelationship relationship;
    private Selector next;
    private boolean reversed;

    private Selector() {
        this.qualifiers = new LinkedList<Qualifier>();
    }

    public SelectorRelationship getRelationship() {
        return relationship;
    }

    protected void setRelationship(SelectorRelationship relationship) {
        this.relationship = relationship;
    }

    public Selector getNext() {
        return next;
    }

    protected void setNext(Selector next, boolean reversed) {
        this.next = next;
        this.reversed = reversed;
    }

    public Qualifier lastQualifier() {
        return qualifiers.peek();
    }

    protected void addQualifier(Qualifier qualifier) {
        qualifiers.push(qualifier);
    }

    public List<Qualifier> getQualifiers() {
        return qualifiers;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Iterator<Qualifier> it = qualifiers.descendingIterator();
        while (it.hasNext()) {
            str.append(it.next());
        }
        if (relationship != null) {
            if (relationship == SelectorRelationship.PROGENY)
                str.append(relationship);
            else
                str.append(' ').append(relationship).append(' ');
        }
        if (next != null)
            str.append(next);
        return str.toString();
    }

    public static Selector newInstance() {
        return newInstance(null, null);
    }

    public static Selector newInstance(Token token, Selector last) {
        Selector me = new Selector();
        if (token == null || last == null)
            return me;
        Assert.assertTrue(INVALID_SYMBOL, token.isDelimiter());
        token.markBegin();
        while (true) {
            if (!token.moveNext()) {
                Assert.failure(INVALID_SYMBOL);
                return null;
            }
            if (!token.isDelimiter()) {
                last.relationship = SelectorRelationship.get(token.markEnd(false).trim());
                token.back();
                return me;
            }
        }
    }

}
