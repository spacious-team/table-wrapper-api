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
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * Mutable implementation. Used by {@link AbstractTable#iterator()} and {@link AbstractTable#stream()} to eliminate
 * heap pollution. On each iteration {@link #row} field is updated.
 * <br/>
 * When {@code MutableTableRow} should be passed to outside iterator or stream, call {@link #clone()} and
 * use cloned object. Cloned object is safe when used outside iterator or stream, because cloned {@link #row} field
 * holds its original value when {@link AbstractTable#iterator()} continues to loop.
 */
@Data
class MutableTableRow<C, R extends ReportPageRow> implements TableRow {

    private final Table table;
    private final CellDataAccessObject<C, R> dao;

    @Setter(AccessLevel.PACKAGE)
    private volatile R row;

    @Override
    public @Nullable TableCell getCell(TableHeaderColumn column) {
        return getCell(getCellIndex(column));
    }

    @Override
    public @Nullable TableCell getCell(int i) {
        @Nullable TableCell cell = row.getCell(i);
        return updateCellDataAccessObject(cell);
    }

    private @Nullable TableCell updateCellDataAccessObject(@Nullable TableCell cell) {
        if (cell instanceof AbstractTableCell) {
            // hopes, dao is compatible
            //noinspection unchecked
            cell = ((AbstractTableCell<C, CellDataAccessObject<C, R>>) cell).withCellDataAccessObject(dao);
        }
        return cell;
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
    public Iterator<@Nullable TableCell> iterator() {
        Iterator<@Nullable TableCell> it = row.iterator();
        return new Iterator<@Nullable TableCell>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public @Nullable TableCell next() {
                @Nullable TableCell cell = it.next();
                return updateCellDataAccessObject(cell);
            }
        };
    }

    @Override
    public @Nullable Object getCellValue(TableHeaderColumn column) {
        return dao.getValue(row, getCellIndex(column));
    }

    @Override
    public int getIntCellValue(TableHeaderColumn column) {
        return dao.getIntValue(row, getCellIndex(column));
    }

    @Override
    public long getLongCellValue(TableHeaderColumn column) {
        return dao.getLongValue(row, getCellIndex(column));
    }

    @Override
    public double getDoubleCellValue(TableHeaderColumn column) {
        return dao.getDoubleValue(row, getCellIndex(column));
    }

    @Override
    public BigDecimal getBigDecimalCellValue(TableHeaderColumn column) {
        return dao.getBigDecimalValue(row, getCellIndex(column));
    }

    @Override
    public String getStringCellValue(TableHeaderColumn column) {
        return dao.getStringValue(row, getCellIndex(column));
    }

    @Override
    public Instant getInstantCellValue(TableHeaderColumn column) {
        return dao.getInstantValue(row, getCellIndex(column));
    }

    @Override
    public LocalDateTime getLocalDateTimeCellValue(TableHeaderColumn column) {
        return dao.getLocalDateTimeValue(row, getCellIndex(column));
    }

    private int getCellIndex(TableHeaderColumn column) {
        @Nullable Integer cellIndex = table.getHeaderDescription()
                .get(column.getColumn());
        @SuppressWarnings("nullness")
        int cell = requireNonNull(cellIndex, "Cell is not found");
        return cell;
    }

    /**
     * This object is mutable.
     * Clone it if it should be used outside table rows loop block ({@link Table#iterator()} or {@link Table#stream()}).
     * Cloned  object is safe use everywhere, this object should be used oly inside of one iteration
     * of {@link Table#iterator()} or {@link Table#stream()}
     */
    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public MutableTableRow<C, R> clone() {
        return (MutableTableRow<C, R>) super.clone();
    }
}
