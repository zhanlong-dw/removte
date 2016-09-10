package com.dw.remote;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 控制端：客户端
 * @author weixian
 */
public class Client {

	/**
	 * 1、接受服务器端发送的图片，通过网络接受的
	 * 2、监听事件，发送键盘事件和鼠标事件
	 * @throws Exception 
	 * @throws IOException 
	 */
	
	public static void main(String[] args) throws Exception, IOException{
		Socket socket = new Socket("127.0.0.1",1234);
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
		ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
		ClientWindow client = new ClientWindow(objectOut);
		byte[] image;
		while(true){
			image = new byte[dataIn.readInt()];
			dataIn.readFully(image);
			client.repaintImage(image);
		}
	}
}

//定义一个窗体
class  ClientWindow extends JFrame{

	private JLabel backGround;
	private ObjectOutputStream objectOut;
	
	public void repaintImage(byte[] image){
		backGround.setIcon( new ImageIcon(image));
		this.repaint();
	}
	
	public ClientWindow(ObjectOutputStream objectOut){
		this.objectOut = objectOut;
		this.setTitle("远程协助工具");
		this.setSize(1024, 768);
		backGround = new JLabel();
		JPanel jp = new JPanel();
		jp.add(backGround);
		JScrollPane scrollPanel = new JScrollPane(jp);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(scrollPanel);
		this.setVisible(true);
		
		//绑定键盘事件
		addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				sendEvent(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				sendEvent(e);
			}
		});
		
		//绑定鼠标事件
		backGround.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
		//绑定鼠标移动事件
		backGround.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				sendEvent(e);
			}
		});
	}
	
	public void sendEvent(InputEvent event){
		try {
			objectOut.writeObject(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}