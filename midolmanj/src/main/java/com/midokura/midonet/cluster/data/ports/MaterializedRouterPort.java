/*
* Copyright 2012 Midokura Europe SARL
*/
package com.midokura.midonet.cluster.data.ports;

import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.midokura.midolman.state.BGP;
import com.midokura.midolman.util.Net;
import com.midokura.midonet.cluster.data.Router;

/**
 * A {@link com.midokura.midonet.cluster.data.ports.RouterPort} which is materialized on a certain host.
 */
public class MaterializedRouterPort
    extends RouterPort<MaterializedRouterPort.Data, MaterializedRouterPort> {

    public MaterializedRouterPort(Router router, UUID uuid, Data data) {
        super(router, uuid, data);
    }

    public MaterializedRouterPort(UUID uuid, Data data) {
        this(null, uuid, data);
    }

    public MaterializedRouterPort(@Nonnull Data data) {
        this(null, null, data);
    }
    public MaterializedRouterPort(Router router) {
        this(router, null, new Data());
    }

    public MaterializedRouterPort() {
        this(null, null, new Data());
    }

    @Override
    protected MaterializedRouterPort self() {
        return this;
    }

    public UUID getHostId() {
        return getData().hostId;
    }

    public MaterializedRouterPort setHostId(UUID hostId) {
        getData().hostId = hostId;
        return self();
    }

    public String getInterfaceName() {
        return getData().interfaceName;
    }

    public MaterializedRouterPort setInterfaceName(String interfaceName) {
        getData().interfaceName = interfaceName;
        return self();
    }

    public Set<BGP> getBgps() {
        return getData().bgps;
    }

    public String getLocalNwAddr() {
        return Net.convertIntAddressToString(getData().localNwAddr);
    }

    public MaterializedRouterPort setLocalNwAddr(String localNwAddr) {
        getData().localNwAddr = Net.convertStringAddressToInt(localNwAddr);
        return this;
    }

    public int getLocalNwLength() {
        return getData().localNwLength;
    }

    public MaterializedRouterPort setLocalNwLength(int localNwLength) {
        getData().localNwLength = localNwLength;
        return this;
    }

    public static class Data extends RouterPort.Data {
        public int localNwAddr;
        public int localNwLength;
        public transient Set<BGP> bgps;
        public UUID hostId;
        public String interfaceName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Data data = (Data) o;

            if (localNwAddr != data.localNwAddr) return false;
            if (localNwLength != data.localNwLength) return false;
            if (bgps != null ? !bgps.equals(data.bgps) : data.bgps != null)
                return false;
            if (hostId != null ? !hostId.equals(
                data.hostId) : data.hostId != null)
                return false;
            if (interfaceName != null ? !interfaceName.equals(
                data.interfaceName) : data.interfaceName != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + localNwAddr;
            result = 31 * result + localNwLength;
            result = 31 * result + (hostId != null ? hostId.hashCode() : 0);
            result = 31 * result + (interfaceName != null ? interfaceName.hashCode() : 0);
            result = 31 * result + (bgps != null ? bgps.hashCode() : 0);
            return result;
        }
    }
}