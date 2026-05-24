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

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Predicate;

class EmptyRowPredicate implements Predicate<@Nullable ReportPageRow> {

    static final EmptyRowPredicate INSTANCE = new EmptyRowPredicate();

    @Override
    public boolean test(@Nullable ReportPageRow row) {
        if (row == null || row.getLastCellNum() == -1) {
            return true; // all row's cells are blank
        }
        for (@Nullable TableCell cell : row) {
            @Nullable Object value;
            if (!(cell == null
                    || ((value = cell.getValue()) == null)
                    || (value instanceof String) && (value.toString().isEmpty()))) {
                return false;
            }
        }
        return true;
    }
}
