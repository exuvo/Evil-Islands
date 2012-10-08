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

public class Genius extends Unit {
	private SimpleDiagramRenderer d;
	
	@SuppressWarnings("unused")
	private Genius(){
		
	}
	
	public Genius(NetUnit u){
		super(u);
	}
	
	public void load(){
		try {
			InkscapeLoader.RADIAL_TRIANGULATION_LEVEL = 2;
			Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
			Renderer.setLineStripRenderer(Renderer.QUAD_BASED_LINE_STRIP_RENDERER);
			d = new SimpleDiagramRenderer(InkscapeLoader.load("data/images/grass.svg"));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.translate(getPosition().x, getPosition().y);
		//d.render(g);
		g.translate(-getPosition().x, -getPosition().y);
		super.draw(g);
	}

}
