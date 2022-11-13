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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.spacious_team.table_wrapper.api.TableCellRange.EMPTY_RANGE;

@ExtendWith(MockitoExtension.class)
class TableFactoryHelperTest {

    @Mock
    ReportPage reportPage;
    @Mock
    TableCellRange range;
    @Mock
    TableCellAddress address;
    @Mock
    TableCell cell;
    Predicate<Object> tableNameFinder = cell -> true;

    @Test
    void getTableNameFromEmptyRange() {
        assertEquals("<not found>", TableFactoryHelper.getTableName(reportPage, tableNameFinder, EMPTY_RANGE));
        //noinspection ConstantConditions
        assertEquals("<not found>", TableFactoryHelper.getTableName(reportPage, tableNameFinder, null));
    }

    @Test
    void getTableNameWithEmptyCellAddress() {
        when(range.getFirstRow()).thenReturn(1);
        when(reportPage.find(1, 2, tableNameFinder)).thenReturn(TableCellAddress.NOT_FOUND);

        assertEquals("<not found>", TableFactoryHelper.getTableName(reportPage, tableNameFinder, range));
    }

    @Test
    void getTableNameWithNullCellAddress() {
        when(range.getFirstRow()).thenReturn(1);
        //noinspection ConstantConditions
        when(reportPage.find(1, 2, tableNameFinder)).thenReturn(null);

        assertEquals("<not found>", TableFactoryHelper.getTableName(reportPage, tableNameFinder, range));
    }

    @Test
    void getTableNameWithNullCell() {
        when(range.getFirstRow()).thenReturn(1);
        when(reportPage.find(1, 2, tableNameFinder)).thenReturn(address);
        //noinspection ConstantConditions
        when(reportPage.getCell(address)).thenReturn(null);

        assertEquals("<not found>", TableFactoryHelper.getTableName(reportPage, tableNameFinder, range));
    }

    @Test
    void getTableName() {
        String expected = "test";
        when(range.getFirstRow()).thenReturn(1);
        when(reportPage.find(1, 2, tableNameFinder)).thenReturn(address);
        //noinspection ConstantConditions
        when(reportPage.getCell(address)).thenReturn(cell);
        when(cell.getStringValue()).thenReturn(expected);

        assertEquals(expected, TableFactoryHelper.getTableName(reportPage, tableNameFinder, range));
    }
}