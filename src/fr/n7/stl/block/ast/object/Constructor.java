package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class Constructor extends Definition {
	
	private String ident;
	
	private List<ParameterDeclaration> parameters;
	
	private Block body;
	
	public Constructor(String _ident, Block _body) {
		this.ident = _ident;
		this.parameters = new LinkedList<ParameterDeclaration>();
		this.body = _body;
	}
	
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

	@Override
	public boolean checkType() {
		boolean b = false;
		if (this.getDefinitionModifier() != null) {
			Logger.error("Constructor " + this.ident + " has a definition modifier.");
		}
		b &= this.body.checkType();
		if (!b) {
			Logger.error("Body of Constructor " + this.ident + this.parameters.toString() + " is not well typed.");
		}
		for (ParameterDeclaration p : this.parameters) {
			b &= !(p.getType().equalsTo(AtomicType.ErrorType));
			if (!b) {
				Logger.error("Parameter : " + p.toString() + " (in Constructor " + this.ident + ") is considered as ErrorType");
			}
		}
		return b;
	}

	@Override
	public int allocateMemory(Register register, int offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for Constructor.");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for Constructor.");
	}

	@Override
	public Type getReturnType() {
    	throw new SemanticsUndefinedException("getReturnType method is undefined for Constructor.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
    	throw new SemanticsUndefinedException("resolve method is undefined for Constructor.");
	}
	
	public String toString() {
		throw new SemanticsUndefinedException("toString method is undefined for Constructor.");
	}
}
