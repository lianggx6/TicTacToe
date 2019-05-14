package tictactoe;
public class AIPlayer extends Player{
	
	int putX = 1;
	int putY = 1;

	@Override
	public void putChess(Board board) {
		getXY(board, board.AIChese);
		board.boardMatrix[putX][putY] = board.AIChese;
		board.now = true;
		board.paint(board.getGraphics());
		
	}
	
	private void getXY(Board board, int AIChese)
	{
		int MAX = -1000;
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(board.boardMatrix[i][j] == 0)
				{
					board.boardMatrix[i][j] = AIChese;
					if(board.judgeWinner() == -1) 
					{
						MAX = 1000;
						setXY(i,j);
						return;
					}
					int MIN = 1000;
					for(int p = 0; p < 3; p++)
					{
						for(int q = 0; q < 3; q++)
						{
							 if(board.boardMatrix[p][q] == 0)
							 {
								 board.boardMatrix[p][q] = -1*AIChese;
								 if(board.judgeWinner() == 1) MIN = -1000;
								 else
								 {
									 int goal = getAllGoals(board.boardMatrix, AIChese) - getAllGoals(board.boardMatrix, -1*AIChese);
									 if(goal < MIN) MIN = goal;
								 }
								 board.boardMatrix[p][q] = 0;
							 }
						}
					}
					if(MIN > MAX)
					{
						MAX = MIN;
						setXY(i,j);
					}
					board.boardMatrix[i][j] = 0;
				}	
			}
		}
	}
	
	private void setXY(int x, int y)
	{
		putX = x;
		putY = y;
	}
	
	public int getAllGoals(int[][] boardMatrix, int color)
	{
		int res = 0;
		for(int i = 0; i < 3; i++)
			res += getOneGoals(boardMatrix[i][0], boardMatrix[i][1], boardMatrix[i][2], color);
		for(int j = 0; j < 3; j++)
			res += getOneGoals(boardMatrix[0][j], boardMatrix[1][j], boardMatrix[2][j], color);
		res += getOneGoals(boardMatrix[0][0], boardMatrix[1][1], boardMatrix[2][2], color);
		res += getOneGoals(boardMatrix[0][2], boardMatrix[1][1], boardMatrix[2][0], color);
		return res;
	}
	
	private int getOneGoals(int first, int second, int third, int color)
	{
		
		if(first == 0 && second == color && third == color) return 100;
		if(first == color && second == 0 && third == color) return 100;
		if(first == color && second == color && third == 0) return 100;
		if(first == color && second == 0 && third == 0) return 10;
		if(first == 0 && second == color && third == 0) return 10;
		if(first == 0 && second == 0 && third == color) return 10;
		return 0;
	}

}
