package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.DeclarationWithOffset;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributeDefinition extends Definition implements DeclarationWithOffset {

	private Type type;
	
	private String name;
	
	private Expression value;

    private Register register;
    private int offset;

    public AttributeDefinition(Type type, String name) {
		this.type = type;
		this.name = name;
		this.value = null;
	}
	
	public AttributeDefinition(Type type, String name, Expression value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		return this.type;
	}


	public int getRelativeOffset() {
	    return parent.attrRelativeOffset.getOrDefault(this, -1);
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        this.register = register;
        this.offset = offset;

	    if (isStatic()) {
	        // a static attribute takes place directly on stack
            return type.length();
        } else {
            // a non-static attribute will be stored among the object itself
            return 0;
	    }
    }

	@Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        if (isStatic()) {
            if (value != null)
                fragment.append(value.getCode(factory));
            else
                factory.createPush(type.length());
        }
        return fragment;
    }

	@Override
	public boolean checkType() {
        boolean ok = true;
		if (value != null && ! value.getType().compatibleWith(type)) {
			Logger.error(value + " is not compatible with " + type);
            ok = false;
		}
		return ok;
	}

	@Override
	public Type getReturnType() {
    	throw new SemanticsUndefinedException("getReturnType method is undefined for AttributeDefinition.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {

		// Verify if the attribute is already in the scope
		if (! scope.accepts(this)) {
			Logger.error("Object " + name + " is already defined in scope.");
			return false;
		}

		// Check if type is well not a void
		if (type.equalsTo(AtomicType.VoidType)) {
			Logger.error("Could not resolve attribute definition because of the type " + this.type + " which is a void.");
			return false;
		}

		// Resolve type part
		if (! type.resolve(scope)) {
    		Logger.error("Could not resolve attribute definition because of the type " + this.type + ".");
			return false;
		}

		// Resolve value part
		if (value != null && ! value.resolve(scope)) {
    		Logger.error("Could not resolve attribute definition because of the value " + this.value + ".");
			return false;
		}

        // Register the attribute
		scope.register(this);
		return true;
	}

    @Override
    public String toString() {
        String result = "";

        if (this.getAccessModifier() != null)
            result += this.getAccessModifier() + " ";

        if (this.getDefinitionModifier() != null)
            result += this.getDefinitionModifier() + " ";

        result += this.type + " " + this.name;

        if (value != null)
            result += " = " + value;

        result += ";";

        return result;
    }
}
