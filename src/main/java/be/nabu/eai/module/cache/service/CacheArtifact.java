package be.nabu.eai.module.cache.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.artifacts.api.StartableArtifact;
import be.nabu.libs.artifacts.api.StoppableArtifact;
import be.nabu.libs.cache.impl.LastModifiedTimeoutManager;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.services.cache.ComplexContentSerializer;
import be.nabu.libs.services.cache.ServiceRefresher;

public class CacheArtifact extends JAXBArtifact<CacheConfiguration> implements StartableArtifact, StoppableArtifact {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private boolean started;
	
	public CacheArtifact(String id, ResourceContainer<?> directory) {
		super(id, directory, "cache.xml", CacheConfiguration.class);
	}

	@Override
	public void stop() throws IOException {
		if (started && getConfiguration().getCacheProvider() != null) {
			getConfiguration().getCacheProvider().remove(getConfiguration().getService().getId());
		}
	}

	@Override
	public void start() throws IOException {
		logger.debug("Creating service cache for: " + getId());
		EAIResourceRepository repository = EAIResourceRepository.getInstance();
		try {
			if (getConfiguration().getService() == null) {
				logger.warn("Can not create cache for '" + getId() + "' because it has no configured service");
			}
			else if (getConfiguration().getCacheProvider() == null) {
				logger.warn("Can not create cache for '" + getId() + "', no cache provider found");
			}
			else {
				getConfiguration().getCacheProvider().create(
					getConfiguration().getService().getId(), 
					// defaults to 100 mb
					getConfiguration().getMaxTotalSize() == null ? 1024*1024*100 : getConfiguration().getMaxTotalSize(),
					// defaults to 10 mb
					getConfiguration().getMaxEntrySize() == null ? 1024*1024*5 : getConfiguration().getMaxEntrySize(),
					new ComplexContentSerializer(getConfiguration().getService().getServiceInterface().getInputDefinition()),
					new ComplexContentSerializer(getConfiguration().getService().getServiceInterface().getOutputDefinition()),
					// only set a refresher if we have a refresh timeout set
					getConfiguration().getRefresh() == null || !getConfiguration().getRefresh() ? null : new ServiceRefresher(repository, SystemPrincipal.ROOT, getConfiguration().getService()),
					// defaults to an hour
					new LastModifiedTimeoutManager(getConfiguration().getCacheTimeout() == null ? 1000*60*60 : getConfiguration().getCacheTimeout())
				);
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
