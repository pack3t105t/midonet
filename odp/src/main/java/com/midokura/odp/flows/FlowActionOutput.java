/*
* Copyright 2012 Midokura Europe SARL
*/
package com.midokura.odp.flows;

import com.midokura.netlink.NetlinkMessage;
import com.midokura.netlink.messages.BaseBuilder;

public class FlowActionOutput implements FlowAction<FlowActionOutput> {

    /** u32 port number. */
    int portNumber;

    @Override
    public void serialize(BaseBuilder builder) {
        builder.addValue(portNumber);
    }

    @Override
    public boolean deserialize(NetlinkMessage message) {
        try {
            portNumber = message.getInt();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public NetlinkMessage.AttrKey<FlowActionOutput> getKey() {
        return FlowActionAttr.OUTPUT;
    }

    @Override
    public FlowActionOutput getValue() {
        return this;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public FlowActionOutput setPortNumber(int portNumber) {
        this.portNumber = portNumber;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowActionOutput that = (FlowActionOutput) o;

        if (portNumber != that.portNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return portNumber;
    }

    @Override
    public String toString() {
        return "FlowActionOutput{" +
            "portNumber=" + portNumber +
            '}';
    }
}