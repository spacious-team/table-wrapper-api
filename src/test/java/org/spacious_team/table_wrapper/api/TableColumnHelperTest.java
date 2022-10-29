/*
 * Table Wrapper API
 * Copyright (C) 2022  Spacious Team <spacious-team@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spacious_team.table_wrapper.api;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TableColumnHelperTest {

    @SuppressWarnings("ConstantConditions")
    static ReportPageRow getRow() {
        ReportPageRow row = mock(ReportPageRow.class);
        Collection<TableCell> cells = Arrays.asList(
                null,
                cell(null, 1),
                cell(123, 2),
                cell(1.23, 3),
                cell(BigDecimal.valueOf(1), 4),
                cell("", 5),
                cell(" ", 6),
                cell("test word", 9),
                cell("This Is Sparta", 10),
                cell("London\nis the\ncapital\nof Great Britain", 20),
                cell("The Mac\rnew line", 21),
                cell("The Windows\r\nnew line", 22));
        when(row.iterator()).then($ -> cells.iterator());
        return row;
    }

    /**
     * Returns 3 rows
     * <pre>
     * Number: |  0   | 1  | 2  | 5  | 6  | 11 | 12 | 15 | 16 | 21 | 22 | 25 | 26 |
     *
     * Row 0:  | null |        One        |        Two        |        Three      |
     * Row 1:  | null |   a1    |   a2    |    a1   |   a2    |    a1   |   a2    |
     * Row 2:  | null | b1 | b2 | b1 | b2 | b1 | b2 | b1 | b2 | b1 | b2 | b1 | b2 |
     * <pre/>
     */
    static ReportPageRow[] getThreeRowsHeader() {
        ReportPageRow[] rows = new ReportPageRow[3];

        rows[0] = mock(ReportPageRow.class);
        when(rows[0].getRowNum()).thenReturn(0);
        Collection<TableCell> cells0 = Arrays.asList(
                null,
                cell("One", 1),
                cell("Two", 20),
                cell("Three", 30));
        when(rows[0].iterator()).then($ -> cells0.iterator());

        rows[1] = mock(ReportPageRow.class);
        when(rows[1].getRowNum()).thenReturn(1);
        Collection<TableCell> cells1 = Arrays.asList(
                null,
                cell("a1", 1),
                cell("a2", 5),
                cell("a1", 11),
                cell("a2", 15),
                cell("a1", 21),
                cell("a2", 25));
        when(rows[1].iterator()).then($ -> cells1.iterator());

        rows[2] = mock(ReportPageRow.class);
        when(rows[2].getRowNum()).thenReturn(2);
        Collection<TableCell> cells2 = Arrays.asList(
                null,
                cell("b1", 1),
                cell("b2", 2),
                cell("b1", 5),
                cell("b2", 6),
                cell("b1", 11),
                cell("b2", 12),
                cell("b1", 15),
                cell("b2", 16),
                cell("b1", 21),
                cell("b2", 22),
                cell("b1", 25),
                cell("b2", 26));
        when(rows[2].iterator()).then($ -> cells2.iterator());
        return rows;
    }

    private static TableCellTestImpl cell(Object value, int columnIndex) {
        return new TableCellTestImpl(value, columnIndex);
    }

    @Getter
    private static class TableCellTestImpl extends AbstractTableCell<Object> {
        private final Object value;
        private final int columnIndex;

        TableCellTestImpl(Object value, int columnIndex) {
            super(value, null);
            this.value = value;
            this.columnIndex = columnIndex;
        }
    }
}
