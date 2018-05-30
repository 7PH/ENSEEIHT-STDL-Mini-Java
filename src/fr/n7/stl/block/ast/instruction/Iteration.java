package fr.n7.stl.block.ast.instruction;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for a conditional instruction. */
public class Iteration implements Instruction {

	protected Expression condition;

	protected Block body;

	public Iteration(Expression _condition, Block _body) {
		this.condition = _condition;
		this.body = _body;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "while (" + this.condition + " )" + this.body;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return condition.resolve(scope) && body.resolve(scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		boolean b = condition.getType() == AtomicType.BooleanType;
        if (!b)
        	throw new RuntimeException("Condition " + this.condition.getType().toString()
        									+ " is not boolean");
		b &= body.checkType();
		if (!b)
			throw new RuntimeException("Body of While block is not well typed.");
		return b;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register register, int offset) {
		body.allocateMemory(register, offset);
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
	    String id = String.valueOf(factory.createLabelNumber());
        String startLabel = "start_" + id;
        String endLabel = "end_" + id;

		Fragment fragment = factory.createFragment();
		fragment.append(condition.getCode(factory));
        fragment.addPrefix(startLabel + ":");
		fragment.add(factory.createJumpIf(endLabel, 0));
        fragment.append(body.getCode(factory));
        fragment.add(factory.createJump(startLabel));
        fragment.addSuffix(endLabel + ":");
		return fragment;
	}

	@Override
	public Type getReturnType() {
		return body.getReturnType();
	}

}
