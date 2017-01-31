package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

import java.util.Objects;

public enum ServiceStatus {

    SUCCEEDED, FAILED, UNAUTHORIZED, CONNECTION_ERROR;

    ServiceStatus() {
    }

    public boolean getBooleanStatus() {
        return (Objects.equals(name(), SUCCEEDED.name()));
    }
}


