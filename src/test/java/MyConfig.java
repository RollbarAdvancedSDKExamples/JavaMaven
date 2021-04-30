import java.io.IOException;

public class MyConfig {

    public static String getMyBuildFile = "build.txt";
    public static final String automationToken = "<token>";
    public static final String myToken = "404e3cc8d178433693af2f4c051b9fa6"; //   use <post_server_item> token for your rollbar project
    public static final String myENv =  Utils.changeEnv(); // "prod";
    public static double myVersion;

    static {
        try {
            myVersion = Utils.getBuildNumber(getMyBuildFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    ; // Utils.IncrementBuildNumber(true); old RollbarUtils.randomString(10);
    public static final String framework = "Junit5";
    public static final String platform = "macOS";

    // Server Provider info
    public static final String myRoot = "com.IanFlanagan";
    public static final String myHost = "Ians-MacBook-Pro.local"; // old localhost new Utils.GetHostName(); Ians-MacBook-Pro.local
    public static final String myBranch = "main";

    // other config stuff
    public static final String myBuildFile = "build.txt";

    // API releated URLS

    public static  String updateItemURL = "https://api.rollbar.com/api/1/item/";

}


