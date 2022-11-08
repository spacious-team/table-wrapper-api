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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantPositionTableColumnTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 20, 1_000_000})
    void getColumnIndex(int columnNum) {
        ReportPageRow row = ReportPageRowHelperTest.getRow();
        ConstantPositionTableColumn column = ConstantPositionTableColumn.of(columnNum);

        assertEquals(columnNum, column.getColumnIndex(row));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(ConstantPositionTableColumn.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    @Test
    void testToString() {
        assertEquals(
                "ConstantPositionTableColumn(columnIndex=10)",
                ConstantPositionTableColumn.of(10).toString());
    }
}