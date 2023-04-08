/*
 * Table Wrapper API
 * Copyright (C) 2022  Spacious Team <spacious-team@ya.ru>
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

import lombok.Data;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;

import static java.util.Collections.emptyIterator;

@Data
class EmptyTableRow implements TableRow {
    private static final String CELL_NOT_FOUND = "Cell not found";
    private final Table table;
    private final int rowNum;

    @Override
    public @Nullable TableCell getCell(TableHeaderColumn column) {
        return null;
    }

    @Override
    public @Nullable TableCell getCell(int i) {
        return null;
    }

    @Override
    public int getFirstCellNum() {
        return -1;
    }

    @Override
    public int getLastCellNum() {
        return -1;
    }

    @Override
    public boolean rowContains(@Nullable Object expected) {
        return false;
    }

    @Override
    public Iterator<TableCell> iterator() {
        return emptyIterator();
    }

    @Override
    public @Nullable Object getCellValue(TableHeaderColumn column) {
        return null;
    }

    @Override
    public int getIntCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public long getLongCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public double getDoubleCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public BigDecimal getBigDecimalCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public String getStringCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public Instant getInstantCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public LocalDateTime getLocalDateTimeCellValue(TableHeaderColumn column) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    public LocalDateTime getLocalDateTimeCellValue(TableHeaderColumn column, ZoneId zoneId) {
        throw new NullPointerException(CELL_NOT_FOUND);
    }

    @Override
    @SneakyThrows
    public TableRow clone() {
        return (TableRow) super.clone();
    }
}
