package se.exuvo.evil.server.world;

/**
 * TODO I suggest Research should
 * work like some sort of advanced map.
 *
 * More than one system may access it and
 * there's also the issue of sharing the research
 * between client-server
 * As such some sort of centrally managed map may be the best.
 * 
 * It would probably be easiest to
 * describe progress as a 0-100 int-scale,
 *
 * Most research will probably just need
 * tp be researched (1) or not (0).
 * But some research may want multiple levels:
 * (laser mark I, II, II, etc.)
 * Therefore I suggest keeping track
 * of the players overall progress either with
 * an int (supports both boolean and tiers)
 * or a bunch of components.
 *
 */
public class ResearchMap {
    private int playerNameHash;
    private List<ResearchAble> researches;
    
    
    
    public ResearchAble getByName(String name);
    
    public ResearchAble get(String name);
    
    public ResearchAble add(ResearchAble researchAble);
}