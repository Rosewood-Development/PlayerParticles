package dev.esophose.playerparticles.locale;

import java.util.Map;

public interface Locale {

    String getLocaleName();

    String getTranslatorName();

    Map<String, String> getDefaultLocaleStrings();

}
