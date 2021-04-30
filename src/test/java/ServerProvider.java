
import com.rollbar.notifier.config.Config;
import com.rollbar.notifier.config.ConfigBuilder;
import com.rollbar.notifier.config.ConfigProvider;

import com.rollbar.api.payload.data.Server;
import com.rollbar.notifier.provider.Provider;

import java.util.HashMap;
import java.util.Map;


class ServerProvider implements Provider<Server> {

    @Override
    public Server provide() {
        return new Server.Builder()
                //   .codeVersion(MyConfiguration.myCodeVersion)
                .branch(MyConfig.myBranch)
                .host(MyConfig.myHost)
                .root(MyConfig.myRoot)
                .build();
    }
}