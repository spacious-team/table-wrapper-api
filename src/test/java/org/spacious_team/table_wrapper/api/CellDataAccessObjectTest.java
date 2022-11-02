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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CellDataAccessObjectTest {

    CellDataAccessObject<Object, ReportPageRow> dao;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        dao = spy(CellDataAccessObject.class);
        when(dao.getValue(any())).then(invocation -> invocation.getArguments()[0]);
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
        assertThrows(NumberFormatException.class, () -> dao.getDoubleValue("0xFF"));
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
}