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

import static nl.jqno.equalsverifier.Warning.ALL_FIELDS_SHOULD_BE_USED;
import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.*;

class OptionalTableColumnTest {

    @Test
    void getColumnIndex() {
        ReportPageRow row = ReportPageRowHelper.getRow();
        assertEquals(0, OptionalTableColumn.of(PatternTableColumn.of()).getColumnIndex(row));
        assertEquals(9, OptionalTableColumn.of(PatternTableColumn.of("test")).getColumnIndex(row));
        assertEquals(-1, OptionalTableColumn.of(PatternTableColumn.of("test")).getColumnIndex(10, row));
        assertEquals(-1, OptionalTableColumn.of(PatternTableColumn.of("not found")).getColumnIndex(row));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(OptionalTableColumn.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .suppress(ALL_FIELDS_SHOULD_BE_USED)
                .verify();
    }

    @Test
    void testToString() {
        assertEquals(
                "OptionalTableColumn(column=PatternTableColumn(words=[test]))",
                OptionalTableColumn.of(PatternTableColumn.of("test")).toString());
    }
}