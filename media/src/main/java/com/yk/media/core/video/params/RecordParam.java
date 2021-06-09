package com.yk.media.core.video.params;

public class RecordParam {
    private String path;

    private RecordParam() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static class Builder {
        private RecordParam audioRecordParam;

        public Builder() {
            audioRecordParam = new RecordParam();
        }

        public Builder setPath(String path) {
            audioRecordParam.setPath(path);
            return this;
        }

        public RecordParam build() {
            return audioRecordParam;
        }
    }
}
