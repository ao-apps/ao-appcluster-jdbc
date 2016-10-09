/*
 * ao-appcluster-jdbc - Application-level clustering tools for JDBC-level database replication.
 * Copyright (C) 2011, 2016  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-appcluster-jdbc.
 *
 * ao-appcluster-jdbc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-appcluster-jdbc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-appcluster-jdbc.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.appcluster.jdbc;

import com.aoindustries.util.i18n.EditableResourceBundle;
import java.io.File;
import java.util.Locale;

/**
 * Provides a simplified interface for obtaining localized values from the ApplicationResources.properties files.
 *
 * @author  AO Industries, Inc.
 */
public final class ApplicationResources_ja extends EditableResourceBundle {

	/**
	 * Do not use directly.
	 */
	public ApplicationResources_ja() {
		super(
			Locale.JAPANESE,
			ApplicationResources.bundleSet,
			new File(System.getProperty("user.home")+"/common/ao/cvswork/ao-appcluster-jdbc/src/com/aoindustries/appcluster/jdbc/ApplicationResources_ja.properties")
		);
	}
}
