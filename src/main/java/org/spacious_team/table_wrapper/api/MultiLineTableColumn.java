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

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

/**
 * Implements table header kind of
 * <pre>
 * |             One             |             Two            |
 * |   a1    |   a2    |   a3    |   a1   |   a2    |   a3    |
 * | b1 | b2 | b1 | b2 | b1 | b2 |b1 | b2 | b1 | b2 | b1 | b2 |
 * <pre/>
 * Can find index for (Two -> a3 -> b1) column
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = PRIVATE)
public class MultiLineTableColumn implements TableColumn {
    private final TableColumn[] rowDescriptors;

    /**
     * @param rowDescriptors each array element describes next rows column
     */
    public static MultiLineTableColumn of(TableColumn... rowDescriptors) {
        return new MultiLineTableColumn(rowDescriptors);
    }

    public static MultiLineTableColumn of(String... rowDescriptors) {
        TableColumn[] descriptors = Arrays.stream(rowDescriptors)
                .map(Objects::requireNonNull)
                .map(PatternTableColumn::of)
                .toArray(TableColumn[]::new);
        return new MultiLineTableColumn(descriptors);
    }

    /**
     * @param headerRows header rows count should be equal to count of row descriptors
     */
    @Override
    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        if (headerRows.length != rowDescriptors.length) {
            throw new RuntimeException("Внутренняя ошибка, в таблице ожидается " + rowDescriptors.length +
                    " строк в заголовке");
        }
        int columnIndex = firstColumnForSearch;
        int i = 0;
        for (ReportPageRow row : headerRows) {
            TableColumn rowDescriptor = rowDescriptors[i++];
            columnIndex = rowDescriptor.getColumnIndex(columnIndex, row);
        }
        return columnIndex;
    }
}
