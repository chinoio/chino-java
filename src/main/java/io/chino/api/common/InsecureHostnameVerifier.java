package io.chino.api.common;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@Deprecated
public class InsecureHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}