/*
 * Rasengan - Manga and Comic Downloader
 *    Copyright (C) 2013  Sauriel
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.sauriel.rasengan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CBZUtils {
	
	ArrayList<String> fileList;
	private String comicName;
    
    public CBZUtils(String comicName) {
    	this.comicName = comicName;
    	fileList = new ArrayList<String>();
    	generateFileList(new File("temp/" + comicName));
    }
	
	public boolean createCBZ() {
		byte[] buffer = new byte[1024];
		 
	     try{
	 
	    	FileOutputStream fos = new FileOutputStream(comicName + ".cbz");
	    	ZipOutputStream zos = new ZipOutputStream(fos);
	 
	    	for (String file : this.fileList) {

	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze);

	        	FileInputStream in = new FileInputStream("temp/" + comicName + File.separator + file);
	 
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	 
	        	in.close();
	    	}
	 
	    	zos.closeEntry();
	    	//remember close it
	    	zos.close();
	 
	    	return true;
	    } catch (IOException e) {
	    	return false;  
	    }
	}
	
	public void generateFileList(File node) {
		 
    	//add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}
	 
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
 
    }
	
	private String generateZipEntry(String file) {
    	return file.substring(new File("temp/" + comicName).getAbsolutePath().length()+1, file.length());
    }

}
