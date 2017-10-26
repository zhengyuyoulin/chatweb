package com.netty.service.imp;

import javax.annotation.PostConstruct;

import com.netty.service.InitService;
import com.netty.websocket.WebSocketServer;
import org.springframework.stereotype.Service;

/**
 * Created by cai on 2017/10/16.
 */
@Service
public class InitServiceImp implements InitService {

    /*@PostConstruct
    public void nettyServerInit() throws Exception{
        System.out.println(".........");
        WebSocketServer wss = new WebSocketServer();
        wss.start();
    }*/

}
