package fr.n7.stl.block.ast.object;

import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.Type;

public class Constructor extends Definition {
	
	private String ident;
	private List<ParameterDeclaration> parameters;
	private Block body;
	
	public Constructor(String _ident, List<ParameterDeclaration> _parameters, Block _body) {
		this.ident = _ident;
		this.parameters = _parameters;
		this.body = _body;
	}

	@Override
	public String getName() {
		return this.ident;
	}

	@Override
	public Type getType() {
		return null;
	}

}
