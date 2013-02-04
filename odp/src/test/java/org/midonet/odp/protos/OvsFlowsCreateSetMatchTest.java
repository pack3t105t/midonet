/*
* Copyright 2012 Midokura Europe SARL
*/
package org.midonet.odp.protos;

import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import org.midonet.odp.Datapath;
import org.midonet.odp.Flow;
import org.midonet.odp.FlowMatch;
import org.midonet.odp.flows.FlowAction;


public abstract class OvsFlowsCreateSetMatchTest
    extends AbstractNetlinkProtocolTest<OvsDatapathConnection> {

    private static final Logger log = LoggerFactory
        .getLogger(OvsFlowsCreateSetMatchTest.class);

    protected void setUp(final byte[][] responses) throws Exception {
        super.setUp(responses);
        connection = OvsDatapathConnection.create(channel, reactor);
    }

    public void doTest() throws Exception {

        initializeConnection(connection.initialize(), 6);

        Future<Datapath> dpFuture = connection.datapathsGet("bibi");
        exchangeMessage();
        Datapath datapath = dpFuture.get();

        Future<Flow> flowFuture =
            connection.flowsCreate(dpFuture.get(),
                                   new Flow().setMatch(flowMatch()));

        exchangeMessage();
        assertThat("The returned flow has the same Match as we wanted",
                   flowFuture.get().getMatch(), equalTo(flowMatch()));

        Future<Flow> retrievedFlowFuture =
            connection.flowsGet(datapath, flowMatch());

        exchangeMessage(2);
        assertThat("The retrieved flow has the same Match as we wanted",
                   retrievedFlowFuture.get().getMatch(), equalTo(flowMatch()));

        // update the with actions.
        Flow updatedFlow =
            new Flow()
                .setMatch(flowMatch())
                .setActions(flowActions());
//                userspace()

        Future<Flow> flowWithActionsFuture = connection.flowsSet(datapath,
                                                                 updatedFlow);
        exchangeMessage();

        assertThat("The updated flow has the same keySet as the requested one",
                   flowWithActionsFuture.get().getMatch(),
                   equalTo(flowMatch()));
        assertThat("The updated flow has the same actionSet we wanted",
                   flowWithActionsFuture.get().getActions(),
                   equalTo(updatedFlow.getActions()));
    }

    protected abstract FlowMatch flowMatch();

    protected abstract List<FlowAction<?>> flowActions();
}