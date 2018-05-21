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

import java.util.LinkedList;
import java.util.List;

public class InterfaceDeclaration implements Instruction {
	
	/** Class name of the interface. */
	private ClassName name;
	
	/** List of the methods signatures. */
	private List<Signature> signatures = new LinkedList<Signature>();
	
	/** Extended class. */
	private List<TypeInstantiation> extendedClass = new LinkedList<TypeInstantiation>();
	
	public InterfaceDeclaration(ClassName _name, List<Signature> _signatures) {
		this.name = _name;
		for (Signature s : _signatures) {
			this.signatures.add(s);
		}
	}
	
	public InterfaceDeclaration(ClassName _declaration, List<TypeInstantiation> _Type_instantiations, List<Signature> _signatures) {
		this(_declaration, _signatures);
		for (TypeInstantiation i : _Type_instantiations) {
			extendedClass.add(i);
		}
	}

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
		throw new SemanticsUndefinedException("resolve method not implemented yet in InterfaceDeclaration");
    }

	@Override
	public boolean checkType() {
		throw new SemanticsUndefinedException("checkType method not implemented yet in InterfaceDeclaration");
	}

	@Override
	public int allocateMemory(Register register, int offset) {
		throw new SemanticsUndefinedException("allocateMemory method not implemented yet in InterfaceDeclaration");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		throw new SemanticsUndefinedException("getCode method not implemented yet in InterfaceDeclaration");
	}

	@Override
	public Type getReturnType() {
		return AtomicType.VoidType;
	}
}
