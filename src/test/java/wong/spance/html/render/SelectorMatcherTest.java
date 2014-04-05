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

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;
import wong.spance.html.css.Selector;
import wong.spance.html.css.SelectorParser;
import wong.spance.html.element.HtmlElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by spance on 14/4/5.
 */
public class SelectorMatcherTest {

    private HtmlElement root;
    private List<String> patterns = new LinkedList<String>();
    private List<HtmlElement> elements = new LinkedList<HtmlElement>();

    @Before
    public void setUp() throws Exception {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = this.getClass().getResourceAsStream("/selector-matcher-case.css");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                patterns.add(line.trim());
            }
        } finally {
            try {
                if (reader != null) reader.close();
                if (in != null) in.close();
            } catch (IOException ignored) {
            }
        }
        try {
            in = this.getClass().getResourceAsStream("/selector-matcher-case.xml");
            Document doc = new SAXReader().read(in);
            Element docRootElement = doc.getRootElement();
            root = buildHtmlElement(docRootElement);
            recursiveChildren(root, docRootElement.elements());
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {
            }
        }

    }

    void recursiveChildren(HtmlElement parent, List<Element> list) {
        for (Element item : list) {
            HtmlElement the = buildHtmlElement(item);
            List children = item.elements();
            if (children != null && !children.isEmpty()) {
                recursiveChildren(the, children);
            }
            parent.addChild(the);
            elements.add(parent);
        }
    }

    HtmlElement buildHtmlElement(Element element) {
        HTMLElement ele = new HTMLElement(element.getName());
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            ele.setAttribute(attribute.getName(), attribute.getValue());
        }
        return ele;
    }

    static class HTMLElement extends HtmlElement {

        protected HTMLElement(String tagName) {
            super(tagName);
        }
    }

    ///////////////////////////////////////////////////////

    private int count = 0;
    private int error = 0;
    private int miss = 0;
    private int matched = 0;


    @Test
    public void testMatches() {
        long t1 = System.currentTimeMillis();
        StringBuilder buf = new StringBuilder();
        for (HtmlElement htmlElement : elements) {
            for (String pattern : patterns) {
                try {
                    Selector selector = new SelectorParser(pattern, true).parse();
                    SelectorMatcher matcher = new UpwardSelectorMatcher(selector);
                    boolean bool = matcher.matches(htmlElement);
                    if (bool) {
                        matched++;
                        buf.append("match [ ").append(pattern).append(" ]   ").append(htmlElement.getXPath()).append('\n');
                    } else {
                        miss++;
                    }
                } catch (Exception e) {
                    error++;
                    buf.append(e.getMessage()).append('\n');
                }
            }
            count += patterns.size();
        }
        double t2 = (System.currentTimeMillis() - t1) / 1000;
        System.out.println("---------------------------");
        System.out.println("---------------------------");
        System.out.print(buf.toString());
        System.out.println("---------------------------");
        System.out.println("---------------------------");
        System.out.printf("total loop count: %d , matched: %d , miss: %d , error: %d%n",
                count, matched, miss, error);
        System.out.printf("used: %.2fs , speed: %.2f/s , avg match: %.5fms/per",
                t2, count / t2, t2 * 1000 / count);
    }
}
