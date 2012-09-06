/*
 * Copyright 2012 Midokura PTE LTD.
 */
package com.midokura.midolman.mgmt.rest_api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.midokura.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guice module for REST API.
 */
public class RestApiModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(
            RestApiModule.class);

    @Override
    protected void configure() {
        log.debug("configure: entered.");

        requireBinding(ConfigProvider.class);

        bind(WebApplicationExceptionMapper.class).asEagerSingleton();

        bind(ApplicationResource.class);
        install(new FactoryModuleBuilder().build(ResourceFactory.class));

        log.debug("configure: exiting.");
    }

    @Provides
    RestApiConfig provideRestApiConfig(ConfigProvider provider) {
        log.debug("provideRestApiConfig: entered.");
        return provider.getConfig(RestApiConfig.class);
    }

}