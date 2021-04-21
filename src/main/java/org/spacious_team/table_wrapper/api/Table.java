/*
 * Table Wrapper API
 * Copyright (C) 2020  Vitalii Ananev <an-vitek@ya.ru>
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Table extends Iterable<TableRow> {

    /**
     * Extracts exactly one object from excel row
     */
    default <T> List<T> getData(Function<TableRow, T> rowExtractor) {
        return getData("unknown", rowExtractor);
    }

    <T> List<T> getData(Object report, Function<TableRow, T> rowExtractor);

    /**
     * Extracts objects from table without duplicate objects handling (duplicated row are both will be returned)
     */
    default <T> List<T> getDataCollection(Function<TableRow, Collection<T>> rowExtractor) {
        return getDataCollection("unknown", rowExtractor);
    }

    <T> List<T> getDataCollection(Object report, Function<TableRow, Collection<T>> rowExtractor);

    /**
     * Extracts objects from table with duplicate objects handling logic
     */
    <T> List<T> getDataCollection(Object report, Function<TableRow, Collection<T>> rowExtractor,
                                  BiPredicate<T, T> equalityChecker,
                                  BiFunction<T, T, Collection<T>> mergeDuplicates);

    boolean isEmpty();

    Stream<TableRow> stream();

    /**
     * @return row containing given value or null if not found
     */
    TableRow findRow(Object value);

    Map<TableColumn, Integer> getHeaderDescription();

    /**
     * By default table iterates throw all rows, call method if last row is "total" row and it should be excluded
     */
    default Table excludeTotalRow() {
        return subTable(0, -1);
    }

    /**
     * Returns table with same columns but without some top and bottom data rows. For example use
     * <pre>
     *     subTable(0, -1)
     * </pre>
     * for exclude last "Total" row from iterator or stream.
     * @param topRows positive value for inclusion, negative for exclusion
     * @param bottomRows positive value for inclusion, negative for exclusion
     */
    Table subTable(int topRows, int bottomRows);
}
