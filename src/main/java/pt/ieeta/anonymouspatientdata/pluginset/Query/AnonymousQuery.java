package pt.ieeta.anonymouspatientdata.pluginset.Query;

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
		return "Anonymous-Query-Plugin";
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
		if (!enabled || query == null) {return null;}
		return null;
	}

	@Override
	public void setPlatformProxy(DicooglePlatformInterface core) {
		this.platform=core;		
	}

}
