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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.spacious_team.table_wrapper.api.TableColumn.LEFTMOST_COLUMN;

class PatternTableColumnTest {

    @Test
    @SuppressWarnings("ConstantConditions")
    void testZeroArg() {
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of());
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(""));
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of((String[]) null));
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(new String[]{null}));
        assertEquals(LEFTMOST_COLUMN, PatternTableColumn.of(null, null));
    }

    @Test
    void getColumnIndex() {
        ReportPageRow row = ReportPageRowHelper.getRow();
        assertEquals(0, PatternTableColumn.of().getColumnIndex(row));
        assertEquals(9, PatternTableColumn.of("test").getColumnIndex(row));
        assertEquals(9, PatternTableColumn.of("WORD").getColumnIndex(row));
        assertEquals(10, PatternTableColumn.of("this is\\s*sparta").getColumnIndex(row));
        assertEquals(10, PatternTableColumn.of("this is").getColumnIndex(row));
        assertEquals(10, PatternTableColumn.of("is sparta").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("is the").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("is", "the").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("is", null, "the").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("(old|Gr..t)").getColumnIndex(row));
        assertEquals(20, PatternTableColumn.of("of").getColumnIndex(row));
        assertEquals(21, PatternTableColumn.of("mac").getColumnIndex(row));
        assertEquals(22, PatternTableColumn.of("windows").getColumnIndex(row));
        assertEquals(22, PatternTableColumn.of("windows").getColumnIndex(21, row));

        TableColumn column1 = PatternTableColumn.of("windows");
        assertThrows(TableColumnNotFound.class, () -> column1.getColumnIndex(23, row));
        TableColumn column2 = PatternTableColumn.of("not found");
        assertThrows(TableColumnNotFound.class, () -> column2.getColumnIndex(row));
        TableColumn column3 = PatternTableColumn.of("london is");
        assertThrows(TableColumnNotFound.class, () -> column3.getColumnIndex(row));
        TableColumn column4 = PatternTableColumn.of("mac new");
        assertThrows(TableColumnNotFound.class, () -> column4.getColumnIndex(row));
        TableColumn column5 = PatternTableColumn.of("windows new");
        assertThrows(TableColumnNotFound.class, () -> column5.getColumnIndex(row));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(PatternTableColumn.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .suppress(ALL_FIELDS_SHOULD_BE_USED)
                .verify();
    }

    @Test
    void testToString() {
        assertEquals("PatternTableColumn(words=[test])", PatternTableColumn.of("test").toString());
    }
}