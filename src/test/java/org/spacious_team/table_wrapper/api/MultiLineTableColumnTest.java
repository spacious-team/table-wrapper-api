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

class MultiLineTableColumnTest {

    @Test
    void getColumnIndex() {
        ReportPageRow[] rows = ReportPageRowHelper.getThreeRowsHeader();
        assertEquals(2, MultiLineTableColumn.of("one", "a1", "b2").getColumnIndex(rows));
        assertEquals(5, MultiLineTableColumn.of("one", "a2", "b1").getColumnIndex(rows));
        assertEquals(26, MultiLineTableColumn.of("TWO", "A2", "b2").getColumnIndex(rows));

        TableColumn column1 = MultiLineTableColumn.of("not", "fo", "und");
        assertThrows(TableColumnNotFound.class, () -> column1.getColumnIndex(rows));
        TableColumn column2 = MultiLineTableColumn.of("one");
        assertThrows(TableColumnNotFound.class, () -> column2.getColumnIndex(rows));
        TableColumn column3 = MultiLineTableColumn.of("Three", "a2");
        assertThrows(TableColumnNotFound.class, () -> column3.getColumnIndex(rows));
        TableColumn column4 = MultiLineTableColumn.of("one", "a1", "b2");
        assertThrows(TableColumnNotFound.class, () -> column4.getColumnIndex(3, rows));
    }

    @Test
    void equalsAndHashCode() {
        EqualsVerifier
                .forClass(MultiLineTableColumn.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    @Test
    void restToString() {
        assertEquals(
                "MultiLineTableColumn(rowDescriptors=[PatternTableColumn(words=[a]), PatternTableColumn(words=[b])])",
                MultiLineTableColumn.of("a", "b").toString());
    }
}