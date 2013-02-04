/*
* Copyright 2012 Midokura Europe SARL
*/
package org.midonet.functional_test;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static org.midonet.util.Waiters.waitFor;
import static java.lang.String.format;

import org.midonet.packets.ARP;
import org.midonet.packets.Ethernet;
import org.midonet.functional_test.utils.RemoteTap;
import org.midonet.tools.timed.Timed;

/**
 * @author Mihai Claudiu Toader <mtoader@midokura.com>
 *         Date: 4/27/12
 */
public class PacketsHelper {

    public static Ethernet waitForArpPacket(RemoteTap tap) throws Exception {
        return waitForPacketOfType(ARP.ETHERTYPE, tap);
    }

    public static Ethernet waitForPacketOfType(final short etherType,
                                               final RemoteTap tap)
        throws Exception {

        long totalWait = TimeUnit.SECONDS.toMillis(30);
        long sleepWait = TimeUnit.MILLISECONDS.toMillis(50);

        return
            waitFor(
                format("Wait for an packet of type %xd", etherType),
                totalWait,
                sleepWait,
                new Timed.Execution<Ethernet>() {
                    @Override
                    protected void _runOnce() throws Exception {
                        byte[] frameBytes = tap.recv(getRemainingTime());

                        if (frameBytes != null) {
                            setResult(new Ethernet());
                            getResult().deserialize(
                                ByteBuffer.wrap(frameBytes));
                            setCompleted(
                                getResult().getEtherType() == etherType);
                        }
                    }
                });
    }
}