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

public interface TableFactory {

    boolean canHandle(ReportPage reportPage);

    /**
     * Creates a table which starts with name followed by header
     * and ends with row containing cell with text starting with {@code lastRowPrefix}.
     *
     * @param tableName     table name row should contain cell which starts with given text
     * @param lastRowPrefix table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 String lastRowPrefix,
                 Class<T> headerDescription) {
        return create(reportPage, tableName, lastRowPrefix, headerDescription, 1);
    }

    /**
     * Creates a table which starts with name followed by header and ends with empty row or last row of report page.
     *
     * @param tableName table name row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 Class<T> headerDescription) {
        return create(reportPage, tableName, headerDescription, 1);
    }

    /**
     * Creates a table which starts with name followed by header
     * and ends with row containing cell with text starting with {@code lastRowPrefix}.
     *
     * @param tableName     table name row should contain cell which starts with given text
     * @param lastRowPrefix table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 String lastRowPrefix,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        return create(reportPage,
                tableName,
                reportPage.getCellRange(tableName, lastRowPrefix, headerRowsCount),
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table which starts with name followed by header and ends with empty row or last row of report page.
     *
     * @param tableName table name row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        return create(reportPage,
                tableName,
                reportPage.getCellRange(tableName, headerRowsCount),
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table. Table name row and last row is determined by predicate.
     *
     * @param tableNameFinder table name row should contain cell satisfying predicate
     * @param lastRowFinder   table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 Predicate<@Nullable Object> lastRowFinder,
                 Class<T> headerDescription) {
        return create(reportPage, tableNameFinder, lastRowFinder, headerDescription, 1);
    }

    /**
     * Creates a table. Table name row is determined by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param tableNameFinder table name row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 Class<T> headerDescription) {
        return create(reportPage, tableNameFinder, headerDescription, 1);
    }

    /**
     * Creates a table. Table name row and last row is determined by predicate.
     *
     * @param tableNameFinder table name row should contain cell satisfying predicate
     * @param lastRowFinder   table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 Predicate<@Nullable Object> lastRowFinder,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableNameFinder, lastRowFinder, headerRowsCount);
        String tableName = TableFactoryHelper.getTableName(reportPage, tableNameFinder, range);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table. Table name row is determined by predicate,
     * and the table ends at either an empty row or the last row of the report page.
     *
     * @param tableNameFinder table name row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 Predicate<@Nullable Object> tableNameFinder,
                 Class<T> headerDescription,
                 int headerRowsCount) {
        TableCellRange range = reportPage.getCellRange(tableNameFinder, headerRowsCount);
        String tableName = TableFactoryHelper.getTableName(reportPage, tableNameFinder, range);
        return create(reportPage, tableName, range, headerDescription, headerRowsCount);
    }

    /**
     * Creates a table without name which starts with header
     * and ends with row containing cell with text starting with {@code lastRowPrefix}.
     *
     * @param firstRowPrefix table first row should contain cell which starts with given text
     * @param lastRowPrefix  table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String firstRowPrefix,
                         String lastRowPrefix,
                         Class<T> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowPrefix, lastRowPrefix, headerDescription, 1);
    }

    /**
     * Creates a table without name which starts with header and ends with empty row or last row of report page.
     *
     * @param firstRowPrefix table first row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String firstRowPrefix,
                         Class<T> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowPrefix, headerDescription, 1);
    }

    /**
     * Creates a table with a predefined name which starts with header
     * and ends with row containing cell with text starting  {@code lastRowPrefix}.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowPrefix    table first row should contain cell which starts with given text
     * @param lastRowPrefix     table last row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String firstRowPrefix,
                         String lastRowPrefix,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(firstRowPrefix, lastRowPrefix, headerRowsCount)
                        .addRowsToTop(1), // add fantom first line for provided table name
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table with a predefined name which starts with header and ends with empty row or last row of report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowPrefix    table first row should contain cell which starts with given text
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String firstRowPrefix,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(firstRowPrefix, headerRowsCount)
                        .addRowsToTop(1), // add fantom first line for provided table name
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table without name. Table first and last row is determined by predicate.
     *
     * @param firstRowFinder table first row should contain cell satisfying predicate
     * @param lastRowFinder  table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         Predicate<@Nullable Object> firstRowFinder,
                         Predicate<@Nullable Object> lastRowFinder,
                         Class<T> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, lastRowFinder, headerDescription, 1);
    }

    /**
     * Creates a table without name. Table first row is determined by predicate,
     * and the table ends at either an empty row or the last row of the report page.
     *
     * @param firstRowFinder table first row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         Predicate<@Nullable Object> firstRowFinder,
                         Class<T> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, headerDescription, 1);
    }

    /**
     * Creates a table with a predefined name. Table first and last row is determined by predicate.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowFinder    table first row should contain cell satisfying predicate
     * @param lastRowFinder     table last row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<@Nullable Object> firstRowFinder,
                         Predicate<@Nullable Object> lastRowFinder,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(firstRowFinder, lastRowFinder, headerRowsCount)
                        .addRowsToTop(1), // add fantom first line for provided table name
                headerDescription,
                headerRowsCount);
    }

    /**
     * Creates a table with a predefined name. Table first row is determined by a predicate,
     * and the table ends at either an empty row or the last row of the report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowFinder    table first row should contain cell satisfying predicate
     */
    default <T extends Enum<T> & TableHeaderColumn>
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<@Nullable Object> firstRowFinder,
                         Class<T> headerDescription,
                         int headerRowsCount) {
        return create(reportPage,
                providedTableName,
                reportPage.getCellRange(firstRowFinder, headerRowsCount)
                        .addRowsToTop(1), // add fantom first line for provided table name
                headerDescription,
                headerRowsCount);
    }


    <T extends Enum<T> & TableHeaderColumn>
    Table create(ReportPage reportPage,
                 String tableName,
                 TableCellRange tableRange,
                 Class<T> headerDescription,
                 int headerRowsCount);
}
