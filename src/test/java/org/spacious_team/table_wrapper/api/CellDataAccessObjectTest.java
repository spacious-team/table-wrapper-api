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
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CellDataAccessObjectTest {

    final int EXISTS_CELL_INDEX = 0;
    final int NOT_EXISTS_CELL_INDEX = 1_000_000;
    @Mock
    ReportPageRow row;
    @Mock
    Object cell;
    CellDataAccessObject<Object, ReportPageRow> dao;

    @BeforeEach
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    void setUp() {
        dao = spy(CellDataAccessObject.class);
        lenient().when(dao.getValue(any())).then(invocation -> invocation.getArguments()[0]);
        lenient().when(dao.getCell(row, EXISTS_CELL_INDEX)).thenReturn(cell);
        lenient().when(dao.getCell(row, NOT_EXISTS_CELL_INDEX)).thenReturn(null);
    }

    @Test
    void getIntValue() {
        Object cell = "10";
        dao.getIntValue(cell);
        verify(dao).getLongValue(cell);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void getLongValueException() {
        assertThrows(NullPointerException.class, () -> dao.getLongValue(null));
        assertThrows(NumberFormatException.class, () -> dao.getLongValue("10.0"));
        assertThrows(NumberFormatException.class, () -> dao.getLongValue("abc"));
        assertThrows(NumberFormatException.class, () -> dao.getLongValue("0xFF"));
    }

    @ParameterizedTest
    @MethodSource("longFactory")
    void getLongValue(long expected, Object value) {
        assertEquals(expected, dao.getLongValue(value));
    }

    static Object[][] longFactory() {
        return new Object[][]{
                {10L, (byte) 10},
                {10L, (short) 10},
                {10L, 10},
                {10L, 10L},
                {10L, BigInteger.valueOf(10)},
                {10L, BigDecimal.valueOf(10.1)},
                {10L, 10.0},
                {10L, 10.1},
                {10L, 10.9},
                {10L, "10"},
        };
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void getDoubleValueException() {
        assertThrows(NullPointerException.class, () -> dao.getDoubleValue(null));
        assertThrows(NumberFormatException.class, () -> dao.getDoubleValue("abc"));
        assertThrows(NumberFormatException.class, () -> dao.getDoubleValue("a.bc"));
        assertThrows(NumberFormatException.class, () -> dao.getDoubleValue("0xFF"));
        assertThrows(NumberFormatException.class, () -> dao.getDoubleValue("1.2.3"));
    }

    @ParameterizedTest
    @MethodSource("doubleFactory")
    void getDoubleValue(double expected, Object value) {
        assertEquals(expected, dao.getDoubleValue(value));
    }

    static Object[][] doubleFactory() {
        return new Object[][]{
                {10d, (byte) 10},
                {10d, (short) 10},
                {10d, 10},
                {10d, 10L},
                {10d, BigInteger.valueOf(10)},
                {10.1d, BigDecimal.valueOf(10.1)},
                {10d, 10.0},
                {10.1d, 10.1},
                {10.9d, 10.9},
                {10d, "10"},
                {10.1d, "10.1"},
                {10.1d, "10,1"},
                {1000.1d, "1 000.1"},
        };
    }

    @Test
    @SuppressWarnings({"ConstantConditions"})
    void getBigDecimalValueSpecialCases() {
        assertThrows(NullPointerException.class, () -> dao.getDoubleValue(null));
        assertThrows(NumberFormatException.class, () -> dao.getDoubleValue("abc"));
        assertThrows(NumberFormatException.class, () -> dao.getDoubleValue("0xFF"));
        assertNotEquals(BigDecimal.valueOf(10), dao.getBigDecimalValue("10.0"));
        assertNotEquals(BigDecimal.valueOf(10.0), dao.getBigDecimalValue("10"));
        assertSame(BigDecimal.ZERO, dao.getBigDecimalValue("0"));
        assertSame(BigDecimal.ZERO, dao.getBigDecimalValue("0.0"));
        assertSame(BigDecimal.ZERO, dao.getBigDecimalValue("0.00"));
        assertNotSame(BigDecimal.ZERO, dao.getBigDecimalValue("0.000"));
        assertEquals(BigDecimal.valueOf(0, 3), dao.getBigDecimalValue("0.000"));
        assertNotEquals(BigDecimal.valueOf(0, 4), dao.getBigDecimalValue("0.000"));
        assertNotEquals(BigDecimal.valueOf(0.0), dao.getBigDecimalValue("0.000"));
    }

    @ParameterizedTest
    @MethodSource("bigDecimalFactory")
    void getBigDecimalValue(BigDecimal expected, Object value) {
        assertEquals(expected, dao.getBigDecimalValue(value));
    }

    static Object[][] bigDecimalFactory() {
        return new Object[][]{
                {BigDecimal.valueOf(10), (byte) 10},
                {BigDecimal.valueOf(10), (short) 10},
                {BigDecimal.valueOf(10), 10},
                {BigDecimal.valueOf(10), 10L},
                {BigDecimal.valueOf(10), BigInteger.valueOf(10)},
                {BigDecimal.valueOf(10.1), BigDecimal.valueOf(10.1)},
                {BigDecimal.valueOf(10.0), 10.0},
                {BigDecimal.valueOf(10.1), 10.1},
                {BigDecimal.valueOf(10.9), 10.9},
                {BigDecimal.valueOf(10.0), "10.0"},
                {BigDecimal.valueOf(10), "10"},
                {BigDecimal.valueOf(10.1), "10.1"},
                {BigDecimal.valueOf(10.1), "10,1"},
                {BigDecimal.valueOf(1000.1), "1 000.1"},
        };
    }

    @Test
    void getStringValue() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> dao.getStringValue(null));
        assertEquals("Abc", dao.getStringValue("Abc"));
        assertEquals("12", dao.getStringValue(12));
    }

    @Test
    void getLocalDateTimeValue() {
        LocalDateTime expected = LocalDateTime.of(2000, 1, 2, 3, 4, 5);
        Instant instant = expected
                .atZone(ZoneId.systemDefault())
                .toInstant();
        when(dao.getInstantValue(any())).thenReturn(instant);
        assertEquals(expected, dao.getLocalDateTimeValue(instant));
    }

    @Test
    void getLocalDateTimeValueAtTimeZone() {
        int zoneOffset = 3;
        Instant instant = LocalDateTime.of(2000, 1, 2, zoneOffset, 4, 5)
                .atZone(ZoneOffset.ofHours(zoneOffset))
                .toInstant();
        LocalDateTime expectedAtUtc = LocalDateTime.of(2000, 1, 2, 0, 4, 5);
        when(dao.getInstantValue(any())).thenReturn(instant);
        assertEquals(expectedAtUtc, dao.getLocalDateTimeValue(instant, ZoneOffset.UTC));
    }

    @Test
    void getValueNull() {
        assertNull(dao.getValue(row, NOT_EXISTS_CELL_INDEX));
        verify(dao, never()).getValue(any());
    }

    @Test
    void getValue() {
        dao.getValue(row, EXISTS_CELL_INDEX);

        verify(dao).getCell(row, EXISTS_CELL_INDEX);
        verify(dao).getValue(cell);
    }

    @Test
    void testGetIntValue() {
        doReturn(10).when(dao).getIntValue(cell);

        dao.getIntValue(row, EXISTS_CELL_INDEX);

        verify(dao).getIntValue(cell);
        assertThrows(NullPointerException.class, () -> dao.getIntValue(row, NOT_EXISTS_CELL_INDEX));
    }

    @Test
    void testGetLongValue() {
        doReturn(10L).when(dao).getLongValue(cell);

        dao.getLongValue(row, EXISTS_CELL_INDEX);

        verify(dao).getLongValue(cell);
        assertThrows(NullPointerException.class, () -> dao.getLongValue(row, NOT_EXISTS_CELL_INDEX));
    }

    @Test
    void testGetDoubleValue() {
        doReturn(10.0).when(dao).getDoubleValue(cell);

        dao.getDoubleValue(row, EXISTS_CELL_INDEX);

        verify(dao).getDoubleValue(cell);
        assertThrows(NullPointerException.class, () -> dao.getDoubleValue(row, NOT_EXISTS_CELL_INDEX));
    }

    @Test
    void testGetBigDecimalValue() {
        doReturn(null).when(dao).getBigDecimalValue(cell);

        dao.getBigDecimalValue(row, EXISTS_CELL_INDEX);

        verify(dao).getBigDecimalValue(cell);
        assertThrows(NullPointerException.class, () -> dao.getBigDecimalValue(row, NOT_EXISTS_CELL_INDEX));
    }

    @Test
    void testGetStringValue() {
        doReturn(null).when(dao).getStringValue(cell);

        dao.getStringValue(row, EXISTS_CELL_INDEX);

        verify(dao).getStringValue(cell);
        assertThrows(NullPointerException.class, () -> dao.getStringValue(row, NOT_EXISTS_CELL_INDEX));
    }

    @Test
    void getInstantValue() {
        doReturn(null).when(dao).getInstantValue(cell);

        dao.getInstantValue(row, EXISTS_CELL_INDEX);

        verify(dao).getInstantValue(cell);
        assertThrows(NullPointerException.class, () -> dao.getInstantValue(row, NOT_EXISTS_CELL_INDEX));
    }

    @Test
    void testGetLocalDateTimeValue() {
        doReturn(null).when(dao).getLocalDateTimeValue(cell);

        dao.getLocalDateTimeValue(row, EXISTS_CELL_INDEX);

        verify(dao).getLocalDateTimeValue(cell);
        assertThrows(NullPointerException.class, () -> dao.getLocalDateTimeValue(row, NOT_EXISTS_CELL_INDEX));
    }

    @Test
    void testGetLocalDateTimeValueAtTimeZone() {
        doReturn(null).when(dao).getLocalDateTimeValue(cell, ZoneOffset.UTC);

        dao.getLocalDateTimeValue(row, EXISTS_CELL_INDEX, ZoneOffset.UTC);

        verify(dao).getLocalDateTimeValue(cell, ZoneOffset.UTC);
        assertThrows(NullPointerException.class, () -> dao.getLocalDateTimeValue(row, NOT_EXISTS_CELL_INDEX, ZoneOffset.UTC));
    }
}