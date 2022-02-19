/*
 * Table Wrapper API
 * Copyright (C) 2020  Vitalii Ananev <spacious-team@ya.ru>
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

import java.util.function.Predicate;

public interface TableFactory {

    boolean canHandle(ReportPage reportPage);

    /**
     * Creates table which starts with name followed by header and ends with row containing cell with text starting with
     * given string.
     *
     * @param tableName     table name's row should contain cell which starts with given text
     * @param lastRowString table's last row should contain cell which starts with given text
     */
    default Table create(ReportPage reportPage,
                         String tableName,
                         String lastRowString,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableName, lastRowString, headerDescription, 1);
    }

    /**
     * Creates table which starts with name followed by header and ends with empty row or last row of report page.
     *
     * @param tableName table name's row should contain cell which starts with given text
     */
    default Table create(ReportPage reportPage,
                         String tableName,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableName, headerDescription, 1);
    }

    /**
     * Creates table which starts with name followed by header and ends with row containing cell with text starting with
     * given string.
     *
     * @param tableName     table name's row should contain cell which starts with given text
     * @param lastRowString table's last row should contain cell which starts with given text
     */
    Table create(ReportPage reportPage,
                 String tableName,
                 String lastRowString,
                 Class<? extends TableColumnDescription> headerDescription,
                 int headersRowCount);

    /**
     * Creates table which starts with name followed by header and ends with empty row or last row of report page.
     *
     * @param tableName table name's row should contain cell which starts with given text
     */
    Table create(ReportPage reportPage,
                 String tableName,
                 Class<? extends TableColumnDescription> headerDescription,
                 int headersRowCount);

    /**
     * Creates table. Table name containing row and last row will be found by predicate.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     * @param lastRowFinder   table's last row should contain cell satisfying predicate
     */
    default Table create(ReportPage reportPage,
                         Predicate<Object> tableNameFinder,
                         Predicate<Object> lastRowFinder,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableNameFinder, lastRowFinder, headerDescription, 1);
    }

    /**
     * Creates table. Table name containing row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     */
    default Table create(ReportPage reportPage,
                         Predicate<Object> tableNameFinder,
                         Class<? extends TableColumnDescription> headerDescription) {
        return create(reportPage, tableNameFinder, headerDescription, 1);
    }

    /**
     * Creates table. Table name containing row and last row will be found by predicate.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     * @param lastRowFinder   table's last row should contain cell satisfying predicate
     */
    Table create(ReportPage reportPage,
                 Predicate<Object> tableNameFinder,
                 Predicate<Object> lastRowFinder,
                 Class<? extends TableColumnDescription> headerDescription,
                 int headersRowCount);

    /**
     * Creates table. Table name containing row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param tableNameFinder table name containing row should contain cell satisfying predicate
     */
    Table create(ReportPage reportPage,
                 Predicate<Object> tableNameFinder,
                 Class<? extends TableColumnDescription> headerDescription,
                 int headersRowCount);

    /**
     * Creates table without name which starts with header and ends with row containing cell with text starting with
     * given string.
     *
     * @param firstRowString table first row should contain cell which starts with given text
     * @param lastRowString  table's last row should contain cell which starts with given text
     */
    default Table createNameless(ReportPage reportPage,
                                 String firstRowString,
                                 String lastRowString,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowString, lastRowString, headerDescription, 1);
    }

    /**
     * Creates table without name which starts with header and ends with empty row or last row of report page.
     *
     * @param firstRowString table first row should contain cell which starts with given text
     */
    default Table createNameless(ReportPage reportPage,
                                 String firstRowString,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowString, headerDescription, 1);
    }

    /**
     * Creates table with predefined name which starts with header and ends with row containing cell with text starting
     * with given string.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowString    table first row should contain cell which starts with given text
     * @param lastRowString     table's last row should contain cell which starts with given text
     */
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String firstRowString,
                         String lastRowString,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount);

    /**
     * Creates table with predefined name which starts with header and ends with empty row or last row of report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowString    table first row should contain cell which starts with given text
     */
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         String firstRowString,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount);

    /**
     * Creates table without name. Table first and last row will be found by predicate.
     *
     * @param firstRowFinder table first row should contain cell satisfying predicate
     * @param lastRowFinder  table's last row should contain cell satisfying predicate
     */
    default Table createNameless(ReportPage reportPage,
                                 Predicate<Object> firstRowFinder,
                                 Predicate<Object> lastRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, lastRowFinder, headerDescription, 1);
    }

    /**
     * Creates table without name. Table first row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param firstRowFinder table first row should contain cell satisfying predicate
     */
    default Table createNameless(ReportPage reportPage,
                                 Predicate<Object> firstRowFinder,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return createNameless(reportPage, "undefined", firstRowFinder, headerDescription, 1);
    }

    /**
     * Creates table with predefined name. Table first and last row will be found by predicate.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowFinder    table first row should contain cell satisfying predicate
     * @param lastRowFinder     table's last row should contain cell satisfying predicate
     */
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<Object> firstRowFinder,
                         Predicate<Object> lastRowFinder,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount);

    /**
     * Creates table with predefined name. Table first row will be found by predicate, table ends by empty row
     * or last row of report page.
     *
     * @param providedTableName predefined (not existing in reportPage) table name
     * @param firstRowFinder    table first row should contain cell satisfying predicate
     */
    Table createNameless(ReportPage reportPage,
                         String providedTableName,
                         Predicate<Object> firstRowFinder,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount);
}
