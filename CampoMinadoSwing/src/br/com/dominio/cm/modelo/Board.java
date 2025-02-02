package br.com.dominio.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.dominio.cm.excecao.ExplosionException;

public class Board {

	private final int row;
	private final int col;
	private final int mines;
	
	private final List<Field> fields = new ArrayList<Field>();

	public Board(int row, int col, int mines) {
		this.row = row;
		this.col = col;
		this.mines = mines;
		
		generateFields();
		patnerNeighbor();
		sortMines();
	}

	private void generateFields() {
		for (int row = 0; row < this.row; row++) {
			for (int col = 0; col < this.col; col++) {
				fields.add(new Field(row, col));
			}
		}
	}
	
	public void open(int row, int col) {
		try {
			Predicate<Field> filterLinCol = c -> (c.getRow() == row && c.getCol() == col);
			fields.stream().filter(filterLinCol).findFirst().ifPresent(Field::open);
		} catch (ExplosionException e) {
			fields.forEach(c -> c.setOpened(true));
			throw e;
		}

	}
	
	public void changeMark(int row, int col) {
		Predicate<Field> filterLinCol = c -> (c.getRow() == row && c.getCol() == col);
		fields.stream().filter(filterLinCol).findFirst().ifPresent(Field::toggleMark);
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
			minasArmadas = fields.stream().filter(v -> v.isMined()).count();
		} while (minasArmadas < mines);
		
	}
	
	public boolean goalAchieved() {
		return fields.stream().allMatch(c -> c.goalAchieved());
	}
	
	public void restart() {
		fields.stream().forEach(c -> c.reiniciar());
		sortMines();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (int l = 0; l < row; l++) {
			for (int c = 0; c < col; c++) {
                sb.append(fields.get(i));
                i++;
			}
			sb.append("\n");
		};
		
		return sb.toString();
	}
	
}
