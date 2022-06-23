package com.whatscan.toolkit.forwa.GetSet;

public class GetSetAudio {
    private final String LastModified;
    private final String fileName;
    private final String filePath;
    private final String fileSize;

    public GetSetAudio(String str, String str2, String str3, String str4) {
        this.fileName = str;
        this.LastModified = str2;
        this.fileSize = str3;
        this.filePath = str4;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileSize() {
        return fileSize;
    }
}
