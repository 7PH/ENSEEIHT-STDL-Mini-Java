package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class MethodDefinition extends Definition {

	protected Signature signature;

    protected Block body;

    protected boolean abstractArgument;

    protected Register register;

    protected int offset;
	
	public MethodDefinition(Signature _signature, Block _body) {
		this.signature = _signature;
		this.body = _body;
	}

	/** Get the method signature.
	 * @return the method signature
	 */
	public Signature getSignature() {
        return this.signature;
    }
	
	/** Get the method body.
	 * @return the method body
	 */
	public Block getBody() {
        return this.body;
    }
	
	/** Say if the method is abstract.
	 * @return true if the method is abstract, false if not
	 */
	public boolean isAbstract() {
		return this.abstractArgument;
	}
	
	/** Get the method name.
	 * @return the method name
	 */
    public String getName() {
        return this.signature.getName();
    }

	/** Get the method type.
	 * @return the method type
	 */
    public Type getType() {
        return this.signature.getType();
    }

    /** Get the register of the method.
     * @return the register
     */
    public Register getRegister() {
        return this.register;
    }

    /** Get the offset of the method
     * @return the offset
     */
    public int getOffset() {
        return this.offset;
    }

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
        boolean resolved = true;

		// Verify if the attribute is already in the scope
		if (! scope.accepts(signature)) {
			Logger.error("Method " + signature.toString() + " has already been defined");
            resolved = false;
		}

		// Register it
		scope.register(signature);

		// Resolve signature
        resolved &= signature.getType().resolve(scope);

        HierarchicalScope<Declaration> newScope = new SymbolTable(scope);
        // @TODO Do something with parameters?
        for (ParameterDeclaration parameterDeclaration: this.signature.getParameters()) {
            parameterDeclaration.getType().resolve(scope);
            newScope.register(parameterDeclaration);
        }

		// abstract => ! body
		if (isAbstract() && body != null) {
            Logger.error("Method " + this.getName() + " is declared abstract but has a body.");
            resolved = false;
		}

		if (body != null && ! body.resolve(newScope))
		    resolved = false;

		return resolved;
	}
	
	@Override
    public boolean checkType() {
        boolean ok = true;

        // body?
        ok &= this.body.checkType();

        // parameters ok?
        for (ParameterDeclaration p : this.signature.getParameters()) {
            if (p.getType().equalsTo(AtomicType.ErrorType)) {
                Logger.error("Error in parameter " + p + " of " + this.getName());
                ok = false;
            }
        }

        return ok;
    }

	@Override
    public int allocateMemory(Register _register, int _offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for MethodDefinition.");
    }

	@Override
    public Fragment getCode(TAMFactory _factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for MethodDefinition.");
    }

    @Override
    public String toString() {
    	return this.signature.toString() + this.body.toString();
    }

	@Override
	public Type getReturnType() {
    	throw new SemanticsUndefinedException("getReturnType method is undefined for MethodDefinition.");
	}

}
