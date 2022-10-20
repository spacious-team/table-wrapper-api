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

/**
 * {@link TableRow} subclass can be mutable. Use {@link #clone()} to make copy.
 */
@SuppressWarnings("unused")
public interface TableRow extends ReportPageRow, Cloneable {

    Table getTable();

    @Nullable
    TableCell getCell(TableColumnDescription column);

    /**
     * Returns cell's native value
     */
    @Nullable
    Object getCellValue(TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract int value
     */
    int getIntCellValue(TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract long value
     */
    long getLongCellValue(TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract Double value
     */
    double getDoubleCellValue(TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract BigDecimal value
     */
    BigDecimal getBigDecimalCellValue(TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract string value
     */
    String getStringCellValue(TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract instant value
     */
    Instant getInstantCellValue(TableColumnDescription column);

    /**
     * @throws RuntimeException if method can't extract local date time value
     */
    LocalDateTime getLocalDateTimeCellValue(TableColumnDescription column);

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    @Nullable
    default Object getCellValueOrDefault(TableColumnDescription column, @Nullable Object defaultValue) {
        try {
            return getCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default int getIntCellValueOrDefault(TableColumnDescription column, int defaultValue) {
        try {
            return getIntCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default long getLongCellValueOrDefault(TableColumnDescription column, long defaultValue) {
        try {
            return getLongCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default double getDoubleCellValue(TableColumnDescription column, double defaultValue) {
        try {
            return getDoubleCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default BigDecimal getBigDecimalCellValueOrDefault(TableColumnDescription column, BigDecimal defaultValue) {
        try {
            return getBigDecimalCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default String getStringCellValueOrDefault(TableColumnDescription column, String defaultValue) {
        try {
            return getStringCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default Instant getInstantCellValueOrDefault(TableColumnDescription column, Instant defaultValue) {
        try {
            return getInstantCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    default LocalDateTime getLocalDateTimeCellValueOrDefault(TableColumnDescription column, LocalDateTime defaultValue) {
        try {
            return getLocalDateTimeCellValue(column);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    TableRow clone() throws CloneNotSupportedException;
}
