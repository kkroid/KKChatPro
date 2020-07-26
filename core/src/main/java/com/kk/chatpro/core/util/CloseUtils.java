package com.kk.chatpro.core.util;

import java.io.Closeable;
import java.io.IOException;


@SuppressWarnings("unused")
public final class CloseUtils {
    private CloseUtils() {
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
