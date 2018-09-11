//package com.fusioncharts.fusionexport.client;
//
//import javax.websocket.*;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URL;
//
//@ClientEndpoint
//public class WebSocketManager
//{
//    private Session session;
//    private WebSocketContainer container;
//    private static Object waitLock = new Object();
//    private ExportDataProcessor exporter;
//    private static final String WEBSOCKETSCHEME = "ws";
//
//    public WebSocketManager(String host,int port,ExportDataProcessor exporter){
//        this.exporter = exporter;
//        try{
//            this.container=ContainerProvider.getWebSocketContainer();
//            container.connectToServer(this, new URI(createURI()));
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    @OnOpen
//    public void onOpen(Session session){
//        System.out.println("Connection Opened");
//        this.session=session;
//    }
//
//    @OnMessage
//    public void onMessage(String message,Session session){
//        exporter.processDataReceived(message);
//    }
//
//    @OnClose
//    public void onClose(){
//        System.out.println("Connection Closed");
//        synchronized (waitLock){
//            waitLock.notifyAll();
//        }
//
//    }
//
//    public void sendMessage(String message){
//        try {
//            session.getBasicRemote().sendText(message);
//            waitForExportDone();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void close(){
//        try{
//        session.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private static void  waitForExportDone() {
//        synchronized(waitLock){
//            try {
//            waitLock.wait();
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String createURI(){
//        return new StringBuilder(WEBSOCKETSCHEME)
//                .append("://")
//                .append(this.exporter.getExportServerHost())
//                .append(':')
//                .append(this.exporter.getExportServerPort()).toString();
//
//    }
//}
