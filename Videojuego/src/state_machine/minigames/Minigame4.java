package state_machine.minigames;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import controllers.KeyboardController;
import hud.EndMiniGameHud;
import main.MainManager;
import minigames.GameObject;
import minigames.MiniGameCurrentState;
import resources.ResourceLoader;

public class Minigame4 extends BasicGameState {

	/*
	 * Attributes
	 */
	private final int stateId;

	private final KeyboardController keyboard;

	private MiniGameCurrentState state;
	private final EndMiniGameHud endMiniGame;
	private final MainManager mainManager;

	private Image backgroundImage;
	private Animation playerImage1;
	private Animation playerImage2;
	private Animation bombImage;

	private int[] posArray = new int[] { 20, 360 };
	private GameObject player1;
	private GameObject player2;
	private GameObject bomb;

	private int spawnSpeed;
	private int xp1;
	private int xp2;
	private int elapsedTime;
	private int cuentaatras;
	private int fin_cuenta;

	private int saltopl1 = 0;
	private int saltopl2 = 0;
	private int portador_bombapl1, portador_bombapl2;
	private int caidapl1, caidapl2;

	/*
	 * Constructors
	 */
	public Minigame4(final int stateId, final MainManager mainManager) {
		this.stateId = stateId;
		keyboard = new KeyboardController(640);
		xp1 = 200;
		xp2 = 200;
		elapsedTime = 0;
		spawnSpeed = 50;
		cuentaatras = 0;
		fin_cuenta = 3500;
		portador_bombapl1 = 1;
		portador_bombapl2 = 0;
		caidapl1 = 0;
		caidapl2 = 0;
		this.mainManager = mainManager;
		state = new MiniGameCurrentState();
		endMiniGame = new EndMiniGameHud(mainManager, keyboard);
	}

	/*
	 * Init
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		backgroundImage = ResourceLoader.loadImageFromUrl("res/images/volcan3.png");
		playerImage1 = ResourceLoader.animationfromimage("res/images/prota_movimiento.png", 64, 64, 100);
		playerImage2 = ResourceLoader.animationfromimage("res/images/prota_movimiento.png", 64, 64, 100);
		bombImage = ResourceLoader.animationfromimage("res/images/bomb.png", 64, 64, 100);
		player1 = new GameObject(playerImage1, xp1, 360, 2f); // Set values as constants
		player2 = new GameObject(playerImage2, xp2, 20, 2f);
		bomb = new GameObject(bombImage, xp2, player2.getY() + 60, 0.7f);
		bomb.updateCurrentAnimation(0, 3, 0.7f);
		endMiniGame.init(gc);
	}

	/*
	 * Render
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		backgroundImage.draw();

		player1.render(g);
		player2.render(g);
		bomb.render(g);

		// State
		if (state.endMiniGameScreen) {
			endMiniGame.render(gc, g);
		} else if (state.gamePaused) {
			// TODO: Pausa
		}

	}

	/*
	 * Update
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (!state.gamePaused && !state.endMiniGameScreen) {
			movimientopl1(gc, delta);
			movimientopl2(gc, delta);
			desplazamientospl1(gc, delta);
			desplazamientospl2(gc, delta);
			animacion_salto(gc);

			if (keyboard.getPressedpl1(gc) == "Space" && saltopl1 == 0) {
				saltopl1 = 1;
				if (keyboard.previouspressedpl1 == "Right") {
					player1.updateCurrentAnimation(5, 9, 2f);
					player1.updateX(xp1 += 450 * delta / 50f);
					player1.getAnimation().stopAt(4);
				}
				if (keyboard.previouspressedpl1 == "Left") {
					player1.updateCurrentAnimation(10, 14, 2f);
					player1.updateX(xp1 -= 450 * delta / 50f);
					player1.getAnimation().stopAt(4);
				}
				if ((Math.sqrt(Math.pow(player2.getX() - player1.getX(), 2)
						+ Math.pow(player2.getY() - player1.getY(), 2)) < 64) && portador_bombapl1 == 1) {
					portador_bombapl1 = 0;
					portador_bombapl2 = 1;
					caidapl2 = 1;
				}
			}

			if (keyboard.getPressedpl2(gc) == "Zero" && saltopl2 == 0) {
				saltopl2 = 1;
				if (keyboard.previouspressedpl2 == "Right") {
					player2.updateCurrentAnimation(5, 9, 2f);
					player2.updateX(xp2 += 450 * delta / 50f);
					player2.getAnimation().stopAt(4);

				}
				if (keyboard.previouspressedpl2 == "Left") {
					player2.updateCurrentAnimation(10, 14, 2f);
					player2.updateX(xp2 -= 450 * delta / 50f);
					player2.getAnimation().stopAt(4);

				}
				if ((Math.sqrt(Math.pow(player2.getX() - player1.getX(), 2)
						+ Math.pow(player2.getY() - player1.getY(), 2)) < 64) && portador_bombapl2 == 1) {
					portador_bombapl1 = 1;
					portador_bombapl2 = 0;
					caidapl1 = 1;
				}

			}

			if (portador_bombapl2 == 1) {
				bomb.setX(player2.getX() + 32);
				bomb.setY(player2.getY() + 60);
			}
			if (portador_bombapl1 == 1) {
				bomb.setX(player1.getX() + 32);
				bomb.setY(player1.getY() + 60);
			}

			if (caidapl1 == 1) {
				player1.updateCurrentAnimation(30, 34, 2f);
				if (elapsedTime++ > spawnSpeed) {
					caidapl1 = 0;
					elapsedTime = 0;
				}
				//
			}
			if (caidapl2 == 1) {
				player2.updateCurrentAnimation(30, 34, 2f);
				if (elapsedTime++ > spawnSpeed) {
					caidapl2 = 0;
					elapsedTime = 0;
				}
				//
			}

			// FIN
			if (cuentaatras++ > fin_cuenta) {
				if (portador_bombapl2 == 1) {
					state.endMiniGameScreen = true;
					endMiniGame.setPlayerWinner(1);
				}
				if (portador_bombapl1 == 1) {
					state.endMiniGameScreen = true;
					endMiniGame.setPlayerWinner(2);
				}
			}
		} else if (state.endMiniGameScreen) {
			endMiniGame.update();
		} else {
			// TODO: PAUSE
		}
	}

	/*
	 * Getters
	 */
	@Override
	public int getID() {
		return stateId;
	}

	private void movimientopl1(GameContainer gc, int delta) {
		// Movimientos
		if (keyboard.getXMovementPl1() == 100 && saltopl1 == 0 && caidapl1 == 0) {
			player1.updateCurrentAnimation(15, 18, 2f);
			player1.updateX(xp1 += 100 * delta / 200f);
		}
		if (keyboard.getXMovementPl1() == -100 && saltopl1 == 0 && caidapl1 == 0) {
			player1.updateCurrentAnimation(25, 28, 2f);
			player1.updateX(xp1 -= 100 * delta / 200f);
		}
		if (keyboard.getXMovementPl1() == 0 && keyboard.getYMovementPl1() == 0 && saltopl1 == 0 && caidapl1 == 0) {
			player1.updateCurrentAnimation(20, 20, 2f);
		}
	}

	private void movimientopl2(GameContainer gc, int delta) {
		if (keyboard.getXMovementPl2() == 100 && saltopl2 == 0 && caidapl2 == 0) {
			player2.updateCurrentAnimation(15, 18, 2f);
			player2.updateX(xp2 += 100 * delta / 200f);
		}
		if (keyboard.getXMovementPl2() == -100 && saltopl2 == 0 && caidapl2 == 0) {
			player2.updateCurrentAnimation(25, 28, 2f);
			player2.updateX(xp2 -= 100 * delta / 200f);
		}
		if (keyboard.getXMovementPl2() == 0 && keyboard.getYMovementPl2() == 0 && saltopl2 == 0 && caidapl2 == 0) {
			player2.updateCurrentAnimation(20, 20, 2f);
		}
	}

	private void animacion_salto(GameContainer gc) {
		if (saltopl1 == 1 && player1.getAnimation().getFrame() == 4) {
			if (keyboard.getPressedpl1(gc) != "") {
				saltopl1 = 0;
			}
		}
		if (saltopl2 == 1 && player2.getAnimation().getFrame() == 4) {
			if (keyboard.getPressedpl2(gc) != "") {
				saltopl2 = 0;
			}
		}
	}

	private void desplazamientospl1(GameContainer gc, int delta) {
		// Desplazamientos al llegar al final
		if (player1.getX() > gc.getWidth() && player1.getY() == 360) {
			player1.setY(getRandom(posArray));
			player1.updateX(xp1 = 10);

		}
		if (player1.getX() < 0 && player1.getY() == 360) {
			player1.setY(20);
			player1.updateX(xp1 = 100);
		}
		if (player1.getX() > gc.getWidth() && player1.getY() == 20) {
			player1.setY(360);
			player1.updateX(xp1 = gc.getWidth() - 20);
		}
		if (player1.getX() < 0 && player1.getY() == 20) {
			player1.setY(360);
			player1.updateX(xp1 = gc.getWidth() - 20);
		}
	}

	private void desplazamientospl2(GameContainer gc, int delta) {
		if (player2.getX() > gc.getWidth() && player2.getY() == 360) {
			player2.setY(getRandom(posArray));
			player2.updateX(xp2 = 10);

		}
		if (player2.getX() < 0 && player2.getY() == 360) {
			player2.setY(20);
			player2.updateX(xp2 = 100);
		}
		if (player2.getX() > gc.getWidth() && player2.getY() == 20) {
			player2.setY(360);
			player2.updateX(xp2 = gc.getWidth() - 20);
		}
		if (player2.getX() < 0 && player2.getY() == 20) {
			player2.setY(360);
			player2.updateX(xp2 = gc.getWidth() - 20);
		}
	}

	public int getRandom(int[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

}
