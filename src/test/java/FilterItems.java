import com.rollbar.api.payload.data.Data;
import com.rollbar.api.payload.data.Level;
import com.rollbar.notifier.filter.Filter;

import java.util.Map;

public class FilterItems implements Filter {

    @Override
    // don't send debug items
    public boolean preProcess(Level level, Throwable error, Map<String, Object> custom, String description) {
        return level.equals(Level.DEBUG);
    }

    @Override
    public boolean postProcess(Data data) {
        return false;
    }
}
