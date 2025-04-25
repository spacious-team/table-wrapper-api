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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.spacious_team.table_wrapper.api.StringPrefixPredicate.IgnoreCaseStringPrefixPredicate;
import org.spacious_team.table_wrapper.api.StringPrefixPredicate.PredicateOnObjectWrapper;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.spacious_team.table_wrapper.api.StringPrefixPredicate.ignoreCaseStringPrefixPredicate;
import static org.spacious_team.table_wrapper.api.StringPrefixPredicate.ignoreCaseStringPrefixPredicateOnObject;

class StringPrefixPredicateTest {
    
    static Object[][] prefixAndMatchingString() {
        return new Object[][]{
                {"First", "First second"},
                {"First", "first second"},
                {"first", "First second"},
                {"first", "first second"},
                {"FIRST", "first second"},
                {" FIRST", "first second"},
                {" FIRST", "    first second"},
                {"\tfirst\n", "\n   first \tsecond"},
                {"first", "\t   \nfirst\n\tsecond"},
                {"\nfirst\n", "\t   \n\rfirst\r\n\tsecond"},
        };
    }

    static Object[][] prefixAndNotMatchingSting() {
        return new Object[][]{
                {"First", "  \n\r\n\t"},
                {"First", "One two"},
                {"first", "fir st two"},
                {"first", "zero first"},
                {"first", "zero first second"},
        };
    }

    static Object[][] prefixAndObject() {
        return new Object[][]{
                {"First", 1},
                {"First", 1.1},
                {"First", new Object()},
        };
    }

    //  ignoreCaseStringPrefixPredicateOnObject(CharSequence) tests

    @ParameterizedTest
    @MethodSource("prefixAndMatchingString")
    void ignoreCaseStringPrefixPredicateOnObject_matched(String prefix, String testingString) {
        assertTrue(ignoreCaseStringPrefixPredicateOnObject(prefix).test(testingString));
    }

    @ParameterizedTest
    @MethodSource("prefixAndNotMatchingSting")
    void ignoreCaseStringPrefixPredicateOnObject_notMatchedString(String prefix, String testingString) {
        assertFalse(ignoreCaseStringPrefixPredicateOnObject(prefix).test(testingString));
    }

    @ParameterizedTest
    @MethodSource("prefixAndObject")
    void ignoreCaseStringPrefixPredicateOnObject_object(String prefix, Object testingObject) {
        assertFalse(ignoreCaseStringPrefixPredicateOnObject(prefix).test(testingObject));
    }

    @Test
    void ignoreCaseStringPrefixPredicateOnObject_null() {
        assertFalse(ignoreCaseStringPrefixPredicateOnObject("any").test(null));
    }


    //  ignoreCaseStringPrefixPredicate(CharSequence) tests

    @ParameterizedTest
    @MethodSource("prefixAndMatchingString")
    void ignoreCaseStringPrefixPredicate_matched(String prefix, String testingString) {
        assertTrue(ignoreCaseStringPrefixPredicate(prefix).test(testingString));
    }

    @ParameterizedTest
    @MethodSource("prefixAndNotMatchingSting")
    void ignoreCaseStringPrefixPredicate_notMatchedString(String prefix, String testingString) {
        assertFalse(ignoreCaseStringPrefixPredicate(prefix).test(testingString));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void ignoreCaseStringPrefixPredicate_null() {
        Predicate<String> predicate = ignoreCaseStringPrefixPredicate("any");
        assertThrows(NullPointerException.class, () -> predicate.test(null));
    }

    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(IgnoreCaseStringPrefixPredicate.class)
                .verify();
        EqualsVerifier
                .forClass(PredicateOnObjectWrapper.class)
                .verify();
    }

    @Test
    void testToString() {
        assertEquals(
                "StringPrefixPredicate.PredicateOnObjectWrapper(predicate=StringPrefixPredicate.IgnoreCaseStringPrefixPredicate(prefix=First))",
                ignoreCaseStringPrefixPredicateOnObject("First").toString());
        assertEquals(
                "StringPrefixPredicate.IgnoreCaseStringPrefixPredicate(prefix=First)",
                ignoreCaseStringPrefixPredicate("First").toString());
    }
}