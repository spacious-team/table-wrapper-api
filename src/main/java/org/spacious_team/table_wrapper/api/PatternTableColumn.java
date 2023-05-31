/*
 * Table Wrapper API
 * Copyright (C) 2020  Spacious Team <spacious-team@ya.ru>
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
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.*;
import static lombok.AccessLevel.PRIVATE;

/**
 * Finds cell column index by case-insensitive checks by all the predefined regexp patterns.
 */
@ToString(of = "words")
@EqualsAndHashCode(of = "words")
@RequiredArgsConstructor(access = PRIVATE)
public class PatternTableColumn implements TableColumn {
    private final Pattern[] patterns;
    private final Set<String> words;

    /**
     * Cell text should match to all regexp patterns.
     */
    public static TableColumn of(@Nullable String... words) {
        //noinspection ConstantConditions
        if (words == null) {
            return LEFTMOST_COLUMN;
        }
        @SuppressWarnings("nullness")
        Set<String> nonNullWords = Arrays.stream(words)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
        Pattern[] patterns = nonNullWords.stream()
                .map(PatternTableColumn::toPattern)
                .toArray(Pattern[]::new);
        if (patterns.length == 0) {
            return LEFTMOST_COLUMN;
        }
        return new PatternTableColumn(patterns, nonNullWords);
    }

    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        for (ReportPageRow header : headerRows) {
            for (@Nullable TableCell cell : header) {
                @Nullable Object value;
                if (cell != null &&
                        cell.getColumnIndex() >= firstColumnForSearch &&
                        (value = cell.getValue()) != null &&
                        value instanceof CharSequence &&
                        matches((CharSequence) value)) {
                    return cell.getColumnIndex();
                }
            }
        }
        throw new TableColumnNotFound("Header including '" + String.join(", ", words) + "' is not found");
    }

    private boolean matches(CharSequence cellText) {
        for (Pattern pattern : patterns) {
            if (!pattern.matcher(cellText).find()) {
                return false;
            }
        }
        return true;
    }


    private static Pattern toPattern(String pattern) {
        return Pattern.compile(pattern, CASE_INSENSITIVE | UNICODE_CASE | UNICODE_CHARACTER_CLASS);
    }
}
