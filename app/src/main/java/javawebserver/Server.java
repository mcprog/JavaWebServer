package javawebserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    protected final  Map<String, RequestRunner> routes;
    protected final ServerSocket socket;
    protected final Executor threadPool;
    protected HttpHandler handler;

    public Server(int port) throws IOException {
        routes = new HashMap<>();
        threadPool = Executors.newFixedThreadPool(100);
        socket = new ServerSocket(port);
    }

    public void addRoute(HttpMethod operationCode, String route, RequestRunner runner) {
        routes.put(operationCode.name().concat(route), runner);
    }

    public void start() throws IOException {
        handler = new HttpHandler(routes);

        while (true) {
            Socket clientConnection = socket.accept();
            handleConnection(clientConnection);
            
        }

    }

    private void handleConnection(Socket connection) {
        Runnable httpRequestRunner = () -> {
            try {
                handler.handleConnection(connection.getInputStream(), connection.getOutputStream());
            } catch (IOException ignored) {
            }
        };
        threadPool.execute(httpRequestRunner);
        
    }

}

interface RequestRunner {
    HttpResponse run(HttpRequest req);
}
