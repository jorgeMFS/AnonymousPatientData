/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  AnonymousQuery is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AnonymousQuery is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.pluginset.Query;
import java.util.Objects;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ieeta.anonymouspatientdata.pluginset.storage.AnonymousStorage;
import pt.ua.dicoogle.sdk.QueryInterface;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;
import pt.ua.dicoogle.sdk.datastructs.SearchResult;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;

public class AnonymousQuery implements QueryInterface, PlatformCommunicatorInterface {

	private static final Logger logger = LoggerFactory.getLogger(AnonymousStorage.class);
	private ConfigurationHolder settings;
	private boolean enabled=true;
	protected DicooglePlatformInterface platform;
	AnonymousQuery(){
		
		super();
		logger.info("Initializing -> Anonymous Query");
	}
	
	@Override
	public String getName() {
		return "Anonymous-Wrapper-Plugin";
	}

	@Override
	public boolean enable() {
		return this.enabled=true;
	}

	@Override
	public boolean disable() {
		return this.enabled=false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setSettings(ConfigurationHolder settings) {
		// TODO Auto-generated method stub

	}

	@Override
	public ConfigurationHolder getSettings() {
		return this.settings;
	}

	@Override
	public Iterable<SearchResult> query(String query, Object... parameters) {
		Objects.requireNonNull(query);
		return null;
	}

	@Override
	public void setPlatformProxy(DicooglePlatformInterface core) {
		this.platform=core;		
	}

}
