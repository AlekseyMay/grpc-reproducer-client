package com.reproducer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class StreamingTest {

    @GrpcClient("streaming")
    StreamSvc streamSvc;


    // here all is ok, and we see failure which happened at the 30th element on the server side
    @Test
    void testQuickStreamFailure() {
        Multi<StringRequest> multi = Multi.createFrom().range(1, 50)
                // delaying stream to make it a bit longer
                .call(() -> Uni.createFrom().nullItem().onItem().delayIt().by(Duration.of(1000, ChronoUnit.NANOS)))
                .map(x -> StringRequest.newBuilder()
                        .setAnyValue(x.toString())
                        .build());
        //                .invoke(x -> log.info("Stream piece number is: " + x.getAnyValue()));

        UniAssertSubscriber<StringReply> subscriber = streamSvc.stringStream(multi)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber
                .awaitFailure()
                .assertFailedWith(StatusRuntimeException.class);
    }

    // here all is strange, and we don't see failure which happened at the 30th element on the server side
    // we actually see nothing, so test fails
    @Test
    void testBigStreamFailure() {
        Multi<StringRequest> multi = Multi.createFrom().range(1, 10000)
                // delaying stream to make it a bit longer
                .call(() -> Uni.createFrom().nullItem().onItem().delayIt().by(Duration.of(1000, ChronoUnit.NANOS)))
                .map(x -> StringRequest.newBuilder()
                        .setAnyValue(x.toString())
                        .build());
//                        .invoke(x -> log.info("Stream piece number is: " + x.getAnyValue()));

        UniAssertSubscriber<StringReply> subscriber = streamSvc.stringStream(multi)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber
                .awaitFailure()
                .assertFailedWith(StatusRuntimeException.class);
    }
}
