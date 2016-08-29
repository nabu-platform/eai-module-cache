package be.nabu.eai.module.cache.provider.memory;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class MemoryCacheProviderManager extends JAXBArtifactManager<MemoryCacheProviderConfiguration, MemoryCacheProviderArtifact> {

	public MemoryCacheProviderManager() {
		super(MemoryCacheProviderArtifact.class);
	}

	@Override
	protected MemoryCacheProviderArtifact newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new MemoryCacheProviderArtifact(id, container, repository);
	}

}
