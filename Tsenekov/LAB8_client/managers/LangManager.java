package managers;

import java.util.ResourceBundle;
import java.util.Arrays;
import java.util.Locale;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LangManager {
	
	String[][] langs = {
		{"Русский (Русский)", "ru", "RU", "GUI.GuiLabels_ru"},
		{"Português (Португальский)", "pt", "PT", "GUI.GuiLabels_pt"},
		{"Dansk (Датский)", "da", "DA", "GUI.GuiLabels_da"},
		{"English (Английский)", "en", "GB", "GUI.GuiLabels_en"}
	};
	
	int selected = 0;
	
	ResourceBundle rb = ResourceBundle.getBundle("GUI.GuiLabels_ru");
	Locale locale = new Locale("ru", "RU");

	public LangManager() { }

	public String get(String k) {
		return rb.getString(k);
	}

	public String[] getLangs() {
		return Arrays.stream(langs).map(x->x[0]).toArray(String[]::new);
	}

	public int getIndex() {
		return selected;
	}

	public String formatFloat(double s) {
		return NumberFormat.getInstance(locale).format(s);
	}

	public String formatDate(LocalDateTime d) {
		return d.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale));
	}

	public void setLang(String s) {
		for (var i=0; i < langs.length; i++)
			if (langs[i][0] == s) {
				rb = ResourceBundle.getBundle(langs[i][3]);
				selected = i;
				locale = new Locale(langs[i][1], langs[i][2]);
			}
	}
}