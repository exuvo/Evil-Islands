package se.exuvo.mmo.shared.world;

import com.esotericsoftware.kryo.NotNull;

public class NetOrder{
	private @NotNull NetAbility ability;
	private Position targetPos;
	private long targetUnitId;
	private @NotNull long casterId;
	
	@SuppressWarnings("unused")
	private NetOrder(){
	}
	
	public NetOrder(long casterId, NetAbility ability, Position pos){
		setAbility(ability);
		setTargetPos(pos);
		setCasterId(casterId);
	}
	
	public NetOrder(long casterId, NetAbility ability, long targetId){
		setAbility(ability);
		setTargetUnitId(targetId);
		setCasterId(casterId);
	}

	public void setTargetUnitId(long targetUnit) {
		this.targetUnitId = targetUnit;
	}

	public long getTargetUnitId() {
		return targetUnitId;
	}

	public void setTargetPos(Position targetPos) {
		this.targetPos = targetPos;
	}

	public Position getTargetPos() {
		return targetPos;
	}

	public void setAbility(NetAbility ability) {
		this.ability = ability;
	}

	public NetAbility getAbility() {
		return ability;
	}

	public void setCasterId(long casterId) {
		this.casterId = casterId;
	}

	public long getCasterId() {
		return casterId;
	}

}
