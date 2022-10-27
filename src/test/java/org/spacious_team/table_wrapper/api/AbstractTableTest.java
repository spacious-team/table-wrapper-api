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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.spacious_team.table_wrapper.api.TableColumn.LEFTMOST_COLUMN;

@ExtendWith(MockitoExtension.class)
class AbstractTableTest {

    @Mock
    AbstractReportPage<EmptyTableRow> report;
    @Mock
    TableCellRange tableRange;
    Class<Columns> headerDescription = Columns.class;
    @Mock
    CellDataAccessObject<?, EmptyTableRow> dao;
    AbstractTable<EmptyTableRow> table;

    @BeforeEach
    void beforeEach() {
        table = spy(new TableImpl(report, "table name", tableRange, headerDescription, 1));
    }

    @Test
    void testEmptyRangeConstructor() {
        AbstractTable<EmptyTableRow> table = getEmptyTable();

        assertEquals(report, table.getReportPage());
        assertEquals(new TableCellRange(2, 3, 0, 100), table.getTableRange());
        assertTrue(table.getHeaderDescription().isEmpty());
        assertTrue(table.isEmpty());
    }

    /**
     * Builds not empty table of 2 columns, row #2 contains table name, row #3 - header, no data rows
     */
    private AbstractTable<EmptyTableRow> getEmptyTable() {
        TableCellRange tableRange = new TableCellRange(2, 3, 0, 100);
        return new TableImpl(report, "table name", tableRange, headerDescription, 1);
    }

    @Test
    void testNotEmptyRangeConstructor() {
        AbstractTable<EmptyTableRow> table = getNotEmptyTable();
        TableCellRange range = new TableCellRange(2, 6, 0, 1);
        ReportPageRow[] headerRows = new EmptyTableRow[0]; // mock
        Map<TableColumn, Integer> headerDescriptionMap = Map.of(
                Columns.FIRST.getColumn(),
                Columns.FIRST.getColumn().getColumnIndex(headerRows),
                Columns.SECOND.getColumn(),
                Columns.SECOND.getColumn().getColumnIndex(headerRows));

        assertEquals(report, table.getReportPage());
        assertEquals(range, table.getTableRange());
        assertEquals(headerDescriptionMap, table.getHeaderDescription());
        assertFalse(table.isEmpty());
    }

    /**
     * Builds not empty table of 2 columns, row #2 contains table name, row #3 and #4 - header, row #5 - data,
     * row #6 - data with is equals to null
     */
    @SuppressWarnings("ConstantConditions")
    private AbstractTable<EmptyTableRow> getNotEmptyTable() {
        // 2-th row - table name, 3-st and 4-nd rows - table header
        TableCellRange tableRange = new TableCellRange(2, 6, 0, 100);
        when(report.getRow(3)).thenReturn(new EmptyTableRow(table, 3));
        when(report.getRow(4)).thenReturn(new EmptyTableRow(table, 4));
        return new TableImpl(report, "table name", tableRange, headerDescription, 2);
    }

    @Test
    void testEmptyRangeConstructor2() {
        AbstractTable<EmptyTableRow> originalTable = getNotEmptyTable();
        AbstractTable<EmptyTableRow> table = new TableImpl(originalTable, -1, -1);
        TableCellRange range = new TableCellRange(3, 5, 0, 1);

        assertEquals(report, table.getReportPage());
        assertEquals(range, table.getTableRange());
        assertEquals(originalTable.getHeaderDescription(), table.getHeaderDescription());
        assertTrue(table.isEmpty());
    }

    @Test
    void testNotEmptyRangeConstructor2() {
        AbstractTable<EmptyTableRow> originalTable = getEmptyTable();
        AbstractTable<EmptyTableRow> table = new TableImpl(originalTable, 1, 2);
        TableCellRange range = new TableCellRange(1, 5,
                table.getTableRange().getFirstColumn(), table.getTableRange().getLastColumn());

        assertEquals(report, table.getReportPage());
        assertEquals(range, table.getTableRange());
        assertEquals(originalTable.getHeaderDescription(), table.getHeaderDescription());
        assertFalse(table.isEmpty());
    }

    @Test
    void getData() {
        TableRow sourceRow = mock(TableRow.class);
        EmptyTableRow resultRow = mock(EmptyTableRow.class);
        Iterator<TableRow> iterator = Arrays.asList(sourceRow, null).iterator();
        when(table.iterator()).thenReturn(iterator);
        @SuppressWarnings("unchecked")
        Function<TableRow, EmptyTableRow> extractor = mock(Function.class);
        when(extractor.apply(sourceRow)).thenReturn(resultRow);

        Collection<EmptyTableRow> result = table.getData(report, extractor);

        assertEquals(List.of(resultRow), result);
        verify(extractor).apply(sourceRow);
    }

    @Test
    void getDataWithException() {
        TableRow sourceRow = mock(TableRow.class);
        Iterator<TableRow> iterator = Arrays.asList(sourceRow, null).iterator();
        when(table.iterator()).thenReturn(iterator);
        Function<TableRow, EmptyTableRow> extractor = srcRow -> {
            throw new RuntimeException();
        };

        Collection<EmptyTableRow> result = table.getData(report, extractor);

        assertEquals(emptyList(), result);
    }

    @Test
    void getDataCollection() {
        TableRow sourceRow = mock(TableRow.class);
        EmptyTableRow resultRow = mock(EmptyTableRow.class);
        Iterator<TableRow> iterator = Arrays.asList(sourceRow, null).iterator();
        when(table.iterator()).thenReturn(iterator);
        @SuppressWarnings("unchecked")
        Function<TableRow, Collection<EmptyTableRow>> rowExtractor = mock(Function.class);
        when(rowExtractor.apply(sourceRow)).thenReturn(List.of(resultRow));

        Collection<EmptyTableRow> result = table.getDataCollection(report, rowExtractor);

        assertEquals(List.of(resultRow), result);
        verify(rowExtractor).apply(sourceRow);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetDataCollection() {
        TableRow sourceRow = mock(TableRow.class);
        EmptyTableRow internalRow1 = mock(EmptyTableRow.class);
        EmptyTableRow internalRow2 = mock(EmptyTableRow.class);
        EmptyTableRow mergedRow = mock(EmptyTableRow.class);
        Iterator<TableRow> iterator = Arrays.asList(sourceRow, null).iterator();
        when(table.iterator()).thenReturn(iterator);
        Function<TableRow, Collection<EmptyTableRow>> rowExtractor = mock(Function.class);
        when(rowExtractor.apply(sourceRow)).thenReturn(List.of(internalRow1, internalRow1, internalRow2)); // has duplicated
        BiPredicate<EmptyTableRow, EmptyTableRow> equalityChecker = mock(BiPredicate.class);
        when(equalityChecker.test(internalRow1, internalRow1)).thenReturn(true);
        when(equalityChecker.test(mergedRow, internalRow2)).thenReturn(false);
        BiFunction<EmptyTableRow, EmptyTableRow, Collection<EmptyTableRow>> mergeDuplicates = mock(BiFunction.class);
        when(mergeDuplicates.apply(internalRow1, internalRow1)).thenReturn(singleton(mergedRow));

        Collection<EmptyTableRow> result = table.getDataCollection(report, rowExtractor, equalityChecker, mergeDuplicates);

        assertEquals(List.of(mergedRow, internalRow2), result);
        verify(rowExtractor).apply(sourceRow);
        verify(equalityChecker).test(internalRow1, internalRow1);
        verify(equalityChecker).test(mergedRow, internalRow2);
        verify(mergeDuplicates).apply(internalRow1, internalRow1);
    }

    @Test
    void stream() {
        TableRow row = mock(TableRow.class);
        Iterator<TableRow> iterator = Arrays.asList(row, null).iterator();
        when(table.iterator()).thenReturn(iterator);

        Stream<TableRow> stream = table.stream();

        assertEquals(Arrays.asList(row, null), stream.collect(Collectors.toList()));
    }

    @Test
    void iterator() {
        Iterator<TableRow> iterator = table.iterator();
        assertEquals(AbstractTable.TableIterator.class, iterator.getClass());
    }

    @Test
    void testIteration() {
        table = getNotEmptyTable();
        //noinspection ConstantConditions
        when(report.getRow(5)).thenReturn(new EmptyTableRow(table, 5));
        // report.getRow(6) == null

        Iterator<TableRow> iterator = table.iterator();
        boolean nextA = iterator.hasNext();
        TableRow rowA = iterator.next();
        boolean nextB = iterator.hasNext();
        TableRow rowB = iterator.next();
        boolean nextC = iterator.hasNext();

        assertEquals(AbstractTable.TableIterator.class, iterator.getClass());
        assertTrue(nextA);
        assertTrue(nextB);
        assertFalse(nextC);
        assertEquals(MutableTableRow.class, rowA.getClass());
        assertEquals(EmptyTableRow.class, rowB.getClass());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals(5, rowA.getRowNum());
        assertEquals(6, rowB.getRowNum());
    }

    @Test
    void getRow() {
        table.getRow(1);
        verify(report).getRow(1);
    }

    @Test
    void findRowNotFound() {
        when(tableRange.contains(any())).thenReturn(false);
        assertNull(table.findRow("row value"));
    }

    @Test
    void findRowFoundEmptyRow() {
        when(tableRange.contains(any())).thenReturn(true);
        assertThrows(NullPointerException.class, () -> table.findRow("row value"));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void findRow() {
        TableCellAddress address = new TableCellAddress(1, 0);
        EmptyTableRow row = new EmptyTableRow(table, address.getRow());
        MutableTableRow<EmptyTableRow> mutableRow = new MutableTableRow<>(table, dao);
        mutableRow.setRow(row);
        when(report.find("row value")).thenReturn(address);
        when(tableRange.contains(address)).thenReturn(true);
        when(table.getRow(address.getRow())).thenReturn(row);

        assertEquals(mutableRow, table.findRow("row value"));
        verify(report).find("row value");
        verify(tableRange).contains(address);
        verify(table).getRow(address.getRow());
    }

    @Test
    void findRowByPrefix() {
        TableCellAddress address = new TableCellAddress(1, 0);
        when(report.findByPrefix("row value")).thenReturn(address);

        table.findRowByPrefix("row value");

        verify(report).findByPrefix("row value");
    }

    @Test
    void testToString() {
        assertEquals("AbstractTable(tableName=table name)", table.toString());
    }

    @Test
    void testEquals() {
        assertEquals(getNotEmptyTable(), getNotEmptyTable());
        assertNotEquals(getNotEmptyTable(), getEmptyTable());
    }

    @Test
    void testHashCode() {
        assertEquals(getNotEmptyTable().hashCode(), getNotEmptyTable().hashCode());
        assertNotEquals(getNotEmptyTable().hashCode(), getEmptyTable().hashCode());
    }

    class TableImpl extends AbstractTable<EmptyTableRow> {

        protected TableImpl(AbstractReportPage<EmptyTableRow> reportPage,
                            String tableName,
                            TableCellRange tableRange,
                            Class<? extends TableColumnDescription> headerDescription,
                            int headersRowCount) {
            super(reportPage, tableName, tableRange, headerDescription, headersRowCount);
        }

        public TableImpl(AbstractTable<EmptyTableRow> table, int appendDataRowsToTop, int appendDataRowsToBottom) {
            super(table, appendDataRowsToTop, appendDataRowsToBottom);
        }

        @Override
        protected CellDataAccessObject<?, EmptyTableRow> getCellDataAccessObject() {
            return dao;
        }

        @Override
        public Table subTable(int topRows, int bottomRows) {
            throw new UnsupportedOperationException();
        }
    }

    @Getter
    @RequiredArgsConstructor
    enum Columns implements TableColumnDescription {
        FIRST(LEFTMOST_COLUMN),
        SECOND(ConstantPositionTableColumn.of(1));
        private final TableColumn column;
    }
}