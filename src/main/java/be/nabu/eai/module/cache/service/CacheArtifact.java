package be.nabu.eai.module.cache.service;

import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.resources.api.ResourceContainer;

public class CacheArtifact extends JAXBArtifact<CacheConfiguration> {

	public CacheArtifact(String id, ResourceContainer<?> directory) {
		super(id, directory, "cache.xml", CacheConfiguration.class);
	}

}
