package net.incongru.tichu.tomcat;

import net.incongru.tichu.websocket.ChatEndpoint;
import org.apache.catalina.Context;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.websocket.server.WsSci;

import javax.servlet.http.HttpServlet;
import java.util.Set;


public class WebSocketServer {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setSilent(false);
        tomcat.setPort(8080);

        final Context ctx = tomcat.addContext("", "/Users/gjoseph/Dev/Tichu/tichu-clients/tichu-web/");
        Tomcat.addServlet(ctx, "default-servlet", new DefaultServlet());
        ctx.addServletMappingDecoded("/index.html", "default-servlet");
        ctx.addServletMappingDecoded("/style.css", "default-servlet");
        ctx.addServletMappingDecoded("/websocket.js", "default-servlet");

        // if mapping only to /index.html, then the websocket-filter doesn't kick in
        // http://mail-archives.apache.org/mod_mbox/tomcat-users/201911.mbox/%3cCAJ3=HkAvxeZn6hM1PLV6C9sZE9Dr-yXfJPjHenj1uwn7vB5GmA@mail.gmail.com%3e
        Tomcat.addServlet(ctx, "ws-only", new HttpServlet() {
        });
        ctx.addServletMappingDecoded(ChatEndpoint.PATH + "/*", "ws-only");

        ctx.addServletContainerInitializer(new WsSci(), Set.of(
                // Our "pojo" endpoint
                ChatEndpoint.class
        ));

        tomcat.getConnector(); // Triggers the creation of the default connector
        tomcat.start();

        tomcat.getServer().await();
    }
}
