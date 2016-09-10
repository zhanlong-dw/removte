package com.dw;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ScreenTest {

	public static void main(String[] args) throws Exception {
		Robot robot = new Robot();
		//创建一个窗体
		JFrame jframe = new JFrame("本地截图");
		//设置窗体的尺寸
		jframe.setSize(1365, 722);
		//设置一个标签
		JLabel jlabel = new JLabel();
		jframe.add(jlabel);
		//显示这个窗体
		jframe.setVisible(true);
		//将窗口关闭，进程结束
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while(true){
			//使用robot获取本地截取的图片
			BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, 1365, 722));
			jlabel.setIcon(new ImageIcon(image));
			Thread.sleep(50);
		}
	}

}
