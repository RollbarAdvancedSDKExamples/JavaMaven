import java.io.IOException;

public class MyConfig {

    public static String getMyBuildFile = "build.txt";
    public static final String automationToken = "<token>";
    public static final String myToken = "ab07f26dc2824f3c93a613c8cb2c4f29"; //   use <post_server_item> token for your rollbar project
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
    public static final String myRoot = "JavaJenkinsNew";
    public static final String myHost = "MacBook-Pro.local"; // old localhost new Utils.GetHostName();
    public static final String myBranch = "main";

    // other config stuff
    public static final String myBuildFile = "build.txt";

    // API releated URLS

    public static  String updateItemURL = "https://api.rollbar.com/api/1/item/";

}


