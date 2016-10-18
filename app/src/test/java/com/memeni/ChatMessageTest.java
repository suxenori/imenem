package com.menemi;


import com.menemi.dbfactory.stream.messages.DialogSendMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertEquals;
/**
 * Created by Ui-Developer on 23.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChatMessageTest {

        @Test
        public void jsonTest() throws Exception {
       DialogSendMessage cm = new DialogSendMessage(1,2,"Hello");
            DialogSendMessage cm2 = new DialogSendMessage(2,3, "olleH");
        assertEquals("{\"message_body\":\"Hello\",\"command\":\"ZCMD_SEND_MESSAGE\",\"profile_id\":\"1\"}", cm.toJSON().toString());
        assertEquals("{\"message_body\":\"olleH\",\"command\":\"ZCMD_SEND_MESSAGE\",\"profile_id\":\"2\"}", cm2.toJSON().toString());

        }

    }


