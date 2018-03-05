package model.util;

import java.time.LocalDateTime;
import java.time.Month;

//Klasa koja ima metode za dobavljanje 'custom' minimalnih i maksimalnih vrijednosti datuma, s obzirom da DATETIME u MySQL ima te vrijednosti kao ogranicenja
public class DateTimeUtil {
	public static LocalDateTime getDateTimeMin() {
		
		LocalDateTime dateTimeMin = LocalDateTime.of(1000, Month.JANUARY, 1, 0, 0, 0);
		return dateTimeMin;
	}
	
	public static LocalDateTime getDateTimeMax() {
		
		LocalDateTime dateTimeMax = LocalDateTime.of(9999, Month.DECEMBER, 31, 23, 59, 59);
		return dateTimeMax;
	}
}
