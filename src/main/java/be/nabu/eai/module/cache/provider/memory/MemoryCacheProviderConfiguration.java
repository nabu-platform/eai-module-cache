package be.nabu.eai.module.cache.provider.memory;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "memoryCache")
public class MemoryCacheProviderConfiguration {
	private boolean serializeValues;
	private boolean cluster;

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
}
