package net.puzzon.properties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.ServletContext;

public class Resources {
	
	// Request "locale" parameter value
	String locale = null;	
	ServletContext context = null;
	
	public Resources(final String locale, final ServletContext servletContext) {
		setLocale( locale );
		this.context = servletContext; 
	}
	
	public String getLocale() {
		return this.locale;
	}
	
	public void setLocale(final String locale) {
		if (null == locale) { this.locale = "en"; return; }
		
		this.locale = locale.trim().toLowerCase();
		if (this.locale.equalsIgnoreCase("pl") || this.locale.equalsIgnoreCase("pl_pl"))
			this.locale = "pl";
		else
			this.locale = "en";
	}
	
	@SuppressWarnings("serial")
	final static HashMap<String, String> texts = new HashMap<String, String>() {
		{
			put("menu.about", "About PuzzOn");
			put("menu.howto", "How To...");
			put("menu.tweets", "Latest Tweets!");
			put("menu.scores", "Your Scores");
			put("carousel.prev", "previous");
			put("carousel.next", "next");
			
			put("arena.snapping", "snapping puzzles");
			put("arena.greatjob", "Great Job! Next Puzzle?");
			
			put("scores.noscores", "No Scores - you did not solve any puzzle in this session");
		}
	};

	@SuppressWarnings("serial")
	final static HashMap<String, String> texts_pl = new HashMap<String, String>() {
		{
			put("menu.about", "O Nas");
			put("menu.howto", "Jak Zacząć?");
			put("menu.tweets", "Najnowsze Pomysły!");
			put("menu.scores", "Twoje Wyniki");
			put("carousel.prev", "poprzedni");
			put("carousel.next", "następny");
			
			put("arena.snapping", "dopasowywanie puzzli");
			put("arena.greatjob", "Znakomicie! Kolejna figura?");
			
			put("scores.noscores", "Brak Wyników - w bieżącej sesji nie rozwiązano żadnego puzzla");
		}
	};

	public static String getText(final String key, final String locale) {
		if (null == locale)
			return texts.get(key);

		if ((locale.equalsIgnoreCase("pl") || locale.equalsIgnoreCase("pl_pl")))
			return texts_pl.get(key);

		return texts.get(key);
	}
	
	public String getText(final String key) {
		return Resources.getText(key, this.locale);
	}
	
	public String getSource(final String filename) {	
		if (this.locale.equalsIgnoreCase("pl")) return filename + "-pl";
		
		return filename;
	}
	
	public String getSourceContent(final String source) {
		final StringBuilder content = new StringBuilder();
		
		try {
			final FileInputStream in = new FileInputStream(this.context.getRealPath(getSource(source)));
			final InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			final BufferedReader input = new BufferedReader(reader);
			
			String line = "";
			while((line = input.readLine()) != null) {
				content.append(line);
			}
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		return content.toString();
	}
}
