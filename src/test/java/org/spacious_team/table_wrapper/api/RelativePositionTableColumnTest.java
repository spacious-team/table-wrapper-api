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

class RelativePositionTableColumnTest {

    @Test
    void getColumnIndex() {
        ReportPageRow row = ReportPageRowHelper.getRow();
        assertEquals(2, RelativePositionTableColumn.of(PatternTableColumn.of(), 2).getColumnIndex(row));
        assertEquals(7, RelativePositionTableColumn.of(PatternTableColumn.of("test"), -2).getColumnIndex(row));

        TableColumn column1 = RelativePositionTableColumn.of(PatternTableColumn.of("test"), 2);
        assertThrows(TableColumnNotFound.class, () -> column1.getColumnIndex(10, row));

        TableColumn column2 = RelativePositionTableColumn.of(PatternTableColumn.of("not found"), 2);
        assertThrows(TableColumnNotFound.class, () -> column2.getColumnIndex(row));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(RelativePositionTableColumn.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    @Test
    void testToString() {
        assertEquals(
                "RelativePositionTableColumn(column=PatternTableColumn(words=[test]), positionOffset=2)",
                RelativePositionTableColumn.of(PatternTableColumn.of("test"), 2).toString());
    }
}