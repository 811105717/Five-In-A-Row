package xingsiwen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MainFrame extends JFrame {
	QiPan qp;
	int qpSize=9;
	JComboBox jcomb; //下拉菜单
	final String[] size = new String[] {"9*9","13*13","19*19"};//下拉菜单选择项数组
	JButton jbt_start,jbt_restart;
	Container cp;
	public MainFrame() {
		super("五子棋 BY：stranger");
		drawWindow();
		initListener();
	}
	private void drawWindow() {
		jcomb = new JComboBox<String>(size);
		jbt_start = new JButton("开始");
		jbt_restart = new JButton("复盘");
		jbt_restart.setEnabled(false);
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		Container menu = new Container();
		menu.setLayout(new FlowLayout());
		menu.add(jcomb);menu.add(jbt_start);menu.add(jbt_restart);
		cp.add(menu,BorderLayout.NORTH);
	}
	private void initListener() {
		jbt_restart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				int x = jcomb.getSelectedIndex();
				switch(x) {
				case 0 : qpSize = 9;break;
				case 1 : qpSize = 13;break;
				case 2 : qpSize = 19;break;
				}
				if(qp!=null)cp.remove(qp); //如果之前已经放进去一个棋盘 则必须删掉 //这句必须在下一句上面
				qp= new QiPan(qpSize,jbt_start,jbt_restart);
				cp.add(qp, BorderLayout.CENTER);
				jbt_restart.setEnabled(true);
				jbt_start.setEnabled(false);
				validate(); //重绘界面 
			}
		});
		jbt_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				int x = jcomb.getSelectedIndex();
				switch(x) {
				case 0 : qpSize = 9;break;
				case 1 : qpSize = 13;break;
				case 2 : qpSize = 19;break;
				}
				if(qp!=null)cp.remove(qp); //如果之前已经放进去一个棋盘 则必须删掉 //这句必须在下一句上面
				qp = new QiPan(qpSize,jbt_start,jbt_restart);
				cp.add(qp, BorderLayout.CENTER);
				jbt_restart.setEnabled(true);
				jbt_start.setEnabled(false);
				validate(); //重绘界面 
			}
		});
	}
	
}
