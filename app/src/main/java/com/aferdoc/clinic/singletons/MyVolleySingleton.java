package com.aferdoc.clinic.singletons;

/**
 * Created by kasumba on 9/26/16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MyVolleySingleton {

        private static MyVolleySingleton mInstance;
        private RequestQueue mRequestQueue;
        private ImageLoader mImageLoader;
        private static Context mCtx;

        private MyVolleySingleton(Context context) {
            mCtx = context;
            mRequestQueue = getRequestQueue();

            mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
                LruCache<String, Bitmap> cache = new LruCache((int)Runtime.getRuntime().maxMemory()/1024/8);
                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url,bitmap);
                }
            });
        }
        public static synchronized MyVolleySingleton getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new MyVolleySingleton(context);
            }
            return mInstance;
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }

        public ImageLoader getImageLoader() {
            return mImageLoader;
        }

    }