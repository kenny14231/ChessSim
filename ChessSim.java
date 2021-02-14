package chesssim;

import java.util.*;

import com.rits.cloning.Cloner;

public class ChessSim extends ChessBoard {

	public ChessSim() {
		// TODO Auto-generated constructor stub
	}

	public static ChessPiece[] init(ChessPiece[] chessBoard) {

		for (int i = 0; i < 64; i++) {

			int[] position;
			String pieceType = "";
			String side = "";

			position = getPositionFromNumber(i);
			int posX = position[0];
			int posY = position[1];

			if (posY == 1 || posY == 2) {
				side = "white";
			} else if (posY == 7 || posY == 8) {
				side = "black";
			}

			if (posY == 2 || posY == 7) {
				pieceType = "pawn";
			} else if (posY == 1 || posY == 8) {
				switch (posX) {
				case 1:
					pieceType = "rook";
					break;
				case 8:
					pieceType = "rook";
					break;
				case 2:
					pieceType = "knight";
					break;
				case 7:
					pieceType = "knight";
					break;
				case 3:
					pieceType = "bishop";
					break;
				case 6:
					pieceType = "bishop";
					break;
				case 4:
					pieceType = "queen";
					break;
				case 5:
					pieceType = "king";
					break;
				}
			}

			chessBoard[i] = new ChessPiece(position, pieceType, side);
		}

		return chessBoard;

	}

	public static void printBoard(ChessPiece[] chessBoard) {
		for (int i = 0; i < 64; i++) {
			ChessPiece piece = getPieceFromPosition(getPositionFromNumber(i), chessBoard);
			// System.out.println(getPositionFromNumber(i)[0] + "," +
			// getPositionFromNumber(i)[1]);
			System.out.println(piece.getPosition()[0] + "," + piece.getPosition()[1] + "|" + piece.getType());
					// + "|" + piece.getHasMoved() + "|" + piece.getSide() + "|" + piece.getIsDead());
		}
	}

	public static void setCheck (ChessPiece[] chessBoard, ArrayList<Move> allMoves, String side) {
		
		String opposingPieceSide = "";
		
		if (side == "black") {
			opposingPieceSide = "white";
		} else if (side == "white") {
			opposingPieceSide = "black";
		}
		
		for(ChessPiece piece : chessBoard) {
			if(piece.getSide() == opposingPieceSide) {
				piece.setCheck(false);
			}
		}
		
		for (Move move : allMoves) {
			
			for (int[] attackedPosition : move.getMoves()) {
				ChessPiece attackedPiece = getPieceFromPosition(attackedPosition, chessBoard);
				if(attackedPiece.getType() == "king") {
					attackedPiece.setCheck(true);
					//System.out.println(opposingPieceSide + " king is in check: " + attackedPiece.getIsInCheck());
				}
			}
		}
	}
	
	public int[][] getBestMove (ArrayList<Move> allPotentialMoves, ChessPiece[] chessBoard, String side) {
		HashMap<int[][], Double> evaluations = new HashMap<int[][], Double>();

		Cloner cloner = new Cloner();
		
		ChessPiece[] testBoard = cloner.deepClone(chessBoard);
		
		for (Move moves : allPotentialMoves) {
			ChessPiece piece = moves.getPiece();
			int[] oldPosition = piece.getPosition();
			ArrayList<int[]> pieceMoves = moves.getMoves();
			
			// System.out.println(allMoves.get(i).getPiece().getType());
			for (int[] newPosition : pieceMoves) {
				movePiece(oldPosition, newPosition, testBoard, false, 0);
				double evaluation = evaluate(oldPosition, newPosition, piece, chessBoard, side);
				int[][] move = { oldPosition, newPosition };
				evaluations.put(move, evaluation);
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// initiation
		ChessPiece[] chessBoard = init(new ChessPiece[64]);
		// printBoard(chessBoard);

		String side = "white";

		String moves = "";
		
		// start of loop
		for (int i = 0; i < 200; i++) {
			
			ChessGuider guide = new ChessGuider(chessBoard, side);
			int[] bestOldPosition = guide.getBestOldPosition();
			int[] bestNewPosition = guide.getBestNewPosition();
			boolean isInCheckMate = guide.getInCheckMate();
			
			if(isInCheckMate) {
				System.out.println(side + " loses");
				break;
			} else  {
				moves = moves + movePiece(bestOldPosition, bestNewPosition, chessBoard, true, i+1) + "\n";
				
				MoveOption potentialMoves = new MoveOption(chessBoard, side);
				ArrayList<Move> allPotentialMoves = potentialMoves.getAllMoves();
				setCheck(chessBoard, allPotentialMoves, side);
			}
			
			//System.out.println("change sides");
			if (side == "white") {
				side = "black";
			} else if (side == "black") {
				side = "white";
			}
		}
		System.out.println(moves);
		printBoard(chessBoard);
	}

}
