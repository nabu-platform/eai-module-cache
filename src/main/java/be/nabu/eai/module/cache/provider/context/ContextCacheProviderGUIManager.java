package be.nabu.eai.module.cache.provider.context;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class ContextCacheProviderGUIManager extends BaseJAXBGUIManager<ContextCacheProviderConfiguration, ContextCacheProviderArtifact> {

	public ContextCacheProviderGUIManager() {
		super("Context Cache Provider", ContextCacheProviderArtifact.class, new ContextCacheProviderManager(), ContextCacheProviderConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected ContextCacheProviderArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>...values) throws IOException {
		return new ContextCacheProviderArtifact(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	@Override
	public String getCategory() {
		return "Caching";
	}
	
}
