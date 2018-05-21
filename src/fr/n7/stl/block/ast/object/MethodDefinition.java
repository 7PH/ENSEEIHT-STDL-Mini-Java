package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class MethodDefinition extends Definition {

	private Signature signature;
	
	private Block body;
	
	private boolean abstractArgument;
	
	private Register register;
	
    private int offset;
	
	public MethodDefinition(Signature _signature, Block _body, boolean _abstractArgument) {
		this.signature = _signature;
		this.body = _body;
		this.abstractArgument = _abstractArgument;
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

	/** Check if the method is well typed.
	 * @return true if the mehod is well typed, false if not
	 */
    public boolean checkType() {
        throw new SemanticsUndefinedException("checkType method is undefined for MethodDefinition.");
    }

	/** Compute the size of the allocated memory. 
	 * @param _register register associated to the address of the method.
	 * @param _offset current offset for the address of the method.
	 * @return size of the memory allocated to the method.
	 */
    public int allocateMemory(Register _register, int _offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for MethodDefinition.");
    }

	/** Provide the generated TAM code.
	 * @param _factory factory to build AST nodes for TAM code.
	 * @return the generated TAM code.
	 */
    public Fragment getCode(TAMFactory _factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for MethodDefinition.");
    }

    @Override
    public String toString() {
    	throw new SemanticsUndefinedException("toString method is undefined for MethodDefinition.");
    }
}
