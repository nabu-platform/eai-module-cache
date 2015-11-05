package be.nabu.eai.module.cache.provider;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class CacheProviderManager extends JAXBArtifactManager<CacheProviderConfiguration, CacheProviderArtifactImpl> {

	public CacheProviderManager() {
		super(CacheProviderArtifactImpl.class);
	}

	@Override
	protected CacheProviderArtifactImpl newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new CacheProviderArtifactImpl(id, container, repository);
	}

}
