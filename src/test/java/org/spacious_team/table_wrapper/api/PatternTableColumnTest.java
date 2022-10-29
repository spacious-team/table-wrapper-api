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
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static nl.jqno.equalsverifier.Warning.ALL_FIELDS_SHOULD_BE_USED;
import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.spacious_team.table_wrapper.api.TableColumn.LEFTMOST_COLUMN;

class PatternTableColumnTest {

    @SuppressWarnings("ConstantConditions")
    ReportPageRow getRow() {
        ReportPageRow row = mock(ReportPageRow.class);
        Collection<TableCell> cells = Arrays.asList(
                null,
                new TableCellTestImpl(null, 1),
                new TableCellTestImpl(123, 2),
                new TableCellTestImpl(1.23, 3),
                new TableCellTestImpl(BigDecimal.valueOf(1), 4),
                new TableCellTestImpl("", 5),
                new TableCellTestImpl(" ", 6),
                new TableCellTestImpl("test word", 9),
                new TableCellTestImpl("This Is Sparta", 10),
                new TableCellTestImpl("London\nis the\ncapital\nof Great Britain", 20),
                new TableCellTestImpl("The Mac\rnew line", 21),
                new TableCellTestImpl("The Windows\r\nnew line", 22));
        when(row.iterator()).then($ -> cells.iterator());
        return row;
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void testZeroArg() {
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of());
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(""));
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of((String[]) null));
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(new String[]{null}));
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(null, null));
    }

    @Test
    void getColumnIndex() {
        ReportPageRow row = getRow();
        assertEquals(0, PatternTableColumn.of().getColumnIndex(row));
        assertEquals(9, PatternTableColumn.of("test").getColumnIndex(row));
        assertEquals(9, PatternTableColumn.of("WORD").getColumnIndex(row));
        assertEquals(10, PatternTableColumn.of("this is\\s*sparta").getColumnIndex(row));
        assertEquals(10, PatternTableColumn.of("this is").getColumnIndex(row));
        assertEquals(10, PatternTableColumn.of("is sparta").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("is the").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("is", "the").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("is", null, "the").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("(old|Gr..t)").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("of").getColumnIndex(row));
        assertEquals(21, PatternTableColumn.of("mac").getColumnIndex(row));
        assertEquals(22, PatternTableColumn.of("windows").getColumnIndex(row));
        assertEquals(22, PatternTableColumn.of("windows").getColumnIndex(21, row));

        TableColumn column1 = PatternTableColumn.of("windows");
        assertThrows(RuntimeException.class, () -> column1.getColumnIndex(23, row));
        TableColumn column2 = PatternTableColumn.of("not found");
        assertThrows(RuntimeException.class, () -> column2.getColumnIndex(row));
        TableColumn column3 = PatternTableColumn.of("london is");
        assertThrows(RuntimeException.class, () -> column3.getColumnIndex(row));
        TableColumn column4 = PatternTableColumn.of("mac new");
        assertThrows(RuntimeException.class, () -> column4.getColumnIndex(row));
        TableColumn column5 = PatternTableColumn.of("windows new");
        assertThrows(RuntimeException.class, () -> column5.getColumnIndex(row));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(PatternTableColumn.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .suppress(ALL_FIELDS_SHOULD_BE_USED)
                .verify();
    }

    @Test
    void testToString() {
        assertEquals("PatternTableColumn(words=[test])", PatternTableColumn.of("test").toString());
    }

    @Getter
    static class TableCellTestImpl extends AbstractTableCell<Object> {
        private final Object value;
        private final int columnIndex;

        TableCellTestImpl(Object value, int columnIndex) {
            super(value, null);
            this.value = value;
            this.columnIndex = columnIndex;
        }
    }
}