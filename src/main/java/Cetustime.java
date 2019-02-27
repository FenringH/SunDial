import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static final int DAY_LENGTH = 100;
    private static final int NIGHT_LENGTH = 50;
    private static final int CYCLE_LENGTH = DAY_LENGTH + NIGHT_LENGTH;
    private static final int DAY_CYCLES = (int) ceil(24d * 60 / CYCLE_LENGTH);

    private HashMap<String, String> dataMap;
    private boolean dayEh;
    private GregorianCalendar expiry;

    private boolean okEh;
    private String result;

    public Cetustime() {

        okEh = true;
        result = "success";

        dataMap = new HashMap<>();

        dayEh = false;
        expiry = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

        requestNewData();
    }

    public boolean cetusDayEh() {
        return dayEh;
    }

    public GregorianCalendar getCetusExpiry() {
        return expiry;
    }

    public void requestNewData() {

        okEh = true;

        try {

            URL url = new URL(CETUS_TIME_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty(REQUEST_PROPERTY_KEY, REQUEST_PROPERTY_VALUE);
            connection.connect();

            String data = readContent(connection);

            dataMap = parseData(data);
            dayEh = dataMap.get(ISDAY_KEY).equals("true");
            expiry = parseDate(dataMap.get(EXPIRY_KEY), expiry.getTimeZone());

        } catch (IOException e) {
            okEh = false;
            result = "failed with IOException: " + e.getMessage();
        } catch (NullPointerException e) {
            okEh = false;
            result = "failed with NullPointerException: " + e.getMessage();
        } catch (NumberFormatException e) {
            okEh = false;
            result = "failed with NumberFormatException: " + e.getMessage();
        }
    }

    public ArrayList<ArrayList<GregorianCalendar>> getNightList(GregorianCalendar date) {

        ArrayList<ArrayList<GregorianCalendar>> nightList = new ArrayList<>();

        GregorianCalendar dateUtc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        dateUtc.setTimeInMillis(date.getTimeInMillis());

        GregorianCalendar dateStartUtc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        dateStartUtc.set(
                dateUtc.get(Calendar.YEAR),
                dateUtc.get(Calendar.MONTH),
                dateUtc.get(Calendar.DAY_OF_MONTH),
                0, 0, 0
        );

        long offsetInMillis = expiry.getTimeInMillis() - dateStartUtc.getTimeInMillis();
        long numberOfcycles = (long) floor(offsetInMillis / (1000d * 60 * CYCLE_LENGTH));

        int direction = (offsetInMillis > 0) ? 1 : -1;

        for (int i = 0; i < DAY_CYCLES; i++) {
            long cycleStart = expiry.getTimeInMillis() + (i * CYCLE_LENGTH * numberOfcycles * 1000 * 60);
        }

        return nightList;
    }

    private GregorianCalendar parseDate(String dateString, TimeZone timeZone) throws NumberFormatException {

        GregorianCalendar calendar = new GregorianCalendar(timeZone);

        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(dateString);

        if (matcher.matches()) {

            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2)) + 1;
            int day = Integer.parseInt(matcher.group(3));
            int hour = Integer.parseInt(matcher.group(4));
            int minute = Integer.parseInt(matcher.group(5));
            int second = Integer.parseInt(matcher.group(6));

            calendar.set(year, month, day, hour, minute, second);
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

        String content = "";

        if (connection == null) { return content; }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String input;
        while ((input = bufferedReader.readLine()) != null) { content += input + "\n"; }

        if (bufferedReader != null) { bufferedReader.close(); }

        return content;
    }


    // Gettttterers
    public boolean isOkEh() {
        return okEh;
    }

    public String getResult() {
        return result;
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }
}
