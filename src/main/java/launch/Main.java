package launch;

import org.apache.catalina.LifecycleException;

import server.TomcatServer;

public class Main {
    public static void main(String[] args) throws LifecycleException {
    	TomcatServer tomcat = TomcatServer.getInstance();
        tomcat.start();
    }
}
