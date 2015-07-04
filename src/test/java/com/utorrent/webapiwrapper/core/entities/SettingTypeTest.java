package com.utorrent.webapiwrapper.core.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SettingTypeTest {

    @Test
    public void testGetTypeFromCode() throws Exception {
        for(ClientSettings.SettingType type : ClientSettings.SettingType.values()) {
            assertEquals(type, ClientSettings.SettingType.getTypeFromCode(type.getCode()));
        }

    }

    @Test
    public void testIsValueValidBoolean() throws Exception {
        ClientSettings.SettingType settingType = ClientSettings.SettingType.BOOLEAN;
        assertTrue(settingType.isValueValid("true"));
        assertTrue(settingType.isValueValid("false"));
        assertTrue(settingType.isValueValid(true));
        assertTrue(settingType.isValueValid(false));
        assertFalse(settingType.isValueValid("ciao"));
        assertFalse(settingType.isValueValid(new Object()));
    }

    public void testIsValueValidInteger() throws Exception {
        ClientSettings.SettingType settingType = ClientSettings.SettingType.INTEGER;
        assertTrue(settingType.isValueValid(1));
        assertTrue(settingType.isValueValid(12));
        assertTrue(settingType.isValueValid("15"));
        assertTrue(settingType.isValueValid("123"));
        assertFalse(settingType.isValueValid("ciao"));
        assertFalse(settingType.isValueValid("12.4"));
        assertFalse(settingType.isValueValid(new Object()));
        assertFalse(settingType.isValueValid(125l));
        assertFalse(settingType.isValueValid(125d));
        assertFalse(settingType.isValueValid("12a"));
        assertFalse(settingType.isValueValid("a12"));
    }
}