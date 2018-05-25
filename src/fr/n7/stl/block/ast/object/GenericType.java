package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

public class GenericType {

	private String name;
	
	/* Exemple : MyClass<T extends A,B,C> */
	private List<TypeInstantiation> extendedTypes;
	
	public GenericType(String ident) {
		this.name = ident;
		extendedTypes = new LinkedList<>();
	}
	
	public GenericType(String ident, List<TypeInstantiation> extendedTypes) {
		this(ident);
		this.extendedTypes = extendedTypes;
		
	}

	public List<TypeInstantiation> getExtendedTypes() {
		return extendedTypes;
	}

	@Override
    public String toString() {
	    String result = name;
	    if (extendedTypes.size() > 0) {
	        result += " extends ";
	        for (TypeInstantiation type: extendedTypes)
	            result += type + " & ";
	        result = result.substring(0, result.length() - 3);
        }
        return result;
    }
}