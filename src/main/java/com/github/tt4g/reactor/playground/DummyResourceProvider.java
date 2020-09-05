package com.github.tt4g.reactor.playground;

import java.util.Objects;

import reactor.core.publisher.Mono;

public class DummyResourceProvider {

    private final DummyResourceFactory dummyResourceFactory;

    public DummyResourceProvider(DummyResourceFactory dummyResourceFactory) {
        this.dummyResourceFactory = Objects.requireNonNull(dummyResourceFactory);
    }

    public Mono<DummyResource> provide() {
        DummyResource dummyResource = this.dummyResourceFactory.create();

        return Mono.just(dummyResource)
            .doFinally(signal -> dummyResource.close());
    }

}
