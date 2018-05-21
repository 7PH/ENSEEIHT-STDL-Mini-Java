package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class ClassDeclaration implements Instruction {
	
	private ClassModifier modifier;
	
	private ClassName name;
	
	private List<TypeInstantiation> extension;
	
	private List<Definition> definitions;
	
	public ClassDeclaration(ClassModifier _modifier, ClassName _name, List<TypeInstantiation> _extension, List<Definition> _definitions) {
		this.modifier = _modifier;
		this.name = _name;
		this.extension = _extension;
		this.definitions = _definitions;
	}

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
		throw new SemanticsUndefinedException("resolve method not implemented yet in ClassDeclaration");
    }

	@Override
	public boolean checkType() {
		throw new SemanticsUndefinedException("checkType method not implemented yet in ClassDeclaration");
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
}
