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

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * Mutable implementation. Used by {@link AbstractTable#iterator()} and {@link AbstractTable#stream()} to eliminate
 * heap pollution. Use {@link #clone()} for using variable outside iterator or stream.
 */
@Data
class MutableTableRow<T extends ReportPageRow> implements TableRow {

    private final Table table;
    private final CellDataAccessObject<?, T> dao;

    @Setter(AccessLevel.PACKAGE)
    private volatile T row;

    public TableCell getCell(TableColumnDescription column) {
        return getCell(getCellIndex(column));
    }

    @Override
    public TableCell getCell(int i) {
        return row.getCell(i);
    }

    @Override
    public int getRowNum() {
        return row.getRowNum();
    }

    @Override
    public int getFirstCellNum() {
        return row.getFirstCellNum();
    }

    @Override
    public int getLastCellNum() {
        return row.getLastCellNum();
    }

    @Override
    public boolean rowContains(Object expected) {
        return row.rowContains(expected);
    }

    @Override
    public Iterator<TableCell> iterator() {
        return row.iterator();
    }

    public Object getCellValue(TableColumnDescription column) {
        return dao.getValue(row, getCellIndex(column));
    }

    /**
     * @throws RuntimeException if can't extract int value
     */
    public int getIntCellValue(TableColumnDescription column) {
        return dao.getIntValue(row, getCellIndex(column));
    }

    /**
     * @throws RuntimeException if can't extract long value
     */
    public long getLongCellValue(TableColumnDescription column) {
        return dao.getLongValue(row, getCellIndex(column));
    }

    /**
     * @throws RuntimeException if can't extract Double value
     */
    public double getDoubleCellValue(TableColumnDescription column) {
        return dao.getDoubleValue(row, getCellIndex(column));
    }

    /**
     * @throws RuntimeException if can't extract BigDecimal value
     */
    public BigDecimal getBigDecimalCellValue(TableColumnDescription column) {
        return dao.getBigDecimalValue(row, getCellIndex(column));
    }

    /**
     * @throws RuntimeException if can't extract string value
     */
    public String getStringCellValue(TableColumnDescription column) {
        return dao.getStringValue(row, getCellIndex(column));
    }

    /**
     * @throws RuntimeException if can't extract instant value
     */
    public Instant getInstantCellValue(TableColumnDescription column) {
        return dao.getInstantValue(row, getCellIndex(column));
    }

    /**
     * @throws RuntimeException if can't extract local date time value
     */
    public LocalDateTime getLocalDateTimeCellValue(TableColumnDescription column) {
        return dao.getLocalDateTimeValue(row, getCellIndex(column));
    }

    private Integer getCellIndex(TableColumnDescription column) {
        return table.getHeaderDescription()
                .get(column.getColumn());
    }

    /**
     * Object is mutable.
     * Clone it if it should be outside table rows loop block, {@link Table#iterator()} or {@link Table#stream()}
     */
    @SuppressWarnings("unchecked")
    public MutableTableRow<T> clone() {
        try {
            MutableTableRow<T> tableRow = (MutableTableRow<T>) super.clone();
            tableRow.setRow(row);
            return tableRow;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone " + this.getClass().getName());
        }
    }
}
