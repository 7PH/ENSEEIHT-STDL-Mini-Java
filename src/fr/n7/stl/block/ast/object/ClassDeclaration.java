package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.ASTNode;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

import java.util.List;

public class ClassDeclaration implements ASTNode {
	
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
        return false;
    }
}
