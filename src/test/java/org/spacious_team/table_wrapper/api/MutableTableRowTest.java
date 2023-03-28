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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static nl.jqno.equalsverifier.Warning.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutableTableRowTest {

    @Mock
    Table table;
    @Mock
    CellDataAccessObject<?, ReportPageRow> dao;
    @Mock
    TableHeaderColumn headerColumn;
    @Mock
    TableColumn column;
    final int COLUMN_INDEX = 10;
    @Mock
    ReportPageRow wrappedRow;
    MutableTableRow<?, ReportPageRow> row;

    @BeforeEach
    void setUp() {
        row = spy(new MutableTableRow<>(table, dao));
        row.setRow(wrappedRow);
        lenient().when(headerColumn.getColumn()).thenReturn(column);
        lenient().when(table.getHeaderDescription()).thenReturn(Map.of(column, COLUMN_INDEX));
    }

    @Test
    void getCellByTableHeaderColumn() {
        row.getCell(headerColumn);
        verify(row).getCell(COLUMN_INDEX);
    }

    @Test
    void getCellByIndex_sameDao() {
        row.getCell(COLUMN_INDEX);
        verify(wrappedRow).getCell(COLUMN_INDEX);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getCellByIndex_differentDao() {
        CellDataAccessObject<Object, ?> dao = mock(CellDataAccessObject.class);
        AbstractTableCell<Object> expectedCell = mock(AbstractTableCell.class);
        AbstractTableCell<Object> cell = new AbstractTableCell<>(new Object(), dao) {
            @Override
            protected AbstractTableCell<Object> createWithCellDataAccessObject(CellDataAccessObject<Object, ?> dao) {
                return expectedCell;
            }

            @Override
            public int getColumnIndex() {
                return 0;
            }
        };
        //noinspection ConstantConditions
        when(wrappedRow.getCell(COLUMN_INDEX)).thenReturn(cell);

        @Nullable TableCell actualCell = row.getCell(COLUMN_INDEX);

        assertSame(expectedCell, actualCell);
        verify(wrappedRow).getCell(COLUMN_INDEX);
    }

    @Test
    void getRowNum() {
        row.getRowNum();
        verify(wrappedRow).getRowNum();
    }

    @Test
    void getFirstCellNum() {
        row.getFirstCellNum();
        verify(wrappedRow).getFirstCellNum();
    }

    @Test
    void getLastCellNum() {
        row.getLastCellNum();
        verify(wrappedRow).getLastCellNum();
    }

    @Test
    void rowContains() {
        Object object = new Object();
        row.rowContains(object);
        verify(wrappedRow).rowContains(object);
    }

    @Test
    void iterator() {
        row.iterator();
        verify(wrappedRow).iterator();
    }

    @Test
    @SuppressWarnings("unchecked")
    void iterator_differentDao() {
        CellDataAccessObject<Object, ?> dao = mock(CellDataAccessObject.class);
        AbstractTableCell<Object> expectedCell = mock(AbstractTableCell.class);
        TableCell cell = new AbstractTableCell<>(new Object(), dao) {
            @Override
            protected AbstractTableCell<Object> createWithCellDataAccessObject(CellDataAccessObject<Object, ?> dao) {
                return expectedCell;
            }

            @Override
            public int getColumnIndex() {
                return 0;
            }
        };
        Iterator<@Nullable TableCell> iterator = List.of(cell).iterator();
        when(wrappedRow.iterator()).thenReturn(iterator);

        Iterator<@Nullable TableCell> actualIterator = row.iterator();
        //noinspection ConstantConditions
        TableCell actualCell = actualIterator.next();

        assertSame(expectedCell, actualCell);
        verify(wrappedRow).iterator();
    }

    @Test
    void getCellValue() {
        row.getCellValue(headerColumn);
        verify(dao).getValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void getIntCellValue() {
        row.getIntCellValue(headerColumn);
        verify(dao).getIntValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void getLongCellValue() {
        row.getLongCellValue(headerColumn);
        verify(dao).getLongValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void getDoubleCellValue() {
        row.getDoubleCellValue(headerColumn);
        verify(dao).getDoubleValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void getBigDecimalCellValue() {
        row.getBigDecimalCellValue(headerColumn);
        verify(dao).getBigDecimalValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void getStringCellValue() {
        row.getStringCellValue(headerColumn);
        verify(dao).getStringValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void getInstantCellValue() {
        row.getInstantCellValue(headerColumn);
        verify(dao).getInstantValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void getLocalDateTimeCellValue() {
        row.getLocalDateTimeCellValue(headerColumn);
        verify(dao).getLocalDateTimeValue(wrappedRow, COLUMN_INDEX);
    }

    @Test
    void testClone() {
        MutableTableRow<?, ReportPageRow> row = new MutableTableRow<>(table, dao);
        row.setRow(wrappedRow);
        assertEquals(row, row.clone());
    }

    @Test
    void getTable() {
        assertSame(table, row.getTable());
    }

    @Test
    void getDao() {
        assertSame(dao, row.getDao());
    }

    @Test
    void getRow() {
        assertSame(wrappedRow, row.getRow());
    }

    @Test
    void testEquals() {
        EqualsVerifier
                .forClass(MutableTableRow.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .suppress(NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        assertEquals("MutableTableRow(table=table, dao=dao, row=wrappedRow)", row.toString());
    }
}