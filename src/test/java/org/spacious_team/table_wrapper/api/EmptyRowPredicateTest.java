/*
 * Table Wrapper API
 * Copyright (C) 2026  Spacious Team <spacious-team@ya.ru>
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

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.spacious_team.table_wrapper.api.ReportPageRowHelper.cell;
import static org.spacious_team.table_wrapper.api.ReportPageRowHelper.getRow;

@ExtendWith(MockitoExtension.class)
class EmptyRowPredicateTest {

    @ParameterizedTest
    @MethodSource("getRows")
    void isEmptyRow(boolean expected, ReportPageRow row) {
        assertEquals(expected, EmptyRowPredicate.INSTANCE.test(row));
    }

    @SuppressWarnings("ConstantConditions")
    static Object[][] getRows() {
        return new Object[][]{
                {true, null},
                {true, getRow(0)},
                {true, getRow(0, null, null)},
                {true, getRow(0, cell(null, 3), cell("", 4))},
                {false, getRow(0, cell(null, 3), cell(" ", 4))},
                {false, getRow(0, cell("value", 3))},
                {false, getRow(0, cell(123, 3))}};
    }
}