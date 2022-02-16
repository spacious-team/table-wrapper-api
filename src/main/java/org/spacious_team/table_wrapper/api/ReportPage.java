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

import java.util.Objects;
import java.util.function.BiPredicate;

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
        return find(value, startRow, endRow, (cell, _value) ->
                cell == _value || (_value != null && Objects.equals(cell, _value.toString())));
    }

    /**
     * @param startRow            search rows start from this
     * @param endRow              search rows excluding this, can handle values greater than real rows count
     * @param stringCellPredicate predicate for testing string containing cell with 'value' arg
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    default TableCellAddress find(Object value, int startRow, int endRow, BiPredicate<String, Object> stringCellPredicate) {
        return find(value, startRow, endRow, 0, Integer.MAX_VALUE, stringCellPredicate);
    }

    /**
     * @param value               searching value
     * @param startRow            search rows start from this
     * @param endRow              search rows excluding this, can handle values greater than real rows count
     * @param startColumn         search columns start from this
     * @param endColumn           search columns excluding this, can handle values greater than real columns count
     * @param stringCellPredicate predicate for testing string containing cell with 'value' arg
     * @return cell address or {@link TableCellAddress#NOT_FOUND}
     */
    TableCellAddress find(Object value, int startRow, int endRow,
                          int startColumn, int endColumn,
                          BiPredicate<String, Object> stringCellPredicate);

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
    default TableCellAddress findByPrefix(String prefix, int startRow, int endRow, int startColumn, int endColumn) {
        if (prefix != null) {
            return find(prefix.trim().toLowerCase(),
                    startRow, endRow, startColumn, endColumn,
                    ReportPageHelper.CELL_STARTS_WITH_IGNORE_CASE);
        }
        return TableCellAddress.NOT_FOUND;
    }

    /**
     * For vertical table of key-value records (table with two columns), search and return value for requested key.
     */
    default Object getNextColumnValue(String firstColumnValuePrefix) {
        TableCellAddress address = findByPrefix(firstColumnValuePrefix);
        for (TableCell cell : getRow(address.getRow())) {
            if (cell != null && cell.getColumnIndex() > address.getColumn()) {
                Object value = cell.getValue();
                if (value != null && (!(value instanceof String) || !((String) value).isBlank())) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * @param i zero-based index
     * @return row object or null is row does not exist
     */
    ReportPageRow getRow(int i);

    /**
     * @return last row contained on this page (zero-based) or -1 if no row exists
     */
    int getLastRowNum();

    default TableCell getCell(TableCellAddress address) {
        return getRow(address.getRow()).getCell(address.getColumn());
    }

    /**
     * Returns table range, table ends with predefined string in one of the row cells.
     */
    default TableCellRange getTableCellRange(String tableName, int headersRowCount, String tableFooterString) {
        if (tableFooterString == null) {
            return TableCellRange.EMPTY_RANGE;
        }
        TableCellAddress startAddress = findByPrefix(tableName);
        if (startAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        TableCellAddress endAddress = findByPrefix(
                tableFooterString,
                startAddress.getRow() + headersRowCount + 1,
                getLastRowNum());
        if (endAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        return new TableCellRange(
                startAddress.getRow(),
                endAddress.getRow(),
                getRow(startAddress.getRow()).getFirstCellNum(),
                getRow(endAddress.getRow()).getLastCellNum());
    }

    /**
     * Returns table range, table ends with empty row or last row of report page.
     */
    default TableCellRange getTableCellRange(String tableName, int headersRowCount) {
        TableCellAddress startAddress = findByPrefix(tableName);
        if (startAddress.equals(TableCellAddress.NOT_FOUND)) {
            return TableCellRange.EMPTY_RANGE;
        }
        int lastRowNum = findEmptyRow(startAddress.getRow() + headersRowCount + 1);
        if (lastRowNum == -1) {
            lastRowNum = getLastRowNum(); // empty row not found
        } else if (lastRowNum <= getLastRowNum()) {
            lastRowNum--; // exclude last row from table
        }
        if (lastRowNum < startAddress.getRow()) {
            lastRowNum = startAddress.getRow();
        }
        return new TableCellRange(
                startAddress.getRow(),
                lastRowNum,
                getRow(startAddress.getRow()).getFirstCellNum(),
                getRow(lastRowNum).getLastCellNum());
    }

    /**
     * Returns zero-based index of empty row.
     * This implementation generates a huge amount of garbage. May be override for improve performance.
     *
     * @param startRow first row for check
     * @return index of first empty row or -1 if not found
     */
    default int findEmptyRow(int startRow) {
        int lastRowNum = startRow;
        LAST_ROW:
        for (int n = getLastRowNum(); lastRowNum <= n; lastRowNum++) {
            ReportPageRow row = getRow(lastRowNum);
            if (row == null || row.getLastCellNum() == -1) {
                return lastRowNum; // all row cells blank
            }
            for (TableCell cell : row) {
                Object value;
                if (!(cell == null
                        || ((value = cell.getValue()) == null)
                        || (value instanceof String) && (value.toString().isEmpty()))) {
                    // not empty
                    continue LAST_ROW;
                }
            }
            return lastRowNum; // all row cells blank
        }
        return -1;
    }

    default Table create(String tableName,
                         String tableFooterString,
                         Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableFooterString, headerDescription);
    }

    default Table create(String tableName,
                         Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, headerDescription);
    }

    default Table create(String tableName,
                         String tableFooterString,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, tableFooterString, headerDescription, headersRowCount);
    }

    default Table create(String tableName,
                         Class<? extends TableColumnDescription> headerDescription,
                         int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .create(this, tableName, headerDescription, headersRowCount);
    }

    default Table createNameless(String firstLineText,
                                 String lastRowString,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineText, lastRowString, headerDescription);
    }

    default Table createNameless(String providedTableName,
                                 String firstLineText,
                                 String lastRowString,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineText, lastRowString, headerDescription, headersRowCount);
    }

    default Table createNameless(String firstLineText,
                                 Class<? extends TableColumnDescription> headerDescription) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, firstLineText, headerDescription);
    }

    default Table createNameless(String providedTableName,
                                 String firstLineText,
                                 Class<? extends TableColumnDescription> headerDescription,
                                 int headersRowCount) {
        return TableFactoryRegistry.get(this)
                .createNameless(this, providedTableName, firstLineText, headerDescription, headersRowCount);
    }
}
