package be.nabu.eai.module.cache.provider;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import be.nabu.eai.repository.api.CacheProviderArtifact;
import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.cache.api.Cache;
import be.nabu.libs.cache.api.CacheRefresher;
import be.nabu.libs.cache.api.CacheTimeoutManager;
import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.cache.resources.ResourceCache;
import be.nabu.libs.resources.ResourceUtils;
import be.nabu.libs.resources.URIUtils;
import be.nabu.libs.resources.api.ManageableContainer;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.resources.api.features.CacheableResource;

public class CacheProviderArtifactImpl extends JAXBArtifact<CacheProviderConfiguration> implements CacheProviderArtifact {

	private Map<String, Cache> caches = new HashMap<String, Cache>();
	
	public CacheProviderArtifactImpl(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "cache-provider.xml", CacheProviderConfiguration.class);
	}

	public ManageableContainer<?> getCacheContainer(String id) {
		try {
			URI uri = getConfiguration().getUri();
			if (uri == null) {
				uri = new URI("memory:/cache/" + getId() + "/" + id);
			}
			else {
				uri = URIUtils.getChild(uri, id);
			}
			ManageableContainer<?> cacheContainer = (ManageableContainer<?>) ResourceUtils.mkdir(uri, SystemPrincipal.ROOT);
			if (cacheContainer instanceof CacheableResource) {
				((CacheableResource) cacheContainer).setCaching(getConfiguration().getShared() == null || !getConfiguration().getShared());
			}
			return cacheContainer;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized Cache create(String artifactId, long maxTotalSize, long maxEntrySize, DataSerializer<?> keySerializer, DataSerializer<?> valueSerializer, CacheRefresher refresher, CacheTimeoutManager timeoutManager) {
		ResourceCache resourceCache = new ResourceCache(getCacheContainer(artifactId), maxEntrySize, maxTotalSize, refresher, timeoutManager);
		resourceCache.setKeySerializer(keySerializer);
		resourceCache.setValueSerializer(valueSerializer);
		caches.put(artifactId, resourceCache);
		return resourceCache;
	}

	@Override
	public Cache get(String name) throws IOException {
		return caches.get(name);
	}

	@Override
	public synchronized void remove(String name) throws IOException {
		caches.remove(name);
	}

	@Override
	public Collection<String> getCaches() {
		return caches.keySet();
	}

}
