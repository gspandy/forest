package com.dempe.forest.bus.handler;

import com.dempe.forest.common.client.ha.HAClientService;
import com.dempe.forest.common.proto.Request;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class BusDispatcherHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(BusDispatcherHandler.class);

    private final static Map<String, HAClientService> nameClientMap = Maps.newConcurrentMap();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Request) {
            Request request = (Request) msg;
            LOGGER.info("dispatcher request = {}", request);
            HAClientService clientService = getClientServiceByName(request.getName());
            clientService.sendAndWrite(ctx, request);
        }


    }

    private HAClientService getClientServiceByName(String name) throws Exception {
        HAClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new HAClientService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }


}