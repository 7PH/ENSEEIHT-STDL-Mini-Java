package fr.n7.stl.block.ast.scope;

/** Interface to mark a node in the ABSTRACT Syntax Tree as a Scope in the language. */
public interface Scope <D extends Declaration> {
	
	/**
	 * Provide either :
	 * - an empty option if the name is not contained in the scope.
	 * - an option contained the first value associated with the name if the name is contained in the scope.
	 * @param name : Name of the element looked for in the scope.
	 * @return : An optional object that carry an element named name if there exists one in the scope, empty if not.
	 */
	public D get(String name);
	
	/**
	 * Check if an element is registered (contained) in the current scope.
	 * @param name : Name of the element looked for in the current scope.
	 * @return : True if the scope contains an element named name, false if not.
	 */
	public boolean contains(String name);
	
	/**
	 * Check if a declaration can be registered in the scope.
	 * @param declaration : declaration which is checked.
	 * @return : True if the declaration can be registered in the scope, false if not.
	 */
	public boolean accepts(D declaration);
	
	/**
	 * Register a declaration in the scope.
	 * @param declaration : declaration to be registered in the scope.
	 */
	public void register(D declaration);

	public void register(D declaration, String... aliases);
	
}
