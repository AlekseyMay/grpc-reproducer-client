package com.reproducer;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@ApplicationScoped
public class StreamStringGrpcClient {

    @GrpcClient("streaming")
    StreamSvc streamSvc;

    void onStart(@Observes StartupEvent event) {
//        Multi<StringRequest> multi = Multi.createFrom().range(1, 10000)
//                // delaying stream to make it a bit longer
//                .call(() -> Uni.createFrom().nullItem().onItem().delayIt().by(Duration.of(1000, ChronoUnit.NANOS)))
//                .map(x -> StringRequest.newBuilder()
//                .setAnyValue(x.toString())
//                .build())
//                .invoke(x -> log.info("Stream piece number is: " + x.getAnyValue()));
//
//        streamSvc.stringStream(multi)
//                .subscribe().with(ok -> log.info("All is ok: {}", ok), th -> log.error(th.getMessage(), th));
    }

}
