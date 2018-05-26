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

    protected String startLabel;

    protected int offset;

    private static int ID = 0;

    private synchronized static Integer getID() {
        return ++ ID;
    }
	
	public MethodDefinition(Signature _signature, Block _body) {
		this.signature = _signature;
		this.body = _body;
		this.abstractArgument = body == null;
        this.startLabel = "method_start_" + MethodDefinition.getID();
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
        for (ParameterDeclaration parameterDeclaration: signature.getParameters()) {
            parameterDeclaration.getType().resolve(scope);
            newScope.register(parameterDeclaration);
        }

        // abstract => ! modifier
        if (isAbstract()) {
            AccessModifier am = this.getAccessModifier();
            DefinitionModifier dm = this.getDefinitionModifier();
            if (am != AccessModifier.PUBLIC || dm != null) {
                Logger.error("An abstract method cannot be " + (am == null ? "" : am) + " " + (dm == null ? "" : dm));
                resolved = false;
            }
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
        ok &= body == null || body.checkType();
        
        if(body != null) {
        	boolean isWild = this.getSignature().getType().equals(AtomicType.VoidType) && body.getReturnType().equals(AtomicType.Wildcard);
        	
        	if(!isWild && !body.getReturnType().compatibleWith(this.getSignature().getType())) {
        		ok = false;
        		Logger.error("The method " + this.getName() + " should return a " + this.getSignature().getType() + " but its body returns a non compatible type : " + body.getReturnType());
        	}        	
        }       	

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
    public int allocateMemory(Register register, int offset) {
        body.allocateMemory(register, offset);
        return 0;
    }

    public int getParametersLength() {
        int length = 0;
        for (ParameterDeclaration parameterDeclaration: signature.getParameters())
            length += parameterDeclaration.getType().length();
        return length;
    }

	@Override
    public Fragment getCode(TAMFactory factory) {
        String id = String.valueOf(factory.createLabelNumber());
        String endLabel = "method_end_" + id;

        Fragment fragment1 = factory.createFragment();
        fragment1.add(factory.createJump(endLabel));

        Fragment fragment2 = factory.createFragment();
        fragment2.add(factory.createLoad(Register.LB, - 1 - getReturnType().length() - getParametersLength(), getParametersLength()));
        fragment2.append(body.getCode(factory));
        fragment2.addPrefix(startLabel + ":");
        fragment2.addSuffix(endLabel + ":");

        fragment1.append(fragment2);

        return fragment1;
    }

    @Override
    public String toString() {
    	return signature.toString() + ((body == null) ? "" : body.toString());
    }

	@Override
	public Type getReturnType() {
        return body.getReturnType();
	}

    public String getStartLabel() {
        return startLabel;
    }
}
