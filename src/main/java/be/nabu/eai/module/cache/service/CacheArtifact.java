package be.nabu.eai.module.cache.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.artifacts.api.StartableArtifact;
import be.nabu.libs.artifacts.api.StoppableArtifact;
import be.nabu.libs.cache.impl.LastModifiedTimeoutManager;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.services.cache.ComplexContentSerializer;
import be.nabu.libs.services.cache.ServiceRefresher;

public class CacheArtifact extends JAXBArtifact<CacheConfiguration> implements StartableArtifact, StoppableArtifact {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private boolean started;
	private Repository repository;
	
	public CacheArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, "cache.xml", CacheConfiguration.class);
		this.repository = repository;
	}

	@Override
	public void stop() throws IOException {
		if (started && getConfiguration().getCacheProvider() != null && getConfiguration().getService() != null) {
			for (DefinedService service : getConfiguration().getService()) {
				getConfiguration().getCacheProvider().remove(service.getId());
			}
		}
		started = false;
	}

	@Override
	public void start() throws IOException {
		logger.debug("Creating service cache for: " + getId());
		try {
			if (getConfiguration().getService() == null) {
				logger.warn("Can not create cache for '" + getId() + "' because it has no configured service");
			}
			else if (getConfiguration().getCacheProvider() == null) {
				logger.warn("Can not create cache for '" + getId() + "', no cache provider found");
			}
			else if (getConfiguration().getService() != null && !getConfiguration().getService().isEmpty()) {
				for (DefinedService service : getConfiguration().getService()) {
					getConfiguration().getCacheProvider().create(
							service.getId(), 
						// defaults to 100 mb
						getConfiguration().getMaxTotalSize() == null ? 1024*1024*100 : getConfiguration().getMaxTotalSize(),
						// defaults to 10 mb
						getConfiguration().getMaxEntrySize() == null ? 1024*1024*5 : getConfiguration().getMaxEntrySize(),
						new ComplexContentSerializer(service.getServiceInterface().getInputDefinition()),
						new ComplexContentSerializer(service.getServiceInterface().getOutputDefinition()),
						// only set a refresher if we have a refresh timeout set
						getConfiguration().getRefresh() == null || !getConfiguration().getRefresh() ? null : new ServiceRefresher(repository, SystemPrincipal.ROOT, service),
						// defaults to an hour
						new LastModifiedTimeoutManager(getConfiguration().getCacheTimeout() == null ? 1000*60*60 : getConfiguration().getCacheTimeout())
					);
				}
				started = true;
			}
		}
		catch (Exception e) {
			logger.error("Could not create cache for: " + getId(), e);
		}
	}

	@Override
	public boolean isStarted() {
		return started;
	}

}
