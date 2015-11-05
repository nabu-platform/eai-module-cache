package nabu.modules.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.validation.constraints.NotNull;

import be.nabu.eai.api.Eager;
import be.nabu.eai.api.Hidden;
import be.nabu.eai.module.cache.CacheEntryOverview;
import be.nabu.eai.module.cache.CacheOverview;
import be.nabu.eai.module.cache.ServiceCacheProvider;
import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.libs.cache.api.Cache;
import be.nabu.libs.cache.resources.ResourceCache;
import be.nabu.libs.resources.api.AccessTrackingResource;
import be.nabu.libs.resources.api.FiniteResource;
import be.nabu.libs.resources.api.Resource;
import be.nabu.libs.resources.api.TimestampedResource;

@WebService
public class Services {

	@Hidden
	@Eager
	public void install() {
		EAIResourceRepository.getInstance().getServiceRunner().setCacheProvider(new ServiceCacheProvider());
	}
	
	public List<CacheOverview> getCaches() {
		List<CacheOverview> cacheOverviews = new ArrayList<CacheOverview>();
		if (EAIResourceRepository.getInstance().getServiceRunner().getCacheProvider() instanceof ServiceCacheProvider) {
			ServiceCacheProvider cacheProvider = (ServiceCacheProvider) EAIResourceRepository.getInstance().getServiceRunner().getCacheProvider();
			for (String name : cacheProvider) {
				CacheOverview cacheOverview = new CacheOverview();
				ResourceCache cache = cacheProvider.get(name);
				cacheOverview.setServiceId(name);
				cacheOverview.setMaxCacheSize(cache.getMaxCacheSize());
				cacheOverview.setCurrentCacheSize(cache.getSize());
				cacheOverview.setMaxEntrySize(cache.getMaxEntrySize());
				List<CacheEntryOverview> cacheEntries = new ArrayList<CacheEntryOverview>();
				for (Resource resource : cache.getEntries()) {
					CacheEntryOverview cacheEntry = new CacheEntryOverview();
					String keyValue = resource.getName().replaceAll("\\..*$", "");
					if (resource instanceof AccessTrackingResource) {
						cacheEntry.setLastAccessed(((AccessTrackingResource) resource).getLastAccessed());
					}
					if (resource instanceof TimestampedResource) {
						cacheEntry.setLastModified(((TimestampedResource) resource).getLastModified());
					}
					if (resource instanceof FiniteResource) {
						cacheEntry.setSize(((FiniteResource) resource).getSize() + keyValue.length());
					}
					cacheEntries.add(cacheEntry);
				}
				cacheOverview.setEntries(cacheEntries);
				cacheOverviews.add(cacheOverview);
			}
		}
		return cacheOverviews;
	}
	
	public void prune(@WebParam(name = "serviceId") @NotNull String serviceId) throws IOException {
		Cache cache = EAIResourceRepository.getInstance().getServiceRunner().getCacheProvider().get(serviceId);
		if (cache != null) {
			cache.prune();
		}
	}
	
	public void refresh(@WebParam(name = "serviceId") @NotNull String serviceId) throws IOException {
		Cache cache = EAIResourceRepository.getInstance().getServiceRunner().getCacheProvider().get(serviceId);
		if (cache != null) {
			cache.refresh();
		}
	}
	
	public void clear(@WebParam(name = "serviceId") @NotNull String serviceId) throws IOException {
		Cache cache = EAIResourceRepository.getInstance().getServiceRunner().getCacheProvider().get(serviceId);
		if (cache != null) {
			cache.clear();
		}
	}
}
