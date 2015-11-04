package be.nabu.eai.module.cache;

public class CacheEntry {
	private String serviceId;
	private long currentCacheSize, maxCacheSize, maxEntrySize;
	private int amountOfEntries;
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public long getCurrentCacheSize() {
		return currentCacheSize;
	}
	public void setCurrentCacheSize(long currentCacheSize) {
		this.currentCacheSize = currentCacheSize;
	}
	public long getMaxCacheSize() {
		return maxCacheSize;
	}
	public void setMaxCacheSize(long maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}
	public long getMaxEntrySize() {
		return maxEntrySize;
	}
	public void setMaxEntrySize(long maxEntrySize) {
		this.maxEntrySize = maxEntrySize;
	}
	public int getAmountOfEntries() {
		return amountOfEntries;
	}
	public void setAmountOfEntries(int amountOfEntries) {
		this.amountOfEntries = amountOfEntries;
	}
}
