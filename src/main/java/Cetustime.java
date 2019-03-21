import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class Cetustime {

    private static final String CETUS_TIME_URL = "https://api.warframestat.us/pc/cetusCycle";
    private static final String DATE_REGEX = "([0-9]*)-([0-9]*)-([0-9]*)T([0-9]*):([0-9]*):([0-9]*)\\.([0-9]*)Z";

    private static final String REQUEST_PROPERTY_KEY = "User-Agent";
    private static final String REQUEST_PROPERTY_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    private static final String ISDAY_KEY = "isDay";
    private static final String EXPIRY_KEY = "expiry";

    private static final long CETUS_DATA_EXPIRY_TIMEOUT = 3 * 60 * 60 * 1000; // 3 hours in ms

    public static final int DAY_LENGTH = 100 * 60 * 1000;              // in ms
    public static final int NIGHT_LENGTH = 50 * 60 * 1000;             // in ms
    public static final int CYCLE_LENGTH = DAY_LENGTH + NIGHT_LENGTH;  // in ms

    public static final int CYCLES_PER_DAY = (int) ceil(24d * 60 * 60 * 1000 / CYCLE_LENGTH);

    private HashMap<String, String> dataMap;
    private boolean dayEh;
    private GregorianCalendar expiry;

    private GregorianCalendar lastUpdateDate;

    private boolean cetusTimeOkEh;
    private boolean cetusTimeExpiredEh;

    private int reloadCounter;

    private String result;

    public Cetustime() {

        cetusTimeOkEh = false;
        cetusTimeExpiredEh = true;
        result = "unknown";

        lastUpdateDate = new GregorianCalendar();

        reloadCounter = 0;

        dataMap = new HashMap<>();

        dayEh = false;
        expiry = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

    }

    public boolean cetusDayEh() {
        return dayEh;
    }

    public GregorianCalendar getCetusExpiry() {
        return expiry;
    }

    public void requestNewData() {

        try {

            URL url = new URL(CETUS_TIME_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty(REQUEST_PROPERTY_KEY, REQUEST_PROPERTY_VALUE);
            connection.connect();

            String data = readContent(connection);

            dataMap = parseData(data);
            dayEh = dataMap.get(ISDAY_KEY).equals("true");
            expiry = parseDate(dataMap.get(EXPIRY_KEY), expiry.getTimeZone());

            lastUpdateDate = new GregorianCalendar();

            cetusTimeOkEh = true;
            cetusTimeExpiredEh = false;
            result = "Success";

            reloadCounter++;

        } catch (IOException e) {
            cetusTimeOkEh = false;
            result = "failed with IOException: " + e.getMessage();
        } catch (NullPointerException e) {
            cetusTimeOkEh = false;
            result = "failed with NullPointerException: " + e.getMessage();
        } catch (NumberFormatException e) {
            cetusTimeOkEh = false;
            result = "failed with NumberFormatException: " + e.getMessage();
        }
    }

    public ArrayList<ArrayList<GregorianCalendar>> getNightList(GregorianCalendar date) {

        ArrayList<ArrayList<GregorianCalendar>> nightList = new ArrayList<>();

        if (!cetusTimeOkEh) { return nightList; }

        GregorianCalendar dateUtc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        dateUtc.setTimeInMillis(date.getTimeInMillis());

        GregorianCalendar dateMidnightUtc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        dateMidnightUtc.set(
                dateUtc.get(Calendar.YEAR),
                dateUtc.get(Calendar.MONTH),
                dateUtc.get(Calendar.DAY_OF_MONTH),
                0, 0, 0
        );

        long offsetInMillis = expiry.getTimeInMillis() - dateMidnightUtc.getTimeInMillis();
        long cycleStart = dateMidnightUtc.getTimeInMillis() + (offsetInMillis % CYCLE_LENGTH);

        if (offsetInMillis > 0) { cycleStart -= CYCLE_LENGTH; }

        if (!dayEh) { cycleStart -= NIGHT_LENGTH; }

        for (int i = 0; i <= CYCLES_PER_DAY; i++) {

            ArrayList<GregorianCalendar> cycle = new ArrayList<>();

            GregorianCalendar nightStart = new GregorianCalendar();
            GregorianCalendar nightEnd = new GregorianCalendar();

            nightStart.setTimeInMillis(cycleStart + (i * CYCLE_LENGTH));
            nightEnd.setTimeInMillis(cycleStart + (i * CYCLE_LENGTH) + NIGHT_LENGTH);

            cycle.add(nightStart);
            cycle.add(nightEnd);

            nightList.add(cycle);
        }

        return nightList;
    }

    private GregorianCalendar parseDate(String dateString, TimeZone timeZone) throws NumberFormatException {

        GregorianCalendar calendar = new GregorianCalendar(timeZone);

        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(dateString);

        if (matcher.matches()) {

            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            int hour = Integer.parseInt(matcher.group(4));
            int minute = Integer.parseInt(matcher.group(5));
            int second = Integer.parseInt(matcher.group(6));

            calendar.set(year, month - 1, day, hour, minute, second);
        }

        return calendar;
    }

    private HashMap<String, String> parseData(String data) {

        HashMap<String, String> parsedData = new HashMap<>();
        if (data == null) { return parsedData; }

        String trimmedData = data.replaceAll("[\\{\\}\\n\\r]", "");
        String[] itemArray = trimmedData.split(",");

        for (int i = 0; i < itemArray.length; i++) {

            String item = itemArray[i];
            String[] itemParts = item.split("\":");

            String itemKey = "unknown";
            String itemValue = "unknown";

            if (itemParts.length >= 1) { itemKey = itemParts[0]; }
            if (itemParts.length >= 2) { itemValue = itemParts[1]; }

            itemKey = itemKey.replace("\"", "");
            itemValue = itemValue.replace("\"", "");

            parsedData.put(itemKey, itemValue);
        }

        return parsedData;
    }

    private String readContent(URLConnection connection) throws IOException {

        StringBuilder content = new StringBuilder();

        if (connection == null) { return content.toString(); }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String input;
        while ((input = bufferedReader.readLine()) != null) { content.append(input).append("\n"); }

        bufferedReader.close();

        return content.toString();
    }

    public boolean cetusTimeExpiredEh() {

        long curentTimeMs = new GregorianCalendar().getTimeInMillis();
        long lastUpdateTimeMs = lastUpdateDate.getTimeInMillis();

        if (abs(curentTimeMs - lastUpdateTimeMs) > CETUS_DATA_EXPIRY_TIMEOUT) {
            cetusTimeExpiredEh = true;
        }

        return cetusTimeExpiredEh;
    }


    // Gettttterers
    public boolean cetusTimeOkEh() {
        return cetusTimeOkEh;
    }

    public String getResult() {
        return result;
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    public int getReloadCounter() {
        return reloadCounter;
    }
}
