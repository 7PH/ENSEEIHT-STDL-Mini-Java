package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

/** Elementary types in the Bloc language. */
public enum AtomicType implements Type {
	BooleanType,
	CharacterType,
	FloatingType,
	IntegerType,
	StringType,
	VoidType,
    Wildcard,
	NullType,
	ErrorType
	;

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type other) {
		return this == other || this == Wildcard;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type other) {
		if (equalsTo(other)) {
			return true;
		}

		if (other instanceof NamedType) {
            return compatibleWith(((NamedType) other).getType());
        }

        switch (this) {
            case NullType :
                return ((other != ErrorType) && (other != VoidType));

            case IntegerType:
                return (other == FloatingType);

            default:
                return false;
        }
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type other) {
		if (this.compatibleWith(other)) {
			return other;
		} else if (other.compatibleWith(this)) {
            return this;
        } else if (this == AtomicType.Wildcard) {
            return other;
        } else {
            return ErrorType;
        }
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length()
	 */
	@Override
	public int length() {
		switch (this) {
			case NullType :
			case BooleanType :
			case CharacterType :
			case FloatingType :
			case IntegerType :
			case StringType : return 1;
            case VoidType : return 0;
            case Wildcard : return 0;
			default : throw new IllegalArgumentException( "Must not call length on the Error type.");
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		switch (this) {
		case BooleanType: return "boolean";
		case CharacterType: return "char";
		case ErrorType: return "error";
		case FloatingType: return "float";
		case IntegerType: return "int";
		case StringType: return "String";
        case VoidType: return "void";
        case Wildcard: return "*";
		case NullType: return "unit";
		default: throw new IllegalArgumentException( "The default case should never be triggered.");
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return true;
	}

}
