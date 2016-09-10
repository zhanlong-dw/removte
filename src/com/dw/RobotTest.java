package com.dw;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * 了解和学习Robot的作用和常见功能
 * @author weixian
 *
 */
public class RobotTest {
	/**
	 * java应用程序的主入口
	 * @param args
	 * @throws AWTException 
	 */
	public static void main(String[] args) throws AWTException{
		//实例化一个对象
		Robot robot = new Robot();
		
		/*robot.mouseMove(85, 750);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.mouseMove(282, 281);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(1000);*/
		//鼠标移动
		//robot.mouseMove(1140, 62);
		//鼠标按下
		//robot.mousePress(InputEvent.BUTTON1_MASK);
		//鼠标释放
		//robot.mouseRelease(InputEvent.BUTTON1_MASK);
		//键盘
		robot.keyPress((int)'A');
		robot.keyRelease((int)'A');
	}

}
