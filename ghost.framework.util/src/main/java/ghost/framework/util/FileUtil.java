package ghost.framework.util;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:文件工具类。
 * @Date: 21:32 2018/3/26
 */
public final class FileUtil {
    /**
     * 读取文件内容
     * @param fileName
     * @return
     */
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
    public static URL toURL(String path) throws MalformedURLException {
        return new URL("file:/" + path.replaceAll("\\\\", "/"));
    }

    public static URL toURL(File file) throws MalformedURLException {
        return new URL("file:/" + file.getPath().replaceAll("\\\\", "/"));
    }

    /**
     * 获取文件列表的数组URL
     *
     * @param paths 文件列表
     * @return
     * @throws MalformedURLException
     */
    public static URL[] getURLs(List<File> paths) throws MalformedURLException {
        URL[] urls = new URL[paths.size()];
        int i = 0;
        for (File path : paths) {
            urls[i] = new URL("file:/" + path.getPath().replaceAll("\\\\", "/"));
            i++;
        }
        return urls;
    }

    /**
     * 输入流创建文件
     *
     * @param stream   文件流
     * @param filePath 文件路径
     * @throws IOException
     */
    public static void inputStreamCreateFile(InputStream stream, String filePath) throws IOException {
        inputStreamCreateFile(stream, new File(filePath));
    }

    /**
     * 输入流创建文件
     *
     * @param stream   文件流
     * @param filePath 文件路径
     * @throws IOException
     */
    public static void inputStreamCreateFile(InputStream stream, File filePath) throws IOException {
        //判断文件是否存在
        if (!filePath.exists()) {
            //文件不存在创建文件
            filePath.createNewFile();
        }
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            byte[] bytes = new byte[1024];
            int len = 0;
            while (-1 != (len = stream.read(bytes))) {
                output.write(bytes, 0, len);
            }
            output.flush();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获取目录下所有文件
     *
     * @param directory 文件夹
     * @return
     */
    public static List<File> getFiles(String directory) {
        //目标集合fileList
        List<File> list = new ArrayList();
        getFiles(directory, list);
        return list;
    }

    /**
     * 获取目录下所有文件
     *
     * @param directory 文件夹
     * @param list      文件列表
     */
    public static void getFiles(String directory, List<File> list) {
        File file = new File(directory);
        File[] files = file.listFiles();
        if (files != null) {
            for (File fileIndex : files) {
                //如果这个文件是目录，则进行递归搜索
                if (fileIndex.isDirectory()) {
                    getFiles(fileIndex.getPath(), list);
                } else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    list.add(fileIndex);
                }
            }
        }
    }
    public static @NotNull String streamToString(@NotNull InputStream stream) throws UnsupportedEncodingException {
        return new String(streamToBytes(stream), "UTF-8");
    }
    /**
     * @param stream
     * @return
     */
    public static @NotNull byte[] streamToBytes(@NotNull InputStream stream) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int rc = 0;
            while ((rc = stream.read(buff, 0, buff.length)) != -1) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 流转base64
     *
     * @param stream
     * @return
     */
    public static String streamToBase64String(InputStream stream) {
        return new String(Base64.getEncoder().encode(streamToBytes(stream)));
    }

    /**
     * 更新文件扩展名。
     *
     * @param file 要更改的文件。
     * @param s    指定要替换的文件扩展名。
     * @param u    指定替换的文件扩展名。
     * @return
     */
    public static File updateExtensionName(File file, String s, String u) {
        String path = file.getPath();
        if (path.endsWith(s)) {
            path = path.substring(0, path.length() - 4) + "." + u;
            return new File(path);
        }
        return file;
    }

    /**
     * 判断url路径文件是否存在
     * @param url
     * @return
     * @throws MalformedURLException
     */
    public static boolean exists(String url) throws MalformedURLException{
        return exists(new URL(url));
    }

    /**
     * 判断url路径文件是否存在
     * @param url
     * @return
     */
    public static boolean exists(URL url) {
        try (InputStream stream = url.openConnection().getInputStream()) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 获取资源文件路径。
     *
     * @return
     */
    public static File getResourcePath() {
        File path = null;
        try {
            //application应用获取路径。
            return new File(Class.class.getClass().getResource("/").getPath());
        } catch (NullPointerException e) {
            //web spring boot 获取路径。
            try {
                //获取跟目录
                path = new File(Thread.currentThread().getContextClassLoader().getResource("/").getPath());
                if (!path.exists()) path = new File("");
            } catch (Exception fe) {
                fe.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 创建临时程序包文件。
     *
     * @param fileName 程序包文件名称。
     * @param data     程序包数据。
     * @return
     * @throws IOException
     */
    public static File createTmpDirNewFile(String fileName, byte[] data) throws IOException {
        String tmpdir = System.getProperty("java.io.tmpdir");
        File jar = new File(tmpdir + File.separator + fileName);
        if (jar.exists())
            jar.delete();
        createNewFile(jar, data);
        return jar;
    }

    /**
     * 按照指定路径创建文件目录。
     *
     * @param path    指定跟目录。
     * @param strings 要在跟目录创建的文件列表。
     * @return 返回以根目录创建的目录列表。
     */
    public static List<File> createNewMkdirsList(String path, String[] strings) {
        List<File> fileList = new ArrayList<>();
        for (String s : strings) {
            fileList.add(FileUtil.createNewMkdirs(path + File.separator + s));
        }
        return fileList;
    }

    /**
     * @param path
     * @param fileName
     * @param data
     * @return
     * @throws IOException
     */
    public static File createTmpDirNewFile(String path, String fileName, byte[] data) throws IOException {
        File jar = new File(path + File.separator + fileName);
        if (jar.exists())
            jar.delete();
        createNewFile(jar, data);
        return jar;
    }

    /**
     * 获取文件扩展名称。
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 获取文件名称。
     *
     * @param filename
     * @return
     */
    public static String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 读取文件全部数据。
     *
     * @param file
     * @return
     */
    public static byte[] fileAllData(File file) throws IOException {
        InputStream input = null;
        ByteArrayOutputStream buffer = null;
        try {
            input = new FileInputStream(file);
            buffer = new ByteArrayOutputStream();
            int data = input.read();
            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (buffer != null) buffer.close();
                if (input != null) input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建写入文件。
     *
     * @param file
     * @param data
     * @throws IOException
     */
    public static void createNewFile(File file, byte[] data) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data);
        } finally {
            if (out != null) out.close();
        }
    }

    /**
     * 创建文件。
     * 文件目录不存在是自动创建目录
     *
     * @param path
     * @return
     */
    public static File createNewMkdirs(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    /**
     * 删除跟目录下指定文件。
     * 包类指定文件所有上级目录，直到跟目录。
     *
     * @param rootDirectory
     * @param file
     */
    public static void deleteRootDirectoryNextAll(String rootDirectory, File file) {
        if (file != null) {
            List<File> del = new ArrayList<>();
            del.add(file);
            File parent = file.getParentFile();
            while (parent != null) {
                if (parent.getPath().equals(rootDirectory)) {
                    break;
                }
                if (!parent.getPath().equals(file.getParent())) {
                    //判断目录没有子文件夹删除。
                    if (parent.listFiles().length == 1) {
                        if (parent.isDirectory()) del.add(parent);
                    } else {
                        break;
                    }
                } else {
                    if (parent.isDirectory()) del.add(parent);
                }
                parent = parent.getParentFile();
            }
            for (File d : del) {
                d.delete();
            }
        }
    }

    /**
     * url转文件
     * @param url
     * @return
     */
    public static File toFile(URL url) {
        return new File(url.getPath());
    }
}
