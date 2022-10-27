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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class AbstractTableCellTest {

    @Mock
    CellDataAccessObject<Object, ?> dao;
    @Mock
    Object cellValue;
    TableCellImpl cell;

    @BeforeEach
    void setUp() {
        cell = new TableCellImpl(cellValue, dao);
    }

    @Test
    void getValue() {
        cell.getValue();
        Mockito.verify(dao).getValue(cellValue);
    }

    @Test
    void getIntValue() {
        cell.getIntValue();
        Mockito.verify(dao).getIntValue(cellValue);
    }

    @Test
    void getLongValue() {
        cell.getLongValue();
        Mockito.verify(dao).getLongValue(cellValue);
    }

    @Test
    void getDoubleValue() {
        cell.getDoubleValue();
        Mockito.verify(dao).getDoubleValue(cellValue);
    }

    @Test
    void getBigDecimalValue() {
        cell.getBigDecimalValue();
        Mockito.verify(dao).getBigDecimalValue(cellValue);
    }

    @Test
    void getStringValue() {
        cell.getStringValue();
        Mockito.verify(dao).getStringValue(cellValue);
    }

    @Test
    void getInstantValue() {
        cell.getInstantValue();
        Mockito.verify(dao).getInstantValue(cellValue);
    }

    @Test
    void getLocalDateTimeValue() {
        cell.getLocalDateTimeValue();
        Mockito.verify(dao).getLocalDateTimeValue(cellValue);
    }

    @Test
    void getCell() {
        assertEquals(cellValue, cell.getCell());
    }

    @Test
    void getDao() {
        assertEquals(dao, cell.getDao());
    }

    @Test
    void testEquals() {
        TableCellImpl expected = new TableCellImpl(cellValue, dao);
        TableCellImpl notExpected = new TableCellImpl(new Object(), dao);

        assertEquals(expected, cell);
        assertNotEquals(notExpected, cell);
    }

    @Test
    void testHashCode() {
        TableCellImpl expected = new TableCellImpl(cellValue, dao);
        TableCellImpl notExpected = new TableCellImpl(new Object(), dao);

        assertEquals(expected.hashCode(), cell.hashCode());
        assertNotEquals(notExpected.hashCode(), cell.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("AbstractTableCell(cell=cellValue, dao=dao)", cell.toString());
    }

    static class TableCellImpl extends AbstractTableCell<Object> {

        protected TableCellImpl(Object cell, CellDataAccessObject<Object, ?> dao) {
            super(cell, dao);
        }

        @Override
        public int getColumnIndex() {
            throw new UnsupportedOperationException();
        }
    }
}