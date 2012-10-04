package com.midokura.mmdpctl.commands;

import com.midokura.mmdpctl.commands.callables.GetDatapathCallable;
import com.midokura.mmdpctl.commands.results.GetDatapathResult;

import java.util.concurrent.Future;

public class GetDatapathCommand extends Command<GetDatapathResult> {

    private String datapathName;

    public GetDatapathCommand(String datapathName) {
        this.datapathName = datapathName;
    }

    public Future<GetDatapathResult> execute() {
        return run(new GetDatapathCallable(datapathName));
    }

}