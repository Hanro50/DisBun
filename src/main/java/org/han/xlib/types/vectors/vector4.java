package org.han.xlib.types.vectors;

public class vector4<type> extends vector3<type> implements vectors<type> {
	public vector4(type x, type y, type z, type w) {
		super(x, y, z);
		A.add(w);
	}

	public type w() {
		return A.get(3);
	}

	public void w(type w) {
		change(3, w);
	}
}
