package be.nabu.eai.module.cache.provider.memory;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.InterfaceFilter;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;

@XmlRootElement(name = "memoryCache")
public class MemoryCacheProviderConfiguration {
	private boolean serializeValues;
	private boolean cluster;
	private DefinedService annotater;

	public boolean isSerializeValues() {
		return serializeValues;
	}
	public void setSerializeValues(boolean serializeValues) {
		this.serializeValues = serializeValues;
	}
	public boolean isCluster() {
		return cluster;
	}
	public void setCluster(boolean cluster) {
		this.cluster = cluster;
	}
	
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	@InterfaceFilter(implement = "be.nabu.libs.cache.api.CacheAnnotater.annotate")
	public DefinedService getAnnotater() {
		return annotater;
	}
	public void setAnnotater(DefinedService annotater) {
		this.annotater = annotater;
	}
}
