package com.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.Socket;


/**
 * 服务端接受并保存文件操作封装在线程
 * @author xiaoyu
 *
 */
public class UploadThread implements Runnable	
{
    private Socket client;

    public UploadThread(Socket s) {
        this.client = s;
    }

    public void run() {
//        String ip = client.getInetAddress().getHostAddress();	//得到IP地址
        try {
            BufferedInputStream sin = new BufferedInputStream(client.getInputStream()); // Socket读取流
            byte b[] = new byte[1024];
            int l = sin.read(b);
            String line = new String(b, 0, l).split("\n")[0];
            System.out.println("--------文件后缀为------" + line);
            
//            File file = new File("E:\\loadServer\\" + ImgUtils.getImgName() + line);
            File file = new File("E:\\loadServer\\" + Thread.currentThread().getName() + line);
            BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(file)); // 文件输出流
            
            int len = 0;
            //开始从网络中读取数据
            while (true) {
                len = sin.read(b);
                if(len != -1){
                    fout.write(b, 0, len);
                }else{
                    break;
                }
//                System.out.println("--------------"+new String(buf,0,len));
            }
            PrintStream sout = new PrintStream(client.getOutputStream());
            sout.write("发送成功".getBytes());
            // sout.flush(); //��Ȼ���ֽ����������õ���BufferedOutputStream
            fout.close();
            sin.close();
            sout.close();
        } catch (Exception ex) {
            System.out.println();
            ex.printStackTrace();

        }

    }
}