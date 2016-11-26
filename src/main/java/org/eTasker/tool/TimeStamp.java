package org.eTasker.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {

	public static String get() {
		return new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());
	}
}
