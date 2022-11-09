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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableColumnTest {

    @Spy
    TableColumn column;
    @Mock
    ReportPageRow row;

    @Test
    void getColumnIndex() {
        column.getColumnIndex(row);
        verify(column).getColumnIndex(0, row);
    }

    @Test
    void testConstants() {
        assertThrows(RuntimeException.class, () -> TableColumn.NOCOLUMN.getColumnIndex(0));
        assertThrows(RuntimeException.class, () -> TableColumn.NOCOLUMN.getColumnIndex(1));
        assertThrows(RuntimeException.class, () -> TableColumn.NOCOLUMN.getColumnIndex(-2));
        assertEquals(0, TableColumn.LEFTMOST_COLUMN.getColumnIndex(0));
        assertEquals(1, TableColumn.LEFTMOST_COLUMN.getColumnIndex(1));
        assertEquals(-1, TableColumn.LEFTMOST_COLUMN.getColumnIndex(-1));
    }
}