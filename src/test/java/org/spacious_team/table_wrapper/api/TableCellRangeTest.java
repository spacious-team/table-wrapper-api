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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.spacious_team.table_wrapper.api.TableCellRange.EMPTY_RANGE;

class TableCellRangeTest {

    TableCellRange range = TableCellRange.of(1, 2, 3, 4);

    @Test
    void of1() {
        assertEquals(1, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());
    }

    @Test
    void of2() {
        TableCellAddress upperLeft = TableCellAddress.of(1, 3);
        TableCellAddress bottomRight = TableCellAddress.of(2, 4);
        TableCellRange range = TableCellRange.of(upperLeft, bottomRight);
        assertEquals(1, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());
    }

    @Test
    void contains() {
        TableCellAddress upperLeft = TableCellAddress.of(range.getFirstRow(), range.getFirstColumn());
        TableCellAddress bottomRight = TableCellAddress.of(range.getLastRow(), range.getLastColumn());
        assertTrue(range.contains(upperLeft));
        assertTrue(range.contains(bottomRight));
        assertTrue(range.contains(TableCellAddress.of(range.getFirstRow(), range.getLastColumn())));
        assertFalse(range.contains(TableCellAddress.of(range.getFirstRow() - 1, range.getLastColumn())));
        assertFalse(range.contains(TableCellAddress.of(range.getFirstRow(), range.getFirstColumn() - 1)));
        assertFalse(range.contains(TableCellAddress.of(range.getFirstRow(), range.getLastColumn() + 1)));
        assertFalse(range.contains(TableCellAddress.of(range.getLastRow() + 1, range.getLastColumn())));
    }

    @Test
    void containsRow() {
        assertTrue(range.containsRow(range.getFirstRow()));
        assertTrue(range.containsRow(range.getLastRow()));
        assertFalse(range.containsRow(range.getFirstRow() - 1));
        assertFalse(range.containsRow(range.getLastRow() + 1));
    }

    @Test
    void containsColumn() {
        assertTrue(range.containsColumn(range.getFirstColumn()));
        assertTrue(range.containsColumn(range.getLastColumn()));
        assertFalse(range.containsColumn(range.getFirstColumn() - 1));
        assertFalse(range.containsColumn(range.getLastColumn() + 1));
    }

    @Test
    void addRowsToTop() {
        TableCellRange range = this.range.addRowsToTop(1);
        assertEquals(0, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());

        range = this.range.addRowsToTop(-1);
        assertEquals(2, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());
    }

    @Test
    void addRowsToBottom() {
        TableCellRange range = this.range.addRowsToBottom(1);
        assertEquals(1, range.getFirstRow());
        assertEquals(3, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());

        range = this.range.addRowsToBottom(-1);
        assertEquals(1, range.getFirstRow());
        assertEquals(1, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());
    }

    @Test
    void addColumnsToLeft() {
        TableCellRange range = this.range.addColumnsToLeft(1);
        assertEquals(1, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(2, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());

        range = this.range.addColumnsToLeft(-1);
        assertEquals(1, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(4, range.getFirstColumn());
        assertEquals(4, range.getLastColumn());
    }

    @Test
    void addColumnsToRight() {
        TableCellRange range = this.range.addColumnsToRight(1);
        assertEquals(1, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(5, range.getLastColumn());

        range = this.range.addColumnsToRight(-1);
        assertEquals(1, range.getFirstRow());
        assertEquals(2, range.getLastRow());
        assertEquals(3, range.getFirstColumn());
        assertEquals(3, range.getLastColumn());
    }

    @Test
    void testEmptyRange() {
        assertEquals(-1, EMPTY_RANGE.getFirstRow());
        assertEquals(-1, EMPTY_RANGE.getLastRow());
        assertEquals(-1, EMPTY_RANGE.getFirstColumn());
        assertEquals(-1, EMPTY_RANGE.getLastColumn());
        assertFalse(EMPTY_RANGE.contains(TableCellAddress.NOT_FOUND));
        assertFalse(EMPTY_RANGE.contains(TableCellAddress.of(-1, -1)));
        assertFalse(EMPTY_RANGE.contains(TableCellAddress.of(0, 0)));
        assertFalse(EMPTY_RANGE.contains(TableCellAddress.of(10, 10)));
        assertFalse(EMPTY_RANGE.containsRow(-1));
        assertFalse(EMPTY_RANGE.containsRow(0));
        assertFalse(EMPTY_RANGE.containsRow(10));
        assertFalse(EMPTY_RANGE.containsColumn(-1));
        assertFalse(EMPTY_RANGE.containsColumn(0));
        assertFalse(EMPTY_RANGE.containsColumn(10));
        assertSame(EMPTY_RANGE, EMPTY_RANGE.addRowsToTop(1));
        assertSame(EMPTY_RANGE, EMPTY_RANGE.addRowsToBottom(1));
        assertSame(EMPTY_RANGE, EMPTY_RANGE.addColumnsToLeft(1));
        assertSame(EMPTY_RANGE, EMPTY_RANGE.addColumnsToRight(1));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(TableCellRange.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    @Test
    void testToString() {
        assertEquals(
                "TableCellRange(firstRow=1, lastRow=2, firstColumn=3, lastColumn=4)",
                range.toString());
    }
}