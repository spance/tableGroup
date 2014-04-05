package wong.spance.html.stat;

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

import wong.spance.html.element.Cell;
import wong.spance.html.element.Table;
import wong.spance.html.span.SpanRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spance on 14/3/30.
 */
public class StatisticsContext {

    private final Table table;
    private final Map<Integer, Statistics.Rule> statMap;
    private final List<StatisticsMatcher> matchesList;

    public StatisticsContext(Table table) {
        this.table = table;
        statMap = new HashMap<Integer, Statistics.Rule>();
        matchesList = new ArrayList<StatisticsMatcher>();
    }

    public void initContext(Statistics.Rule... rules) {
        for (Statistics.Rule rule : rules) {
            statMap.put(rule.getGroupBy(), rule);
        }
    }

    public boolean hasRule(int i) {
        return statMap.containsKey(i);
    }

    public void doStat(int i, Cell cell) {
        Statistics.Rule rule = statMap.get(i);
        Cell pointer = cell;
        while (pointer != null) {
            StatisticsMatcher matcher = new StatisticsMatcher(rule, pointer);
            matchesList.add(matcher);
            SpanRange range = pointer.getSpanRange();
            int dis = range.getRangeY();
            if (dis > 0) {
                while (dis-- >= 0) {
                    matcher.add(table.getCell(rule.getTarget(), pointer.getY()));
                    pointer = pointer.down();
                }
            } else {
                matcher.add(table.getCell(rule.getTarget(), pointer.getY()));
                pointer = pointer.down();
            }
        }
    }

    public Map<String, Number> calculate(){
        Map<String, Number> result = new HashMap<String, Number>();
        for (StatisticsMatcher matcher : matchesList) {
            int type = matcher.getRule().getType();
            try {
                switch (type) {
                    case Statistics.COUNT:
                        result.put(matcher.getGroup().getText(), matcher.getTargets().size());
                        break;
                    case Statistics.SUM:
                    case Statistics.AVG:
                        BigDecimal sum = new BigDecimal(0);
                        for (Cell cell : matcher.getTargets()) {
                            sum = sum.add(new BigDecimal(cell.getText()));
                        }
                        if (type == Statistics.AVG) {
                            sum = sum.divide(new BigDecimal(matcher.getTargets().size()), 2,
                                    RoundingMode.HALF_UP);
                        }
                        result.put(matcher.getGroup().getText(), sum);
                        break;
                    default:
                        throw new IllegalStateException();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
