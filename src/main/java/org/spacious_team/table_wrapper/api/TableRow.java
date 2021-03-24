/*
 * Table Wrapper API
 * Copyright (C) 2020  Vitalii Ananev <an-vitek@ya.ru>
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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;

@RequiredArgsConstructor
public class TableRow extends ReportPageRow {

    @Getter
    private final Table table;
    private final ReportPageRow row;

    public TableCell getCell(TableColumnDescription column) {
        int i = table.getHeaderDescription()
                .get(column.getColumn());
        return getCell(i);
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
    public boolean rowContains(Object value) {
        return row.rowContains(value);
    }

    @Override
    public Iterator<TableCell> iterator() {
        return row.iterator();
    }

    public Object getCellValue(TableColumnDescription column) {
        return getCell(column).getValue();
    }

    /**
     * @throws RuntimeException if can't extract int value
     */
    public int getIntCellValue(TableColumnDescription column) {
        return getCell(column).getIntValue();
    }

    /**
     * @throws RuntimeException if can't extract long value
     */
    public long getLongCellValue(TableColumnDescription column) {
        return getCell(column).getLongValue();
    }

    /**
     * @throws RuntimeException if can't extract Double value
     */
    public Double getDoubleCellValue(TableColumnDescription column) {
        return getCell(column).getDoubleValue();
    }

    /**
     * @throws RuntimeException if can't extract BigDecimal value
     */
    public BigDecimal getBigDecimalCellValue(TableColumnDescription column) {
        return getCell(column).getBigDecimalValue();
    }

    /**
     * @throws RuntimeException if can't extract string value
     */
    public String getStringCellValue(TableColumnDescription column) {
        return getCell(column).getStringValue();
    }

    /**
     * @throws RuntimeException if can't extract instant value
     */
    public Instant getInstantCellValue(TableColumnDescription column) {
        return getCell(column).getInstantValue();
    }

    /**
     * @throws RuntimeException if can't extract local date time value
     */
    public LocalDateTime getLocalDateTimeCellValue(TableColumnDescription column) {
        return getCell(column).getLocalDateTimeValue();
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public Object getCellValueOrDefault(TableColumnDescription column, Object defaultValue) {
        try {
            return getCell(column).getValueOrDefault(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public int getIntCellValueOrDefault(TableColumnDescription column, int defaultValue) {
        try {
            return getCell(column).getIntValueOrDefault(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public long getLongCellValueOrDefault(TableColumnDescription column, long defaultValue) {
        try {
            return getCell(column).getLongValueOrDefault(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public double getDoubleCellValue(TableColumnDescription column, double defaultValue) {
        try {
            return getCell(column).getDoubleValue(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public BigDecimal getBigDecimalCellValueOrDefault(TableColumnDescription column, BigDecimal defaultValue) {
        try {
            return getCell(column).getBigDecimalValueOrDefault(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public String getStringCellValueOrDefault(TableColumnDescription column, String defaultValue) {
        try {
            return getCell(column).getStringValueOrDefault(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public Instant getInstantCellValueOrDefault(TableColumnDescription column, Instant defaultValue) {
        try {
            return getCell(column).getInstantValueOrDefault(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @return return cell value or defaultValue if the cell is missing or the type does not match the expected
     */
    public LocalDateTime getLocalDateTimeCellValueOrDefault(TableColumnDescription column, LocalDateTime defaultValue) {
        try {
            return getCell(column).getLocalDateTimeValueOrDefault(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
