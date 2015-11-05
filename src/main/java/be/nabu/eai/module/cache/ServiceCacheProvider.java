package be.nabu.eai.module.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.module.cache.service.CacheArtifact;
import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.cache.api.CacheProvider;
import be.nabu.libs.cache.resources.ResourceCache;
import be.nabu.libs.services.cache.ComplexContentSerializer;
import be.nabu.libs.services.cache.ServiceRefresher;

public class ServiceCacheProvider implements CacheProvider, Iterable<String> {

	private Map<String, ResourceCache> caches = new HashMap<String, ResourceCache>();
	private Logger logger = LoggerFactory.getLogger(getClass());

	public ServiceCacheProvider() {
		buildCaches();
	}
	
	@Override
	public ResourceCache get(String name) {
		return caches.get(name);
	}

	private synchronized void buildCaches() {
		logger.info("Building service caches...");
		EAIResourceRepository repository = EAIResourceRepository.getInstance();
		for (CacheArtifact artifact : repository.getArtifacts(CacheArtifact.class)) {
			logger.debug("Cache artifact: " + artifact.getId());
			try {
				if (artifact.getConfiguration().getCacheProvider() == null) {
					logger.warn("Can not create cache for '" + artifact.getId() + "' because it has no provider");
				}
				else if (artifact.getConfiguration().getService() == null) {
					logger.warn("Can not create cache for '" + artifact.getId() + "' because it has no configured service");
				}
				else {
					ResourceCache cache = new ResourceCache(artifact.getConfiguration().getCacheProvider().getCacheContainer(), 
						// defaults to 10 mb
						artifact.getConfiguration().getMaxEntrySize() == null ? 1024*1024*5 : artifact.getConfiguration().getMaxEntrySize(),
						// defaults to 100 mb
						artifact.getConfiguration().getMaxTotalSize() == null ? 1024*1024*100 : artifact.getConfiguration().getMaxTotalSize(),
						// defaults to an hour
						artifact.getConfiguration().getCacheTimeout() == null ? 1000*60*60 : artifact.getConfiguration().getCacheTimeout(),
						// only set a refresher if we have a refresh timeout set
						artifact.getConfiguration().getRefreshTimeout() == null ? null : new ServiceRefresher(repository, SystemPrincipal.ROOT, artifact.getConfiguration().getService()),
						// defaults to an hour
						artifact.getConfiguration().getRefreshTimeout() == null ? -1 : artifact.getConfiguration().getRefreshTimeout()
					);
					cache.setKeySerializer(new ComplexContentSerializer(artifact.getConfiguration().getService().getServiceInterface().getInputDefinition()));
					cache.setValueSerializer(new ComplexContentSerializer(artifact.getConfiguration().getService().getServiceInterface().getOutputDefinition()));
					caches.put(artifact.getConfiguration().getService().getId(), cache);
				}
			}
			catch (Exception e) {
				logger.error("Could not create cache for: " + artifact.getId(), e);
			}
		}
	}

	@Override
	public synchronized void remove(String name) {
		caches.remove(name);
	}

	@Override
	public Iterator<String> iterator() {
		return new ArrayList<String>(caches.keySet()).iterator();
	}
}
