package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;

public abstract class DefinitionAccess implements Instruction, Expression {

    protected String name;

    protected Expression object;
    
    protected InstanceType objectType;

}