package dev.esophose.playerparticles.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringPlaceholders {

    private Map<String, String> placeholders;

    public StringPlaceholders() {
        this.placeholders = new HashMap<>();
    }

    public void addPlaceholder(String placeholder, Object value) {
        this.placeholders.put(placeholder, objectToString(value));
    }

    public String apply(String string) {
        for (String key : this.placeholders.keySet())
            string = string.replaceAll(Pattern.quote('%' + key + '%'), Matcher.quoteReplacement(this.placeholders.get(key)));
        return string;
    }

    public Map<String, String> getPlaceholders() {
        return Collections.unmodifiableMap(this.placeholders);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String placeholder, Object value) {
        return new Builder(placeholder, objectToString(value));
    }

    public static StringPlaceholders empty() {
        return builder().build();
    }

    public static StringPlaceholders single(String placeholder, Object value) {
        return builder(placeholder, value).build();
    }

    private static String objectToString(Object object) {
        return object != null ? object.toString() : "null";
    }

    public static class Builder {

        private StringPlaceholders stringPlaceholders;

        private Builder() {
            this.stringPlaceholders = new StringPlaceholders();
        }

        private Builder(String placeholder, Object value) {
            this();
            this.stringPlaceholders.addPlaceholder(placeholder, objectToString(value));
        }

        public Builder addPlaceholder(String placeholder, Object value) {
            this.stringPlaceholders.addPlaceholder(placeholder, objectToString(value));
            return this;
        }

        public String apply(String string) {
            return this.stringPlaceholders.apply(string);
        }

        public StringPlaceholders build() {
            return this.stringPlaceholders;
        }

    }

}