package tictactoe;
import java.awt.event.*;

import javax.swing.*;

public class Bin {

	public static void main(String[] args) throws InterruptedException {
		Board board = new Board();
		AIPlayer AI = new AIPlayer();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		board.initial();
		if(!board.now) AI.putChess(board); 
		board.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					if(board.now)
					{
						int i = (e.getX()-15)/110;
						int j = (e.getY()-50)/110;
						if(board.boardMatrix[i][j] == 0)
						{
							board.boardMatrix[i][j] = board.HumanChese;
							board.now = false;
							board.paint(board.getGraphics());
						}
					}
					if(board.judgeWinner() != 0) 
					{
						if(board.judgeWinner() == 1)
							JOptionPane.showMessageDialog(null,"        你赢啦！！！", "游戏结束",JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null,"        平局！！！", "游戏结束",JOptionPane.INFORMATION_MESSAGE);
						board.again();
						if(!board.now) AI.putChess(board);
					}
					if(!board.now) AI.putChess(board);
					if(board.judgeWinner() != 0)
					{
						if(board.judgeWinner() == -1)
							JOptionPane.showMessageDialog(null,"        你输啦！！！", "游戏结束",JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null,"        平局！！！", "游戏结束",JOptionPane.INFORMATION_MESSAGE);
						board.again();
						if(!board.now) AI.putChess(board);
					}
				}
			}
		});
		
	}

}
