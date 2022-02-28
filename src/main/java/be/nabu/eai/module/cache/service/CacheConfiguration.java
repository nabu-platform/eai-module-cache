package be.nabu.eai.module.cache.service;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.EnvironmentSpecific;
import be.nabu.eai.repository.api.CacheProviderArtifact;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;

@XmlRootElement(name = "cache")
@XmlType(propOrder = { "cacheProvider", "service", "maxEntrySize", "maxTotalSize", "cacheTimeout", "refresh", "enableInDevelopment" })
public class CacheConfiguration {
	
	private CacheProviderArtifact cacheProvider;
	private List<DefinedService> services;
	private Long maxEntrySize, maxTotalSize, cacheTimeout;
	private Boolean refresh;
	private boolean enableInDevelopment = true;
	
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public List<DefinedService> getService() {
		return services;
	}
	public void setService(List<DefinedService> services) {
		this.services = services;
	}
	
	@EnvironmentSpecific
	public Long getMaxEntrySize() {
		return maxEntrySize;
	}
	public void setMaxEntrySize(Long maxEntrySize) {
		this.maxEntrySize = maxEntrySize;
	}
	
	@EnvironmentSpecific
	public Long getMaxTotalSize() {
		return maxTotalSize;
	}
	public void setMaxTotalSize(Long maxTotalSize) {
		this.maxTotalSize = maxTotalSize;
	}
	
	@EnvironmentSpecific
	public Long getCacheTimeout() {
		return cacheTimeout;
	}
	public void setCacheTimeout(Long accessTimeout) {
		this.cacheTimeout = accessTimeout;
	}
	
	@NotNull
	@EnvironmentSpecific
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
	
	public boolean isEnableInDevelopment() {
		return enableInDevelopment;
	}
	public void setEnableInDevelopment(boolean enableInDevelopment) {
		this.enableInDevelopment = enableInDevelopment;
	}
	
}
