package be.nabu.eai.module.cache.provider;

import java.net.URI;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.resources.ResourceUtils;
import be.nabu.libs.resources.api.ManageableContainer;
import be.nabu.libs.resources.api.ResourceContainer;

public class CacheProviderArtifact extends JAXBArtifact<CacheProviderConfiguration> {

	private ManageableContainer<?> cacheContainer;
	private Repository repository;
	
	public CacheProviderArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, "cache-provider.xml", CacheProviderConfiguration.class);
		this.repository = repository;
	}

	public ManageableContainer<?> getCacheContainer() {
		if (cacheContainer == null) {
			synchronized(this) {
				if (cacheContainer == null) {
					try {
						URI uri = getConfiguration().getUri();
						if (uri == null) {
							uri = new URI("memory:/cache/services/" + getId());
						}
						cacheContainer = (ManageableContainer<?>) ResourceUtils.mkdir(uri, SystemPrincipal.ROOT);
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return cacheContainer;
	}

	public Repository getRepository() {
		return repository;
	}
	
}
