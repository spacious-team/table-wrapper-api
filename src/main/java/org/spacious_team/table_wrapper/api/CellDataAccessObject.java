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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @apiNote Impl may have parameters that affect how the value is parsed,
 * for example DataTimeFormat that changes behavior of date time value parser.
 */
public interface CellDataAccessObject<C, R extends ReportPageRow> {

    ZoneId defaultZoneId = ZoneId.systemDefault();
    Pattern spacePattern = Pattern.compile("\\s");
    String NO_CELL_VALUE_EXCEPTION_MESSAGE = "Cell doesn't contains value";

    C getCell(R row, Integer cellIndex);

    Object getValue(C cell);

    /**
     * @throws RuntimeException if method can't extract int value
     */
    default int getIntValue(C cell) {
        return (int) getLongValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract long value
     */
    default long getLongValue(C cell) {
        Object value = getValue(cell);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value != null) {
            return Long.parseLong(spacePattern.matcher((CharSequence) value).replaceAll(""));
        } else {
            throw new NullPointerException(NO_CELL_VALUE_EXCEPTION_MESSAGE);
        }
    }

    /**
     * @throws RuntimeException if method can't extract Double value
     */
    default double getDoubleValue(C cell) {
        Object value = getValue(cell);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value != null) {
            String str = spacePattern.matcher((CharSequence) value).replaceAll("");
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                if (str.indexOf(',') != -1) {
                    return Double.parseDouble(str.replace(',', '.'));
                } else if (str.indexOf('.') != -1) {
                    return Double.parseDouble(str.replace('.', ','));
                }
                throw e;
            }
        } else {
            throw new NullPointerException(NO_CELL_VALUE_EXCEPTION_MESSAGE);
        }
    }

    /**
     * @throws RuntimeException if method can't extract BigDecimal value
     * @see <a href="https://stackoverflow.com/questions/6787142/bigdecimal-equals-versus-compareto">Stack overflow</a>
     * for BigDecimal values equality
     */
    default BigDecimal getBigDecimalValue(C cell) {
        String number = getStringValue(cell);
        number = number.replace(',', '.');
        return (Objects.equals(number, "0") || Objects.equals(number, "0.0")) ?
                BigDecimal.ZERO : new BigDecimal(number);
    }

    /**
     * @throws RuntimeException if method can't extract string value
     */
    default String getStringValue(C cell) {
        return getValue(cell).toString();
    }

    /**
     * @throws RuntimeException if method can't extract instant value
     */
    Instant getInstantValue(C cell);

    /**
     * @throws RuntimeException if method can't extract local date time value
     */
    default LocalDateTime getLocalDateTimeValue(C cell) {
        return getInstantValue(cell)
                .atZone(defaultZoneId)
                .toLocalDateTime();
    }

    default Object getValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract int value
     */
    default int getIntValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getIntValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract long value
     */
    default long getLongValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getLongValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract Double value
     */
    default double getDoubleValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getDoubleValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract BigDecimal value
     */
    default BigDecimal getBigDecimalValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getBigDecimalValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract string value
     */
    default String getStringValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getStringValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract instant value
     */
    default Instant getInstantValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getInstantValue(cell);
    }

    /**
     * @throws RuntimeException if method can't extract local date time value
     */
    default LocalDateTime getLocalDateTimeValue(R row, Integer cellIndex) {
        C cell = getCell(row, cellIndex);
        return getLocalDateTimeValue(cell);
    }

}
