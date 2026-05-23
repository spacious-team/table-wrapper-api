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

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.spacious_team.table_wrapper.api.ReportPageRowHelper.*;
import static org.spacious_team.table_wrapper.api.StringPrefixPredicate.ignoreCaseStringPrefixPredicateOnObject;
import static org.spacious_team.table_wrapper.api.TableCellAddress.NOT_FOUND;
import static org.spacious_team.table_wrapper.api.TableCellRange.EMPTY_RANGE;

@ExtendWith(MockitoExtension.class)
class ReportPageTest {

    static TableFactory tableFactory;
    Object value = new Object();
    TableCellAddress address1 = TableCellAddress.of(1, 2);
    TableCellAddress address2 = TableCellAddress.of(3, 4);
    String prefix1 = "A";
    String prefix2 = "B";
    Predicate<Object> predicate1 = ignoreCaseStringPrefixPredicateOnObject(prefix1);
    Predicate<Object> predicate2 = ignoreCaseStringPrefixPredicateOnObject(prefix2);
    String tableName = "table name";
    String headerRow = "header row";
    String tableFooterString = "footer";
    Predicate<Object> tableNameFinder = cell -> true;
    Predicate<Object> tableFooterFinder = cell -> true;
    Class<TableHeader> tableHeader = TableHeader.class;
    @Mock
    ReportPageRow row1;
    @Mock
    ReportPageRow row2;
    @Spy
    ReportPage reportPage;

    @BeforeAll
    static void beforeAll() {
        tableFactory = mock(TableFactory.class);
        when(tableFactory.canHandle(any())).thenReturn(true);
        TableFactoryRegistry.add(tableFactory);
    }

    @AfterAll
    static void afterAll() {
        TableFactoryRegistry.remove(tableFactory);
    }

    @Test
    void find1() {
        reportPage.find(value);
        verify(reportPage).find(value, 0);
    }

    @Test
    void find2() {
        reportPage.find(value, 1);
        verify(reportPage).find(value, 1, Integer.MAX_VALUE);
    }

    @Test
    void find3() {
        reportPage.find(value, 1, 2);
        verify(reportPage).find(value, 1, 2, 0, Integer.MAX_VALUE);
    }

    @Test
    void find4() {
        reportPage.find(predicate1);
        verify(reportPage).find(0, predicate1);
    }

    @Test
    void find5() {
        reportPage.find(1, predicate1);
        verify(reportPage).find(1, Integer.MAX_VALUE, predicate1);
    }

    @Test
    void find6() {
        reportPage.find(1, 2, predicate1);
        verify(reportPage).find(1, 2, 0, Integer.MAX_VALUE, predicate1);
    }

    @Test
    void findByPrefix1() {
        reportPage.findByPrefix(prefix1);
        verify(reportPage).findByPrefix(prefix1, 0);
    }

    @Test
    void findByPrefix2() {
        reportPage.findByPrefix(prefix1, 1);
        verify(reportPage).findByPrefix(prefix1, 1, Integer.MAX_VALUE);
    }

    @Test
    void findByPrefix3() {
        reportPage.findByPrefix(prefix1, 1, 2);
        verify(reportPage).findByPrefix(prefix1, 1, 2, 0, Integer.MAX_VALUE);
    }

    @Test
    void testFindByPrefix_withNull() {
        assertSame(
                NOT_FOUND,
                reportPage.findByPrefix(null, 1, 2, 3, 4));
        assertSame(
                NOT_FOUND,
                reportPage.findByPrefix("", 1, 2, 3, 4));
    }

    @Test
    void findByPrefix4() {
        reportPage.findByPrefix(prefix1, 1, 2, 3, 4);
        verify(reportPage).find(1, 2, 3, 4, predicate1);
    }

    @Test
    void getNextColumnValue() {
        String prefix = "test";
        doReturn(NOT_FOUND).when(reportPage).findByPrefix(prefix);

        reportPage.getNextColumnValue(prefix);

        verify(reportPage).getNextColumnValue(prefix, 1, Integer.MAX_VALUE);
    }

    @Test
    void getNextColumnValue_exactPosition() {
        String prefix = "test";
        doReturn(NOT_FOUND).when(reportPage).findByPrefix(prefix);

        reportPage.getNextColumnValue(prefix, 10);

        verify(reportPage).getNextColumnValue(prefix, 10, 10);
    }


    @ParameterizedTest
    @MethodSource("nextColumnValueRows_rangePosition")
    void getNextColumnValue_rangePosition(ReportPageRow row,
                                          int searchMinOffset, int searchMaxOffset,
                                          Object expected) {
        String prefix = "test";
        doReturn(address1).when(reportPage).findByPrefix(prefix);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row);

        @Nullable
        Object result = reportPage.getNextColumnValue(prefix, searchMinOffset, searchMaxOffset);

        assertEquals(expected, result);
        verify(reportPage).findByPrefix(prefix);
        verify(reportPage).getRow(address1.getRow());
    }

    @SuppressWarnings("ConstantConditions")
    static Object[][] nextColumnValueRows_rangePosition() {
        return new Object[][]{
                {null, -1, 1, null},
                {getRow(0, null, null), -2, 5, null},
                {getRow(0, cell("value", 3)), -1, 5, "value"},
                {getRow(0, cell("value", 3)), 1, 5, "value"},
                {getRow(0, cell("value", 3)), 2, 5, null},
                {getRow(0, cell(123, 1)), -1, -1, 123},
                {getRow(0, cell(123, 3)), -1, -1, null},
                {getRow(0,
                        cell("key", 2),
                        cell(null, 3),
                        cell("", 4),
                        cell(" ", 5),
                        cell("value1", 6),
                        cell("value2", 7)), -1, 10, "value1"}};
    }

    @Test
    void getNextRowValue() {
        String prefix = "test";
        doReturn(NOT_FOUND).when(reportPage).findByPrefix(prefix);

        reportPage.getNextRowValue(prefix);

        verify(reportPage).getNextRowValue(prefix, 1, Integer.MAX_VALUE);
    }

    @Test
    void getNextRowValue_exactPosition() {
        String prefix = "test";
        doReturn(NOT_FOUND).when(reportPage).findByPrefix(prefix);

        reportPage.getNextRowValue(prefix, 10);

        verify(reportPage).getNextRowValue(prefix, 10, 10);
    }


    @ParameterizedTest
    @MethodSource("nextRowValueRows_rangePosition")
    void getNextRowValue_rangePosition(ReportPageRow[] rows,
                                       int searchMinOffset, int searchMaxOffset,
                                       Object expected) {
        String prefix = "test";
        doReturn(address1).when(reportPage).findByPrefix(prefix);
        for (int i = 0; i < rows.length; i++) {
            //noinspection ConstantConditions
            lenient().when(reportPage.getRow(i)).thenReturn(rows[i]);
        }
        when(reportPage.getLastRowNum()).thenReturn(rows.length - 1);

        @Nullable
        Object result = reportPage.getNextRowValue(prefix, searchMinOffset, searchMaxOffset);

        assertEquals(expected, result);
        verify(reportPage).findByPrefix(prefix);
    }

    @SuppressWarnings("ConstantConditions")
    static Object[][] nextRowValueRows_rangePosition() {
        return new Object[][]{
                {new ReportPageRow[]{}, -1, 1, null},
                {new ReportPageRow[]{null, null, null}, -1, 1, null},
                {getThreeRowsHeader(), 1, 1, "b2"},
                {getThreeRowsHeader(), -1, 5, "b2"},
                {getThreeRowsHeader(), 1, 5, "b2"},
                {getThreeRowsHeader(), 2, 5, null},
                {getThreeRowsHeader(), -1, 0, null},
                {new ReportPageRow[]{
                        getRow(0),
                        getRow(1, cell("key", 2)),
                        getRow(2),
                        getRow(3),
                        getRow(4, null, null),
                        getRow(5, cell(null, 2)),
                        getRow(6, cell("", 2)),
                        getRow(7, cell(" ", 2)),
                        getRow(8, cell(123L, 2)),
                        getRow(9, cell("value", 2))}, -1, 20, 123L}};
    }

    @Test
    void getCell_returnNull() {
        assertNull(reportPage.getCell(address1));
        verify(reportPage).getRow(address1.getRow());
    }

    @Test
    void getCell() {
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);

        reportPage.getCell(address1);

        verify(reportPage).getRow(address1.getRow());
        verify(row1).getCell(address1.getColumn());
    }

    @Test
    void getCellRange_withNullArgs() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange(null, "xyz", 2));
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange("", "xyz", 2));
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange("xyz", null, 2));
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange("xyz", "", 2));
    }

    @Test
    void getCellRange() {
        doReturn(null).when(reportPage).getCellRange(predicate1, predicate2, 2);
        reportPage.getCellRange(prefix1, prefix2, 2);
        verify(reportPage).getCellRange(predicate1, predicate2, 2);
    }

    @Test
    void getCellRange_withNullPredicates() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange(null, cell -> false, 2));
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange(cell -> false, null, 2));
    }

    @Test
    void getCellRange_returnEmptyRange1() {
        doReturn(NOT_FOUND).when(reportPage).find(predicate1);

        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange(predicate1, cell -> true, 2));
    }

    @Test
    void getCellRange_returnEmptyRange2() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        doReturn(NOT_FOUND).when(reportPage).find(anyInt(), eq(predicate2));

        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange(predicate1, predicate2, 2));
    }

    @Test
    void getCellRange_returnExceptionally1() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> reportPage.getCellRange(predicate1, predicate2, 2));
    }

    @Test
    void getCellRange_returnExceptionally2() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        doReturn(address2).when(reportPage).find(anyInt(), eq(predicate2));
        //noinspection ConstantConditions
        when(reportPage.getRow(address2.getRow())).thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> reportPage.getCellRange(predicate1, predicate2, 2));
    }

    @Test
    void getCellRange_returnOk() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        doReturn(address2).when(reportPage).find(anyInt(), eq(predicate2));
        //noinspection ConstantConditions
        when(reportPage.getRow(address2.getRow())).thenReturn(row2);
        when(row1.getFirstCellNum()).thenReturn(10);
        when(row2.getLastCellNum()).thenReturn(20);

        assertEquals(
                TableCellRange.of(address1.getRow(), address2.getRow(), row1.getFirstCellNum(), row2.getLastCellNum()),
                reportPage.getCellRange(predicate1, predicate2, 2));
    }

    @Test
    void getCellRange_withNullArgs2() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange((String) null, 2));
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange("", 2));
    }

    @Test
    void getCellRange2() {
        doReturn(null).when(reportPage).getCellRange(predicate1, 2);
        reportPage.getCellRange(prefix1, 2);
        verify(reportPage).getCellRange(predicate1, 2);
    }

    @Test
    void getCellRange_withNullPredicates2() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange((Predicate<Object>) null, 2));
    }

    @Test
    void getCellRange_returnEmptyRange12() {
        doReturn(NOT_FOUND).when(reportPage).find(predicate1);

        assertSame(
                EMPTY_RANGE,
                reportPage.getCellRange(predicate1, 2));
    }

    @Test
    void getCellRange_returnExceptionally21() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> reportPage.getCellRange(predicate1, 2));
    }

    @Test
    void getCellRange_lastRowNotFound_returnOk() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        doReturn(-1).when(reportPage).findEmptyRow(anyInt());
        int lastRowNum = 3;
        when(reportPage.getLastRowNum()).thenReturn(lastRowNum);
        //noinspection ConstantConditions
        when(reportPage.getRow(lastRowNum)).thenReturn(row2);
        when(row1.getFirstCellNum()).thenReturn(10);
        when(row2.getLastCellNum()).thenReturn(20);

        assertEquals(
                TableCellRange.of(address1.getRow(), lastRowNum, row1.getFirstCellNum(), row2.getLastCellNum()),
                reportPage.getCellRange(predicate1, 2));
    }

    @Test
    void getCellRange_excludeEmptyRow_returnOk() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        int emptyRowNum = 3;
        doReturn(emptyRowNum).when(reportPage).findEmptyRow(anyInt());
        int lastRowNum = 4;
        when(reportPage.getLastRowNum()).thenReturn(lastRowNum);
        //noinspection ConstantConditions
        when(reportPage.getRow(emptyRowNum - 1)).thenReturn(row2);
        when(row1.getFirstCellNum()).thenReturn(10);
        when(row2.getLastCellNum()).thenReturn(20);

        assertTrue(emptyRowNum > address1.getRow());
        assertEquals(
                TableCellRange.of(address1.getRow(), emptyRowNum - 1, row1.getFirstCellNum(), row2.getLastCellNum()),
                reportPage.getCellRange(predicate1, 2));
    }

    @Test
    void getCellRange_emptyRowGreaterThanStartAddress_returnOk() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        int emptyRowNum = 1;
        doReturn(emptyRowNum).when(reportPage).findEmptyRow(anyInt());
        int lastRowNum = 4;
        when(reportPage.getLastRowNum()).thenReturn(lastRowNum);
        when(row1.getFirstCellNum()).thenReturn(10);
        when(row1.getLastCellNum()).thenReturn(20);

        assertTrue(emptyRowNum <= address1.getRow());
        assertEquals(
                TableCellRange.of(address1.getRow(), address1.getRow(), row1.getFirstCellNum(), row1.getLastCellNum()),
                reportPage.getCellRange(predicate1, 2));
        verify(reportPage, times(1)).getRow(address1.getRow());
    }

    @Test
    void findEmptyRow_notFound() {
        ReportPageRow row = getRow(1, cell("abc", 0));
        //noinspection ConstantConditions
        when(reportPage.getRow(1)).thenReturn(row);
        when(reportPage.getLastRowNum()).thenReturn(1);

        assertEquals(-1, reportPage.findEmptyRow(1));
    }

    @Test
    void findEmptyRow_foundFirst() {
        //noinspection ConstantConditions
        when(reportPage.getRow(1)).thenReturn(null);
        when(reportPage.getLastRowNum()).thenReturn(1000);

        assertEquals(1, reportPage.findEmptyRow(1));
    }

    @Test
    void findEmptyRow_foundSecond() {
        ReportPageRow row = getRow(1, cell(123, 0));
        //noinspection ConstantConditions
        when(reportPage.getRow(1)).thenReturn(row);
        //noinspection ConstantConditions
        when(reportPage.getRow(2)).thenReturn(null);
        when(reportPage.getLastRowNum()).thenReturn(1000);

        assertEquals(2, reportPage.findEmptyRow(1));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void findEmptyRow_foundSecondWithNull() {
        ReportPageRow row1 = getRow(1, cell(null, 0), cell("abc", 1));
        ReportPageRow row2 = getRow(2, cell(null, 0), cell(null, 1));
        when(reportPage.getRow(1)).thenReturn(row1);
        when(reportPage.getRow(2)).thenReturn(row2);
        when(reportPage.getLastRowNum()).thenReturn(1000);

        assertEquals(2, reportPage.findEmptyRow(1));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void findEmptyRow_foundSecondWithNullCell() {
        ReportPageRow row1 = getRow(1, cell(null, 0), cell("abc", 1));
        ReportPageRow row2 = getRow(2, null, null);
        when(reportPage.getRow(1)).thenReturn(row1);
        when(reportPage.getRow(2)).thenReturn(row2);
        when(reportPage.getLastRowNum()).thenReturn(1000);

        assertEquals(2, reportPage.findEmptyRow(1));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void findEmptyRow_foundSecondWithEmptyString() {
        ReportPageRow row1 = getRow(1, cell(null, 0), cell("abc", 1));
        ReportPageRow row2 = getRow(2, cell("", 0), cell("", 20));
        when(reportPage.getRow(1)).thenReturn(row1);
        when(reportPage.getRow(2)).thenReturn(row2);
        when(reportPage.getLastRowNum()).thenReturn(1000);

        assertEquals(2, reportPage.findEmptyRow(1));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void findEmptyRow_foundSecondWithNoCell() {
        ReportPageRow row1 = getRow(1, cell(null, 0), cell("abc", 1));
        ReportPageRow row2 = getRow(2);
        when(reportPage.getRow(1)).thenReturn(row1);
        when(reportPage.getRow(2)).thenReturn(row2);
        when(reportPage.getLastRowNum()).thenReturn(1000);

        assertEquals(2, reportPage.findEmptyRow(1));
    }

    @ParameterizedTest
    @MethodSource("getRows")
    void isEmptyRow(boolean expected, ReportPageRow row) {
        assertEquals(expected, reportPage.isRowEmpty(row));
    }

    @SuppressWarnings("ConstantConditions")
    static Object[][] getRows() {
        return new Object[][]{
                {true, null},
                {true, getRow(0)},
                {true, getRow(0, null, null)},
                {true, getRow(0, cell(null, 3), cell("", 4))},
                {false, getRow(0, cell(null, 3), cell(" ", 4))},
                {false, getRow(0, cell("value", 3))},
                {false, getRow(0, cell(123, 3))}};
    }

    @Test
    void create() {
        reportPage.create(tableName, tableFooterString, tableHeader);
        verify(tableFactory).create(reportPage, tableName, tableFooterString, tableHeader);
    }

    @Test
    void testCreate() {
        reportPage.create(tableName, tableHeader);
        verify(tableFactory).create(reportPage, tableName, tableHeader);
    }

    @Test
    void testCreate1() {
        reportPage.create(tableName, tableFooterString, tableHeader, 2);
        verify(tableFactory).create(reportPage, tableName, tableFooterString, tableHeader, 2);
    }

    @Test
    void testCreate2() {
        reportPage.create(tableName, tableHeader, 2);
        verify(tableFactory).create(reportPage, tableName, tableHeader, 2);
    }

    @Test
    void testCreate3() {
        reportPage.create(tableNameFinder, tableFooterFinder, tableHeader);
        verify(tableFactory).create(reportPage, tableNameFinder, tableFooterFinder, tableHeader);
    }

    @Test
    void testCreate4() {
        reportPage.create(tableNameFinder, tableHeader);
        verify(tableFactory).create(reportPage, tableNameFinder, tableHeader);
    }

    @Test
    void testCreate5() {
        reportPage.create(tableNameFinder, tableFooterFinder, tableHeader, 2);
        verify(tableFactory).create(reportPage, tableNameFinder, tableFooterFinder, tableHeader, 2);
    }

    @Test
    void testCreate6() {
        reportPage.create(tableNameFinder, tableHeader, 2);
        verify(tableFactory).create(reportPage, tableNameFinder, tableHeader, 2);
    }

    @Test
    void createNameless() {
        reportPage.createNameless(headerRow, tableFooterString, tableHeader);
        verify(tableFactory).createNameless(reportPage, headerRow, tableFooterString, tableHeader);
    }

    @Test
    void testCreateNameless() {
        reportPage.createNameless(headerRow, tableHeader);
        verify(tableFactory).createNameless(reportPage, headerRow, tableHeader);
    }

    @Test
    void testCreateNameless1() {
        reportPage.createNameless(tableName, headerRow, tableFooterString, tableHeader, 2);
        verify(tableFactory).createNameless(reportPage, tableName, headerRow, tableFooterString, tableHeader, 2);
    }

    @Test
    void testCreateNameless2() {
        reportPage.createNameless(tableName, headerRow, tableHeader, 2);
        verify(tableFactory).createNameless(reportPage, tableName, headerRow, tableHeader, 2);
    }

    @Test
    void testCreateNameless3() {
        reportPage.createNameless(tableNameFinder, tableFooterFinder, tableHeader);
        verify(tableFactory).createNameless(reportPage, tableNameFinder, tableFooterFinder, tableHeader);
    }

    @Test
    void testCreateNameless4() {
        reportPage.createNameless(tableNameFinder, tableHeader);
        verify(tableFactory).createNameless(reportPage, tableNameFinder, tableHeader);
    }

    @Test
    void testCreateNameless5() {
        reportPage.createNameless(tableName, tableNameFinder, tableFooterFinder, tableHeader, 2);
        verify(tableFactory).createNameless(reportPage, tableName, tableNameFinder, tableFooterFinder, tableHeader, 2);
    }

    @Test
    void testCreateNameless6() {
        reportPage.createNameless(tableName, tableNameFinder, tableHeader, 2);
        verify(tableFactory).createNameless(reportPage, tableName, tableNameFinder, tableHeader, 2);
    }


    enum TableHeader implements TableHeaderColumn {
        ;

        @Override
        public TableColumn getColumn() {
            throw new UnsupportedOperationException();
        }
    }
}