package be.nabu.eai.module.cache.provider;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class CacheProviderGUIManager extends BaseJAXBGUIManager<CacheProviderConfiguration, CacheProviderArtifactImpl> {

	public CacheProviderGUIManager() {
		super("Cache Provider", CacheProviderArtifactImpl.class, new CacheProviderManager(), CacheProviderConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected CacheProviderArtifactImpl newInstance(MainController controller, RepositoryEntry entry, Value<?>...values) throws IOException {
		return new CacheProviderArtifactImpl(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	@Override
	public String getCategory() {
		return "Caching";
	}
	
}
