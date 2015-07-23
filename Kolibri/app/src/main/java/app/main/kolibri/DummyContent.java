package app.main.kolibri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {
    private static int lastId = 0;
    /**
     * An array of sample (dummy) items.
     */
    public static ArrayList<DummyItem> ITEMS = new ArrayList<DummyItem>();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        Timer timer = new Timer("setList ");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateMap();
            }
        },1000,1000*2*60);
    }
    private static void updateMap() {
        DataSingleton dataSingleton = DataSingleton.getInstance();
        Map<String,Float> distances = dataSingleton.getDistances();
        lastId = 0;
        for(String i: distances.keySet()) {
            addItem(new DummyItem(Integer.toString(lastId), i + " - " + Math.round(distances.get(i))));
            lastId++;
            System.out.println(i);
        }
    }
    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
