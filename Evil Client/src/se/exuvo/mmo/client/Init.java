package se.exuvo.mmo.client;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.newdawn.slick.loading.LoadingList;

import se.exuvo.mmo.client.commands.Parser;
import se.exuvo.mmo.client.connection.Connectionn;
import se.exuvo.mmo.client.gui.GUI;
import se.exuvo.mmo.shared.MinlogTolog4j;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

public class Init {
	protected static final Logger log = Logger.getLogger(Init.class);
	public static final long serialVersionUID = 1;;
	public static JSAPResult config = null;
	private static GUI gui;
	public static ShutDownHook shutdownhook = new ShutDownHook();
	public static Connectionn connection = new Connectionn();
	
	
	/**
	 * @param args
	 * @return 	1=Invalid Params,
	 * 			2=Invalid Config,
	 * 		 	3=Unable to autoConnect,
	 * 			4=Failed to find UI xml 
	 * 			5=GUI died
	 */
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
        
        if(config.getBoolean("test")){
        	//net.java.games.input.test.ControllerReadTest.main(null);
        	return;
        }else if(config.getBoolean("list")){
        	//net.java.games.input.test.ControllerTextTest.main(null);
        	return;
        }else{
        	if(Settings.getBol("autoConnect")){
        		try {
					connection.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(3);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(3);
				}
        	}
        }
        
        gui = new GUI();
	}
	
	public static GUI getGUI(){
		return gui;
	}
	
	private static final void arguments(JSAP jsap){
		
		Switch list = new Switch("list")
			.setShortFlag('l')
			.setLongFlag("list");
		list.setHelp("List controllers.");
		
		Switch test = new Switch("test")
			.setShortFlag('t')
			.setLongFlag("test");
		test.setHelp("Open test windows.");
		
		FlaggedOption port = new FlaggedOption("port")
			.setStringParser(JSAP.STRING_PARSER)
			.setDefault(JSAP.NO_DEFAULT)
			.setRequired(false)
			.setShortFlag('p')
			.setLongFlag("port");
		port.setHelp("Port to connect to.");
		
		FlaggedOption server = new FlaggedOption("server")
			.setStringParser(JSAP.STRING_PARSER)
			.setDefault(JSAP.NO_DEFAULT)
			.setRequired(false)
			.setShortFlag('s')
			.setLongFlag("server");
		server.setHelp("Server to connect to.");
		
		try {
			jsap.registerParameter(list);
			jsap.registerParameter(test);
			jsap.registerParameter(port);
			jsap.registerParameter(server);
		} catch (JSAPException e) {
			log.warn("JSAP: Failed to register parameters due to: " + e);
		}
	}

}

class ShutDownHook extends Thread{
	public ShutDownHook(){}
	
	public void run(){
		Settings.save();
	}
}

