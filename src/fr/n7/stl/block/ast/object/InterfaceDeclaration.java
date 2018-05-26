package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
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
        this.extendedClass = extendedClass;
	}

	public List<Signature> getSignatures() {
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
        for (InstanceType tp: extendedClass) {
            if (tp.getDeclaration() instanceof ClassDeclaration) {
                Logger.error("The interface " + this.getName() + " extends the class "+ tp.getDeclaration().getName() + " which is not correct.");
                return false;
            }
        }

        if (implementedClasses.size() > 0) {
            Logger.error("Interface " + getName() + " cannot implements another");
            return false;
        }

        return true;
	}

	@Override
	public int allocateMemory(Register register, int offset) {
		throw new SemanticsUndefinedException("allocateMemory method not implemented yet in InterfaceDeclaration");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		throw new SemanticsUndefinedException("getCode method not implemented yet in InterfaceDeclaration");
	}

	@Override
	public Type getReturnType() {
		return AtomicType.VoidType;
	}

	@Override
	public String toString() {
		String result = "interface " + className;

		if (extendedClass.size() > 0) {
		    result += " extends ";
            for (InstanceType extended: extendedClass)
                result += extended + ", ";
            result = result.substring(0, result.length() - 2);
		}
        result += " {" + "\n";
		
		for (Signature s : this.signatures)
			result += s.toString() + "; \n";

        return result + "\n}\n";
	}

}

