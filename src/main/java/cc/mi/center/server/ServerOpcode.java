package cc.mi.center.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerOpcode {
	private final Set<Integer> set;
	
	public ServerOpcode() {
		this.set = new HashSet<>();
	}
	
	public void addOpcode(int opcode) {
		this.set.add(opcode);
	}
	
	public void addOpcodes(List<Integer> opcodes) {
		for (int opcode : opcodes) {
			this.addOpcode(opcode);
		}
	}
	
	public void remove(int opcode) {
		this.set.remove(opcode);
	}
	
	public boolean contains(int opcode) {
		return this.set.contains(opcode);
	}
}
