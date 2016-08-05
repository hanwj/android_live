package com.xcyo.baselib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Map;

public class FileUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final String CHAR_SET = "UTF-8";

    /**
     * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功�?)
     *
     * @param res      原字符串
     * @param filePath 文件路径
     * @return 成功标记
     */
    public static boolean string2File(String res, String filePath) {
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            File distFile = new File(filePath);
            if (!distFile.getParentFile().exists()) distFile.getParentFile().mkdirs();
            bufferedReader = new BufferedReader(new StringReader(res));
            bufferedWriter = new BufferedWriter(new FileWriter(distFile));
            char buf[] = new char[1024];         //字符缓冲�?
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf, 0, len);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            return flag;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 文本文件转换为指定编码的字符�?
     *
     * @param file     文本文件
     * @param encoding 编码类型
     * @return 转换后的字符�?
     * @throws IOException
     */
    public static String file2String(File file, String encoding) {
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            if (encoding != null && !encoding.isEmpty()) {
                reader = new InputStreamReader(new FileInputStream(file), encoding);
            } else {
                reader = new InputStreamReader(new FileInputStream(file));
            }
            //将输入流写入输出�?
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }

    private final static String TAG = "FileTool";

    /**
     * 下载文件
     *
     * @param loadUrl
     * @param sdcardPath
     * @param saveName
     * @return
     */
    public synchronized static boolean download(String loadUrl,
                                                String sdcardPath, String saveName) {
        InputStream input = null;
        BufferedInputStream bis = null;
        OutputStream output = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(loadUrl);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.connect();
            input = new BufferedInputStream(url.openStream());
            bis = new BufferedInputStream(input);
            output = new FileOutputStream(sdcardPath + saveName);
            bos = new BufferedOutputStream(output);
            byte data[] = new byte[1024];
            int count;
            while ((count = bis.read(data, 0, 1024)) != -1) {
                bos.write(data, 0, count);
                bos.flush();
            }
            httpConn.disconnect();
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage(), e);
        }finally {
            try {
                if(input != null)
                    input.close();
                if(bis != null)
                    bis.close();
                if(output != null)
                    output.close();
                if(bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输及文件传�??
     *
     * @param params
     * @param files
     * @return
     */
    public static String postFile(String action, Map<String, String> params,
                                  Map<String, File> files) {
        InputStream in = null;
        DataOutputStream outStream = null;
        try {
            String boundary = java.util.UUID.randomUUID().toString();
            String prefix = "--", linend = "\r\n", multipart_form_data = "multipart/form-data";
            URL uri = new URL(action);
            HttpURLConnection conn = (HttpURLConnection) uri
                    .openConnection();
            conn.setReadTimeout(15 * 1000); // 缓存的最长时�??
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓�??
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", CHAR_SET);
            conn.setRequestProperty("Content-Type", multipart_form_data
                    + ";boundary=" + boundary);
            // 首先组拼文本类型的参�??
            StringBuilder txtParamStr = new StringBuilder();
            outStream = new DataOutputStream(conn.getOutputStream());
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    txtParamStr.append(prefix);
                    txtParamStr.append(boundary);
                    txtParamStr.append(linend);
                    txtParamStr
                            .append("Content-Disposition: form-data; name=\""
                                    + entry.getKey() + "\"" + linend);
                    txtParamStr.append("Content-Type: text/plain; charset="
                            + CHAR_SET + linend);
                    txtParamStr.append("Content-Transfer-Encoding: 8bit"
                            + linend);
                    txtParamStr.append(linend);
                    txtParamStr.append(entry.getValue());
                    txtParamStr.append(linend);
                }
                outStream.write(txtParamStr.toString().getBytes());
            }

            // 发�?文件数据
            if (files != null) {
                for (String paramName : files.keySet()) {
                    File file = files.get(paramName);
                    StringBuilder fileParamStr = new StringBuilder();
                    fileParamStr.append(prefix);
                    fileParamStr.append(boundary);
                    fileParamStr.append(linend);
                    fileParamStr
                            .append("Content-Disposition: form-data; name=\""
                                    + paramName
                                    + "\"; filename=\""
                                    + file.getName() + "\"" + linend);
                    fileParamStr
                            .append("Content-Type: image/png; charset="
                                    + CHAR_SET + linend);
                    fileParamStr.append(linend);
                    outStream.write(fileParamStr.toString().getBytes());
                    InputStream is = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    android.util.Log.d(TAG, "post file size: " + file.length());
                    is.close();
                    outStream.write(linend.getBytes());
                }
            }

            // 请求结束标志
            byte[] end_data = (prefix + boundary + prefix + linend)
                    .getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应�??
            int res = conn.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (res == 200) {
                in = conn.getInputStream();
                int ch;

                while ((ch = in.read()) != -1) {
                    response.append((char) ch);
                }
            }
            conn.disconnect();
            return response.toString();
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage(), e);
            return null;
        }finally {
            try {
                if(outStream != null)
                    outStream.close();
                if(in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 本地文件拷贝
     *
     * @param srcFile
     * @param copyFile
     * @return
     */
    public static boolean copy(File srcFile, File copyFile) {
        InputStream input = null;
        OutputStream output = null;
        try {
            if (srcFile.exists()) {
                input = new FileInputStream(srcFile);
                output = new FileOutputStream(copyFile);
                int count;
                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                    output.flush();
                }
                return true;
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage(), e);
        }finally {
            try {
                if(input != null)
                    input.close();
                if(output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取格式化后的文件大�??
     *
     * @param file
     * @return
     */
    public static String getFormatSize(File file) {
        try {
            if (file.exists()) {
                double size = getDirSize(file);
                DecimalFormat df = new DecimalFormat("#.#");
                String formatSize = "0B";
                if (size < 1024) {
                    formatSize = df.format(size) + "B";
                } else if (size < (1024 * 1024)) {
                    formatSize = df.format(size / 1024) + "K";
                } else if (size < (1024 * 1024 * 1024)) {
                    formatSize = df.format(size / (1024 * 1024)) + "M";
                } else if (size < (1024 * 1024 * 1024 * 1024)) {
                    formatSize = df.format(size / (1024 * 1024 * 1024)) + "G";
                }
                return formatSize;
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage(), e);
        }
        return "0B";
    }

    public static final double getDirSize(final File file){
        if(file.exists()){
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“B”为单位
                double size = (double) file.length();
                return size;
            }
        }
        return 0;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路�??如：c:/fqf.txt
     * @param newPath String 复制后路�??如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
//           int bytesum = 0;   
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在�??
                inStream  = new FileInputStream(oldPath); //读入原文�??
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
//                   bytesum += byteread; //字节�??文件大小   
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(inStream != null)
                    inStream.close();
                if(fs != null)
                    fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static boolean clearFile(String filePath) throws Exception {
        if(filePath == null)
            filePath = "";
        File file = new File(filePath);
        if (file.exists()) {
            String[] name = file.list();
            for (int index = 0; index < name.length; index++) {
                File child = new File(file.getPath(), name[index]);
                if(child.exists() && !child.isDirectory() && !child.getName().startsWith(".")){
                    child.delete();
                }else if(child.exists() && child.isDirectory()){
                    clearFile(child.getPath());
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否是一个目录，如果是则判断是否存在，若不存在则创建
     */
    public static final void isFileAndMkDir(String urlPath) {
        File file = new File(urlPath);
        if (!file.exists()) {
            if (!file.getPath().contains("."))
                file.mkdirs();
            else if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
    }

}
