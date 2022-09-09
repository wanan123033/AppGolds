package appgodx.ffmpeg;

import android.view.SurfaceHolder;

public class FFmpegPlayer {
    static {
        System.loadLibrary("godx-ffmpeg");
    }
    private static long handle;
    public FFmpegPlayer(){
        handle = nativeInit();
    }

    public void setDataSource(String path){
        setDataSource(handle,path);
    }
    public void setSurface(SurfaceHolder holder){
        setSurface(handle,holder);
    }

    public void prepare(){
        prepare(handle);
    }
    public void start(){
        start(handle);
    }

    private native void start(long handle);
    private native void setSurface(long handle, SurfaceHolder holder);
    private native void prepare(long handle);
    private native static long nativeInit();
    private native void setDataSource(long handle, String path);
}
