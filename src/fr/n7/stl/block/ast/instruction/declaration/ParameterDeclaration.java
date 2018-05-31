package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.object.MethodDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Register;

/** ABSTRACT Syntax Tree node for a formal parameter in a function declaration. */
public class ParameterDeclaration implements DeclarationWithOffset {
	
	/** Name of the formal parameter */
	protected String name;
	
	/** AST node for the type of the formal parameter */
	protected Type type;
	
	/**
	 * Offset of the formal parameter in the list of parameters for the function
	 * i.e. the size of the memory allocated to the previous parameters
	 */
	protected int offset;

    private MethodDefinition methodDefinition;

    /**
	 * Builds an AST node for a formal parameter declaration
	 * @param name : Name of the formal parameter
	 * @param type : AST node for the type of the formal parameter
	 */
	public ParameterDeclaration(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.type + " " + this.name;
	}

	/**
	 * Provide the type of the formal parameter in the list of formal parameters for the function
	 * @return Type of the formal parameter
	 */
	public Type getType() {
		return this.type;
	}

    @Override
    public Register getRegister() {
        return Register.LB;
    }

    /**
	 * Provide the offset of the formal parameter in the list of formal parameters for the function
	 * @return Offset of the formal parameter
	 */
	public int getOffset() {
		return this.offset;
	}

    /**
     * Set the offset of the format parameter in the list of formal parameters for the function
     * @param offset Offset
     */
	public void setOffset(int offset) {
	    this.offset = offset;
    }

    public void setMethodDefinition(MethodDefinition methodDefinition) {
        this.methodDefinition = methodDefinition;
    }

    public MethodDefinition getMethodDefinition() {
        return methodDefinition;
    }
}
