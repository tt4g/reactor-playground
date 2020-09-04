package com.github.tt4g.reactor.playground;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DummyResourceProviderTest {

    private DummyResource dummyResource;

    private DummyResourceProvider dummyResourceProvider;

    @BeforeEach
    void setUp() {
        this.dummyResource = new DummyResource();
        this.dummyResourceProvider = new DummyResourceProvider();
    }

    @AfterEach
    void tearDown() {
        this.dummyResource = null;
        this.dummyResourceProvider = null;
    }

    private Mono<DummyResource> provide() {
        return this.dummyResourceProvider.provideAndClose(this.dummyResource);
    }

    @Test
    public void isClosedAfterBlock() {
        Mono<DummyResource> dummyResourceMono = provide();

        DummyResourceAssert.assertThat(dummyResourceMono.block())
            .isClosed();
    }

    @Test void isClosedAfterThrowException() {
        Mono<DummyResource> dummyResourceMono =
            provide()
                .map(_dummyResource -> {
                    throw new RuntimeException();
                });

        assertThatThrownBy(() -> dummyResourceMono.block())
            .isInstanceOf(RuntimeException.class);
        DummyResourceAssert.assertThat(this.dummyResource)
            .isClosed();
    }

    static class DummyResourceAssert extends AbstractAssert<DummyResourceAssert, DummyResource> {

        public DummyResourceAssert(DummyResource dummyResource) {
            super(dummyResource, DummyResourceAssert.class);
        }

        public static DummyResourceAssert assertThat(DummyResource dummyResource) {
            return new DummyResourceAssert(dummyResource);
        }

        public DummyResourceAssert isClosed() {
            Assertions.assertThat(actual.isClosed()).isTrue();

            return this;
        }

        public DummyResourceAssert isNotClosed() {
            Assertions.assertThat(actual.isClosed()).isFalse();

            return this;
        }
    }

}
