package com.eric.xlee.Region;

import java.util.Locale;

/**
 * 国家枚举类，列表出T类型所需要的国家列表
 *
 * @author xlee
 */
public enum RegionEnum {
    AUSTRALIA("en", "AU"), RUSSI("ru", "RU"), SERBIA("sr", "RS"), ESTONIA("et", "EE"), ENGLAND("en", "GB"), GERMANY(
            "de", "DE"), SPAIN("es", "ES"), ITALY(Locale.ITALY), SWEDEN("sv", "SE"), AUSTRIA("de", "AT"), DENMARK("da",
            "DK"), FINLAND("fi", "FI"), THAILAND("th", "TH"), IRAN("fa", "IR"), BRAZIL("pt", "BR"), CHILE("es", "CL"), ARGENTINA(
            "es", "AR"), PERU("es", "PE"), COLOMBIA("es", "CO"), PANAMA("es", "PA"), GREECE("el", "GR"), FRENCH("fr",
            "FR"), BULGARIA("bg", "BG"), HUNGARY("hu", "HU"), POLAND("pl", "PL"), PORTUGAL("pt", "PT"), SLOVAKIA("sk",
            "SK"), CROATIAN("hr", "HR"), CZECH("cs", "CZ"), MACEDONIA("mk", "MK"), NEDERLAND("nl", "NL"), BELGIUM("fr",
            "BE"), UKRAINE("uk", "UA"), TAIWAN("zh", "TW"), VIETNAM("vi", "VN"), QAA("qaa", "qaa"), QAD("qad", "qad"), MUL(
            "mul", "mul");

    Locale locale;
    public static final Locale LOCALE_LANGEUAGE_ENGLISH = new Locale("en", "AU");

    public Object getObject() {

        if (this.equals(QAA)) {
            // return R.string.audio_lan_qaa;
        } else if (this.equals(QAD)) {
            // return R.string.audio_lan_qad;
        } else if (this.equals(MUL)) {
            // return R.string.audio_lan_mul;
        }

        return locale;
    }

    // enum 的构造函数不需要用private来修饰
    // interface 的成员变量都是final static 的，因此也不需要用final static 进行修饰
    RegionEnum(Locale locale) {
        this.locale = locale;
    }

    RegionEnum(String languageCode, String countryCode) {
        this.locale = new Locale(languageCode, countryCode);
    }

    /**
     * 获取以当前设备语言显示的 国家名称
     *
     * @return 国家名称
     */
    public String getName() {
        String name = locale.getDisplayCountry();

        if (name.contains(" [")) {
            name = name.split(" \\[")[0];
        }
        return name;
    }

    /**
     * 获取以当前国家语言显示的 国家名称
     *
     * @return 国家名称
     */
    public String getNameForLocale() {
        String name = locale.getDisplayCountry(this.locale);
        if (name.contains(" [")) {
            name = name.split(" \\[")[0];
        }
        return name;
    }

    /**
     * 获取以英语显示的 国家名称
     *
     * @return
     */
    public String getEnglishName() {
        return getName(LOCALE_LANGEUAGE_ENGLISH);
    }

    /**
     * 获取以showLocale语言显示的国家名称
     *
     * @param showLocale 显示语言 (传入：LOCALE_LANGEUAGE_ENGLISH 则该国家名称显示语言为英文)
     * @return 国家名称
     */
    public String getName(Locale showLocale) {
        String name = locale.getDisplayCountry(showLocale);
        if (name.contains(" [")) {
            name = name.split(" \\[")[0];
        }
        return name;
    }

    public Locale getLocale() {
        return locale;
    }
}