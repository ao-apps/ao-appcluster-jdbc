/*
 * ao-appcluster-jdbc - Application-level clustering tools for JDBC-level database replication.
 * Copyright (C) 2011, 2016, 2021  AO Industries, Inc.
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

import com.aoindustries.appcluster.AppClusterConfigurationException;
import com.aoindustries.appcluster.AppClusterPropertiesConfiguration;
import com.aoindustries.appcluster.CronResourceNodePropertiesConfiguration;
import com.aoindustries.appcluster.Node;

/**
 * The configuration for a JDBC resource.
 *
 * @author  AO Industries, Inc.
 */
public class JdbcResourceNodePropertiesConfiguration extends CronResourceNodePropertiesConfiguration<JdbcResource, JdbcResourceNode> implements JdbcResourceNodeConfiguration {

	private final String dataSource;

	protected JdbcResourceNodePropertiesConfiguration(AppClusterPropertiesConfiguration properties, String resourceId, String nodeId, String type) throws AppClusterConfigurationException {
		super(properties, resourceId, nodeId);
		this.dataSource = properties.getString("appcluster.resource."+resourceId+".node."+nodeId+"."+type+".dataSource", true);
	}

	@Override
	public String getDataSource() {
		return dataSource;
	}

	@Override
	public JdbcResourceNode newResourceNode(Node node) throws AppClusterConfigurationException {
		return new JdbcResourceNode(node, this);
	}
}
