package de.htwg.monopoly.controller.impl;

import java.util.ArrayList;
import java.util.List;

import de.htwg.monopoly.controller.IController;
import de.htwg.monopoly.entities.Bank;
import de.htwg.monopoly.entities.Dice;
import de.htwg.monopoly.entities.Player;
import de.htwg.monopoly.entities.Playfield;
import de.htwg.monopoly.entities.Street;
import de.htwg.monopoly.observer.impl.Observable;
import de.htwg.monopoly.util.IMonopolyUtil;

public class Controller extends Observable implements IController {
	private PlayerController players;
	private Playfield field;
	private Player currentPlayer;
	private String message;

	public Controller() {
		this.players = new PlayerController();
		this.field = new Playfield();
	}
	
	@Override
	public boolean setNumberofPlayer() {
		return players.readNumberOfPlayer();
	}
	
	@Override
	public boolean setNameofPlayer(int i) {
		return players.readNameOfPlayer(i);
	}
	
	@Override
	public void initGame(int fieldSize) {
		this.field.initialize(fieldSize);
	}

	@Override
	public void startNewGame() {
		//startTurn();
		this.currentPlayer = players.currentPlayer();
		notifyObservers(0);
	}

	@Override
	public void startTurn() {
		//this.currentPlayer = players.currentPlayer();
		if (currentPlayer.isInPrison()) {
			currentPlayer.incrementPrisonRound();
		} else {
			Dice.throwDice();
			field.movePlayer(currentPlayer, (Dice.resultDice));
		}
		// �berpr�fen auf was f�rn feldobjek
		// dementsprechend notify
		notifyObservers(1);
		//notifyObservers();
	}

	@Override
	public void rollDice() {

	}

	@Override
	public void endTurn() {
		this.currentPlayer = players.getNextPlayer();
	}

	@Override
	public void exitGame() {

	}

	@Override
	public boolean buyStreet() {
		Street currentStreet = (Street) field.getCurrentField(currentPlayer);
		if (currentStreet.getPriceForStreet() < currentPlayer.getBudget()) {
			currentPlayer.setBudget(currentPlayer.getBudget() - currentStreet.getPriceForStreet());
			currentStreet.setOwner(currentPlayer);
			currentPlayer.setOwnership(currentStreet);
			return true;
		}
		return false;

	}

	@Override
	public void checkFieldType() {
		field.getCurrentField(currentPlayer);
		// pseudo
		/*
		 * switsch fieldobject case street case ereignis case gemeinschaft case
		 * los case frei parken case gehe ins gef�ngnis case zu besuch im
		 * gef�ngnis case bahnhof case elektrowerk case wasserwerk case
		 * steuerfeld notifyObserver
		 */
	}

	@Override
	public void payRent() {
		Bank.payRent(currentPlayer, field.getCurrentField(currentPlayer));
		notifyObservers();

	}

	@Override
	public void receiveGoMoney() {
		Bank.receiveMoney(currentPlayer, IMonopolyUtil.LOS_MONEY);
		notifyObservers();
	}

	public PlayerController getPlayers() {
		return players;
	}

	public Playfield getField() {
		return field;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public List<String> getOptions() {

		List<String> options = new ArrayList<String>();
		
		options.add("d - W�rfeln");
		options.add("x - Beenden");
		options.add("b - Zug beenden");
		/**
		 * checkt optionen:
		 *  - case im Gef�ngnis
		 *  - case NICHT im Gef�gnis
		 */
		return options;
	}
	public String getMessage() {
		return this.message;
	}
	

}
