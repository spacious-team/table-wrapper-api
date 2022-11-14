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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.spacious_team.table_wrapper.api.TableColumn.LEFTMOST_COLUMN;

class AnyOfTableColumnTest {

    TableColumn NOT_FOUND = (x, y) -> {
        throw new RuntimeException();
    };

    @Test
    void getColumnIndex() {
        TableColumn column1 = AnyOfTableColumn.of(LEFTMOST_COLUMN, NOT_FOUND);
        TableColumn column2 = AnyOfTableColumn.of(NOT_FOUND, LEFTMOST_COLUMN);
        TableColumn column3 = AnyOfTableColumn.of(NOT_FOUND, NOT_FOUND);

        assertEquals(0, column1.getColumnIndex());
        assertEquals(10, column1.getColumnIndex(10));
        assertEquals(0, column2.getColumnIndex());
        assertEquals(10, column2.getColumnIndex(10));
        assertThrows(TableColumnNotFound.class, column3::getColumnIndex);
        assertThrows(TableColumnNotFound.class, () -> column3.getColumnIndex(10));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(AnyOfTableColumn.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    @Test
    void testToString() {
        assertEquals("AnyOfTableColumn(columns=[PatternTableColumn(words=[test word])])",
                AnyOfTableColumn.of(PatternTableColumn.of("test word")).toString());
    }
}