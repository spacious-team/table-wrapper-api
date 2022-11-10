/*
 * Table Wrapper API
 * Copyright (C) 2020  Spacious Team <spacious-team@ya.ru>
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

public interface TableColumn {
    TableColumn LEFTMOST_COLUMN = (firstColumnForSearch, $) -> firstColumnForSearch;
    TableColumn NOCOLUMN = (i, j) -> {
        throw new TableColumnNotFound("No column impl");
    };

    /**
     * @param headerRows header rows
     * @return column index of table
     * @throws TableColumnNotFound if column is not found
     */
    default int getColumnIndex(ReportPageRow... headerRows) {
        return getColumnIndex(0, headerRows);
    }

    /**
     * @param firstColumnForSearch start column index for search from
     * @param headerRows           header rows
     * @return column index of table
     * @throws TableColumnNotFound if column is not found
     */
    int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows);
}
