package org.themassacre.sabotage.utils;

import java.util.Comparator;

public class CaseInsensitiveComparator implements Comparator<Object> {
	@Override
	public int compare(Object arg0, Object arg1) {
		int v = arg0.toString().toLowerCase().compareTo(arg1.toString().toLowerCase());
		if(v == 0)
		{
			return arg0.toString().compareTo(arg1.toString());
		}
		
		return v;
	}

}
