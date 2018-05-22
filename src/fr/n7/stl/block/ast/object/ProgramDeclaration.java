package fr.n7.stl.block.ast.object;

import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public abstract class ProgramDeclaration implements Declaration {
	
	protected ClassName className;
	
	protected List<TypeInstantiation> extendedClass;

    public abstract boolean resolve(HierarchicalScope<Declaration> _scope);

	public abstract boolean checkType();

	public abstract int allocateMemory(Register register, int offset);

	public abstract Fragment getCode(TAMFactory factory);

	public abstract Type getReturnType();
	
	public abstract String toString();
	
	public String getName() {
		return this.className.getName();
	}
	
	public Type getType() {
		throw new SemanticsUndefinedException("getType method not implemented yet in ProgramDeclaration");
	}
}
