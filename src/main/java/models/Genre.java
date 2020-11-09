package models;

public enum Genre {
    BLUES ("blues"),
    CLASSICAL ("classical"),
    COUNTRY ("country"),
    JAZZ ("jazz"),
    POP ("pop"),
    ROCK ("rock");

	private final String genre;
	
    Genre(String genre) {
        this.genre = genre;
    }
 
    public String getGenre() {
        return this.genre;
    }
}