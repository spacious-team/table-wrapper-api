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

import static org.spacious_team.table_wrapper.api.TableCellRange.EMPTY_RANGE;

public interface TableFactory {

    boolean canHandle(ReportPage reportPage);

    /**
     * Creates a table.
     * The table found by {@code tableName} prefix. Table name spans {@code tableNameRowCount} rows.
     * The table header starts after the table name rows.
     * If {@code lastRowPrefix} is not null and not empty,
     * the range ends with row containing cell with text starting with {@code lastRowPrefix}.
     * If {@code lastRowPrefix} is null or empty, the range ends with empty row or last row of report page.
     * The first data row is determined by cell with text starting with {@code firstDataRowPrefix}.
     *
     * @param tableName          the table name row should contain cell which starts with this prefix
     * @param tableNameRowCount  the number of rows to skip after the first table name row (inclusive) to reach the table header
     * @param firstDataRowPrefix the first data row should contain cell which starts with this prefix
     * @param lastRowPrefix      if not null, then the table last row should contain cell which starts with this prefix,
     *                           if null the table ends with empty row or last row of report page
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 int tableNameRowCount,
                 String firstDataRowPrefix,
                 @Nullable String lastRowPrefix,
                 Class<T> headerDescription) {
        TableCellRange headerRange = reportPage.getCellRange(tableName, firstDataRowPrefix,
                0, tableNameRowCount);  // (tableNameRowCount + 1 - 1): table name rows and at least 1 header row
        int headerRowsCount = 0;
        TableCellRange range = EMPTY_RANGE;
        if (!Objects.equals(headerRange, EMPTY_RANGE)) {
            headerRowsCount = headerRange.getLastRow() - headerRange.getFirstRow() - tableNameRowCount;
            range = reportPage.getCellRange(tableName, lastRowPrefix, headerRange.getFirstRow(), headerRowsCount)
                    .addRowsToTop(-tableNameRowCount);
        }
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }


    /**
     * Creates a table.
     * The table found by {@code tableName} prefix. Table name spans {@code tableNameRowCount} rows.
     * The table header starts after the table name rows.
     * If {@code lastRowPrefix} is not null and not empty,
     * the range ends with row containing cell with text starting with {@code lastRowPrefix}.
     * If {@code lastRowPrefix} is null or empty, the range ends with empty row or last row of report page.
     *
     * @param tableName         the table name row should contain cell which starts with this prefix
     * @param tableNameRowCount the number of rows to skip after the table name to reach the table header
     * @param lastRowPrefix     if not null, then the table last row should contain cell which starts with this prefix,
     *                          if null the table ends with empty row or last row of report page
     * @param headerRowsCount   the number of rows to skip after the first header row (inclusive) to reach the table data row
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 int tableNameRowCount,
                 @Nullable String lastRowPrefix,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableName, lastRowPrefix,
                        0, tableNameRowCount + headerRowsCount - 1)
                .addRowsToTop(-tableNameRowCount);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table.
     * The table found by {@code tableNameFinder} predicate. Table name spans {@code tableNameRowCount} rows.
     * The table header starts after the table name rows.
     * If {@code lastRowFinder} is not null, the end of the range is determined by this predicate.
     * If {@code lastRowFinder} is null, the range ends with empty row or last row of report page.
     * The first data row is determined by {@code firstDataRowFinder} predicate.
     *
     * @param tableNameFinder    table name row should contain cell satisfying predicate
     * @param tableNameRowCount  the number of rows to skip after the first table name row (inclusive) to reach the table header
     * @param firstDataRowFinder the first data row should contain cell satisfying predicate
     * @param lastRowFinder      if not null, then the table last row should contain cell satisfying predicate,
     *                           if null the table ends with empty row or last row of report page
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 int tableNameRowCount,
                 Predicate<@Nullable Object> firstDataRowFinder,
                 @Nullable Predicate<@Nullable Object> lastRowFinder,
                 Class<T> headerDescription) {
        TableCellRange headerRange = reportPage.getCellRange(tableNameFinder, firstDataRowFinder,
                0, tableNameRowCount);  // (tableNameRowCount + 1 - 1): table name rows and at least 1 header row
        int headerRowsCount = 0;
        TableCellRange range = EMPTY_RANGE;
        if (!Objects.equals(headerRange, EMPTY_RANGE)) {
            headerRowsCount = headerRange.getLastRow() - headerRange.getFirstRow() - tableNameRowCount;
            range = reportPage.getCellRange(tableNameFinder, lastRowFinder, headerRange.getFirstRow(), headerRowsCount)
                    .addRowsToTop(-tableNameRowCount);
        }
        String tableName = TableFactoryHelper.getTableName(reportPage, tableNameFinder, range);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table.
     * The table found by {@code tableNameFinder} predicate. Table name spans {@code tableNameRowCount} rows.
     * The table header starts after the table name rows.
     * If {@code lastRowFinder} is not null, the end of the range is determined by this predicate.
     * If {@code lastRowFinder} is null, the range ends with empty row or last row of report page.
     *
     * @param tableNameFinder   table name row should contain cell satisfying predicate
     * @param tableNameRowCount the number of rows to skip after the first table name row (inclusive) to reach the table header
     * @param lastRowFinder     if not null, then the table last row should contain cell satisfying predicate,
     *                          if null the table ends with empty row or last row of report page
     * @param headerRowsCount   the number of rows to skip after the first header row (inclusive) to reach the table data row
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 int tableNameRowCount,
                 @Nullable Predicate<@Nullable Object> lastRowFinder,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableNameFinder, lastRowFinder,
                        0, tableNameRowCount + headerRowsCount - 1)
                .addRowsToTop(-tableNameRowCount);
        String tableName = TableFactoryHelper.getTableName(reportPage, tableNameFinder, range);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table.
     * The table found by {@code headerRowPrefix} prefix.
     * If {@code lastRowPrefix} is not null and not empty,
     * the range ends with row containing cell with text starting with {@code lastRowPrefix}.
     * If {@code lastRowPrefix} is null or empty, the range ends with empty row or last row of report page.
     * The first data row is determined by cell with text starting with {@code firstDataRowPrefix}.
     *
     * @param providedTableName  the generated name of the table
     * @param headerRowPrefix    the table first row should contain cell which starts with this prefix
     * @param firstDataRowPrefix the first data row should contain cell which starts with this prefix
     * @param lastRowPrefix      if not null, then the table last row should contain cell which starts with this prefix,
     *                           if null the table ends with empty row or last row of report page
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String headerRowPrefix,
                         String firstDataRowPrefix,
                         @Nullable String lastRowPrefix,
                         Class<T> headerDescription) {
        TableCellRange headerRange = reportPage.getCellRange(headerRowPrefix, firstDataRowPrefix, 0, 0);
        int headerRowsCount = 0;
        TableCellRange range = EMPTY_RANGE;
        if (!Objects.equals(headerRange, EMPTY_RANGE)) {
            headerRowsCount = headerRange.getLastRow() - headerRange.getFirstRow();
            range = reportPage.getCellRange(headerRowPrefix, lastRowPrefix, headerRange.getFirstRow(), headerRowsCount);
        }
        return create(reportPage, providedTableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table.
     * The table found by {@code headerRowPrefix} prefix.
     * If {@code lastRowPrefix} is not null and not empty,
     * the range ends with row containing cell with text starting with {@code lastRowPrefix}.
     * If {@code lastRowPrefix} is null or empty, the range ends with empty row or last row of report page.
     *
     * @param providedTableName the generated name of the table
     * @param headerRowPrefix   the table first row should contain cell which starts with this prefix
     * @param lastRowPrefix     if not null, then the table last row should contain cell which starts with this prefix,
     *                          if null the table ends with empty row or last row of report page
     * @param headerRowsCount   the number of rows to skip after the first header row (inclusive) to reach the table data row
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String headerRowPrefix,
                         @Nullable String lastRowPrefix,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(headerRowPrefix, lastRowPrefix, 0, headerRowsCount - 1),
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table.
     * The table found by {@code headerRowFinder} predicate.
     * If {@code lastRowFinder} is not null, the end of the range is determined by this predicate.
     * If {@code lastRowFinder} is null, the range ends with empty row or last row of report page.
     * The first data row is determined by {@code firstDataRowFinder} predicate.
     *
     * @param providedTableName  the generated name of the table
     * @param headerRowFinder    the table first row should contain cell satisfying predicate
     * @param firstDataRowFinder the first data row should contain cell satisfying predicate
     * @param lastRowFinder      if not null, then the table last row should contain cell satisfying predicate,
     *                           if null the table ends with empty row or last row of report page
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<@Nullable Object> headerRowFinder,
                         Predicate<@Nullable Object> firstDataRowFinder,
                         @Nullable Predicate<@Nullable Object> lastRowFinder,
                         Class<T> headerDescription) {
        TableCellRange headerRange = reportPage.getCellRange(headerRowFinder, firstDataRowFinder, 0, 0);
        int headerRowsCount = 0;
        TableCellRange range = EMPTY_RANGE;
        if (!Objects.equals(headerRange, EMPTY_RANGE)) {
            headerRowsCount = headerRange.getLastRow() - headerRange.getFirstRow();
            range = reportPage.getCellRange(headerRowFinder, lastRowFinder, headerRange.getFirstRow(), headerRowsCount);
        }
        return create(reportPage, providedTableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table.
     * The table found by {@code headerRowFinder} predicate.
     * If {@code lastRowFinder} is not null, the end of the range is determined by this predicate.
     * If {@code lastRowFinder} is null, the range ends with empty row or last row of report page.
     *
     * @param providedTableName the generated name of the table
     * @param headerRowFinder   the table first row should contain cell satisfying predicate
     * @param lastRowFinder     if not null, then the table last row should contain cell satisfying predicate,
     *                          if null the table ends with empty row or last row of report page
     * @param headerRowsCount   the number of rows to skip after the first header row (inclusive) to reach the table data row
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<@Nullable Object> headerRowFinder,
                         @Nullable Predicate<@Nullable Object> lastRowFinder,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(headerRowFinder, lastRowFinder, 0, headerRowsCount - 1),
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table using the cell range from {@code tableRange}.
     * The first table row is the first header row.
     */
    <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 TableCellRange tableRange,
                 Class<T> headerDescription,
                 int headerRowsCount);
}
