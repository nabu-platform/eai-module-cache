package be.nabu.eai.module.cache.service;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class CacheManager extends JAXBArtifactManager<CacheConfiguration, CacheArtifact> {

	public CacheManager() {
		super(CacheArtifact.class);
	}

	@Override
	protected CacheArtifact newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new CacheArtifact(id, container);
	}

}
