package com.xcyo.live.utils;

import com.xutils.http.cookie.DbCookieStore;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/3/25.
 */
public enum CookieUtil{

    INSTANCE;

    private DbCookieStore db;
    private static List<URI> uris;
    private static final String[] url = new String[]{
    };

    static{
        uris = new ArrayList<URI>();
        for(String u : url){
            try {
                uris.add(new URI(u));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    CookieUtil(){
        db = DbCookieStore.INSTANCE;
    }

    /**
     * 注册需要正确提交cookies的URL
     * @param url
     */
    public synchronized  void registURL(String url){
        try {
            if(url != null) {
                uris.add(new URI(url));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有注册的uri
     * @return
     */
    public List<URI> getURL(){
        return uris;
    }

    /**
     * 删除注册的URL及其相关的所有COOKIE
     * @param url
     */
    public synchronized  void removeCookieURL(String url){
        try {
            if(url != null && getDb() != null){
                URI uri = new URI(url);
                uris.remove(uri);
                List<HttpCookie> cookies = getDb().get(uri);
                for(HttpCookie cookie : cookies){
                    getDb().remove(uri, cookie);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private synchronized DbCookieStore getDb(){
        if(db != null){
            synchronized (db){
                return db;
            }
        }
        return null;
    }

    /**
     * 保存cookie
     * @param key
     * @param value
     */
    public synchronized void saveCookie(String key, String value){
        if(key == null || value == null)
            return;
        if(getDb() != null){
            for(URI uri : uris){
                getDb().add(uri, configHttpCookie(key, value));
            }
        }
    }

    private HttpCookie configHttpCookie(String key, String value){
        return new HttpCookie(key, value);
    }

    /**
     * 删除一个指定cookies, 会删除所有注册URL下的这个cookie
     * @param key
     */
    public synchronized void clearCookies(String key){
        if(key == null){
            return;
        }
        if(getDb() != null){
            for (URI uri : uris){
                List<HttpCookie> cookies = getDb().get(uri);
                if(cookies != null){
                    for(HttpCookie cookie : cookies){
                        getDb().remove(uri, cookie);
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 删除所有cookie
     */
    public synchronized  void clearCookies(){
        if(getDb() != null)
            getDb().removeAll();
    }


    /**
     * 获取所有的Cookies
     * @return
     */
    public synchronized  List<HttpCookie> getCookies(){
        if(getDb() != null)
            return getDb().getCookies();
        return null;
    }

    /**
     * 获取这个指定Url下的所有cookies
     * @param url
     * @return
     */
    public synchronized  List<HttpCookie> getCookies(String url){
        if(url == null)
            return null;
        if(getDb() != null){
            try {
                return getDb().get(new URI(url));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取这个指定的Url下的指定key的cookies值
     * @param url
     * @param key
     * @return
     */
    public synchronized  String getCookies(String url, String key){
        if(url == null || key == null)
            return null;
        if(getDb() != null){
            List<HttpCookie> cookies = getCookies(url);
            for(HttpCookie cookie : cookies){
                if(key.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
