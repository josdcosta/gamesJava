package br.com.dominio.cm;

import br.com.dominio.cm.modelo.Board;
import br.com.dominio.cm.visao.BoardConsole;

public class App {

	public static void main(String[] args) {
		Board board = new Board(6, 6, 6);
		
		new BoardConsole(board);
	}
	
}
