package com.tcn.funcommon.fileoperation;

import android.util.Log;

import com.tcn.funcommon.media.Utils;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class FileOperation {
	private static final String TAG = "FileOperation";
	private static FileOperation s_m = null;
	public static synchronized FileOperation instance() {
		if (s_m == null)
		{
			s_m = new FileOperation();
		}
		return s_m;
	}

	public String getExternalStorageDirectory() {
		return Utils.getExternalStorageDirectory();
	}

	public boolean isFileExist(String folderPath,String fileNmae) {
		boolean bExist = false;
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			if (!folderPath.startsWith(mStrRootPath)) {
				folderPath = mStrRootPath + "//" + folderPath;
			}
			// 获取根目录
			File folder = new File(folderPath);
			if (!folder.exists()) {
				return bExist;
			}
			// 获取根目录下所有文件
			File[] files = folder.listFiles();
			// 循环添加到本地列表
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isHidden() || file.getName().equals("LOST.DIR")) {
					continue;
				}
				if ((file.getName()).equals(fileNmae)) {
					if (file.isFile()) {
						bExist = true;
						break;
					}
				};
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bExist;
	}

	public long getFileLastModifiedTime(String filePathName) {
		long time = -1;
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!filePathName.startsWith(mStrRootPath)) {
			filePathName = mStrRootPath + "//" + filePathName;
		}
		File f = new File(filePathName);
		//Date time=new Date(f.lastModified());//两种方法都可以
		if(!f.exists()) {	//喜欢的话可以判断一下。。。
			return time;
		}
		time = f.lastModified();
		return time;
	}

	public String getFileName(String path,String fileNameStart, String fileNameEnd)
	{
		String retFileName = null;
		if (null == path) {
			return retFileName;
		}
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			if (!path.startsWith(mStrRootPath)) {
				path = mStrRootPath+ "/"+ path;
			}
			File file = new File(path);
			if(!file.exists()) {
				return retFileName;
			}
			File[] files = file.listFiles();

			for(int i=0 ;i < files.length ;i++)
			{
				if(files[i].isFile()) {
					String fileName = files[i].getName();
					if ((fileName.startsWith(fileNameStart)) || (fileName.startsWith(fileNameStart.toLowerCase()))
							|| (fileName.startsWith(fileNameStart.toUpperCase()))) {

						if ((fileName.endsWith(fileNameEnd)) || (fileName.endsWith(fileNameEnd.toLowerCase()))
								|| (fileName.endsWith(fileNameEnd.toUpperCase()))) {
							retFileName = fileName;
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retFileName;
	}

	public static List<String> getDirFilePathAndName(String path)
	{
		List<String> fileList = new ArrayList<String>();
		if (null == path) {
			return fileList;
		}
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			if (!path.startsWith(mStrRootPath)) {
				path = mStrRootPath+ "/"+ path;
			}
			File file = new File(path);
			if(!file.exists()) {
				return fileList;
			}
			File[] files = file.listFiles();

			for(int i=0 ;i < files.length ;i++)
			{
				if(files[i].isFile()) {
					String filePath = files[i].getAbsolutePath();
					fileList.add(filePath);
				} else if(files[i].isDirectory()) {
					path = files[i].getAbsolutePath();
					List<String> mList = getDirFilePathAndName(path);
					for (String tmpPath:mList) {
						fileList.add(tmpPath);
					}
				} else {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileList;
	}

	public List<String> getDirFileName(String filePath) {
		List<String> localFile = new ArrayList<String>();
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			if (!filePath.startsWith(mStrRootPath)) {
				filePath = mStrRootPath+ "//"+ filePath;
			}
			// 获取根目录
			File f = new File(filePath);
			if (!f.exists()) {
				return localFile;
			}
			// 获取根目录下所有文件
			File[] files = f.listFiles();
			// 循环添加到本地列表
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isHidden() || file.getName().equals("LOST.DIR")) {
					continue;
				}
				localFile.add(file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localFile;
	}

	/**
	 * 加载本地列表.
	 *
	 * @param filePath
	 *            文件目录
	 */
	public List<File> getDirFile(String filePath) {
		List<File> localFile = new ArrayList<File>();
		try {
			// 获取根目录
			File f = new File(filePath);
			// 获取根目录下所有文件
			File[] files = f.listFiles();
			// 循环添加到本地列表
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isHidden() || file.getName().equals("LOST.DIR")) {
					continue;
				}
				localFile.add(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localFile;
	}

	public File createFile(String filePath, String fileName) {
		String mDirPath = filePath;
		try {
			File mDir = new File(mDirPath.trim());
			if (!mDir.exists()) {
				mDir.mkdir();
			}
			String mFilePath = mDirPath  + "/"+ fileName;
			File mFile = new File(mFilePath.trim());
			if (!mFile.exists()) {
				mFile.createNewFile();
			}
			return mFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("file","createFile e: "+e);
		}

		return null;
	}

	public boolean deleteFile(String dirPath, String fileName) {
		boolean bRet = false;
		if ((null == dirPath) || (null == fileName)) {
			return bRet;
		}
		String filePath = dirPath;
		if (filePath.endsWith("/")) {
			filePath = filePath + fileName;
		} else {
			filePath = filePath +"/"+ fileName;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!filePath.startsWith(mStrRootPath)) {
			filePath = mStrRootPath + "/" + filePath;
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return bRet;
		}
		return file.delete();
	}

	public boolean deleteFile(String fileName) {
		if (null == fileName) {
			return false;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!fileName.startsWith(mStrRootPath)) {
			fileName = mStrRootPath + "/" + fileName;
		}
		File file = new File(fileName);
		if (file == null || !file.exists()) {
			return false;
		}
		return file.delete();
	}

	public void deleteFileNotContain(String dirPath,List<String> fileListNew) {
		if (null == fileListNew) {
			return;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!dirPath.startsWith(mStrRootPath)) {
			dirPath = mStrRootPath + "/" + dirPath;
		}
		List<String> fileListOrig = getDirFilePathAndName(dirPath);
		for (String localPathName:fileListOrig) {
			boolean bContain = false;
			for (String newPathName:fileListNew) {
				if(localPathName.endsWith(newPathName)) {
					bContain = true;
					break;
				}
			}

			if (!bContain) {
				deleteFile(localPathName);
			}
		}
	}

	/**
	 * 删除文件夹下所有文件
	 *
	 *  要删除的根目录
	 */
	public void deleteAllFile(String dir,String exceptFileName) {
		if ((null == dir) || (null == exceptFileName)) {
			return;
		}
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			if (!dir.startsWith(mStrRootPath)) {
				dir = mStrRootPath + "/" + dir;
			}
			File file = new File(dir);
			if (!file.exists()) {
				return;
			} else {
				if (file.isFile()) {
					return;
				}
				if (file.isDirectory()) {
					File[] childFile = file.listFiles();
					if ((childFile == null) || (childFile.length == 0)) {
						return;
					}
					for (File f : childFile) {
						if (f.isDirectory()) {
							f.delete();
						} else {
							if (!(f.getName()).equals(exceptFileName)) {
								f.delete();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	public void deleteAllFile(String dir) {
		if (null == dir) {
			return;
		}
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			if (!dir.startsWith(mStrRootPath)) {
				dir = mStrRootPath + "/" + dir;
			}
			File file = new File(dir);
			deleteDirAndFile(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	private boolean deleteDirAndFile(File dir) {
		if ((null == dir) || (!dir.exists())) {
			return false;
		}
		if (dir.isDirectory()) {
			String[] children = dir.list();
			//递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirAndFile(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	public boolean isFileExit(String fileName) {
		boolean bRet = false;

		if ((fileName == null) || (fileName.length() < 1)) {
			return bRet;
		}
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			if (!fileName.startsWith(mStrRootPath)) {
				fileName = mStrRootPath +"/"+ fileName;
			}
			File file = new File(fileName);
			if (file == null || !file.exists() || file.isDirectory()) {
				return bRet;
			}
			bRet = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bRet;
	}

	public String getAvailablePath(String path) {
		String strAvailablePath = path;
		if ((path == null) || (path.length() < 1)) {
			return strAvailablePath;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!strAvailablePath.startsWith(mStrRootPath)) {
			strAvailablePath = mStrRootPath+"/"+path;
		}

		return strAvailablePath;
	}

	public boolean createFoldersAndExist(String filePath) {
		boolean bRet = false;

		if ((filePath == null) || (filePath.length() < 1)) {
			return bRet;
		}
		try {
			String mStrRootPath = Utils.getExternalStorageDirectory();
			String tmpPath = filePath;
			if (!filePath.startsWith(mStrRootPath)) {
				tmpPath = mStrRootPath+"/"+filePath;
			}

			File dir = new File(tmpPath);

			dir.mkdirs();

			if((dir.exists()) && (dir.isDirectory())) {
				bRet = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bRet;
	}

	public boolean createFolder(String filePath) {
		boolean bRet = false;

		if ((filePath == null) || (filePath.length() < 1)) {
			return bRet;
		}
		try {
			String strFile = "";
			String mStrRootPath = Utils.getExternalStorageDirectory();
			String tmpPath = filePath;
			if (!filePath.startsWith(mStrRootPath)) {
				tmpPath = mStrRootPath+"/"+filePath;
			}
			strFile = tmpPath.substring(mStrRootPath.length());
			File dir = new File(tmpPath);
			if ((dir.exists()) && (dir.isDirectory())) {
				bRet = true;
				return bRet;
			}

			int indexX = strFile.indexOf("/");
			int iLength = strFile.length();
			do {
				if (indexX < 0) {
					File file = new File(mStrRootPath + "/"+strFile);
					if ((!file.exists()) || (!file.isDirectory())) {
						file.mkdir();
					}
					bRet = true;
					break;
				} else {
					String mStr = strFile.substring(0,indexX);
					if (iLength > (indexX+1)) {
						strFile = strFile.substring(indexX+1);
					} else {
						bRet = true;
						break;
					}
					if ((mStr != null) && (mStr.trim().length() > 0)) {
						mStrRootPath = mStrRootPath+"/"+mStr.trim();
						File mDir = new File(mStrRootPath);
						if ((!mDir.exists()) || (!mDir.isDirectory())) {
							mDir.mkdir();
						}
					}
					indexX = strFile.indexOf("/");
					iLength = strFile.length();
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bRet;
	}

	public boolean writeFileByLine(List<String> dataList,String filePath,String fileName) {
		boolean bRet = false;
		if ((null == dataList) || (dataList.isEmpty())) {
			return bRet;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mDirPath = filePath;
		if (!filePath.startsWith(mStrRootPath)) {
			mDirPath = mStrRootPath + "/" + filePath;
		}
		String mFilePath = mDirPath  + "/"+ fileName;
		createFile(mDirPath,fileName);
		StringBuffer sb = new StringBuffer();
		FileWriter fw = null;
		try {
			for (String data:dataList) {
				sb.append(data+"\n");
			}
			String strData = sb.toString();
			//createFile(mDirPath,fileName);
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			fw = new FileWriter(mFilePath, false);
			String[] strArry = strData.split("\\n");
			for (String data : strArry) {
				fw.write(data+"\n");
			}
			fw.close();

			bRet = true;
		} catch (Exception e) {
			Log.i("file", "deleteFileContent 2 e: "+e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bRet;
	}

	/**
	 * 写入内容到txt文本中
	 * str为内容
	 */
	public void writeFileByLine(String str,String filePath,String fileName) {
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mDirPath = filePath;
		if (!filePath.startsWith(mStrRootPath)) {
			mDirPath = mStrRootPath + "//" + filePath;
		}
		String mFilePath = mDirPath  + "//"+ fileName;
		createFile(mDirPath,fileName);
		str = str+"\n";
		FileWriter fw = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			fw = new FileWriter(mFilePath, true);
			fw.write(str);
			fw.close();
		} catch (Exception e) {
			Log.i("file", "writeFileByLine filePath: "+filePath+" fileName: "+fileName+" e: "+e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 写入内容到txt文本中
	 * str为内容
	 */
	public void writeFileByLine(boolean append,String str,String filePath,String fileName) {
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mDirPath = filePath;
		if (!filePath.startsWith(mStrRootPath)) {
			mDirPath = mStrRootPath + "/" + filePath;
		}
		String mFilePath = mDirPath  + "/"+ fileName;
		createFile(mDirPath,fileName);
		str = str+"\n";
		FileWriter fw = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			fw = new FileWriter(mFilePath, append);
			fw.write(str);
			fw.close();
		} catch (Exception e) {
			Log.i("file", "writeFileByLine filePath: "+filePath+" fileName: "+fileName+" e: "+e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isFileContentData(String data,String filePathAndName) {
		boolean bRet = false;
		if ((null == data) || (data.isEmpty())) {
			return bRet;
		}

		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "/" + filePathAndName;
		}
		BufferedReader br = null;
		try {
			File mFile = new File(mFilePathAndName.trim());
			if ((!mFile.exists()) || (!mFile.isFile())) {
				return bRet;
			}
			br = new BufferedReader(new FileReader(mFile));
			String line = "";
			while((line = br.readLine()) != null){
				if (line.contains(data)) {
					bRet = true;
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("file","deleteFileContent 1 e: "+e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bRet;
	}

	public String getMessageContentData(String data,String filePathAndName) {
		String strRet = "";
		if ((null == data) || (data.isEmpty())) {
			return strRet;
		}

		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "//" + filePathAndName;
		}
		BufferedReader br = null;
		try {
			File mFile = new File(mFilePathAndName.trim());
			if ((!mFile.exists()) || (!mFile.isFile())) {
				return strRet;
			}
			br = new BufferedReader(new FileReader(mFile));
			String line = "";
			while((line = br.readLine()) != null){
				if (line.contains(data)) {
					strRet = line;
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("file","deleteFileContent 1 e: "+e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return strRet;
	}

	public boolean deleteFileContentByLine(String deleteLineData,String filePathAndName) {
		boolean bRet = false;
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "//" + filePathAndName;
		}
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		boolean bReadSuccess = false;
		try {
			File mFile = new File(mFilePathAndName.trim());
			if ((!mFile.exists()) || (!mFile.isFile())) {
				return bRet;
			}
			br = new BufferedReader(new FileReader(mFile));
			String line = "";
			while((line = br.readLine()) != null){
				if (!line.equals(deleteLineData)) {
					sb.append(line+"\n");
				}
			}
			bReadSuccess = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("file","deleteFileContent 1 e: "+e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (!bReadSuccess) {
			return bRet;
		}

		FileWriter fw = null;
		try {
			String strData = sb.toString();
			//createFile(mDirPath,fileName);
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			fw = new FileWriter(mFilePathAndName, false);
			String[] strArry = strData.split("\\n");
			for (String data : strArry) {
				fw.write(data+"\n");
			}
			fw.close();

			bRet = true;
		} catch (Exception e) {
			Log.i("file", "deleteFileContent 2 e: "+e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bRet;
	}

	public boolean deleteFileContent(String deleteContainData,String filePathAndName) {
		boolean bRet = false;
		if ((null == deleteContainData) || (deleteContainData.isEmpty())) {
			return bRet;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "//" + filePathAndName;
		}
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		boolean bReadSuccess = false;
		try {
			File mFile = new File(mFilePathAndName.trim());
			if ((!mFile.exists()) || (!mFile.isFile())) {
				return bRet;
			}
			br = new BufferedReader(new FileReader(mFile));
			String line = "";
			while((line = br.readLine()) != null){
				if (!line.contains(deleteContainData)) {
					sb.append(line+"\n");
				}
			}
			bReadSuccess = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("file","deleteFileContent 1 e: "+e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (!bReadSuccess) {
			return bRet;
		}

		FileWriter fw = null;
		try {
			String strData = sb.toString();
			//createFile(mDirPath,fileName);
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			fw = new FileWriter(mFilePathAndName, false);
			String[] strArry = strData.split("\\n");
			for (String data : strArry) {
				fw.write(data+"\n");
			}
			fw.close();

			bRet = true;
		} catch (Exception e) {
			Log.i("file", "deleteFileContent 2 e: "+e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bRet;
	}

	public boolean deleteFileContent(String filePathAndName) {
		boolean bRet = false;

		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "//" + filePathAndName;
		}

		FileWriter fw = null;
		try {
			//createFile(mDirPath,fileName);
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			fw = new FileWriter(mFilePathAndName, false);
			fw.write("");
			fw.close();
			bRet = true;
		} catch (Exception e) {
			Log.i("file", "deleteFileContent 2 e: "+e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bRet;
	}

	/**
	 * 读取第一行文本文件
	 *
	 * @param
	 * @return
	 */
	public String readFileFirstLine(String filePathAndName,String startData,String endData) {
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "//" + filePathAndName;
		}
		StringBuffer sb = new StringBuffer();
		File file = new File(mFilePathAndName);
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "readFile return.");
			return null;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			int iEndDataLength = endData.length();
			int iIndexStart = -1;
			int iIndexEnd = -1;
			while((line = br.readLine()) != null){
				iIndexStart = line.indexOf(startData);
				iIndexEnd = line.indexOf(endData);
				if ((iIndexStart > -1) && (iIndexEnd > -1) && (iIndexEnd > iIndexStart)) {
					line = line.substring(iIndexStart,iIndexEnd+iEndDataLength);
					sb.append(line);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 读取第一行文本文件
	 *
	 * @param
	 * @return
	 */
	public List<String> readFileLinesContain(String filePathAndName,String startData,String endData,String containData) {
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "//" + filePathAndName;
		}
		List<String> mRetList = new ArrayList<>();
		File file = new File(mFilePathAndName);
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "readFile return.");
			return mRetList;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			int iEndDataLength = endData.length();
			int iIndexStart = -1;
			int iIndexEnd = -1;
			while((line = br.readLine()) != null){
				iIndexStart = line.indexOf(startData);
				iIndexEnd = line.indexOf(endData);
				if ((iIndexStart > -1) && (iIndexEnd > -1) && (iIndexEnd > iIndexStart) && (line.contains(containData))) {
					line = line.substring(iIndexStart,iIndexEnd+iEndDataLength);
					mRetList.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mRetList;
	}

	/**
	 * 读取第一行文本文件
	 *
	 * @param
	 * @return
	 */
	public List<String> readFileLinesContain(String filePathAndName,String containData) {
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "/" + filePathAndName;
		}
		List<String> mRetList = new ArrayList<>();
		File file = new File(mFilePathAndName);
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "readFile return.");
			return mRetList;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				if (line.contains(containData)) {
					mRetList.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mRetList;
	}

	public List<String> readFileLinesContain(File file) {

		List<String> mRetList = new ArrayList<>();
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "readFile return.");
			return mRetList;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				if (line.length() > 0) {
					mRetList.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mRetList;
	}

	/**
	 * 读取第一行文本文件
	 *
	 * @param
	 * @return
	 */
	public List<String> readFileLinesContainStart(String filePathAndName,String startData) {
		String mStrRootPath = Utils.getExternalStorageDirectory();
		String mFilePathAndName = filePathAndName;
		if (!filePathAndName.startsWith(mStrRootPath)) {
			mFilePathAndName = mStrRootPath + "//" + filePathAndName;
		}
		List<String> mRetList = new ArrayList<>();
		File file = new File(mFilePathAndName);
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "readFile return.");
			return mRetList;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				line = line.trim();
				if (line.startsWith(startData)) {
					mRetList.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mRetList;
	}

	/**
	 * 读取文本文件
	 *
	 * @param fileName
	 * @return
	 */
	public String readFile(boolean bLines, String filePath,String fileName) {
		if ((null == filePath) || (null == fileName)) {
			return null;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!filePath.startsWith(mStrRootPath)) {
			filePath = mStrRootPath + "/" + filePath;
		}
		String mfile = filePath + "/"+fileName;
		StringBuffer sb = new StringBuffer();
		File file = new File(mfile);
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "1 readFile return.");
			return null;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				if (bLines) {
					sb.append(line+"\n");
				} else {
					sb.append(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile 1 FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile 1 IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 读取文本文件
	 *
	 * @param fileName
	 * @return
	 */
	public String readFile(String filePath,String fileName) {
		if ((null == filePath) || (null == fileName)) {
			return null;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!filePath.startsWith(mStrRootPath)) {
			filePath = mStrRootPath + "/" + filePath;
		}
		String mfile = filePath + "/"+fileName;
		StringBuffer sb = new StringBuffer();
		File file = new File(mfile);
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "readFile return.");
			return null;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 读取文本文件
	 *
	 * @param filePathAndName
	 * @return
	 */
	public String readFileFirstLine(String cotainData, String filePathAndName) {
		if (null == filePathAndName) {
			return null;
		}
		String mStrRootPath = Utils.getExternalStorageDirectory();
		if (!filePathAndName.startsWith(mStrRootPath)) {
			filePathAndName = mStrRootPath + "/" + filePathAndName;
		}
		StringBuffer sb = new StringBuffer();
		File file = new File(filePathAndName);
		if (file == null || !file.exists() || file.isDirectory()) {
			return null;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				if (line.contains(cotainData)) {
					sb.append(line);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 读取文本文件
	 * @return
	 */
	public String readFileFirstLine(String cotainData, File file) {

		if (file == null || !file.exists() || file.isDirectory()) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				if (line.contains(cotainData)) {
					sb.append(line);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 读取文本文件
	 * @return
	 */
	public boolean readToFile(String selectData, String filePath,String sourceFileName, String targetFileName) {
		boolean bRet = false;
		String mfile = filePath + "/"+sourceFileName;
		StringBuffer sb = new StringBuffer();
		File file = new File(mfile);
		if (file == null || !file.exists() || file.isDirectory()) {
			Log.e("file", "readFile return.");
			return bRet;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				if (line.contains(selectData)) {
					sb.append(line+"\n");
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("file", "readFile FileNotFoundException e: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("file", "readFile IOException e: " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		FileWriter fw = null;
		try {
			String strData = sb.toString();
			String mFilePath = filePath  + "/"+ targetFileName;
			//createFile(mDirPath,fileName);
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			fw = new FileWriter(mFilePath, false);
			String[] strArry = strData.split("\\n");
			for (String data : strArry) {
				fw.write(data+"\n");
			}
			fw.close();

			bRet = true;
		} catch (Exception e) {
			Log.i("file", "readFile filePath: "+filePath+" e: "+e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bRet;
	}

	public String queryFilePathIgnoreCase(String fileName, List<String> pathList) {
		if (null == fileName || null == pathList) {
			Log.e(TAG, "-------queryFilePathIgnoreCase() null, return");
			return null;
		}
		String mPath = null;
		String mFileName = null;
		int iLastIndex = 0;
		int iIndexPoint = 0;
		for (String path: pathList) {
			iLastIndex = path.lastIndexOf("/");
			iIndexPoint = path.lastIndexOf(".");
			Log.d(TAG, "queryFilePathIgnoreCase() iLastIndex: "+iLastIndex+" iIndexPoint: "+iIndexPoint+" path: "+path);
			if (path.length() > iIndexPoint) {
				mFileName = path.substring(iLastIndex + 1, iIndexPoint);
				if (fileName.equalsIgnoreCase(mFileName)) {
					mPath = path;
					break;
				}
			}
		}

		return mPath;
	}

	public String queryFilePath(String fileName, List<String> pathList) {
		if (null == fileName || null == pathList) {
			Log.e(TAG, "-------queryFilePath() null, return");
			return null;
		}
		String mPath = null;
		String mFileName = null;
		int iLastIndex = 0;
		int iIndexPoint = 0;
		for (String path: pathList) {
			iLastIndex = path.lastIndexOf("/");
			iIndexPoint = path.lastIndexOf(".");
			if (path.length() > iIndexPoint) {
				mFileName = path.substring(iLastIndex + 1, iIndexPoint);
				if (fileName.equals(mFileName)) {
					mPath = path;
					break;
				}
			}
		}

		return mPath;
	}

	public boolean readAsFile(InputStream inSream, File file) {
		boolean bRet = false;
		if ((null == inSream) || (null == file)) {
			return bRet;
		}

		try {
			FileOutputStream outStream = new FileOutputStream(file);

			byte[] buffer = new byte[1024];

			int len = -1;

			while( (len = inSream.read(buffer)) != -1 ){

				outStream.write(buffer, 0, len);

			}

			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (inSream != null) {
				try {
					inSream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			bRet = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bRet;
	}

	public boolean readAsFile(InputStream inSream, String filePath) {
		boolean bRet = false;
		if ((null == inSream) || (null == filePath) || (filePath.length() < 1)) {
			return bRet;
		}

		try {
			filePath = getAvailablePath(filePath);
			File storeFile = new File(filePath + ".tmp"); // 先存为临时文件，等全部下完再改回原来的文件名
			//File file = new File(filePath);

			FileOutputStream outStream = new FileOutputStream(storeFile);

			byte[] buffer = new byte[1024];

			int len = -1;

			while( (len = inSream.read(buffer)) != -1 ){

				outStream.write(buffer, 0, len);

			}

			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (inSream != null) {
				try {
					inSream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			storeFile.renameTo(new File(filePath));

			bRet = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bRet;
	}

	/**
	 * 描述：复制源文件(可以是目录)到目的地址 函数名：copyFile
	 *
	 * @param：sourceFile 源文件
	 * @param：targetFile 目的地址
	 * @author song 2017-02-05
	 * */
	public boolean copyFile(String sourceFile, String targetFile) {
		boolean bRet = false;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			(new File(targetFile)).mkdirs(); //如果文件夹不存在 则建立新文件夹
			File mSource = new File(sourceFile);

			if (mSource.isHidden() || mSource.getName().equals("LOST.DIR")) {
				return bRet;
			}
			if (mSource.isFile()) {
				in = new FileInputStream(new File(sourceFile));
				out = new FileOutputStream(new File(targetFile+ "/" +(mSource.getName()).toString()));
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
				bRet = true;
			} else {
				String[] file = mSource.list();
				for (int i = 0; i < file.length; i++) {
					File mFile = new File(sourceFile+ File.separator + file[i]);
					if (mFile.isFile()) {
						in = new FileInputStream(mFile);
						out = new FileOutputStream(new File(targetFile+ "/" +file[i]));
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = in.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}
						out.flush();
						out.close();
						in.close();
					} else {
						if (mFile.isDirectory()) {
							copyFile(sourceFile+"/"+file[i], targetFile+ "/" +file[i]);
						}

					}
				}
				bRet = true;
			}


		} catch (Exception e) {
			e.printStackTrace();
			TcnVendIF.getInstance().LoggerDebug(TAG, "readXLSX copyFile e:"+e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					TcnVendIF.getInstance().LoggerDebug(TAG, "readXLSX copyFile e2:"+e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					TcnVendIF.getInstance().LoggerDebug(TAG, "readXLSX copyFile e3:"+e);
				}
			}
		}
		TcnVendIF.getInstance().LoggerDebug(TAG, "readXLSX copyFile bRet:"+bRet);
		return bRet;
	}

	/**
	 * 描述：复制源文件(可以是目录)到目的地址 函数名：copyFile
	 *
	 * @param：sourceFile 源文件
	 * @param：targetFile 目的地址
	 * @author song 2017-02-05
	 * */
	public boolean copyFile(String sourceFile, String targetFileFolder, String targetFileNmae) {
		boolean bRet = false;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {

			File targetFolder = new File(targetFileFolder);
			if (!targetFolder.exists() || !targetFolder.isDirectory()) { //如果文件夹不存在 则建立新文件夹
				targetFolder.mkdirs();
			}

			File mSource = new File(sourceFile);

			if (!mSource.isFile() || mSource.isHidden() || mSource.getName().equals("LOST.DIR")) {
				return bRet;
			}

			in = new FileInputStream(mSource);
			out = new FileOutputStream(new File(targetFileFolder+ "/" +targetFileNmae));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			bRet = true;

		} catch (Exception e) {
			e.printStackTrace();
			TcnVendIF.getInstance().LoggerDebug(TAG, "copyFile e:"+e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					TcnVendIF.getInstance().LoggerDebug(TAG, "copyFile e2:"+e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					TcnVendIF.getInstance().LoggerDebug(TAG, "copyFile e3:"+e);
				}
			}
		}
		return bRet;
	}
}
