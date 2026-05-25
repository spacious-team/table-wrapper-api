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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.spacious_team.table_wrapper.api.TableCellRange.EMPTY_RANGE;

@ExtendWith(MockitoExtension.class)
class TableFactoryTest {

    @Spy
    TableFactory tableFactory;
    @Mock
    ReportPage reportPage;
    @Mock
    TableCellRange tableCellRange;
    @Mock
    TableCellRange tableCellRange2;
    @Mock
    TableCellRange tableCellRangeRemoveTop1;
    String tableName = "table name";
    String dataRow = "data row";
    String lastRowString = "footer";
    Predicate<Object> tableNameFinder = cell -> true;
    Predicate<Object> dataRowFinder = cell -> true;
    Predicate<Object> lastRowFinder = cell -> true;
    String firstRowString = "first row";
    String providedTableName = "provided table name";
    Predicate<Object> firstRowFinder = cell -> true;
    Class<ReportPageTest.TableHeader> headerDescription = ReportPageTest.TableHeader.class;


    @Test
    void create_dataPrefix_dataRowFound() {
        when(reportPage.getCellRange(tableName, dataRow, 0, 1)).thenReturn(tableCellRange);
        when(tableCellRange.getFirstRow()).thenReturn(10);
        when(tableCellRange.getLastRow()).thenReturn(20);
        when(reportPage.getCellRange(tableName, lastRowString, 10, 9)).thenReturn(tableCellRange2);
        when(tableCellRange2.addRowsToTop(-1)).thenReturn(tableCellRangeRemoveTop1);

        tableFactory.create(reportPage, tableName, 1, dataRow, lastRowString, headerDescription);

        verify(reportPage).getCellRange(tableName, dataRow, 0, 1);
        verify(reportPage).getCellRange(tableName, lastRowString, 10, 9);
        verify(tableCellRange2).addRowsToTop(-1);
        verify(tableFactory).create(
                reportPage,
                tableName,
                tableCellRangeRemoveTop1,
                headerDescription,
                9);
    }

    @Test
    void create_dataPrefix_dataRowNotFound() {
        when(reportPage.getCellRange(tableName, dataRow, 0, 1)).thenReturn(EMPTY_RANGE);

        tableFactory.create(reportPage, tableName, 1, dataRow, lastRowString, headerDescription);

        verify(reportPage).getCellRange(tableName, dataRow, 0, 1);
        verify(reportPage, never()).getCellRange(eq(tableName), eq(lastRowString), anyInt(), anyInt());
        verify(tableFactory).create(
                reportPage,
                tableName,
                EMPTY_RANGE,
                headerDescription,
                0);
    }

    @Test
    void create_tableNameAndLastRowPrefix() {
        when(reportPage.getCellRange(tableName, lastRowString, 0, 2)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(-1)).thenReturn(tableCellRangeRemoveTop1);

        tableFactory.create(reportPage, tableName, 1, lastRowString, headerDescription, 2);

        verify(reportPage).getCellRange(tableName, lastRowString, 0, 2);
        verify(tableCellRange).addRowsToTop(-1);
        verify(tableFactory).create(
                reportPage,
                tableName,
                tableCellRangeRemoveTop1,
                headerDescription,
                2);
    }

    @Test
    void create_tableNamePrefix() {
        when(reportPage.getCellRange(tableName, null, 0, 2)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(-1)).thenReturn(tableCellRangeRemoveTop1);

        tableFactory.create(reportPage, tableName, 1, null, headerDescription,  2);

        verify(reportPage).getCellRange(tableName, null, 0, 2);
        verify(tableCellRange).addRowsToTop(-1);
        verify(tableFactory).create(
                reportPage,
                tableName,
                tableCellRangeRemoveTop1,
                headerDescription,
                2);
    }

    @Test
    void create_dataRowFinder_dataRowFound() {
        when(reportPage.getCellRange(tableNameFinder, dataRowFinder, 0, 1)).thenReturn(tableCellRange);
        when(tableCellRange.getFirstRow()).thenReturn(10);
        when(tableCellRange.getLastRow()).thenReturn(20);
        when(reportPage.getCellRange(tableNameFinder, lastRowFinder, 10, 9)).thenReturn(tableCellRange2);
        when(tableCellRange2.addRowsToTop(-1)).thenReturn(tableCellRangeRemoveTop1);

        tableFactory.create(reportPage, tableNameFinder, 1, dataRowFinder, lastRowFinder, headerDescription);

        verify(reportPage).getCellRange(tableNameFinder, dataRowFinder, 0, 1);
        verify(reportPage).getCellRange(tableNameFinder, lastRowFinder, 10, 9);
        verify(tableCellRange2).addRowsToTop(-1);
        verify(tableFactory).create(
                eq(reportPage),
                any(String.class),
                eq(tableCellRangeRemoveTop1),
                eq(headerDescription),
                eq(9));
    }

    @Test
    void create_dataRowFinder_dataRowNotFound() {
        when(reportPage.getCellRange(tableNameFinder, dataRowFinder, 0, 1)).thenReturn(EMPTY_RANGE);

        tableFactory.create(reportPage, tableNameFinder, 1, dataRowFinder, lastRowFinder, headerDescription);

        verify(reportPage).getCellRange(tableNameFinder, dataRowFinder, 0, 1);
        verify(reportPage, never()).getCellRange(eq(tableNameFinder), eq(lastRowFinder), anyInt(), anyInt());
        verify(tableFactory).create(
                eq(reportPage),
                any(String.class),
                eq(EMPTY_RANGE),
                eq(headerDescription),
                eq(0));
    }

    @Test
    void create_tableNameAndLastRowFinder() {
        when(reportPage.getCellRange(tableNameFinder, lastRowFinder, 0, 2)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(-1)).thenReturn(tableCellRangeRemoveTop1);

        tableFactory.create(reportPage, tableNameFinder, 1, lastRowFinder, headerDescription, 2);

        verify(reportPage).getCellRange(tableNameFinder, lastRowFinder, 0, 2);
        verify(tableCellRange).addRowsToTop(-1);
        verify(tableFactory).create(
                eq(reportPage),
                any(String.class),
                eq(tableCellRangeRemoveTop1),
                eq(headerDescription),
                eq(2));
    }

    @Test
    void create_tableNameFinder() {
        when(reportPage.getCellRange(tableNameFinder, null, 0, 2)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(-1)).thenReturn(tableCellRangeRemoveTop1);

        tableFactory.create(reportPage, tableNameFinder, 1, null, headerDescription, 2);

        verify(reportPage).getCellRange(tableNameFinder, null, 0, 2);
        verify(tableCellRange).addRowsToTop(-1);
        verify(tableFactory).create(
                eq(reportPage),
                any(String.class),
                eq(tableCellRangeRemoveTop1),
                eq(headerDescription),
                eq(2));
    }

    @Test
    void createNameless_dataPrefix_dataRowFound() {
        when(reportPage.getCellRange(firstRowString, dataRow, 0, 0)).thenReturn(tableCellRange);
        when(tableCellRange.getFirstRow()).thenReturn(10);
        when(tableCellRange.getLastRow()).thenReturn(30);
        when(reportPage.getCellRange(firstRowString, lastRowString, 10, 20)).thenReturn(tableCellRange2);

        tableFactory.createNameless(reportPage, providedTableName, firstRowString, dataRow, lastRowString, headerDescription);

        verify(reportPage).getCellRange(firstRowString, dataRow, 0, 0);
        verify(reportPage).getCellRange(firstRowString, lastRowString, 10, 20);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRange2,
                headerDescription,
                20);
    }

    @Test
    void createNameless_dataPrefix_dataRowNotFound() {
        when(reportPage.getCellRange(firstRowString, dataRow, 0, 0)).thenReturn(EMPTY_RANGE);

        tableFactory.createNameless(reportPage, providedTableName, firstRowString, dataRow, lastRowString, headerDescription);

        verify(reportPage).getCellRange(firstRowString, dataRow, 0, 0);
        verify(reportPage, never()).getCellRange(eq(firstRowString), eq(lastRowString), anyInt(), anyInt());
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                EMPTY_RANGE,
                headerDescription,
                0);
    }

    @Test
    void createNameless_headerRowAndLastRowPrefix() {
        when(reportPage.getCellRange(firstRowString, lastRowString, 0, 1)).thenReturn(tableCellRange);

        tableFactory.createNameless(reportPage, providedTableName, firstRowString, lastRowString,
                headerDescription, 2);

        verify(reportPage).getCellRange(firstRowString, lastRowString, 0, 1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRange,
                headerDescription,
                2);
    }

    @Test
    void createNameless_headerRowPrefix() {
        when(reportPage.getCellRange(firstRowString, null, 0, 1)).thenReturn(tableCellRange);

        tableFactory.createNameless(reportPage, providedTableName, firstRowString, null, headerDescription, 2);

        verify(reportPage).getCellRange(firstRowString, null, 0, 1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRange,
                headerDescription,
                2);
    }

    @Test
    void createNameless_dataFinder_dataRowFound() {
        when(reportPage.getCellRange(firstRowFinder, dataRowFinder, 0, 0)).thenReturn(tableCellRange);
        when(tableCellRange.getFirstRow()).thenReturn(10);
        when(tableCellRange.getLastRow()).thenReturn(30);
        when(reportPage.getCellRange(firstRowFinder, lastRowFinder, 10, 20)).thenReturn(tableCellRange2);

        tableFactory.createNameless(reportPage, providedTableName, firstRowFinder, dataRowFinder, lastRowFinder, headerDescription);

        verify(reportPage).getCellRange(firstRowFinder, dataRowFinder, 0, 0);
        verify(reportPage).getCellRange(firstRowFinder, lastRowFinder, 10, 20);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRange2,
                headerDescription,
                20);
    }

    @Test
    void createNameless_dataFinder_dataRowNotFound() {
        when(reportPage.getCellRange(firstRowFinder, dataRowFinder, 0, 0)).thenReturn(EMPTY_RANGE);

        tableFactory.createNameless(reportPage, providedTableName, firstRowFinder, dataRowFinder, lastRowFinder, headerDescription);

        verify(reportPage).getCellRange(firstRowFinder, dataRowFinder, 0, 0);
        verify(reportPage, never()).getCellRange(eq(firstRowFinder), eq(lastRowFinder), anyInt(), anyInt());
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                EMPTY_RANGE,
                headerDescription,
                0);
    }

    @Test
    void createNameless_headerRowAndLastRowFinder() {
        when(reportPage.getCellRange(firstRowFinder, lastRowFinder, 0, 1)).thenReturn(tableCellRange);

        tableFactory.createNameless(reportPage, providedTableName, firstRowFinder, lastRowFinder,
                headerDescription, 2);

        verify(reportPage).getCellRange(firstRowFinder, lastRowFinder, 0, 1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRange,
                headerDescription,
                2);
    }

    @Test
    void createNameless_headerRowFinder() {
        when(reportPage.getCellRange(firstRowFinder, null, 0, 1)).thenReturn(tableCellRange);

        tableFactory.createNameless(reportPage, providedTableName, firstRowFinder, null, headerDescription, 2);

        verify(reportPage).getCellRange(firstRowFinder, null, 0, 1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRange,
                headerDescription,
                2);
    }
}