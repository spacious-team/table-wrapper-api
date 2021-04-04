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

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

public abstract class AbstractReportPageRow implements ReportPageRow {

    @RequiredArgsConstructor
    protected static class ReportPageRowIterator<T> implements Iterator<TableCell> {

        private final Iterator<T> innerIterator;
        private final Function<T, TableCell> converter;


        @Override
        public boolean hasNext() {
            return innerIterator.hasNext();
        }

        @Override
        public TableCell next() {
            return converter.apply(innerIterator.next());
        }
    }
}