package com.whatscan.toolkit.forwa.BulkSender.helper;

import com.whatscan.toolkit.forwa.Util.Event;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public final class FileSharingUtils {
    public static final FileSharingUtils INSTANCE = new FileSharingUtils();
    private static final DecimalFormat format = new DecimalFormat("#.##");

    private FileSharingUtils() {
    }

    public final double getFileSizeInMB(long j) {
        return ((double) j) / ((double) 1048576);
    }

    @NotNull
    public final String getFileSizeInString(long j) {
        double parseDouble = Double.parseDouble(String.valueOf(j));
        double d = 1048576;
        if (parseDouble > d) {
            return format.format(parseDouble / d) + " MB";
        }
        double d2 = 1024;
        if (parseDouble > d2) {
            return format.format(parseDouble / d2) + " KB";
        }
        return format.format(parseDouble) + " B";
    }

    @NotNull
    public final String getFileType(@NotNull String str) {
        switch (str) {
            case FileExtension.pdf:
                String name = Event.PDF.name();
                return name.toLowerCase();
            case FileExtension.Audio.mp3:
            case FileExtension.Audio.flac:
            case FileExtension.Audio.wav:
            case FileExtension.Audio.ogg:
            case FileExtension.Audio.m4a:
            case FileExtension.Audio.aac:
            case FileExtension.Audio.amr:
                String name2 = Event.AUDIO.name();
                return name2.toLowerCase();
            case "png":
            case "jpg":
            case FileExtension.Image.jpeg:
            case "webp":
                String name3 = Event.IMAGE.name();
                return name3.toLowerCase();
            case "mp4":
            case FileExtension.Video.mkv:
            case FileExtension.Video.threegp:
            case FileExtension.Video.webm:
                String name4 = Event.VIDEO.name();
                return name4.toLowerCase();
            default:
                String name5 = Event.IMAGE.name();
                return name5.toLowerCase();
        }
    }
}