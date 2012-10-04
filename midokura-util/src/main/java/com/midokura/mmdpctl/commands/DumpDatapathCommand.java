package com.midokura.mmdpctl.commands;

import com.midokura.mmdpctl.commands.callables.DumpDatapathCallable;
import com.midokura.mmdpctl.commands.results.DumpDatapathResult;

import java.util.concurrent.Future;

public class DumpDatapathCommand extends Command<DumpDatapathResult>{

    String datapath;

    public DumpDatapathCommand(String datapath) {
        this.datapath = datapath;
    }

    public Future<DumpDatapathResult> execute() {
        return run(new DumpDatapathCallable(datapath));
    }
}