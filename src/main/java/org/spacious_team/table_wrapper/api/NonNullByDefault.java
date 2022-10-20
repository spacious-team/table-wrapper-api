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

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be applied to a package, class or method to indicate that the class fields,
 * method return types and parameters in that element are not null by default unless there is:
 * The method overrides a method in a superclass (in which
 * case the annotation of the corresponding parameter in the superclass applies) there is a
 * default parameter annotation applied to a more tightly nested element.
 *
 * @see <a href="https://habr.com/ru/post/237843/">Explanation</a> and linked to this article
 * <a href="https://youtrack.jetbrains.com/issue/IDEA-125281">implementation</a>
 */
@Documented
@Nonnull
@TypeQualifierDefault({
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD,
        ElementType.PACKAGE,
        ElementType.PARAMETER,
        ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNullByDefault {
}