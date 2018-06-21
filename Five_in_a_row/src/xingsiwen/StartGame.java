package xingsiwen;

import javax.swing.JFrame;

public class StartGame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mrf =  new MainFrame(); //启动主窗体
		mrf.setVisible(true);//设置窗体可见
		mrf.setLocationRelativeTo(null); //设置窗体位置
		mrf.setResizable(false);//不可改变大小
		mrf.setSize(900, 1000);//大小 
		mrf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //点击关闭按钮退出程序 
	}
}
