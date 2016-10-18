package com.menemi;


import com.menemi.dbfactory.stream.messages.ReadMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ui-Developer on 23.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ReadMessageTest {

        @Test
        public void jsonTest() throws Exception {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(1);
            ids.add(2);
            ids.add(3);
            ReadMessage cm = new ReadMessage();
            cm.setIds(ids);
            cm.setProfileID(44);
            cm.setDialogID(45);
        assertEquals("{\"message_body\":\"Hello\",\"command\":\"ZCMD_SEND_MESSAGE\",\"profile_id\":\"1\"}", cm.toJSON().toString());


        }

    }


