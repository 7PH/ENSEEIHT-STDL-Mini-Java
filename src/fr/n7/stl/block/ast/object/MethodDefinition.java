package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.Block;

public class MethodDefinition extends Definition {

	private Signature signature;
	
	private Block body;
	
	private boolean abstractArgument;
	
	public MethodDefinition(Signature _signature, Block _body, boolean _abstractArgument) {
		this.signature = _signature;
		this.body = _body;
		this.abstractArgument = _abstractArgument;
	}
}
