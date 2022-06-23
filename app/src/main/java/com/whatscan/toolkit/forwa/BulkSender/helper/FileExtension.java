package com.whatscan.toolkit.forwa.BulkSender.helper;

public final class FileExtension {
    public static final FileExtension INSTANCE = new FileExtension();
    public static final String pdf = "pdf";

    private FileExtension() {
    }

    public static final class Audio {
        public static final Audio INSTANCE = new Audio();
        public static final String aac = "aac";
        public static final String amr = "amr";
        public static final String flac = "flac";
        public static final String m4a = "m4a";
        public static final String mp3 = "mp3";
        public static final String ogg = "ogg";
        public static final String wav = "wav";

        private Audio() {
        }
    }

    public static final class Image {
        public static final Image INSTANCE = new Image();
        public static final String jpeg = "jpeg";
        public static final String jpg = "jpg";
        public static final String png = "png";
        public static final String webp = "webp";

        private Image() {
        }
    }

    public static final class Video {
        public static final Video INSTANCE = new Video();
        public static final String mkv = "mkv";
        public static final String mp4 = "mp4";
        public static final String threegp = "3gp";
        public static final String webm = "webm";

        private Video() {
        }
    }
}