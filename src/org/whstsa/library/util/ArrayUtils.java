package org.whstsa.library.util;

public class ArrayUtils {
	public static String join(Object[] array, Character delimiter) {
		StringBuilder joined = new StringBuilder();
		for (Object o : array) {
			joined.append(o);
			if (delimiter != null) {
				joined.append(delimiter);
			}
		}
		return joined.toString();
	}
	
	public static String join(Object[] array) {
		return join(array, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] merge(T[] ...arrays) {
		int size = 0;
		for (T[] array : arrays) {
			size += array.length;
		}
		T[] mainArray = (T[]) new Object[size];
		int prevLength = 0;
		for (T[] array : arrays) {
			System.arraycopy(array, 0, mainArray, prevLength, array.length);
			prevLength = array.length;
		}
		return mainArray;
	}
}
