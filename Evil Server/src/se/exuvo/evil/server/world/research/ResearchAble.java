package se.exuvo.evil.server.world.research;


public interface ResearchAble {
    
    public String getName(); // TODO hash on server, String on client?
    
    public String getDescription();
    
    // TODO OBS: client-side only
    //public Image getIcon(); // TODO type
    
    
}