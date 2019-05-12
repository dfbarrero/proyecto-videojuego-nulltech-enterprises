package state_machine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import constants.Constants;
import entities.Player;
import main.MainManager;
import maps.Map;
import resources.ResourceLoader;

public class MapState extends BasicGameState {
	
	private Map[] maps;
	private final int stateId;
	private final MainManager mainManager;
	private final MapGameState state;
	
	private Image playerOneTurn;
	private Image playerTwoTurn;
	
	public MapState(final int stateId, final MainManager mainManager) {
		this.stateId = stateId;
		this.mainManager = mainManager;
		
		state = new MapGameState();
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TEST
		mainManager.initGameMode(1);
		Player[] players = {new Player("Jugador 1", ResourceLoader.loadAnimationFromSpriteSheetUrl(Constants.PATH_MINIGAME_TEST_MONKEY, 220, 280, 10)), new Player("Jugador 2", ResourceLoader.loadAnimationFromSpriteSheetUrl(Constants.PATH_MINIGAME_TEST_BANANA, 220, 280, 10))};
		mainManager.initPlayers(players);
		mainManager.initGameState(mainManager.getGameMode(), mainManager.getPlayers());
		// TEST
		
		maps = new Map[1];
		maps[0] = new Map(new TiledMap("res/maps/test.tmx"));
		
		playerOneTurn = ResourceLoader.loadImageFromUrl(Constants.PATH_MAPSTATE_PLAYER_ONE);
		playerTwoTurn = ResourceLoader.loadImageFromUrl(Constants.PATH_MAPSTATE_PLAYER_TWO);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Map
		Map map = maps[mainManager.getGameState().getPlayerByTurn().getMapLocation().getMapId()];
		map.render(0, 0);
		
		// Player
		int squareId = mainManager.getGameState().getPlayerByTurn().getMapLocation().getSquareId();
		mainManager.getGameState().getPlayerByTurn().render(g, map.getTiledMap().getObjectX(0, squareId), map.getTiledMap().getObjectY(0, squareId));
		
		// Animations
		if (state.switchingTurnAnimation) {
			g.drawImage(mainManager.getGameState().getPlayerTurn() == 0 ? playerOneTurn : playerTwoTurn, 100, 100);
		} else if (state.loadingMapAnimation) {
			
		} else if (state.loadingMinigameAnimation) {
			
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		if (mainManager.getGameState().getFinishingTurn()) {
			
			if (mainManager.getGameState().nextTurn() == 0) {
				
				// LAUNCH MINIGAME
//				mainManager.enterState(3);
				System.out.println("MINIGAME");
				state.loadingMapAnimation = true;
				
			} else {
				
				// SWITCH PLAYER
				System.out.println("\n\tPlayer switch\n");
				state.switchingTurnAnimation = true;
				
			}
			
			mainManager.getGameState().setFinishingTurn(false);
			
		} else {
			
			mainManager.getGameState().getPlayerByTurn().update(state.switchingTurnAnimation || state.loadingMapAnimation, maps.length);
			
			if (state.switchingTurnAnimation && state.timeElapsed < 3000) {
				
				state.timeElapsed += delta;
				
			} else if (state.switchingTurnAnimation) {
				
				state.timeElapsed = 0;
				state.switchingTurnAnimation = false;
				
			}
			
		}
		
	}
	

	
	@Override
	public int getID() {
		return stateId;
	}
	
}

class MapGameState {
	boolean switchingTurnAnimation;
	boolean loadingMinigameAnimation;
	boolean loadingMapAnimation;
	int timeElapsed;
}