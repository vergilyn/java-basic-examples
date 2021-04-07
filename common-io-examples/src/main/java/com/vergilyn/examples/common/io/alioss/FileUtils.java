package com.vergilyn.examples.common.io.alioss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Ali-OSS `OSSDownloadOperation`
 *
 * @author vergilyn
 * @since 2021-03-29
 */
public class FileUtils {

	public static boolean deleteFile(String filePath) {
		return deleteFile(new File(filePath));
	}

	/**
	 * see: com.aliyun.oss.internal.OSSDownloadOperation#remove
	 */
	public static boolean deleteFile(File file){
		if (file == null){
			return true;
		}

		boolean flag = false;
		if (file.isFile() && file.exists()) {
			flag = file.delete();
		}

		return flag;
	}

	/**
	 * see: com.aliyun.oss.internal.OSSDownloadOperation#moveFile
	 */
	public static void moveFile(final File srcFile, final File destFile) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' is a directory");
		}
		if (destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' is a directory");
		}
		if (destFile.exists()) {
			if (!destFile.delete()) {
				throw new IOException("Failed to delete original file '" + srcFile + "'");
			}
		}

		final boolean rename = srcFile.renameTo(destFile);
		if (!rename) {
			copyFile(srcFile, destFile);
			if (!srcFile.delete()) {
				throw new IOException(
						"Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
			}
		}
	}

	/**
	 * see: com.aliyun.oss.internal.OSSDownloadOperation#moveFile
	 */
	public static void copyFile(File source, File dest) throws IOException {
		try(InputStream is = new FileInputStream(source);
				OutputStream os = new FileOutputStream(dest)) {

			byte[] buffer = new byte[4096];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		}
	}

	/**
	 * 创建固定大小的文件。
	 * see: com.aliyun.oss.internal.OSSDownloadOperation#createFixedFile
	 * @throws IOException
	 */
	public static void createFixedFile(String filePath, long length) throws IOException {
		File file = new File(filePath);

		// 如果该文件还不存在，那么将尝试创建它。
		try(RandomAccessFile rf = new RandomAccessFile(file, "rw")) {
			rf.setLength(length);  // 会截取或扩展文件
		}
	}

	/**
	 * see: com.aliyun.oss.internal.OSSDownloadOperation#renameTo
	 */
	public static void renameTo(String srcFilePath, String destFilePath) throws IOException {
		File srcfile = new File(srcFilePath);
		File destfile = new File(destFilePath);
		moveFile(srcfile, destfile);
	}
}
