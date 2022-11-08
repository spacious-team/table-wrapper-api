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
import org.spacious_team.table_wrapper.api.ReportPageHelper.StringIgnoreCasePrefixPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.spacious_team.table_wrapper.api.ReportPageHelper.getCellStringValueIgnoreCasePrefixPredicate;

class ReportPageHelperTest {

    @Test
    void test() {
        assertTrue(getCellStringValueIgnoreCasePrefixPredicate("First").test("First second"));
        assertTrue(getCellStringValueIgnoreCasePrefixPredicate("First").test("first second"));
        assertTrue(getCellStringValueIgnoreCasePrefixPredicate("first").test("First second"));
        assertTrue(getCellStringValueIgnoreCasePrefixPredicate("first").test("first second"));
        assertTrue(getCellStringValueIgnoreCasePrefixPredicate("FIRST").test("first second"));
        assertFalse(getCellStringValueIgnoreCasePrefixPredicate("First").test("One two"));
        //noinspection ConstantConditions
        assertFalse(getCellStringValueIgnoreCasePrefixPredicate("First").test(null));
        assertFalse(getCellStringValueIgnoreCasePrefixPredicate("First").test(1));
        assertFalse(getCellStringValueIgnoreCasePrefixPredicate("First").test(1.1));
        assertFalse(getCellStringValueIgnoreCasePrefixPredicate("First").test(new Object()));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(StringIgnoreCasePrefixPredicate.class)
                .verify();
    }

    @Test
    void testToString() {
        assertEquals(
                "ReportPageHelper.StringIgnoreCasePrefixPredicate(lowercasePrefix=first)",
                getCellStringValueIgnoreCasePrefixPredicate("First").toString());
    }
}