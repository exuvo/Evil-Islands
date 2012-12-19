package se.exuvo.evil.server.world.units;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import se.exuvo.evil.shared.ClassFinder;

import com.artemis.utils.Bag;

public class Template {
	private static final Logger log = Logger.getLogger(Template.class);
	private static Bag<UnitTemplate> templates = new Bag<UnitTemplate>();
	private static Map<String, UnitTemplate> templateMap = new HashMap<String, UnitTemplate>();

	public static void init() {
		templates.clear();
		log.debug("Loading unit templates");
		try {
			List<Class<?>> l = ClassFinder.getClasses("se.exuvo.mmo.server.units");
			for (Class<?> c : l) {
				if (UnitTemplate.class.isAssignableFrom(c) && !c.equals(UnitTemplate.class)) {
					try {
						Class<? extends UnitTemplate> cc = c.asSubclass(UnitTemplate.class);
						UnitTemplate p = cc.newInstance();
						if (p.getName() != null && !p.getName().equals("")) {
							templates.add(p);
							log.trace("Loaded command: " + p.getName());
						}
					} catch (Throwable e) {
						log.warn("Failed to load command: \"" + c.getSimpleName() + "\"", e);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			log.warn("Failed to load unit templates", e);
		} catch (IOException e) {
			log.warn("Failed to load unit templates", e);
		}
		
		for(UnitTemplate t : templates){
			templateMap.put(t.getName(), t);
		}
	}

	public static UnitTemplate getTemplate(String name){
		return templateMap.get(name);
	}
	
}
