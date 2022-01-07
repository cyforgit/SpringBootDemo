package example.asyncService;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@Slf4j
public class CommonAsyncService {

    @SneakyThrows
    @Async(value = "asyncPool")
    public Future<String> getA(int sleepTime) {
        Thread.sleep(sleepTime);
//        if (true) {
//            throw new RuntimeException("invalids");
//        }
        return new AsyncResult<>("A");
    }

    @SneakyThrows
    @Async
    public Future<String> getB(int sleepTime) {
        Thread.sleep(sleepTime);
        return new AsyncResult<>("B");
    }
}
