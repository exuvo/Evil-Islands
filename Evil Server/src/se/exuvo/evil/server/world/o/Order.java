package se.exuvo.evil.server.world.o;


import org.apache.log4j.Logger;

import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.world.Planet;
import se.exuvo.evil.server.world.Square;
import se.exuvo.evil.server.world.o.abilities.Ability;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.NetOrder;
import se.exuvo.evil.shared.world.Position;

public class Order {
	private static final Logger log = Logger.getLogger(Order.class);
	private Ability ability = null;
	private Position targetPos = null;
	private Entity targetUnit, caster;
	private Client issuer = null;
	
	public Order(Ability ability, Position pos, Entity caster){
		setAbility(ability);
		setTargetPos(pos);
		setCaster(caster);
	}
	
	public Order(Ability ability, Entity target, Entity caster){
		setAbility(ability);
		setCaster(caster);
		targetUnit = target;
	}
	
	public boolean isValidB(){
		try{
			isValid();
			return true;
		}catch(InvalidOrderException e){
			return false;
		}
	}
	
	public void isValid() throws InvalidOrderException{
		if(caster == null){
			throw new InvalidOrderException("Invalid caster");
		}
		if(!caster.isAlive()){
			throw new InvalidOrderException("Caster is dead");
		}
		
		if(caster.getAbility(getAbility().getClass()) == null)
			throw new InvalidOrderException("Caster doesn't have ability "+getAbility());
			
		if(targetPos == null){
			if(targetUnit == null){
				throw new InvalidOrderException("Invalid target entity");
			}
			if(caster.getZone() != targetUnit.getZone()){
				throw new InvalidOrderException("Caster and target Entity are not in the same zone!");
			}
		}else{
			if(targetPos.getX() < 0 || targetPos.getY() < 0 || 
					targetPos.getY() > caster.getZone().getSquares().length*Square.size ||
					targetPos.getX() > caster.getZone().getSquares()[0].length*Square.size ){
				throw new InvalidOrderException("Target position is outside zone!");
			}
		}
		
		synchronized(caster.getZone().getLock()){
			if(targetPos == null){
				getAbility().validate(caster, targetUnit);
			}else{
				getAbility().validate(caster, getTargetPos());
			}
		}
	}
	
	public void use() throws InvalidOrderException{
		isValid();
		log.debug("Order \"" + getAbility().getName() + "\": Casting..");
		
		if(!isInRange()){
			throw new InvalidOrderException("Too far away");
		}
		
		synchronized(caster.getZone().getLock()){
			if(targetPos == null){
				log.trace("IssueOrder Entity");
				
				getAbility().setCasting();
				getAbility().cast(caster, targetUnit);
			}else{
				log.trace("IssueOrder Position");
				getAbility().setCasting();
				getAbility().cast(caster, getTargetPos());
			}
		}
	}
	
	public Order(NetOrder o) throws InvalidOrderException{
		setTargetUnit(Planet.getEntityById(o.getTargetUnitId()));
		setTargetPos(o.getTargetPos());
		setCaster(Planet.getEntityById(o.getCasterId()));
		if(getCaster() == null){
			throw new InvalidOrderException("Caster not found: " + o.getCasterId());
		}
		for(Ability a : getCaster().getAbilities()){
			if(a.getId().equals(o.getAbility().getId()) && a.getName().equals(o.getAbility().getName())){
				setAbility(a.clone());
				break;
			}
		}
		if(getAbility() == null){
			throw new InvalidOrderException("Ability not found on caster: " + o.getAbility().getId());
		}
	}
	
	public boolean isInRange(){		
		if(getAbility().getRange() == -1){
			return true;
		}
		if(getCaster().getPosition().distance(getTargetPos()) < getAbility().getRange()){
			return true;
		}else{
			return false;
		}
	}
	
	public void setTargetUnit(Entity e){
		this.targetUnit=e;
	}

	public void setTargetUnit(long targetUnitId) {
		this.targetUnit = Planet.getEntityById(targetUnitId);
	}

	public Entity getTargetUnit() {
		return targetUnit;
	}

	public void setTargetPos(Position targetPos) {
		this.targetPos = targetPos;
	}

	/**
	 * Returns targetPos if not null.
	 * Else returns position of targetUnit.
	 * @return
	 */
	public Position getTargetPos() {
		if(targetPos == null){
			return getTargetUnit().getPosition();
		}
		return targetPos;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}

	public Ability getAbility() {
		return ability;
	}

	public void setCaster(Entity e) {
		this.caster = e;
	}
	
	public Entity getCaster(){
		return caster;
	}

	public void setIssuer(Client issuer) {
		this.issuer = issuer;
	}

	public Client getIssuer() {
		return issuer;
	}

	public boolean isCasting(){
		return ability.isCasting();
	}
	
	public boolean isCasted(){
		return ability.isCasted();
	}
	
}
