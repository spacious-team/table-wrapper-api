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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmptyTableCellTest {

    EmptyTableCell cell = EmptyTableCell.of(1);

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 10})
    void getColumnIndex(int columnIndex) {
        assertEquals(columnIndex, EmptyTableCell.of(columnIndex).getColumnIndex());
    }

    @Test
    void getValue() {
        assertNull(cell.getValue());
        assertThrows(NullPointerException.class, () -> cell.getIntValue());
        assertThrows(NullPointerException.class, () -> cell.getLongValue());
        assertThrows(NullPointerException.class, () -> cell.getDoubleValue());
        assertThrows(NullPointerException.class, () -> cell.getBigDecimalValue());
        assertThrows(NullPointerException.class, () -> cell.getStringValue());
        assertThrows(NullPointerException.class, () -> cell.getInstantValue());
        assertThrows(NullPointerException.class, () -> cell.getLocalDateTimeValue());
    }
}