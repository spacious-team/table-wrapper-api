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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableFactoryTest {

    @Spy
    TableFactory tableFactory;
    @Mock
    ReportPage reportPage;
    @Mock
    TableCellRange tableCellRange;
    @Mock
    TableCellRange tableCellRangeAddTop1;
    String tableName = "table name";
    String lastRowString = "footer";
    Predicate<Object> tableNameFinder = cell -> true;
    Predicate<Object> lastRowFinder = cell -> true;
    String firstRowString = "first row";
    String providedTableName = "provided table name";
    Predicate<Object> firstRowFinder = cell -> true;
    Class<ReportPageTest.TableHeader> headerDescription = ReportPageTest.TableHeader.class;

    @BeforeEach
    void setUp() {

    }

    @Test
    void create() {
        tableFactory.create(reportPage, tableName, lastRowString, headerDescription);
        verify(tableFactory).create(reportPage, tableName, lastRowString, headerDescription, 1);
    }

    @Test
    void testCreate() {
        tableFactory.create(reportPage, tableName, headerDescription);
        verify(tableFactory).create(reportPage, tableName, headerDescription, 1);
    }

    @Test
    void testCreate1() {
        when(reportPage.getTableCellRange(tableName, 2, lastRowString)).thenReturn(tableCellRange);

        tableFactory.create(reportPage, tableName, lastRowString, headerDescription, 2);

        verify(reportPage).getTableCellRange(tableName, 2, lastRowString);
        verify(tableFactory).create(
                reportPage,
                tableName,
                tableCellRange,
                headerDescription,
                2);
    }

    @Test
    void testCreate2() {
        when(reportPage.getTableCellRange(tableName, 2)).thenReturn(tableCellRange);

        tableFactory.create(reportPage, tableName, headerDescription, 2);

        verify(reportPage).getTableCellRange(tableName, 2);
        verify(tableFactory).create(
                reportPage,
                tableName,
                tableCellRange,
                headerDescription,
                2);
    }

    @Test
    void testCreate3() {
        when(reportPage.getTableCellRange(tableNameFinder, 1, lastRowFinder)).thenReturn(tableCellRange);
        tableFactory.create(reportPage, tableNameFinder, lastRowFinder, headerDescription);
        verify(tableFactory).create(reportPage, tableNameFinder, lastRowFinder, headerDescription, 1);
    }

    @Test
    void testCreate4() {
        when(reportPage.getTableCellRange(tableNameFinder, 1)).thenReturn(tableCellRange);
        tableFactory.create(reportPage, tableNameFinder, headerDescription);
        verify(tableFactory).create(reportPage, tableNameFinder, headerDescription, 1);
    }

    @Test
    void testCreate5() {
        when(reportPage.getTableCellRange(tableNameFinder, 2, lastRowFinder)).thenReturn(tableCellRange);

        tableFactory.create(reportPage, tableNameFinder, lastRowFinder, headerDescription, 2);

        verify(reportPage).getTableCellRange(tableNameFinder, 2, lastRowFinder);
        verify(tableFactory).create(
                eq(reportPage),
                any(String.class),
                any(TableCellRange.class),
                eq(headerDescription),
                eq(2));
    }

    @Test
    void testCreate6() {
        when(reportPage.getTableCellRange(tableNameFinder, 2)).thenReturn(tableCellRange);

        tableFactory.create(reportPage, tableNameFinder, headerDescription, 2);

        verify(reportPage).getTableCellRange(tableNameFinder, 2);
        verify(tableFactory).create(
                eq(reportPage),
                any(String.class),
                any(TableCellRange.class),
                eq(headerDescription),
                eq(2));
    }

    @Test
    void createNameless() {
        when(reportPage.getTableCellRange(firstRowString, 1, lastRowString)).thenReturn(tableCellRange);
        tableFactory.createNameless(reportPage, firstRowString, lastRowString, headerDescription);
        verify(tableFactory).createNameless(reportPage, "undefined", firstRowString, lastRowString,
                headerDescription, 1);
    }

    @Test
    void testCreateNameless() {
        when(reportPage.getTableCellRange(firstRowString, 1)).thenReturn(tableCellRange);
        tableFactory.createNameless(reportPage, firstRowString, headerDescription);
        verify(tableFactory).createNameless(reportPage, "undefined", firstRowString,
                headerDescription, 1);
    }

    @Test
    void testCreateNameless1() {
        when(reportPage.getTableCellRange(firstRowString, 2, lastRowString)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(1)).thenReturn(tableCellRangeAddTop1);

        tableFactory.createNameless(reportPage, providedTableName, firstRowString, lastRowString,
                headerDescription, 2);

        verify(reportPage).getTableCellRange(firstRowString, 2, lastRowString);
        verify(tableCellRange).addRowsToTop(1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRangeAddTop1,
                headerDescription,
                2);
    }

    @Test
    void testCreateNameless2() {
        when(reportPage.getTableCellRange(firstRowString, 2)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(1)).thenReturn(tableCellRangeAddTop1);

        tableFactory.createNameless(reportPage, providedTableName, firstRowString, headerDescription, 2);

        verify(reportPage).getTableCellRange(firstRowString, 2);
        verify(tableCellRange).addRowsToTop(1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRangeAddTop1,
                headerDescription,
                2);
    }

    @Test
    void testCreateNameless3() {
        when(reportPage.getTableCellRange(firstRowFinder, 1, lastRowFinder)).thenReturn(tableCellRange);
        tableFactory.createNameless(reportPage, firstRowFinder, lastRowFinder, headerDescription);
        verify(tableFactory).createNameless(reportPage, "undefined", firstRowFinder, lastRowFinder,
                headerDescription, 1);
    }

    @Test
    void testCreateNameless4() {
        when(reportPage.getTableCellRange(firstRowFinder, 1)).thenReturn(tableCellRange);
        tableFactory.createNameless(reportPage, firstRowFinder, headerDescription);
        verify(tableFactory).createNameless(reportPage, "undefined", firstRowFinder,
                headerDescription, 1);
    }

    @Test
    void testCreateNameless5() {
        when(reportPage.getTableCellRange(firstRowFinder, 2, lastRowFinder)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(1)).thenReturn(tableCellRangeAddTop1);

        tableFactory.createNameless(reportPage, providedTableName, firstRowFinder, lastRowFinder,
                headerDescription, 2);

        verify(reportPage).getTableCellRange(firstRowFinder, 2, lastRowFinder);
        verify(tableCellRange).addRowsToTop(1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRangeAddTop1,
                headerDescription,
                2);
    }

    @Test
    void testCreateNameless6() {
        when(reportPage.getTableCellRange(firstRowFinder, 2)).thenReturn(tableCellRange);
        when(tableCellRange.addRowsToTop(1)).thenReturn(tableCellRangeAddTop1);

        tableFactory.createNameless(reportPage, providedTableName, firstRowFinder, headerDescription, 2);

        verify(reportPage).getTableCellRange(firstRowFinder, 2);
        verify(tableCellRange).addRowsToTop(1);
        verify(tableFactory).create(
                reportPage,
                providedTableName,
                tableCellRangeAddTop1,
                headerDescription,
                2);
    }
}