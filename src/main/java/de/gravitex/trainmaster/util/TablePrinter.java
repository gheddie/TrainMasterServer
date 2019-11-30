package de.gravitex.trainmaster.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import de.gravitex.trainmaster.entity.BaseEntity;

@Component
public class TablePrinter {
	
	public void print(List<? extends BaseEntity> entities) {

		List<String> fields = new ArrayList<String>();
		for (Field f : entities.get(0).getClass().getDeclaredFields()) {
			fields.add(f.getName());
		}
		int werner = 5;
	}
}