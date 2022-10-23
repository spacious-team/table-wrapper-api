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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractTableTest {

    @Mock
    AbstractReportPage<EmptyTableRow> report;
    @Mock
    TableCellRange tableRange;
    @Mock
    TableColumnDescription headerDescription;
    AbstractTable<EmptyTableRow> table;

    @BeforeEach
    void beforeEach() {
        table = spy(new TableImpl(report, "table name", tableRange, headerDescription.getClass(), 1));
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
        EmptyTableRow internalRow = mock(EmptyTableRow.class);
        EmptyTableRow resultRow = mock(EmptyTableRow.class);
        Iterator<TableRow> iterator = Arrays.asList(sourceRow, null).iterator();
        when(table.iterator()).thenReturn(iterator);
        Function<TableRow, Collection<EmptyTableRow>> rowExtractor = mock(Function.class);
        when(rowExtractor.apply(sourceRow)).thenReturn(List.of(internalRow, internalRow)); // more than one object
        BiPredicate<EmptyTableRow, EmptyTableRow> equalityChecker = mock(BiPredicate.class);
        when(equalityChecker.test(internalRow, internalRow)).thenReturn(true);
        BiFunction<EmptyTableRow, EmptyTableRow, Collection<EmptyTableRow>> mergeDuplicates = mock(BiFunction.class);
        when(mergeDuplicates.apply(internalRow, internalRow)).thenReturn(singleton(resultRow));

        Collection<EmptyTableRow> result = table.getDataCollection(report, rowExtractor, equalityChecker, mergeDuplicates);

        assertEquals(List.of(resultRow), result);
        verify(rowExtractor).apply(sourceRow);
        verify(equalityChecker).test(internalRow, internalRow);
        verify(mergeDuplicates).apply(internalRow, internalRow);
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
    void getRow() {
    }

    @Test
    void findRow() {
    }

    @Test
    void findRowByPrefix() {
    }

    @Test
    void getCellDataAccessObject() {
    }

    @Test
    void testToString() {
    }

    @Test
    void getReportPage() {
    }

    @Test
    void getTableRange() {
    }

    @Test
    void getHeaderDescription() {
    }

    @Test
    void isEmpty() {
    }

    static class TableImpl extends AbstractTable<EmptyTableRow> {

        @Mock
        CellDataAccessObject<?, EmptyTableRow> dao;

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
}