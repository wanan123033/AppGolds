package appgodx.ffmpeg;

public class FFmpegCmd {
    static {
        System.loadLibrary("godx-ffmpeg");
    }
    public static void exec(String command){
        exec(command.split(" "));
    }
    public static native void exec(String[] command);
}
