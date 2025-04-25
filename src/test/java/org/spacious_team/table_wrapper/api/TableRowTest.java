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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableRowTest {

    @Spy
    TableRow row;
    @Mock
    TableHeaderColumn column;

    @Test
    void getCellValueOrDefault() {
        Object expected = new Object();
        //noinspection ConstantConditions
        when(row.getCellValue(any())).thenReturn(expected);
        assertSame(expected, row.getCellValueOrDefault(column, new Object()));
    }

    @Test
    void getCellValueOrDefaultThrowable() {
        Object expected = new Object();
        //noinspection ConstantConditions
        when(row.getCellValue(any())).thenThrow(RuntimeException.class);
        assertSame(expected, row.getCellValueOrDefault(column, expected));
    }

    @Test
    void getIntCellValueOrDefault() {
        when(row.getIntCellValue(any())).thenReturn(2);
        assertEquals(2, row.getIntCellValueOrDefault(column, 1));
    }

    @Test
    void getIntCellValueOrDefaultThrowable() {
        when(row.getIntCellValue(any())).thenThrow(RuntimeException.class);
        assertEquals(1, row.getIntCellValueOrDefault(column, 1));
    }

    @Test
    void getLongCellValueOrDefault() {
        when(row.getLongCellValue(any())).thenReturn(2L);
        assertEquals(2L, row.getLongCellValueOrDefault(column, 1L));
    }

    @Test
    void getLongCellValueOrDefaultThrowable() {
        when(row.getLongCellValue(any())).thenThrow(RuntimeException.class);
        assertEquals(1L, row.getLongCellValueOrDefault(column, 1L));
    }

    @Test
    void getDoubleCellValue() {
        when(row.getDoubleCellValue(any())).thenReturn(2.0);
        assertEquals(2.0, row.getDoubleCellValueOrDefault(column, 1.0));
    }

    @Test
    void getDoubleCellValueThrowable() {
        when(row.getDoubleCellValue(any())).thenThrow(RuntimeException.class);
        assertEquals(1.0, row.getDoubleCellValueOrDefault(column, 1.0));
    }

    @Test
    void getBigDecimalCellValueOrDefault() {
        BigDecimal expectedBigDecimal = BigDecimal.valueOf(1.0);
        when(row.getBigDecimalCellValue(any())).thenReturn(expectedBigDecimal);
        assertSame(expectedBigDecimal, row.getBigDecimalCellValueOrDefault(column, BigDecimal.ZERO));
    }

    @Test
    void getBigDecimalCellValueOrDefaultThrowable() {
        BigDecimal expectedBigDecimal = BigDecimal.valueOf(1.0);
        when(row.getBigDecimalCellValue(any())).thenThrow(RuntimeException.class);
        assertSame(expectedBigDecimal, row.getBigDecimalCellValueOrDefault(column, expectedBigDecimal));
    }

    @Test
    void getStringCellValueOrDefault() {
        String expectedString = "test";
        when(row.getStringCellValue(any())).thenReturn(expectedString);
        assertSame(expectedString, row.getStringCellValueOrDefault(column, "default"));
    }

    @Test
    void getStringCellValueOrDefaultThrowable() {
        String expectedString = "test";
        when(row.getStringCellValue(any())).thenThrow(RuntimeException.class);
        assertSame(expectedString, row.getStringCellValueOrDefault(column, expectedString));
    }

    @Test
    void getInstantCellValueOrDefault() {
        Instant expectedInstant = Instant.now();
        when(row.getInstantCellValue(any())).thenReturn(expectedInstant);
        assertSame(expectedInstant, row.getInstantCellValueOrDefault(column, Instant.MIN));
    }

    @Test
    void getInstantCellValueOrDefaultThrowable() {
        Instant expectedInstant = Instant.now();
        when(row.getInstantCellValue(any())).thenThrow(RuntimeException.class);
        assertSame(expectedInstant, row.getInstantCellValueOrDefault(column, expectedInstant));
    }

    @Test
    void getLocalDateTimeCellValueOrDefault() {
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        when(row.getLocalDateTimeCellValue(any())).thenReturn(expectedLocalDateTime);
        assertSame(expectedLocalDateTime, row.getLocalDateTimeCellValueOrDefault(column, LocalDateTime.MIN));
    }

    @Test
    void getLocalDateTimeCellValueOrDefaultThrowable() {
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        when(row.getLocalDateTimeCellValue(any())).thenThrow(RuntimeException.class);
        assertSame(expectedLocalDateTime, row.getLocalDateTimeCellValueOrDefault(column, expectedLocalDateTime));
    }

    @Test
    void getLocalDateTimeCellValueOnZoneIdOrDefault() {
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        when(row.getLocalDateTimeCellValue(any(), any())).thenReturn(expectedLocalDateTime);
        assertSame(expectedLocalDateTime, row.getLocalDateTimeCellValueOrDefault(column, ZoneOffset.UTC, LocalDateTime.MIN));
    }

    @Test
    void getLocalDateTimeCellValueOnZoneIdOrDefaultThrowable() {
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        when(row.getLocalDateTimeCellValue(any(), any())).thenThrow(RuntimeException.class);
        assertSame(expectedLocalDateTime, row.getLocalDateTimeCellValueOrDefault(column, ZoneOffset.UTC, expectedLocalDateTime));
    }
}