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

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * Mutable implementation. Used by {@link AbstractTable#iterator()} and {@link AbstractTable#stream()} to eliminate
 * heap pollution. On each iteration {@link #row} field is updated. Call {@link #clone()} instead of using this object
 * outside iterator or stream, that safer in outside code, because {@link #row} field holds value even if iterator
 * will continue to work.
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

    public int getIntCellValue(TableColumnDescription column) {
        return dao.getIntValue(row, getCellIndex(column));
    }

    public long getLongCellValue(TableColumnDescription column) {
        return dao.getLongValue(row, getCellIndex(column));
    }

    public double getDoubleCellValue(TableColumnDescription column) {
        return dao.getDoubleValue(row, getCellIndex(column));
    }

    public BigDecimal getBigDecimalCellValue(TableColumnDescription column) {
        return dao.getBigDecimalValue(row, getCellIndex(column));
    }

    public String getStringCellValue(TableColumnDescription column) {
        return dao.getStringValue(row, getCellIndex(column));
    }

    public Instant getInstantCellValue(TableColumnDescription column) {
        return dao.getInstantValue(row, getCellIndex(column));
    }

    public LocalDateTime getLocalDateTimeCellValue(TableColumnDescription column) {
        return dao.getLocalDateTimeValue(row, getCellIndex(column));
    }

    private Integer getCellIndex(TableColumnDescription column) {
        return table.getHeaderDescription()
                .get(column.getColumn());
    }

    /**
     * This object is mutable.
     * Clone it if it should be used outside table rows loop block ({@link Table#iterator()} or {@link Table#stream()}).
     * Cloned  object is safe use everywhere, this object should be used oly inside of one iteration
     * of {@link Table#iterator()} or {@link Table#stream()}
     */
    @SuppressWarnings("unchecked")
    public MutableTableRow<T> clone() {
        try {
            return (MutableTableRow<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone " + this.getClass().getName());
        }
    }
}
