package be.nabu.eai.module.cache.session;

import java.io.IOException;
import java.util.ArrayList;
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
import be.nabu.libs.resources.api.ResourceContainer;

public class SessionCacheArtifact extends JAXBArtifact<SessionCacheConfiguration> implements CacheProviderArtifact {

	private Map<String, SessionCache> caches = new HashMap<String, SessionCache>();
	
	public SessionCacheArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "session-cache.xml", SessionCacheConfiguration.class);
	}

	@Override
	public Cache get(String name) throws IOException {
		return caches.get(name);
	}

	@Override
	public void remove(String name) throws IOException {
		if (caches.containsKey(name)) {
			synchronized(caches) {
				caches.remove(name);
			}
		}
	}

	@Override
	public Cache create(String artifactId, long maxTotalSize, long maxEntrySize, DataSerializer<?> keySerializer, DataSerializer<?> valueSerializer, CacheRefresher refresher, CacheTimeoutManager timeoutManager) {
		synchronized(caches) {
			caches.put(artifactId, new SessionCache(artifactId, keySerializer, getConfig().isSerializeValues() ? valueSerializer : null));
		}
		return caches.get(artifactId);
	}

	@Override
	public Collection<String> getCaches() {
		return new ArrayList<String>(caches.keySet());
	}

}