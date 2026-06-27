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

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.spacious_team.table_wrapper.api.StringPrefixPredicate.ignoreCaseStringPrefixPredicateOnObject;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ReportPage {

    /**
     * Finds cell address containing exact value.
     *
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress find(Object value) {
        return find(value, 0);
    }

    /**
     * Finds cell address containing exact value.
     *
     * @param startRow search rows start from this
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress find(Object value, int startRow) {
        return find(value, startRow, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address containing exact value.
     *
     * @param startRow search rows start from this
     * @param endRow   search rows excluding this, can handle values greater than real rows count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress find(Object value, int startRow, int endRow) {
        return find(value, startRow, endRow, 0, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address containing exact value.
     *
     * @param value       searching value
     * @param startRow    search rows start from this
     * @param endRow      search rows excluding this, can handle values greater than real rows count
     * @param startColumn search columns start from this
     * @param endColumn   search columns excluding this, can handle values greater than real columns count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    TableCellAddress find(Object value, int startRow, int endRow, int startColumn, int endColumn);

    /**
     * Finds cell by predicate.
     *
     * @param cellValuePredicate predicate for testing cell value
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress find(Predicate<@Nullable Object> cellValuePredicate) {
        return find(0, cellValuePredicate);
    }

    /**
     * Finds cell by predicate.
     *
     * @param startRow search rows start from this
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress find(int startRow, Predicate<@Nullable Object> cellValuePredicate) {
        return find(startRow, Integer.MAX_VALUE, cellValuePredicate);
    }

    /**
     * Finds cell by predicate.
     *
     * @param startRow search rows start from this
     * @param endRow   search rows excluding this, can handle values greater than real rows count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress find(int startRow, int endRow, Predicate<@Nullable Object> cellValuePredicate) {
        return find(startRow, endRow, 0, Integer.MAX_VALUE, cellValuePredicate);
    }

    /**
     * Finds cell by predicate.
     *
     * @param startRow           search rows start from this
     * @param endRow             search rows excluding this, can handle values greater than real rows count
     * @param startColumn        search columns start from this
     * @param endColumn          search columns excluding this, can handle values greater than real columns count
     * @param cellValuePredicate predicate for testing cell value
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    TableCellAddress find(int startRow, int endRow,
                          int startColumn, int endColumn,
                          Predicate<@Nullable Object> cellValuePredicate);

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress findByPrefix(String prefix) {
        return findByPrefix(prefix, 0);
    }

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @param startRow search rows start from this
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress findByPrefix(String prefix, int startRow) {
        return findByPrefix(prefix, startRow, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @param startRow search rows start from this
     * @param endRow   search rows excluding this, can handle values greater than real rows count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress findByPrefix(String prefix, int startRow, int endRow) {
        return findByPrefix(prefix, startRow, endRow, 0, Integer.MAX_VALUE);
    }

    /**
     * Finds cell address staring with value (ignore case, trims leading spaces).
     *
     * @param startRow    search rows start from this
     * @param endRow      search rows excluding this, can handle values greater than real rows count
     * @param startColumn search columns start from this
     * @param endColumn   search columns excluding this, can handle values greater than real columns count
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress findByPrefix(@Nullable String prefix, int startRow, int endRow, int startColumn, int endColumn) {
        return (prefix == null || prefix.isEmpty()) ?
                TableCellAddress.NOT_FOUND :
                find(startRow, endRow, startColumn, endColumn, ignoreCaseStringPrefixPredicateOnObject(prefix));
    }

    /**
     * Returns the zero-based index of the first row that matches the predicate.
     *
     * @param startRow search rows start from this
     * @param endRow   search rows excluding this, can handle values greater than real rows count
     * @return the zero-based index of the first matching row, or {@code -1} if no row matches the predicate
     * @implNote The default implementation may produce significant garbage during execution.
     * Subclasses are encouraged to override this method for better performance.
     */
    default int findRow(int startRow, int endRow, Predicate<@Nullable ReportPageRow> predicate) {
        int max = Math.min(endRow, getLastRowNum() + 1);  // exclusive
        for (int i = startRow; i < max; i++) {
            @Nullable ReportPageRow row = getRow(i);
            if (predicate.test(row)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches for a key and returns the value from a multi-column table, where i-th column contains the key,
     * and the nearest non-empty cell in the same row to the right contains the value
     */
    default @Nullable Object getNextColumnValue(String keyPrefix) {
        return getNextColumnValue(keyPrefix, 1, Integer.MAX_VALUE);
    }

    /**
     * Searches for a key and returns the value from a multi-column table, where i-th column contains the key,
     * and the value is in column {@code i + valueColumnOffset} of the same row
     */
    default @Nullable Object getNextColumnValue(String keyPrefix, int valueColumnOffset) {
        return getNextColumnValue(keyPrefix, valueColumnOffset, valueColumnOffset);
    }

    /**
     * Searches for a key and returns the value from a multi-column table, where i-th column contains the key,
     * and the first non-empty cell in the same row contains the value.
     * A constraint applies: the distance from the key cell to the value cell (measured in columns)
     * must be {@code >= searchColumnMinOffset} and {@code <= searchColumnMaxOffset}.
     *
     * @param searchColumnMinOffset positive or negative min column offset
     * @param searchColumnMaxOffset positive or negative max column offset
     */
    default @Nullable Object getNextColumnValue(String keyPrefix, int searchColumnMinOffset, int searchColumnMaxOffset) {
        TableCellAddress address = findByPrefix(keyPrefix);
        @Nullable ReportPageRow row = getRow(address.getRow());
        if (row != null) {
            int keyColumnIndex = address.getColumn();
            int minValueColumnIndex = Math.max(0, keyColumnIndex + searchColumnMinOffset);
            // long for overflow protection
            int maxValueColumnIndex = (int) Math.min(((long) keyColumnIndex) + searchColumnMaxOffset, row.getLastCellNum());
            for (int i = minValueColumnIndex; i <= maxValueColumnIndex; i++) {
                if (i == keyColumnIndex) continue;
                @Nullable TableCell cell = row.getCell(i);
                if (cell != null) {
                    @Nullable Object value = cell.getValue();
                    if (value != null && (!(value instanceof String) || !((String) value).isBlank())) {
                        return value;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Searches for a key and returns the value from a multi-row table, where i-th row contains the key,
     * and the nearest non-empty cell below it in the same column contains the value
     */
    default @Nullable Object getNextRowValue(String keyPrefix) {
        return getNextRowValue(keyPrefix, 1, Integer.MAX_VALUE);
    }

    /**
     * Searches for a key and returns the value from a multi-row table, where i-th row contains the key,
     * and the value is in row {@code i + valueRowOffset} of the same column
     */
    default @Nullable Object getNextRowValue(String keyPrefix, int valueRowOffset) {
        return getNextRowValue(keyPrefix, valueRowOffset, valueRowOffset);
    }

    /**
     * Searches for a key and returns the value from a multi-row table, where i-th row contains the key,
     * and the first non-empty cell in the same column contains the value.
     * A constraint applies: the distance from the key cell to the value cell (measured in rows)
     * must be {@code >= valueRowMinOffset} and {@code <= valueRowMaxOffset}.
     *
     * @param valueRowMinOffset positive or negative min row offset
     * @param valueRowMaxOffset positive or negative max row offset
     */
    default @Nullable Object getNextRowValue(String keyPrefix, int valueRowMinOffset, int valueRowMaxOffset) {
        TableCellAddress address = findByPrefix(keyPrefix);
        if (address == TableCellAddress.NOT_FOUND) {
            return null;
        }
        int keyRowIndex = address.getRow();
        int keyColIndex = address.getColumn();
        int minValueRowIndex = Math.max(0, keyRowIndex + valueRowMinOffset);
        // long for overflow protection
        int maxValueRowIndex = (int) Math.min(((long) keyRowIndex) + valueRowMaxOffset, getLastRowNum());
        for (int i = minValueRowIndex; i <= maxValueRowIndex; i++) {
            if (i == keyRowIndex) continue;
            @Nullable ReportPageRow row = getRow(i);
            if (row != null) {
                @Nullable TableCell cell = row.getCell(keyColIndex);
                if (cell != null) {
                    @Nullable Object value = cell.getValue();
                    if (value != null && (!(value instanceof String) || !((String) value).isBlank())) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param i zero-based index
     * @return row object or null is row does not exist
     * @apiNote Method impl should return {@link CellDataAccessObject} aware {@link ReportPageRow} impl
     */
    @Nullable
    ReportPageRow getRow(int i);

    /**
     * @return last row contained on this page (zero-based) or -1 if no row exists
     */
    int getLastRowNum();

    default @Nullable TableCell getCell(TableCellAddress address) {
        @Nullable ReportPageRow row = getRow(address.getRow());
        return (row == null) ? null : row.getCell(address.getColumn());
    }

    /**
     * Returns a range of rows.
     * The range begins with the first row that contains a cell starting with {@code firstRowPrefix}.
     * If {@code lastRowPrefix} is not null and not empty,
     * then the range ends with the row that contains a cell starting with {@code lastRowPrefix}.
     * If {@code lastRowPrefix} is null or empty, then the range ends with empty row or last row of report page.
     *
     * @param firstRowFinderOffset the offset to start searching for the first row
     * @param lastRowFinderOffset  the number of rows to skip after the first row to start searching for the last row
     *                             (0 to start searching from the next row)
     */
    default TableCellRange getCellRange(@Nullable String firstRowPrefix,
                                        @Nullable String lastRowPrefix,
                                        int firstRowFinderOffset,
                                        int lastRowFinderOffset) {
        if (firstRowPrefix == null || firstRowPrefix.isEmpty()) {
            return TableCellRange.EMPTY_RANGE;
        } else if (lastRowPrefix == null || lastRowPrefix.isEmpty()) {
            return getCellRange(
                    ignoreCaseStringPrefixPredicateOnObject(firstRowPrefix),
                    null,
                    firstRowFinderOffset,
                    lastRowFinderOffset);
        } else {
            return getCellRange(
                    ignoreCaseStringPrefixPredicateOnObject(firstRowPrefix),
                    ignoreCaseStringPrefixPredicateOnObject(lastRowPrefix),
                    firstRowFinderOffset,
                    lastRowFinderOffset);
        }
    }

    /**
     * Returns a range of rows.
     * The first row of the range is determined by a predicate.
     * If {@code lastRowFinder} is not null, then the last row of the range is determined by a predicate.
     * If {@code lastRowFinder} is null, then the range ends with empty row or last row of report page.
     *
     * @param firstRowFinderOffset the offset to start searching for the first row
     * @param lastRowFinderOffset  the number of rows to skip after the first row to start searching for the last row
     *                             (0 to start searching from the next row)
     */
    default TableCellRange getCellRange(@Nullable Predicate<@Nullable Object> firstRowFinder,
                                        @Nullable Predicate<@Nullable Object> lastRowFinder,
                                        int firstRowFinderOffset,
                                        int lastRowFinderOffset) {
        if (firstRowFinder == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        TableCellAddress startAddress = find(firstRowFinderOffset, firstRowFinder);
        if (Objects.equals(startAddress, TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        @SuppressWarnings({"nullness", "ConstantConditions"})
        ReportPageRow firstRow = requireNonNull(getRow(startAddress.getRow()), "Row is not found");
        int lastRowSearchOffset = firstRow.getRowNum() + lastRowFinderOffset + 1;

        ReportPageRow lastRow;
        if (lastRowFinder == null) {  // search end of range by empty row
            int emptyRowNum = findRow(lastRowSearchOffset, Integer.MAX_VALUE, EmptyRowPredicate.INSTANCE);
            if (emptyRowNum == -1) {
                emptyRowNum = getLastRowNum(); // empty row is not found, use last row
            } else if (emptyRowNum <= getLastRowNum()) {
                emptyRowNum--; // exclude empty row
            }
            if (emptyRowNum <= firstRow.getRowNum()) {
                lastRow = firstRow;
            } else {
                @SuppressWarnings({"nullness", "ConstantConditions"})
                ReportPageRow row = requireNonNull(getRow(emptyRowNum), "Row is not found");  // NPE logically impossible
                lastRow = row;
            }
        } else {  // search end of range by predicate
            TableCellAddress endAddress = find(lastRowSearchOffset, lastRowFinder);
            if (Objects.equals(endAddress, TableCellAddress.NOT_FOUND)) {
                return TableCellRange.EMPTY_RANGE;
            }
            @SuppressWarnings({"nullness", "ConstantConditions"})
            ReportPageRow row = requireNonNull(getRow(endAddress.getRow()), "Row is not found");
            lastRow = row;
        }

        return TableCellRange.of(
                firstRow.getRowNum(),
                lastRow.getRowNum(),
                firstRow.getFirstCellNum(),
                lastRow.getLastCellNum());
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createTable(String tableName,
                      int tableNameRowCount,
                      String firstDataRowPrefix,
                      @Nullable String lastRowPrefix,
                      Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableNameRowCount, firstDataRowPrefix, lastRowPrefix, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createTable(String tableName,
                      int tableNameRowCount,
                      @Nullable String lastRowPrefix,
                      Class<T> headerDescription,
                      int headerRowsCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableNameRowCount, lastRowPrefix, headerDescription, headerRowsCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createTable(Predicate<@Nullable Object> tableNameFinder,
                      int tableNameRowCount,
                      Predicate<@Nullable Object> firstDataRowFinder,
                      @Nullable Predicate<@Nullable Object> lastRowFinder,
                      Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, tableNameRowCount, firstDataRowFinder, lastRowFinder, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createTable(Predicate<@Nullable Object> tableNameFinder,
                      int tableNameRowCount,
                      @Nullable Predicate<@Nullable Object> lastRowFinder,
                      Class<T> headerDescription,
                      int headerRowsCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, tableNameRowCount, lastRowFinder, headerDescription, headerRowsCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNamelessTable(String providedTableName,
                              String headerRowPrefix,
                              String firstDataRowPrefix,
                              @Nullable String lastRowPrefix,
                              Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, headerRowPrefix, firstDataRowPrefix, lastRowPrefix, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNamelessTable(String providedTableName,
                              String headerRowPrefix,
                              @Nullable String lastRowPrefix,
                              Class<T> headerDescription,
                              int headerRowsCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, headerRowPrefix, lastRowPrefix, headerDescription, headerRowsCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNamelessTable(String providedTableName,
                              Predicate<@Nullable Object> headerRowFinder,
                              Predicate<@Nullable Object> firstDataRowFinder,
                              @Nullable Predicate<@Nullable Object> lastRowFinder,
                              Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, headerRowFinder, firstDataRowFinder, lastRowFinder, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNamelessTable(String providedTableName,
                              Predicate<@Nullable Object> headerRowFinder,
                              @Nullable Predicate<@Nullable Object> lastRowFinder,
                              Class<T> headerDescription,
                              int headerRowsCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, headerRowFinder, lastRowFinder, headerDescription, headerRowsCount);
    }
}
