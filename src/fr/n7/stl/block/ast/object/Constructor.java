package fr.n7.stl.block.ast.object;

import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;

public class Constructor {
	
	private String ident;
	private List<ParameterDeclaration> parameters;
	private Block body;
	
	public Constructor(String _ident, List<ParameterDeclaration> _parameters, Block _body) {
		this.ident = _ident;
		this.parameters = _parameters;
		this.body = _body;
	}

}
