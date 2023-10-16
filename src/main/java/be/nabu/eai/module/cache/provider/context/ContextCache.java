package be.nabu.eai.module.cache.provider.context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import be.nabu.libs.cache.api.Cache;
import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.services.ServiceRuntime;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ContextCache implements Cache {

	private DataSerializer keySerializer;
	private DataSerializer valueSerializer;
	private String identifier;

	public ContextCache(String identifier, DataSerializer<?> keySerializer, DataSerializer<?> valueSerializer) {
		this.identifier = identifier;
		this.keySerializer = keySerializer;
		this.valueSerializer = valueSerializer;
	}
	
	private Map<String, Object> getContext() {
		ServiceRuntime runtime = ServiceRuntime.getRuntime();
		if (runtime != null) {
			Map<String, Object> context = runtime.getContext();
			Map<String, Object> cache = (Map<String, Object>) context.get("context-cache:" + identifier);
			if (cache == null) {
				cache = new HashMap<String, Object>();
				context.put("context-cache:" + identifier, cache);
			}
			return cache;
		}
		return null;
	}
	
	@Override
	public boolean put(Object key, Object value) throws IOException {
		Map<String, Object> context = getContext();
		if (context != null) {
			if (!(key instanceof String)) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				keySerializer.serialize(key, output);
				key = new String(output.toByteArray());
			}
			if (valueSerializer != null) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				valueSerializer.serialize(value, output);
				value = output.toByteArray();
			}
			if (value == null) {
				context.remove(key.toString());
			}
			else {
				context.put(key.toString(), value);
			}
			return true;
		}
		return false;
	}

	@Override
	public Object get(Object key) throws IOException {
		Map<String, Object> context = getContext();
		if (context != null) {
			if (!(key instanceof String)) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				keySerializer.serialize(key, output);
				key = new String(output.toByteArray());
			}
			Object value = context.get(key.toString());
			if (value != null && valueSerializer != null) {
				value = valueSerializer.deserialize(new ByteArrayInputStream((byte[]) value));
			}
			return value;
		}
		return null;
	}

	@Override
	public void clear(Object key) throws IOException {
		put(key, null);
	}

	@Override
	public void clear() throws IOException {
		Map<String, Object> context = getContext();
		if (context != null) {
			context.clear();
		}
	}

	@Override
	public void prune() throws IOException {
		// do nothing
	}

	@Override
	public void refresh() throws IOException {
		// do nothing
	}

	@Override
	public void refresh(Object key) throws IOException {
		// do nothing
	}

}
