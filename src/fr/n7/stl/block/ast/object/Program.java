package fr.n7.stl.block.ast.object;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.ASTNode;
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

    public MethodDefinition findFirstPublicStaticVoidMain() {
        List<ParameterDeclaration> mainParams = new ArrayList<>();
        //mainParams.add(new ParameterDeclaration("args", new ArrayType(AtomicType.StringType)));
        Signature mainSignature = new Signature(AtomicType.VoidType, "main", mainParams);

        for (ProgramDeclaration declaration: declarations) {
            if (! (declaration instanceof ClassDeclaration))
                continue;
            ClassDeclaration classDeclaration = (ClassDeclaration) declaration;
            List<MethodDefinition> main = classDeclaration.getMethodDefinitionsBySignature(mainSignature, false);
            if (main.size() > 0) return main.get(0);
        }

        return null;
    }

    public int allocateMemory(Register register, int offset) {
        int oldOffset = offset;
        for (ProgramDeclaration declaration: declarations) {
            offset += declaration.allocateMemory(register, offset);
        }
        return offset - oldOffset;
    }
	
	/** Provide the generated TAM code.
	 * @param factory factory to build AST nodes for TAM code.
	 * @return the generated TAM code.
	 */
    public Fragment getCode(TAMFactory factory){
        Fragment fragment = factory.createFragment();

        // add all the code of all the declarations
        for (ProgramDeclaration declaration: declarations)
            fragment.append(declaration.getCode(factory));

        // get code of the public static void main if found
        MethodDefinition main = findFirstPublicStaticVoidMain();
        if (main == null) {
            fragment.add(factory.createHalt());
            return fragment;
        }

        // @TODO String args[]
        // for (Expression argument : arguments)
        //     fragment.append(argument.getCode(factory));
        //fragment.add(factory.createPush(main.getType().length()));
        fragment.add(factory.createCall(main.getStartLabel(), Register.SB));
        //fragment.add(factory.createPop(main.getType().length(), main.getParametersLength()));

        // ok
        fragment.add(factory.createHalt());
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