import com.rollbar.api.payload.data.Data;
import com.rollbar.notifier.transformer.Transformer;

// For example, to remove the framework:
class RemoveFrameworkTransformer implements Transformer {

    @Override
    public Data transform(Data data) {
        return new Data.Builder(data)
                .framework(null)
                .build();
    }
}