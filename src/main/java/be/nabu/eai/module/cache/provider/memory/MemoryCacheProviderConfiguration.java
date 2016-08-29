package be.nabu.eai.module.cache.provider.memory;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "memoryCache")
public class MemoryCacheProviderConfiguration {
	private Boolean serializeValues;

	public Boolean getSerializeValues() {
		return serializeValues;
	}
	public void setSerializeValues(Boolean serializeValues) {
		this.serializeValues = serializeValues;
	}
	
}
