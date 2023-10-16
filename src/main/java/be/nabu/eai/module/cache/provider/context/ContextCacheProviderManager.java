package be.nabu.eai.module.cache.provider.context;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class ContextCacheProviderManager extends JAXBArtifactManager<ContextCacheProviderConfiguration, ContextCacheProviderArtifact> {

	public ContextCacheProviderManager() {
		super(ContextCacheProviderArtifact.class);
	}

	@Override
	protected ContextCacheProviderArtifact newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new ContextCacheProviderArtifact(id, container, repository);
	}

}
