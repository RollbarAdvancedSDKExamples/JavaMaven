import com.rollbar.api.payload.data.Person;
import com.rollbar.notifier.provider.Provider;
import java.util.Random;

public class MyPersonProvider implements Provider<Person> {

    // @Override
    public Person provide() {

        // with rollbars person object you can pass in User ID, email and username or only use one of these to identify a user
        // this example is used in generating random users


        int max = 20;
        int min = 1;
        Random rand = new Random();
        int randomNumId = rand.nextInt((max - min) + 1) + min;

        Person command = null;
        command = new Person.Builder().id(String.valueOf(randomNumId)).build();
        return command;

    }
}