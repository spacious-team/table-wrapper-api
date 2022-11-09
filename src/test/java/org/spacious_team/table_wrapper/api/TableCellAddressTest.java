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
import static org.spacious_team.table_wrapper.api.TableCellAddress.NOT_FOUND;

class TableCellAddressTest {

    @Test
    void testConstructor() {
        assertEquals(1, TableCellAddress.of(1, 2).getRow());
        assertEquals(2, TableCellAddress.of(1, 2).getColumn());
    }

    @Test
    void testNotFoundCell() {
        assertEquals(-1, NOT_FOUND.getRow());
        assertEquals(-1, NOT_FOUND.getColumn());
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(TableCellAddress.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    @Test
    void testToString() {
        assertEquals(
                "TableCellAddress(row=1, column=2)",
                TableCellAddress.of(1, 2).toString());
    }
}