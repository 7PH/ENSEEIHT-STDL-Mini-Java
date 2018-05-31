package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;


public class InstanceType implements Type {

    public static final int OBJECT_ADDR_LENGTH = 1;

    private String name;

	/* Types génériques : <String> etc */
	private List<InstanceType> typeInstantiations;

	private ProgramDeclaration declaration;
	
	private GenericType genericDeclaration;

    public InstanceType(String name, List<InstanceType> instantiations) {
        this.name = name;
        this.typeInstantiations = instantiations;
    }

	public InstanceType(String name) {
		this(name, new LinkedList<>());
	}

	@Override
	public boolean equalsTo(Type other) {
        if (! (other instanceof InstanceType))
            return false;

        InstanceType instanceType = (InstanceType) other;
        if (declaration != null) {
            // this one is a class/interface type, like 'Box'. Other has to be too
            if (declaration != instanceType.getDeclaration())
                return false;
        } else {
            if (genericDeclaration != instanceType.getGenericDeclaration())
                return false;
        }

        Logger.warning("@TODO InstanceType: double check");
        return name.equals(instanceType.name);
	}

	public String getName() { return name; }

	@Override
	public boolean compatibleWith(Type other) {
        if (other == AtomicType.Wildcard)
            return true;

        if (! (other instanceof InstanceType))
            return false;

        InstanceType instanceType = (InstanceType) other;
        //System.out.println(this + " compatible with " + other + " " + (declaration == null) + " / " + (instanceType.getDeclaration() == null));

        if (instanceType.getDeclaration() == null) {
            // if the other is a generic type like 'T extends Stuff'

            if (declaration == null) {
                // this one is also a generic type
                // we're trying to match 'T1 extends Foo' against 'T2 extends Bar'

                // same declaration, this is the same type
                if (getGenericDeclaration() == instanceType.getGenericDeclaration()) return true;

                // if one constraint on this type is compatible with the other
                for (InstanceType superClassT: genericDeclaration.getExtendedTypes())
                    if (superClassT.compatibleWith(other))
                        return true;
            } else {
                // this one is a "class" type like 'Integer' and the other is like 'T extends Stuff'

                // if the other type has no constraint it's ok
                if (instanceType.getGenericDeclaration().getExtendedTypes().size() == 0)
                    return true;

                // if a superclass is compatible with the other, it's OK
                for (InstanceType superClassT: declaration.getExtendsList())
                    if (superClassT.compatibleWith(other))
                        return true;

                // idem for an interface
                for (InstanceType implemented: declaration.getImplementsList())
                    if (implemented.compatibleWith(other))
                        return true;

                // @TODO here
                /*
                for (InstanceType constraint: instanceType.getGenericDeclaration().getExtendedTypes()) {
                    boolean ok = false;

                    // if a superclass is compatible with the other, it's OK
                    for (InstanceType superClassT: declaration.getExtendsList())
                        if (superClassT.compatibleWith(constraint))
                            ok = true;

                    // idem for an interface
                    for (InstanceType implemented: declaration.getImplementsList())
                        if (implemented.compatibleWith(constraint))
                            ok = true;

                    // we could not satisfy this constraint
                    if (! ok) return false;
                }
                */

                return true;
            }

        } else {
            // other is a class/interface

            if (declaration != null) {
                // this one too

                if (declaration == instanceType.getDeclaration())
                    return true;

                // if a superclass is compatible with the other, it's OK
                for (InstanceType superClassT : declaration.getExtendsList())
                    if (superClassT.compatibleWith(other))
                        return true;
                // idem for an interface
                for (InstanceType implemented : declaration.getImplementsList())
                    if (implemented.compatibleWith(other))
                        return true;
            } else {
                // we're trying to set a generic type 'T extends Foo' in an object (eg: 'Foo').
                // this can only work if one the superclass of 'T' is compatible with 'Foo'

                if (instanceType.name.equals(name))
                    return true;

                // if one constraint on this type is compatible with the other
                for (InstanceType superClassT: genericDeclaration.getExtendedTypes())
                    if (superClassT.compatibleWith(other))
                        return true;
            }

        }

        return false;
	}

	@Override
	public Type merge(Type other) {
		throw new SemanticsUndefinedException("merge method is undefined for InstanceType.");
	}

	@Override
	public int length() {
	    return OBJECT_ADDR_LENGTH;
    }

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.knows(name)) {
			Logger.error("InstanceType: Unknown reference to '" + name + "'");
			return false;
		}

		for (InstanceType type: typeInstantiations)
		    type.resolve(scope);

		Declaration scopeDeclaration = scope.get(name);
        if (scopeDeclaration instanceof ProgramDeclaration) {
            this.declaration = (ProgramDeclaration) scopeDeclaration;

            // check generic types instantiated with class one
            /* @TODO we can't do here because of the constructor. "class Box<T> { public Box() { } }"
            if (! checkClassGenericsMatch())
                return false;
            */

        } else if (scope.get(this.name) instanceof GenericType) {
            // This is an instance of the generic type.
            this.genericDeclaration = (GenericType) scopeDeclaration;

            /* @TODO make this or not ??
            for (Type type: genericDeclaration.getExtendedTypes()) {
                if (! compatibleWith(type)) {
                    Logger.error("Could not instantiate generic type '" + this + "' because it does not extends '" + type + "'");
                    return false;
                }
            }
            */
        } else {
            Logger.error(this.name + " is not a class nor an interface and cannot be instantiated. " + scope.get(this.name).getClass().getName() );
            return false;
        }

        return true;
	}

	@Override
	public String toString() {
		return this.name + (this.typeInstantiations.size() > 0 ? ("<" + this.typeInstantiations + ">") : "");
	}


	public ProgramDeclaration getDeclaration() {
		return declaration;
	}

	public GenericType getGenericDeclaration() {
		return genericDeclaration;
	}

	public List<InstanceType> getTypeInstantiations() {
		return typeInstantiations;
	}

	/** Say if the object contains this attribute.
	 * @param name the searched attribute
	 * @return true if contains, false if not
	 */
	public boolean contains(String name) {
		if (this.getDeclaration() instanceof InterfaceDeclaration) {
			Logger.error("Interface " + this.name + " does not contain attribute " + name + " because " + this.name + " is an interface.");
			return false;
		} else if (this.getDeclaration() instanceof ClassDeclaration) {
			ClassDeclaration _classe = (ClassDeclaration) this.getDeclaration();
			// TODO : check if attribute is present
		} else {
			Logger.error("This message should not appear. Contains method was called on a InstanceType that was initiated with a declaration that is not a ProgramDeclaration");
			return false;
		}
		return false;
	}
}
