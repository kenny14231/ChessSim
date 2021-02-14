package chesssim;
import java.util.*;

public class MoveOption {
	
	ArrayList<Move> allMoves;

	public static ChessPiece getPieceFromPosition(int[] position, ChessPiece[] chessBoard) {

		ChessPiece piece = chessBoard[0];

		for (int i = 0; i < 64; i++) {
			ChessPiece p = chessBoard[i];
			int[] pPosition = p.getPosition();
			if (pPosition[0] == position[0] && pPosition[1] == position[1]) {
				piece = p;
			}
		}

		return piece;
	}
	
	public static int[] getPositionFromNumber(int number) {
		int posX = number % 8 + 1;
		int posY = number / 8 + 1;
		int[] position = { posX, posY };
		return position;
	}

	public static int getNumberFromPosition(int[] position) {
		int posX = position[0];
		int posY = position[1];
		int number = (posX - 1) + 8 * (posY - 1);
		return number;
	}
	
	public MoveOption(ChessPiece[] chessBoard, String side) {
		// TODO Auto-generated constructor stub
		ArrayList<Move> allMoves = new ArrayList<Move>();
		
		for (int i = 0; i < 64; i++) {
			int[] position = getPositionFromNumber(i);
			ChessPiece piece = getPieceFromPosition(position, chessBoard);
			
			if(piece.getSide() == side) {
				//System.out.println("piece: " + piece.getSide() + "-" + piece.getType());
				allMoves.add(new Move(piece, position, chessBoard));
			}
		}
		this.allMoves = allMoves;
	}
	
	public ArrayList<Move> getAllMoves () {
		return this.allMoves;
	}
}
