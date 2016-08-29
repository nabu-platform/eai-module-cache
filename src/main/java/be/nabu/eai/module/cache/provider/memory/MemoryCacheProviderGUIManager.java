package be.nabu.eai.module.cache.provider.memory;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class MemoryCacheProviderGUIManager extends BaseJAXBGUIManager<MemoryCacheProviderConfiguration, MemoryCacheProviderArtifact> {

	public MemoryCacheProviderGUIManager() {
		super("Memory Cache Provider", MemoryCacheProviderArtifact.class, new MemoryCacheProviderManager(), MemoryCacheProviderConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected MemoryCacheProviderArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>...values) throws IOException {
		return new MemoryCacheProviderArtifact(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	@Override
	public String getCategory() {
		return "Caching";
	}
	
}
