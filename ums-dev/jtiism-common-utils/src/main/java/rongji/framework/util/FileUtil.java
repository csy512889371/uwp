package rongji.framework.util;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.*;

/**
 * <p>
 * Title: 文件工具类
 * </p>
 * <p>
 * Description:封装一些常用的文件常用方法 1.提供向指定文件末尾追加指定内容 2.获取一个目录下所有的文件内容 3.获取指定目录文件的内容
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: RongJi
 * </p>
 *
 * @author redlwb
 * @version 1.0
 * @create in 2012-8-22
 */
public class FileUtil {

    private FileUtil() {
    }

    ;

    /**
     * 对指定位置的文件在增加指定内容
     *
     * @param dir     文件地址
     * @param content 内容
     * @throws IOException
     * @author：redlwb add 2012-9-24
     */
    public static void appendConent(String dir, String content) throws IOException {
        File file = new File(dir);
        if (!file.exists()) {
            int index = dir.lastIndexOf("/");
            new File(dir.substring(0, index)).mkdirs();
        }
        FileWriter fileWriter = new FileWriter(file, true); // 第二个参数true表示将以追加形式写文件
        fileWriter.write("\n" + content + "\n");
        fileWriter.close();
    }

    /**
     * 获取一个目录下的所有文件内容
     *
     * @param filePath 文件地址
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @author：redlwb add 2012-9-24
     */
    public static String[][] getFilesContent(String filePath) throws FileNotFoundException, IOException {
        File[] files = new File(filePath).listFiles();
        String[][] results = new String[files.length][2];
        String line = "";
        for (int i = 0; i < files.length; i++) {
            StringBuffer sb = new StringBuffer();
            FileInputStream is = new FileInputStream(files[i].getCanonicalPath());
            @SuppressWarnings("resource")
            BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
            line = bReader.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = bReader.readLine();
            }
            String[] result = new String[2];
            result[0] = files[i].getName();
            result[1] = sb.toString();
            System.out.println("文件内容长度：" + sb.length());
            results[i] = result;
        }
        return results;

    }

    /**
     * 获取指定目录下的文件的个数
     *
     * @param filePath
     * @return
     */
    public static int getFileNums(String filePath) {
        int total = 0;
        File file = new File(filePath);
        if (file.isDirectory()) {
            // 如果是文件夹
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    // 如果是文件夹 递归
                    total = total + getFileNums(files[i].getPath());
                } else {
                    // System.out.println("文件夹"+file.getPath()+"下的文件"+files[i].getName());
                    // System.out.println(files[i].getName());
                    total++;
                }
            }
        } else {
            // 如果是文件
            // System.out.println(file.getName());
            total++;
        }
        return total;
    }

    /**
     * 获取指定目录下的文件的个数
     *
     * @param filePath
     * @return
     */
    public static List<File> getAllFiles(String filePath) {
        List<File> result = new ArrayList<File>();
        List<File> resulttemp = new ArrayList<File>();

        File file = new File(filePath);
        if (file.isDirectory()) {
            // 如果是文件夹
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    // 如果是文件夹 递归
                    resulttemp = getAllFiles(files[i].getPath());
                    for (File file2 : resulttemp) {
                        result.add(file2);
                    }
                } else {
                    result.add(files[i]);
                }
            }
        } else {
            result.add(file);
        }
        return result;
    }

    /**
     * 获取单个文件内容
     *
     * @param filePath
     * @return
     * @throws IOException
     * @author redlwb add 2011-9-11
     */
    public static String getFileContent(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileInputStream is = new FileInputStream(filePath);
        @SuppressWarnings("resource")
        BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
        String line = bReader.readLine();
        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = bReader.readLine();
        }
        return sb.toString();
    }

    /**
     * 压缩指定目录下所有文件（不包含下级目录），生成的压缩文件名为上级目录的文件夹名称
     *
     * @param filePath
     * @return
     * @throws IOException
     * @author zhangzhiyi
     */
    public static File compressFile2Zip(String filePath) throws IOException {
        String fileName = filePath.substring(filePath.lastIndexOf("/"), filePath.length()) + ".zip";
        /**
         * 创建一个临时压缩文件， 我们会把文件流全部注入到这个文件中 这里的文件你可以自定义是.rar还是.zip
         */
        File file = new File(filePath + fileName);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
        }
        File[] files = new File(filePath).listFiles();

        // 创建文件输出流
        FileOutputStream fous = new FileOutputStream(file);

        ZipOutputStream zipOut = new ZipOutputStream(fous);

        for (File f : files) {
            zipFile(f, zipOut);
        }

        zipOut.close();
        fous.close();
        return file;
    }

    /**
     * 压缩指定文件
     *
     * @param filePath 生成的文件全路径
     * @param files    需要压缩的文件
     */
    public static void zipFile(String filePath, List<File> files) {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(filePath));
            for (File file : files) {
                FileInputStream fis = null;
                try {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zipOutputStream.putNextEntry(entry);
                    fis = new FileInputStream(file);
                    int nNumber;
                    byte[] buffer = new byte[1024];
                    while ((nNumber = fis.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, nNumber);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null)
                        fis.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipOutputStream != null)
                    zipOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     *
     * @param inputFile
     * @param org       .apache.tools.zip.ZipOutputStream
     */
    private static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        FileInputStream in = null;
        BufferedInputStream bins = null;
        try {
            if (inputFile.exists() && inputFile.isFile()) {
                in = new FileInputStream(inputFile);
                bins = new BufferedInputStream(in, 512);
                // org.apache.tools.zip.ZipEntry
                ZipEntry entry = new ZipEntry(inputFile.getName());
                ouputStream.putNextEntry(entry);
                // 向压缩文件中输出数据
                int nNumber;
                byte[] buffer = new byte[512];
                while ((nNumber = bins.read(buffer)) != -1) {
                    ouputStream.write(buffer, 0, nNumber);
                }
                // 关闭创建的流对象

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bins.close();
                in.close();
            } catch (IOException e) {
                bins = null;
                in = null;
            }
        }
    }

    /**
     * 文件拷贝
     *
     * @param src
     * @param dst
     */
    public static void copy(File src, File dst, int bufferSize) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src), bufferSize);
                out = new BufferedOutputStream(new FileOutputStream(dst), bufferSize);
                byte[] buffer = new byte[bufferSize];
                while (in.read(buffer) > 0) {
                    out.write(buffer);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory(File folder) {
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                files[i].delete();
            } // 删除子目录
            else {
                deleteDirectory(files[i]);
            }
        }
        folder.delete();
    }


    /**
     * 批量拷贝指定目录下的文件到指定目录
     *
     * @param filesStr
     * @param srcDir
     * @param dstDir
     */
    public static void copyFiles(String filesStr, String srcDir, String dstDir) throws Exception {
        try {
            String[] arr = filesStr.split(",");
            File dstFileDir = new File(dstDir);
            for (int i = 0; i < arr.length; i++) {
                String fileName = arr[i];
                if (!dstFileDir.exists()) {
                    dstFileDir.mkdirs();
                }
                copyFile(srcDir + fileName, dstDir + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void copyFile(String oldPath, String newPath) {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                is = new FileInputStream(oldPath); // 读入原文件
                os = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = is.read(buffer)) != -1) {
                    os.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
            if (os != null) {
                IOUtils.closeQuietly(os);
            }
        }
    }


    /**
     * 根据文件名后缀判断是否复合文件类型要求
     *
     * @param ext
     * @return
     */
    public static boolean checkFileType(String ext, String fileTypeArr) {
        return Arrays.asList(fileTypeArr.split(",")).contains(ext);
    }

    /**
     * 根据文件名后缀判断是否复合图片类型要求
     *
     * @param ext
     * @return
     */
    public static boolean isImgFile(String ext) {
        return checkFileType(ext, "jpg,bmp,gif,png,jpeg");
    }


    public static void  mkDir(String resourcePath,String relativePath) {
        File file = new File(resourcePath+relativePath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

}
