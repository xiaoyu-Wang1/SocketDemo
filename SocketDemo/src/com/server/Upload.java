package com.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Upload {

	public static void main(String args[]) throws Exception {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					System.out.println("--------开始上传文件-------");
					loadMethod();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 500);
	}

	public static void loadMethod() throws Exception {

		String name = "E:\\test.jpg";
		Socket client = new Socket("127.0.0.1", 2222);
		File file = new File(name);

		String imgSuffix = ImgUtils.getImgSuffix(name);
//		String imgSuffix = name.substring(name.indexOf("."));
		System.out.println(imgSuffix);

		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file)); //文件读取流
		PrintStream sout = new PrintStream(client.getOutputStream(), true); //得到socket流
		
		//写入图片后缀名
		byte[] b = new byte[1024];
		byte[] bb = (imgSuffix + "\n").getBytes();
		for (int j = 0; j < bb.length; j++) {
			b[j] = bb[j];
		}
		sout.write(b, 0, b.length);

		int len = 0;
		//写入图片文件
		while ((len = fin.read(b)) != -1) {
			sout.write(b, 0, len);
			System.out.println(len + "...发送中");
		}
		client.shutdownOutput();
		sout.close();
		fin.close();
	}
}
