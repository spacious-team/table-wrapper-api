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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableCellTest {

    @Spy
    TableCell cell;

    @Test
    void getValueOrDefault() {
        cell.getValueOrDefault(new Object());
        verify(cell).getValue();
    }

    @Test
    void getValueOrDefaultExceptionally() {
        Object expectedDefault = new Object();
        doThrow(RuntimeException.class).when(cell).getValue();

        @SuppressWarnings("DataFlowIssue") @Nullable
        Object result = cell.getValueOrDefault(expectedDefault);

        assertSame(expectedDefault, result);
        verify(cell).getValue();
    }

    @Test
    void getIntValueOrDefault() {
        doReturn(1).when(cell).getIntValue();

        int result = cell.getIntValueOrDefault(2);

        assertEquals(1, result);
        verify(cell).getIntValue();
    }

    @Test
    void getIntValueOrDefaultExceptionally() {
        int expectedDefault = 2;
        doThrow(RuntimeException.class).when(cell).getIntValue();

        int result = cell.getIntValueOrDefault(expectedDefault);

        assertEquals(expectedDefault, result);
        verify(cell).getIntValue();
    }

    @Test
    void getLongValueOrDefault() {
        doReturn(1L).when(cell).getLongValue();

        long result = cell.getLongValueOrDefault(2);

        assertEquals(1, result);
        verify(cell).getLongValue();
    }

    @Test
    void getLongValueOrDefaultExceptionally() {
        long expectedDefault = 2;
        doThrow(RuntimeException.class).when(cell).getLongValue();

        long result = cell.getLongValueOrDefault(expectedDefault);

        assertEquals(expectedDefault, result);
        verify(cell).getLongValue();
    }

    @Test
    void getDoubleValue() {
        doReturn(1.0).when(cell).getDoubleValue();

        double result = cell.getDoubleValueOrDefault(2.0);

        assertEquals(1.0, result);
        verify(cell).getDoubleValue();
    }

    @Test
    void getDoubleValueOrDefaultExceptionally() {
        double expectedDefault = 2.0;
        doThrow(RuntimeException.class).when(cell).getDoubleValue();

        double result = cell.getDoubleValueOrDefault(expectedDefault);

        assertEquals(expectedDefault, result);
        verify(cell).getDoubleValue();
    }

    @Test
    void getBigDecimalValueOrDefault() {
        doReturn(BigDecimal.valueOf(1.0)).when(cell).getBigDecimalValue();

        BigDecimal result = cell.getBigDecimalValueOrDefault(BigDecimal.valueOf(2.0));

        assertEquals(BigDecimal.valueOf(1.0), result);
        verify(cell).getBigDecimalValue();
    }

    @Test
    void getBigDecimalValueOrDefaultExceptionally() {
        BigDecimal expectedDefault = BigDecimal.valueOf(2.0);
        doThrow(RuntimeException.class).when(cell).getBigDecimalValue();

        BigDecimal result = cell.getBigDecimalValueOrDefault(expectedDefault);

        assertSame(expectedDefault, result);
        verify(cell).getBigDecimalValue();
    }

    @Test
    void getStringValueOrDefault() {
        doReturn("1").when(cell).getStringValue();

        String result = cell.getStringValueOrDefault("2");

        assertEquals("1", result);
        verify(cell).getStringValue();
    }

    @Test
    void getStringValueOrDefaultExceptionally() {
        String expectedDefault = "2";
        doThrow(RuntimeException.class).when(cell).getStringValue();

        String result = cell.getStringValueOrDefault(expectedDefault);

        assertSame(expectedDefault, result);
        verify(cell).getStringValue();
    }

    @Test
    void getInstantValueOrDefault() {
        Instant expected = Instant.now();
        doReturn(expected).when(cell).getInstantValue();

        Instant result = cell.getInstantValueOrDefault(Instant.EPOCH);

        assertEquals(expected, result);
        verify(cell).getInstantValue();
    }

    @Test
    void getInstantValueOrDefaultExceptionally() {
        Instant expectedDefault = Instant.now();
        doThrow(RuntimeException.class).when(cell).getInstantValue();

        Instant result = cell.getInstantValueOrDefault(expectedDefault);

        assertSame(expectedDefault, result);
        verify(cell).getInstantValue();
    }

    @Test
    void getLocalDateTimeValueOrDefault() {
        LocalDateTime expected = LocalDateTime.now();
        doReturn(expected).when(cell).getLocalDateTimeValue();

        LocalDateTime result = cell.getLocalDateTimeValueOrDefault(LocalDateTime.MIN);

        assertEquals(expected, result);
        verify(cell).getLocalDateTimeValue();
    }

    @Test
    void getLocalDateTimeValueOrDefaultExceptionally() {
        LocalDateTime expectedDefault = LocalDateTime.now();
        doThrow(RuntimeException.class).when(cell).getLocalDateTimeValue();

        LocalDateTime result = cell.getLocalDateTimeValueOrDefault(expectedDefault);

        assertSame(expectedDefault, result);
        verify(cell).getLocalDateTimeValue();
    }

    @Test
    void getLocalDateTimeValueOnZoneIdOrDefault() {
        ZoneOffset expectedZone = ZoneOffset.UTC;
        LocalDateTime expected = LocalDateTime.now();
        doReturn(expected).when(cell).getLocalDateTimeValue(any());

        LocalDateTime result = cell.getLocalDateTimeValueOrDefault(expectedZone, LocalDateTime.MIN);

        assertEquals(expected, result);
        verify(cell).getLocalDateTimeValue(expectedZone);
    }

    @Test
    void getZoneIdLocalDateTimeValueOnZoneIdOrDefaultExceptionally() {
        ZoneOffset expectedZone = ZoneOffset.UTC;
        LocalDateTime expectedDefault = LocalDateTime.now();
        doThrow(RuntimeException.class).when(cell).getLocalDateTimeValue(any());

        LocalDateTime result = cell.getLocalDateTimeValueOrDefault(expectedZone, expectedDefault);

        assertSame(expectedDefault, result);
        verify(cell).getLocalDateTimeValue(expectedZone);
    }
}