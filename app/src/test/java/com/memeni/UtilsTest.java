package com.memeni;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.menemi.BuildConfig;
import com.menemi.personobject.PayPlan;
import com.menemi.utils.Utils;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ui-Developer on 04.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UtilsTest {
    Bitmap bmpMoreWidth;
    Bitmap bmpMoreHeight;
    Bitmap bmpSmall;
    int initialWidth = 1500;
    int initialHeight = 1300;
    int dstH = 1280;
    int dstW = 1500;


    @Before
    public void setUp() throws Exception {
        int[] colors = new int[initialWidth * initialHeight];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.BLACK;
        }
        int[] colorsSmall = new int[800 * 600];
        for (int i = 0; i < colorsSmall.length; i++) {
            colorsSmall[i] = Color.BLACK;
        }
        bmpMoreWidth = Bitmap.createBitmap(colors, initialWidth, initialHeight, Bitmap.Config.ARGB_8888);
        bmpMoreHeight = Bitmap.createBitmap(colors, initialHeight, initialWidth, Bitmap.Config.ARGB_8888);
        bmpSmall = Bitmap.createBitmap(colorsSmall, 800, 600, Bitmap.Config.ARGB_8888);


    }


    @Test
    public void bimapNull() throws Exception {
        try {
            assertNotEquals(bmpMoreWidth, null);
            assertNotEquals(bmpMoreHeight, null);
            assertNotEquals(bmpSmall, null);
        } catch (RuntimeException ignored) {
            // Multidex support doesn't play well with Robolectric yet
        }

    }

    @Test
    public void initialBitmapSize() throws Exception {
        assertEquals(bmpMoreWidth.getWidth(), initialWidth);
        assertEquals(bmpMoreWidth.getHeight(), initialHeight);

        assertEquals(bmpMoreHeight.getWidth(), initialHeight);
        assertEquals(bmpMoreHeight.getHeight(), initialWidth);

    }
    @Test
    public void initialBitmapSmallSize() throws Exception {
        assertEquals(bmpSmall.getWidth(), 800);
        assertEquals(bmpSmall.getHeight(), 600);
    }

    @Test
    public void getScaledBitmap() throws Exception {

        assertEquals(Utils.scaleBitmapToMin(bmpMoreWidth).getWidth(), 1280);
        assertNotEquals(Utils.scaleBitmapToMin(bmpMoreWidth).getHeight(), initialHeight);

        assertEquals(Utils.scaleBitmapToMin(bmpMoreHeight).getHeight(), 1280);
        assertNotEquals(Utils.scaleBitmapToMin(bmpMoreHeight).getWidth(), initialHeight);



    }

    @Test
    public void getScaledBitmapSmall() throws Exception {

        assertEquals(Utils.scaleBitmapToMin(bmpSmall).getWidth(), 800);
        assertEquals(Utils.scaleBitmapToMin(bmpSmall).getHeight(), 600);


    }
    @Test
    public void stringToBoolTest() throws Exception {

        assertTrue(Utils.stringToBool("true") );
        assertFalse(Utils.stringToBool("false") );
        assertTrue(Utils.stringToBool("TrUe") );
        assertFalse(Utils.stringToBool("FaLsE") );


    }
    @Test
    public void sortPlansTest0() throws Exception {
        ArrayList<PayPlan> plans = new ArrayList<>();
        plans.add(new PayPlan(0, 100, 100,"true"));
        plans.add(new PayPlan(1, 100, 100,"true"));
        plans.add(new PayPlan(4, 100, 100,"true"));
        plans.add(new PayPlan(5, 100, 100,"true"));
        plans.add(new PayPlan(8, 100, 100,"true"));
        plans.add(new PayPlan(9, 100, 100,"true"));

        plans.add(new PayPlan(2, 100, 100,"false"));
        plans.add(new PayPlan(3, 100, 100,"false"));
        plans.add(new PayPlan(6, 100, 100,"false"));
        plans.add(new PayPlan(7, 100, 100,"false"));
        plans.add(new PayPlan(10, 100, 100,"false"));
        plans.add(new PayPlan(11, 100, 100,"false"));

        Utils.sortPlans(plans);

        sortPlansTestBase(plans);
    }
    @Test
    public void sortPlansTest1() throws Exception {
        ArrayList<PayPlan> plans = new ArrayList<>();
        plans.add(new PayPlan(0, 100, 100,"true"));
        plans.add(new PayPlan(1, 100, 100,"true"));
        plans.add(new PayPlan(2, 100, 100,"false"));
        plans.add(new PayPlan(3, 100, 100,"false"));
        plans.add(new PayPlan(4, 100, 100,"true"));
        plans.add(new PayPlan(5, 100, 100,"true"));
        plans.add(new PayPlan(6, 100, 100,"false"));
        plans.add(new PayPlan(7, 100, 100,"false"));
        plans.add(new PayPlan(8, 100, 100,"true"));
        plans.add(new PayPlan(9, 100, 100,"true"));
        plans.add(new PayPlan(10, 100, 100,"false"));
        plans.add(new PayPlan(11, 100, 100,"false"));

        Utils.sortPlans(plans);

        sortPlansTestBase(plans);
    }
    @Test
    public void testEmails() throws Exception {
        String[] valid = {"me@example.com",

        "a.nonymous@example.com",
        "name+tag@example.com",
        "name\\@tag@example.com", // – this is a valid email address containing two @ symbols.
                "spaces\\ are\\ allowed@example.com",
        "\"spaces may be quoted\"@example.com",
        "!#$%&'+-/=.?^`{|}~@[1.0.0.127]",
        "!#$%&'+-/=.?^`{|}~@[IPv6:0123:4567:89AB:CDEF:0123:4567:89AB:CDEF]",
        "me(this is a comment)@example.com"};// – comments are discouraged but not prohibited by RFC2822.




        String[] invalid = {"me@", "@example.com","me.@example.com",
                ".me@example.com",
                "me@example..com",
        "me.example@com",
        "me\\@example.com"};


        assertTrue( Utils.isEmailValid(valid[2]));
    }
    @Test
    public void sortPlansTest2() throws Exception {
        ArrayList<PayPlan> plans = new ArrayList<>();
        Utils.sortPlans(plans);
    }
    @Test
    public void sortPlansTest3() throws Exception {
        ArrayList<PayPlan> plans = new ArrayList<>();

        plans.add(new PayPlan(0, 100, 100,"false"));
        plans.add(new PayPlan(1, 100, 100,"false"));
        plans.add(new PayPlan(2, 100, 100,"true"));
        plans.add(new PayPlan(3, 100, 100,"true"));
        plans.add(new PayPlan(4, 100, 100,"true"));
        plans.add(new PayPlan(5, 100, 100,"true"));
        plans.add(new PayPlan(6, 100, 100,"false"));
        plans.add(new PayPlan(7, 100, 100,"false"));
        plans.add(new PayPlan(8, 100, 100,"true"));
        plans.add(new PayPlan(9, 100, 100,"true"));
        plans.add(new PayPlan(10, 100, 100,"false"));
        plans.add(new PayPlan(11, 100, 100,"false"));

        Utils.sortPlans(plans);
        sortPlansTestBase(plans);

    }
    @Test
    public void sortPlansTest4() throws Exception {
        ArrayList<PayPlan> plans = new ArrayList<>();
        plans.add(new PayPlan(0, 100, 100,"false"));
        plans.add(new PayPlan(1, 100, 100,"false"));
        plans.add(new PayPlan(2, 100, 100,"false"));
        plans.add(new PayPlan(3, 100, 100,"false"));
        plans.add(new PayPlan(4, 100, 100,"false"));
        plans.add(new PayPlan(5, 100, 100,"false"));
        plans.add(new PayPlan(6, 100, 100,"false"));
        plans.add(new PayPlan(7, 100, 100,"false"));
        plans.add(new PayPlan(8, 100, 100,"false"));

        Utils.sortPlans(plans);
        for (int i = 0; i < plans.size(); i++) {
                assertFalse(plans.get(i).isPopular() );
                assertEquals(plans.get(i).getId(), i);
        }

    }
    @Test
    public void sortPlansTest5() throws Exception {
        ArrayList<PayPlan> plans = new ArrayList<>();

        plans.add(new PayPlan(0, 100, 100,"true"));
        plans.add(new PayPlan(1, 100, 100,"true"));
        plans.add(new PayPlan(2, 100, 100,"true"));
        plans.add(new PayPlan(3, 100, 100,"true"));
        plans.add(new PayPlan(4, 100, 100,"true"));
        plans.add(new PayPlan(5, 100, 100,"true"));
        plans.add(new PayPlan(6, 100, 100,"true"));
        plans.add(new PayPlan(7, 100, 100,"true"));
        plans.add(new PayPlan(8, 100, 100,"true"));
        plans.add(new PayPlan(9, 100, 100,"true"));
        plans.add(new PayPlan(10, 100, 100,"true"));
        plans.add(new PayPlan(11, 100, 100,"true"));

        Utils.sortPlans(plans);
        for (int i = 0; i < plans.size(); i++) {
            assertTrue(plans.get(i).isPopular() );
            assertEquals(plans.get(i).getId(), i);
        }

    }
    private void sortPlansTestBase(ArrayList<PayPlan> plans){
        int minimalTrue = -1;
        int minimalFalse = -1;
        for (int i = 0; i < plans.size(); i++) {
            if(i < 6){
                assertTrue(plans.get(i).isPopular());
                assertTrue(plans.get(i).getId() > minimalTrue);
                minimalTrue = plans.get(i).getId();

            } else{
                assertFalse(plans.get(i).isPopular() );
                assertTrue(plans.get(i).getId() > minimalFalse);
                minimalFalse = plans.get(i).getId();
            }
        }
    }

    @Test
    public void dateTest1() throws Exception {
        Date date = Utils.getDateFromServer("2016-08-12T16:44:24.640+00:00");
        assertEquals(date.toString(), "2016-08-15");
        assertEquals(Utils.getStringTimeFromDate(date), "19:44"); // as computer in +3

    }
    @Test
    public void dateTest2() throws Exception {

        Date date = Utils.getDateFromServer2("2016-08-15T16:44:24.640+00:00");
        Date date1 = Utils.getDateFromServer2("2016-08-14T16:44:24.640+00:00");
        Date date2 = Utils.getDateFromServer2("2016-08-13T16:44:24.640+00:00");
        Date date3 = Utils.getDateFromServer2("2016-07-13T16:44:24.640+00:00");
        Date date4 = Utils.getDateFromServer2("2015-08-13T16:44:24.640+00:00");
        assertEquals(date.toString(), "2016-08-15");
        assertEquals("19:44", Utils.getMessageTimeForChat(date)); // as computer in +3
        assertEquals("Yesterday", Utils.getMessageTimeForChat(date1)); // as computer in +3
        assertEquals("2016-08-13", Utils.getMessageTimeForChat(date2)); // as computer in +3
        assertEquals("2016-07-13", Utils.getMessageTimeForChat(date3)); // as computer in +3
        assertEquals("2015-08-13", Utils.getMessageTimeForChat(date4)); // as computer in +3

    }
    @Test
    public void  JSONArrayToIntArrayTest() throws Exception{
        JSONArray j = new JSONArray();
        j.put(1);
        j.put(2);
        j.put(3);
        j.put(4);
        assertArrayEquals(new int[]{1,2,3,4},Utils.JSONArrayToIntArray(j));
    }

}
