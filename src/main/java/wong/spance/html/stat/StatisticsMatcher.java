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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spance on 14/3/30.
 */
public class StatisticsMatcher {

    private final Statistics.Rule rule;
    private final Cell group;
    private final List<Cell> targets;

    public StatisticsMatcher(Statistics.Rule rule, Cell group) {
        this.rule = rule;
        this.group = group;
        targets = new ArrayList<Cell>();
    }

    public void add(Cell target) {
        targets.add(target);
    }

    public Statistics.Rule getRule() {
        return rule;
    }

    public Cell getGroup() {
        return group;
    }

    public List<Cell> getTargets() {
        return targets;
    }
}
