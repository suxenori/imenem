package com.menemi;

import com.menemi.personobject.NotificationSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ui-Developer on 08.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class NotificationSettingsTest {
    NotificationSettings testedSettings;
    @Before
    public void setUp() throws Exception {
        testedSettings = new NotificationSettings(1,1);
        testedSettings.setFavorites("email|push|inapp");
        testedSettings.setGifts("push|inapp");
        testedSettings.setMessages("inapp");
        testedSettings.setOther("push|inapp");
        testedSettings.setTheir_likes("push");
    }
    @Test
    public void testedSettingsNull() throws Exception {
            assertNotEquals(testedSettings, null);
    }
    @Test
    public void settingsTests() throws Exception {
        assertTrue(testedSettings.getFavorites(NotificationSettings.SETTINGTYPE.EMAIL));
        assertFalse(testedSettings.getGifts(NotificationSettings.SETTINGTYPE.EMAIL));
        assertTrue(testedSettings.getGifts(NotificationSettings.SETTINGTYPE.INAPP));
        assertTrue(testedSettings.getMessages(NotificationSettings.SETTINGTYPE.INAPP));
        assertFalse(testedSettings.getMessages(NotificationSettings.SETTINGTYPE.EMAIL));
        assertFalse(testedSettings.getMutual_likes(NotificationSettings.SETTINGTYPE.EMAIL));

        assertEquals(testedSettings.getFavorites(), "email|push|inapp");
        assertEquals(testedSettings.getGifts(), "push|inapp");
        assertEquals(testedSettings.getMessages(), "inapp");
        assertEquals(testedSettings.getOther(), "push|inapp");
        assertEquals(testedSettings.getTheir_likes(), "push");
    }
    enum A{
        A,
        B
    }
    @Test
    public void enumTest() throws Exception {

        assertEquals("A", A.valueOf("A").toString());
        //assertEquals("{\"message_body\":\"olleH\",\"command\":\"ZCMD_SEND_MESSAGE\",\"profile_id\":\"2\"}", cm2.toJSON().toString());

    }
}
