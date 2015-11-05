package be.nabu.eai.module.cache.provider;

import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "cacheProvider")
@XmlType(propOrder = { "uri", "shared" })
public class CacheProviderConfiguration {
	
	private URI uri;
	private Boolean shared;

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public Boolean getShared() {
		return shared;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}

}
