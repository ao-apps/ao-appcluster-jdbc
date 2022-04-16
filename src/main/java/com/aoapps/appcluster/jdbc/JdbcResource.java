/*
 * ao-appcluster-jdbc - Application-level clustering tools for JDBC-level database replication.
 * Copyright (C) 2011, 2016, 2020, 2021, 2022  AO Industries, Inc.
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
 * along with ao-appcluster-jdbc.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aoapps.appcluster.jdbc;

import com.aoapps.appcluster.AppCluster;
import com.aoapps.appcluster.AppClusterConfigurationException;
import com.aoapps.appcluster.CronResource;
import com.aoapps.appcluster.ResourceConfiguration;
import com.aoapps.appcluster.ResourceNode;
import com.aoapps.collections.AoCollections;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Resources are synchronized through JDBC.
 *
 * @author  AO Industries, Inc.
 */
public class JdbcResource extends CronResource<JdbcResource, JdbcResourceNode> {

	private final Set<String> schemas;
	private final Set<String> tableTypes;
	private final Set<String> excludeTables;
	private final Set<String> noWarnTables;
	private final Map<String, String> prepareSlaves;

	protected JdbcResource(AppCluster cluster, JdbcResourceConfiguration resourceConfiguration, Collection<? extends ResourceNode<?, ?>> resourceNodes) throws AppClusterConfigurationException {
		super(cluster, resourceConfiguration, resourceNodes);
		this.schemas = AoCollections.unmodifiableCopySet(resourceConfiguration.getSchemas());
		this.tableTypes = AoCollections.unmodifiableCopySet(resourceConfiguration.getTableTypes());
		this.excludeTables = AoCollections.unmodifiableCopySet(resourceConfiguration.getExcludeTables());
		this.noWarnTables = AoCollections.unmodifiableCopySet(resourceConfiguration.getNoWarnTables());
		this.prepareSlaves = AoCollections.unmodifiableCopyMap(resourceConfiguration.getPrepareSlaves());
	}

	/**
	 * Multi master synchronization is not supported for JDBC.
	 */
	@Override
	public boolean getAllowMultiMaster() {
		return false;
	}

	/**
	 * Gets the set of schemas that will be synchronized.
	 */
	@SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
	public Set<String> getSchemas() {
		return schemas;
	}

	/**
	 * Gets the set of table types that will be synchronized.
	 */
	@SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
	public Set<String> getTableTypes() {
		return tableTypes;
	}

	/**
	 * Gets the set of tables that will be excluded from synchronization, in schema.name format.
	 */
	@SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
	public Set<String> getExcludeTables() {
		return excludeTables;
	}

	/**
	 * Gets the set of tables that will not cause warnings when the data is not an exact match, in schema.name format.
	 */
	@SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
	public Set<String> getNoWarnTables() {
		return noWarnTables;
	}

	/**
	 * Gets the set of SQL statements that should be executed on the slave in preparation for a synchronization pass.
	 * This should be executed in iteration order.  The key is a unique name of the statement for reference and debugging,
	 * while the SQL statement is the value.
	 */
	@SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
	public Map<String, String> getPrepareSlaves() {
		return prepareSlaves;
	}

	@Override
	protected JdbcResourceSynchronizer newResourceSynchronizer(JdbcResourceNode localResourceNode, JdbcResourceNode remoteResourceNode, ResourceConfiguration<JdbcResource, JdbcResourceNode> resourceConfiguration) throws AppClusterConfigurationException {
		JdbcResourceConfiguration jdbcResourceConfiguration = (JdbcResourceConfiguration)resourceConfiguration;
		return new JdbcResourceSynchronizer(
			localResourceNode,
			remoteResourceNode,
			jdbcResourceConfiguration.getSynchronizeSchedule(localResourceNode, remoteResourceNode),
			jdbcResourceConfiguration.getTestSchedule(localResourceNode, remoteResourceNode)
		);
	}
}
