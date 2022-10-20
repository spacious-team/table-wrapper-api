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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public interface TableCell {

    /**
     * Zero-based column index
     */
    int getColumnIndex();

    @Nullable
    Object getValue();

    /**
     * @throws RuntimeException if can't extract int value
     */
    int getIntValue();

    /**
     * @throws RuntimeException if can't extract long value
     */
    long getLongValue();

    /**
     * @throws RuntimeException if can't extract Double value
     */
    Double getDoubleValue();

    /**
     * @throws RuntimeException if can't extract BigDecimal value
     */
    BigDecimal getBigDecimalValue();

    /**
     * @throws RuntimeException if can't extract string value
     */
    String getStringValue();

    /**
     * @throws RuntimeException if can't extract instant value
     */
    Instant getInstantValue();

    /**
     * @throws RuntimeException if can't extract local date time value
     */
    LocalDateTime getLocalDateTimeValue();

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @Nullable
    default Object getValueOrDefault(@Nullable Object defaultValue) {
        try {
            return getValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default int getIntValueOrDefault(int defaultValue) {
        try {
            return getIntValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default long getLongValueOrDefault(long defaultValue) {
        try {
            return getLongValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default double getDoubleValue(double defaultValue) {
        try {
            return getDoubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default BigDecimal getBigDecimalValueOrDefault(BigDecimal defaultValue) {
        try {
            return getBigDecimalValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default String getStringValueOrDefault(String defaultValue) {
        try {
            return getStringValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default Instant getInstantValueOrDefault(Instant defaultValue) {
        try {
            return getInstantValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default LocalDateTime getLocalDateTimeValueOrDefault(LocalDateTime defaultValue) {
        try {
            return getLocalDateTimeValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
