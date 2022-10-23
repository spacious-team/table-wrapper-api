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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spacious_team.table_wrapper.api.AbstractReportPageRow.ReportPageRowIterator;

import java.util.Iterator;
import java.util.function.Function;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractReportPageRowTest {

    @Mock
    Iterator<IteratorElement> innerIterator;
    @Mock
    Function<IteratorElement, TableCell> converter;
    @InjectMocks
    ReportPageRowIterator<IteratorElement> iterator;
    @Mock
    AbstractReportPageRow row;


    @BeforeEach
    void beforeEach() {
        when(row.iterator()).thenReturn(iterator);
        when(innerIterator.next()).thenReturn(IteratorElement.INSTANCE);
    }

    static class IteratorElement {
        @SuppressWarnings("InstantiationOfUtilityClass")
        static final IteratorElement INSTANCE = new IteratorElement();
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testIterator() {
        Iterator<TableCell> it = row.iterator();

        it.hasNext();
        verify(innerIterator).hasNext();

        it.next();
        verify(innerIterator).next();
        verify(converter).apply(IteratorElement.INSTANCE);
    }
}