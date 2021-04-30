
import org.junit.platform.engine.TestExecutionResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RollbarAPI {

    /*
    This is a work in progress and not all methods work, will be working to update this in the coming weeks
     */

    private static final String command = " curl https://api.rollbar.com/api/1/status/ping";
    private static final String myaccesstoken = "<post_server_item>";
    private static final String myenv = "DEV";
    private static final String buildNum = "1.9";
    private static final String myUser = "ianianf";
    private static final String myExpectedResponse = "pong";

    private String urlBase = "https://api.rollbar.com/api/1/status/ping";
    private String deployURL = "https://api.rollbar.com/api/1/deploy";
    private static final String itemsURL = "https://api.rollbar.com/api/1/items";
    public static  String updateItemURL = "https://api.rollbar.com/api/1/item/";

    private static final String mysecondcommand = "curl https://api.rollbar.com/api/1/deploy/ -F access_token=" + myaccesstoken +
            " -F environment=" + myenv +
            " -F revision=" + buildNum +
            " -F local_username=" + myUser;

    public static URL appendURL(String ID, String myURL) {

        URL relativeUrl = null;

        try {

            URL baseUrl = new URL(myURL);
            relativeUrl = new URL(baseUrl, ID);
          //  url = String.valueOf(relativeUrl);
          //  System.out.println(relativeUrl);
        //    System.out.println(url);

        } catch (Exception e) {
            System.out.println("Can't append URL: " +e.getMessage());
            e.printStackTrace();
        }
        return relativeUrl;
    }

    public static void updateItemStatus(String token, String ID, String itemStatus, String versionBuild) {

        URL url;
        String myURL = String.valueOf(appendURL(ID,updateItemURL));
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        boolean value = false;
        String line = null;
        String[] items = null;

        try {

            // allow multiple TLS versions
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

            // create the HttpURLConnection
            url = new URL(myURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("X-Rollbar-Access-Token", token);
            connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.addRequestProperty("item_id", ID);
            connection.addRequestProperty("status", itemStatus);
            connection.addRequestProperty("resolved_in_version", versionBuild);
            // https://explorer.docs.rollbar.com/#operation/list-all-items

            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15 * 1000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            InputStream inputStream = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            if (responseCode == 200) {

                System.out.println("Able to retrive a list of items");
                try {

                    String lineinput = null;
                    //    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((lineinput = rd.readLine()) != null)
                    {
                        if (lineinput.contains("\"id\":")) {

                            String itemsList = lineinput.replaceAll("[^0-9]", "");
                            items = itemsList.split("\\r\\n");
                            //   System.out.println(items);

                            for (String item : items) {
                                System.out.println("Item ID in array items[]: " +item);
                            }
                        }
                    }
                    //  rd.close();
                } catch (IOException e) {
                    System.out.println("Issue:" +e.getMessage());
                }
            } else
            {
                System.out.println("Unable to retrive a list of items");
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.defaultCharset()));
            stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

        } catch (Exception e) {
            System.out.println("Unable to call ListItems() method: " +e.getMessage());
            e.printStackTrace();

        } finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

    }

    public static String[] ListItems(String token,  String myenv, String errorSeverity, String currentStatus) throws Exception {

        // Returns an array of items based on criteria: Environment, Error Severity, and Item Status

        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        boolean value = false;
        String line = null;
        String[] items = null;

        try {

            // allow multiple TLS versions
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

            // create the HttpURLConnection
            url = new URL(itemsURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("X-Rollbar-Access-Token", token);
            connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.addRequestProperty("status", currentStatus);
            connection.addRequestProperty("environment", myenv);
            connection.addRequestProperty("level", errorSeverity);
            // https://explorer.docs.rollbar.com/#operation/list-all-items

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15 * 1000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            InputStream inputStream = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            if (responseCode == 200) {

                System.out.println("Able to retrive a list of items");
                try {

                String lineinput = null;
            //    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((lineinput = rd.readLine()) != null)
                {
                    if (lineinput.contains("\"id\":")) {

                        String itemsList = lineinput.replaceAll("[^0-9]", "");
                        items = itemsList.split("\\r\\n");
                     //   System.out.println(items);

                        for (String item : items) {
                            System.out.println("Item ID in array items[]: " +item);
                        }
                    }
                }
              //  rd.close();
                } catch (IOException e) {
                    System.out.println("Issue:" +e.getMessage());
                }
            } else
            {
                System.out.println("Unable to retrive a list of items");
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.defaultCharset()));
            stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

        } catch (Exception e) {
            System.out.println("Unable to call ListItems() method: " +e.getMessage());
            e.printStackTrace();

        } finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return items;
        //return value;
    }

   /* public static boolean checkRollbarAPI() {

        boolean isAPIUp = false;

        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String response = br.readLine();
            if (response.equals(myExpectedResponse)) {

                System.out.println("Rollbar API test Passed.");
                isAPIUp = true;
            } else {
                System.out.println("Rollbar API test failed");
            }
            //  System.out.println(response);
        } catch (IOException e) {
            System.out.println("Can't check Rollbar API status: " +e.getMessage());
            e.printStackTrace();
        }
        return isAPIUp;
    }*/



    public boolean RollbarAPIcheck() throws Exception {

        //
        // Return a string with the response JSON 
        //
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        boolean value = false;
        String line = null;

        try {

          //  String fullUrl = String.format(urlBase);
            // allow multiple TLS versions
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

            // create the HttpURLConnection
            url = new URL(urlBase);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

         //   connection.addRequestProperty("X-Rollbar-Access-Token", token);
            connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15 * 1000);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {

                System.out.println("Rollbar API is up");
                value = true;
            } else
            {
                System.out.println("Rollbar API is down");
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.defaultCharset()));
            stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return value;
    }

    /*public static void getOccurrences(String token, String env, String vers) {

      System.out.println("Calling getOccurrences() method now.");

       try {

           // allow multiple TLS versions
           System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

           // create the HttpURLConnection
           url = new URL(deployURL);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.addRequestProperty("X-Rollbar-Access-Token", token);
           connection.addRequestProperty("environment", env);
           connection.addRequestProperty("revision", vers);
           connection.addRequestProperty("rollbar_username", "ianianf");
           connection.addRequestProperty("local_username", "iflanagan");
           connection.addRequestProperty("comment", "Deployment from Java REST code");
           connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
           connection.addRequestProperty("Content-Length", "1000");
           // connection.setRequestProperty("Content-Length", "1000"); // Content-Length:0


           // connection.setRequestProperty("Content-Length", "20");
           // just want to do an HTTP GET here
           connection.setRequestMethod("POST");

           // uncomment this if you want to write output to this url
           connection.setDoOutput(true);

           // give it 15 seconds to respond
           connection.setReadTimeout(15 * 1000);
           connection.connect();

           int responseCode = connection.getResponseCode();
           System.out.println("Code is: " +responseCode);
           //    System.out.println("Rollbar Deploy response is : " +connection.getResponseMessage());

           if (responseCode == 200) {
               System.out.println("Deploy was successful");
               value = true;
           } else {
               System.out.println("Deploy was unsuccessful");
           }
           // read the output from the server
           reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.defaultCharset()));
           stringBuilder = new StringBuilder();

           while ((line = reader.readLine()) != null) {
               stringBuilder.append(line + "\n");
           }

           String resp = stringBuilder.toString();
           System.out.println("\nRollbar API response is: from deploy is: " +resp);

        } catch (Exception e ) {
           System.out.println("Can't Call getOccurrences() method now." +e.getMessage());
            e.printStackTrace();
        } finally {

           // close the reader; this can throw an exception too, so
           // wrap it in another try/catch block.
           if (reader != null) {
               try {
                   reader.close();
               } catch (IOException io) {
                   io.printStackTrace();
               }
           }
       }
    }
*/
    public  boolean deploy(String token, String env, String vers) {

        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        boolean value = false;
        String line = null;

        String jsonString = "{\n" +
                "  \"environment\": \"prod\",\n" +
                "  \"revision\": \"2.9\",\n" +
                "  \"rollbar_username\": \"ianianf\",\n" +
                "}";

        try
        {
            // allow multiple TLS versions
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

            // create the HttpURLConnection
            url = new URL(deployURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("X-Rollbar-Access-Token", token);
            connection.addRequestProperty("environment", env);
            connection.addRequestProperty("revision", String.valueOf(vers));
            connection.addRequestProperty("rollbar_username", "ianianf");
            connection.addRequestProperty("local_username", "iflanagan");
            connection.addRequestProperty("comment", "Deployment from Java REST code");
            connection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
          //  connection.addRequestProperty("Content-Length", "1000");
           // connection.setRequestProperty("Content-Length", "1000"); // Content-Length:0


           // connection.setRequestProperty("Content-Length", "20");
            // just want to do an HTTP GET here
            connection.setRequestMethod("POST");

            // uncomment this if you want to write output to this url
            connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15 * 1000);
            connection.connect();
            connection.getOutputStream().write(jsonString.getBytes(StandardCharsets.UTF_8));

            int responseCode = connection.getResponseCode();
            System.out.println("\nRollbar Deploy response Code is: " +responseCode);

            if (responseCode == 200) {
                System.out.println("Deploy was successful");
                value = true;
            } else {
                System.out.println("Deploy was unsuccessful");
            }
            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.defaultCharset()));
            stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            String resp = stringBuilder.toString();
            System.out.println("\nRollbar API response is: from deploy is: " +resp);

        } catch (Exception e ) {
            System.out.println("Error in deploy " +e.getMessage());

        }
        finally {

            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
        return value;
    }
}
