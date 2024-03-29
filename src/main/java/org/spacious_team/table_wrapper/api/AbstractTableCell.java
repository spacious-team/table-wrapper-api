/*
 * Table Wrapper API
 * Copyright (C) 2021  Spacious Team <spacious-team@ya.ru>
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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = PROTECTED)
public abstract class AbstractTableCell<T, D extends CellDataAccessObject<T, ?>> implements TableCell {

    @Getter(PROTECTED)
    private final T cell;
    private final D dao;

    @Override
    public @Nullable Object getValue() {
        return dao.getValue(cell);
    }

    @Override
    public int getIntValue() {
        return dao.getIntValue(cell);
    }

    @Override
    public long getLongValue() {
        return dao.getLongValue(cell);
    }

    @Override
    public double getDoubleValue() {
        return dao.getDoubleValue(cell);
    }

    @Override
    public BigDecimal getBigDecimalValue() {
        return dao.getBigDecimalValue(cell);
    }

    @Override
    public String getStringValue() {
        return dao.getStringValue(cell);
    }

    @Override
    public Instant getInstantValue() {
        return dao.getInstantValue(cell);
    }

    @Override
    public LocalDateTime getLocalDateTimeValue() {
        return dao.getLocalDateTimeValue(cell);
    }

    @Override
    public LocalDateTime getLocalDateTimeValue(ZoneId zoneId) {
        return dao.getLocalDateTimeValue(cell, zoneId);
    }

    public D getCellDataAccessObject() {
        return dao;
    }

    /**
     * Creates new cell object if provided {@link CellDataAccessObject}
     * is different from this class CellDataAccessObject.
     */
    public AbstractTableCell<T, D> withCellDataAccessObject(D dao) {
        return Objects.equals(this.dao, dao) ? this : createWithCellDataAccessObject(dao);
    }

    protected abstract AbstractTableCell<T, D> createWithCellDataAccessObject(D dao);
}
