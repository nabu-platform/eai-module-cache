package be.nabu.eai.module.cache.service;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.module.cache.provider.CacheProviderArtifact;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;

@XmlRootElement(name = "cache")
@XmlType(propOrder = { "cacheProvider", "service", "maxEntrySize", "maxTotalSize", "accessTimeout", "refreshTimeout" })
public class CacheConfiguration {
	
	private CacheProviderArtifact cacheProvider;
	private DefinedService service;
	private Long maxEntrySize, maxTotalSize, accessTimeout, refreshTimeout;
	
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getService() {
		return service;
	}
	public void setService(DefinedService service) {
		this.service = service;
	}
	public Long getMaxEntrySize() {
		return maxEntrySize;
	}
	public void setMaxEntrySize(Long maxEntrySize) {
		this.maxEntrySize = maxEntrySize;
	}
	public Long getMaxTotalSize() {
		return maxTotalSize;
	}
	public void setMaxTotalSize(Long maxTotalSize) {
		this.maxTotalSize = maxTotalSize;
	}
	public Long getAccessTimeout() {
		return accessTimeout;
	}
	public void setAccessTimeout(Long accessTimeout) {
		this.accessTimeout = accessTimeout;
	}
	public Long getRefreshTimeout() {
		return refreshTimeout;
	}
	public void setRefreshTimeout(Long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
	}
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public CacheProviderArtifact getCacheProvider() {
		return cacheProvider;
	}
	public void setCacheProvider(CacheProviderArtifact cacheProvider) {
		this.cacheProvider = cacheProvider;
	}
	
}
