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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableTest {

    @Spy
    Table table;

    @Test
    void getData() {
        Function<TableRow, ?> rowExtractor = Function.identity();
        table.getData(rowExtractor);
        verify(table).getData("unknown", rowExtractor);
    }

    @Test
    void getDataCollection() {
        Function<TableRow, Collection<Object>> rowExtractor = x -> Set.of();
        table.getDataCollection(rowExtractor);
        verify(table).getDataCollection("unknown", rowExtractor);
    }

    @Test
    void excludeTotalRow() {
        table.excludeTotalRow();
        verify(table).subTable(0, -1);
    }
}