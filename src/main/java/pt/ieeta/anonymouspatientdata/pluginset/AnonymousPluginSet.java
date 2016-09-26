/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  AnonymousPluginSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AnonymousPluginSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */


package pt.ieeta.anonymouspatientdata.pluginset;




import pt.ieeta.anonymouspatientdata.core.impl.MatchTables;
import pt.ieeta.anonymouspatientdata.pluginset.storage.AnonymousStorage;
import pt.ua.dicoogle.sdk.GraphicalInterface;
import pt.ua.dicoogle.sdk.IndexerInterface;
import pt.ua.dicoogle.sdk.JettyPluginInterface;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */

import pt.ua.dicoogle.sdk.PluginSet;// TODO implement
import pt.ua.dicoogle.sdk.QueryInterface;// TODO implement
import pt.ua.dicoogle.sdk.StorageInterface; // TODO implement
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder; // TODO implement


import java.util.Collection;
import java.util.Collections;

import org.restlet.resource.ServerResource;

import net.xeoh.plugins.base.annotations.PluginImplementation;



@SuppressWarnings("deprecation")
@PluginImplementation
public class AnonymousPluginSet implements PluginSet, PlatformCommunicatorInterface {

	protected DicooglePlatformInterface platform;
	private ConfigurationHolder settings;


	@Override
	public void setPlatformProxy(DicooglePlatformInterface core) {
		this.platform=core;
	}


	@Override
	public String getName() {
		return "Anonymous-Wrapper-Plugin";
	}

	private AnonymousStorage storage= new AnonymousStorage();
	private String dbLocation="jdbc:sqlite:AnonymousInformation.db"; 

	@Override
	public Collection<StorageInterface> getStoragePlugins() {
		return Collections.singleton((StorageInterface) this.storage);
	}


	@Override
	public Collection<GraphicalInterface> getGraphicalPlugins() {
		return Collections.emptyList();
	}

	@Override
	public Collection<IndexerInterface> getIndexPlugins() {
		return Collections.emptyList();
	}

	@Override
	public Collection<JettyPluginInterface> getJettyPlugins() {
		return Collections.emptyList();
	}

	@Override
	public Collection<QueryInterface> getQueryPlugins() {
		return Collections.emptyList();
	}

	@Override
	public Collection<ServerResource> getRestPlugins() {
		return Collections.emptyList();
	}

	@Override
	public ConfigurationHolder getSettings() {
		return this.settings;
	}

	@Override
	public void setSettings(ConfigurationHolder arg0) {
		this.setLocation(arg0.getConfiguration().getString("Location","./Anon_index/"));
	MatchTables.getInstance().bootstrapDataBase(getLocation());
		this.settings =arg0;
	}

	@Override
	public void shutdown() {
	}

	public String getLocation() {
		return dbLocation;
	}

	public void setLocation(String Location) {
		this.dbLocation = Location;
	}




}
