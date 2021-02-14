package chesssim;

import java.util.*;

import com.rits.cloning.Cloner;

public class ChessGuider extends ChessBoard {

	int[] bestNewPosition;
	int[] bestOldPosition;
	ChessPiece piece;
	boolean isInCheckMate;
	ArrayList<Move> allPotentialMoves;

	public ChessGuider(ChessPiece[] chessBoard, String side) {

		MoveOption moves = new MoveOption(chessBoard, side);
		ArrayList<Move> allMoves = moves.getAllMoves();
		
		int[][] optimal = bestMove(allMoves, chessBoard, side);
		
		int[][] checkMate = {{0,0}, {0,0}};
		if(optimal[0][0] == checkMate[0][0] && optimal[0][1] == checkMate[0][1]
				&& optimal[1][0] == checkMate[1][0] && optimal[1][1] == checkMate[1][1]) {
			this.isInCheckMate = true;
			System.out.println(side + " is in checkmate");
		} else {
			this.isInCheckMate = false;
		}
		
		int[] optimalStart = optimal[0];
		int[] optimalEnd = optimal[1];

		this.bestOldPosition = optimalStart;
		this.bestNewPosition = optimalEnd;
		this.allPotentialMoves = allMoves;

	}
	
	public int[][] bestMove (ArrayList<Move> allMoves, ChessPiece[] chessBoard, String side) {

		HashMap<int[][], Double> evaluations = new HashMap<int[][], Double>();

		for (Move moves : allMoves) {
			ChessPiece piece = moves.getPiece();
			int[] oldPosition = piece.getPosition();
			ArrayList<int[]> pieceMoves = moves.getMoves();
			
			// System.out.println(allMoves.get(i).getPiece().getType());
			for (int[] newPosition : pieceMoves) {
				double evaluation = evaluate(oldPosition, newPosition, piece, chessBoard, side);
				int[][] move = { oldPosition, newPosition };
				evaluations.put(move, evaluation);
			}
		}
		
		int[][] bestMove = Collections.max(evaluations.entrySet(), Map.Entry.comparingByValue()).getKey();
		double bestMoveEvaluation = evaluations.get(bestMove);

		//System.out.println(bestMoveEvaluation);
		if(bestMoveEvaluation < -5000) {
			int[][] checkMate = {{0,0}, {0,0}};
			return checkMate;
		}

		return bestMove;
	}
	
	public double evaluate(int[] oldPosition, int[] newPosition, ChessPiece piece, ChessPiece[] chessBoard, String side) {
		Cloner cloner = new Cloner();
		
		ChessPiece[] testBoard = cloner.deepClone(chessBoard);
		
		movePiece(oldPosition, newPosition, testBoard, false, 0);
		
		String opposingPieceSide = "";
		
		if (side == "black") {
			opposingPieceSide = "white";
		} else if (side == "white") {
			opposingPieceSide = "black";
		}
		
		MoveOption potentialEnemyMoves = new MoveOption(testBoard, opposingPieceSide);
		ArrayList<Move> allPotentialEnemyMoves = potentialEnemyMoves.getAllMoves();
		
		String pieceType = piece.getType();
		setCheck(testBoard, allPotentialEnemyMoves, side);
		
		//actual value for neural network testing
		double ownPieceValue = 0;
		double enemyPieceValue = 0;
		double value = 0;
		
		HashMap<String, Double> weights = new HashMap<String, Double>();
		weights.put("pawn", 1.0);
		weights.put("knight", 3.0);
		weights.put("bishop", 3.0);
		weights.put("rook", 5.0);
		weights.put("queen", 9.0);
		weights.put("king", 200.0);
		weights.put("", 0.0);
		
		for(int i = 0; i<64; i++) {
			ChessPiece p = getPieceFromPosition(getPositionFromNumber(i), testBoard);
			String pType = p.getType();
			String pSide = p.getSide();
			if(pSide == side) {
				ownPieceValue += weights.get(pType);
			} else if (pSide == opposingPieceSide) {
				enemyPieceValue += weights.get(pType);
			}
		}
		
		value = ownPieceValue - enemyPieceValue;
		
		if(isInCheck(testBoard, side)) {
			value = -10000;
		}
	
		return (value + Math.random());
	}
	
	public boolean isInCheck (ChessPiece[] chessBoard, String side)  {
		for (int i = 0; i < 64; i++) {
			ChessPiece piece = getPieceFromPosition(getPositionFromNumber(i), chessBoard);
			if(piece.isInCheck && piece.getSide() == side) {
				return true;
			}
		}
		
		return false;
	}
	
	public void setCheck (ChessPiece[] chessBoard, ArrayList<Move> allPotentialEnemyMoves, String ownSide) {
		for(ChessPiece piece : chessBoard) {
			if(piece.getSide() == ownSide) {
				piece.setCheck(false);
			}
		}
		
		boolean isInCheck = false;
		
		for (Move move : allPotentialEnemyMoves) {
			ArrayList<int[]> moveSet = move.getMoves();
			for (int[] attackedPosition : moveSet) {
				ChessPiece attackedPiece = getPieceFromPosition(attackedPosition, chessBoard);
				if(attackedPiece.getType() == "king") {
					//System.out.println(ownSide + " king will be in check if " + ownSide + " makes this move: " + pieceType + " " + oldPosition[0] + "," + oldPosition[1] + " to " + newPosition[0] + "," + newPosition[1]);
					attackedPiece.setCheck(true);
					isInCheck = true;
				}
			}
		}
		
		if(isInCheck == false){
			//System.out.println(ownSide + " king will not be in check if " + ownSide + " makes this move: " + pieceType + " " + oldPosition[0] + "," + oldPosition[1] + " to " + newPosition[0] + "," + newPosition[1]);
		}
	}

	public boolean getInCheckMate () {
		return this.isInCheckMate;
	}
	
	public int[] getBestNewPosition () {
		return this.bestNewPosition;
	}
	
	public int[] getBestOldPosition () {
		return this.bestOldPosition;
	}

}
