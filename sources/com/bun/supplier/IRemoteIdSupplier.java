package com.bun.supplier;

public interface IRemoteIdSupplier extends InnerIdSupplier {
    String getAAID(String str);

    String getVAID(String str);
}
