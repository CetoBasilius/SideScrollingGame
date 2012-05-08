package com.gamefinal.global;

import java.awt.Component;
import java.awt.Image;
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



public class Global{

	private static final String CONFIG_FILENAME = "config.txt";
	private static final int DEFAULT_MAP_SIZE_Y = 256;
	private static final int DEFAULT_MAP_SIZE_X = 512;
	private static final String DEFAULT_MAP_FILENAME = "out.txt";
	private static final int DEFAULT_RESOLUTION_Y = 480;
	private static final int DEFAULT_RESOLUTION_X = 640;

	private static Global reference;
	
	private int resolutionX;
	private int resolutionY;
	private int halfResoulutionX;
	private int halfResoulutionY;
	
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
	
	private Global()
	{
		
	}
	
	public void init(Component component){
		setMapDefaultValues();
		readConfig();
		readImageList();
		initWorldMap();
		loadGlobalResources(component);

		graphicsEngine = new GraphicsEngine(component);
		collisionEngine = new CollisionEngine();
		inputEngine = new InputEngine();
	}

	private void loadGlobalResources(Component component) {
		loadGlobalResourcesThread = new LoadGlobalResources(component);
		loadGlobalResourcesThread.start();
	}
	
	private void readImageList()
	{
		Vector<String> tileFileNamesVector = new Vector<String>();
		Vector<Vector<String>> animatedTileFileNamesVector = new Vector<Vector<String>>();
		
		
		//TODO extract a function to set a value depending on the string that we ask for
		//study property files
		int allAnimatedTiles=0;
		int maxAnimatedTileFrames=0;
		try {
			InputStream imageFileInputStream;
			URL imageFileFileLocation = getClass().getResource("configuration\\"+imageListFileName);
			imageFileInputStream = imageFileFileLocation.openStream();
			InputStreamReader imageFileInputStreamReader = new InputStreamReader(imageFileInputStream);
			BufferedReader imageFileBufferedReader = new BufferedReader(imageFileInputStreamReader);
			String currentReadingLine = null;
			
			
			while ((currentReadingLine = imageFileBufferedReader.readLine()) != null) {	
			
				String currentReadingLineSplit[] = currentReadingLine.split("=",-1);
				if(currentReadingLineSplit.length>1){

					if(currentReadingLineSplit[0].equalsIgnoreCase("tile")){
						tileFileNamesVector.add(currentReadingLineSplit[1]);
					}
					
					if(currentReadingLineSplit[0].equalsIgnoreCase("animatedtile")){
						allAnimatedTiles++;
						
						String animatedImageSplit[] = currentReadingLineSplit[1].split(",", -1);
						
						int numberOfDigits = StringHelper.countOccurrences(animatedImageSplit[0],'%');
						String zeros = "";
						for(int a=0;a<numberOfDigits;a++){
							zeros+="0";
						}
						zeros=zeros.substring(1, zeros.length());
						Vector<String> thisAnimatedTileImages = new Vector<String>();
						int numFrames = Integer.parseInt(animatedImageSplit[1]);
						
						if(numFrames>maxAnimatedTileFrames){maxAnimatedTileFrames=numFrames;}
						for(int a=0;a<numFrames;a++){
							if(NumberHelper.countDigits(a)>NumberHelper.countDigits((a-1)) && a>0){
								zeros = zeros.substring(0, zeros.length()-1);
							}
							String finalFileName = animatedImageSplit[0].replaceAll("%+",zeros+a);
							thisAnimatedTileImages.add(finalFileName);
						}
						animatedTileFileNamesVector.add(thisAnimatedTileImages);
					}
				}
			}
		} catch (Exception e) {
			setGlobalStatus("Tile list file not found.");
		}

		int tileVectorSize = tileFileNamesVector.size();
		if(tileVectorSize>0){
			tileImages = new Image[tileVectorSize];
			triangleTileImages = new Image[tileVectorSize];
			tileImagesFileNames = new String[tileVectorSize];
			tileFileNamesVector.toArray(tileImagesFileNames); 
		}

		
		int animatedTileVectorSize = animatedTileFileNamesVector.size();
		if(animatedTileVectorSize>0){
			animatedTileImages = new Image[allAnimatedTiles][maxAnimatedTileFrames];
			animatedTileImagesFileNames = new String[allAnimatedTiles][maxAnimatedTileFrames];
			for(int a=0;a<allAnimatedTiles;a++){
				for(int b=0;b<maxAnimatedTileFrames;b++){
					if(b<animatedTileFileNamesVector.elementAt(a).size()){
						animatedTileImagesFileNames[a][b] = animatedTileFileNamesVector.elementAt(a).elementAt(b);
					}
				}
			}
		}
	}

	private void initWorldMap() {
		worldMap = new WorldMap(mapSizeX,mapSizeY,mapFileName);
	}
	
	private void readConfig()
	{
		InputStream configInputStream;
		try {
			URL configFileLocation = getClass().getResource("configuration\\"+CONFIG_FILENAME);
			
			configInputStream = configFileLocation.openStream();
			InputStreamReader configInputStreamReader = new InputStreamReader(configInputStream);
			BufferedReader configBufferedReader = new BufferedReader(configInputStreamReader);
			String currentReadingLine = null;

			while ((currentReadingLine = configBufferedReader.readLine()) != null) {	
				String currentReadingLineSplit[] = currentReadingLine.split("=",-1);
				if(currentReadingLineSplit.length>1){

					if (currentReadingLineSplit[0]
							.equalsIgnoreCase("resolutionx")) {
						Global.getGlobals().setResolutionX(
								Integer.parseInt(currentReadingLineSplit[1]));
					}
					if (currentReadingLineSplit[0]
							.equalsIgnoreCase("resolutiony")) {
						Global.getGlobals().setResolutionY(
								Integer.parseInt(currentReadingLineSplit[1]));
					}
					if (currentReadingLineSplit[0].equalsIgnoreCase("mapsizex")) {
						Global.getGlobals().setMapSizeX(
								Integer.parseInt(currentReadingLineSplit[1]));
					}
					if (currentReadingLineSplit[0].equalsIgnoreCase("mapsizey")) {
						Global.getGlobals().setMapSizeY(
								Integer.parseInt(currentReadingLineSplit[1]));
					}
					if (currentReadingLineSplit[0].equalsIgnoreCase("mapfile")) {
						Global.getGlobals().setMapFileName(
								currentReadingLineSplit[1]);
					}
					if (currentReadingLineSplit[0]
							.equalsIgnoreCase("imagelistfile")) {
						Global.getGlobals().setImageListFileName(
								currentReadingLineSplit[1]);
					}

				}
			}
		} catch (Exception e) {
			setGlobalStatus("config file not found.");
		}
	}
	
	public void setMapDefaultValues() {
		setResolutionX(DEFAULT_RESOLUTION_X);
		setResolutionY(DEFAULT_RESOLUTION_Y);
		setMapFileName(DEFAULT_MAP_FILENAME);
		setMapSizeX(DEFAULT_MAP_SIZE_X);
		setMapSizeY(DEFAULT_MAP_SIZE_Y);
	}

	public void setResolution(int resolutionX, int resolutionY) {
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		setHalfResoulutionX(resolutionX/2);
		setHalfResoulutionY(resolutionY/2);
	}

	public static Global getGlobals()
	{
		if (reference == null){
			reference = new Global();
		}
		return reference;
	}

	@Override
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException(); 
	}
	

	
	

	public int getResolutionX() {
		return resolutionX;
	}

	public void setResolutionX(int resolutionX) {
		this.resolutionX = resolutionX;
		setHalfResoulutionX(resolutionX/2);
	}

	public int getResolutionY() {
		return resolutionY;
	}

	public void setResolutionY(int resolutionY) {
		this.resolutionY = resolutionY;
		setHalfResoulutionY(resolutionY/2);
	}

	public int getHalfResoulutionY() {
		return halfResoulutionY;
	}

	private void setHalfResoulutionY(int halfResoulutionY) {
		this.halfResoulutionY = halfResoulutionY;
	}

	public int getHalfResoulutionX() {
		return halfResoulutionX;
	}

	private void setHalfResoulutionX(int halfResoulutionX) {
		this.halfResoulutionX = halfResoulutionX;
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

}
