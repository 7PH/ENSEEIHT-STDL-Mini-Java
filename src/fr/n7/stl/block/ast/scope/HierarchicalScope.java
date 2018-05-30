package fr.n7.stl.block.ast.scope;

/** Interface to mark a node in the ABSTRACT Syntax Tree as a Hierarchical Scope in the language. */
public interface HierarchicalScope <D extends Declaration> extends Scope<D> {
	
	/**
	 * Check if an element is registered (known) in the whole hierarchical scope.
	 * @param name : Name of the element looked for in the whole hierarchical scope.
	 * @return : True if the whole hierarchical scope knows an element named name, false if not.
	 */
	public boolean knows(String name);

}
