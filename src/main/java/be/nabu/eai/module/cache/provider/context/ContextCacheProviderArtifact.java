/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.eai.module.cache.provider.context;

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

public class ContextCacheProviderArtifact extends JAXBArtifact<ContextCacheProviderConfiguration> implements CacheProviderArtifact {
	
	private Map<String, ContextCache> caches = new HashMap<String, ContextCache>();
	
	public ContextCacheProviderArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "context-cache.xml", ContextCacheProviderConfiguration.class);
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
			caches.put(artifactId, new ContextCache(artifactId, keySerializer, null));
		}
		return caches.get(artifactId);
	}

	@Override
	public Collection<String> getCaches() {
		return new ArrayList<String>(caches.keySet());
	}

}
