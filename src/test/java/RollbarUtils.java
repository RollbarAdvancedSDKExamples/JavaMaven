import com.rollbar.notifier.Rollbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;

import static com.rollbar.notifier.config.ConfigBuilder.withAccessToken;

public class RollbarUtils {

    private String token;
    private String environment;
    private double codeVersion;
    private Rollbar rollbar;
    private static String urlBase = "https://api.rollbar.com/api/1/deploy";

    // constructor and it will return a rollbar object
    public Rollbar RollbarUtils(String token, String environment, double codeVersion) {
        this.token = token;
        this.environment = environment;
        this.codeVersion = codeVersion;

        rollbar = Rollbar.init(withAccessToken(token)
                .environment(environment)
                .codeVersion(String.valueOf(codeVersion)) // enable if you want to use versions
                .person(new MyPersonProvider()) // enable if you want to enable people tracking
                .server(new ServerProvider()) // enable if you want to enable source control
                .transformer(new RemoveFrameworkTransformer()) // enable is you want to transform the payload in this example it removes the framework
                .filter(new FilterItems()) // don't send debug items
                .enabled(true) // required set to false to not send data to rollbar
                .handleUncaughtErrors(true)
               // .endpoint("test") uncomment if you need to send rollbar data to a proxy
                .framework(MyConfig.framework) // optional not required
                .platform(MyConfig.platform) // optional not required
                .build());

        return this.rollbar;
    }

    public String getToken() {
        return token;
    }

    public String getEnvironment() {
        return environment;
    }

    public double getCodeVersion() {
        return codeVersion;
    }

    public Rollbar getRollbar() {
        return rollbar;
    }

    public static String randomString(int length) {

        char[] characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }


    public static String doHttpUrlConnectionAction(String token, String env, String codeVersion) throws Exception {

        System.out.println("Notifying Rollbar we did a deploy");
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try {
            String fullUrl = String.format(urlBase);

            // allow multiple TLS versions
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

            // create the HttpURLConnection
            url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("X-Rollbar-Access-Token", token);
            connection.addRequestProperty("environment", env);
            connection.addRequestProperty("revision", codeVersion);
            connection.addRequestProperty("rollbar_username", "ianianf");
            connection.addRequestProperty("rollbar_username", "iflanagan");
            connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
            System.out.println("Print properties" +connection);

            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15 * 1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.defaultCharset()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String resp = stringBuilder.toString();
            System.out.println(resp);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



