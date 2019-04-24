import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;

public class KriegsrahmenZeit {

    public enum Platform {

        PC ("pc"),
        PS4 ("ps4"),
        XB1 ("xb1"),
        SWI ("swi");

        private String code;

        private Platform(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }

    public enum Location {

        CETUS (
                "Cetus",
                "cetusCycle",
                50 * 60 * 1000,       // 100 minutes (DAY)
                100 * 60 * 1000,        // 50 minutes (NIGHT)
                "isDay",
                "expiry"
        ),

        ORB_VALLIS (
                "Orb Vallis",
                "vallisCycle",
                (6 * 60 + 40) * 1000,    // 6 minutes and 40 seconds (WARM)
                20 * 60 * 1000,        // 20 minutes (COLD)
                "isWarm",
                "expiry"
        );

        private String fullName;
        private String code;
        private int mainPhaseLength;
        private int lastPhaseLength;
        private String mainPhaseKeyword;
        private String expiryKeyword;

        private Location(
                String fullName,
                String code,
                int mainPhaseLength,
                int lastPhaseLength,
                String mainPhaseKeyword,
                String expiryKeyword)
        {
            this.fullName = fullName;
            this.code = code;
            this.mainPhaseLength = mainPhaseLength;
            this.lastPhaseLength = lastPhaseLength;
            this.mainPhaseKeyword = mainPhaseKeyword;
            this.expiryKeyword = expiryKeyword;
        }

        public String getFullName() {
            return this.fullName;
        }

        public String getCode() {
            return this.code;
        }

        public int getMainPhaseLength() {
            return this.mainPhaseLength;
        }

        public int getLastPhaseLength() {
            return this.lastPhaseLength;
        }

        public String getMainPhaseKeyword() {
            return this.mainPhaseKeyword;
        }

        public String getExpiryKeyword() {
            return this.expiryKeyword;
        }

        public int getCycleLength() {
            return  this.mainPhaseLength + this.lastPhaseLength;
        }
    }


    private static final String WARFRAME_API_URL = "https://api.warframestat.us";
    private static final String REQUEST_PROPERTY_KEY = "User-Agent";
    private static final String REQUEST_PROPERTY_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    private static final int CONNECTION_TIMEOUT = 20000; // ms
    private static final long DATA_EXPIRY_TIMEOUT = 3 * 60 * 60 * 1000; // 3 hours in ms

    private static final String DATE_REGEX = "([0-9]*)-([0-9]*)-([0-9]*)T([0-9]*):([0-9]*):([0-9]*)\\.([0-9]*)Z";

    public static final long CYCLES_OFFSET = 12 * 60 * 60 * 1000; // 12 hour offset to accommodate negative timezones

    private HashMap<String, String> dataMap;
    private boolean mainPhaseEh;
    private GregorianCalendar expiry;

    private GregorianCalendar lastUpdateDate;

    private boolean statusOkEh;
    private boolean expiredEh;

    private int reloadCounter;

    private String result;
    private String shortResult;

    private String fullName;
    private String url;
    private long mainPhaseLength;
    private long lastPhaseLength;
    private long cycleLength;
    private long cyclesPerTwoDays;
    private String mainPhaseKeyword;
    private String expiryKeyword;

    public KriegsrahmenZeit(Platform platform, Location location) {

        fullName = location.getFullName();
        url = WARFRAME_API_URL + "/" + platform.getCode() + "/" + location.getCode();
        mainPhaseLength = location.getMainPhaseLength(); // ms
        lastPhaseLength = location.getLastPhaseLength(); // ms
        cycleLength = location.getCycleLength();         // ms
        cyclesPerTwoDays = (long) ceil(48d * 60 * 60 * 1000 / cycleLength);
        mainPhaseKeyword = location.getMainPhaseKeyword();
        expiryKeyword = location.getExpiryKeyword();

        statusOkEh = false;
        expiredEh = true;
        result = "unknown";

        lastUpdateDate = new GregorianCalendar();

        reloadCounter = 0;

        dataMap = new HashMap<>();

        mainPhaseEh = false;
        expiry = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
    }

    public void requestNewData() {

        try {

            URL url = new URL(this.url);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty(REQUEST_PROPERTY_KEY, REQUEST_PROPERTY_VALUE);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();

            String data = readContent(connection);

            dataMap = parseData(data);
            mainPhaseEh = dataMap.get(mainPhaseKeyword).equals("true");
            expiry = parseDate(dataMap.get(expiryKeyword), expiry.getTimeZone());

            lastUpdateDate = new GregorianCalendar();

            statusOkEh = true;
            expiredEh = false;
            result = "Success";
            shortResult = "Sync with " + fullName + " complete.";

            reloadCounter++;

        } catch (SocketTimeoutException e) {
            statusOkEh = false;
            result = "failed with SocketTimeoutException: " + e.getMessage();
            shortResult = "Connection Timed Out after " + (CONNECTION_TIMEOUT / 1000) + "s";
        } catch (MalformedURLException e) {
            statusOkEh = false;
            result = "failed with MalformedURLException: " + e.getMessage();
            shortResult = "Borked URL";
        } catch (IOException e) {
            statusOkEh = false;
            result = "failed with IOException: " + e.getMessage();
            shortResult = "Connection Failed";
        }
    }

    public ArrayList<ArrayList<GregorianCalendar>> getCycleList(GregorianCalendar date) {

        ArrayList<ArrayList<GregorianCalendar>> cycleList = new ArrayList<>();

        if (!statusOkEh) { return cycleList; }

        GregorianCalendar dateUtc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        dateUtc.setTimeInMillis(date.getTimeInMillis());

        GregorianCalendar dateMidnightUtc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        dateMidnightUtc.set(
                dateUtc.get(Calendar.YEAR),
                dateUtc.get(Calendar.MONTH),
                dateUtc.get(Calendar.DAY_OF_MONTH),
                0, 0, 0
        );
        dateMidnightUtc.setTimeInMillis(dateMidnightUtc.getTimeInMillis() - CYCLES_OFFSET);

        long offsetInMillis = expiry.getTimeInMillis() - dateMidnightUtc.getTimeInMillis();
        long cycleStart = dateMidnightUtc.getTimeInMillis() + (offsetInMillis % cycleLength);

        if (offsetInMillis > 0) { cycleStart -= cycleLength; }

        if (!mainPhaseEh) { cycleStart -= mainPhaseLength; }

        for (int i = 0; i <= cyclesPerTwoDays; i++) {

            ArrayList<GregorianCalendar> cycle = new ArrayList<>();

            GregorianCalendar mainPhaseStart = new GregorianCalendar();
            GregorianCalendar mainPhaseEnd = new GregorianCalendar();

            mainPhaseStart.setTimeInMillis(cycleStart + (i * cycleLength));
            mainPhaseEnd.setTimeInMillis(cycleStart + (i * cycleLength) + mainPhaseLength);

            cycle.add(mainPhaseStart);
            cycle.add(mainPhaseEnd);

            cycleList.add(cycle);
        }

//        printCycleList(cycleList);

        return cycleList;
    }

    private void printCycleList(ArrayList<ArrayList<GregorianCalendar>> cycleList) {
        int i = 1;
        for (ArrayList<GregorianCalendar> cycle : cycleList) {
            int j = 0;
            for (GregorianCalendar date : cycle) {
                System.out.println(i + ". " + ((j % 2 == 0) ? "Start" : "End") + " = " + date.getTime());
                j++;
            }
            i++;
        }
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

    public boolean dataExpiredEh() {

        long curentTimeMs = new GregorianCalendar().getTimeInMillis();
        long lastUpdateTimeMs = lastUpdateDate.getTimeInMillis();

        if (abs(curentTimeMs - lastUpdateTimeMs) > DATA_EXPIRY_TIMEOUT) {
            expiredEh = true;
        }

        return expiredEh;
    }


    // Gettttterers
    public boolean getStatusOkEh() {
        return statusOkEh;
    }

    public String getResult() {
        return result;
    }

    public String getShortResult() {
        return shortResult;
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    public int getReloadCounter() {
        return reloadCounter;
    }

    public boolean cetusDayEh() {
        return mainPhaseEh;
    }

    public GregorianCalendar getExpiry() {
        return expiry;
    }

}
