package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/** ABSTRACT Syntax Tree node for a variable declaration instruction. */
public class VariableDeclaration implements DeclarationWithOffset, Instruction {

	/** Name of the declared variable. */
	protected String name;
	
	/** AST node for the type of the declared variable. */
	protected Type type;
	
	/** AST node for the initial value of the declared variable. */
	protected Expression value;
	
	/** Address register that contains the base address used to store the declared variable. */
	protected Register register;
	
	/** Offset from the base address used to store the declared variable
	 * i.e. the size of the memory allocated to the previous declared variables
	 */
	protected int offset;
	
	/**
	 * Creates a variable declaration instruction node for the ABSTRACT Syntax Tree.
	 * @param name Name of the declared variable.
	 * @param type AST node for the type of the declared variable.
	 * @param value AST node for the initial value of the declared variable.
	 */
	public VariableDeclaration(String name, Type type, Expression value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.type + " " + this.name + " = " + this.value + ";\n";
	}

	/**
	 * Synthesized semantics attribute for the type of the declared variable.
	 * @return Type of the declared variable.
	 */
	public Type getType() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see fr.n7.block.ast.VariableDeclaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Synthesized semantics attribute for the register used to compute the address of the variable.
	 * @return Register used to compute the address where the declared variable will be stored.
	 */
	public Register getRegister() {
		return this.register;
	}
	
	/**
	 * Synthesized semantics attribute for the offset used to compute the address of the variable.
	 * @return Offset used to compute the address where the declared variable will be stored.
	 */
	public int getOffset() {
		return this.offset;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
    @Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.accepts(this)) {
			Logger.error("The variable " + this.name + " is already declared in this block.");
			return false;
		}
		if (! type.resolve(scope)) {
			Logger.error("Could not resolve the type " + type + " of the declared variable " + name);
			return false;
		}
		if (! value.resolve(scope)) {
			Logger.error("Could not resolve the value of the declared variable " + name);
			return false;
		}
	    scope.register(this);
	    return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		if (! value.getType().compatibleWith(type))
			throw new RuntimeException("Value type : " + value.getType().toString() + " is not compatible with " + type.toString());
        return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register register, int offset) {
	    this.register = register;
		this.offset = offset;
		return value.getType().length();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        return value.getCode(factory);
	}

	@Override
	public Type getReturnType() {
		return AtomicType.VoidType;
	}

}
