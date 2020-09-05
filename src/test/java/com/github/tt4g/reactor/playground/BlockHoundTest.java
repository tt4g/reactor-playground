package com.github.tt4g.reactor.playground;

import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BlockHoundTest {

    @Test
    public void blockingSubscribeOnParallelScheduler() {
        assertThatThrownBy(() -> {
            Mono.fromCallable(() -> {
                Thread.sleep(10);

                return 0;
            })
            .subscribeOn(Schedulers.parallel())
            .block();
        }).hasCauseInstanceOf(BlockingOperationError.class)
        .hasMessageContaining("Blocking call!");
    }

    @Test
    public void blockingSubscribeOnBoundedElasticScheduler() {
        assertThatCode(() -> {
            Mono.fromCallable(() -> {
                Thread.sleep(10);

                return 0;
            })
                .subscribeOn(Schedulers.boundedElastic())
                .block();
        }).doesNotThrowAnyException();
    }
}
