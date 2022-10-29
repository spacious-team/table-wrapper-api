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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TableColumnHelperTest {

    @SuppressWarnings("ConstantConditions")
    static ReportPageRow getRow() {
        ReportPageRow row = mock(ReportPageRow.class);
        Collection<TableCell> cells = Arrays.asList(
                null,
                new PatternTableColumnTest.TableCellTestImpl(null, 1),
                new PatternTableColumnTest.TableCellTestImpl(123, 2),
                new PatternTableColumnTest.TableCellTestImpl(1.23, 3),
                new PatternTableColumnTest.TableCellTestImpl(BigDecimal.valueOf(1), 4),
                new PatternTableColumnTest.TableCellTestImpl("", 5),
                new PatternTableColumnTest.TableCellTestImpl(" ", 6),
                new PatternTableColumnTest.TableCellTestImpl("test word", 9),
                new PatternTableColumnTest.TableCellTestImpl("This Is Sparta", 10),
                new PatternTableColumnTest.TableCellTestImpl("London\nis the\ncapital\nof Great Britain", 20),
                new PatternTableColumnTest.TableCellTestImpl("The Mac\rnew line", 21),
                new PatternTableColumnTest.TableCellTestImpl("The Windows\r\nnew line", 22));
        when(row.iterator()).then($ -> cells.iterator());
        return row;
    }
}
