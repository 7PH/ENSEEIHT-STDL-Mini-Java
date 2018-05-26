package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public abstract class ProgramDeclaration implements Declaration {
	
	protected ClassName className;
	
	protected List<InstanceType> extendedClass = new LinkedList<>();
	
	protected List<InstanceType> implementedClasses = new LinkedList<>();

	public abstract boolean subResolve(HierarchicalScope<Declaration> scope);

    public boolean resolve(HierarchicalScope<Declaration> scope) {
        if (! scope.accepts(this)) {
            Logger.error(this.getName() + " has already been declared");
            return false;
        }
        scope.register(this);

        SymbolTable subScope = new SymbolTable(scope);
        
        // register generic types in scope
        for (GenericType g : className.getGenerics()) {
        	subScope.register(g);
        }
        
        // register 'this' keyword in scope
        AbstractThisUse abstractThisUse = new AbstractThisUse(this);
        abstractThisUse.resolve(subScope);

        for (InstanceType extended: extendedClass) {
            if (! extended.resolve(subScope)) {
                Logger.error("Could not resolve extended class " + extended + " from " + getName());
                return false;
            }
        }

        for (InstanceType implemented: implementedClasses) {
            if (! implemented.resolve(subScope)) {
                Logger.error("Could not resolve implemented class " + implemented + " from " + getName());
                return false;
            }
        }

        return subResolve(subScope);
    }

	public abstract boolean checkType();

	public abstract int allocateMemory(Register register, int offset);

	public abstract Fragment getCode(TAMFactory factory);

	public abstract Type getReturnType();
	
	public abstract String toString();
	
	public String getName() {
	    return className.getName();
	}
	
	/*/!\ Not the same as getName ! Both are needed. */
	public ClassName getClassName() {
		return className;
	}

	/** Retourne la liste des types génériques déclarés.
	 * Exemple : public class toto<A, B, C> 
	 * getGenerics retourne [A,B,C]
	 * @return La liste des ClassName generiques
	 */
	public List<GenericType> getGenerics() {
		return this.className.getGenerics();
	}
	
	public Type getType() {
	    return new InstanceType(className.getName());
	}

	public List<InstanceType> getExtendedClass() {
		return extendedClass;
	}

	public List<InstanceType> getImplementedClasses() {
		return implementedClasses;
	}

}
