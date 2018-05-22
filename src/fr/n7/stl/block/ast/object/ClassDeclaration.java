package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.List;

public class ClassDeclaration extends ProgramDeclaration {
	
	private ClassModifier modifier;

	private List<Definition> definitions;
	
	public ClassDeclaration(ClassModifier _modifier, ClassName _name, List<TypeInstantiation> _extension, List<Definition> _definitions) {
		this.modifier = _modifier;
		this.className = _name;
		this.extendedClass = _extension;
		this.definitions = _definitions;
	}

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
		throw new SemanticsUndefinedException("resolve method not implemented yet in ClassDeclaration");
    }

	@Override
	public boolean checkType() {
		boolean b = false;
		for (Definition d : this.definitions) {
			b &= d.checkType();
		}
		return b;
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

	@Override
	public String toString() {
		String _result = "interface" + this.className.toString() + " { \n";
		
		for (Definition d : this.definitions) {
			_result += d.toString() + "\n";
		}		
		
		return _result + "\n }";
	}
}
