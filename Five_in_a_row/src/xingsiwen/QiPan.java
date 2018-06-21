package xingsiwen;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
public class QiPan extends JPanel implements MouseListener, Runnable ,Lock{
	int curr = 1; //当前的棋子   1代表黑棋  2 代表白旗
	JButton jbt_st,jbt_rs;  //开始和复盘按钮  
	static int[][] qp; //用来保存当前棋盘中的棋子  
	int x=-1000, y=-1000; // 棋子坐标  -1000代表当前不下棋子
	int curxp,curyp;   //当前刚刚下的棋子  
	static int qpsize;// 棋盘大小 
	static int asize;// 每个格大小
	public void paintConmponents(Graphics g) {
		super.paintComponents(g);
	}
	public QiPan(int size,JButton j,JButton jrs) {  //构造方法 
		this.jbt_st = j;
		this.jbt_rs = jrs;
		this.qpsize = size;
		qp = new int[qpsize][qpsize];  //初始化棋盘
		addMouseListener(this);   //给棋盘添加监听器 
		asize = 800 / qpsize; // 计算每一个格的宽度 
	}

	@Override
	public void paint(Graphics g) { // 画棋盘
		// TODO Auto-generated method stub
		super.paint(g);
		g.setColor(Color.BLACK);   //设置棋盘格为黑色  
		g.setFont(new Font("微软雅黑", Font.BOLD, 20)); // 字体属性
		for (int i = 0; i < qpsize; i++) {   //根据棋盘大小画线  
			g.drawLine(i * asize + 50, 50, i * asize + 50, asize * (qpsize - 1) + 50); // 画横线
			if (i == qpsize / 2)
				g.fillRect(i * asize + 45, i * asize + 45, 10, 10);// 画中点
			if (i == 2) {
				g.fillRect(i * asize + 45, i * asize + 45, 10, 10); // 左上
				g.fillRect((qpsize - 3) * asize + 45, i * asize + 45, 10, 10);// 右上
			}
			if (i == qpsize - 3) {
				g.fillRect(i * asize + 45, i * asize + 45, 10, 10);// 右下
				g.fillRect(2 * asize + 45, (qpsize - 3) * asize + 45, 10, 10);// 左下
			}
			g.drawLine(50, i * asize + 50, asize * (qpsize - 1) + 50, i * asize + 50); // 画竖线
		}
		//根据棋盘数组画当前棋的状态 
		for(int i=0;i<qpsize;i++){ 
			for(int j=0;j<qpsize;j++){
				if(qp[i][j]!=0){ //  定位到棋盘位置
					x = i*asize+45;
					y = j*asize+45;
//					System.out.println(x +"  "+ y);
					if(qp[i][j]==1) {  
						g.setColor(Color.black); 
//						g.fillRect(x, y, 10, 10); //画方块  黑色 
						g.fillOval(x-5, y-5, 20, 20);//画实心圆 蓝色
					}
					else {
						g.setColor(Color.white);
//						g.fillRect(x, y, 10, 10); //画方块  白色
						g.fillOval(x-5, y-5, 20, 20); //画实心圆  白色 
					}
				}
			}
		}
		g.setFont(new Font("宋体",0, 20));
		g.setColor(Color.BLUE);
		String curone = curr==1?"黑棋":"白棋";
		g.drawString("当前轮到"+curone, 200, 850);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {  //按下鼠标事件  
		// TODO Auto-generated method stub
		dingWei( e.getX(), e.getY());  //定位鼠标点击的位置  
		Graphics g = this.getGraphics();  //得到Graphics对象
		this.paint(g);  //调用paint进行界面绘制  
		if(checkWin(curxp,curyp)) {  //绘制完毕后检查是否有获胜方或平局 
			String winner = (curr==2)?"黑棋":"白棋";  //下完后已经交换，所以这里curr应该是反的才能代表上一次棋的颜色
			JOptionPane.showMessageDialog(this, winner+"赢了", "提示", JOptionPane.OK_OPTION);
			removeMouseListener(this); //移除鼠标监听器  防止赢了还能接着下 
			jbt_st.setText("重新开始"); //有一方胜出  应该是重新开始 
			jbt_st.setEnabled(true);  //开始按钮可用
			jbt_rs.setEnabled(false); //复盘按钮不可用  
		}
		else { //当棋盘下满还没有获胜者时  平局 
			boolean p = true;
			for(int i=0;i<qpsize;i++) {
				for(int j=0;j<qpsize;j++) {
					if(qp[i][j]==0)p=false;
				}
			}
			if(p) {
				JOptionPane.showMessageDialog(this, "平局", "提示",JOptionPane.OK_OPTION);
				removeMouseListener(this);//移除鼠标监听器  防止赢了还能接着下 
				jbt_st.setText("重新开始"); //平局  应该是重新开始 
				jbt_st.setEnabled(true);  //开始按钮可用
				jbt_rs.setEnabled(false); //复盘按钮不可用  
			}
		}
	}
	private void dingWei(int cx,int cy) { //定位函数 
		if(cx>=45&&cx<=840&&cy>=45&&cy<=840) { //保证只有鼠标落在有效区域才进行处理 
			//定位点击的位置在qp（棋盘）数组中的下标 
			int xp=0; 
			while(cx>asize) {
				cx = cx - asize;
				xp++;
			}
			int yp=0;
			while(cy>asize) {
				cy = cy-asize;
				yp++;
			}
			//19*19 由于格子偏小 定位偏差一个偏移量  所以减一  
			if(qpsize==19){  //19*19 有点小问题  单独处理 
				xp--;
				yp--;
			}
			if(qp[xp][yp]==0) {		//如果当前位置没有棋子 
				this.qp[xp][yp] = curr; //记录到数组中
				//记录当前棋子的位置
				curxp=xp;
				curyp=yp;
				//换棋子颜色 
				if(curr==1)curr=2; 
				else curr=1;
			}
		}
	}
	private boolean checkWin(int x, int y) {
		boolean flag = false; //假设没有赢 
		int count = 1;// 保存共有多少相同颜色棋子相连
		int color = qp[x][y]; //记录颜色 
		//赢得条件是大于等于5均可 
		// 判断横向
		count = this.checkCount(x, y, 0, 1, color);  //x不变  y变 
		if (count >= 5) {
			flag = true;
		} else {
			// 判断纵向
			count = this.checkCount(x, y, 1, 0, color); //x变 y 不变 
			if (count >= 5) {
				flag = true;
			} else {
				// 判断左下右上
				count = this.checkCount(x, y, 1, -1, color); //x增大y减小  x减小y增大 
				if (count >= 5) {
					flag = true;
				} else {
					// 判断左上右下
					count = this.checkCount(x, y, 1, 1, color);  //x y 同时增大 或减小  
					if (count >= 5) {
						flag = true;
					}
				}
			}
		}
		return flag; //返回结果
	}
	
	//x y 为当前棋子位置  xChange  yChange是检索条件 （x y增大还是减小） 
	private int checkCount(int x, int y, int xChange, int yChange, int color) {
		int count = 1;  //要被判断的棋子算一个 
		int tempX = xChange;
		int tempY = yChange; //这两个变量时为了保存检索条件 
		//数组不越界并且颜色与当前颜色相同 则加一 
		//以当前棋子所在垂线为分界线将棋盘分成两部分 
		//处理右半部分  
		while (x + xChange >= 0 && x + xChange <qpsize&& y + yChange >= 0 && y + yChange < qpsize
				&& color == qp[x + xChange][y + yChange]) {
			count++;
			if (xChange != 0) {//若等于零就是不移动
				xChange++; 
			}
			if (yChange != 0) {//等于零不移动 
				if (yChange > 0) {
					yChange++; //这个分支是x增大 y增大
				} else {
					yChange--; //这个分支是  x增大  y减小 
				}
			}
		}
		xChange = tempX;
		yChange = tempY;//恢复由于移动破坏的检索条件 
		//处理左半部分
		while (x - xChange >= 0 && x - xChange < qpsize && y - yChange >= 0 && y - yChange <qpsize
				&& color == qp[x - xChange][y - yChange]) { //数组不越界并且颜色相同就算
			count++;
			if (xChange != 0) {
				xChange++;
			}
			if (yChange != 0) { 
				if (yChange > 0) { 
					yChange++;
				} else {
					yChange--;
				}
			}
		}
		return count; //返回计数  
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
			
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
			
	}

	@Override
	public void lock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
