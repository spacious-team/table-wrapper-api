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

class TableColumnImplTest {

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
        assertEquals(LEFTMOST_COLUMN, TableColumnImpl.of());
        //assertEquals(LEFTMOST_COLUMN, TableColumnImpl.of((String[]) null));
        assertEquals(LEFTMOST_COLUMN, TableColumnImpl.of(new String[]{null}));
        //assertEquals(LEFTMOST_COLUMN, TableColumnImpl.of(null, null));
    }

    @Test
    void getColumnIndex() {
        assertEquals(0, TableColumnImpl.of().getColumnIndex(row));
        assertEquals(9, TableColumnImpl.of("test").getColumnIndex(row));
        assertEquals(9, TableColumnImpl.of("WORD").getColumnIndex(row));
        assertEquals(10, TableColumnImpl.of("is\\s*sparta").getColumnIndex(row));
        assertEquals(20, TableColumnImpl.of("is the").getColumnIndex(row));
        assertEquals(20, TableColumnImpl.of("(old|Gr..t)").getColumnIndex(row));
        assertEquals(20,   TableColumnImpl.of("of").getColumnIndex(row));
        assertEquals(21, TableColumnImpl.of("mac").getColumnIndex(row));
        assertEquals(22, TableColumnImpl.of("windows").getColumnIndex(row));
        assertThrows(RuntimeException.class, () -> TableColumnImpl.of("not found").getColumnIndex(row));
    }

    @Test
    void testEquals() {
        TableColumn expected = TableColumnImpl.of("test");
        TableColumn notExpected = TableColumnImpl.of("abc");

        assertEquals(expected, TableColumnImpl.of("test"));
        assertNotEquals(notExpected, TableColumnImpl.of("test"));
    }

    @Test
    void testHashCode() {
        TableColumn expected = TableColumnImpl.of("test");
        TableColumn notExpected = TableColumnImpl.of("abc");

        assertEquals(expected.hashCode(), TableColumnImpl.of("test").hashCode());
        assertNotEquals(notExpected.hashCode(), TableColumnImpl.of("test").hashCode());
    }

    @Test
    void testToString() {
        assertEquals("TableColumnImpl(words=[test])", TableColumnImpl.of("test").toString());
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