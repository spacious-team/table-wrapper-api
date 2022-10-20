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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@Getter
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
        assertNull(getRow().getCell(col));
    }

    @Test
    void testGetCell() {
        assertNull(getRow().getCell(getColumn()));
    }

    @Test
    void getFirstCellNum() {
        assertEquals(-1, getRow().getFirstCellNum());
    }

    @Test
    void getLastCellNum() {
        assertEquals(-1, getRow().getLastCellNum());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "abc"})
    void rowContains(String value) {
        assertFalse(getRow().rowContains(value));
    }

    @Test
    void iterator() {
        assertFalse(getRow().iterator().hasNext());
    }

    @Test
    void getCellValue() {
        assertNull(getRow().getCellValue(getColumn()));
    }

    @Test
    void getIntCellValue() {
        assertThrows(NullPointerException.class,
                () -> getRow().getIntCellValue(getColumn()));
    }

    @Test
    void getLongCellValue() {
        assertThrows(NullPointerException.class,
                () -> getRow().getLongCellValue(getColumn()));
    }

    @Test
    void getDoubleCellValue() {
        assertThrows(NullPointerException.class,
                () -> getRow().getDoubleCellValue(getColumn()));
    }

    @Test
    void getBigDecimalCellValue() {
        assertThrows(NullPointerException.class,
                () -> getRow().getBigDecimalCellValue(getColumn()));
    }

    @Test
    void getStringCellValue() {
        assertThrows(NullPointerException.class,
                () -> getRow().getStringCellValue(getColumn()));
    }

    @Test
    void getInstantCellValue() {
        assertThrows(NullPointerException.class,
                () -> getRow().getInstantCellValue(getColumn()));
    }

    @Test
    void getLocalDateTimeCellValue() {
        assertThrows(NullPointerException.class,
                () -> getRow().getLocalDateTimeCellValue(getColumn()));
    }

    @Test
    void testClone() {
        assertEquals(getRow(), getRow().clone());
    }

    @Test
    void testGetTable() {
        assertEquals(getTable(), getRow().getTable());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1000_000})
    void getRowNum(int rowNum) {
        assertEquals(rowNum, new EmptyTableRow(table, rowNum).getRowNum());
    }

    @Test
    void testEquals() {
        assertEquals(new EmptyTableRow(getTable(), 0), getRow());
        assertNotEquals(new EmptyTableRow(getTable(), 1), getRow());
    }

    @Test
    void testHashCode() {
        assertEquals(new EmptyTableRow(getTable(), 0).hashCode(), getRow().hashCode());
        assertNotEquals(new EmptyTableRow(getTable(), 1).hashCode(), getRow().hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(getRow().toString());
    }
}