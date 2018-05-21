package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class Program {

    private List<InterfaceDeclaration> interfaces;
    
    private List<ClassDeclaration> classes;

    public Program(InterfaceDeclaration _interface) {
        this.interfaces = new LinkedList<InterfaceDeclaration>();
        this.interfaces.add(_interface);
    }
    
    public Program(ClassDeclaration _classe) {
        this.classes = new LinkedList<ClassDeclaration>();
        this.classes.add(_classe);
    }
    
    /** Add a interface declaration to the program.
     * @param _interface the interface to add
     */
    public void addInterface(InterfaceDeclaration _interface) {
    	this.interfaces.add(_interface);
    }
    
    /** Add a class declaration to the program.
     * @param _classe the class to add
     */
    public void addClass(ClassDeclaration _classe) {
    	this.classes.add(_classe);
    }

    @Override
    public String toString() {
    	throw new SemanticsUndefinedException("toString method is undefined for Program.");
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

}