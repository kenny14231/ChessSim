package chesssim;

public class ChessPiece {

	int[] position;
	String type;
	boolean hasMoved;
	String side;
	boolean isDead;
	boolean canCastle;
	boolean isInCheck;
	
	public ChessPiece(int[] position, String type, String side) {
		// TODO Auto-generated constructor stub
		this.position = position;
		this.type = type;
		this.side = side;
		this.hasMoved = false;
		this.isDead = false;
		this.canCastle = false;
		this.isInCheck = false;
	}
	
	public int[] getPosition () {
		return this.position;
	}
	
	public String getType () {
		return this.type;
	}
	
	public boolean getHasMoved () {
		return this.hasMoved;
	}
	
	public String getSide () {
		return this.side;
	}
	
	public boolean getIsDead () {
		return this.isDead;
	}
	
	public boolean getCanCastle () {
		return this.canCastle;
	}
	
	public boolean getIsInCheck () {
		return this.isInCheck;
	}
	public void setType (String type) {
		this.type = type;
	}
	
	public void setSide (String side) {
		this.side = side;
	}
	
	public void setPosition (int[] position) {
		this.position = position;
	}
	
	public void setHasMoved (boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
	public void setDead (boolean isDead) {
		this.isDead = isDead;
	}
	
	public void setCheck (boolean isInCheck) {
		this.isInCheck = isInCheck;
	}
	
	public void setCanCastle (boolean canCastle) {
		this.canCastle = canCastle;
	}
}
