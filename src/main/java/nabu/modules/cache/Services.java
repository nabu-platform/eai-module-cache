package nabu.modules.cache;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import be.nabu.eai.api.Eager;
import be.nabu.eai.api.Hidden;
import be.nabu.eai.module.cache.CacheEntry;
import be.nabu.eai.module.cache.ServiceCacheProvider;
import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.libs.cache.resources.ResourceCache;

@WebService
public class Services {

	@Hidden
	@Eager
	public void install() {
		EAIResourceRepository.getInstance().getServiceRunner().setCacheProvider(new ServiceCacheProvider());
	}
	
	public List<CacheEntry> getEntries() {
		List<CacheEntry> entries = new ArrayList<CacheEntry>();
		if (EAIResourceRepository.getInstance().getServiceRunner().getCacheProvider() instanceof ServiceCacheProvider) {
			ServiceCacheProvider cacheProvider = (ServiceCacheProvider) EAIResourceRepository.getInstance().getServiceRunner().getCacheProvider();
			for (String name : cacheProvider) {
				CacheEntry entry = new CacheEntry();
				ResourceCache cache = cacheProvider.get(name);
				entry.setServiceId(name);
				entry.setMaxCacheSize(cache.getMaxCacheSize());
				entry.setAmountOfEntries(cache.getAmountOfEntries());
				entry.setCurrentCacheSize(cache.getSize());
				entry.setMaxEntrySize(cache.getMaxEntrySize());
				entries.add(entry);
			}
		}
		return entries;
	}
}
