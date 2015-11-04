package be.nabu.eai.module.cache;

import be.nabu.libs.cache.api.CacheRefresher;
import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.cache.resources.ResourceCache;
import be.nabu.libs.resources.api.ManageableContainer;
import be.nabu.libs.services.api.Service;
import be.nabu.libs.services.cache.ComplexContentSerializer;

public class ServiceAwareResourceCache extends ResourceCache {

	private DataSerializer<?> inputSerializer, outputSerializer;

	public ServiceAwareResourceCache(Service service, ManageableContainer<?> container, long maxEntrySize, long maxCacheSize, long accessTimeout, CacheRefresher cacheRefresher, long refreshTimeout) {
		super(container, maxEntrySize, maxCacheSize, accessTimeout, cacheRefresher, refreshTimeout);
		this.inputSerializer = new ComplexContentSerializer(service.getServiceInterface().getInputDefinition());
		this.outputSerializer = new ComplexContentSerializer(service.getServiceInterface().getOutputDefinition());
	}

	@Override
	protected DataSerializer<?> getKeySerializer(Class<?> keyClass) {
		return inputSerializer;
	}

	@Override
	protected DataSerializer<?> getValueSerializer(Class<?> valueClass) {
		return outputSerializer;
	}
	
}
