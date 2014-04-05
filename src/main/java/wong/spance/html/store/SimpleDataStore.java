package wong.spance.html.store;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by spance on 14/3/30.
 */
public class SimpleDataStore extends AbstractDataStore {
    private List<Object[]> data;

    public SimpleDataStore(Collection<Object[]> data) {
        this.data = new ArrayList<Object[]>(data);
    }

    public SimpleDataStore(Object[][] data) {
        this.data = Arrays.asList(data);
    }

    @Override
    public Object[] getRow(int index) {
        return data.get(index);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return data.size() > 0 ? data.get(0).length : 0;
    }


}
