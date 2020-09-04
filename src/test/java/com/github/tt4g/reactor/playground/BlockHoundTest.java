package com.github.tt4g.reactor.playground;

import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BlockHoundTest {

    @Test
    public void blocking() {
        assertThatThrownBy(() -> {
            Mono.fromCallable(() -> {
                Thread.sleep(1000);

                return 0;
            })
            .subscribeOn(Schedulers.parallel())
            .block();
        }).hasCauseInstanceOf(BlockingOperationError.class)
        .hasMessageContaining("Blocking call!");
    }
}
