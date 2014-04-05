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

import wong.spance.html.element.Cell;

import java.util.Arrays;

/**
 * Created by spance on 14/3/29.
 */
public class SpanRange {

    private int[] start;
    private int[] end;

    public SpanRange(int startX, int startY) {
        start = new int[]{startX, startY};
        end = start;
    }

    public int getRangeY() {
        return end[1] - start[1];
    }

    public boolean isStart(Cell cell) {
        return start[0] == cell.getX() && start[1] == cell.getY();
    }

    public boolean inRangeY(Cell cell) {
        int _dy1 = cell.getY() - start[1], _dy2 = end[1] - cell.getY();
        return _dy1 >= 0 && _dy2 >= 0;
    }

    public void setStart(int x, int y) {
        start[0] = x;
        start[1] = y;
    }

    public void setEnd(int x, int y) {
        end = new int[]{x, y};
    }

    @Override
    public String toString() {
        return "SpanRange{" +
                "start=" + Arrays.toString(start) +
                ", end=" + Arrays.toString(end) +
                '}';
    }
}
