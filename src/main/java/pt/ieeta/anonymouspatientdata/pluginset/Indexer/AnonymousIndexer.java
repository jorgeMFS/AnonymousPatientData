package pt.ieeta.anonymouspatientdata.pluginset.Indexer;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ieeta.anonymouspatientdata.pluginset.storage.AnonymousStorage;
import pt.ua.dicoogle.sdk.IndexerInterface;
import pt.ua.dicoogle.sdk.StorageInputStream;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;
import pt.ua.dicoogle.sdk.datastructs.Report;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;
import pt.ua.dicoogle.sdk.task.Task;

public class AnonymousIndexer implements IndexerInterface, PlatformCommunicatorInterface {
	private static final Logger logger = LoggerFactory.getLogger(AnonymousStorage.class);
	private ConfigurationHolder settings;
	private boolean enabled=true;
	private String index="Lucene";
	protected DicooglePlatformInterface platform;


	AnonymousIndexer(){

		super();
		logger.info("Initializing -> Anonymous Indexer");
	}

	@Override
	public String getName() {
		return "Anonymous-Indexer-Plugin";
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
		this.index=settings.getConfiguration().getString("index","Lucene");
		this.settings=settings;
	}

	@Override
	public ConfigurationHolder getSettings() {
		return this.settings;
	}

	@Override
	public void setPlatformProxy(DicooglePlatformInterface core) {
		this.platform=core;	
	}

	@Override
	public Task<Report> index(StorageInputStream file, Object... parameters) {
		return this.platform.requestIndexPlugin(this.index).index(file, parameters);
	}

	@Override
	public Task<Report> index(Iterable<StorageInputStream> files, Object... parameters) {
		return this.platform.requestIndexPlugin(this.index).index(files, parameters);
	}

	@Override
	public boolean handles(URI path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unindex(URI path) {
		// TODO Auto-generated method stub
		return false;
	}

}
