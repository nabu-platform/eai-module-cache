package be.nabu.eai.module.cache.provider.memory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import be.nabu.eai.repository.api.CacheProviderArtifact;
import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.cache.api.Cache;
import be.nabu.libs.cache.api.CacheRefresher;
import be.nabu.libs.cache.api.CacheTimeoutManager;
import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.cache.memory.MemoryCache;
import be.nabu.libs.resources.api.ResourceContainer;

public class MemoryCacheProviderArtifact extends JAXBArtifact<MemoryCacheProviderConfiguration> implements CacheProviderArtifact {

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
			caches.remove(name);
		}
	}

	@Override
	public Cache create(String artifactId, long maxTotalSize, long maxEntrySize, DataSerializer<?> keySerializer, DataSerializer<?> valueSerializer, CacheRefresher refresher, CacheTimeoutManager timeoutManager) {
		synchronized(caches) {
			MemoryCache cache = new MemoryCache(refresher, timeoutManager);
			cache.setKeySerializer(keySerializer);
			try {
				if (getConfiguration().getSerializeValues() != null && getConfiguration().getSerializeValues()) {
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
}
