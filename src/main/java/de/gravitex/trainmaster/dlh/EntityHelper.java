package de.gravitex.trainmaster.dlh;

import de.gravitex.trainmaster.entity.Locomotive;
import de.gravitex.trainmaster.entity.Waggon;

public class EntityHelper {

	public static Waggon makeWaggon(String waggonNumber) {
		Waggon waggon = new Waggon();
		waggon.setWaggonNumber(waggonNumber);
		return waggon;
	}

	public static Locomotive makeLocomotive(String locoNumber) {
		Locomotive locomotive = new Locomotive();
		locomotive.setLocoNumber(locoNumber);
		return locomotive;
	}
}