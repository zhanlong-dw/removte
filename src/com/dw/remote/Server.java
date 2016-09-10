package com.dw.remote;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.sun.image.codec.jpeg.*;

/**
 * 被控制端：服务器端
 * @author weixian
 */

public class Server {
	
	/**
	 * 1、将电脑桌面发送到客户端
	 * 2、监听事件，发送键盘事件和鼠标事件
	 * 3、使用Robot类进行鼠标和键盘的自动化操作
	 * @throws Exception 
	 * 
	 */
	public static void main(String[] args) throws Exception {
		//实例化服务端监听 在 1234端口
		ServerSocket sever = new ServerSocket(1234);
		System.out.println("服务器已经启动...");
		
		Socket socket = sever.accept();	//等待接受请求
		System.out.println("有客户已经连接");
		
		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
		ImageThread imageThread = new ImageThread(dataOut);
		imageThread.start();
		EventThread eventThread = new EventThread(new ObjectInputStream(socket.getInputStream())); 
		eventThread.start(); 
	}
}

/**
 * 处理接受到的事件
 * @author weixian
 *
 */
class EventThread extends Thread{
	private ObjectInput objectIn;
	private Robot robot;
	public EventThread(ObjectInputStream objectIn){
		this.objectIn = objectIn;
	}
	
	@Override
	public void run() {
		try {
			robot = new Robot();
			while(true){
				Object event = objectIn.readObject();
				InputEvent inEvent = (InputEvent)event;
				//处理事件
				actionEvent(inEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void actionEvent(InputEvent event){
		if(event instanceof KeyEvent){
			KeyEvent e = (KeyEvent)event;
			int type = e.getID(); //获取事件了类型
			if(type==Event.KEY_PRESS){
				robot.keyPress(e.getKeyCode());
			}else if(type==Event.KEY_RELEASE){
				robot.keyRelease(e.getKeyCode());
			}
		}else{
			MouseEvent e = (MouseEvent)event;
			int type = e.getID();
			if(type == Event.MOUSE_DOWN){
				robot.mousePress(getMouseButton(e.getButton()));
			}else if(type == Event.MOUSE_UP){
				robot.mouseRelease(getMouseButton(e.getButton())); 
			}else if(type == Event.MOUSE_MOVE){
				robot.mouseMove(e.getX(), e.getY());
			}else if(type == Event.MOUSE_DRAG){
				robot.mouseMove(e.getX(), e.getY());
			}
		}
	}
	private int getMouseButton(int button){
		if(button == MouseEvent.BUTTON1){
			return InputEvent.BUTTON1_MASK;
		}else if(button == MouseEvent.BUTTON2){
			return InputEvent.BUTTON2_MASK;
		}if(button == MouseEvent.BUTTON3){
			return InputEvent.BUTTON3_MASK;
		}else{
			return -1;
		}
	}
}

/*创建一个子线程，用来将本地图片发送到客户端去*/
class ImageThread extends Thread{
	private DataOutputStream dataOut;
	
	public ImageThread (DataOutputStream dataOut){
		this.dataOut = dataOut;
	}
	
	@Override
	public void run() {
		try {
			Robot robot = new Robot();
			//截取整个屏幕
			//Toolkit是一个工具包，它可以获取当前操作系统的一些信息
			Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle rect = new Rectangle(dm);
			BufferedImage img ;
			byte[] imageByte;
			while(true){
				img = robot.createScreenCapture(rect);
				//压缩处理以及转换成byte数组
				imageByte = getByteImage(img);
				//将图片的长度告诉客户端  客户端要用byte数组接受
				dataOut.writeInt(imageByte.length);
				//将图片传输到客户端
				dataOut.write(imageByte);
				Thread.sleep(50);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private byte[] getByteImage(BufferedImage image) throws ImageFormatException, IOException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bout);
		encoder.encode(image);
		return bout.toByteArray();
	}
}

