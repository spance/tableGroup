package wong.spance.html.span;

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
 * Created by spance on 14/3/29.
 */
public class RowSpan implements CellSpan {

    private SpanDefinition config;

    @Override
    public SpanDefinition getDefinition() {
        return config;
    }

    @Override
    public SpanOn by(int columnIndex) {
        return new RowSpanBy(columnIndex);
    }

    @Override
    public CellSpan on(int columnIndex) {
        config = new SpanDefinition('R', columnIndex, columnIndex);
        return this;
    }

    @Override
    public RowSpanCascade cascade(int columnIndex) {

        return new RowSpanCascade(columnIndex);
    }


    public class RowSpanBy implements SpanOn {
        private int byIndex;
        private Integer cascadeIndex;

        public RowSpanBy(int byIndex) {
            this.byIndex = byIndex;
        }

        public RowSpanBy(int byIndex, int cascadeIndex) {
            this.byIndex = byIndex;
            this.cascadeIndex = cascadeIndex;
        }

        @Override
        public CellSpan on(int columnIndex) {
            if (cascadeIndex == null)
                config = new SpanDefinition('R', byIndex, columnIndex);
            else
                config = new SpanDefinition('R', byIndex, columnIndex, cascadeIndex);
            return RowSpan.this;
        }
    }

    public class RowSpanCascade implements SpanBy {

        private int cascadeIndex;

        public RowSpanCascade(int cascadeIndex) {
            this.cascadeIndex = cascadeIndex;
        }

        @Override
        public SpanOn by(int columnIndex) {
            return new RowSpanBy(columnIndex, cascadeIndex);
        }
    }

    public static RowSpan newRule() {
        return new RowSpan();
    }
}
