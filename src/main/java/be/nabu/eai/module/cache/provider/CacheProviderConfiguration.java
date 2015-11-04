package be.nabu.eai.module.cache.provider;

import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cacheProvider")
public class CacheProviderConfiguration {
	
	private URI uri;

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
	
}
