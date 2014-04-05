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

/**
 * Created by spance on 14/3/30.
 */
public class Statistics {

    private int groupBy;

    public final static int COUNT = 1;
    public final static int SUM = 2;
    public final static int AVG = 3;

    private Statistics(int groupBy) {
        this.groupBy = groupBy;
    }

    public Rule count(int columnIndex) {
        return new Rule(groupBy, COUNT, columnIndex);
    }

    public Rule sum(int columnIndex) {
        return new Rule(groupBy, SUM, columnIndex);
    }

    public Rule avg(int columnIndex) {
        return new Rule(groupBy, AVG, columnIndex);
    }

    public static Statistics groupBy(int columnIndex) {
        return new Statistics(columnIndex);
    }

    public static class Rule {
        private int groupBy;
        private int type;
        private int target;

        private Rule(int groupBy, int type, int target) {
            this.groupBy = groupBy;
            this.type = type;
            this.target = target;
        }

        public int getGroupBy() {
            return groupBy;
        }

        public int getType() {
            return type;
        }

        public int getTarget() {
            return target;
        }
    }
}
