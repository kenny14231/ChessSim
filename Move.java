package chesssim;

import java.util.*;

public class Move {

	ArrayList<int[]> moves;
	ChessPiece piece;

	public boolean inBounds(int[] position) {
		int posX = position[0];
		int posY = position[1];
		boolean inBounds = false;

		if (posX <= 8 && posX >= 1 && posY <= 8 && posY >= 1) {
			inBounds = true;
		} else {
			inBounds = false;
		}

		return inBounds;
	}

	public boolean hasPiece(int[] position, ChessPiece[] chessBoard) {
		boolean hasPiece = true;

		int posX = position[0];
		int posY = position[1];

		if (posX <= 8 && posX >= 1 && posY <= 8 && posX >= 1) {
			ChessPiece piece = getPieceFromPosition(position, chessBoard);
			String pieceType = piece.getType();
			if (pieceType.equals("")) {
				hasPiece = false;
			}
		}
		return hasPiece;

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

	public static int getNumberFromPosition(int[] position) {
		int posX = position[0];
		int posY = position[1];
		int number = (posX - 1) + 8 * (posY - 1);
		return number;
	}

	public int[] newPosition(int[] position, int moveX, int moveY) {

		int posX = position[0];
		int posY = position[1];

		int newPosX = posX + moveX;
		int newPosY = posY + moveY;

		int[] newPosition = { newPosX, newPosY };
		return newPosition;
	}

	public Move(ChessPiece piece, int[] position, ChessPiece[] chessBoard) {
		// TODO Auto-generated constructor stub
		ArrayList<int[]> moves = new ArrayList<int[]>();

		String pieceType = piece.getType();
		String pieceSide = piece.getSide();
		boolean hasMoved = piece.getHasMoved();
		String opposingPieceSide = "white";
		int directionMultiplier = 1;

		if (pieceSide == "black") {
			opposingPieceSide = "white";
			directionMultiplier = -1;
		} else if (pieceSide == "white") {
			opposingPieceSide = "black";
			directionMultiplier = 1;
		}

		switch (pieceType) {
		case "pawn":

			boolean oneForward = false;
			boolean twoForward = false;
			boolean oneDiagonalRight = false;
			boolean oneDiagonalLeft = false;

			int[] newPositionOneForward = newPosition(position, 0, 1 * directionMultiplier);
			int[] newPositionTwoForward = newPosition(position, 0, 2 * directionMultiplier);
			oneForward = (inBounds(newPositionOneForward) && hasPiece(newPositionOneForward, chessBoard) == false);
			twoForward = (inBounds(newPositionTwoForward) && hasPiece(newPositionTwoForward, chessBoard) == false);

			int[] newPositionRight = newPosition(position, 1, directionMultiplier);
			ChessPiece attackedRightPiece = getPieceFromPosition(newPositionRight, chessBoard);
			String attackedRightPieceSide = attackedRightPiece.getSide();
			oneDiagonalRight = (inBounds(newPositionRight) && hasPiece(newPositionRight, chessBoard)
					&& attackedRightPieceSide == opposingPieceSide);

			int[] newPositionLeft = newPosition(position, -1, directionMultiplier);
			ChessPiece attackedLeftPiece = getPieceFromPosition(newPositionLeft, chessBoard);
			String attackedLeftPieceSide = attackedLeftPiece.getSide();
			oneDiagonalLeft = (inBounds(newPositionLeft) && hasPiece(newPositionLeft, chessBoard)
					&& attackedLeftPieceSide == opposingPieceSide);

			if (oneForward) {
				moves.add(newPositionOneForward);
				if (hasMoved == false && twoForward) { // move forward 2 if first move
					moves.add(newPositionTwoForward);
				}
			}
			if (oneDiagonalRight) {
				moves.add(newPositionRight);
			}
			if (oneDiagonalLeft) {
				moves.add(newPositionLeft);
			}
			break;
		case "knight":
			int[][] possibleMoves = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 }, { 1, -2 },
					{ -1, -2 } };

			for (int[] move : possibleMoves) {
				//System.out.println(move[0] + "," + move[1]);
				int moveX = move[0];
				int moveY = move[1];
				int[] newPosition = newPosition(position, moveX, moveY);
				
				if (inBounds(newPosition)) {
					//System.out.println(newPosition[0] + "," +  newPosition[1]);
					if (hasPiece(newPosition, chessBoard)) {
						ChessPiece attackedPiece = getPieceFromPosition(newPosition, chessBoard);
						String attackedPieceSide = attackedPiece.getSide();
						if (attackedPieceSide == opposingPieceSide) {
							moves.add(newPosition);
						}
					} else if (hasPiece(newPosition, chessBoard) == false) {
						moves.add(newPosition);
					}
				}
			}
			break;
		case "bishop":
			int[][] bishopLines = {{1,1}, {-1,1}, {-1,-1}, {1, -1}};
			for(int[] m : bishopLines) { //go through all four directions
				for (int j=1; j<=8; j++) { //go along the diagonal
					int[] newPosition = newPosition(position, j*m[0], j*m[1]);
					if(inBounds(newPosition)) {
						if(hasPiece(newPosition, chessBoard)) { //attack piece
							ChessPiece attackedPiece = getPieceFromPosition(newPosition, chessBoard);
							String attackedPieceSide = attackedPiece.getSide();
							if (attackedPieceSide == opposingPieceSide) {
								moves.add(newPosition);
							}
							break;
						} else if (hasPiece(newPosition, chessBoard) == false) { //free space
							moves.add(newPosition);
						}
					} else {
						break;
					}
				}
			}
			break;
		case "rook":
			int[][] rookLines = {{0,1}, {0,-1}, {-1,0}, {1, 0}};
			for(int[] m : rookLines) { //go through all four directions
				for (int j=1; j<=8; j++) { //go along the lines
					int[] newPosition = newPosition(position, j*m[0], j*m[1]);
					//System.out.println("dx: " + j*m[0] + ", dy: " + j*m[1]);
					if(inBounds(newPosition)) {
						if(hasPiece(newPosition, chessBoard)) { //attack piece
							ChessPiece attackedPiece = getPieceFromPosition(newPosition, chessBoard);
							String attackedPieceSide = attackedPiece.getSide();
							if (attackedPieceSide == opposingPieceSide) {
								moves.add(newPosition);
							}
							break;
						} else if (hasPiece(newPosition, chessBoard) == false) { //free space
							moves.add(newPosition);
						}
					} else {
						break;
					}
				}
			}
			break;
		case "king":
			//System.out.println(pieceSide + "king is being analyzed: ");
			int[][] kingLines = {{0,1}, {0,-1}, {-1,0}, {1, 0}, {1,1}, {-1,1}, {-1,-1}, {1, -1}};
			for(int[] m : kingLines) { //go through all eight directions
				//System.out.println(m[0] + "," + m[1]);
				int[] newPosition = newPosition(position, m[0], m[1]);
				//System.out.println("potential " + pieceSide + "king move: "+ newPosition[0] + "," + newPosition[1]);
				if(inBounds(newPosition)) {
					if(hasPiece(newPosition, chessBoard)) { //attack piece
						ChessPiece attackedPiece = getPieceFromPosition(newPosition, chessBoard);
						String attackedPieceSide = attackedPiece.getSide();
						if (attackedPieceSide == opposingPieceSide) {
							//System.out.println("king move added: " + newPosition[0] + "," + newPosition[1]);
							moves.add(newPosition);
						}
					} else if (hasPiece(newPosition, chessBoard) == false) { //free space
						moves.add(newPosition);
						//System.out.println("king move added: " + newPosition[0] + "," + newPosition[1]);
					}
				}
			}
			break;
		case "queen":
			int[][] queenLines = {{0,1}, {0,-1}, {-1,0}, {1, 0}, {1,1}, {-1,1}, {-1,-1}, {1, -1}};
			for(int[] m : queenLines) { //go through all eight directions
				for (int j=1; j<=8; j++) { //go along the lines
					int[] newPosition = newPosition(position, j*m[0], j*m[1]);
					if(inBounds(newPosition)) {
						if(hasPiece(newPosition, chessBoard)) { //attack piece
							ChessPiece attackedPiece = getPieceFromPosition(newPosition, chessBoard);
							String attackedPieceSide = attackedPiece.getSide();
							if (attackedPieceSide == opposingPieceSide) {
								moves.add(newPosition);
							}
							break;
						} else if (hasPiece(newPosition, chessBoard) == false) { //free space
							moves.add(newPosition);
						}
					} else {
						break; //not in bounds change direction
					}
				}
			}
			break;
		default:
			break;
		}

		this.moves = moves;

		this.piece = piece;

	}

	public ArrayList<int[]> getMoves() {
		return this.moves;
	}

	public ChessPiece getPiece() {
		return this.piece;
	}

	public int getNumMoves() {
		return this.moves.size();
	}
}
