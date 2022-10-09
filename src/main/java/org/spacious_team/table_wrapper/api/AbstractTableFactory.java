/*
 * Table Wrapper API
 * Copyright (C) 2021  Spacious Team <spacious-team@ya.ru>
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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * {@link TableFactory} factory with specified {@link ReportPage}
 * @param <T> the factory supported {@link ReportPage} type and subtypes
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTableFactory<T extends ReportPage> implements TableFactory {

    private final Class<T> reportPageType;

    @Override
    public boolean canHandle(ReportPage reportPage) {
        return reportPageType.isAssignableFrom(reportPage.getClass());
    }

    /**
     * Safe cast operation if {@link #canHandle(ReportPage)} is true
     */
    @SuppressWarnings({"unchecked", "unused"})
    protected T cast(ReportPage reportPage) {
        return (T) reportPage;
    }
}