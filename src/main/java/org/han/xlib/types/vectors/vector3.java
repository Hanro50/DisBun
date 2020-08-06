package org.han.xlib.types.vectors;

public class vector3<type> extends vector2<type> implements vectors<type> {
	public vector3(type x, type y, type z) {
		super(x, y);
		A.add(z);
	}

	public type z() {
		return A.get(2);
	}

	public void z(type z) {
		change(2, z);
	}
}
