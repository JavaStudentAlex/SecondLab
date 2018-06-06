package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractProcessor {
    protected String process;
    protected ConnectionHandler handler;

    public AbstractProcessor(ConnectionHandler handler) {
        this.handler = handler;
    }

    public boolean canProcess(String example){
        return process.equals(example);
    }

    public abstract void process(DataInputStream reader, DataOutputStream writer) throws IOException;


}
