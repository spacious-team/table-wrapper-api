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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@RequiredArgsConstructor(staticName = "of")
public class EmptyTableCell implements TableCell {
    @Getter
    private final int columnIndex;

    @Override
    public @Nullable Object getValue() {
        return null;
    }

    @Override
    public int getIntValue() {
        throw new NullPointerException("Can't get int value");
    }

    @Override
    public long getLongValue() {
        throw new NullPointerException("Can't get long value");
    }

    @Override
    public Double getDoubleValue() {
        throw new NullPointerException("Can't get double value");
    }

    @Override
    public BigDecimal getBigDecimalValue() {
        throw new NullPointerException("Can't get BigDecimal value");
    }

    @Override
    public String getStringValue() {
        throw new NullPointerException("Can't get String value");
    }

    @Override
    public Instant getInstantValue() {
        throw new NullPointerException("Can't get Instant value");
    }

    @Override
    public LocalDateTime getLocalDateTimeValue() {
        throw new NullPointerException("Can't get LocalDateTime value");
    }
}
