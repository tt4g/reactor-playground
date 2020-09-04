package com.github.tt4g.reactor.playground;

public class DummyResource implements AutoCloseable {
    private boolean closed;

    public DummyResource() {
        this.closed = false;
    }

    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void close() {
        this.closed = true;
    }
}
