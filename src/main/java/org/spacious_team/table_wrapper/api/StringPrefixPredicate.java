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

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Predicate;

import static java.lang.Character.isWhitespace;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class StringPrefixPredicate {

    public static Predicate<@Nullable Object> ignoreCaseStringPrefixPredicateOnObject(CharSequence prefix) {
        Predicate<CharSequence> predicate = ignoreCaseStringPrefixPredicate(prefix);
        return PredicateOnObjectWrapper.of(predicate);
    }

    public static <T extends CharSequence> Predicate<T> ignoreCaseStringPrefixPredicate(CharSequence prefix) {
        return new IgnoreCaseStringPrefixPredicate<>(prefix);
    }


    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor(staticName = "of", access = PRIVATE)
    static final class PredicateOnObjectWrapper implements Predicate<@Nullable Object> {
        private final Predicate<CharSequence> predicate;

        @Override
        public boolean test(@Nullable Object o) {
            return (o instanceof CharSequence) && predicate.test((CharSequence) o);
        }
    }


    @ToString
    @EqualsAndHashCode
    static final class IgnoreCaseStringPrefixPredicate<T extends CharSequence> implements Predicate<T> {
        private final String prefix;

        private IgnoreCaseStringPrefixPredicate(CharSequence prefix) {
            this.prefix = prefix.toString().strip();
        }

        @Override
        public boolean test(T cs) {
            int nonWhitespaceIndex = getIndexOfNonWhitespace(cs);
            if (nonWhitespaceIndex == -1) {
                return false;
            }
            String string = cs.toString();
            return string.regionMatches(true, nonWhitespaceIndex, prefix, 0, prefix.length());
        }

        private static int getIndexOfNonWhitespace(CharSequence cs) {
            for (int i = 0, n = cs.length(); i < n; i++) {
                char c = cs.charAt(i);
                if (!isWhitespace(c)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
