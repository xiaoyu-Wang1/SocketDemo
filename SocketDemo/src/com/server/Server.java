package com.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器处理多线程问题
 * 
 * 1.因为服务器是要很多人访问的，因此里面一定要用多线程来处理 
 * 2.拿这个文件上传的例子来说，它将每个连接它的用户封装到线程里面去，把用户要执行的操作定义到 run 方法里面
 * 一个用户拿一个线程，拿到线程的就自己去执行，如果有其它用户来的时候，再给新来的用户分配一个新的线程 这样就完成了服务器处理多线程的问题
 * 3.在服务器与客户端互传数据时，我们要特别注意的是，防止两个程序造成 死等的状态，一般原因有以下：
 * 1. 客户端向数据端发送数据时，当发送的是字符时，依次以一行来发送，而服务端在读取的时候，也是以行来读取，readLine()
 * 而发送的时候往往只是发送换行以行的内容，而不能发换行也发送过去， 那么服务端在读取的时候就不读取不到换行 ，那么 readLine() 就不会停止 2.
 * 客户端发送数据时,如果处理的是用 字符流 或是缓冲流的话，一定要记得刷新流，不然的话，数据就会发不出来 3 在用IO
 * 读取文件里面的数据然后发送到服务端时，当家读取文件 while(in.read()) 读取文件结束时，而在 服务端的接收程序
 * while(sin.read())不会接到一个发送完毕的提示，所以会一直等待下去，所以我们在处理这
 * 个问题的时候，还要将其发送一个文件读取结束的标志，告诉接收端文件已经读取完结，不要再等待了 
 * 而socket 里面给我们封装了 shutdownInput shutdownOutput 两个操作，此可以关闭流，同样也可以起到告诉 接收方文件传送完毕的效果
 */
public class Server {

	private static Logger logger = LoggerFactory.getLogger(Server.class);
	private static final int PORT = 2222;

	public static void main(String args[]) throws Exception {

		ServerSocket server = new ServerSocket(PORT);

		logger.info("服务器启动。。。端口号为：{}", PORT);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 300, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.CallerRunsPolicy());

		while (true) {
			Socket client = server.accept();
			executor.execute(new UploadThread(client));
			// new Thread(new UploadThread(client)).start();
		}

	}
}