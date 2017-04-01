package be.nabu.eai.module.cache.session;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class SessionCacheGUIManager extends BaseJAXBGUIManager<SessionCacheConfiguration, SessionCacheArtifact> {

	public SessionCacheGUIManager() {
		super("Session Cache", SessionCacheArtifact.class, new SessionCacheManager(), SessionCacheConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected SessionCacheArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>...values) throws IOException {
		return new SessionCacheArtifact(entry.getId(), entry.getContainer(), entry.getRepository());
	}
	
	@Override
	public String getCategory() {
		return "Caching";
	}

}
