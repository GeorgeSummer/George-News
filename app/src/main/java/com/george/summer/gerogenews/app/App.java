package com.george.summer.gerogenews.app;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.xutils.x;

/**
 * Created by Administrator on 2016/8/31.
 */
public class App extends Application {

    private static ImageLoader loader;
    private static RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        queue = Volley.newRequestQueue(this);
        loader = new ImageLoader(queue,new BitmapCache());
    }

    private static class BitmapCache implements ImageLoader.ImageCache {
        private static LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(5*1024*1024){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        @Override
        public Bitmap getBitmap(String s) {
            return cache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            cache.put(s,bitmap);
        }
    }

    public static ImageLoader getLoader() {
        return loader;
    }

    public static RequestQueue getQueue() {
        return queue;
    }
}
