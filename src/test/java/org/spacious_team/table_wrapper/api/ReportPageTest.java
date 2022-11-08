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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.spacious_team.table_wrapper.api.ReportPageHelper.getCellStringValueIgnoreCasePrefixPredicate;
import static org.spacious_team.table_wrapper.api.TableCellAddress.NOT_FOUND;
import static org.spacious_team.table_wrapper.api.TableCellRange.EMPTY_RANGE;

@ExtendWith(MockitoExtension.class)
class ReportPageTest {

    Object value = new Object();
    TableCellAddress address1 = TableCellAddress.of(1, 2);
    TableCellAddress address2 = TableCellAddress.of(3, 4);
    String prefix1 = "A";
    String prefix2 = "B";
    Predicate<Object> predicate1 = getCellStringValueIgnoreCasePrefixPredicate(prefix1);
    Predicate<Object> predicate2 = getCellStringValueIgnoreCasePrefixPredicate(prefix2);
    @Mock
    ReportPageRow row1;
    @Mock
    ReportPageRow row2;
    @Spy
    ReportPage reportPage;

    @BeforeEach
    void setUp() {
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
    void testFindByPrefix() {
        reportPage.findByPrefix(prefix1, 1, 2);
        verify(reportPage).findByPrefix(prefix1, 1, 2, 0, Integer.MAX_VALUE);
    }

    @Test
    void testFindByPrefixWithNull() {
        assertSame(
                NOT_FOUND,
                reportPage.findByPrefix(null, 1, 2, 3, 4));
        assertSame(
                NOT_FOUND,
                reportPage.findByPrefix("", 1, 2, 3, 4));
    }

    @Test
    void testFindByPrefix1() {
        reportPage.findByPrefix(prefix1, 1, 2, 3, 4);
        verify(reportPage).find(1, 2, 3, 4, predicate1);
    }

    @Test
    void getNextColumnValueNull() {
        String prefix = "test";
        doReturn(address1).when(reportPage).findByPrefix(prefix);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(null);

        @Nullable
        Object result = reportPage.getNextColumnValue(prefix);

        assertNull(result);
        verify(reportPage).findByPrefix(prefix);
        verify(reportPage).getRow(address1.getRow());
    }

    @Test
    void getNextColumnValue() {
        String prefix = "test";
        doReturn(address1).when(reportPage).findByPrefix(prefix);
        ReportPageRow row = TableColumnHelperTest.getRow();
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row);

        @Nullable
        Object result = reportPage.getNextColumnValue(prefix);

        assertEquals(1.23, result);
        verify(reportPage).findByPrefix(prefix);
        verify(reportPage).getRow(address1.getRow());
    }

    @Test
    void getCellNullReturn() {
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
    void getTableCellRangeWithNullArgs() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange(null, 2, "xyz"));
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange("", 2, "xyz"));
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange("xyz", 2, null));
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange("xyz", 2, ""));
    }

    @Test
    void getTableCellRange() {
        doReturn(null).when(reportPage).getTableCellRange(predicate1, 2, predicate2);
        reportPage.getTableCellRange(prefix1, 2, prefix2);
        verify(reportPage).getTableCellRange(predicate1, 2, predicate2);
    }

    @Test
    void testGetTableCellRangeWithNullPredicates() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange(null, 2, cell -> false));
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange(cell -> false, 2, null));
    }

    @Test
    void testGetTableCellRangeReturnEmptyRange1() {
        doReturn(NOT_FOUND).when(reportPage).find(predicate1);

        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange(predicate1, 2, cell -> true));
    }

    @Test
    void testGetTableCellRangeReturnEmptyRange2() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        doReturn(NOT_FOUND).when(reportPage).find(anyInt(), eq(predicate2));

        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange(predicate1, 2, predicate2));
    }

    @Test
    void testGetTableCellRangeReturnExceptionally1() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> reportPage.getTableCellRange(predicate1, 2, predicate2));
    }

    @Test
    void testGetTableCellRangeReturnExceptionally2() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(row1);
        doReturn(address2).when(reportPage).find(anyInt(), eq(predicate2));
        //noinspection ConstantConditions
        when(reportPage.getRow(address2.getRow())).thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> reportPage.getTableCellRange(predicate1, 2, predicate2));
    }

    @Test
    void testGetTableCellRangeReturnOk() {
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
                reportPage.getTableCellRange(predicate1, 2, predicate2));
    }

    @Test
    void getTableCellRangeWithNullArgs2() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange((String) null, 2));
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange("", 2));
    }

    @Test
    void getTableCellRange2() {
        doReturn(null).when(reportPage).getTableCellRange(predicate1, 2);
        reportPage.getTableCellRange(prefix1, 2);
        verify(reportPage).getTableCellRange(predicate1, 2);
    }

    @Test
    void testGetTableCellRangeWithNullPredicates2() {
        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange((Predicate<Object>) null, 2));
    }

    @Test
    void testGetTableCellRangeReturnEmptyRange12() {
        doReturn(NOT_FOUND).when(reportPage).find(predicate1);

        assertSame(
                EMPTY_RANGE,
                reportPage.getTableCellRange(predicate1, 2));
    }

    @Test
    void testGetTableCellRangeReturnExceptionally21() {
        doReturn(address1).when(reportPage).find(predicate1);
        //noinspection ConstantConditions
        when(reportPage.getRow(address1.getRow())).thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> reportPage.getTableCellRange(predicate1, 2));
    }

    @Test
    void testGetTableCellRangeReturnOkLastRowNotFound() {
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
                reportPage.getTableCellRange(predicate1, 2));
    }

    @Test
    void testGetTableCellRangeReturnOkExcludeEmptyRow() {
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
                reportPage.getTableCellRange(predicate1, 2));
    }

    @Test
    void testGetTableCellRangeReturnOkEmptyRowGreaterThanStartAddress() {
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
                reportPage.getTableCellRange(predicate1, 2));
        verify(reportPage, times(1)).getRow(address1.getRow());
    }
}