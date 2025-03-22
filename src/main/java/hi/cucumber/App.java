package hi.cucumber;

import io.cucumber.core.cli.Main;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import io.muserver.handlers.ResourceHandler;
import io.muserver.handlers.ResourceHandlerBuilder;

public class App {

    public static void main(String[] args) {
        MuServer server = MuServerBuilder.httpServer()
                .withHttpPort(3000)
                .addHandler(Method.GET, "/", (request, response, pathParams) -> {
                    response.write("Hello, world");
                })
                .addHandler(Method.POST, "/regression", ((muRequest, muResponse, map) -> {

                    String testCase = muRequest.query().get("test_case");

                    String[] cucumberArgs = {
                            "--plugin", "pretty",
                            "--plugin", "html:target/cucumber-reports.html",
                            "--glue", "testCase"
                    };

                    int result = Main.run(cucumberArgs, Thread.currentThread().getContextClassLoader());

                    muResponse.write("Regression test");
                }))
//                .addHandler(Method.GET, "/t", new ResourceHandlerBuilder().withDefaultFile("src/main/resources").build())
                .start();
        System.out.println("Started server at " + server.uri());
    }
}
