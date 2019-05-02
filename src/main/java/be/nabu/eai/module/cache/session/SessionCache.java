package be.nabu.eai.module.cache.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import be.nabu.libs.cache.api.Cache;
import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.http.api.server.Session;
import be.nabu.libs.services.ServiceRuntime;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SessionCache implements Cache {

	private DataSerializer keySerializer;
	private DataSerializer valueSerializer;
	private String identifier;

	public SessionCache(String identifier, DataSerializer<?> keySerializer, DataSerializer<?> valueSerializer) {
		this.identifier = identifier;
		this.keySerializer = keySerializer;
		this.valueSerializer = valueSerializer;
	}
	
	private Session getSession() {
		ServiceRuntime runtime = ServiceRuntime.getRuntime();
		if (runtime != null) {
			Object session = runtime.getContext().get("session");
			return session instanceof Session ? (Session) session : null;
		}
		return null;
	}
	
	@Override
	public boolean put(Object key, Object value) throws IOException {
		Session session = getSession();
		if (session != null) {
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
			session.set(identifier + "::" + key, value);
			return true;
		}
		return false;
	}

	@Override
	public Object get(Object key) throws IOException {
		Session session = getSession();
		if (session != null) {
			if (!(key instanceof String)) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				keySerializer.serialize(key, output);
				key = new String(output.toByteArray());
			}
			Object value = session.get(identifier + "::" + key);
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
		Session session = getSession();
		if (session != null) {
			for (String key : session) {
				session.set(key, null);
			}
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
