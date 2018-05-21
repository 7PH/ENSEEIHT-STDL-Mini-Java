package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.ASTNode;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class Program implements ASTNode {

    private List<ASTNode> declarations = new LinkedList<>();

    public Program(ASTNode declaration) {
        this.declarations.add(declaration);
    }

    /**
     *
     * @param declaration
     */
    public void add(ASTNode declaration) {
    	this.declarations.add(declaration);
    }

	/** Check if a program is well typed.
	 * @return True if the program is well typed, False if not.
	 */
    boolean checkType() {
    	throw new SemanticsUndefinedException("checktype method is undefined for Program.");
    }
	
	/** Compute the size of the allocated memory. 
	 * @param _register register associated to the address of the program.
	 * @param _offset current offset for the address of the program.
	 * @return size of the memory allocated to the program.
	 */
    int allocateMemory(Register _register, int _offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for Program.");
    }
	
	/** Provide the generated TAM code.
	 * @param _factory factory to build AST nodes for TAM code.
	 * @return the generated TAM code.
	 */
    Fragment getCode(TAMFactory _factory){
    	throw new SemanticsUndefinedException("getCode method is undefined for Program.");
    }

    // @TODO
    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        for (ASTNode declaration: declarations) {
            if (! declaration.resolve(scope))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        throw new SemanticsUndefinedException("toString method is undefined for Program.");
    }
}