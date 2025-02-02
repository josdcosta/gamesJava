package br.com.minesweeper.cm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Field {
	
	private final int row;
	private final int col;
	private boolean open = false;
	private boolean checked = false;
	private boolean mined = false;
	
	private List<Field> neighbors = new ArrayList<>();

	private List<FieldObserver> observers = new ArrayList<>();

	private void notifyObservers(FieldEventEnum event){
		observers.forEach(o -> o.eventTrigger(this, event));
	}

	Field(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public void subscribe(FieldObserver observer){
		observers.add(observer);
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
	
	public void toggleCheck() {
		if(!open) {
			checked = !checked;
			if(checked){
				notifyObservers(FieldEventEnum.CHECK);
			} else {
				notifyObservers(FieldEventEnum.UNCHECK);
			}
		}
	}
	
	public boolean open() {
		if(!checked && !open) {
			if(mined) {
				notifyObservers(FieldEventEnum.EXPLOSION);
				return true;
			}

			setOpened(true);
			if(neighborhoodSafe()) {
				neighbors.stream().forEach(v -> v.open());
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean neighborhoodSafe() {
		return neighbors.stream().noneMatch(v -> v.mined);
	}
	
	void mine() {
		mined = true;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	void setOpened(boolean open) {
		this.open = open;
		if(open){
			notifyObservers(FieldEventEnum.OPEN);
		}
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
		boolean protegido = mined && checked;
		return desvendado || protegido;
	}
	
	public int minesInTheNighborhood() {
		return (int) neighbors.stream().filter(v -> v.mined).count();
	}
	
	void restart() {
		open = false;
		mined = false;
		checked = false;
		notifyObservers(FieldEventEnum.REINICIAR);
	}
	
}
