package com.bun.supplier;

public interface InnerIdSupplier extends IdSupplier {
    void a(SupplierListener supplierListener);

    boolean a();

    String getUDID();

    void shutDown();
}
