package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.expression.*;
import fr.n7.stl.block.ast.expression.accessible.ArrayAccess;
import fr.n7.stl.block.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.expression.value.BooleanValue;
import fr.n7.stl.block.ast.expression.value.IntegerValue;
import fr.n7.stl.block.ast.expression.value.NullValue;
import fr.n7.stl.block.ast.instruction.*;

import java.util.List;

public class BlockFactory {

	public Block createBlock(List<Instruction> _instructions) {
		return new Block(_instructions);
	}
	
	public Instruction createAssignment(AssignableExpression _target, Expression _value) {
		return new Assignment(_target, _value);
	}
	
	public Instruction createConditional(Expression _condition, Block _then) {
		return new Conditional(_condition, _then);
	}
	
	public Instruction createConditional(Expression _condition, Block _then, Block _else) {
		return new Conditional(_condition, _then, _else);
	}
	
	public Instruction createIteration(Expression _condition, Block _body) {
		return new Iteration(_condition, _body);
	}
	
	public Instruction createPrinter(Expression expression) {
		return new LogInstruction(expression);
	}
	
	public Expression createTrueValue() {
		return BooleanValue.True;
	}
	
	public Expression createFalseValue() {
		return BooleanValue.False;
	}
	
	public Expression createNullValue() {
		return NullValue.Null;
	}
	
	public Expression createIntegerValue(String _value) {
		return new IntegerValue(_value);
	}
	
	public Expression createUnaryExpression( UnaryOperator _operator, Expression _parameter) {
		return new UnaryExpression( _operator, _parameter);
	}
	
	public Expression createBinaryExpression( Expression _left, BinaryOperator _operator, Expression _right) {
		return new BinaryExpression( _left, _operator, _right);
	}
	
	public Expression createIdentifierAccess(String _name) {
		return new IdentifierAccess(_name);
	}
	
	public Expression createArrayAccess( Expression _array, Expression _index) {
		return new ArrayAccess( _array, _index);
	}

}
