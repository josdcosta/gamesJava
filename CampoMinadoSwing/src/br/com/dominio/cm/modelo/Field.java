package br.com.dominio.cm.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.dominio.cm.excecao.ExplosionException;

public class Field {
	
	private final int row;
	private final int col;
	private boolean open = false;
	private boolean marked = false;
	private boolean mined = false;
	
	private List<Field> neighbors = new ArrayList<>();
	
	Field(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public boolean addNeighbor(Field neighbor) {
		boolean rowDiff = row != neighbor.row;
		boolean colDiff = row != neighbor.col;
		boolean diagonal = rowDiff && colDiff;
		
		int deltaRow = Math.abs(row - neighbor.row);
		int deltaCol = Math.abs(col - neighbor.col);
		int deltaLC = deltaRow + deltaCol;
		
		boolean neighborValidator = (deltaLC == 1 && !diagonal)
				|| (deltaLC == 2 && diagonal);
		
		if(neighborValidator) {
			neighbors.add(neighbor);
			return true;
		} else {
			return false;
		}
	}
	
	public void toggleMark() {
		if(!open) {
			marked = !marked;
		}
	}
	
	boolean open() {
		if(!marked && !open) {
			open = true;
			if(mined) {
				throw new ExplosionException();
			}
			if(neighborhoodSafe()) {
				neighbors.stream().forEach(v -> v.open());
			}
			return true;
		} else {
			return false;
		}
	}
	
	boolean neighborhoodSafe() {
		return neighbors.stream().noneMatch(v -> v.mined);
	}
	
	void mine() {
		mined = true;
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	void setOpened(boolean open) {
		this.open = open;
	}

	public boolean isMined() {
		return mined;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	boolean goalAchieved() {
		boolean desvendado = !mined && open;
		boolean protegido = mined && marked;
		return desvendado || protegido;
	}
	
	long minesInTheNighborhood() {
		return neighbors.stream().filter(v -> v.mined).count();
	}
	
	void reiniciar() {
		open = false;
		mined = false;
		marked = false;
	}
	
	public String toString() {
		if(marked) {
			return "[ x ]";
		} else if(open && mined) {
			return "[ * ]";
		} else if(open && minesInTheNighborhood() > 0) {
			return "[ " + Long.toString(minesInTheNighborhood()) + " ]";
		} else if(open) {
			return "";
		} else {
			return "[" + String.valueOf(row) + "," + String.valueOf(col) + "]";
		}
	}
	
}
