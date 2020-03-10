package com.zuoer.sofa.blog.base;

import com.zuoer.sofa.blog.base.error.ErrorCode;
import com.zuoer.sofa.blog.base.exception.BaseRuntimeException;
import com.zuoer.sofa.blog.base.utils.ListUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author zuoer
 * @version $Id: BaseClassUtils.java, v 0.1 2019/12/12 10:27 zuoer Exp $$
 */
public class BaseClassUtils {

    private static final String BENCH_PACKAGE_BASE_NAME="com.zuoer.sofa.blog.base";

    private static List<Class<?>> baseClassList=new ArrayList<>();
    private static Object baseClassesLock = new Object();

    /**
     * 获取所有的BenchClass
     *
     * @return
     */
    public static List<Class<?>> getClasses() {
        if ( !ListUtils.isEmpty(baseClassList)) {
            return baseClassList;
        }
        synchronized (baseClassesLock) {
            if (!ListUtils.isEmpty(baseClassList)) {
                return baseClassList;
            }
            try {
                baseClassList = Collections.unmodifiableList(getChildClasses(BENCH_PACKAGE_BASE_NAME,true));
            }catch (Exception e){
                throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "获取base中所有类异常" , e);
            }
        }
        return baseClassList;
    }

    public static List<Class<?>> getChildClasses(String packageName, boolean childPackage)  throws IOException,ClassNotFoundException {
        List<Class<?>> classList=new ArrayList<>();
        for(String className:getClassName(packageName,childPackage)){
            classList.add(Class.forName(className));
        }
        return classList;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName  包名
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws UnsupportedEncodingException
     */
    public static List<String> getClassName(String packageName, boolean childPackage) throws IOException {
        List<String> fileNames = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        Enumeration<URL> urls = loader.getResources(packagePath);
        List<URL> urlList=new ArrayList<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url == null)
                continue;
            urlList.add(url);
            String type = url.getProtocol();
            if (type.equals("file")) {
                fileNames.addAll(getClassNameByFile(url.getPath(), childPackage));
            } else if (type.equals("jar")) {
                fileNames.addAll(getClassNameByJar(url.getPath(), childPackage));
            }
        }

        fileNames.addAll(getClassNameByJars(urlList, packagePath, childPackage));
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath     文件路径
     *                     类名集合
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws UnsupportedEncodingException
     */
    private static List<String> getClassNameByFile(String filePath, boolean childPackage) throws UnsupportedEncodingException {
        List<String> myClassName = new ArrayList<>();
        filePath = URLDecoder.decode(filePath);
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null)
            return myClassName;
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), childPackage));
                }
            } else {
                String childFilePath = childFile.getPath();
//                childFilePath = FileUtil.clearPath(childFilePath);
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("classes") + 8, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }
        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath      jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws UnsupportedEncodingException
     */
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) throws UnsupportedEncodingException {
        List<String> myClassName = new ArrayList<String>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        jarFilePath = URLDecoder.decode(jarFilePath);
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            //SystemLog.Log(LogType.systemInfo, e.getMessage(), e);
        }
        return myClassName;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls         URL集合
     * @param packagePath  包路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws UnsupportedEncodingException
     */
    private static List<String> getClassNameByJars(List<URL> urls, String packagePath, boolean childPackage) throws UnsupportedEncodingException {
        List<String> myClassName = new ArrayList<String>();
        if (urls != null) {
            for (int i = 0; i < urls.size(); i++) {
                URL url = urls.get(i);
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }
}
