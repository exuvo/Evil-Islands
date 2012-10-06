package se.exuvo.evil.shared.world;


public class NetUnit extends NetMovable{
	private NetAbility attack;
	
	public NetUnit(){
		super();
	}

	public void setAttack(NetAbility attack) {
		this.attack = attack;
	}

	public NetAbility getAttack() {
		return attack;
	}
	

}