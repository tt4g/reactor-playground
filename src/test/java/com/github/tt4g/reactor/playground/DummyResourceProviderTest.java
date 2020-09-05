package com.github.tt4g.reactor.playground;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class DummyResourceProviderTest {

    @Mock
    private DummyResource dummyResource;

    @Mock
    private DummyResourceFactory dummyResourceFactory;

    @InjectMocks
    private DummyResourceProvider dummyResourceProvider;

    @BeforeEach
    void setUp() {
        Mockito.when(this.dummyResourceFactory.create()).thenReturn(this.dummyResource);
    }

    @AfterEach
    void tearDown() {
        this.dummyResource = null;
        this.dummyResourceProvider = null;
    }

    @Test
    public void closedAfterBlock() {
        Mono<DummyResource> dummyResourceMono = this.dummyResourceProvider.provide();

        Mockito.verify(this.dummyResource, Mockito.never()).close();

        StepVerifier.create(dummyResourceMono)
            .expectNext(this.dummyResource)
            .verifyComplete();

        Mockito.verify(this.dummyResource, Mockito.atLeastOnce()).close();
    }

    @Test
    public void closedAfterThrowException() {
        Mono<DummyResource> dummyResourceMono =
            this.dummyResourceProvider.provide()
                .map(_dummyResource -> {
                    throw new RuntimeException();
                });

        StepVerifier.create(dummyResourceMono)
            .expectError(RuntimeException.class)
            .verify();

        Mockito.verify(this.dummyResource, Mockito.atLeastOnce()).close();
    }

    @Test
    public void closedAfterFlatMap() {
        Mono<Integer> dummyResourceMono =
            this.dummyResourceProvider.provide()
                .flatMap(_dummyResource -> Mono.just(100));

        StepVerifier.create(dummyResourceMono)
            .expectNext(100)
            .verifyComplete();

        Mockito.verify(this.dummyResource, Mockito.atLeastOnce()).close();
    }

    @Test
    public void closedAfterThrowExceptionFromFlatMap() {
        Mono<Integer> dummyResourceMono =
            this.dummyResourceProvider.provide()
                .flatMap(_dummyResource ->
                    Mono.fromCallable(() -> {
                        throw new RuntimeException();}
                    ));

        StepVerifier.create(dummyResourceMono)
            .expectError(RuntimeException.class)
            .verify();

        Mockito.verify(this.dummyResource, Mockito.atLeastOnce()).close();
    }

    @Test
    public void closedAfterDispose() {
        Mono<DummyResource> dummyResourceMono = this.dummyResourceProvider.provide();

        dummyResourceMono.subscribe().dispose();

        Mockito.verify(this.dummyResource, Mockito.atLeastOnce()).close();
    }

}
