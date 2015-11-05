package nabu.modules.cache;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.api.Eager;
import be.nabu.eai.api.Hidden;
import be.nabu.eai.module.cache.service.CacheArtifact;
import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.services.cache.ComplexContentSerializer;
import be.nabu.libs.services.cache.ServiceRefresher;

@WebService
public class Services {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Hidden
	@Eager
	public void initialize() {
		logger.info("Building service caches...");
		EAIResourceRepository repository = EAIResourceRepository.getInstance();
		for (CacheArtifact artifact : repository.getArtifacts(CacheArtifact.class)) {
			logger.debug("Cache artifact: " + artifact.getId());
			try {
				if (artifact.getConfiguration().getService() == null) {
					logger.warn("Can not create cache for '" + artifact.getId() + "' because it has no configured service");
				}
				else if (artifact.getConfiguration().getCacheProvider() == null) {
					logger.warn("Can not create cache for '" + artifact.getId() + "', no cache provider found");
				}
				else {
					artifact.getConfiguration().getCacheProvider().create(
						artifact.getConfiguration().getService().getId(), 
						// defaults to 100 mb
						artifact.getConfiguration().getMaxTotalSize() == null ? 1024*1024*100 : artifact.getConfiguration().getMaxTotalSize(),
						// defaults to 10 mb
						artifact.getConfiguration().getMaxEntrySize() == null ? 1024*1024*5 : artifact.getConfiguration().getMaxEntrySize(),
						// defaults to an hour
						artifact.getConfiguration().getCacheTimeout() == null ? 1000*60*60 : artifact.getConfiguration().getCacheTimeout(),
						new ComplexContentSerializer(artifact.getConfiguration().getService().getServiceInterface().getInputDefinition()),
						new ComplexContentSerializer(artifact.getConfiguration().getService().getServiceInterface().getOutputDefinition()),
						// only set a refresher if we have a refresh timeout set
						artifact.getConfiguration().getRefresh() == null || !artifact.getConfiguration().getRefresh() ? null : new ServiceRefresher(repository, SystemPrincipal.ROOT, artifact.getConfiguration().getService())
					);
				}
			}
			catch (Exception e) {
				logger.error("Could not create cache for: " + artifact.getId(), e);
			}
		}
	}
}
