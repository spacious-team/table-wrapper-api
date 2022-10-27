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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AbstractTableFactoryTest {

    @Mock
    AbstractReportPage<ReportPageRow> reportPage;
    AbstractTableFactory<AbstractReportPage<ReportPageRow>> factory;

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        factory = new TableFactoryTestImpl((Class<AbstractReportPage<ReportPageRow>>) reportPage.getClass());
    }

    @Test
    void canHandle() {
        assertTrue(factory.canHandle(reportPage));
        assertFalse(factory.canHandle(mock(ReportPage.class)));
    }

    @Test
    void cast() {
        assertEquals(reportPage, factory.cast(reportPage));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEquals() {
        TableFactoryTestImpl expected = new TableFactoryTestImpl(
                (Class<AbstractReportPage<ReportPageRow>>) reportPage.getClass());
        assertEquals(expected, factory);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testHashCode() {
        TableFactoryTestImpl expected = new TableFactoryTestImpl(
                (Class<AbstractReportPage<ReportPageRow>>) reportPage.getClass());
        assertEquals(expected.hashCode(), factory.hashCode());
    }

    static class TableFactoryTestImpl extends AbstractTableFactory<AbstractReportPage<ReportPageRow>> {

        protected TableFactoryTestImpl(Class<AbstractReportPage<ReportPageRow>> reportPageType) {
            super(reportPageType);
        }

        @Override
        public Table create(ReportPage reportPage, String tableName, TableCellRange tableRange,
                            Class<? extends TableColumnDescription> headerDescription, int headersRowCount) {
            throw new UnsupportedOperationException();
        }
    }
}