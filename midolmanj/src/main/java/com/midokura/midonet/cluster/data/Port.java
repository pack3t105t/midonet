/*
* Copyright 2012 Midokura Europe SARL
*/
package com.midokura.midonet.cluster.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * // TODO: mtoader ! Please explain yourself.
 */
public abstract class Port<
    PortData extends Port.Data,
    Self extends Port<PortData, Self>
    >
    extends Entity.Base<UUID, PortData, Self> {

    protected Port(UUID uuid, PortData portData) {
        super(uuid, portData);
    }

    protected Port(PortData portData) {
        super(null, portData);
    }

    public Self setDeviceId(UUID deviceId) {
        getData().device_id = deviceId;
        return self();
    }

    public UUID getDeviceId() {
        return getData().device_id;
    }

    public Self setInboundFilter(UUID filterId) {
        getData().inboundFilter = filterId;
        return self();
    }

    public UUID getInboundFilter() {
        return getData().inboundFilter;
    }

    public Self setOutboundFilter(UUID filterId) {
        getData().outboundFilter = filterId;
        return self();
    }

    public UUID getOutboundFilter() {
        return getData().outboundFilter;
    }

    public Self setPortGroups(Set<UUID> portGroups) {
        getData().portGroupIDs = new HashSet<UUID>(portGroups);
        return self();
    }

    public Set<UUID> getPortGroups() {
        return getData().portGroupIDs;
    }

    public Self setGreKey(int greKey) {
        getData().greKey = greKey;
        return self();
    }

    public int getGreKey() {
        return getData().greKey;
    }

    public Self setProperties(Map<String, String> properties) {
        getData().properties = new HashMap<String, String>(properties);
        return self();
    }

    public Map<String, String> getProperties() {
        return getData().properties;
    }

    public static class Data {
        public UUID device_id;
        public UUID inboundFilter;
        public UUID outboundFilter;
        public Set<UUID> portGroupIDs;
        public int greKey;
        public Map<String, String> properties = new HashMap<String, String>();


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            if (greKey != data.greKey) return false;
            if (device_id != null ? !device_id.equals(
                data.device_id) : data.device_id != null) return false;
            if (inboundFilter != null ? !inboundFilter.equals(
                data.inboundFilter) : data.inboundFilter != null) return false;
            if (outboundFilter != null ? !outboundFilter.equals(
                data.outboundFilter) : data.outboundFilter != null)
                return false;
            if (portGroupIDs != null ? !portGroupIDs.equals(
                data.portGroupIDs) : data.portGroupIDs != null) return false;
            if (properties != null ? !properties.equals(
                data.properties) : data.properties != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = device_id != null ? device_id.hashCode() : 0;
            result = 31 * result + (inboundFilter != null ? inboundFilter.hashCode() : 0);
            result = 31 * result + (outboundFilter != null ? outboundFilter.hashCode() : 0);
            result = 31 * result + (portGroupIDs != null ? portGroupIDs.hashCode() : 0);
            result = 31 * result + greKey;
            result = 31 * result + (properties != null ? properties.hashCode() : 0);
            return result;
        }
    }
}