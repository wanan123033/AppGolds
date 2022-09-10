package appgodx.mmkv;

import android.content.Context;

public class MMKV {
    private static String rootDir;
    private static final String MMKV_DEFAULT = "mmkv_default";

    static {
        System.loadLibrary("godx-mmkv");
    }
    public static final int SINGLE_PROCESS_MODE = 1;
    public static final int MULTI_PROCESS_MODE = 2;
    private final long handle;
    private MMKV(long handle) {
        this.handle = handle;
    }

    public static void initialize(Context context){
        initialize(context.getFilesDir().getAbsolutePath()+"/mmkv");
    }

    private static void initialize(String rootPath) {
        MMKV.rootDir = rootPath;
        jniInitialize(rootPath);
    }

    public static MMKV defaultMMKV(){
        if (rootDir == null){
            throw new IllegalArgumentException("You should Call MMKV.initialize() first.");
        }
        return defaultMMKV(SINGLE_PROCESS_MODE);
    }

    public static MMKV defaultMMKV(int mode) {
        if (rootDir == null){
            throw new IllegalArgumentException("You should Call MMKV.initialize() first.");
        }
        return mmkvById(MMKV_DEFAULT,mode);
    }

    public static MMKV mmkvById(String mmapId, int mode) {
        if (rootDir == null){
            throw new IllegalArgumentException("You should Call MMKV.initialize() first.");
        }
        return new MMKV(mmkvWithId(mmapId,mode));
    }

    public void putInt(String key,int value){
        putInt(handle,key,value);
    }
    public int getInt(String key){
        return getInt(handle,key);
    }
    public void putString(String key,String value){
        putString(handle,key,value);
    }
    public String getString(String key){
        return getString(handle,key);
    }

    private static native String getString(long handle, String key);

    private static native void putString(long handle, String key, String value);

    private static native void jniInitialize(String rootPath);
    private static native long mmkvWithId(String mmapId, int mode);
    private native static void putInt(long handle, String key, int value) ;
    private native static int getInt(long handle, String key);
}
