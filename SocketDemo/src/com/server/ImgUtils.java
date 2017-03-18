package com.server;

public class ImgUtils {
	private String imgName;

	
	public static String getImgSuffix(String name) {
		String imgSuffix = name.substring(name.indexOf("."));
		return imgSuffix;
	}
}
