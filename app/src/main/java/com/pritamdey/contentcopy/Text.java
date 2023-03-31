package com.pritamdey.contentcopy;

public class Text {
    private String text, appName;
    private long timestamp;

    public Text(String text, String appName) {
        this.text = text;
        this.appName = appName;
        this.timestamp = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
