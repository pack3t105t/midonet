/*
 * Copyright 2013 Midokura Europe SARL
 */

package org.midonet.midolman.state;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.midonet.midolman.state.zkManagers.BridgeDhcpZkManager.Host;
import org.midonet.midolman.state.PortDirectory.*;
import org.midonet.midolman.util.JSONSerializer;
import org.midonet.packets.IntIPv4;
import org.midonet.packets.MAC;


import static junit.framework.Assert.assertEquals;

public class TestJsonSerialization {

    @Test
    public void testIntIPv4() throws IOException {
        IntIPv4 ip = IntIPv4.fromString("10.1.2.3");
        String json = JSONSerializer.objToJsonString(ip);
        assertEquals("\"10.1.2.3\"", json);
        IntIPv4 ip2 = JSONSerializer.jsonStringToObj(json, IntIPv4.class);
        assertEquals(ip, ip2);
    }

    @Test
    public void testMac() throws IOException {
        MAC mac = MAC.fromString("aa:bb:cc:dd:ee:11");
        String json = JSONSerializer.objToJsonString(mac);
        assertEquals("\"aa:bb:cc:dd:ee:11\"", json);
        MAC mac2 = JSONSerializer.jsonStringToObj(json, MAC.class);
        assertEquals(mac, mac2);
    }

    @Test
    public void testHost() throws IOException {
        Host host = new Host(MAC.fromString("aa:bb:cc:dd:ee:11"),
                             IntIPv4.fromString("10.1.2.3"),
                             "mars");
        String json = JSONSerializer.objToJsonString(host);
        assertEquals(
            "{\"mac\":\"aa:bb:cc:dd:ee:11\",\"ip\":\"10.1.2.3\",\"name\":\"mars\"}",
            json);
        Host host2 = JSONSerializer.jsonStringToObj(json, Host.class);
        assertEquals(host, host2);
    }

    @Test
    public void testMaterializedRouterPort() throws IOException {
        MaterializedRouterPortConfig port = new MaterializedRouterPortConfig(
            UUID.randomUUID(), 0x0a000000, 24,0x0a00000a,
            MAC.fromString("aa:bb:cc:dd:ee:00"), null, null);
        String json = JSONSerializer.objToJsonString(port);
        MaterializedRouterPortConfig port2 =
            JSONSerializer.jsonStringToObj(json,
                MaterializedRouterPortConfig.class);
        assertEquals(port, port2);
        // Now deserialize to the superclass.
        PortConfig port3 = JSONSerializer.jsonStringToObj(json,
            PortConfig.class);
        assertEquals(port, port3);
    }

    @Test
    public void testMaterializedBridgePort() throws IOException {
        MaterializedBridgePortConfig port = new MaterializedBridgePortConfig(
            UUID.randomUUID());
        String json = JSONSerializer.objToJsonString(port);
        MaterializedBridgePortConfig port2 =
            JSONSerializer.jsonStringToObj(json,
                MaterializedBridgePortConfig.class);
        assertEquals(port, port2);
        // Now deserialize to the superclass.
        PortConfig port3 = JSONSerializer.jsonStringToObj(json,
            PortConfig.class);
        assertEquals(port, port3);
    }

    @Test
    public void testTrunkVlanBridgePort() throws IOException {
        TrunkVlanBridgePortConfig port = new TrunkVlanBridgePortConfig(
            UUID.randomUUID());
        String json = JSONSerializer.objToJsonString(port);
        TrunkVlanBridgePortConfig port2 =
            JSONSerializer.jsonStringToObj(json, TrunkVlanBridgePortConfig.class);
        assertEquals(port, port2);
        // Now deserialize to the superclass.
        PortConfig port3 = JSONSerializer.jsonStringToObj(json, PortConfig.class);
        assertEquals(port, port3);
    }

    @Test
    public void testLogicalVlanBridgePort() throws IOException {
        LogicalVlanBridgePortConfig port = new LogicalVlanBridgePortConfig(
            UUID.randomUUID(), UUID.randomUUID(), (short)3);
        String json = JSONSerializer.objToJsonString(port);
        LogicalVlanBridgePortConfig port2 =
            JSONSerializer.jsonStringToObj(json, LogicalVlanBridgePortConfig.class);
        assertEquals(port, port2);
        // Now deserialize to the superclass.
        PortConfig port3 = JSONSerializer.jsonStringToObj(json, PortConfig.class);
        assertEquals(port, port3);
    }
}