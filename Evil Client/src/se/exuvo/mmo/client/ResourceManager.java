package se.exuvo.mmo.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

import se.exuvo.mmo.client.gui.Loading;


/**
 * This class provide access to all resources contents in a single jar.
 * 
 * Use the init() method with the jar path location to initialize the resources,
 * then the loadNextResource() and isLoadComplete() to load data progressively.
 * 
 * After loading you can use data with getters method.
 * 
 * Example: SpriteSheet sheet = Resources.getSprite("mySpriteSheet");
 * 
 * /resources /fonts /images /maps /musics /sounds /sprites /systems
 * 
 * @author Vincent PIRAULT
 * @author exuvo
 * 
 */
public class ResourceManager implements ResourceLocation{

	private static final boolean FONT_WITH_CACHE = false;
	private static final Logger log = Logger.getLogger(Loading.class);
	private static final String regexFileSeparator = File.separator.equals("/") ? "\\"+ File.separator:File.separator;

	private static HashMap<String, AngelCodeFont> fonts = new HashMap<String, AngelCodeFont>();
	private static HashMap<String, Image> images = new HashMap<String, Image>();
	private static HashMap<String, Music> musics = new HashMap<String, Music>();
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private static HashMap<String, SpriteSheet> sprites = new HashMap<String, SpriteSheet>();
	private static HashMap<String, String> systemsAndEmitters = new HashMap<String, String>();

	private static LoadingList list;
	private static int progress;

	/**
	 * Initialize the content of the resources list.
	 * 
	 * @param jarLocation
	 *            The location of the jar that contain resources.
	 * @throws IOException
	 *             If the jar location is false.
	 * @throws SlickException
	 *             If a resource can't be loaded from the jar.
	 */
	public static void init(String jarLocation) throws SlickException {
		LoadingList.setDeferredLoading(true);
		list = LoadingList.get();
		
		JarFile jarFile;
		try {
			jarFile = new JarFile(jarLocation);
		
			Enumeration<JarEntry> e = jarFile.entries();
			while (e.hasMoreElements()) {
				JarEntry je = e.nextElement();
				String path = je.getName();
				addResource(path);
			}
		} catch (IOException e) {
			log.warn("Resource Jar not found");
		}
		
		File data = new File("data"+File.separator);
		if(data.exists()){
			for(File f : getAllFiles(data)){
				if(! f.getPath().contains(".svn")){
					addResource(f.getPath());
				}
			}
		}else{
			log.warn("Data folder not found");
		}
		
		ResourceLoader.addResourceLocation(new ResourceManager());
	}
	
	public static List<File> getAllFiles(File ff){
		List<File> files = new ArrayList<File>();
		for(File f : ff.listFiles()){
			if(f.isDirectory()){
				files.addAll(getAllFiles(f));
			}else{
				files.add(f);
			}
		}
		return files;
	}
	
	public static void addResource(String path){
		String folder = path.substring(path.indexOf(File.separator)+1, path.indexOf(File.separator, path.indexOf(File.separator)+1));
		String name = path.substring(path.indexOf(File.separator)+1).replace("\\", "/");
		//log.debug("Adding Resource: " + folder + ":" + name);
		
		if(folder.equalsIgnoreCase("music")){
			list.add(new DeferredMusic(name, path));
			
		}else if(folder.equalsIgnoreCase("fonts")){
			list.add(new DeferredFont(name, path));
			
		}else if(folder.equalsIgnoreCase("sounds")){
			list.add(new DeferredSound(name, path));
			
		}else if(folder.equalsIgnoreCase("images") && !name.endsWith(".svg")){
			list.add(new DeferredImage(name, path));
			
		}else if(folder.equalsIgnoreCase("sprites")){
			list.add(new DeferredSprite(name, path, 1, 1));
			
		}else if(folder.equalsIgnoreCase("particle systems")){
			systemsAndEmitters.put(name, path);
			
		}

	}

	/**
	 * Load the next resource of the list.
	 */
	public static void loadNextResource() {
		if (LoadingList.get().getRemainingResources() > 0) {
			DeferredResource nextResource = LoadingList.get().getNext();
			try {
				//log.debug("LL: " + nextResource.getDescription());
				nextResource.load();
			} catch (IOException e) {
				log.error("Error while loading resource: " + nextResource.getDescription(), e);
			}
		}
		if (LoadingList.get().getTotalResources() > 0){
			progress = (100 * (LoadingList.get().getTotalResources() -
					LoadingList.get().getRemainingResources())) / LoadingList.get().getTotalResources();
		}
	}

	/**
	 * Check if there is no more remaining resource to load.
	 * 
	 * @return true if there is no more resource to load, false otherwise.
	 */
	public static boolean isLoadComplete() {
		return LoadingList.get().getRemainingResources() == 0;
	}

	/**
	 * The advancement of the resource list loading in percentage.
	 * 
	 * @return the advancement in percentage ( 0 - 100 ).
	 */
	public static int getProgress() {
		return progress;
	}

	/*
	 * Delegate Getters/Setters to access data by name.
	 */

	/**
	 * Get a font from the resources.
	 * 
	 * @param name
	 *            the name of the font.
	 * @return the requested font or null if the resource was not found.
	 */
	public static AngelCodeFont getFont(String name) {
		AngelCodeFont f = fonts.get(name);
		if(f == null){
			log.warn("Requested AngelCodeFont not found: " + name, new Exception());
		}
		return f;
	}

	/**
	 * Get an image from the resources.
	 * 
	 * @param name
	 *            the name of the image.
	 * @return the requested image or null if the resource was not found.
	 */
	public static Image getImage(String name) {
		Image i = images.get(name);
		if(i == null){
			log.warn("Requested Image not found: " + name, new Exception());
		}
		return i;
	}

	/**
	 * Get a music from the resources.
	 * 
	 * @param name
	 *            the name of the music.
	 * @return the requested music or null if the resource was not found.
	 */
	public static Music getMusic(String name) {
		Music m = musics.get(name);
		if(m == null){
			log.warn("Requested Music not found: " + name, new Exception());
		}
		return m;
	}

	/**
	 * Get a sound from the resources.
	 * 
	 * @param name
	 *            the name of the sound.
	 * @return the requested sound or null if the resource was not found.
	 */
	public static Sound getSound(String name) {
		Sound s = sounds.get(name);
		if(s == null){
			log.warn("Requested Sound not found: " + name, new Exception());
		}
		return s;
	}

	/**
	 * Get a sprite from the resources.
	 * 
	 * @param name
	 *            the name of the sprite.
	 * @return the requested sprite or null if the resource was not found.
	 */
	public static SpriteSheet getSpriteSheet(String name) {
		SpriteSheet s = sprites.get(name);
		if(s == null){
			log.warn("Requested SpriteSheet not found: " + name, new Exception());
		}
		return s;
	}

	/**
	 * Get a particle system from the resources.
	 * 
	 * @param name
	 *            the name of the particle system.
	 * @return the requested particle system or null if the resource was not
	 *         found.
	 * @throws IOException
	 *             If the system can't be loaded.
	 */
	public static ParticleSystem getSystem(String name){
		ParticleSystem s = null;
		try {
			s = ParticleIO.loadConfiguredSystem(systemsAndEmitters.get(name));
		} catch (Exception e) {
			log.warn("Requested ParticleSystem failed to load: " + name, e);
		}
		return s;
	}

	/**
	 * Get a particle emitter from the resources.
	 * 
	 * @param name
	 *            the name of the particle emitter.
	 * @return the requested particle emitter or null if the resource was not
	 *         found.
	 * @throws IOException
	 *             If the emitter can't be loaded.
	 */
	public static ConfigurableEmitter getEmitter(String name){
		
		ConfigurableEmitter e = null;
		try {
			 e = ParticleIO.loadEmitter(systemsAndEmitters.get(name));
		} catch (IOException ex) {
			log.warn("Requested ConfigurableEmitter failed to load: " + name, ex);
		}
		return e;
	}

	/*
	 * Useful classes to load deferred resources.
	 */

	private static class DeferredFont implements DeferredResource {

		private String name;
		private String path;

		public DeferredFont(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred font";
		}

		public void load() throws IOException {
			try {
				AngelCodeFont font = new AngelCodeFont(name, Thread.currentThread().getContextClassLoader().getResourceAsStream(path + ".fnt"), Thread
						.currentThread().getContextClassLoader().getResourceAsStream(path + ".png"), FONT_WITH_CACHE);
				ResourceManager.fonts.put(name, font);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	private static class DeferredImage implements DeferredResource {

		private String name;
		private String path;

		public DeferredImage(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.images.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	private static class DeferredSprite implements DeferredResource {

		private String name;
		private String path;
		private int tileWidth;
		private int tileHeight;

		public DeferredSprite(String name, String path, int tileWidth, int tileHeight) {
			this.name = name;
			this.path = path;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public String getDescription() {
			return "Deferred sprite";
		}

		public void load() throws IOException {
			try {
				ResourceManager.sprites.put(name, new SpriteSheet(name, Thread.currentThread().getContextClassLoader().getResourceAsStream(path), tileWidth,
						tileHeight));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	private static class DeferredSound implements DeferredResource {

		private String name;
		private String path;

		public DeferredSound(String name, String path) {
			super();
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred sound";
		}

		public void load() throws IOException {
			try {
				ResourceManager.sounds.put(name, new Sound(path));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	private static class DeferredMusic implements DeferredResource {

		private String name;
		private String path;

		public DeferredMusic(String name, String path) {
			super();
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred music";
		}

		public void load() throws IOException {
			try {
				ResourceManager.musics.put(name, new Music(path));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public URL getResource(String ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String ref) {
		// TODO Auto-generated method stub
		return null;
	}

}
