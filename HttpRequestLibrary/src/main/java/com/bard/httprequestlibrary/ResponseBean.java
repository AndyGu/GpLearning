package com.bard.httprequestlibrary;

public class ResponseBean {
    private int resulecode;

    private String reason;

    public int getResulecode() {
        return resulecode;
    }

    public void setResulecode(int resulecode) {
        this.resulecode = resulecode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "resulecode=" + resulecode +
                ", reason='" + reason + '\'' +
                '}';
    }
}
