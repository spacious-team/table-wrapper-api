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
     * For vertical table of key-value records (table with two columns), search and return value for requested key.
     */
    default @Nullable Object getNextColumnValue(String firstColumnValuePrefix) {
        TableCellAddress address = findByPrefix(firstColumnValuePrefix);
        @Nullable ReportPageRow row = getRow(address.getRow());
        if (row != null) {
            for (@Nullable TableCell cell : row) {
                if (cell != null && cell.getColumnIndex() > address.getColumn()) {
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
     * Returns table range. Table's first row starts with 'firstRowPrefix' prefix in one of the cells
     * and table ends with predefined prefix in one of the last row cells.
     */
    default TableCellRange getTableCellRange(@Nullable String firstRowPrefix,
                                             int headersRowCount,
                                             @Nullable String lastRowPrefix) {
        if (firstRowPrefix == null || lastRowPrefix == null || firstRowPrefix.isEmpty() || lastRowPrefix.isEmpty()) {
            return TableCellRange.EMPTY_RANGE;
        }
        return getTableCellRange(
                ignoreCaseStringPrefixPredicateOnObject(firstRowPrefix),
                headersRowCount,
                ignoreCaseStringPrefixPredicateOnObject(lastRowPrefix));
    }

    /**
     * Returns table range. First and last row will be found by predicate.
     */
    default TableCellRange getTableCellRange(@Nullable Predicate<@Nullable Object> firstRowFinder,
                                             int headersRowCount,
                                             @Nullable Predicate<@Nullable Object> lastRowFinder) {
        if (firstRowFinder == null || lastRowFinder == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        TableCellAddress startAddress = find(firstRowFinder);
        if (startAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        @SuppressWarnings({"nullness", "ConstantConditions"})
        ReportPageRow firstRow = requireNonNull(getRow(startAddress.getRow()), "Row is not found");
        TableCellAddress endAddress = find(startAddress.getRow() + headersRowCount + 1, lastRowFinder);
        if (endAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        @SuppressWarnings({"nullness", "ConstantConditions"})
        ReportPageRow lastRow = requireNonNull(getRow(endAddress.getRow()), "Row is not found");
        return TableCellRange.of(
                startAddress.getRow(),
                endAddress.getRow(),
                firstRow.getFirstCellNum(),
                lastRow.getLastCellNum());
    }

    /**
     * Returns table range. First row starts with 'firstRowPrefix' prefix in one of the cells,
     * range ends with empty row or last row of report page.
     */
    default TableCellRange getTableCellRange(@Nullable String firstRowPrefix, int headersRowCount) {
        if (firstRowPrefix == null || firstRowPrefix.isEmpty()) {
            return TableCellRange.EMPTY_RANGE;
        }
        return getTableCellRange(
                ignoreCaseStringPrefixPredicateOnObject(firstRowPrefix),
                headersRowCount);
    }

    /**
     * Returns table range. First row will be found by predicate, range ends with empty row or last row of report page.
     */
    default TableCellRange getTableCellRange(@Nullable Predicate<@Nullable Object> firstRowFinder, int headersRowCount) {
        if (firstRowFinder == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        TableCellAddress startAddress = find(firstRowFinder);
        if (startAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        @SuppressWarnings({"nullness", "ConstantConditions"})
        ReportPageRow firstRow = requireNonNull(getRow(startAddress.getRow()), "Row is not found");
        int emptyRowNum = findEmptyRow(startAddress.getRow() + headersRowCount + 1);
        if (emptyRowNum == -1) {
            emptyRowNum = getLastRowNum(); // empty row is not found, use last row
        } else if (emptyRowNum <= getLastRowNum()) {
            emptyRowNum--; // exclude empty row
        }
        ReportPageRow lastRow;
        if (emptyRowNum <= startAddress.getRow()) {
            emptyRowNum = startAddress.getRow();
            lastRow = firstRow;
        } else {
            @SuppressWarnings({"nullness", "ConstantConditions"})
            ReportPageRow row = requireNonNull(getRow(emptyRowNum), "Row is not found");
            lastRow = row;
        }
        return TableCellRange.of(
                startAddress.getRow(),
                emptyRowNum,
                firstRow.getFirstCellNum(),
                lastRow.getLastCellNum());
    }

    /**
     * Returns zero-based index of empty row.
     * This implementation generates a huge amount of garbage. May be overridden for improve performance.
     *
     * @param startRow first row for check
     * @return index of first empty row or -1 if empty row is not found
     */
    default int findEmptyRow(int startRow) {
        int lastRowNum = startRow;
        for (int n = getLastRowNum(); lastRowNum <= n; lastRowNum++) {
            @Nullable ReportPageRow row = getRow(lastRowNum);
            if (row == null || row.getLastCellNum() == -1) {
                return lastRowNum; // all row's cells are blank
            }
            boolean isEmptyRow = true;
            for (@Nullable TableCell cell : row) {
                @Nullable Object value;
                if (!(cell == null
                        || ((value = cell.getValue()) == null)
                        || (value instanceof String) && (value.toString().isEmpty()))) {
                    isEmptyRow = false;
                    break;
                }
            }
            if (isEmptyRow) {
                return lastRowNum; // all row's cells are blank
            }
        }
        return -1;
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(String tableName,
                 String tableFooterString,
                 Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableFooterString, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(String tableName,
                 Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(String tableName,
                 String tableFooterString,
                 Class<T> headerDescription,
                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableFooterString, headerDescription, headersRowCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(String tableName,
                 Class<T> headerDescription,
                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, headerDescription, headersRowCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(Predicate<@Nullable Object> tableNameFinder,
                 Predicate<@Nullable Object> tableFooterFinder,
                 Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, tableFooterFinder, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(Predicate<@Nullable Object> tableNameFinder,
                 Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(Predicate<@Nullable Object> tableNameFinder,
                 Predicate<@Nullable Object> tableFooterFinder,
                 Class<T> headerDescription,
                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, tableFooterFinder, headerDescription, headersRowCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table create(Predicate<@Nullable Object> tableNameFinder,
                 Class<T> headerDescription,
                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableNameFinder, headerDescription, headersRowCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(String firstLineText,
                         String lastRowString,
                         Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineText, lastRowString, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(String firstLineText,
                         Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineText, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(String providedTableName,
                         String firstLineText,
                         String lastRowString,
                         Class<T> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineText, lastRowString, headerDescription, headersRowCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(String providedTableName,
                         String firstLineText,
                         Class<T> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineText, headerDescription, headersRowCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(Predicate<@Nullable Object> firstLineFinder,
                         Predicate<@Nullable Object> lastRowFinder,
                         Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineFinder, lastRowFinder, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(Predicate<@Nullable Object> firstLineFinder,
                         Class<T> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineFinder, headerDescription);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(String providedTableName,
                         Predicate<@Nullable Object> firstLineFinder,
                         Predicate<@Nullable Object> lastRowFinder,
                         Class<T> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineFinder, lastRowFinder, headerDescription, headersRowCount);
    }

    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(String providedTableName,
                         Predicate<@Nullable Object> firstLineFinder,
                         Class<T> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineFinder, headerDescription, headersRowCount);
    }
}
