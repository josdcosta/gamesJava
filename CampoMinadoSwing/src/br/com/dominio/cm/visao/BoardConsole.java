package br.com.dominio.cm.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Function;

import br.com.dominio.cm.excecao.ExplosionException;
import br.com.dominio.cm.excecao.QuitException;
import br.com.dominio.cm.modelo.Board;

public class BoardConsole {
	
	private Board board;
	private Scanner input = new Scanner(System.in);
	
	public BoardConsole(Board board) {
		this.board = board;
		executeGame();
	}

	private void executeGame() {
		try {
			boolean goOn = true;
			while(goOn) {
				System.out.println("Iniciar partida? S/n");
				String response = input.nextLine();
				if("n".equalsIgnoreCase(response)){
					goOn = false;
				} else if("s".equalsIgnoreCase(response)) {
					gameCicle();
					board.restart();
				}
			}
			
		} catch (QuitException e){
			System.out.println("Bye!");
		} finally {
			input.close();
		}
	}

	private void gameCicle() {
		try {
			while(!board.goalAchieved()) {
				System.out.println(board);
				String typed = captureTypedValue("Digite o valor x e y:");
				String[] coordArray = typed.trim().split(",");
				Function<String, Integer> integerValue = d ->Integer.parseInt(d);
				if(coordArray.length == 2) {
					Iterator<Integer> xy = Arrays.stream(coordArray).map(integerValue).iterator();
					typed = captureTypedValue("1 - Abrir campo; 2 - (Des)marcar");
					if("1".equalsIgnoreCase(typed)) {
						board.open(xy.next(), xy.next());
					} else if("2".equalsIgnoreCase(typed)) {
						board.changeMark(xy.next(), xy.next());
					} else {
						System.out.println("Digite uma das opções");
					}
				}
			}
			System.out.println(board);
			System.out.println("Você ganhou!");
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println("Você perdeu!");
		}
	}

	private String captureTypedValue(String text) {
		System.out.println(text);
		String typed = input.nextLine();
		if("sair".equalsIgnoreCase(typed)) {
			throw new QuitException();
		}
		return typed;
	};

}
