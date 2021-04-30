import com.rollbar.notifier.Rollbar;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public  class MyTests {

    private static String myHash = Utils.genHash(10);
    private static String test = null;
    private static Rollbar rollbar;
    private static HashMap<String,Object> myRollbarMap = new HashMap<>();

   @BeforeEach
  // @BeforeAll
   public void setup() throws IOException {

       System.out.println("\nStarting setup() method before test starts\n");
       // create instance of rollbarutils class
       RollbarUtils rb = new RollbarUtils();

       /*
       Rollbar rollbar = Rollbar.init(withAccessToken(System.getenv("ROLLBAR_ACCESS_TOKEN"))
                .environment(System.getenv("ENVIRONMENT"))
                .codeVersion(System.getenv("CODE_VERSION"))
        */

      // System.out.println("updating build number");
      // int mybuild = Utils.getBuildNumber(MyConfig.myBuildFile);
     //  mybuild = mybuild +1;

       // call constructor which returns a rollbar object
       rollbar = rb.RollbarUtils(MyConfig.myToken, MyConfig.myENv, MyConfig.myVersion);
       System.out.println("Connecting to rollbar with Token: " +rb.getToken()+ " Environment: " +rb.getEnvironment()+ " Code_version: " +rb.getCodeVersion());
       rollbar.info("Starting setup() method before test starts");

   }

  @AfterEach
  //@AfterAll
   public void teardown() throws Exception {

      RollbarAPI rbapi = new RollbarAPI();

       try {

         //  rbapi.deploy(MyConfig.myToken, MyConfig.myENv, String.valueOf(MyConfig.myVersion));
           rollbar.info("Close Rollbar Connection");

       } catch (Exception e) {
           System.out.println("Can't close rollbar connection " +e.getMessage());
       } finally {
           rollbar.close(true);
       }
   }

   @Test
   public void checkAPI() throws Exception {

      RollbarAPI rbapi = new RollbarAPI();
      boolean isUp = rbapi.RollbarAPIcheck();
      //assertEquals(true, isUp);
   }

   @Test
   public void generateLotsErrors() {

       System.out.println("calling generateLotsErrors() test now");

       try
       {
           throw new Exception("Error");
       } catch (Exception e) {
           for (int i =0; i<=5; i++) {
               rollbar.critical(e,i+ " new Critical Error");
           }
       }
   }
   @Test
   public void customFields() {

       System.out.println("calling customFields() test now");

       try
       {
           throw new AccessDeniedException("New Runtime exception");
       } catch (Exception e) {

           rollbar.critical(e,myRollbarMap, "Custom Field Test");
           rollbar.error(e,myRollbarMap, "Custom Field Test");
       }
   }

    @Test
    public void dividebyZeroError() {

        System.out.println("Running dividebyZeroError() test now");

        try
        {
            System.out.println("Try loop");
            int value = 1 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Exception " +e.getMessage());
            rollbar.error(e," Divide by Zero Error");
            rollbar.critical(e," Divide by Zero Error");
            rollbar.debug(e," Divide by Zero Error");


        } finally
        {
            try
            {
                rollbar.close(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        System.out.println("End test method");
    }
    @Test
    public void generateNullPointerError(){

        System.out.println("\nStarting generateNullPointerError() test now\n");

        try
        {
            test.toString();

        } catch (NullPointerException e) {
            rollbar.error(e,"Null Pointer Exception");

        }
    }
    @Test
    public void filenotFoundError() {

        System.out.println("\nStarting filenotFoundError() test now\n");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("myfile.txt");
        } catch (FileNotFoundException e) {
            rollbar.error(e,"File not found Exception " +e.getMessage());
            e.printStackTrace();
        }
    }
    @Test
    public void arrayOutOfBoundsError() {

        System.out.println("\nStarting arrayOutOfBoundsError() test now\n");

        int myArray[] = { 1,2,3 };

        try {
            int myValue = myArray[3]; // creates error
            System.out.println(myValue);
        } catch (ArrayIndexOutOfBoundsException e) {
            rollbar.critical(e," Out of bounds array exception " +e.getMessage().toString());
        }
    }
    @Test
    public void generateRandomError() {

        String myHash = Utils.genHash(10);
        System.out.println("Calling generateRandomError()  method");
        rollbar.info(myHash+ " Information Message");
    }
   // @Test
    public void unhandledException() {

        System.out.println("Calling unhandledException() test");
        int quotient = 1 / 0;
        System.out.println("Quotient: " +quotient);

    }
    @Test
    public void illegalStateException() {

        System.out.println("Calling illegalStateException()  method");

        try
        {
            //Instantiating an ArrayList object
            ArrayList<String> list = new ArrayList<String>();
            //populating the ArrayList
            list.add("apples");
            list.add("mangoes");
            //Getting the Iterator object of the ArrayList
            ListIterator<String> it = list.listIterator();
            //Removing the element without moving to first position which throws an exception
            it.remove();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            rollbar.critical(e, " illegalStateException " +e.getMessage());
        }
    }
}
