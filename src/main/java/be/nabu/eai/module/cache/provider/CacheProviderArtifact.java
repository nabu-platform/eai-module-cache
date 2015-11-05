package be.nabu.eai.module.cache.provider;

import java.net.URI;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.resources.ResourceUtils;
import be.nabu.libs.resources.api.ManageableContainer;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.resources.api.features.CacheableResource;

public class CacheProviderArtifact extends JAXBArtifact<CacheProviderConfiguration> {

	private Repository repository;
	
	public CacheProviderArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, "cache-provider.xml", CacheProviderConfiguration.class);
		this.repository = repository;
	}

	public ManageableContainer<?> getCacheContainer(String id) {
		try {
			URI uri = getConfiguration().getUri();
			if (uri == null) {
				uri = new URI("memory:/cache/" + getId() + "/" + id);
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

	public Repository getRepository() {
		return repository;
	}
	
}
