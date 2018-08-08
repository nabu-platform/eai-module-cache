package be.nabu.eai.module.cache.provider.memory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.api.CacheProviderArtifact;
import be.nabu.eai.repository.api.ClusteredServer;
import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.artifacts.api.StoppableArtifact;
import be.nabu.libs.cache.api.Cache;
import be.nabu.libs.cache.api.CacheRefresher;
import be.nabu.libs.cache.api.CacheTimeoutManager;
import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.cache.memory.MemoryCache;
import be.nabu.libs.cache.memory.MemoryCacheEntry;
import be.nabu.libs.cluster.api.ClusterInstance;
import be.nabu.libs.cluster.api.Destroyable;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.services.api.ServiceRunner;

public class MemoryCacheProviderArtifact extends JAXBArtifact<MemoryCacheProviderConfiguration> implements CacheProviderArtifact, StoppableArtifact {

	private Map<String, MemoryCache> caches = new HashMap<String, MemoryCache>();
	
	public MemoryCacheProviderArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "memory-cache.xml", MemoryCacheProviderConfiguration.class);
	}

	@Override
	public Cache get(String name) throws IOException {
		return caches.get(name);
	}

	@Override
	public void remove(String name) throws IOException {
		synchronized(caches) {
			MemoryCache remove = caches.remove(name);
			// if it is a destroyable map, destroy it
			if (remove != null && remove.getEntryMap() instanceof Destroyable) {
				((Destroyable) remove.getEntryMap()).destroy();
			}
		}
	}

	@Override
	public Cache create(String artifactId, long maxTotalSize, long maxEntrySize, DataSerializer<?> keySerializer, DataSerializer<?> valueSerializer, CacheRefresher refresher, CacheTimeoutManager timeoutManager) {
		synchronized(caches) {
			Map<Object, MemoryCacheEntry> entries = null;
			if (getConfig().isCluster()) {
				ServiceRunner serviceRunner = EAIResourceRepository.getInstance().getServiceRunner();
				if (serviceRunner instanceof ClusteredServer) {
					ClusterInstance cluster = ((ClusteredServer) serviceRunner).getCluster();
					entries = cluster.map(artifactId);
				}
			}
			if (entries == null) {
				entries = new HashMap<Object, MemoryCacheEntry>();
			}
			MemoryCache cache = new MemoryCache(refresher, timeoutManager, entries, getConfig().isCluster());
			cache.setKeySerializer(keySerializer);
			try {
				if (getConfiguration().isSerializeValues()) {
					cache.setValueSerializer(valueSerializer);
				}
				caches.put(artifactId, cache);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return caches.get(artifactId);
	}

	@Override
	public Collection<String> getCaches() {
		return caches.keySet();
	}

	@Override
	public void stop() throws IOException {
		for (MemoryCache cache : caches.values()) {
			// if it is a destroyable map, destroy it
			if (cache != null && cache.getEntryMap() instanceof Destroyable) {
				((Destroyable) cache.getEntryMap()).destroy();
			}
		}
	}
}
