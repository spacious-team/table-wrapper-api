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
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CASE;

@ToString(of = "words")
@RequiredArgsConstructor
@EqualsAndHashCode(of = "words")
public class PatternTableColumn implements TableColumn {
    private final Pattern[] patterns;
    private final String[] words;

    public static TableColumn of(@Nullable String... words) {
        //noinspection ConstantConditions
        if (words == null) {
            return LEFTMOST_COLUMN;
        }
        @SuppressWarnings("nullness")
        String[] nonNullWords = Arrays.stream(words)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        Pattern[] patterns = Arrays.stream(nonNullWords)
                .map(PatternTableColumn::toPattern)
                .toArray(Pattern[]::new);
        if (patterns.length == 0) {
            return LEFTMOST_COLUMN;
        }
        return new PatternTableColumn(patterns, nonNullWords);
    }

    public int getColumnIndex(int firstColumnForSearch, ReportPageRow... headerRows) {
        for (ReportPageRow header : headerRows) {
            next_cell:
            for (@SuppressWarnings("NullableProblems") @Nullable TableCell cell : header) {
                @Nullable Object value;
                if ((cell != null) && (cell.getColumnIndex() >= firstColumnForSearch) &&
                        (((value = cell.getValue()) != null) && (value instanceof String))) {
                    String colName = value.toString();
                    for (Pattern pattern : patterns) {
                        if (!containsWord(colName, pattern)) {
                            continue next_cell;
                        }
                    }
                    return cell.getColumnIndex();
                }
            }
        }
        throw new RuntimeException("Не обнаружен заголовок таблицы, включающий слова: " + String.join(", ", words));
    }

    private boolean containsWord(String text, Pattern pattern) {
        return pattern.matcher(text).matches();
    }

    private static Pattern toPattern(String pattern) {
        String leftBoundary = "(^|(.|\\n)*\\b|(.|\\n)*\\s)"; // '.' not matches new lines
        String rightBoundary = "(\\b(.|\\n)*|\\s(.|\\n)*|$)";
        return Pattern.compile(leftBoundary + pattern + rightBoundary, CASE_INSENSITIVE | UNICODE_CASE);
    }
}
