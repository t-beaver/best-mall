package com.bun.supplier;

public interface IdSupplier {
    String getAAID();

    String getOAID();

    String getVAID();

    boolean isSupported();
}
