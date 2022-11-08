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
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Predicate;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
final class ReportPageHelper {

    static Predicate<Object> getCellStringValueIgnoreCasePrefixPredicate(String prefix) {
        return new StringIgnoreCasePrefixPredicate(prefix);
    }

    @ToString
    @EqualsAndHashCode
    static final class StringIgnoreCasePrefixPredicate implements Predicate<Object> {
        private final String lowercasePrefix;

        private StringIgnoreCasePrefixPredicate(String prefix) {
            this.lowercasePrefix = prefix.trim().toLowerCase();
        }

        @Override
        public boolean test(Object cell) {
            return (cell instanceof String) &&
                    ((String) cell).trim().toLowerCase().startsWith(lowercasePrefix);
        }
    }
}
