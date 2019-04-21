package state_machine.minigames;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import constants.Constants;
import controllers.KeyboardController;
import minigames.GameObject;
import resources.ResourceLoader;

public class MiniGameJumpRollers extends BasicGameState {
	
	/*
	 * Attributes
	 */
	private final int stateId;
	
	private final KeyboardController keyboard;
	private final ResourceLoader resLoader;
	
	// Images
	private Image backgroundImage;
	private Image overlayImage;
	private Image rollerImage;
	private Image monkeyImage;

	// Rollers
	private ArrayList<GameObject> arrayRollers1;
	private ArrayList<GameObject> arrayRollers2;
	
	// Players
	private GameObject player1;
	private GameObject player2;
	
	private int speedDificulty;
	
	
	/*
	 * Constructors
	 */
	public MiniGameJumpRollers(final int stateId, final ResourceLoader resLoader) {
		this.stateId = stateId;
		this.resLoader = resLoader;
		
		keyboard = new KeyboardController(640); 
		
		arrayRollers1 = new ArrayList<GameObject>();
		arrayRollers2 = new ArrayList<GameObject>();

		speedDificulty = 3;
	}
	

	/*
	 * Init
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		backgroundImage = new Image(Constants.PATH_MINIGAME_TEST_BACKGROUND);
		overlayImage = new Image(Constants.PATH_MINIGAME_JUMP_ROLLERS_OVERLAY);
		rollerImage = resLoader.loadImageFromUrl(Constants.PATH_MINIGAME_JUMP_ROLLERS_ROLLER);
		monkeyImage = resLoader.loadImageFromUrl(Constants.PATH_MINIGAME_TEST_MONKEY);
		player1 = new GameObject(monkeyImage, (int) (Constants.WINDOW_DEFAULT_WIDTH * 0.1), (int) (Constants.WINDOW_DEFAULT_HEIGHT * 0.45 - monkeyImage.getHeight() * 0.25), 0.25f); // Set values as constants
		player2 = new GameObject(monkeyImage, (int) (Constants.WINDOW_DEFAULT_WIDTH * 0.1), (int) (Constants.WINDOW_DEFAULT_HEIGHT * 0.95 - monkeyImage.getHeight() * 0.25), 0.25f); // Set values as constants
		
//		addRollersToArrays(20);
	}

	/*
	 * Render
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Below player
		backgroundImage.draw();
		
		// Player level
		for (GameObject go : arrayRollers1) { go.render(g); }
		for (GameObject go : arrayRollers2) { go.render(g); }
		
		player1.render(g);
		player2.render(g);
		
		// Above player
		overlayImage.draw(0, 0, Constants.WINDOW_DEFAULT_WIDTH, Constants.WINDOW_DEFAULT_HEIGHT);
	}

	/*
	 * Update
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		player1.updateY((int) (player1.getY() + keyboard.getYMovement() * delta / 200f)); // Set values as constants
		
		for (GameObject go : (ArrayList<GameObject>) arrayRollers1.clone()) {
			go.updateXByIncrease(-speedDificulty);
			if (player1.getCollisionBox().intersects(go.getCollisionBox())) {
				// END GAME
			} else if (go.getX() < -go.getImage().getWidth() * go.getScale()) {
				go.setDeleted(true);
				arrayRollers1.remove(go);
			}
		}
		
		for (GameObject go : (ArrayList<GameObject>) arrayRollers2.clone()) {
			go.updateXByIncrease(-speedDificulty);
			if (player2.getCollisionBox().intersects(go.getCollisionBox())) {
				// END GAME
			} else if (go.getX() < -go.getImage().getWidth() * go.getScale()) {
				go.setDeleted(true);
				arrayRollers2.remove(go);
			}
		}

		if (arrayRollers1.size() < 10) { addRollersToArrays(20); }
		
	}
	
	/*
	 * Create Rollers
	 */
	private void addRollersToArrays(final int numberOfRollers) {
		for (int i = 0; i < numberOfRollers; i++) {
			final int posX = (arrayRollers1.size() > 0 ? arrayRollers1.get(arrayRollers1.size() - 1).getX() : Constants.WINDOW_DEFAULT_WIDTH) + ThreadLocalRandom.current().nextInt(100, 500);
			arrayRollers1.add(new GameObject(rollerImage, posX, (int) (Constants.WINDOW_DEFAULT_HEIGHT * 0.45 - monkeyImage.getHeight() * 0.25), 0.5f));
			arrayRollers2.add(new GameObject(rollerImage, posX, (int) (Constants.WINDOW_DEFAULT_HEIGHT * 0.95 - monkeyImage.getHeight() * 0.25), 0.5f));			
		}
	}
	

	/*
	 * Getters
	 */
	@Override
	public int getID() {
		return stateId;
	}

}
