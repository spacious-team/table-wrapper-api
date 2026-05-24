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
     * Creates a table which starts with name followed by header
     * and ends with row containing cell with text starting with {@code lastRowPrefix}.
     * The first data row is determined by cell with text starting with {@code firstDataRowPrefix}.
     * Table name spans {@code tableNameRowCount} rows.
     *
     * @param tableName          table name row should contain cell which starts with given text
     * @param tableNameRowCount  the number of rows to skip after the table name
     * @param firstDataRowPrefix first data row should contain cell which starts with given text
     * @param lastRowPrefix      table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 int tableNameRowCount,
                 String firstDataRowPrefix,
                 String lastRowPrefix,
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
     * Creates a table which starts with name followed by header
     * and ends with row containing cell with text starting with {@code lastRowPrefix}.
     * Table name spans {@code tableNameRowCount} rows.
     *
     * @param tableName         table name row should contain cell which starts with given text
     * @param tableNameRowCount the number of rows to skip after the table name
     * @param lastRowPrefix     table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 int tableNameRowCount,
                 String lastRowPrefix,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableName, lastRowPrefix,
                        0, tableNameRowCount + headerRowsCount - 1)
                .addRowsToTop(-tableNameRowCount);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table which starts with name followed by header and ends with empty row or last row of report page.
     * Table name spans {@code tableNameRowCount} rows.
     *
     * @param tableName         table name row should contain cell which starts with given text
     * @param tableNameRowCount the number of rows to skip after the table name
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 int tableNameRowCount,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableName, 0,
                        tableNameRowCount + headerRowsCount - 1)
                .addRowsToTop(-tableNameRowCount);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table. Table name row, first data row and last row is determined by predicate.
     * Table name spans {@code tableNameRowCount} rows.
     *
     * @param tableNameFinder    table name row should contain cell satisfying predicate
     * @param tableNameRowCount  the number of rows to skip after the table name
     * @param firstDataRowFinder first data row should contain cell satisfying predicate
     * @param lastRowFinder      table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 int tableNameRowCount,
                 Predicate<@Nullable Object> firstDataRowFinder,
                 Predicate<@Nullable Object> lastRowFinder,
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
     * Creates a table. Table name row and last row is determined by predicate.
     * Table name spans {@code tableNameRowCount} rows.
     *
     * @param tableNameFinder   table name row should contain cell satisfying predicate
     * @param tableNameRowCount the number of rows to skip after the table name
     * @param lastRowFinder     table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 int tableNameRowCount,
                 Predicate<@Nullable Object> lastRowFinder,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableNameFinder, lastRowFinder,
                        0, tableNameRowCount + headerRowsCount - 1)
                .addRowsToTop(-tableNameRowCount);
        String tableName = TableFactoryHelper.getTableName(reportPage, tableNameFinder, range);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table. Table name row is determined by predicate,
     * and the table ends at either an empty row or the last row of the report page.
     * Table name spans {@code tableNameRowCount} rows.
     *
     * @param tableNameFinder   table name row should contain cell satisfying predicate
     * @param tableNameRowCount the number of rows to skip after the table name
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 int tableNameRowCount,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableNameFinder,
                        0, tableNameRowCount + headerRowsCount - 1)
                .addRowsToTop(-tableNameRowCount);
        String tableName = TableFactoryHelper.getTableName(reportPage, tableNameFinder, range);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table with a predefined name which starts with header
     * and ends with row containing cell with text starting with {@code lastRowPrefix}.
     * The first data row is determined by cell with text starting with {@code firstDataRowPrefix}.
     *
     * @param headerRowPrefix    table first row should contain cell which starts with given text
     * @param firstDataRowPrefix table first data row should contain cell which starts with given text
     * @param lastRowPrefix      table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String headerRowPrefix,
                         String firstDataRowPrefix,
                         String lastRowPrefix,
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
     * Creates a table with a predefined name which starts with header
     * and ends with row containing cell with text starting  {@code lastRowPrefix}.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param headerRowPrefix   table first row should contain cell which starts with given text
     * @param lastRowPrefix     table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String headerRowPrefix,
                         String lastRowPrefix,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(headerRowPrefix, lastRowPrefix, 0, headerRowsCount - 1),
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table with a predefined name which starts with header and ends with empty row or last row of report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param headerRowPrefix   table first row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String headerRowPrefix,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(headerRowPrefix, 0, headerRowsCount - 1),
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table with a predefined name. Table header row, first data row and last row is determined by predicate.
     *
     * @param providedTableName  predefined (not existing in reportPage) table name
     * @param headerRowFinder    table first row should contain cell satisfying predicate
     * @param firstDataRowFinder first data row should contain cell satisfying predicate
     * @param lastRowFinder      table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<@Nullable Object> headerRowFinder,
                         Predicate<@Nullable Object> firstDataRowFinder,
                         Predicate<@Nullable Object> lastRowFinder,
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
     * Creates a table with a predefined name. Table first and last row is determined by predicate.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param headerRowFinder   table first row should contain cell satisfying predicate
     * @param lastRowFinder     table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<@Nullable Object> headerRowFinder,
                         Predicate<@Nullable Object> lastRowFinder,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(headerRowFinder, lastRowFinder, 0, headerRowsCount - 1),
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table with a predefined name. Table first row is determined by a predicate,
     * and the table ends at either an empty row or the last row of the report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param headerRowFinder   table first row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<@Nullable Object> headerRowFinder,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(headerRowFinder, 0, headerRowsCount - 1),
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
