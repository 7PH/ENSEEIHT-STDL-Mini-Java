package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Register;

public interface DeclarationWithOffset extends Declaration {
    Register getRegister();
    int getOffset();
}
