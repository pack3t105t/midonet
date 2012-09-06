/*
 * Copyright 2012 Midokura PTE LTD.
 */
package com.midokura.midolman.mgmt.auth;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.midokura.config.ConfigProvider;
import com.midokura.midolman.mgmt.auth.cors.CorsConfig;
import com.midokura.midolman.mgmt.auth.keystone.KeystoneConfig;
import com.midokura.midolman.mgmt.bgp.auth.AdRouteAuthorizer;
import com.midokura.midolman.mgmt.bgp.auth.BgpAuthorizer;
import com.midokura.midolman.mgmt.filter.auth.ChainAuthorizer;
import com.midokura.midolman.mgmt.filter.auth.RuleAuthorizer;
import com.midokura.midolman.mgmt.network.auth.*;
import com.midokura.midolman.mgmt.vpn.auth.VpnAuthorizer;

/**
 * Auth bindings.
 */
public class AuthModule extends AbstractModule {

    @Override
    protected void configure() {

        requireBinding(ConfigProvider.class);

        bind(AuthClient.class).toProvider(
                AuthClientProvider.class).asEagerSingleton();

        bind(AdRouteAuthorizer.class).asEagerSingleton();
        bind(BgpAuthorizer.class).asEagerSingleton();
        bind(BridgeAuthorizer.class).asEagerSingleton();
        bind(ChainAuthorizer.class).asEagerSingleton();
        bind(PortAuthorizer.class).asEagerSingleton();
        bind(PortGroupAuthorizer.class).asEagerSingleton();
        bind(RouteAuthorizer.class).asEagerSingleton();
        bind(RouterAuthorizer.class).asEagerSingleton();
        bind(RuleAuthorizer.class).asEagerSingleton();
        bind(VpnAuthorizer.class).asEagerSingleton();

    }

    @Provides
    @Inject
    KeystoneConfig provideKeystoneConfig(ConfigProvider provider) {
        return provider.getConfig(KeystoneConfig.class);
    }

    @Provides
    @Inject
    CorsConfig provideCorsConfig(ConfigProvider provider) {
        return provider.getConfig(CorsConfig.class);
    }

}