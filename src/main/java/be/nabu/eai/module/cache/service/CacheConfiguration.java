package be.nabu.eai.module.cache.service;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.repository.api.CacheProviderArtifact;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;

@XmlRootElement(name = "cache")
@XmlType(propOrder = { "cacheProvider", "service", "maxEntrySize", "maxTotalSize", "cacheTimeout", "refresh" })
public class CacheConfiguration {
	
	private CacheProviderArtifact cacheProvider;
	private List<DefinedService> services;
	private Long maxEntrySize, maxTotalSize, cacheTimeout;
	private Boolean refresh;
	
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public List<DefinedService> getService() {
		return services;
	}
	public void setService(List<DefinedService> services) {
		this.services = services;
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
	public Long getCacheTimeout() {
		return cacheTimeout;
	}
	public void setCacheTimeout(Long accessTimeout) {
		this.cacheTimeout = accessTimeout;
	}
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public CacheProviderArtifact getCacheProvider() {
		return cacheProvider;
	}
	public void setCacheProvider(CacheProviderArtifact cacheProvider) {
		this.cacheProvider = cacheProvider;
	}
	public Boolean getRefresh() {
		return refresh;
	}
	public void setRefresh(Boolean refresh) {
		this.refresh = refresh;
	}
}
