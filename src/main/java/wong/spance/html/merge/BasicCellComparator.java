package wong.spance.html.merge;

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

/**
 * Created by spance on 14/3/30.
 */
public class BasicCellComparator implements CellComparator {

    @Override
    public boolean compare(Cell cellBy, Cell cellByNext) {
        String valueBy = cellBy.getText();
        String nextValue = cellByNext.getText();
        return !(valueBy == null || nextValue == null) && valueBy.trim().equals(nextValue.trim());
    }
}
