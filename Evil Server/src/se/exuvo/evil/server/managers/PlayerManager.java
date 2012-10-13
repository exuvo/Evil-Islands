package se.exuvo.evil.server.managers;

import java.util.HashMap;
import java.util.Map;

import se.exuvo.evil.server.clients.Player;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;


/**
 * You may sometimes want to specify to which player an entity belongs to.
 * 
 * An entity can only belong to a single player at a time.
 * 
 * @author Arni Arent, exuvo
 *
 */
public class PlayerManager extends Manager {
	private Map<Entity, Player> playerByEntity;
	private Map<Player, Bag<Entity>> entitiesByPlayer;

	public PlayerManager() {
		playerByEntity = new HashMap<Entity, Player>();
		entitiesByPlayer = new HashMap<Player, Bag<Entity>>();
	}
	
	public void setPlayer(Entity e, Player player) {
		playerByEntity.put(e, player);
		Bag<Entity> entities = entitiesByPlayer.get(player);
		if(entities == null) {
			entities = new Bag<Entity>();
			entitiesByPlayer.put(player, entities);
		}
		entities.add(e);
	}
	
	public ImmutableBag<Entity> getEntitiesOfPlayer(Player player) {
		Bag<Entity> entities = entitiesByPlayer.get(player);
		if(entities == null) {
			entities = new Bag<Entity>();
		}
		return entities;
	}
	
	public void removeFromPlayer(Entity e) {
		Player player = playerByEntity.get(e);
		if(player != null) {
			Bag<Entity> entities = entitiesByPlayer.get(player);
			if(entities != null) {
				entities.remove(e);
			}
		}
	}
	
	public Player getPlayer(Entity e) {
		return playerByEntity.get(e);
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void deleted(Entity e) {
		removeFromPlayer(e);
	}

}
