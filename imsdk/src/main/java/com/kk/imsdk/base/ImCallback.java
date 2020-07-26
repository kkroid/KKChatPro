package com.kk.imsdk.base;

public interface ImCallback<Data> {
    void callback(int code, String desc, Data data);

    default void callback(int code, String desc) {
        callback(code, desc, null);
    }
}
