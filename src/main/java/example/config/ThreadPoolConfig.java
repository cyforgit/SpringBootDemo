package example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class ThreadPoolConfig {

    @Bean(name = "asyncPool")
    public Executor taskExecutor() {
        return Executors.newCachedThreadPool();
    }
}
