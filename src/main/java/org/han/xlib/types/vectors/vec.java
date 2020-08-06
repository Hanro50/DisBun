package org.han.xlib.types.vectors;

public final class vec<type> {
	public static <type> vector2<type> D2(type x, type y) {
		return new vector2<type>(x, y);
	}

	public static <type> vector3<type> D3(type x, type y, type z) {
		return new vector3<type>(x, y, z);
	}

	public static <type> vector4<type> D4(type x, type y, type z, type w) {
		return new vector4<type>(x, y, z, w);
	}
}
