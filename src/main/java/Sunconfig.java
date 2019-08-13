import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

import java.text.DecimalFormat;

import static java.lang.Math.*;

public class Sunconfig {

    // Resources
    public static final String DEFAULT_DAY_MAP               = "maps/2k_earth_daymap.jpg";
    public static final String DEFAULT_NIGHT_MAP             = "maps/2k_earth_nightmap.jpg";
    public static final String DEFAULT_EDGE_MAP              = "maps/2k_earth_edge_map.jpg";
    public static final String DEFAULT_SPECULAR_MAP          = "maps/2k_earth_specular_map.jpg";

    public static final Image GLOBE_DAY_IMAGE = Sunutil.convertSphericalToCylindricalMapping(new Image(DEFAULT_DAY_MAP));
    public static final Image GLOBE_NIGHT_IMAGE = Sunutil.convertSphericalToCylindricalMapping(new Image(DEFAULT_NIGHT_MAP));;
    public static final Image GLOBE_SPECULAR_IMAGE = Sunutil.convertSphericalToCylindricalMapping(new Image(DEFAULT_SPECULAR_MAP));;
    public static final Image GLOBE_EDGE_IMAGE = Sunutil.convertSphericalToCylindricalMapping(new Image(DEFAULT_EDGE_MAP));;

    public static final String DEFAULT_LOGO_OSTRON           = "icons/Ostron.png";
    public static final String DEFAULT_LOGO_SOLARIS_UNITED   = "icons/SolarisUnited.png";

    public static final Image LOGO_OSTRON = new Image(DEFAULT_LOGO_OSTRON, 64, 64, true, true);
    public static final Image LOGO_SOLARIS_UNITED = new Image(DEFAULT_LOGO_SOLARIS_UNITED, 64, 64, true, true);

    public static final String DEFAULT_FONT_COURIER_PRIME_CODE = "fonts/Courier Prime Code.ttf";

    public static final Font FONT_DEBUG = Font.loadFont(ClassLoader.getSystemResourceAsStream(DEFAULT_FONT_COURIER_PRIME_CODE), 14);
    public static final Font FONT_CHART_INFO = Font.loadFont(ClassLoader.getSystemResourceAsStream(DEFAULT_FONT_COURIER_PRIME_CODE), 16);
    public static final Font FONT_MINI = Font.loadFont(ClassLoader.getSystemResourceAsStream(DEFAULT_FONT_COURIER_PRIME_CODE), 8);

    public static final String MIRO_URL = "http://www.behance.net/Fenring";
    public static final String MIRO_TEXT = "Created by Miroslav Hundak, 2019.";

    // DEFAULTS Sunface
    public final static String A_BEGINNING =
            "A beginning is a very delicate time.\n" +
            "Know then, that is is the year 10191.\n" +
            "The known universe is ruled by the Padishah Emperor Shaddam the Fourth, my father.\n" +
            "In this time, the most precious substance in the universe is the spice Melange.\n" +
            "The spice extends life. The spice expands consciousness.\n" +
            "A product of the Spice, the red Sapho juice, stains the lips of the Mentats but allows them to be human computers,\n" +
            "as thinking machines have been outlawed. The spice is vital to space travel.\n" +
            "The Spacing Guild and its navigators, who the spice has mutated over 4000 years, use the orange spice gas,\n" +
            "which gives them the ability to fold space. That is, travel to any part of the universe without moving.\n" +
            "Because the Guild controls all interplanetary travel, they are the highest power in the Universe.\n" +
            "The Spice also plays a very secret role in the Bene Gesserit sisterhood, of which I am a part.\n" +
            "The sisterhood has been interfering with the marriages, and the children thereof,\n" +
            "of the great Houses of the Universe, cleverly intermixing one bloodline with another\n" +
            "to form the Kwisatz Haderach, a super being.\n" +
            "They plan to control this super being and use his powers for their own selfish purposes.\n" +
            "The breeding plan has been carried out in a strict manner for 90 generations.\n" +
            "The goal of the super being is in sight.";

    public static final String ICON_APP_URL = "icons/sun1.png";

    public static final double DEFAULT_FPS = 30.0;
    public static final double DEFAULT_LONGITUDE = round(Suntime.DEFAULT_LONGITUDE * 100d) / 100d;
    public static final double DEFAULT_LATITUDE = round(Suntime.DEFAULT_LATITUDE * 100d) / 100d;

    public static final double MIN_WIDTH = 150;
    public static final double MIN_HEIGHT = 150;

    public static final double SNAP_TO_CENTER_RADIUS = 50;

    public static final double NORMAL_STEP_SIZE = 20.0d;
    public static final double FAST_STEP_SIZE = 2.0d;

    public static final DecimalFormat julianDateFormat = new DecimalFormat("###,###,###.00000000");

    public static final String GOOGLEMAPS_REGEX = ".*\\/@([\\+\\-0-9]+\\.[0-9]*),([\\+\\-0-9]+\\.[0-9]*),.*";


    // DEFAULTS Sundial
    public static final double DEFAULT_sunTimeDialAngle      = 0;
    public static final double DEFAULT_highNoonDialAngle     = 10;
    public static final double DEFAULT_sunriseDialAngle      = 20;
    public static final double DEFAULT_sunsetDialAngle       = 30;
    public static final double DEFAULT_localTimeDialAngle    = -5000;

    public static final double MATRIX_SEPARATOR_OFFSET = -1.0d;
    public static final int MAX_MARKER = 96;

    public static final double DEFAULT_nightCompression      = 0;
    public static final double MAX_nightCompression          = 45;
    public static final double MIN_nightCompression          = -45;
    public static final double STEP_nightCompression         = 360d / MAX_MARKER;

    public static final String DEFAULT_localTimeText         = "MMM DDD dd hh:mm:ss ZZZ YYYY";

    public static final String HELPTEXT_DEFAULT = "Hover over highlighted surface" +
            "\nto display available controls.";

    public static final String HELPTEXT_HORIZON = "DAY LENGTH" +
            "\n LMB : toggle Year-Chart";

    public static final String HELPTEXT_NIGHTCOMPRESSION = "NIGHT COMPRESSION\u2122" +
            "\n LMB + drag : change Night-Compression\u2122 (slow)" +
            "\n RMB + drag : change Night-Compression\u2122 (fast)" +
            "\n SCROLL : change Night-Compression\u2122 (fine)" +
            "\n MMB : reset Night-Compression\u2122";

    public static final String HELPTEXT_HOUR = "HOURS" +
            "\n LMB + drag : change Hours (slow)" +
            "\n RMB + drag : change Hours (fast)" +
            "\n SCROLL : change Hours (fine)" +
            "\n MMB : reset Time and Date";

    public static final String HELPTEXT_MINUTE = "MINUTES" +
            "\n LMB + drag : change Minutes (slow)" +
            "\n RMB + drag : change Minutes (fast)" +
            "\n SCROLL : change Minutes (fine)" +
            "\n MMB : reset Time and Date";

    public static final String HELPTEXT_DAY = "DAY" +
            "\n LMB + drag : change Day (slow)" +
            "\n RMB + drag : change Day (fast)" +
            "\n SCROLL : change Day (fine)" +
            "\n MMB : reset Time and Date";

    public static final String HELPTEXT_MONTH = "MONTH" +
            "\n LMB + drag : change Month (slow)" +
            "\n RMB + drag : change Month (fast)" +
            "\n SCROLL : change Month (fine)" +
            "\n MMB : reset Time and Date";

    public static final String HELPTEXT_YEAR = "YEAR" +
            "\n LMB + drag : change Year (slow)" +
            "\n RMB + drag : change Year (fast)" +
            "\n SCROLL : change Year (fine)" +
            "\n MMB : reset Time and Date";

    public static final String HELPTEXT_LONGITUDE = "LONGITUDE" +
            "\n LMB + drag : change Longitude (fast)" +
            "\n RMB + drag : change Longitude (slow)" +
            "\n SCROLL : change Longitude (fine)" +
            "\n MMB : reset Longitude";

    public static final String HELPTEXT_LATITUDE = "LATITUDE" +
            "\n LMB + drag : change Latitude (fast)" +
            "\n RMB + drag : change Latitude (slow)" +
            "\n SCROLL : change Latitude (fine)" +
            "\n MMB : reset Latitude";

    public static final String HELPTEXT_TIMEZONE = "TIME ZONE" +
            "\n SCROLL : change Time Zone" +
            "\n MMB : reset Time Zone";

    public static final String HELPTEXT_CLOSE = "CLOSE" +
            "\n LMB : Close application";

    public static final String HELPTEXT_MINIMIZE = "MINIMIZE" +
            "\n LMB : Minimize application" +
            "\n RMB : toggle Debug window";

    public static final String HELPTEXT_MAXIMIZE = "MAXIMIZE" +
            "\n LMB : Maximize application";

    public static final String HELPTEXT_RESIZE = "RESIZE" +
            "\n LMB + drag : Resize window (squared)" +
            "\n RMB + drag : Resize window" +
            "\n MMB : Reset window size";

    public static final String HELPTEXT_NIGHTMODE = "NIGHT-MODE" +
            "\n LMB : toggle Night-mode\u2122";

    public static final String HELPTEXT_ALWAYSONTOP = "ALWAYS-ON-TOP" +
            "\n LMB : toggle Always-On-Top";

    public static final String HELPTEXT_HIGHNOON = "HIGH NOON" +
            "\n HOVER : toggle High Noon time";

    public static final String HELPTEXT_TINYGLOBE = "TINY GLOBE" +
            "\n LMB : toggle Globe-mode\u2122" +
            "\n RMB : toggle Cetus time" +
            "\n MMB : reset Coordinates to DEFAULT" +
            "\n DRAG-DROP : drag and drop GoogleMaps url ..." +
            "\n  ... to set DEFAULT Coordinates";

    public static final String HELPTEXT_GLOBEGRID = "GLOBE GRID" +
            "\n LMB : toggle Parallels and Meridians";

    public static final String HELPTEXT_GLOBELINES = "GLOBE LINES" +
            "\n LMB : toggle Equator, Tropic and Polar circles";

    public static final String HELPTEXT_GLOBE = "GLOBE" +
            "\n LMB : rotate Globe (fast)" +
            "\n RMB : rotate Globe (slow)" +
            "\n MMB : reset Coordinates";

    public static final String HELPTEXT_WINDOW = "WINDOW" +
            "\n LMB : move Window";

    public static final String HELPTEXT_ANIMATION = "ANIMATION" +
            "\n LMB : toggle Animation";

    public static final String HELPTEXT_PIN_INFO = "PIN INFO" +
            "\n LMB : Pin information and controls";

    public static final String HELPTEXT_CHART = "YEAR CHART" +
            "\n LMB : toggle Year Chart\u2122 window";

    public static final String HELPTEXT_HELP = "HALP !!!" +
            "\n LMB : toggle Help Overlay\u2122";

    public static final String HELPTEXT_CETUS = "CETUS TIME" +
            "\n LMB : toggle Cetus night/day timer";

    public static final String HELPTEXT_ORBVALLIS = "ORB VALIS TIME" +
            "\n LMB : toggle Orb Vallis warm/cold timer";

    public static final String HELPTEXT_DST = "DAYLIGHT SAVING TIME" +
            "\n LMB : toggle Daylight Saving Time";

    public static final String HELPTEXT_MIRO = "Open web page in browser...";

    public static final double MARGIN_X = 6.0d;
    public static final double MARGIN_Y = 6.0d;
    public static final double SCALE_X = 1.0d;
    public static final double SCALE_Y = 1.0d;
    public static final double CENTER_X = Sundial.DEFAULT_WIDTH / 2;
    public static final double CENTER_Y = Sundial.DEFAULT_HEIGHT / 2;

    public static final double DOT_RADIUS_SMOL = 10.0d;
    public static final double DOT_RADIUS_BIGH = 100.0d;
    public static final double SUNTIME_DIAL_LENGTH = 50.0d;
    public static final double SUNRISE_DIAL_LENGTH = CENTER_Y - DOT_RADIUS_SMOL;
    public static final double SUNSET_DIAL_LENGTH = CENTER_Y - DOT_RADIUS_SMOL;
    public static final double SUNRISE_DIAL_SHORT_LENGTH = CENTER_Y - DOT_RADIUS_BIGH;
    public static final double SUNSET_DIAL_SHORT_LENGTH = CENTER_Y - DOT_RADIUS_BIGH;
    public static final double LOCALSECOND_RADIUS_BIG = 5;
    public static final double LOCALSECOND_RADIUS_SMOL = 4;
    public static final double MARKER_HOUR_LENGTH = 10;
    public static final double MARKER_MINUTE_LENGTH = 8.0d;
    public static final double MARKER_MINUTE_WIDTH = 8.0d;
    public static final double LOCALTIME_DIAL_LENGTH = CENTER_Y - DOT_RADIUS_SMOL;
    public static final double LOCALTIME_DIAL_SHORT_LENGTH = CENTER_Y - DOT_RADIUS_BIGH;
    public static final double LOCALTIME_DIAL_SHORTER_LENGTH = CENTER_Y - DOT_RADIUS_BIGH - 20;
    public static final double LOCALTIME_DIAL_MID_LENGTH = Sunconfig.CENTER_Y - Sunconfig.DAYLENGTH_ARC_RADIUS;
    public static final double LOCALTIME_HOUR_WIDTH = 12.0d;
    public static final double LOCALTIME_HOUR_SHORT_WIDTH = 8.0d;
    public static final double LOCALTIME_HOUR_SHORTER_WIDTH = 5.0d;
    public static final double LOCALTIME_HOUR_SHORTEST_WIDTH = 1.0d;
    public static final double LOCALTIME_HOUR_MID_WIDTH = 14.0d;
    public static final double LOCALTIME_MINUTE_WIDTH = 8.0d;
    public static final double HIGHNOON_DIAL_SHORT_LENGTH = CENTER_Y - DOT_RADIUS_BIGH;
    public static final double DAYLENGTH_ARC_RADIUS = 120.0d;
    public static final double HIGHNOON_DIAL_WIDTH = 1.5d;
    public static final double HIGHNOON_DIAL_LENGTH = CENTER_Y - DAYLENGTH_ARC_RADIUS * 0.35;
    public static final double DAY_ARC_MARGIN = 10.0d;
    public static final double LOCALMINUTE_POLY_WIDTH = 6;
    public static final double LOCALMINUTE_POLY_HEIGHT = 20;
    public static final double LOCALMINUTE_WIDTH = 8;
    public static final double LOCALMINUTE_HEIGHT = 16;
    public static final double LOCALMINUTE_ROUND = 6;
    public static final double LOCALSECOND_WIDTH = 8;
    public static final double LOCALSECOND_HEIGHT = 16;
    public static final double LOCALSECOND_POLY_WIDTH = 6;
    public static final double LOCALSECOND_POLY_HEIGHT = 20;
    public static final double LOCALSECOND_ROUND = 6;
    public static final double TINYGLOBE_RADIUS = 40.0d;
    public static final double TINYGLOBE_DOT_RADIUS = 1.0d;
    public static final double CONTROL_HELP_SIZE = 20.0d;
    public static final double CONTROL_THINGY_RADIUS = 10.0d;
    public static final double CONTROL_HELP_RADIUS = 10.0d;
    public static final double CONTROL_RESIZE_SIZE = 45.0d;
    public static final double CONTROL_CLOSE_RADIUS = 10.0d;
    public static final double CONTROL_MAXIMIZE_RADIUS = 10.0d;
    public static final double CONTROL_MINIMIZE_RADIUS = 10.0d;
    public static final double CONTROL_NIGHTMODE_RADIUS = 8.0d;
    public static final double CONTROL_ALWAYSONTOP_RADIUS = 8.0d;
    public static final double CONTROL_ANIMATION_RADIUS = 8.0d;
    public static final double CONTROL_CHART_RADIUS = 8.0d;
    public static final double CONTROL_GLOBEGRID_RADIUS = 8.0d;
    public static final double CONTROL_DST_RADIUS = 7.0d;
    public static final double CETUS_MARKER_LENGTH = 30.0d;
    public static final double ORBVALLIS_MARKER_LENGTH = 21.0d;
    public static final double HELP_OVERLAY_ROUND = Sundial.DEFAULT_WIDTH / 2 + 20.0d;
    public static final double HELP_MARKER_ROUND = 10.0d;
    public static final double HELP_MARKER_RADIUS = 4.0d;
    public static final double HELP_MARKER_MARGIN = 6.0d;
    public static final double NIGHTMODE_RECTANGLE_ROUND = Sundial.DEFAULT_WIDTH / 2 + 20.0d;
    public static final double CETUS_ARC_LENGTH = CENTER_Y - DOT_RADIUS_SMOL;
    public static final double SUNHIGHNOON_RADIUS = 130;
    public static final double SUNDOT_RADIUS = 4.0;
    public static final double SUPER_NICE_ARC_RADIUS_SMOL = 175;
    public static final double SUPER_NICE_ARC_END_EXTENSION = 100;
    public static final double SUPER_NICE_ARC_START_EXTENSION = 120;
    public static final double MARKER_LINE_B_START = CENTER_Y - SUPER_NICE_ARC_RADIUS_SMOL + 10;
    public static final double MARKER_LINE_B_END = CENTER_Y - DAYLENGTH_ARC_RADIUS - LOCALSECOND_RADIUS_BIG * 3 - 10;

    public static final double DAYLENGTH_STROKE_WIDTH = 2.00d * 0.75;
    public static final double SUNTIME_STROKE_WIDTH = 2.00d;
    public static final double HIGHNOON_STROKE_WIDTH = 1.00d;
    public static final double LOCALTIME_HOUR_STROKE_WIDTH = 1.00d;
    public static final double LOCALTIME_MINUTE_STROKE_WIDTH = 1.25d;
    public static final double SUNRISE_STROKE_WIDTH = 1.00d;
    public static final double SUNSET_STROKE_WIDTH = 1.00d;
    public static final double MARKER_HOUR_STROKE_WIDTH = 1.00d;
    public static final double MARKER_FRAME_STROKE_WIDTH = 2.00d;
    public static final double TINYGLOBE_FRAME_STROKE_WIDTH = 3.00d;
    public static final double CONTROL_THINGY_STROKE_WIDTH = 3.00d;
    public static final double CONTROL_HELP_STROKE_WIDTH = 3.00d;
    public static final double CONTROL_RESIZE_STROKE_WIDTH = 3.00d;
    public static final double CONTROL_CLOSE_STROKE_WIDTH = 3.00d;
    public static final double CONTROL_MAXIMIZE_STROKE_WIDTH = 3.00d;
    public static final double CONTROL_MINIMIZE_STROKE_WIDTH = 3.00d;
    public static final double CONTROL_NIGHTMODE_STROKE_WIDTH = 2.00d;
    public static final double CONTROL_ALWAYSONTOP_STROKE_WIDTH = 2.0d;
    public static final double CONTROL_ANIMATION_STROKE_WIDTH = 2.0d;
    public static final double CONTROL_CHART_STROKE_WIDTH = 2.0d;
    public static final double CONTROL_GLOBEGRID_STROKE_WIDTH = 2.0d;
    public static final double CONTROL_DST_STROKE_WIDTH = 2.00d;
    public static final double CETUS_MARKER_WIDTH = 1.00d;
    public static final double ORBVALLIS_MARKER_WIDTH = 1.00d;
    public static final double DAY_TERMINATOR_WIDTH = 1.25d;
    public static final double DAY_TERMINATOR_GLOW_WIDTH = 12.00d;
    public static final double TINYGLOBE_TERMINATOR_WIDTH = 1.00d;
    public static final double GLOBEGRID_LINE_WIDTH = 0.50d;
    public static final double SUPER_NICE_ARC_STROKE_WIDTH = 1.50d;
    public static final double LOCAL_HOUR_ARC_STROKE_WIDTH = MARKER_LINE_B_END - MARKER_LINE_B_START;

    public static final double DAYLENGTH_ARC_OPACITY = 0.65d;
    public static final double DAYLENGTH_MATRIX_OPACITY = 0.80d;
    public static final double MARGIN_CIRCLE_OPACITY = 0.50d;
    public static final double TINYGLOBE_DEFAULT_OPACITY = 1.00d;
    public static final double TINYGLOBE_OFFSET_OPACITY = 0.90d;
    public static final double MARKER_MINUTE_OPACITY = 0.05d;
    public static final double MATRIX_MINUTE_OPACITY = 0.25d;
    public static final double CONTROL_HELP_OPACITY = 1.00d;
    public static final double CONTROL_RESIZE_OPACITY = 1.00d;
    public static final double CONTROL_CLOSE_OPACITY = 1.00d;
    public static final double CONTROL_MAXIMIZE_OPACITY = 1.00d;
    public static final double CONTROL_MINIMIZE_OPACITY = 1.00d;
    public static final double CONTROL_NIGHTMODE_OPACITY = 1.00d;
    public static final double CONTROL_ALWAYSONTOP_OPACITY = 1.00d;
    public static final double CETUS_ARC_OPACITY = 0.65d;
    public static final double ORBVALLIS_ARC_OPACITY = 0.75d;
    public static final double DAY_TERMINATOR_GLOW_OPACITY = 0.35d;
    public static final double DAY_TERMINATOR_LINE_OPACITY = 0.90d;
    public static final double DAY_GRIDLINE_OPACITY = 0.25d;
    public static final double HELP_OVERLAY_OPACITY = 0.25d;
    public static final double NIGHTMODE_RECTANGLE_OPACITY = 0.80d;
    public static final double CONTROL_THINGY_FILL_OPACITY = 0.20d;
    public static final double TIMEDATE_DEFAULT_OPACITY = 1.00d;
    public static final double TIMEDATE_TRANSPARENT_OPACITY = 0.10d;
    public static final double DATE_OFF_OPACITY = 0.35d;
    public static final double MINUTE_LED_OFF_OPACITY = 0.00d;
    public static final double MINUTE_LED_DIM_OPACITY = 0.35d;
    public static final double MINUTE_LED_ON_OPACITY = 1.00d;
    public static final double SECOND_LED_OFF_OPACITY = 0.00d;
    public static final double SECOND_LED_DIM_OPACITY = 0.50d;
    public static final double SECOND_LED_ON_OPACITY = 1.00d;
    public static final double MATRIX_TIMEZONE_DEFAULT_OPACITY = 1.00d;
    public static final double MATRIX_TIMEZONE_GLOBE_OPACITY = 1.00d;
    public static final double HIGHNOON_NORMAL_OPACITY = 0.85d;
    public static final double HIGHNOON_DOWN_OPACITY = 0.75d;
    public static final double LOCAL_HOUR_MARKER_OPACITY = 0.65d;
    public static final double LOCAL_HOUR_MARKER_ON_OPACITY = 0.85d;
    public static final double LOCAL_HOUR_MARKER_OFF_OPACITY = 0.00d;
    public static final double LOCAL_HOUR_MARKER_OFF_ON_OPACITY = 0.85d;
    public static final double DIAL_HIGH_NOON_OPACITY = 0.75d;
    public static final double DIAL_LOCAL_ARC_NORMAL_OPACITY = 1.00;
    public static final double DIAL_LOCAL_ARC_DOWN_OPACITY = 0.00;
    public static final double DIAL_LOCAL_ARC_FUTURE_OPACITY = 0.50;
    public static final double DIAL_LOCAL_ARC_PAST_OPACITY = 0.75;

    public static final double MATRIX_MARKER_OFFSET = MARGIN_Y + 7;
    public static final double MATRIX_HORIZON_OFFSET = 75.0d;
    public static final double MATRIX_TIME_OFFSET = 0.0d;
    public static final double MATRIX_DATE_OFFSET = 45.0d;
    public static final double MATRIX_TIMEDATE_OFFSET = 135.0d;
    public static final double MATRIX_WEEK_OFFSET = 70.0d;
    public static final double MATRIX_LONGITUDE_SLIDE = 0.0d;
    public static final double MATRIX_LATITUDE_SLIDE = 0.0d;
    public static final double MATRIX_HORIZON_SLIDE = 2.0d;
    public static final double COORDINATES_OFFSET = 70.0d;
    public static final double LOCALHOUR_OFFSET = 105.0d;
    public static final double TINYGLOBE_OFFSET = 93.0d;
    public static final double TINYGLOBE_SLIDE = 22.0d;
    public static final double LOCALMINUTE_OFFSET = 50.0d;
    public static final double LOCALSECOND_OFFSET = 50.0d;
    public static final double LOCALMINUTE_CIRCLE_OFFSET = LOCALSECOND_RADIUS_BIG * 2;
    public static final double LOCALMINUTE_DIAL_OFFSET = LOCALMINUTE_CIRCLE_OFFSET + 10.0d;
    public static final double MATRIX_MINUTE_OFFSET = 70.0d;
    public static final double CONTROL_HELP_OFFSET = 236.0d;
    public static final double CONTROL_RESIZE_OFFSET = 137.0d;
    public static final double CONTROL_THINGY_OFFSET = 236.0d;
    public static final double CONTROL_CLOSE_OFFSET = 236.0d;
    public static final double CONTROL_MAXIMIZE_OFFSET = 236.0d;
    public static final double CONTROL_MINIMIZE_OFFSET = 236.0d;
    public static final double CONTROL_NIGHTMODE_OFFSET = 232.0d;
    public static final double CONTROL_ALWAYSONTOP_OFFSET = 232.0d;
    public static final double CONTROL_ANIMATION_OFFSET = 232.0d;
    public static final double CONTROL_CHART_OFFSET = 232.0d;
    public static final double CONTROL_GLOBEGRID_OFFSET = 232.0d;
    public static final double MARKER_MINUTE_OFFSET = 65.0d;
    public static final double CETUS_TIMER_OFFSET = CENTER_Y - 70.0d;
    public static final double ORBVALLIS_TIMER_OFFSET = CENTER_Y - 70.0d;
    public static final double CETUS_TIMEREADER_OFFSET = 170.0d;
    public static final double CETUS_HORIZON_OFFSET = 40.0d;
    public static final double ORBVALLIS_HORIZON_OFFSET = 40.0d;
    public static final double MATRIX_TIMEZONE_OFFSET = CENTER_Y - 48.0d;
    public static final double HELP_TEXT_OFFSET = 10.0d;
    public static final double MATRIX_LONGITUDE_OFFSET = MARGIN_Y + 30;
    public static final double MATRIX_LATITUDE_OFFSET = MATRIX_LONGITUDE_OFFSET + 28;
    public static final double CONTROL_DST_OFFSET_X = CENTER_X;
    public static final double CONTROL_DST_OFFSET_Y = CENTER_Y;
    public static final double MATRIX_DAYLENGTH_OFFSET = CENTER_Y - Sunconfig.DAYLENGTH_ARC_RADIUS * 0.95;

    public static final double MATRIX_TIME_SCALE = 3.75d;
    public static final double MATRIX_DATE_SCALE = 1.50d;
    public static final double MATRIX_TIMEDATE_DOWNSCALE = 0.80d;
    public static final double MATRIX_WEEK_SCALE = 1.00d;
    public static final double MATRIX_HORIZON_SCALE = 1.15d;
    public static final double MATRIX_DAYLENGTH_SCALE = 1.00d;
    public static final double MATRIX_LONGITUDE_SCALE = 1.50d;
    public static final double MATRIX_LATITUDE_SCALE = 1.50d;
    public static final double MATRIX_HOUR_SCALE = 1.25d;
    public static final double MATRIX_HOUR_OFF_SCALE = 1.00d;
    public static final double MATRIX_MINUTE_SCALE = 0.75d;
    public static final double TINYGLOBE_DOWNSCALE = 0.80d;
    public static final double CETUS_TIMER_SCALE = 1.00d;
    public static final double ORBVALLIS_TIMER_SCALE = 1.00d;
    public static final double CETUS_TIMEREADER_SCALE = 0.75d;
    public static final double CETUS_HORIZON_SCALE = 0.75d;
    public static final double ORBVALLIS_HORIZON_SCALE = 0.75d;
    public static final double MATRIX_TIMEZONE_SCALE = 1.15d;
    public static final double MATRIX_HIGHNOON_SCALE = 1.00d;
    public static final double MATRIX_CONTROLTHINGY_SCALE = 1.25d;
    public static final double MATRIX_SUNHIGHNOON_SCALE = 1.00d;
    public static final double HIGHNOON_DOWN_SCALE = 0.825d;
    public static final double CONTROL_DST_MATRIX_SCALE = 1.00d * (CONTROL_DST_RADIUS / 10);

    public static final double CONTROL_MINIMIZE_ANGLE = 45;
    public static final double CONTROL_CLOSE_ANGLE = CONTROL_MINIMIZE_ANGLE - 8;
    public static final double CONTROL_MAXIMIZE_ANGLE = CONTROL_MINIMIZE_ANGLE + 8;
    public static final double CONTROL_HELP_ANGLE = 180 - CONTROL_MAXIMIZE_ANGLE - 6;
    public static final double CONTROL_PIN_INFO_ANGLE = CONTROL_HELP_ANGLE + 6;
    public static final double CONTROL_ALWAYSONTOP_ANGLE = CONTROL_PIN_INFO_ANGLE + 6;
    public static final double CONTROL_NIGHTMODE_ANGLE = CONTROL_ALWAYSONTOP_ANGLE + 6;
    public static final double CONTROL_ANIMATION_ANGLE = CONTROL_NIGHTMODE_ANGLE + 6;
    public static final double CONTROL_CHART_ANGLE  = CONTROL_ANIMATION_ANGLE + 6;
    public static final double CONTROL_GLOBE_LINES_ANGLE = 56 + 180;
    public static final double CONTROL_GLOBE_GRID_ANGLE = CONTROL_GLOBE_LINES_ANGLE + 6;
    public static final double CONTROL_ORBVALLIS_ANGLE = 34 + 180;
    public static final double CONTROL_CETUS_ANGLE = CONTROL_ORBVALLIS_ANGLE - 6;

    public static final int LED_OPACITY_DURATION = 250;             // ms
    public static final int GLOBE_ROTATE_DURATION = 750;            // ms
    public static final int TINY_GLOBE_DURATION = 350;              // ms
    public static final int TIMEANDDATE_DURATION = 350;             // ms
    public static final int CETUS_MARKER_DURATION = 150;            // ms
    public static final int ORBVALLIS_MARKER_DURATION = 150;        // ms
    public static final int OUTER_CONTROLS_HIDE_DURATION = 6000;    // ms
    public static final double OUTER_CONTROLS_QUICK_HIDE_DURATION = 1000;    // ms

    public static final Color Color_Of_Window     = new Color(0.65, 0.85, 0.85, 1.00);
    public static final Color Color_Of_Earth      = new Color(0.85, 0.85, 0.65, 1.00);
    public static final Color Color_Of_Darkness   = new Color(0.00, 0.00, 0.00, 1.00);
    public static final Color Color_Of_TextBack   = new Color(0.90, 0.90, 0.50, 1.00);
    public static final Color Color_Of_Void       = new Color(0.00, 0.00, 0.00, 0.00);
    public static final Color Color_Of_AlmostVoid = new Color(0.00, 0.00, 0.00, 0.35);
    public static final Color Color_Of_SemiPro    = new Color(1.00, 1.00, 1.00, 0.50);

    public static final Color Color_Of_Nominal    = new Color(0.00, 0.65, 1.00, 0.35);
    public static final Color Color_Of_Nominalish = new Color(0.10, 0.40, 1.00, 0.50);
    public static final Color Color_Of_Warning    = new Color(1.00, 0.65, 0.00, 0.35);

    public static final Color Color_Of_DaySky       = new Color(0.50, 0.75, 1.00, 1.00);
    public static final Color Color_Of_Atmosphere   = new Color(0.25, 0.50, 0.90, 0.85);
    public static final Color Color_Of_Space        = new Color(0.25, 0.50, 0.90, 0.00);
    public static final Color Color_Of_NightSky     = new Color(0.50, 0.35, 1.00, 1.00);
    public static final Color Color_Of_HalfNight = new Color(0.00, 0.00, 0.00, 0.10);
    public static final Color Color_Of_MarginEnd    = new Color(0.25, 0.35, 0.65, 0.00);
    public static final Color Color_Of_MarginMiddle = new Color(0.25, 0.35, 0.65, 0.20);
    public static final Color Color_Of_MarginStart  = new Color(0.25, 0.35, 0.65, 0.50);
    public static final Color Color_Of_Margin_Hover = new Color(0.10, 0.35, 0.50, MARGIN_CIRCLE_OPACITY);

    public static final Color Color_Of_SunTime    = new Color(1.00, 0.50, 0.00, 1.00);
    public static final Color Color_Of_HighNoon   = new Color(1.00, 1.00, 0.00, 1.00);
    public static final Color Color_Of_Horizon    = new Color(1.00, 0.90, 0.30, 1.00);
    public static final Color Color_Of_SunRise    = new Color(1.00, 0.00, 0.00, 1.00);
    public static final Color Color_Of_SunSet     = new Color(0.65, 0.00, 0.65, 1.00);
    public static final Color Color_Of_LocalTime  = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_LocalArc   = new Color(1.00, 0.65, 0.15, 1.00);
    public static final Color Color_Of_TinyFrame  = new Color(1.00, 1.00, 1.00, 1.00);

    public static final Color Color_Of_TinyDay      = new Color(0.65, 0.65,0.65, 1.00);
    public static final Color Color_Of_TinyNight    = new Color(0.65, 0.00, 0.00, 1.00);
    public static final Color Color_Of_TinyAmbient  = new Color(0.50, 0.50,0.50, 1.00);

    public static final Color Color_Of_TerminatorLine = new Color(1.00, 0.10, 0.10, 1.00);
    public static final Color Color_Of_TerminatorGlow = new Color(0.90, 0.10, 0.00, 1.00);

    public static final Color Color_Of_Seconds    = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_Minutes    = new Color(1.00, 1.00, 1.00, 1.00);

    public static final Color Color_Of_HelpFill   = new Color(0.10, 0.20, 0.50, 0.20);
    public static final Color Color_Of_HelpStroke_Off = new Color(1.00, 0.35, 0.10, 1.00);
    public static final Color Color_Of_HelpStroke_On = new Color(1.00, 0.65, 0.00, 1.00);
    public static final Color Color_Of_ChartStroke_Off = new Color(0.00, 0.90, 0.10, 1.00);
    public static final Color Color_Of_ChartStroke_On = new Color(0.00, 1.00, 0.00, 1.00);
    public static final Color Color_Of_ResizeFill   = new Color(0.25, 0.50, 1.00, 0.20);
    public static final Color Color_Of_ResizeStroke = new Color(0.50, 0.75, 1.00, 1.00);
    public static final Color Color_Of_CloseFill    = new Color(1.00, 0.25, 0.25, 0.20);
    public static final Color Color_Of_CloseStroke  = new Color(1.00, 0.50, 0.50, 1.00);
    public static final Color Color_Of_MaximizeFill   = new Color(0.25, 1.00, 0.25, 0.20);
    public static final Color Color_Of_MaximizeStroke = new Color(0.50, 1.00, 0.50, 1.00);
    public static final Color Color_Of_MinimizeFill   = new Color(1.00, 1.00, 0.00, 0.20);
    public static final Color Color_Of_MinimizeStroke = new Color(0.90, 0.90, 0.20, 1.00);
    public static final Color Color_Of_NightmodeFill   = new Color(0.50, 0.10, 0.50, 0.20);
    public static final Color Color_Of_NightmodeStroke = new Color(0.80, 0.20, 0.80, 1.00);
    public static final Color Color_Of_AlwaysOnTopFill = new Color(0.35, 0.35, 0.35, 0.20);
    public static final Color Color_Of_AlwaysOnTopStroke = new Color(0.75, 0.75, 0.75, 1.00);
    public static final Color Color_Of_GlobeGridFill = new Color(0.45, 0.20, 0.10, 0.20);
    public static final Color Color_Of_GlobeGridStroke = new Color(0.90, 0.40, 0.20, 1.00);
    public static final Color Color_Of_ThingyFill = new Color(0.35, 0.35, 0.35, 0.20);
    public static final Color Color_Of_ThingyStroke = new Color(0.75, 0.75, 0.75, 1.00);
    public static final Color Color_Of_AnimationOn = new Color(1.00, 0.65, 0.35, 1.00);

    public static final Color Color_Of_CetusMarker = new Color(0.90, 0.70, 1.00, 1.00);
    public static final Color Color_Of_OrbVallisMarker = new Color(1.00, 0.90, 0.70, 1.00);
    public static final Color Color_Of_CetusFrame  = new Color(0.85, 0.60, 0.95, 1.00);
    public static final Color Color_Of_CetusArc    = new Color(0.75, 0.15, 1.00, 1.00);
    public static final Color Color_Of_OrbVallisArc    = new Color(1.00, 0.75, 0.25, 1.00);
    public static final Color Color_Of_CetusDay    = new Color(1.00, 0.90, 0.70, 1.00);
    public static final Color Color_Of_CetusNight  = new Color(0.90, 0.70, 1.00, 1.00);
    public static final Color Color_Of_OrbVallisWarm  = new Color(1.00, 0.75, 0.50, 1.00);
    public static final Color Color_Of_OrbVallisCold  = new Color(0.70, 0.85, 1.00, 1.00);

    public static final Color Color_Of_DayDay           = new Color(1.00, 1.00, 0.80, 1.00);
    public static final Color Color_Of_DayReverse       = new Color(1.00, 0.15, 0.00, 1.00);
    public static final Color Color_Of_DayAmbient       = new Color(0.30, 0.30, 0.30, 1.00);
    public static final Color Color_Of_DaySpecular      = new Color(0.75, 0.75, 0.75, 1.00);
    public static final Color Color_Of_TinySpecular     = new Color(0.40, 0.40, 0.40, 1.00);

    public static final Color Color_Of_NightNight       = new Color(0.10, 0.25, 0.40, 1.00);
    public static final Color Color_Of_NightReverse     = new Color(0.10, 0.50, 1.00, 1.00);
    public static final Color Color_Of_NightAmbient     = new Color(0.95, 0.95, 0.95, 1.00);
    public static final Color Color_Of_NightSpecular    = new Color(1.00, 1.00, 1.00, 1.00);

    public static final Color Color_Of_AtmosphereDay    = new Color(0.50, 0.85, 1.00, 1.00);
    public static final Color Color_Of_AtmosphereNight  = new Color(1.00, 0.38, 0.10, 1.00);
    public static final Color Color_Of_AtmosphereMid    = new Color(0.25, 0.50, 1.00, 1.00);

    public static final Color Color_Of_HourMarkerFull    = new Color(1.00, 0.70, 0.70, 1.00);
    public static final Color Color_Of_HourMarkerHalf    = new Color(0.20, 1.00, 0.20, 1.00);
    public static final Color Color_Of_HourMarkerQwrt    = new Color(0.15, 0.90, 0.15, 1.00);
    public static final Color Color_Of_HourMarkerStroke  = new Color(0.15, 0.30, 0.60, 0.50);


    public static final String MATRIX_GLOW                  = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  5.0, 0.60, 0, 0);";
    public static final String MATRIX_GLOW2                 = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0), 10.0, 0.50, 0, 0);";
    public static final String MATRIX_SHADOW                = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  5.0, 0.60, 0, 0);";
    public static final String MATRIX_SHADOW2               = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0), 10.0, 0.50, 0, 0);";
    public static final String MATRIX_SHADOW3               = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 0.3), 15.0, 0.50, 0, 0);";
    public static final String MATRIX_BLOCK                 = "-fx-effect: dropshadow(three-pass-box, rgba(  0,  0,  0, 1.0), 10.0, 0.50, 0, 0);";
    public static final String HORIZON_GLOW                 = "-fx-effect: dropshadow(three-pass-box, rgba(255, 96, 32, 1.0), 15.0, 0.87, 0, 0);";
    public static final String LOCALTIME_SHADOW             = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0), 15.0, 0.50, 0, 0);";
    public static final String LOCALSECOND_GLOW             = "-fx-effect: dropshadow(three-pass-box, rgba(255, 64, 64, 1.0), 10.0, 0.60, 0, 0);";
    public static final String LOCALMINUTE_GLOW             = "-fx-effect: dropshadow(three-pass-box, rgba( 32,255, 32, 1.0), 10.0, 0.60, 0, 0);";
    public static final String LOCALHOUR_DIAL_GLOWIER       = "-fx-effect: dropshadow(three-pass-box, rgba( 64,192,255, 1.0), 12.0, 0.75, 0, 0);";
    public static final String LOCALSECOND_DIAL_GLOW        = "-fx-effect: dropshadow(three-pass-box, rgba(255, 32, 32, 1.0), 10.0, 0.60, 0, 0);";
    public static final String LOCALMINUTE_DIAL_GLOW        = "-fx-effect: dropshadow(three-pass-box, rgba(  0,192,  0, 1.0), 12.0, 0.67, 0, 0);";
    public static final String LOCALHOUR_DIAL_GLOW          = "-fx-effect: dropshadow(three-pass-box, rgba( 32,164,255, 1.0), 12.0, 0.68, 0, 0);";
    public static final String LOCALMIDNIGHT_DIAL_GLOW      = "-fx-effect: dropshadow(three-pass-box, rgba(255,128,255, 1.0), 12.0, 0.68, 0, 0);";
    public static final String LOCALNOON_DIAL_GLOW          = "-fx-effect: dropshadow(three-pass-box, rgba(255,255, 64, 1.0), 12.0, 0.65, 0, 0);";
    public static final String LOCALNOON_DIAL_HOT           = "-fx-effect: dropshadow(three-pass-box, rgba(255,255,255, 1.0), 15.0, 0.75, 0, 0);";
    public static final String LOCALNOON_DIAL_SHADOW        = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0), 12.0, 0.60, 0, 0);";

    public static final String HORIZON_HOVER_GLOW           = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 0.5), 5.0, 0.50, 0, 0);";
    public static final String TERMINATOR_LINE_GLOW         = "-fx-effect: dropshadow(three-pass-box, rgba(255,255,255, 1.0), 10.0, 0.50, 0, 0);";

    public static final String CONTROL_THINGY_SHADOW        = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_THINGY_GLOW          = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_HELP_SHADOW_OFF      = "-fx-effect: dropshadow(three-pass-box, rgba( 16, 32,128, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_HELP_GLOW            = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_HELP_SHADOW_ON       = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_RESIZE_SHADOW        = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_RESIZE_GLOW          = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_CLOSE_SHADOW         = "-fx-effect: dropshadow(three-pass-box, rgba(128, 32, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_CLOSE_GLOW           = "-fx-effect: dropshadow(three-pass-box, rgba(255,  0,  0, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MAXIMIZE_SHADOW      = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MAXIMIZE_GLOW        = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MINIMIZE_SHADOW      = "-fx-effect: dropshadow(three-pass-box, rgba(112,112, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MINIMIZE_GLOW        = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_NIGHTMODE_SHADOW     = "-fx-effect: dropshadow(three-pass-box, rgba(112, 32,112, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_NIGHTMODE_GLOW       = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_ALWAYSONTOP_SHADOW   = "-fx-effect: dropshadow(three-pass-box, rgba( 64, 64, 64, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_ALWAYSONTOP_GLOW     = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_GLOBEGRID_SHADOW     = "-fx-effect: dropshadow(three-pass-box, rgba( 64, 64, 64, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_GLOBEGRID_GLOW       = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";

    public static final String CETUS_MARKER_SHADOW          = "-fx-effect: dropshadow(three-pass-box, rgba(192, 48,192, 1.0), 10.0, 0.75, 0, 0);";
    public static final String ORBVALLIS_MARKER_SHADOW      = "-fx-effect: dropshadow(three-pass-box, rgba(192,192, 48, 1.0), 10.0, 0.75, 0, 0);";
    public static final String CETUS_MARKER_GLOW            = "-fx-effect: dropshadow(three-pass-box, rgba(255,196,255, 1.0), 10.0, 0.75, 0, 0);";
    public static final String CETUS_MATRIX_SHADOW_DAY      = "-fx-effect: dropshadow(three-pass-box, rgba(128, 64,  0, 1.0), 15.0, 0.75, 0, 0);";
    public static final String CETUS_MATRIX_SHADOW_NIGHT    = "-fx-effect: dropshadow(three-pass-box, rgba(128, 32,164, 1.0), 15.0, 0.75, 0, 0);";
    public static final String ORBVALLIS_MATRIX_SHADOW_WARM = "-fx-effect: dropshadow(three-pass-box, rgba(192, 96,  0, 1.0), 15.0, 0.75, 0, 0);";
    public static final String ORBVALLIS_MATRIX_SHADOW_COLD = "-fx-effect: dropshadow(three-pass-box, rgba(  0, 96,192, 1.0), 15.0, 0.75, 0, 0);";

    public static final String HELP_MARKER_GLOW             = "-fx-effect: dropshadow(three-pass-box, rgba(  0,255,  0, 1.0), 10.0, 0.60, 0, 0);";

    public static final String HOUR_MARKER_SHADOW           = "-fx-effect: dropshadow(three-pass-box, rgba(255,  0,  0, 1.0),  4.0, 0.50, 0, 0);";

    public static final RadialGradient FRAME_DIAL_NOMINAL = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.75, Color_Of_Void),
            new Stop(1.00, Color_Of_Nominal)
    );

    public static final RadialGradient FRAME_DIAL_NOMINALISH = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.60, Color_Of_Void),
            new Stop(0.80, Color_Of_Nominal),
            new Stop(1.00, Color_Of_Nominalish)
    );

    public static final RadialGradient FRAME_DIAL_WARNING = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.90, Color_Of_Void),
            new Stop(1.00, Color_Of_Warning)
    );

    public static final RadialGradient FRAME_GLOBE_NOMINAL = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.95, Color_Of_Void),
            new Stop(1.00, Color_Of_Nominal)
    );

    public static final RadialGradient FRAME_GLOBE_WARNING = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.95, Color_Of_Void),
            new Stop(1.00, Color_Of_Warning)
    );

    public static final RadialGradient MINUTE_MARKER_GRADIENT = new RadialGradient(
            0, 0,
            LOCALMINUTE_WIDTH / 2, LOCALMINUTE_HEIGHT / 2, LOCALMINUTE_HEIGHT,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.10, Color_Of_AlmostVoid),
            new Stop(0.50, Color_Of_Darkness)
    );

    public static final RadialGradient MINUTE_POLY_GRADIENT = new RadialGradient(
            0, 0,
            0, LOCALMINUTE_POLY_HEIGHT / 3, LOCALMINUTE_POLY_HEIGHT,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.10, Color_Of_AlmostVoid),
            new Stop(0.50, Color_Of_Darkness)
    );

    public static final RadialGradient MINUTE_CIRCLE_GRADIENT = new RadialGradient(
            0, 0,
            0, 0, 1 /*LOCALSECOND_RADIUS_BIG*/,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.10, Color_Of_AlmostVoid),
            new Stop(0.90, Color_Of_Darkness)
    );

    public static final RadialGradient TINYGLOBE_FRAME_GRADIENT = new RadialGradient(
            0, 0,
            0, 0, TINYGLOBE_RADIUS,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.80, Color_Of_Void),
            new Stop(1.00, Color_Of_TinyFrame)
    );

    public static final double CETUS_ARC_GRADIENT_STOP1 = 0.87;
    public static final double CETUS_ARC_GRADIENT_STOP2 = 0.95;

    public static final RadialGradient CETUS_ARC_GRADIENT = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(CETUS_ARC_GRADIENT_STOP1, Color_Of_Void),
            new Stop(CETUS_ARC_GRADIENT_STOP2, Color_Of_CetusArc)
    );

    public static final double ORBVALLIS_ARC_GRADIENT_STOP1 = 0.90;
    public static final double ORBVALLIS_ARC_GRADIENT_STOP2 = 0.99;

    public static final RadialGradient ORBVALLIS_ARC_GRADIENT = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(ORBVALLIS_ARC_GRADIENT_STOP1, Color_Of_Void),
            new Stop(ORBVALLIS_ARC_GRADIENT_STOP2, Color_Of_OrbVallisArc)
    );

    public static final RadialGradient CETUS_ARC_GRADIENT_HOVER = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.60, Color_Of_Void),
            new Stop(0.85, Color_Of_CetusArc)
    );

    public static final RadialGradient GLOBE_ATMOSPHERE = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y + 5,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.960, new Color(1, 1, 1, 0)),
            new Stop(0.975, new Color(1, 1, 1, 1)),
            new Stop(1.000, new Color(1, 1, 1, 0))
    );

    public static final RadialGradient LOCAL_HOUR_ARC_FILL = new RadialGradient(
            0, 0,
            0, 0, CENTER_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.99, Color_Of_Nominalish),
            new Stop(1.00, Color.TRANSPARENT)
    );

    public static final RadialGradient MARGIN_CIRCLE_FILL = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.970, Color_Of_MarginStart),
            new Stop(0.980, Color_Of_MarginMiddle),
            new Stop(1.000, Color_Of_MarginEnd)
    );

    public static final RadialGradient NIGHTMODE_CIRCLE_FILL = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.940, Color.BLACK),
            new Stop(1.000, Color.TRANSPARENT)
    );

}
