package pp.rsmmm.global.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DummyDataLoader implements ApplicationRunner {

    private final DummyDataService dummyDataService;

    @Autowired
    public DummyDataLoader(DummyDataService dummyDataService) {
        this.dummyDataService = dummyDataService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dummyDataService.createDummyData();
    }
}
