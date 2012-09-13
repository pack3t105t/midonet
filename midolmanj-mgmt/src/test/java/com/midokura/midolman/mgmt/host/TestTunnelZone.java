/*
 * Copyright 2012 Midokura PTE LTD.
 */
package com.midokura.midolman.mgmt.host;

import com.midokura.midolman.host.state.HostZkManager;
import com.midokura.midolman.mgmt.VendorMediaType;
import com.midokura.midolman.mgmt.host.rest_api.HostTopology;
import com.midokura.midolman.mgmt.rest_api.DtoWebResource;
import com.midokura.midolman.mgmt.rest_api.FuncTest;
import com.midokura.midolman.mgmt.zookeeper.StaticMockDirectory;
import com.midokura.midolman.state.Directory;
import com.midokura.midolman.state.StateAccessException;
import com.midokura.midonet.client.dto.DtoApplication;
import com.midokura.midonet.client.dto.DtoGreTunnelZone;
import com.midokura.midonet.client.dto.DtoTunnelZone;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.net.URI;
import java.util.UUID;

@RunWith(Enclosed.class)
public class TestTunnelZone {

    public static final String ZK_ROOT_MIDOLMAN = "/test/midolman";

    public static class TestCrud extends JerseyTest {

        private DtoWebResource dtoResource;
        private HostTopology topology;
        private HostZkManager hostManager;
        private Directory rootDirectory;

        private UUID host1Id = UUID.randomUUID();

        public TestCrud() {
            super(FuncTest.appDesc);
        }

        @Before
        public void setUp() throws StateAccessException {

            WebResource resource = resource();
            dtoResource = new DtoWebResource(resource);
            rootDirectory = StaticMockDirectory.getDirectoryInstance();
            hostManager = new HostZkManager(rootDirectory, ZK_ROOT_MIDOLMAN);

            topology = new HostTopology.Builder(dtoResource, hostManager)
                    .build();
        }

        @After
        public void resetDirectory() throws Exception {
            StaticMockDirectory.clearDirectoryInstance();
        }

        @Test
        public void testCrud() throws Exception {

            DtoApplication app = topology.getApplication();
            URI tunnelZonesUri = app.getTunnelZones();

            // Get tunnel zones and verify there is none
            DtoGreTunnelZone tunnelZone = new DtoGreTunnelZone();
            tunnelZone.setName("tz1-name");
            tunnelZone = dtoResource.postAndVerifyCreated(tunnelZonesUri,
                    VendorMediaType.APPLICATION_TUNNEL_ZONE_JSON, tunnelZone,
                    DtoGreTunnelZone.class);
            Assert.assertNotNull(tunnelZone.getId());
            Assert.assertEquals("tz1-name", tunnelZone.getName());

            // Update tunnel zone name
            tunnelZone.setName("tz1-name-updated");
            tunnelZone = dtoResource.putAndVerifyNoContent(tunnelZone.getUri(),
                    VendorMediaType.APPLICATION_TUNNEL_ZONE_JSON, tunnelZone,
                    DtoGreTunnelZone.class);
            Assert.assertEquals("tz1-name-updated", tunnelZone.getName());

            // List and make sure that there is one
            DtoTunnelZone[] tunnelZones = dtoResource.getAndVerifyOk(
                    tunnelZonesUri,
                    VendorMediaType.APPLICATION_TUNNEL_ZONE_COLLECTION_JSON,
                    DtoTunnelZone[].class);
            Assert.assertEquals(1, tunnelZones.length);

            // Delete it
            dtoResource.deleteAndVerifyNoContent(tunnelZone.getUri(),
                    VendorMediaType.APPLICATION_TUNNEL_ZONE_JSON);

            // list and make sure it's gone
            tunnelZones = dtoResource.getAndVerifyOk(
                    tunnelZonesUri,
                    VendorMediaType.APPLICATION_TUNNEL_ZONE_COLLECTION_JSON,
                    DtoTunnelZone[].class);
            Assert.assertEquals(0, tunnelZones.length);

        }
    }
}