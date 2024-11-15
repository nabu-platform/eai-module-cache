/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.eai.module.cache.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.api.LicenseManager;
import be.nabu.eai.repository.api.LicensedRepository;
import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.artifacts.api.StartableArtifact;
import be.nabu.libs.artifacts.api.StoppableArtifact;
import be.nabu.libs.cache.impl.LastModifiedTimeoutManager;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.services.cache.ComplexContentSerializer;
import be.nabu.libs.services.cache.ServiceRefresher;

public class CacheArtifact extends JAXBArtifact<CacheConfiguration> implements StartableArtifact, StoppableArtifact {

	public static final String MODULE = "nabu.misc.cache";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private boolean started;
	
	public CacheArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "cache.xml", CacheConfiguration.class);
	}

	@Override
	public void stop() throws IOException {
		if (started && getConfiguration().getCacheProvider() != null && getConfiguration().getService() != null) {
			// @2022-03-22: due to a bug clustered caches were never reset correctly on shutdown. this bug has been fixed but we wanted to keep this behavior, in the future we might reenable it with a checkbox
//			for (DefinedService service : getConfiguration().getService()) {
//				if (service == null) {
//					continue;
//				}
//				getConfiguration().getCacheProvider().remove(service.getId());
//			}
		}
		started = false;
	}

	@Override
	public void start() throws IOException {
		boolean licensed = true;
		if (getRepository() instanceof LicensedRepository) {
			LicenseManager licenseManager = ((LicensedRepository) getRepository()).getLicenseManager();
			licensed = licenseManager != null && licenseManager.isLicensed(MODULE);
			if (!licensed) {
				logger.warn("No license found for the cache module, service caches are disabled");
			}
		}
		logger.debug("Creating service cache for: " + getId());
		try {
			if (getConfiguration().getService() == null) {
				logger.warn("Can not create cache for '" + getId() + "' because it has no configured service");
			}
			else if (getConfiguration().getCacheProvider() == null) {
				logger.warn("Can not create cache for '" + getId() + "', no cache provider found");
			}
			else if (licensed && getConfiguration().getService() != null && !getConfiguration().getService().isEmpty()) {
				// only enable if we are not in development modus or if we allow it in development modus
				if (getConfig().isEnableInDevelopment() || !EAIResourceRepository.isDevelopment()) {
					for (DefinedService service : getConfiguration().getService()) {
						if (service == null) {
							continue;
						}
						getConfiguration().getCacheProvider().create(
								service.getId(), 
							// defaults to 100 mb
							getConfiguration().getMaxTotalSize() == null ? 1024*1024*100 : getConfiguration().getMaxTotalSize(),
							// defaults to 10 mb
							getConfiguration().getMaxEntrySize() == null ? 1024*1024*5 : getConfiguration().getMaxEntrySize(),
							new ComplexContentSerializer(service.getServiceInterface().getInputDefinition()),
							new ComplexContentSerializer(service.getServiceInterface().getOutputDefinition()),
							// only set a refresher if we have a refresh timeout set
							getConfiguration().getRefresh() == null || !getConfiguration().getRefresh() ? null : new ServiceRefresher(getRepository(), SystemPrincipal.ROOT, service),
							// defaults to an hour
							new LastModifiedTimeoutManager(getConfiguration().getCacheTimeout() == null ? 1000*60*60 : getConfiguration().getCacheTimeout())
						);
					}
				}
				started = true;
			}
		}
		catch (Exception e) {
			logger.error("Could not create cache for: " + getId(), e);
		}
	}

	@Override
	public boolean isStarted() {
		return started;
	}

}
