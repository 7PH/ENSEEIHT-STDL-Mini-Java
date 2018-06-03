package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.Block;
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
	
	public MethodDefinition(Signature signature, Block body) {
		this.signature = signature;
		this.body = body;
		this.abstractArgument = this.body == null;
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

        // 'this' only available in non-static methods
        if (! isStatic()) {
            AbstractThisUse abstractThisUse = new AbstractThisUse(parent);
            abstractThisUse.resolve(newScope);
        }

        // @TODO Do something with parameters?
        for (ParameterDeclaration parameterDeclaration: signature.getParameters()) {
            parameterDeclaration.getType().resolve(scope);
            newScope.register(parameterDeclaration);
            parameterDeclaration.setMethodDefinition(this);
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
        boolean ok;

        // body?
        ok = body == null || body.checkType();

        // parameters ok?
        for (ParameterDeclaration p: this.signature.getParameters()) {
            if (p.getType().equalsTo(AtomicType.ErrorType)) {
                Logger.error("Error in parameter " + p + " of " + this.getName());
                ok = false;
            }
        }
        
        if (body != null) {
            // method has a body, we have to check the return type
            Type returnType = body.getReturnType();
            Type other = getSignature().getType();
            if (returnType == AtomicType.Wildcard && other != AtomicType.VoidType
                    || ! returnType.compatibleWith(other)) {
                // that means the body has no return instruction
                Logger.error("Wrong return type for body of method " + getSignature().getName());
                ok = false;
            }
        }

        return ok;
    }

	@Override
    public int allocateMemory(Register register, int offset) {
        int off = offset + 3 + getParametersLength() + getType().length();
        if (! isStatic()) off += 1;
        body.allocateMemory(Register.SB, off);
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
        int l = getParametersLength();
        if (! isStatic()) l += 1;
        fragment2.add(factory.createLoad(Register.LB,  - getReturnType().length() - l, l));
        fragment2.append(body.getCode(factory));
        fragment2.addPrefix(startLabel + ":");
        fragment2.add(factory.createReturn(0, 0));
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
