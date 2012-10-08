package se.exuvo.mmo.client.world.units;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.InkscapeLoader;
import org.newdawn.slick.svg.SimpleDiagramRenderer;

import se.exuvo.mmo.client.world.Unit;
import se.exuvo.mmo.shared.world.NetUnit;

public class Rat extends Unit {
	
	@SuppressWarnings("unused")
	private Rat(){
		
	}
	
	public Rat(NetUnit u){
		super(u);
	}
	
	public void load(){
		try {
			getAnimations().put("stand", new Image("data/images/Mutated_Rat.gif"));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
