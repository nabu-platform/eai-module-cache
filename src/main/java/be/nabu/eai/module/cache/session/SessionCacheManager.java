package be.nabu.eai.module.cache.session;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class SessionCacheManager extends JAXBArtifactManager<SessionCacheConfiguration, SessionCacheArtifact> {

	public SessionCacheManager() {
		super(SessionCacheArtifact.class);
	}

	@Override
	protected SessionCacheArtifact newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new SessionCacheArtifact(id, container, repository);
	}

}
