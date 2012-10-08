package se.exuvo.evil.server.world.systems;

import java.util.HashMap;
import java.util.Map;

import se.exuvo.evil.server.world.components.PowerConsumer;
import se.exuvo.evil.server.world.components.PowerGenerator;
import se.exuvo.evil.server.world.components.PowerStorage;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.PlayerManager;
import com.artemis.systems.IntervalEntityProcessingSystem;

public class PowerSystem extends IntervalEntityProcessingSystem {
	private @Mapper ComponentMapper<PowerGenerator> gm;
	private @Mapper ComponentMapper<PowerConsumer> cm;
	private @Mapper ComponentMapper<PowerStorage> sm;
	private Map<Integer, PlayerPowerStatus> totals = new HashMap<Integer, PlayerPowerStatus>();
	private PlayerManager pm = world.getManager(PlayerManager.class);
	
	
	@SuppressWarnings("unchecked")
	public PowerSystem() {
		super(Aspect.getAspectForOne(PowerGenerator.class, PowerConsumer.class, PowerStorage.class), 1.0f);
	}

	@Override
	protected void process(Entity e) {
		int playerHash = pm.getPlayer(e).hashCode();
		
		PlayerPowerStatus total = totals.get(playerHash);
		
		// getSafe returns null if the entity doesn't have that component.
		PowerGenerator gen = gm.getSafe(e);
		PowerConsumer con = cm.getSafe(e);
		PowerStorage sto = sm.getSafe(e);
		
		
		if (gen != null) {
			total.generated += gen.getGeneration();			
		}
		
		if (con != null) {
			total.consumed += con.getEnergyConsumed();
		}
		
		
		if (con != null) {
			total.stored += sto.getStoredEnergy();
		}
	}
	
	
	public PlayerPowerStatus getPowerStatus(int playerHash) {
		return totals.get(playerHash);
	}
	
	public PlayerPowerStatus getPowerStatus(String player) {
		return getPowerStatus(player.hashCode());
	}
	
	
	public static class PlayerPowerStatus {
		public int stored, generated, consumed;
	}

}