package se.exuvo.evil.server.world.components;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

import com.artemis.Component;
import com.artemis.Entity;

public class PathComponent extends Component implements Mover {
	private Entity owner;//Ugly but unsure how to do this otherwise
	private Path path;

	public Entity getOwner() {
		return owner;
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

}
