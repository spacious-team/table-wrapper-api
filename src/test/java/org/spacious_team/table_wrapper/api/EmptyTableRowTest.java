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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("nullness") // TODO exclude test sources entirely
@ExtendWith(MockitoExtension.class)
class EmptyTableRowTest {

    @Mock
    Table table;
    @Mock
    TableColumnDescription column;
    EmptyTableRow row;

    @BeforeEach
    void beforeEach() {
        row = new EmptyTableRow(table, 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1000_000})
    void getCell(int col) {
        assertNull(row.getCell(col));
    }

    @Test
    void testGetCell() {
        assertNull(row.getCell(column));
    }

    @Test
    void getFirstCellNum() {
        assertEquals(-1, row.getFirstCellNum());
    }

    @Test
    void getLastCellNum() {
        assertEquals(-1, row.getLastCellNum());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "abc"})
    void rowContains(String value) {
        assertFalse(row.rowContains(value));
    }

    @Test
    void iterator() {
        assertFalse(row.iterator().hasNext());
    }

    @Test
    void getCellValue() {
        assertNull(row.getCellValue(column));
    }

    @Test
    void getIntCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getIntCellValue(column));
    }

    @Test
    void getLongCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getLongCellValue(column));
    }

    @Test
    void getDoubleCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getDoubleCellValue(column));
    }

    @Test
    void getBigDecimalCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getBigDecimalCellValue(column));
    }

    @Test
    void getStringCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getStringCellValue(column));
    }

    @Test
    void getInstantCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getInstantCellValue(column));
    }

    @Test
    void getLocalDateTimeCellValue() {
        assertThrows(NullPointerException.class,
                () -> row.getLocalDateTimeCellValue(column));
    }

    @Test
    void testClone() {
        assertEquals(row, row.clone());
    }

    @Test
    void testGetTable() {
        assertEquals(table, row.getTable());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1000_000})
    void getRowNum(int rowNum) {
        assertEquals(rowNum, new EmptyTableRow(table, rowNum).getRowNum());
    }

    @Test
    void testEquals() {
        assertEquals(new EmptyTableRow(table, 0), row);
        assertNotEquals(new EmptyTableRow(table, 1), row);
    }

    @Test
    void testHashCode() {
        assertEquals(new EmptyTableRow(table, 0).hashCode(), row.hashCode());
        assertNotEquals(new EmptyTableRow(table, 1).hashCode(), row.hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(row.toString());
    }
}