package server;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;

import launch.Main;

public class TomcatServer {

	private static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
	private static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));
	private static Tomcat tomcat;
	private static TomcatServer INSTANCE;

	private TomcatServer() throws LifecycleException {
		String contextPath = "";
		String appBase = ".";
		tomcat = new Tomcat();

		// Configuração do conector HTTP com URIEncoding
		Connector connector = new Connector();
		connector.setPort(8080);
		connector.setURIEncoding("UTF-8");
		tomcat.setConnector(connector);

		// define port, host, contextpath
		tomcat.setPort(Integer.valueOf(PORT.orElse("8080")));
		tomcat.setHostname(HOSTNAME.orElse("localhost"));
		tomcat.getHost().setAppBase(appBase);
		tomcat.addWebapp(contextPath, appBase);

		// annotation scanning
		Context ctx = tomcat.addContext("/api", new File(".").getAbsolutePath());
		ctx.addLifecycleListener(new ContextConfig());
		// Add the JAR/folder containing this class to PreResources
		final WebResourceRoot root = new StandardRoot(ctx);
		final URL url = findClassLocation(Main.class);
		root.createWebResourceSet(WebResourceRoot.ResourceSetType.PRE, "/WEB-INF/classes", url, "/");
		ctx.setResources(root);
	}
	
	public void start() throws LifecycleException {
		configureTomcatLogging();
		tomcat.start();
	}
	
	public void stop() throws LifecycleException {
		tomcat.stop();
	}
	
	public boolean isReady() {
        try {
            // Defina o endpoint específico que indica que o Tomcat está pronto
            URL url = new URL("http://" + HOSTNAME.orElse("localhost") + ":" + PORT.orElse("8080"));

            // Abre uma conexão HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Define o tempo limite de conexão em milissegundos
            connection.setConnectTimeout(500);

            // Faz a requisição HTTP GET
            connection.setRequestMethod("GET");

            // Obtém a resposta do servidor
            int responseCode = connection.getResponseCode();

            // Se o código de resposta for 200, consideramos que o Tomcat está pronto
            return responseCode == 200;
        } catch (IOException e) {
            // Trate exceções, se necessário
            e.printStackTrace();
            return false;
        }
    }
	
	// Tries to find the URL of the JAR or directory containing {@code clazz}.
    private static URL findClassLocation(Class< ? > clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }
    
    private static void configureTomcatLogging() {
        // Obtém o Logger do Tomcat
        Logger tomcatLogger = Logger.getLogger("org.apache");

        // Define o nível de log para WARNING (ou SEVERE)
        tomcatLogger.setLevel(Level.WARNING);

        // Remove todos os ConsoleHandlers associados ao Logger do Tomcat
        for (Handler handler : tomcatLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                tomcatLogger.removeHandler(handler);
            }
        }
    }

	public static TomcatServer getInstance() throws LifecycleException {
		if (INSTANCE == null) {
			INSTANCE = new TomcatServer();
		}
		return INSTANCE;
	}
}
