package chesssim;

import java.util.HashMap;

public class ChessBoard {

	public ChessBoard() {
		// TODO Auto-generated constructor stub
	}
	
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
	
	public static void reset(int[] position, ChessPiece[] chessBoard) {
		String side = "neutral";
		String pieceType = "";
		boolean hasMoved = false;

		ChessPiece piece = getPieceFromPosition(position, chessBoard);
		piece.setDead(true);
		piece.setHasMoved(hasMoved);
		piece.setType(pieceType);
		piece.setSide(side);

	}
	
	public static String movePiece(int[] position, int[] newPosition, ChessPiece[] chessBoard, boolean isRealMove, int moveNum) {
		ChessPiece openArea = getPieceFromPosition(newPosition, chessBoard);
		ChessPiece piece = getPieceFromPosition(position, chessBoard);

		if (openArea.getType() != "") { // kill enemy piece
			reset(newPosition, chessBoard);
			//System.out.println("kill");
		}

		openArea.setPosition(position);
		piece.setPosition(newPosition);
		if (piece.getType() != "") {
			piece.setHasMoved(true);
		}

		String pieceType = piece.getType();
		String pieceSide = piece.getSide();
		
		if(pieceType == "pawn") {
			if(pieceSide == "white") {
				if(piece.getPosition()[1] == 8) {
					piece.setType("queen");
				}
			} else if (pieceSide == "black") {
				if(piece.getPosition()[1] == 1) {
					piece.setType("queen");
				}
			}
			
		}
		
		if(isRealMove) {
			//PDN translator
			String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
			HashMap<String, String> pieces = new HashMap<String, String>();
			pieces.put("knight", "N");
			pieces.put("pawn", "");
			pieces.put("rook", "R");
			pieces.put("king", "K");
			pieces.put("queen", "Q");
			pieces.put("bishop", "B");
			String currentLetter = letters[position[0]-1];
			String newLetter = letters[newPosition[0]-1];
			
			String pdnMove = moveNum + ". " + pieces.get(pieceType)+ currentLetter + position[1] + newLetter + newPosition[1];
			//System.out.println(pieceSide + "-" + pdnMove);
			return(pdnMove);
		}
		
		return "";
	}

}
