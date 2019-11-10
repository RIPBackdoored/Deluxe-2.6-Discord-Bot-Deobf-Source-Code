/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;

public class Lang {
    private static final Map<NameType, Lang> Langs = new EnumMap<NameType, Lang>(NameType.class);
    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
    private final Languages languages;
    private final List<LangRule> rules;

    public static Lang instance(NameType nameType) {
        return Langs.get((Object)nameType);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Lang loadFromResource(String languageRulesResourceName, Languages languages) {
        ArrayList<LangRule> rules = new ArrayList<LangRule>();
        InputStream lRulesIS = Lang.class.getClassLoader().getResourceAsStream(languageRulesResourceName);
        if (lRulesIS == null) {
            throw new IllegalStateException("Unable to resolve required resource:org/apache/commons/codec/language/bm/lang.txt");
        }
        Scanner scanner = new Scanner(lRulesIS, "UTF-8");
        try {
            boolean inExtendedComment = false;
            while (scanner.hasNextLine()) {
                String rawLine;
                String line = rawLine = scanner.nextLine();
                if (inExtendedComment) {
                    if (!line.endsWith("*/")) continue;
                    inExtendedComment = false;
                    continue;
                }
                if (line.startsWith("/*")) {
                    inExtendedComment = true;
                    continue;
                }
                int cmtI = line.indexOf("//");
                if (cmtI >= 0) {
                    line = line.substring(0, cmtI);
                }
                if ((line = line.trim()).length() == 0) continue;
                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Malformed line '" + rawLine + "' in language resource '" + languageRulesResourceName + "'");
                }
                Pattern pattern = Pattern.compile(parts[0]);
                String[] langs = parts[1].split("\\+");
                boolean accept = parts[2].equals("true");
                rules.add(new LangRule(pattern, new HashSet<String>(Arrays.asList(langs)), accept));
            }
            return new Lang(rules, languages);
        }
        finally {
            scanner.close();
        }
    }

    private Lang(List<LangRule> rules, Languages languages) {
        this.rules = Collections.unmodifiableList(rules);
        this.languages = languages;
    }

    public String guessLanguage(String text) {
        Languages.LanguageSet ls = this.guessLanguages(text);
        if (!ls.isSingleton()) return "any";
        String string = ls.getAny();
        return string;
    }

    public Languages.LanguageSet guessLanguages(String input) {
        Languages.LanguageSet languageSet;
        String text = input.toLowerCase(Locale.ENGLISH);
        HashSet<String> langs = new HashSet<String>(this.languages.getLanguages());
        for (LangRule rule : this.rules) {
            if (!rule.matches(text)) continue;
            if (rule.acceptOnMatch) {
                langs.retainAll(rule.languages);
                continue;
            }
            langs.removeAll(rule.languages);
        }
        Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
        if (ls.equals(Languages.NO_LANGUAGES)) {
            languageSet = Languages.ANY_LANGUAGE;
            return languageSet;
        }
        languageSet = ls;
        return languageSet;
    }

    static {
        NameType[] arr$ = NameType.values();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            NameType s = arr$[i$];
            Langs.put(s, Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(s)));
            ++i$;
        }
    }

    private static final class LangRule {
        private final boolean acceptOnMatch;
        private final Set<String> languages;
        private final Pattern pattern;

        private LangRule(Pattern pattern, Set<String> languages, boolean acceptOnMatch) {
            this.pattern = pattern;
            this.languages = languages;
            this.acceptOnMatch = acceptOnMatch;
        }

        public boolean matches(String txt) {
            return this.pattern.matcher(txt).find();
        }
    }

}

