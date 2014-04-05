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


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(Parameterized.class)
public class SelectorParserTest {

    /*
    @DataProvider(name = "array")
    public Object[][] data() {
        return data;
    }
    */
    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        InputStream in = null;
        try {
            in = SelectorParserTest.class.getResourceAsStream("/selector-test-case.xml");
            List<Object[]> cases = new ArrayList<Object[]>();
            Document doc = new SAXReader().read(in);
            Element root = doc.getRootElement();
            List<Element> list = root.elements("case");
            for (Element _case : list) {
                cases.add(new Object[]{
                        _case.attributeValue("pattern"),
                        _case.attributeValue("error"),
                });
            }
            return cases;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    private String pattern;
    private String error;

    public SelectorParserTest(String pattern, String error) {
        this.pattern = pattern;
        if (!"null".equals(error))
            this.error = error;
    }

    //   @Test(dataProvider = "array", singleThreaded = true, testName = "parser")
    //    public void testParse(String pattern, String error) throws Exception {
    @Test
    public void testParse() throws Exception {
        SelectorParser session = new SelectorParser(pattern);
        try {
            Selector list = session.parse();
            if (error != null) {
                fail("必须的异常却木有 : " + error + " , Result: " + list);
            }
            System.out.printf("%-30s    %s    %s%n", pattern, "OK", list);
            String expected = replaceBlankWithoutQuotation(pattern, " ");
            expected = expected.replaceAll("(?<=\\S)(>|\\+|,)", " $1");
            expected = expected.replaceAll("(>|\\+|,)(?=\\S)", "$1 ");
            assertEquals(
                    expected,
                    list.toString()
            );
        } catch (Exception e) {
            if (e instanceof SyntaxError && error != null) {
                System.out.printf("%-30s    %s    %s%n", pattern, "Ex", e.getMessage());
                assertTrue(e.getMessage().contains(error));
            } else {
                System.out.println(pattern + "\t\t-----奇葩出现-----");
                throw e;
            }
        }
    }

    /**
     * 扫描字符串中的连续空白，但忽略不处理位于单双引号中的值
     * 把连续空白替换为[replaceTo] 并剔除引号串的边界引号
     *
     * @return
     */
    static String replaceBlankWithoutQuotation(String from, String replaceTo) {
        char[] arr = from.toCharArray();
        int i = 0, qm = 0, cur;
        for (; i < arr.length; i++) {
            cur = arr[i];
            if (cur == '"' || cur == '\'') {
                if (qm == 0 || (qm ^ cur) == 0)
                    arr[i] = 2;
                qm ^= cur;
                if (qm == ('"' ^ '\''))
                    qm ^= cur;
            } else if (Character.isSpaceChar(cur) && qm == 0) {
                arr[i] = 1;
            }
        }
        StringBuilder tmp = new StringBuilder(arr.length);
        char last = 0;
        for (char ch : arr) {
            switch (ch) {
                case 1:
                    if (ch == last)
                        continue;
                    tmp.append(replaceTo);
                    break;
                case 2:
                    break;
                default:
                    tmp.append(ch);
            }
            last = ch;
        }
        return tmp.toString();
    }

}
