package com.dempe.forest.common.client.ha;

import com.dempe.forest.common.client.Client;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class HAClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HAClientService.class);

    private static HAForestClient haForestClient;

    public HAClientService(String name) throws Exception {
        if (haForestClient == null) {
            synchronized (HAClientService.class) {
                if (haForestClient == null) {
                    haForestClient = new HAForestClient(name);
                }
            }
        }
    }

    public void sendOnly(Request request) throws Exception {
        Client client = haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return;
        }
        client.sendOnly(request);

    }

    public Response sendAndWait(Request request) throws Exception {
        Client client = haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.sendAndWait(request);
    }

    public Response sendAndWait(Request request, long timeOut) throws Exception {
        Client client = haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.sendAndWait(request, timeOut);
    }

    public void sendAndWrite(ChannelHandlerContext ctx, Request request) throws Exception {
        Client client = haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return;
        }
        client.sendForward(ctx, request);
    }


}
