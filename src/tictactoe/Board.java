package tictactoe;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends JFrame{
	private static final long serialVersionUID = 2195379760914381351L;
	public int [][] boardMatrix = {{0,0,0},{0,0,0},{0,0,0}};	//棋盘矩阵
	public boolean now;				//当前谁下
	public int AIChese;
	public int HumanChese;
	private Image offScreenImage = null;	//双缓冲解决闪烁
	public Board()
	{
		setTitle("井字棋游戏");
		setSize(360, 400);
		setLocation(700, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setIcon();
	}
	
	public void initial()
	{
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				boardMatrix[i][j] = 0;	//棋盘矩阵//当前谁下
		paint(getGraphics());
		if(JOptionPane.showConfirmDialog(null, "是否选择先手？","选择先手", JOptionPane.OK_CANCEL_OPTION) == 0)
		{
			AIChese = -1;
			HumanChese = 1;
			now = true;
		}
		else
		{
			AIChese = 1;
			HumanChese = -1;
			now = false;
		}
	}
	
	public void again()
	{
		if(JOptionPane.showConfirmDialog(null, "是否继续游戏？","继续游戏", JOptionPane.OK_CANCEL_OPTION) == 0)
			initial();
		else System.exit(0);
	}
	
	@Override
	public void paint(Graphics g) 
	{
		if(offScreenImage == null)
	        offScreenImage = this.createImage(400,400);//这是游戏窗口的宽度和高度
	     
	    Graphics gOff = offScreenImage.getGraphics();
		drawBoard(gOff);
		drawPiece(gOff);
	    g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public int judgeWinner()
	{
		int res = 0;
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(boardMatrix[i][j] != 0) res++;
				if(isWin(i, j, HumanChese)) return 1;
				if(isWin(i, j, AIChese)) return -1;
			}
		}
		if(res == 9) return 2;
		else return 0;
	}
	
	private boolean isWin(int i, int j, int color)
	{
		int count = 0;
		for(int k = i; k < 3; k++)
		{
			if(boardMatrix[k][j] == color) count++;
			else break;
			if(count == 3) return true;
		}
		count = 0;
		for(int k = j; k < 3; k++)
		{
			if(boardMatrix[i][k] == color) count++;
			else break;
			if(count == 3) return true;
		}
		count = 0;
		for(int k = i,l = j; k < 3 && l <3; k++,l++)
		{
			if(boardMatrix[k][l] == color) count++;
			else break;
			if(count == 3) return true;
		}
		count = 0;
		for(int k = i,l = j; k >= 0 && l <3; k--,l++)
		{
			if(boardMatrix[k][l] == color) count++;
			else break;
			if(count == 3) return true;
		}
		return false;
	}

	private void drawBoard(Graphics g)
	{
		g.setColor(new Color(250, 150, 60));
		g.fillRect(0, 0, 400, 400);
		g.setColor(Color.black);
		for(int i = 50; i < 400; i += 110)
			g.drawLine(15, i, 345, i);
		for(int i = 15; i < 400; i += 110)
			g.drawLine(i, 50, i, 380);
	}
	
	private void drawPiece(Graphics g)
	{
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(boardMatrix[i][j] == 1) g.setColor(Color.BLACK);
				else if(boardMatrix[i][j] == -1) g.setColor(Color.WHITE);
				else g.setColor(new Color(0, 0, 0, 0));
				int x = i*110 + 30;
				int y = j*110 + 65;
				g.fillOval(x, y, 80, 80);
			}
		}
	}

	private void setIcon()
	{
		try {
			Image img = ImageIO.read(Board.class.getClassLoader().getResource("images/TicTacToe.png"));
			setIconImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
