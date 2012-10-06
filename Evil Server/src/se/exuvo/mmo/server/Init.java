package se.exuvo.mmo.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;

import se.exuvo.mmo.server.clients.Listener;
import se.exuvo.mmo.server.commands.Parser;
import se.exuvo.mmo.server.world.Planet;
import se.exuvo.mmo.shared.MinlogTolog4j;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.Switch;

/**
 * 
 * @author exuvo, scrypso
 *
 */
public class Init {
	protected static final Logger log = Logger.getLogger(Init.class);
	public static final long serialVersionUID = 1;;
	public static JSAPResult config = null;
	public static ShutDownHook shutdownhook = new ShutDownHook();
	public static Listener listener;
	
	
	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");
		log.fatal("### Starting ###");
		
		JSAP jsap = new JSAP();
        arguments(jsap);
        
        config = jsap.parse(args);
        // check whether the command line was valid, and if it wasn't, display usage information and exit.
        if (!config.success()) {
            System.err.println();
         // print out specific error messages describing the problems
            // with the command line, THEN print usage, THEN print full
            // help.  This is called "beating the user with a clue stick."
            for (Iterator<?> errs = config.getErrorMessageIterator();
                    errs.hasNext();) {
                System.err.println("Error: " + errs.next());
            }
            
            System.err.println();
            System.err.println("Usage: java "
                                + Init.class.getName());
            System.err.println("                "
                                + jsap.getUsage());
            System.err.println("All parameters override config settings");
            System.err.println();
            // show full help as well
            System.err.println(jsap.getHelp());
            System.exit(1);
        }
        
        new Settings(config);
        Runtime.getRuntime().addShutdownHook(shutdownhook);
        Parser.init();
        com.esotericsoftware.minlog.Log.setLogger(new MinlogTolog4j());
        com.esotericsoftware.minlog.Log.set( com.esotericsoftware.minlog.Log.LEVEL_WARN);
        Planet.init();
        
    	//Must maintain a reference to the RMI registered object for clients to be able to connect
    	//Else the GC will remove it -> NullPointerExceptions
		listener = new Listener();	
		if(!listener.start()){
			log.fatal("Unable to start listening. Exiting");
			System.exit(3);
		}
        
        if(Settings.getBol("console")){
	    	new Thread(){
	    		public void run(){
	    			Scanner s = new Scanner(System.in);
	        		
	            	String a;
	        		while(true){
	        			System.out.print("Enter a command: ");
	        			a = s.nextLine();
	        			System.out.println(new Parser().parse(a, null));
	        		}
	    		}
	    	}.start();
        }
	}
	
	private static final void arguments(JSAP jsap){
		List<Parameter> options = new ArrayList<Parameter>();
		
		Switch console = new Switch("console")
			.setShortFlag('c')
			.setLongFlag("console");
		options.add(
				console.setHelp("Enables the console.")
		);
		
		
		FlaggedOption port = new FlaggedOption("port")
			.setStringParser(JSAP.STRING_PARSER)
			.setDefault(JSAP.NO_DEFAULT)
			.setRequired(false)
			.setShortFlag('p')
			.setLongFlag("port");
		options.add(
				port.setHelp("Port to connect to.")
		);
		
		try {
			for(Parameter p : options){
				jsap.registerParameter(p);
			}
		} catch (JSAPException e) {
			log.warn("JSAP: Failed to register parameters due to: " + e);
		}
	}

}

class ShutDownHook extends Thread{
	
	public ShutDownHook(){
		super();
	}
	
	public void run(){
		try{
			Settings.save();
		}catch(Throwable ignore){
			
		}
	}
}

