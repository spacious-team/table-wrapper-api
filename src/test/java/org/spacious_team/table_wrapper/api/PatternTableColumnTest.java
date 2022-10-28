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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.spacious_team.table_wrapper.api.TableColumn.LEFTMOST_COLUMN;

class PatternTableColumnTest {

    @Mock
    CellDataAccessObject<Object, ?> dao;
    ReportPageRow row;

    @BeforeEach
    @SuppressWarnings("ConstantConditions")
    void setUp() {
        row = mock(ReportPageRow.class);
        Collection<TableCell> cells = List.of(
                new TableCellTestImpl(null, 1),
                new TableCellTestImpl(123, 2),
                new TableCellTestImpl(1.23, 3),
                new TableCellTestImpl(BigDecimal.valueOf(1), 4),
                new TableCellTestImpl("", 5),
                new TableCellTestImpl( " ", 6),
                new TableCellTestImpl("test word", 9),
                new TableCellTestImpl("This Is Sparta", 10),
                new TableCellTestImpl("London\nis the\ncapital\nof Great Britain", 20),
                new TableCellTestImpl("The Mac\rnew line", 21),
                new TableCellTestImpl("The Windows\r\nnew line", 22));
        when(row.iterator()).then($ -> cells.iterator());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void testZeroArg() {
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of());
        //assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of((String[]) null));
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(new String[]{null}));
        //assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(null, null));
    }

    @Test
    void getColumnIndex() {
        assertEquals(0, PatternTableColumn.of().getColumnIndex(row));
        assertEquals(9, PatternTableColumn.of("test").getColumnIndex(row));
        assertEquals(9, PatternTableColumn.of("WORD").getColumnIndex(row));
        assertEquals(10, PatternTableColumn.of("is\\s*sparta").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("is the").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("(old|Gr..t)").getColumnIndex(row));
        assertEquals(20,   PatternTableColumn.of("of").getColumnIndex(row));
        assertEquals(21, PatternTableColumn.of("mac").getColumnIndex(row));
        assertEquals(22, PatternTableColumn.of("windows").getColumnIndex(row));
        assertThrows(RuntimeException.class, () -> PatternTableColumn.of("not found").getColumnIndex(row));
    }

    @Test
    void testEquals() {
        TableColumn expected = PatternTableColumn.of("test");
        TableColumn notExpected = PatternTableColumn.of("abc");

        assertEquals(expected, PatternTableColumn.of("test"));
        assertNotEquals(notExpected, PatternTableColumn.of("test"));
    }

    @Test
    void testHashCode() {
        TableColumn expected = PatternTableColumn.of("test");
        TableColumn notExpected = PatternTableColumn.of("abc");

        assertEquals(expected.hashCode(), PatternTableColumn.of("test").hashCode());
        assertNotEquals(notExpected.hashCode(), PatternTableColumn.of("test").hashCode());
    }

    @Test
    void testToString() {
        assertEquals("PatternTableColumn(words=[test])", PatternTableColumn.of("test").toString());
    }

    @Getter
    class TableCellTestImpl extends AbstractTableCell<Object> {
        private final Object value;
        private final int columnIndex;

        TableCellTestImpl(Object value, int columnIndex) {
            super(value, dao);
            this.value = value;
            this.columnIndex = columnIndex;
        }
    }
}