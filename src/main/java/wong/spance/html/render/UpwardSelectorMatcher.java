package wong.spance.html.render;

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

import wong.spance.html.css.Qualifier;
import wong.spance.html.css.Selector;
import wong.spance.html.css.SelectorRelationship;
import wong.spance.html.element.HtmlElement;

import java.util.List;

/**
 * Created by spance on 14/4/4.
 */
public class UpwardSelectorMatcher implements SelectorMatcher {

    private final Selector selector;

    public UpwardSelectorMatcher(Selector selector) {
        this.selector = selector;
    }

    @Override
    public boolean matches(HtmlElement element) {
        Selector _selector = selector;
        int selectorRelativeToElementRank = 0;
        int lastMatchedRank = -1;
        do {
            List<Qualifier> qualifiers = _selector.getQualifiers();
            SelectorRelationship relationship = _selector.getRelationship();
            for (int i = 0, qualifiersSize = qualifiers.size(); i < qualifiersSize; i++) {
                Qualifier qualifier = qualifiers.get(i);
                // 在一个selector范围内，多个qualifier应该基于同一element
                // 即，第二个以后的qualifier认为相对级别为0
                HtmlElement previous = qualifier.matches(i != 0 ? 0 : selectorRelativeToElementRank, element);
                if (previous != null) {
                    if (relationship != null) {
                        switch (relationship) {
                            case PROGENY:
                                break;
                            case CHILDREN:
                                if (selectorRelativeToElementRank - lastMatchedRank > 1)
                                    return false;
                                break;
                            case NEXT_NEIGHBOUR:
                                throw new UnsupportedOperationException("暂不支持邻选择器");
                        }
                    }
                    element = previous;
                    lastMatchedRank = selectorRelativeToElementRank;
                } else {
                    return false;
                }
            }
            selectorRelativeToElementRank++;
        } while ((_selector = _selector.getNext()) != null);
        // 匹配完所有规则，未发现不满足
        return true;
    }

}
