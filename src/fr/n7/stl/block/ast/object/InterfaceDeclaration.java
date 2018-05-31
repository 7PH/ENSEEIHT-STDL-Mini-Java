package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
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

import java.util.LinkedList;
import java.util.List;

public class InterfaceDeclaration extends ProgramDeclaration {
	
	/** List of the methods signatures. */
	private List<Signature> signatures;
	
	public InterfaceDeclaration(ClassName name, List<Signature> signatures) {
		this(name, new LinkedList<>(), signatures);
	}
	
	public InterfaceDeclaration(ClassName name, List<InstanceType> extendedClass, List<Signature> signatures) {
		this.className = name;
		this.signatures = signatures;
        this.extendsList = extendedClass;
	}

	public List<Signature> getSignatures() {
		return signatures;
	}

	public List<Signature> findSignature(String name, List<Expression> parameters) {
	    List<Signature> signatures = new LinkedList<>();
	    for (Signature signature: this.signatures) {
            if (!signature.getMethodName().equals(name)) continue;
            List<ParameterDeclaration> declarations = signature.getParameters();
            if (parameters.size() != declarations.size()) continue;
            boolean ok = true;
            for (int i = 0; i < parameters.size(); ++i) {
                if (!parameters.get(i).getType().compatibleWith(declarations.get(i).getType()))
                    ok = false;
            }
            if (!ok) continue;
            signatures.add(signature);
        }
        for (InstanceType type: extendsList)
            signatures.addAll(((InterfaceDeclaration) type.getDeclaration()).findSignature(name, parameters));
        return signatures;
	}

	@Override
    public boolean subResolve(HierarchicalScope<Declaration> scope) {
    	// Define a new scope for it
    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);

    	// Register all signatures
    	for (Signature signature: signatures) {
    		if (! newScope.accepts(signature)) {
    			Logger.error("Method " + signature.getName() + " is defined twice in " + getName() + ".");
    			return false;
    		}
    		newScope.register(signature);
    	}
    	
		return true;
    }

	@Override
	public boolean checkType() {
        // Check if the superclasses are well superinterface
        for (InstanceType tp: extendsList) {
            if (tp.getDeclaration() instanceof ClassDeclaration) {
                Logger.error("The interface " + this.getName() + " extends the class "+ tp.getDeclaration().getName() + " which is not correct.");
                return false;
            }
        }

        if (implementsList.size() > 0) {
            Logger.error("Interface " + getName() + " cannot implements another");
            return false;
        }

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

	@Override
	public String toString() {
		String result = "interface " + className;

		if (extendsList.size() > 0) {
		    result += " extends ";
            for (InstanceType extended: extendsList)
                result += extended + ", ";
            result = result.substring(0, result.length() - 2);
		}
        result += " {" + "\n";
		
		for (Signature s : this.signatures)
			result += s.toString() + "; \n";

        return result + "\n}\n";
	}

}

