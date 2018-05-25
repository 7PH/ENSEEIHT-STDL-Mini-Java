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
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		// Verify if the attribute is already in the scope
		if (!_scope.accepts(this.signature)) {
			Logger.error("Method " + this.signature.toString() + " is define twice.");
			return false;
		}
		// Register it
		_scope.register(this.signature);
		// Resolve signature
		if (!this.signature.getType().resolve(_scope)) {
			Logger.error("Could not resolve methode " + this.getName() + " because its type could not be resolved.");
			return false;
		}
		/* TODO : GESTION DES PARAMETRES ?
		for (ParameterDeclaration pd : this.signature.getParameters()) {
			if (!pd.resolve(_scope))
        		Logger.error("Could not resolve " + this.getName() + " because of the signature parameter " + pd.toString() + ".");
                return false;
		}
		*/
		// Resolve body if present
		if (this.isAbstract()) {
			if (!(this.body != null)) {
				Logger.error("Method " + this.getName() + " is declared abstract but has a body.");
				return false;
			}
		}
		// Resolve body
		if (!this.body.resolve(_scope)) {
			Logger.error("Could not resolve " + this.getName() + " because body could not be resolved.");
			return false;
		}
		return true;
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
