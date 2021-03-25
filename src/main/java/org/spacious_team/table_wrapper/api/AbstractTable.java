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
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@ToString(of = {"tableName"})
public abstract class AbstractTable<R extends ReportPageRow> implements Table {

    private static final Path unknown = Path.of("unknown");
    protected final AbstractReportPage<R> reportPage;
    protected final String tableName;
    @Getter
    protected final TableCellRange tableRange;
    @Getter
    private final Map<TableColumn, Integer> headerDescription;
    @Getter
    protected final boolean empty;
    /**
     * Offset of first data row. First table row is a header.
     */
    private final int dataRowOffset;
    /**
     * Set to true if last table row contains total information. Default is false.
     */
    @Setter
    private boolean isLastTableRowContainsTotalData = false;


    protected AbstractTable(AbstractReportPage<R> reportPage, String tableName, TableCellRange tableRange,
                            Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
        this.reportPage = reportPage;
        this.tableName = tableName;
        this.tableRange = tableRange;
        this.dataRowOffset = 1 + headersRowCount; // table_name + headersRowCount
        this.empty = tableRange.equals(TableCellRange.EMPTY_RANGE) ||
                ((tableRange.getLastRow() - tableRange.getFirstRow()) <= headersRowCount);
        this.headerDescription = empty ?
                Collections.emptyMap() :
                getHeaderDescription(reportPage, tableRange, headerDescription, headersRowCount);
    }

    private Map<TableColumn, Integer> getHeaderDescription(AbstractReportPage<R> reportPage, TableCellRange tableRange,
                                                           Class<? extends TableColumnDescription> headerDescription,
                                                           int headersRowCount) {
        Map<TableColumn, Integer> columnIndices = new HashMap<>();
        ReportPageRow[] headerRows = new ReportPageRow[headersRowCount];
        for (int i = 0; i < headersRowCount; i++) {
            headerRows[i] = reportPage.getRow(tableRange.getFirstRow() + 1 + i);
        }
        TableColumn[] columns = Arrays.stream(headerDescription.getEnumConstants())
                .map(TableColumnDescription::getColumn)
                .toArray(TableColumn[]::new);
        for (TableColumn column : columns) {
            columnIndices.put(column, column.getColumnIndex(headerRows));
        }
        return Collections.unmodifiableMap(columnIndices);
    }

    /**
     * Extracts exactly one object from excel row
     */
    public <T> List<T> getData(Function<TableRow, T> rowExtractor) {
        return getDataCollection(unknown, (row, data) -> {
            T result = rowExtractor.apply(row);
            if (result != null) {
                data.add(result);
            }
        });
    }

    public <T> List<T> getData(Path file, Function<TableRow, T> rowExtractor) {
        return getDataCollection(file, (row, data) -> {
            T result = rowExtractor.apply(row);
            if (result != null) {
                data.add(result);
            }
        });
    }

    /**
     * Extracts objects from excel table without duplicate objects handling (duplicated row are both will be returned)
     */
    public <T> List<T> getDataCollection(Function<TableRow, Collection<T>> rowExtractor) {
        return getDataCollection(unknown, (row, data) -> {
            Collection<T> result = rowExtractor.apply(row);
            if (result != null) {
                data.addAll(result);
            }
        });
    }

    public <T> List<T> getDataCollection(Path file, Function<TableRow, Collection<T>> rowExtractor) {
        return getDataCollection(file, (row, data) -> {
            Collection<T> result = rowExtractor.apply(row);
            if (result != null) {
                data.addAll(result);
            }
        });
    }

    /**
     * Extracts objects from excel table with duplicate objects handling logic
     */
    public <T> List<T> getDataCollection(Path file, Function<TableRow, Collection<T>> rowExtractor,
                                         BiPredicate<T, T> equalityChecker,
                                         BiFunction<T, T, Collection<T>> mergeDuplicates) {
        return getDataCollection(file, (row, data) -> {
            Collection<T> result = rowExtractor.apply(row);
            if (result != null) {
                for (T r : result) {
                    addWithEqualityChecker(r, data, equalityChecker, mergeDuplicates);
                }
            }
        });
    }

    private <T> List<T> getDataCollection(Path file, BiConsumer<TableRow, Collection<T>> rowHandler) {
        List<T> data = new ArrayList<>();
        for (TableRow row : this) {
            if (row != null) {
                try {
                    rowHandler.accept(row, data);
                } catch (Exception e) {
                    log.warn("Не могу распарсить таблицу '{}' в файле {}, строка {}",
                            tableName, file.getFileName(), row.getRowNum() + 1, e);
                }
            }
        }
        return data;
    }

    public static <T> void addWithEqualityChecker(T element,
                                                  Collection<T> collection,
                                                  BiPredicate<T, T> equalityChecker,
                                                  BiFunction<T, T, Collection<T>> duplicatesMerger) {
        T equalsObject = null;
        for (T e : collection) {
            if (equalityChecker.test(e, element)) {
                equalsObject = e;
                break;
            }
        }
        if (equalsObject != null) {
            collection.remove(equalsObject);
            collection.addAll(duplicatesMerger.apply(equalsObject, element));
        } else {
            collection.add(element);
        }
    }

    /**
     * {@link TableRow} impl is mutable.
     * For performance issue same object with changed state is provided in each loop cycle.
     * Call {@link TableRow#clone()} if you want use row object outside stream() block.
     */
    @Override
    public Stream<TableRow> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * {@link TableRow} impl is mutable.
     * For performance issue same object with changed state is provided in each loop cycle.
     * Call {@link TableRow#clone()} if you want use row object outside iterator() block.
     */
    @Override
    public Iterator<TableRow> iterator() {
        return new TableIterator();
    }

    protected class TableIterator implements Iterator<TableRow> {
        private final MutableTableRow<R> tableRow =
                new MutableTableRow<>(AbstractTable.this, getCellDataAccessObject());
        private final int dataRowsCount = tableRange.getLastRow() - tableRange.getFirstRow()
                - dataRowOffset
                + (isLastTableRowContainsTotalData ? 0 : 1);
        private int cnt = 0;

        @Override
        public boolean hasNext() {
            return cnt < dataRowsCount;
        }

        @Override
        public TableRow next() {
            R row;
            do {
                row = reportPage.getRow(tableRange.getFirstRow() + dataRowOffset + (cnt++));
            } while (row == null && hasNext());
            tableRow.setRow(row);
            return tableRow;
        }
    }

    @Override
    public TableRow findRow(Object value) {
        TableCellAddress address = reportPage.find(value);
        if (address.equals(TableCellAddress.NOT_FOUND)) {
            return null;
        }
        MutableTableRow<R> tableRow = new MutableTableRow<>(this, getCellDataAccessObject());
        tableRow.setRow(reportPage.getRow(address.getRow()));
        return tableRow;
    }

    protected abstract CellDataAccessObject<?, R> getCellDataAccessObject();
}