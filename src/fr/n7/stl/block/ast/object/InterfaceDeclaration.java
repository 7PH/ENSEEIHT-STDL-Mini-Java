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
	
	public InterfaceDeclaration(ClassName _name, List<Signature> _signatures) {
		this.className = _name;
		this.signatures= new LinkedList<Signature>();
		for (Signature s : _signatures) {
			this.signatures.add(s);
		}
		this.implementedClasses = new LinkedList<TypeInstantiation>();
	}
	
	public InterfaceDeclaration(ClassName _declaration, List<TypeInstantiation> _Type_instantiations, List<Signature> _signatures) {
		this(_declaration, _signatures);
		for (TypeInstantiation i : _Type_instantiations) {
			implementedClasses.add(i);
		}
	}

	public List<Signature> getSignatures() {
		return signatures;
	}

	@Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {

    	// Check if the interface is already register
    	if (! scope.accepts(this)) {
			Logger.error("Could not resolve interface " + this.getName() + " because this name is already taken.");
			return false;
		}			
    	
		// Register it
    	scope.register(this);

		// Check if the superclasses are well superinterface
    	for (TypeInstantiation tp: this.implementedClasses) {
    		if (tp.getDeclaration() instanceof ClassDeclaration) {
    			Logger.error("The interface " + this.getName() + " extends the class "+ tp.getDeclaration().getName() + " which is not correct.");
				return false;
    		}
    	}
    	
    	// Define a new scope for it
    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);

    	// Register all signatures
    	for (Signature signature: signatures) {
    		if (! newScope.accepts(signature)) {
    			Logger.error("Methods " + signature.getName() + " is defined twice in " + getName() + ".");
    			return false;
    		}
    		newScope.register(signature);
    	}
    	
		return true;
    }

	@Override
	public boolean checkType() {
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
		String _result = "interface " + this.className.toString() + " { \n";
		
		for (Signature s : this.signatures) {
			_result += s.toString() + "; \n";
		}

        return _result + "\n}\n";
	}

}

