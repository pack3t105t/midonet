/*
* Copyright 2012 Midokura Europe SARL
*/
package com.midokura.midolman.guice.cluster;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midokura.midolman.config.ZookeeperConfig;
import com.midokura.midolman.guice.zookeeper.ZKConnectionProvider;
import com.midokura.midolman.host.state.HostZkManager;
import com.midokura.midolman.state.Directory;
import com.midokura.midolman.state.ZkManager;
import com.midokura.midolman.state.zkManagers.AdRouteZkManager;
import com.midokura.midolman.state.zkManagers.BgpZkManager;
import com.midokura.midolman.state.zkManagers.BridgeDhcpZkManager;
import com.midokura.midolman.state.zkManagers.BridgeZkManager;
import com.midokura.midolman.state.zkManagers.ChainZkManager;
import com.midokura.midolman.state.zkManagers.PortGroupZkManager;
import com.midokura.midolman.state.zkManagers.PortZkManager;
import com.midokura.midolman.state.zkManagers.RouteZkManager;
import com.midokura.midolman.state.zkManagers.RouterZkManager;
import com.midokura.midolman.state.zkManagers.RuleZkManager;
import com.midokura.midolman.state.zkManagers.VpnZkManager;
import com.midokura.midonet.cluster.Client;
import com.midokura.midonet.cluster.LocalClientImpl;
import com.midokura.midonet.cluster.services.MidostoreSetupService;
import com.midokura.util.eventloop.Reactor;

public class ClusterClientModule extends PrivateModule {

    private static final Logger log = LoggerFactory
        .getLogger(ClusterClientModule.class);

    @Override
    protected void configure() {
        binder().requireExplicitBindings();

        requireBinding(Directory.class);

        requireBinding(Key.get(Reactor.class, Names.named(
            ZKConnectionProvider.DIRECTORY_REACTOR_TAG)));

        bindManagers();

        bind(Client.class)
            .to(LocalClientImpl.class)
            .asEagerSingleton();
        expose(Client.class);

        bind(MidostoreSetupService.class).in(Singleton.class);
        expose(MidostoreSetupService.class);
    }

    protected void bindManagers() {
        List<Class<? extends ZkManager>> managers = new ArrayList<Class<? extends ZkManager>>();

        managers.add(HostZkManager.class);
        managers.add(BgpZkManager.class);
        managers.add(RouterZkManager.class);
        managers.add(RouteZkManager.class);
        managers.add(RuleZkManager.class);
        managers.add(BridgeDhcpZkManager.class);
        managers.add(BridgeZkManager.class);
        managers.add(ChainZkManager.class);
        managers.add(PortZkManager.class);
        managers.add(AdRouteZkManager.class);
        managers.add(VpnZkManager.class);
        managers.add(PortGroupZkManager.class);

        for (Class<? extends ZkManager> managerClass : managers) {
            //noinspection unchecked
            bind(managerClass)
                .toProvider(new ZkManagerProvider(managerClass))
                .asEagerSingleton();
            expose(managerClass);
        }
    }

    private static class ZkManagerProvider<T extends ZkManager>
        implements Provider<T> {

        @Inject
        Directory directory;

        @Inject
        ZookeeperConfig config;

        Class<T> managerClass;

        protected ZkManagerProvider(Class<T> managerClass) {
            this.managerClass = managerClass;
        }

        @Override
        public T get() {
            try {
                Constructor<T> constructor =
                    managerClass.getConstructor(Directory.class, String.class);

                return
                    constructor.newInstance(
                        directory,
                        config.getMidolmanRootKey());

            } catch (Exception e) {
                throw new RuntimeException(
                    "Could not create zkManager of class: " + managerClass, e);
            }
        }
    }
}