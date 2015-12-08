package be.nabu.eai.module.cache.service;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class CacheGUIManager extends BaseJAXBGUIManager<CacheConfiguration, CacheArtifact> {

	public CacheGUIManager() {
		super("Service Cache", CacheArtifact.class, new CacheManager(), CacheConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected CacheArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>... values) throws IOException {
		return new CacheArtifact(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	@Override
	public String getCategory() {
		return "Caching";
	}
}
