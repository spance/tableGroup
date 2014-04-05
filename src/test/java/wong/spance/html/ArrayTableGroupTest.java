package wong.spance.html;

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


import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import wong.spance.html.element.TableMeta;
import wong.spance.html.render.DefaultModifiers;
import wong.spance.html.span.RowSpan;
import wong.spance.html.stat.Statistics;
import wong.spance.html.store.SimpleDataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by spance on 14/3/30.
 */

public class ArrayTableGroupTest {


    TableGroup builder;

    @Test
    public void testOn() throws Exception {
        builder = new ArrayTableGroup(TableMeta.newMeta()
                .addColumn("111").addColumn("222").addColumn("333"));
        Object[][] arr = new Object[][]{
                {"a1", "a2", "a3"},
                {"a1", "b2", "b3"},
                {"c1", "c2", "c3"}
        };
        builder.apply(new SimpleDataStore(arr));
        assertEquals(builder.getTable().getBody().length, arr.length);

        builder.group(RowSpan.newRule().on(0));

        String html = builder.render();
        System.out.println(html);
        assertTrue(html.contains("rowspan"));
    }

    @Test
    public void testByOn() throws Exception {
        builder = new ArrayTableGroup(TableMeta.newMeta()
                .addColumn("111").addColumn("222").addColumn("333"));
        Object[][] arr = new Object[][]{
                {"a", "b2", "c3"},
                {"a", "b2", "c3"},
                {"a", "b2", "c3"}
        };
        builder.apply(new SimpleDataStore(arr));
        assertEquals(builder.getTable().getBody().length, arr.length);

        builder.group(RowSpan.newRule().by(0).on(1));

        String html = builder.render();
        System.out.println(html);

        assertArrayEquals("3".split(" "),
                StringUtils.substringsBetween(html, "rowspan=\"", "\""));

    }

    @Test
    public void testByOn2() throws Exception {
        builder = new ArrayTableGroup(TableMeta.newMeta()
                .addColumn("111").addColumn("222").addColumn("333"));
        Object[][] arr = new Object[][]{
                {"a2", "b2", "c3"},
                {"a2", "b2", "c3"},
                {"a1", "b2", "c3"}
        };
        builder.apply(new SimpleDataStore(arr));
        assertEquals(builder.getTable().getBody().length, arr.length);

        builder.group(RowSpan.newRule().on(0),
                RowSpan.newRule().by(2).on(2));

        String html = builder.render();
        System.out.println(html);
        assertArrayEquals("2 3".split(" "),
                StringUtils.substringsBetween(html, "rowspan=\"", "\""));
    }

    @Test
    public void testByOn3() throws Exception {
        builder = new ArrayTableGroup(TableMeta.newMeta()
                .addColumn("111").addColumn("222").addColumn("333"));
        Object[][] arr = new Object[][]{
                {"a3", "b2", "c3"},
                {"a3", "b ", "c3"},
                {"a3", "b2", "c3"},
                {"a1", "b2", "c33"}
        };
        builder.apply(new SimpleDataStore(arr));
        assertEquals(builder.getTable().getBody().length, arr.length);

        builder.group(
                RowSpan.newRule().on(0),
                RowSpan.newRule().on(1),
                RowSpan.newRule().on(2));

        String html = builder.render();
        System.out.println(html);

        assertArrayEquals("3 3 2".split(" "),
                StringUtils.substringsBetween(html, "rowspan=\"", "\""));

    }

    @Test
    public void testCascade4() throws Exception {
        builder = new ArrayTableGroup(TableMeta.newMeta()
                .addColumn("111").addColumn("222").addColumn("333"));
        Object[][] arr = new Object[][]{
                {"a4", "b2", "c3"},
                {"a4", "b2", "c3"},
                {"a1", "b2", "c3"}
        };
        builder.apply(new SimpleDataStore(arr));
        assertEquals(builder.getTable().getBody().length, arr.length);

        builder.group(RowSpan.newRule().on(0),
                RowSpan.newRule().cascade(0).by(2).on(2));

        String html = builder.render();
        System.out.println(html);

        assertArrayEquals("2 2".split(" "),
                StringUtils.substringsBetween(html, "rowspan=\"", "\""));

    }

    @Test
    public void testStatistics() throws Exception {
        builder = new ArrayTableGroup(TableMeta.newMeta()
                .addColumn("111").addColumn("222").addColumn("333"));
        Object[][] arr = new Object[][]{
                {"s0", "b2", "2"},
                {"s0", "b2", "3"},
                {"s1", "b2", "4"},
                {"s1", "b2", "5"},
                {"s1", "b2", "5"}
        };
        builder.apply(new SimpleDataStore(arr));
        assertEquals(builder.getTable().getBody().length, arr.length);

        builder.group(RowSpan.newRule().on(0));

        Map<String, Number> map = builder.statistics(Statistics.groupBy(0).sum(2));
        assertEquals(5, map.get("s0").intValue());
        assertEquals(14, map.get("s1").intValue());

        map = builder.statistics(Statistics.groupBy(0).avg(2));
        assertEquals(2.5, map.get("s0").doubleValue(), 0);
        assertEquals(4.66, map.get("s1").doubleValue(), 0.01);
    }

    @Test
    public void testCss1() throws Exception {
        builder = new ArrayTableGroup(TableMeta.newMeta()
                .addColumn("111").addColumn("222").addColumn("333"));
        Object[][] arr = new Object[][]{
                {"s0", "b2", "2"},
                {"s0", "b2", "3"},
                {"s1", "b2", "4"},
                {"s1", "b ", "5"},
                {"s1", "bB 22--", "5"}
        };
        builder.apply(new SimpleDataStore(arr));
        assertEquals(builder.getTable().getBody().length, arr.length);

        builder.group(
                RowSpan.newRule().on(0),
                RowSpan.newRule().on(1)
        );
        String html = builder.render(new DefaultModifiers()
                        .setAttribute("table", "class", "table-class")
                        .setAttribute("table th", "class", "head")
                        .setAttribute("tbody tr", "style", "kkkk")
                        .replace("tr:last-child td:nth-child(1)", "(\\d+)", "XXX$1ttt")
        );
        System.out.println(html);

        assertArrayEquals("2 3 3".split(" "),
                StringUtils.substringsBetween(html, "rowspan=\"", "\""));


        assertEquals(1, StringUtils.countMatches(html, "class=\"table-class\""));
        assertEquals(3, StringUtils.countMatches(html, "class=\"head\""));
        assertEquals(5, StringUtils.countMatches(html, "style=\"kkk"));
        assertEquals(1, StringUtils.countMatches(html, "XXX22ttt"));
    }

}
