package br.com.minesweeper.cm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Board implements FieldObserver {

	private final int row;
	private final int col;
	private final int mines;

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	private final List<Field> fields = new ArrayList<>();
	private final List<Consumer<ResultEvent>> observers =
			new ArrayList<>();

	public Board(int row, int col, int mines) {
		this.row = row;
		this.col = col;
		this.mines = mines;
		
		generateFields();
		patnerNeighbor();
		sortMines();
	}

	public void forEachField(Consumer<Field> function){
		fields.forEach(function);
	}

	public void subscribe(Consumer<ResultEvent> observer){
		observers.add(observer);
	}

	private void notifyObservers(boolean result){
		observers.forEach(o -> o.accept(new ResultEvent(result)));
	}

	private void generateFields() {
		for (int row = 0; row < this.row; row++) {
			for (int col = 0; col < this.col; col++) {
				Field field = new Field(row, col);
				field.subscribe(this);
				fields.add(field);
			}
		}
	}
	
	public void open(int row, int col) {
		try {
			Predicate<Field> filterLinCol = c -> (c.getRow() == row && c.getCol() == col);
			fields.stream().filter(filterLinCol).findFirst().ifPresent(Field::open);
		} catch (Exception e) {
			fields.forEach(c -> c.setOpened(true));
			throw e;
		}
	}


	
	public void changeCheck(int row, int col) {
		Predicate<Field> filterLinCol = c -> (c.getRow() == row && c.getCol() == col);
		fields.stream().filter(filterLinCol).findFirst().ifPresent(Field::toggleCheck);
	}

	private void patnerNeighbor() {
		for(Field c1: fields) {
			for(Field c2: fields) {
				c1.addNeighbor(c2);
			}
		}
	}
	
	
	private void sortMines() {
		long minasArmadas = 0;
		do {
			int aleatorio = (int) (Math.random() * fields.size());
			fields.get(aleatorio).mine();
			minasArmadas = fields.stream().filter(Field::isMined).count();
		} while (minasArmadas < mines);
		
	}
	
	public boolean goalAchieved() {
		return fields.stream().allMatch(Field::goalAchieved);
	}
	
	public void restart() {
		fields.forEach(Field::restart);
		sortMines();
	}

	@Override
	public void eventTrigger(Field field, FieldEventEnum event) {
		if(event == FieldEventEnum.EXPLOSION){
			showMines();
			notifyObservers(false);
		} else if(goalAchieved()){
			System.out.println("Ganhou!");
			notifyObservers(true);
		}
	}

	private void showMines(){
		fields.stream().filter(Field::isMined).filter(f -> !f.isChecked()).
				forEach(f -> f.setOpened(true));
	}
}
