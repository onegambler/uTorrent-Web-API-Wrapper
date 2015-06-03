package com.utorrent.webapiwrapper.core.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ClientSettings {

    private Map<String, Setting> settingsMap;

    public ClientSettings() {
        settingsMap = new HashMap<>();
    }

    public void addSetting(String name, int typeCode, String value) {
        settingsMap.put(name, new Setting(value, SettingType.getTypeFromCode(typeCode)));
    }

    public Setting getSetting(String name) {
        return settingsMap.get(name);
    }

    @Getter
    @AllArgsConstructor
    private class Setting {
        private final String value;
        private final SettingType type;
    }

    public enum SettingType {

        INTEGER(0),
        BOOLEAN(1),
        STRING(2);

        private int code;

        SettingType(Integer code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static SettingType getTypeFromCode(int code) {
            for(SettingType type : SettingType.values()) {
                if (type.getCode() == code) {
                    return type;
                }
            }

            return null;
        }

        public <T> boolean isValueValid(T value) {
            requireNonNull(value, "value cannot be null");
            String stringValue = value.toString();
            switch (this) {
                case BOOLEAN:
                    return stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false");
                case INTEGER:
                    return stringValue.matches("\\d");
                default:
                    return true;
            }
        }
    }
}
