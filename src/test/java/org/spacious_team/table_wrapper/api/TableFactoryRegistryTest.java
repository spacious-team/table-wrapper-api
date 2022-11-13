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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TableFactoryRegistryTest {

    ReportPage reportPage1;
    ReportPage reportPage2;
    ReportPage reportPageOfNotRegisteredFactory;

    TableFactory factory1;
    TableFactory factory2;
    TableFactory factory1Copy;

    @BeforeEach
    void setUp() {
        reportPage1 = mock(ReportPage.class);
        reportPage2 = mock(ReportPage.class);
        reportPageOfNotRegisteredFactory = mock(ReportPage.class);

        factory1 = mock(TableFactory.class);
        factory2 = mock(TableFactory.class);
        factory1Copy = factory1;

        lenient().when(factory1.canHandle(reportPage1)).thenReturn(true);
        lenient().when(factory1.canHandle(reportPage2)).thenReturn(false);
        lenient().when(factory1.canHandle(reportPageOfNotRegisteredFactory)).thenReturn(false);

        lenient().when(factory2.canHandle(reportPage1)).thenReturn(false);
        lenient().when(factory2.canHandle(reportPage2)).thenReturn(true);
        lenient().when(factory2.canHandle(reportPageOfNotRegisteredFactory)).thenReturn(false);
    }

    @AfterEach
    void tearDown() {
        TableFactoryRegistry.clear();
    }

    @Test
    void test() {
        assertTrue(TableFactoryRegistry.getAll().isEmpty());

        TableFactoryRegistry.add(factory1);
        assertEquals(Set.of(factory1), TableFactoryRegistry.getAll());

        TableFactoryRegistry.add(factory2);
        assertEquals(Set.of(factory1, factory2), TableFactoryRegistry.getAll());

        TableFactoryRegistry.add(factory1Copy);
        assertEquals(Set.of(factory1, factory2), TableFactoryRegistry.getAll());

        TableFactoryRegistry.add(factory2);
        assertEquals(Set.of(factory1, factory2), TableFactoryRegistry.getAll());

        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> TableFactoryRegistry.get(null));
        assertThrows(IllegalArgumentException.class, () -> TableFactoryRegistry.get(reportPageOfNotRegisteredFactory));
        assertSame(factory1, TableFactoryRegistry.get(reportPage1));
        assertSame(factory2, TableFactoryRegistry.get(reportPage2));

        assertTrue(TableFactoryRegistry.remove(factory1Copy));
        assertEquals(Set.of(factory2), TableFactoryRegistry.getAll());

        assertTrue(TableFactoryRegistry.remove(factory2));
        assertTrue(TableFactoryRegistry.getAll().isEmpty());

        TableFactoryRegistry.add(factory1);
        assertEquals(Set.of(factory1), TableFactoryRegistry.getAll());

        TableFactoryRegistry.clear();
        assertTrue(TableFactoryRegistry.getAll().isEmpty());
    }
}