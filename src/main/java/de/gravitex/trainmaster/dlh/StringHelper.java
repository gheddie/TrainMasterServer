package de.gravitex.trainmaster.dlh;

import java.util.List;

public class StringHelper {
	
	public static String stringListAsOrderedAndSeparated(List<String> strings) {
		StringBuffer buffer = new StringBuffer();
		int index = 0;
		for (String s : strings) {
			buffer.append(s);
			buffer.append("@" + index);
			index++;
			if (index < strings.size()) {
				buffer.append("#");
			}
		}
		return buffer.toString();
	}
}