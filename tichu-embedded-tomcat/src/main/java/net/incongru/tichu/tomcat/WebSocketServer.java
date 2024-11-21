package net.incongru.tichu.tomcat;

import net.incongru.tichu.websocket.RoomEndpoint;
import org.apache.catalina.Context;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.websocket.server.WsSci;

import jakarta.servlet.http.HttpServlet;
import java.security.Principal;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;


public class WebSocketServer {

    public static void main(String[] args) throws Exception {
        final Tomcat tomcat = new Tomcat();
        tomcat.setSilent(false);
        tomcat.setPort(8080);

        final Context ctx = tomcat.addContext("", null);
        setupSecurity(ctx);

        Tomcat.addServlet(ctx, "default-servlet", new DefaultServlet());
        // ctx.addServletMappingDecoded("/index.html", "default-servlet");
        // ctx.addServletMappingDecoded("/style.css", "default-servlet");
        // ctx.addServletMappingDecoded("/websocket.js", "default-servlet");

        // if mapping only to /index.html, then the websocket-filter doesn't kick in
        // http://mail-archives.apache.org/mod_mbox/tomcat-users/201911.mbox/%3cCAJ3=HkAvxeZn6hM1PLV6C9sZE9Dr-yXfJPjHenj1uwn7vB5GmA@mail.gmail.com%3e
        Tomcat.addServlet(ctx, "ws-only", new HttpServlet() {
        });
        ctx.addServletMappingDecoded(RoomEndpoint.PATH + "/*", "ws-only");

        ctx.addServletContainerInitializer(new WsSci(), Set.of(
                // Our "pojo" endpoint
                RoomEndpoint.class
        ));

        tomcat.getConnector(); // Triggers the creation of the default connector
        tomcat.start();

        tomcat.getServer().await();
    }

    private static void setupSecurity(Context ctx) {
        appRoles().forEach(ctx::addSecurityRole);
        setupUserRealm(ctx);
        buildSecurityConstraints().forEach(ctx::addConstraint);

        ctx.getPipeline().addValve(new BasicAuthenticator());

        // Apparently this isn't actually needed:
        final LoginConfig loginConfig = new LoginConfig();
        loginConfig.setAuthMethod("BASIC");
        loginConfig.setRealmName("ws-realm");
        ctx.setLoginConfig(loginConfig);
    }

    private static void setupUserRealm(Context ctx) {
        ctx.setRealm(new RealmBase() {
            @Override
            protected String getPassword(String username) {
                return username;
            }

            @Override
            protected Principal getPrincipal(String username) {
                return new GenericPrincipal(username, Collections.singletonList("tichu-player-role"));
            }
        });
    }

    private static Iterable<String> appRoles() {
        return Collections.singleton("tichu-player-role");
    }

    private static Stream<SecurityConstraint> buildSecurityConstraints() {
        final SecurityCollection secResource = new SecurityCollection();
        secResource.setName("ws-sec");
        // secRes.addPattern(RoomEndpoint.PATH + "/*");
        secResource.addPattern("/*");
        // applies all methods

        final SecurityConstraint secConstraint = new SecurityConstraint();
        secConstraint.addCollection(secResource);
        secConstraint.addAuthRole("tichu-player-role");
        // secConstraint.addAuthRole("*"); // require any role (but not none)
        secConstraint.setAuthConstraint(true); // without this, not going through valve!?
        return Stream.of(secConstraint);
    }
}
