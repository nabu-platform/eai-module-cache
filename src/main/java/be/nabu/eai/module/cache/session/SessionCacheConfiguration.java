package be.nabu.eai.module.cache.session;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sessionCache")
public class SessionCacheConfiguration {
	private boolean serializeValues;

	public boolean isSerializeValues() {
		return serializeValues;
	}
	public void setSerializeValues(boolean serializeValues) {
		this.serializeValues = serializeValues;
	}
}
