package com.chess.config;

import java.io.Serializable;

public class RespuestaErrorHttp implements Serializable{
    private String error;

    public RespuestaErrorHttp() {

    }

    public RespuestaErrorHttp(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "RespuestaErrorHttp [error=" + error + "]";
    }

}