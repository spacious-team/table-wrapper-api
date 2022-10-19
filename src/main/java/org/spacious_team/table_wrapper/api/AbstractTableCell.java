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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTableCell<T> implements TableCell {

    private final T cell;
    private final CellDataAccessObject<T, ?> dao;

    @Nullable
    @Override
    public Object getValue() {
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
    public Double getDoubleValue() {
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
}
