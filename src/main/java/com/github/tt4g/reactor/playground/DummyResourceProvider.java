package com.github.tt4g.reactor.playground;

import reactor.core.publisher.Mono;

public class DummyResourceProvider {

    public Mono<DummyResource> provideAndClose(DummyResource dummyResource) {
        return Mono.just(dummyResource)
            .doFinally(signal -> dummyResource.close());
    }

}
