/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

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
