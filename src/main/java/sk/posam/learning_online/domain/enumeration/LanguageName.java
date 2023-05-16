package sk.posam.learning_online.domain.enumeration;

public enum LanguageName {
    ENGLISH("ENGLISH"),
    SPANISH("SPANISH"),
    FRENCH("FRENCH"),
    GERMAN("GERMAN"),
    SLOVAK("SLOVAK"),
    ITALIAN("ITALIAN"),
    CHINESE("CHINESE"),
    RUSSIAN("RUSSIAN");

    private final String name;

    LanguageName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
