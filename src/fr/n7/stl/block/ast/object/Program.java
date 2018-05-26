package fr.n7.stl.block.ast.object;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.ASTNode;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.ArrayType;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class Program implements ASTNode {

    private List<ProgramDeclaration> declarations = new LinkedList<>();

    public Program(ProgramDeclaration declaration) {
        this.declarations.add(declaration);
    }

    /**
     *
     * @param declaration
     */
    public void add(ProgramDeclaration declaration) {
    	this.declarations.add(declaration);
    }

	/** Check if a program is well typed.
	 * @return True if the program is well typed, False if not.
	 */
    public boolean checkType() {
    	boolean b = true;
    	for (ProgramDeclaration pd : this.declarations) {
    		b &= pd.checkType();
        }
    	return b;
    }
    
	/** Compute the size of the allocated memory. 
	 * @param _register register associated to the address of the program.
	 * @param _offset current offset for the address of the program.
	 * @return size of the memory allocated to the program.
	 */
    int allocateMemory(Register _register, int _offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for Program.");
    }

    public MethodDefinition findFirstPublicStaticVoidMain() {
        List<ParameterDeclaration> mainParams = new ArrayList<>();
        mainParams.add(new ParameterDeclaration("args", new ArrayType(AtomicType.StringType)));
        Signature mainSignature = new Signature(AtomicType.VoidType, "main", mainParams);

        for (Declaration declaration: declarations) {
            if (! (declaration instanceof ClassDeclaration))
                continue;
            ClassDeclaration classDeclaration = (ClassDeclaration) declaration;
            MethodDefinition main = classDeclaration.getMethodDefinitionBySignature(mainSignature.getName());
            if (main != null) return main;
        }

        return null;
    }
	
	/** Provide the generated TAM code.
	 * @param factory factory to build AST nodes for TAM code.
	 * @return the generated TAM code.
	 */
    public Fragment getCode(TAMFactory factory){
        MethodDefinition main = findFirstPublicStaticVoidMain();
        Fragment fragment = factory.createFragment();
        if (main == null) return fragment;
        fragment.append(main.getCode(factory));
        return fragment;
    }


    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        // Define a new scope
    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);

    	// Resolve each declaration in it
        for (ProgramDeclaration declaration: declarations) {
            if (! declaration.resolve(newScope))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (ProgramDeclaration programDeclaration: declarations)
            s.append(programDeclaration);
        return s.toString();
    }
}