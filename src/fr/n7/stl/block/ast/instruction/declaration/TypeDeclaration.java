package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for a type declaration. */
public class TypeDeclaration implements Declaration, Instruction {

	/** Name of the declared type */
	private String name;
	
	/** AST node for the type associated to the name */
	private Type type;

	/** Builds an AST node for a type declaration
	 * @param _name : Name of the declared type
	 * @param _type : AST node for the type associated to the name
	 */
	public TypeDeclaration(String _name, Type _type) {
		this.name = _name;
		this.type = _type;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.accepts(this)) return false;
		if (! type.resolve(scope)) return false;
		scope.register(this);
		return true;
	}

	/** Provide the type associated to a name in a type declaration.
	 * @return Type from the declaration.
	 */
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "typedef " + this.type + " " + this.name + ";\n";
	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public int allocateMemory(Register register, int offset) {
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		return factory.createFragment();
	}

	@Override
	public Type getReturnType() {
		return AtomicType.VoidType;
	}
}
