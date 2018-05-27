package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

public interface ASTNode {

    /**
     * Inherited Semantics attribute to transmit the scope, fill it and modify the AST
     * @param scope Inherited Scope that should contain the declarations used in the Expression
     * @return Synthesized Semantics attribute that indicates if the identifier used in the
     * expression have been previously defined.
     */
    public boolean resolve(HierarchicalScope<Declaration> scope);
}
