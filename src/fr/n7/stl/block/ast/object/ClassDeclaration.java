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

public class ClassDeclaration extends ProgramDeclaration {
	
	private ClassModifier modifier;

	private List<Definition> definitions;
	
	public ClassDeclaration(ClassModifier _modifier, ClassName _name, List<TypeInstantiation> _extension, List<TypeInstantiation> _implementation, List<Definition> _definitions) {
		this.modifier = _modifier;
		this.className = _name;
		this.extendedClass = _extension;
		this.implementedClasses = _implementation;
		this.definitions = _definitions;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}
	
	public boolean isAbstract() {
		return (this.modifier == ClassModifier.Abstract);
	}

	public boolean isFinal() {
		return (this.modifier == ClassModifier.Final);
	}

	@Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {

    	// Verify if the class is in the scope
		if (! _scope.accepts(this)) {
			Logger.error("Could not resolve class " + this.getName() + " because this name is already taken.");
			return false;
		}

		// Register it
    	_scope.register(this);
    	
    	// Verify thar implementedClass contains only interfaces
    	for (TypeInstantiation tp : this.implementedClasses) {
    		if (tp.getDeclaration() instanceof ClassDeclaration) {
    			Logger.error("The class " + this.getName() + " implements " + tp.getDeclaration().getName() + " which is not a interface.");
    			return false;
    		}
    	}
    	
    	// Verify that each interface method is implemented
    	for (TypeInstantiation tp : this.implementedClasses) {
    		InterfaceDeclaration interfaceDeclaration = (InterfaceDeclaration) tp.getDeclaration();
    		List<Signature> classSignatures = getClassSignatures();
    		for (Signature s : interfaceDeclaration.getSignatures()) {
    			if (!classSignatures.contains(s)) {
    				Logger.error("The class, by implementing " + tp.getDeclaration().getName() + ", needs to implement the method " + s.getName().split(" ")[0] + ".");
    				return false;
    			}
    		}
    	}
    	
    	// Verify that there is no more one superclass
    	if (this.extendedClass.size() > 1 ) {
    		Logger.error("A simple class like " + this.getName() + " can not have more than one superclass.");
    		return false;
    	}
    	
    	// Verify if every superclass abstract methods are implemented
    	if (this.extendedClass.size() != 0) {
    		TypeInstantiation tp = this.extendedClass.get(0);
    		// Check if the superclass is well a simple class
    		if (tp.getDeclaration() instanceof InterfaceDeclaration) {
    			Logger.error("The class " + this.getName() + " extends the interface "+ tp.getDeclaration().getName() + " which is not correct.");
				return false;
    		}
    		ClassDeclaration classDeclaration = (ClassDeclaration) tp.getDeclaration();
    		for (Definition d : classDeclaration.getDefinitions()) {
    			if (d instanceof MethodDefinition) {
    				MethodDefinition md = (MethodDefinition) d;
    				// For each abstract method of the superclass, we check if it is implemented
    				if (md.isAbstract()) {
    					if (!(this.definitions.contains(md))) {
    						Logger.error("The class, by extending " + classDeclaration.getName() + ", needs to implement the method " + md.getName() + ".");
    	    				return false;
    					}
    				}
    			}
    		}
    	}
    	
    	// Verify if a method is abstract and accord it with class modifier
    	for (Definition d : this.definitions) {
    		if (d instanceof MethodDefinition) {
    			if (((MethodDefinition) d).isAbstract() && !(this.isAbstract())) {
    				Logger.error("You declared " + d.getName() + " as abstract but its class " + this.getName() + " is not declared as abstract.");
    			}
    		}
    	}

    	// Define a new scope for it
    	HierarchicalScope<Declaration> newScope = new SymbolTable(_scope);

    	// Resolve for each definition in the new scope
    	for (Definition d : definitions) {
    		if (!d.resolve(newScope)) {
    			Logger.error("Could not resolve class " + this.getName() + " because of an unresolvable definition.");
    			return false;
    		}
    	}
    	
		return true;
    }

	private List<Signature> getClassSignatures() {
		List<Signature> classSignatures = new LinkedList<>();
		for (Definition d : this.definitions) {
			if (d instanceof MethodDefinition) {
				classSignatures.add(((MethodDefinition) d).getSignature());
			}
		}
		return classSignatures;
	}

	@Override
	public boolean checkType() {
		boolean b = true;
		for (Definition d : this.definitions) {
			b &= d.checkType();
		}
		return b;
	}

	@Override
	public int allocateMemory(Register register, int offset) {
		throw new SemanticsUndefinedException("allocateMemory method not implemented yet in ClassDeclaration");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		throw new SemanticsUndefinedException("getCode method not implemented yet in ClassDeclaration");
	}

	@Override
	public Type getReturnType() {
		return AtomicType.VoidType;
	}

	@Override
	public String toString() {
		String _result = "class " + this.className.toString() + " { \n";
		
		for (Definition d : this.definitions) {
			_result += d.toString() + "\n";
		}		
		
		return _result + "\n}\n";
	}

}
