package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.ASTNode;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;

public class GenericType implements ASTNode, Declaration {

	private String name;
	
	/* Exemple : MyClass<T extends A,B,C> */
	private List<InstanceType> extendedTypes;
	
	public GenericType(String ident) {
		this.name = ident;
		extendedTypes = new LinkedList<>();
	}
	
	public GenericType(String ident, List<InstanceType> extendedTypes) {
		this(ident);
		this.extendedTypes = extendedTypes;
		
	}

	public List<InstanceType> getExtendedTypes() {
		return extendedTypes;
	}

	@Override
    public String toString() {
	    String result = name;
	    if (extendedTypes.size() > 0) {
	        result += " extends ";
	        for (InstanceType type: extendedTypes)
	            result += type + " & ";
	        result = result.substring(0, result.length() - 3);
        }
        return result;
    }

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return null;
	}

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        for (InstanceType type: extendedTypes)
            if (! type.resolve(scope))
                return false;
        return true;
    }
}