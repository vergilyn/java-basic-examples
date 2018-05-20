package com.vergilyn.examples.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @Date 2017/5/3
 */
public class ZipFileUtil {
    public static final String ENCODING_GBK = "GBK";
    public static final String ENCODING_UTF8 = "UTF-8";

    /**
     * 输出zip到response.getOutputStream()输出流，供网页下载
     *
     * @param response
     * @param files     要打包的文件
     * @param fileNames 打包到zip中的名字；key:原文件名字,File.getName() ; value: zip中的文件名。
     * @param zipName   zip压缩包的名字,下载文件名。
     * @throws IOException
     */
    public static void fileToZipDownload(HttpServletResponse response, List<File> files, Map<String, String> fileNames, String zipName) throws IOException {
//		response.reset();
        zipName = StringUtils.containsIgnoreCase(zipName, ".zip") ? zipName : zipName + ".zip";
        response.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
        response.setContentType("application/octet-stream; charset=UTF-8");
        Double length = packageZip(response.getOutputStream(), files, fileNames);
        response.addHeader("Content-Length", "" + length);
    }

    /**
     * 奇怪的问题: 虽然最后有ZipOutputStream.close()。但偶尔会出现360无法解压，反而winRAR可以。
     * (如果最后不close, winRAR无法解压, 但360可以。)
     * @param os
     * @param files     要打包的文件
     * @param fileNames 打包到zip中的名字；key:原文件名字,File.getName() ; value: zip中的文件名。
     * @throws IOException
     */
    public static Double packageZip(OutputStream os, List<File> files,Map<String,String> fileNames) throws IOException {
        if (files == null) throw new FileNotFoundException("打包到zip的文件为null！");
        Map<String, Integer> zipEntryName = new HashMap<String, Integer>(); // ZIP中的文件名
        Double length = 0D;
        byte[] buffer = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(os);
        for (File file : files) {
            length += file.length();
            FileInputStream fis = new FileInputStream(file);
            String fileInZipName = getZipEntryName(zipEntryName, fileNames.get(file.getName()));
            ZipEntry zipEntry = new ZipEntry(fileInZipName);
            out.putNextEntry(zipEntry);
            int dataLen;
            //读入需要下载的文件的内容，打包到zip文件
            //	IOUtils.write(IOUtils.toByteArray(fis), out);
            while ((dataLen = fis.read(buffer)) > 0) {
                out.write(buffer, 0, dataLen);
            }
            out.closeEntry();
            fis.close();
        }
        //必须关闭 ZipOutputStream，否则winRAR无法解压缩（别的解压缩可能可以，比如360压缩）
//      IOUtils.closeQuietly(out);
        out.close();
        return length;
    }
    /**
     * 解决文件重名
     *
     * @param zipEntryName
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getZipEntryName(Map<String, Integer> zipEntryName, String fileName) throws UnsupportedEncodingException {
        String fileInZipName;
        Integer index = 0;
        if (zipEntryName.containsKey(fileName)) {
            index = zipEntryName.get(fileName) + 1;
            fileInZipName = "(" + index + ")" + fileName;
        } else {
            fileInZipName = fileName;
        }
        zipEntryName.put(fileName, index);
        return fileInZipName;
    }
}