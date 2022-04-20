/*
 * ao-appcluster-jdbc - Application-level clustering tools for JDBC-level database replication.
 * Copyright (C) 2011, 2015, 2016, 2019, 2020, 2021, 2022  AO Industries, Inc.
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
import com.aoapps.appcluster.AppClusterPropertiesConfiguration;
import com.aoapps.appcluster.CronResourcePropertiesConfiguration;
import com.aoapps.appcluster.ResourceNode;
import com.aoapps.collections.AoCollections;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * The configuration for a JDBC resource.
 *
 * @author  AO Industries, Inc.
 */
public class JdbcResourcePropertiesConfiguration extends CronResourcePropertiesConfiguration<JdbcResource, JdbcResourceNode> implements JdbcResourceConfiguration {

  private final Set<String> schemas;
  private final Set<String> tableTypes;
  private final Set<String> excludeTables;
  private final Set<String> noWarnTables;
  private final Map<String, String> prepareSlaves;

  protected JdbcResourcePropertiesConfiguration(AppClusterPropertiesConfiguration properties, String id) throws AppClusterConfigurationException {
    super(properties, id);
    this.schemas = properties.getUniqueStrings("appcluster.resource."+id+"."+type+".schemas", true);
    this.tableTypes = properties.getUniqueStrings("appcluster.resource."+id+"."+type+".tableTypes", true);
    this.excludeTables = properties.getUniqueStrings("appcluster.resource."+id+"."+type+".excludeTables", false);
    this.noWarnTables = properties.getUniqueStrings("appcluster.resource."+id+"."+type+".noWarnTables", false);
    Set<String> prepareSlaveNames = properties.getUniqueStrings("appcluster.resource."+id+"."+type+".prepareSlaves", false);
    if (prepareSlaveNames.isEmpty()) {
      this.prepareSlaves = Collections.emptyMap();
    } else {
      Map<String, String> newPrepareSlaves = AoCollections.newLinkedHashMap(prepareSlaveNames.size());
      for (String prepareSlaveName : prepareSlaveNames) {
        newPrepareSlaves.put(
          prepareSlaveName,
          properties.getString("appcluster.resource."+id+"."+type+".prepareSlave."+prepareSlaveName, true)
        );
      }
      this.prepareSlaves = AoCollections.optimalUnmodifiableMap(newPrepareSlaves);
    }
  }

  @Override
  @SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
  public Set<String> getSchemas() {
    return schemas;
  }

  @Override
  @SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
  public Set<String> getTableTypes() {
    return tableTypes;
  }

  @Override
  @SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
  public Set<String> getExcludeTables() {
    return excludeTables;
  }

  @Override
  @SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
  public Set<String> getNoWarnTables() {
    return noWarnTables;
  }

  @Override
  @SuppressWarnings("ReturnOfCollectionOrArrayField") // Returning unmodifiable
  public Map<String, String> getPrepareSlaves() {
    return prepareSlaves;
  }

  @Override
  public Set<? extends JdbcResourceNodePropertiesConfiguration> getResourceNodeConfigurations() throws AppClusterConfigurationException {
    String resourceId = getId();
    Set<String> nodeIds = properties.getUniqueStrings("appcluster.resource."+id+".nodes", true);
    Set<JdbcResourceNodePropertiesConfiguration> resourceNodes = AoCollections.newLinkedHashSet(nodeIds.size());
    for (String nodeId : nodeIds) {
      if (!resourceNodes.add(new JdbcResourceNodePropertiesConfiguration(properties, resourceId, nodeId, type))) {
        throw new AssertionError();
      }
    }
    return AoCollections.optimalUnmodifiableSet(resourceNodes);
  }

  @Override
  public JdbcResource newResource(AppCluster cluster, Collection<? extends ResourceNode<?, ?>> resourceNodes) throws AppClusterConfigurationException {
    return new JdbcResource(cluster, this, resourceNodes);
  }
}
