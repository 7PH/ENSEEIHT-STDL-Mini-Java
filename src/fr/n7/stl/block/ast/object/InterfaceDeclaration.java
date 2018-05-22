package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.LinkedList;
import java.util.List;

public class InterfaceDeclaration extends ProgramDeclaration {
	
	/** List of the methods signatures. */
	private List<Signature> signatures;
	
	public InterfaceDeclaration(ClassName _name, List<Signature> _signatures) {
		this.className = _name;
		this.signatures= new LinkedList<Signature>();
		for (Signature s : _signatures) {
			this.signatures.add(s);
		}
		this.extendedClass = new LinkedList<TypeInstantiation>();
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
		return true;
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

	@Override
	public String toString() {
		String _result = "interface" + this.className.toString() + " { \n";
		
		for (Signature s : this.signatures) {
			_result += s.toString() + "; \n";
		}		
		
		return _result + "\n }";
	}
}
