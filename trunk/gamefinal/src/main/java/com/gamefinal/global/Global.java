package com.gamefinal.global;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;
import com.gamefinal.app.WorldMap;
import com.gamefinal.engines.CollisionEngine;
import com.gamefinal.engines.GraphicsEngine;
import com.gamefinal.engines.InputEngine;
import com.gamefinal.helpers.NumberHelper;
import com.gamefinal.helpers.StringHelper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;

public class Global {
    static final Logger logger = Logger.getLogger(Global.class);
    public static final int CONSOLE_MARGIN = 20;
    public static final String CONFIG_FILENAME = "config.txt";
    public static final int DEFAULT_MAP_SIZE_Y = 256;
    public static final int DEFAULT_MAP_SIZE_X = 512;
    public static final String DEFAULT_MAP_FILENAME = "out.txt";
    public static final int DEFAULT_GAME_RESOLUTION_Y = 480;
    public static final int DEFAULT_GAME_RESOLUTION_X = 640;

    public static final int WINDOW_THICKNESS_X = 6;
    public static final int WINDOW_THICKNESS_Y = 28;

    public static final Dimension DESKTOP_RESOLUTION = Toolkit.getDefaultToolkit().getScreenSize();

    private static Global reference;

    private int gameResolutionX;
    private int gameResolutionY;
    private int gameHalfResoulutionX;
    private int gameHalfResoulutionY;

    private Image defaultImage;
    public Image tileImages[];
    public Image triangleTileImages[];
    public Image animatedTileImages[][];

    private String imageListFileName;
    public String tileImagesFileNames[];
    public String animatedTileImagesFileNames[][];

    private LoadGlobalResources loadGlobalResourcesThread;
    private boolean loadingDone = false;

    private String globalStatus;

    public WorldMap worldMap;

    private int mapSizeX;
    private int mapSizeY;
    private String mapFileName;

    public GraphicsEngine graphicsEngine;
    public CollisionEngine collisionEngine;
    public InputEngine inputEngine;

    public Console gameConsole;

    public JFrame frameReference;
    public JPanel panelReference;
    public Canvas canvasReference;

    private int defaultWindowPositionX;
    private int defaultWindowPositionY;

    private Global() {

    }

    public void init(JFrame mainFrame, JPanel mainPanel, Canvas mainCanvas) {
        logger.info("Global.init begin");
        frameReference = mainFrame;
        panelReference = mainPanel;
        canvasReference = mainCanvas;

        setMapDefaultValues();
        readConfig();
        setDefaultWindowPosition();
        readImageList();
        initWorldMap();
        loadGlobalResources(mainFrame);

        graphicsEngine = new GraphicsEngine(mainFrame);
        collisionEngine = new CollisionEngine();
        inputEngine = new InputEngine();
        gameConsole = new Console(CONSOLE_MARGIN, gameHalfResoulutionY, CONSOLE_MARGIN, gameResolutionY - CONSOLE_MARGIN);

        logger.info("Global.init end");
    }

    private void setDefaultWindowPosition() {
        setDefaultWindowPositionX((DESKTOP_RESOLUTION.width / 2) - (getGameResolutionX() / 2));
        setDefaultWindowPositionY((DESKTOP_RESOLUTION.height / 2) - (getGameResolutionY() / 2));
    }

    public boolean processConsoleMessage(String message) {
        /*
           * Checks the message typed in the console. if the message is a command it will return false
           * if the message is a simple message it will return true
           */
        if (message.equalsIgnoreCase("fullscreen")) {
            graphicsEngine.toggleFullScreen();
            return false;
        }

        if (message.equalsIgnoreCase("record")) {
            inputEngine.inputRecorder.startRecording();
            return false;
        }

        if (message.equalsIgnoreCase("playback")) {
            inputEngine.inputRecorder.startPlayBack();
            return false;
        }

        if (message.equalsIgnoreCase("stoprecord")) {
            inputEngine.inputRecorder.stopRecording();
            return false;
        }

        return true;
    }

    private void loadGlobalResources(Component component) {
        loadGlobalResourcesThread = new LoadGlobalResources(component);
        loadGlobalResourcesThread.start();
    }

    private void readImageList() {
        Vector<String> tileFileNamesVector = new Vector<String>();
        Vector<Vector<String>> animatedTileFileNamesVector = new Vector<Vector<String>>();

        //TODO extract a function to set a value depending on the string that we ask for
        //study property files
        int allAnimatedTiles = 0;
        int maxAnimatedTileFrames = 0;
        try {

            InputStreamReader imageFileInputStreamReader =
                new InputStreamReader(getClass().getResourceAsStream("configuration/" + imageListFileName));
            BufferedReader imageFileBufferedReader = new BufferedReader(imageFileInputStreamReader);
            String currentReadingLine = null;

            while ((currentReadingLine = imageFileBufferedReader.readLine()) != null) {

                String currentReadingLineSplit[] = currentReadingLine.split("=", -1);
                if (currentReadingLineSplit.length > 1) {

                    if (currentReadingLineSplit[0].equalsIgnoreCase("tile")) {
                        tileFileNamesVector.add(currentReadingLineSplit[1]);
                    }

                    if (currentReadingLineSplit[0].equalsIgnoreCase("animatedtile")) {
                        allAnimatedTiles++;

                        String animatedImageSplit[] = currentReadingLineSplit[1].split(",", -1);

                        int numberOfDigits = StringHelper.countOccurrences(animatedImageSplit[0], '%');
                        String zeros = "";
                        for (int a = 0; a < numberOfDigits; a++) {
                            zeros += "0";
                        }
                        zeros = zeros.substring(1, zeros.length());
                        Vector<String> thisAnimatedTileImages = new Vector<String>();
                        int numFrames = Integer.parseInt(animatedImageSplit[1]);

                        if (numFrames > maxAnimatedTileFrames) {
                            maxAnimatedTileFrames = numFrames;
                        }
                        for (int a = 0; a < numFrames; a++) {
                            if (NumberHelper.countDigits(a) > NumberHelper.countDigits((a - 1)) && a > 0) {
                                zeros = zeros.substring(0, zeros.length() - 1);
                            }
                            String finalFileName = animatedImageSplit[0].replaceAll("%+", zeros + a);
                            thisAnimatedTileImages.add(finalFileName);
                        }
                        animatedTileFileNamesVector.add(thisAnimatedTileImages);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            setGlobalStatus("Tile list file not found.");
        }

        int tileVectorSize = tileFileNamesVector.size();
        if (tileVectorSize > 0) {
            tileImages = new Image[tileVectorSize];
            triangleTileImages = new Image[tileVectorSize];
            tileImagesFileNames = new String[tileVectorSize];
            tileFileNamesVector.toArray(tileImagesFileNames);
        }

        int animatedTileVectorSize = animatedTileFileNamesVector.size();
        if (animatedTileVectorSize > 0) {
            animatedTileImages = new Image[allAnimatedTiles][maxAnimatedTileFrames];
            animatedTileImagesFileNames = new String[allAnimatedTiles][maxAnimatedTileFrames];
            for (int a = 0; a < allAnimatedTiles; a++) {
                for (int b = 0; b < maxAnimatedTileFrames; b++) {
                    if (b < animatedTileFileNamesVector.elementAt(a).size()) {
                        animatedTileImagesFileNames[a][b] = animatedTileFileNamesVector.elementAt(a).elementAt(b);
                    }
                }
            }
        }
    }

    private void initWorldMap() {
        worldMap = new WorldMap(mapSizeX, mapSizeY, mapFileName);
    }

    private void readConfig() {
        InputStream configInputStream;
        logger.info("readConfig init");
        try {
            URL configFileLocation = getClass().getResource("configuration/" + CONFIG_FILENAME);

            configInputStream = configFileLocation.openStream();
            InputStreamReader configInputStreamReader = new InputStreamReader(configInputStream);
            BufferedReader configBufferedReader = new BufferedReader(configInputStreamReader);
            String currentReadingLine = null;

            while ((currentReadingLine = configBufferedReader.readLine()) != null) {
                String currentReadingLineSplit[] = currentReadingLine.split("=", -1);
                if (currentReadingLineSplit.length > 1) {

                    if (currentReadingLineSplit[0].equalsIgnoreCase("resolutionx")) {
                        Global.getGlobals().setGameResolutionX(Integer.parseInt(currentReadingLineSplit[1]));
                    }
                    if (currentReadingLineSplit[0].equalsIgnoreCase("resolutiony")) {
                        Global.getGlobals().setGameResolutionY(Integer.parseInt(currentReadingLineSplit[1]));
                    }
                    if (currentReadingLineSplit[0].equalsIgnoreCase("mapsizex")) {
                        Global.getGlobals().setMapSizeX(Integer.parseInt(currentReadingLineSplit[1]));
                    }
                    if (currentReadingLineSplit[0].equalsIgnoreCase("mapsizey")) {
                        Global.getGlobals().setMapSizeY(Integer.parseInt(currentReadingLineSplit[1]));
                    }
                    if (currentReadingLineSplit[0].equalsIgnoreCase("mapfile")) {
                        Global.getGlobals().setMapFileName(currentReadingLineSplit[1]);
                    }
                    if (currentReadingLineSplit[0].equalsIgnoreCase("imagelistfile")) {
                        Global.getGlobals().setImageListFileName(currentReadingLineSplit[1]);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            setGlobalStatus("config file not found.");
        }
    }

    public void setMapDefaultValues() {
        setGameResolutionX(DEFAULT_GAME_RESOLUTION_X);
        setGameResolutionY(DEFAULT_GAME_RESOLUTION_Y);
        setMapFileName(DEFAULT_MAP_FILENAME);
        setMapSizeX(DEFAULT_MAP_SIZE_X);
        setMapSizeY(DEFAULT_MAP_SIZE_Y);
    }

    public void setResolution(int resolutionXIn, int resolutionYIn) {
        gameResolutionX = resolutionXIn;
        gameResolutionY = resolutionYIn;
        setGameHalfResoulutionX(resolutionXIn / 2);
        setGameHalfResoulutionY(resolutionYIn / 2);
    }

    public static Global getGlobals() {
        if (reference == null) {
            reference = new Global();
        }
        return reference;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public int getGameResolutionX() {
        return gameResolutionX;
    }

    public void setGameResolutionX(int resolutionX) {
        this.gameResolutionX = resolutionX;
        setGameHalfResoulutionX(resolutionX / 2);
    }

    public int getGameResolutionY() {
        return gameResolutionY;
    }

    public void setGameResolutionY(int resolutionY) {
        this.gameResolutionY = resolutionY;
        setGameHalfResoulutionY(resolutionY / 2);
    }

    public int getGameHalfResoulutionY() {
        return gameHalfResoulutionY;
    }

    private void setGameHalfResoulutionY(int halfResoulutionY) {
        this.gameHalfResoulutionY = halfResoulutionY;
    }

    public int getGameHalfResoulutionX() {
        return gameHalfResoulutionX;
    }

    private void setGameHalfResoulutionX(int halfResoulutionX) {
        this.gameHalfResoulutionX = halfResoulutionX;
    }

    public Image getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(Image defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(String globalStatus) {
        logger.info(globalStatus);
        this.globalStatus = globalStatus;
    }

    public boolean isLoadingDone() {
        return loadingDone;
    }

    public void setLoadingNotDone() {
        this.loadingDone = false;
    }

    public void setLoadingDone() {
        this.loadingDone = true;
        setGlobalStatus("Loading is done.");
    }

    public String getImageListFileName() {
        return imageListFileName;
    }

    public void setImageListFileName(String tileListFileName) {
        this.imageListFileName = tileListFileName;
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public void setMapFileName(String mapString) {
        this.mapFileName = mapString;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }

    public void setMapSizeY(int mapSizeY) {
        this.mapSizeY = mapSizeY;
    }

    public int getMapSizeX() {
        return mapSizeX;
    }

    public void setMapSizeX(int mapSizeX) {
        this.mapSizeX = mapSizeX;
    }

    public int getDefaultWindowPositionX() {
        return defaultWindowPositionX;
    }

    public void setDefaultWindowPositionX(int defaultWindowPositionX) {
        this.defaultWindowPositionX = defaultWindowPositionX;
    }

    public int getDefaultWindowPositionY() {
        return defaultWindowPositionY;
    }

    public void setDefaultWindowPositionY(int defaultWindowPositionY) {
        this.defaultWindowPositionY = defaultWindowPositionY;
    }
}
