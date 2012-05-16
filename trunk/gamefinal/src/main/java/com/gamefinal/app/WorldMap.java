package com.gamefinal.app;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

import com.gamefinal.global.Global;

public class WorldMap {
    static final Logger logger = Logger.getLogger(WorldMap.class);
	private static final int TILE_SPACING = 32;//this value controls the final resting position of the object
	
	public final int maxMapVisualLevels = 4;
	public final Color[] mapLevelColor = {Color.green,Color.pink,Color.red,Color.yellow};

	public enum mapLevel { BACK_BACK,BACK,MIDDLE,FRONT }
	
	private String mapName;
	private int mapLenght = 512;
	private int mapHeight = 256;
	
	private String mapString[][][] = new String[maxMapVisualLevels][mapLenght][mapHeight];//Back1/back2/Normal/Front
	private String collisionMapString[][] = new String[mapLenght][mapHeight];//collision
	private Tile mapTiles[][][] = new Tile[maxMapVisualLevels][mapLenght][mapHeight];
	

	public WorldMap(int mapSizeX,int mapSizeY,String inMapName){
		setMapLenght(mapSizeX);
		setMapHeight(mapSizeY);
		mapName = inMapName;
		setBlankMap();
	}

	private void setBlankMap() {
		for(int visualMapLevel=0;visualMapLevel<mapString.length;visualMapLevel++){
			for(int visualMapX=0;visualMapX<mapString.length;visualMapX++){
				for(int visualMapY=0;visualMapY<mapString.length;visualMapY++){
					mapString[visualMapLevel][visualMapX][visualMapY]="";
					mapTiles[visualMapLevel][visualMapX][visualMapY] = new Tile();
				}
			}
		}
		
		for(int collisionMapX=0;collisionMapX<mapString.length;collisionMapX++){
			for(int collisionMapY=0;collisionMapY<mapString.length;collisionMapY++){
				collisionMapString[collisionMapX][collisionMapY]="";
			}
		}
	}
	
	public boolean loadMap()
	{
		InputStream mapInputStream;
		int CurrentLineNum = 0;
		int CurrentReadingLevel = 0;

		try {

			URL mapFileLocation = getClass().getResource(mapName);
			mapInputStream = mapFileLocation.openStream();
			InputStreamReader mapInputStreamReader = new InputStreamReader(mapInputStream);
			BufferedReader mapBufferedReader = new BufferedReader(mapInputStreamReader);
			String currentReadingLine = null;
			boolean readingCollision = false;

			//Reads down the document
			while ((currentReadingLine = mapBufferedReader.readLine()) != null) {

				String currentReadingLineSplit[] = currentReadingLine.split(",", -1);

				for (int mapLenghtXIndex = 0; mapLenghtXIndex < mapLenght; mapLenghtXIndex++) {
					if (readingCollision == false) {
						if (currentReadingLineSplit.length > 1 && mapLenghtXIndex < currentReadingLineSplit.length) {
							mapString[CurrentReadingLevel][mapLenghtXIndex][CurrentLineNum] = currentReadingLineSplit[mapLenghtXIndex];
						}
					} else {
						if (currentReadingLineSplit.length > 1 && mapLenghtXIndex < currentReadingLineSplit.length) {
							collisionMapString[mapLenghtXIndex][CurrentLineNum] = currentReadingLineSplit[mapLenghtXIndex];
						}
					}
					
				}
				
				CurrentLineNum++;

				if (CurrentLineNum == mapHeight || currentReadingLine.equals("")) {
					if (CurrentLineNum == mapHeight) {
						currentReadingLine = mapBufferedReader.readLine();
					}

					CurrentLineNum = 0;
					CurrentReadingLevel++;
					if (CurrentReadingLevel == 4) {
						readingCollision = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		initMapTiles();

		return true;
	}
	

	private void initMapTiles() {
		for(int mapLevel=0;mapLevel<maxMapVisualLevels;mapLevel++){
			for(int mapX=0;mapX<mapLenght;mapX++){
				for(int mapY=0;mapY<mapHeight;mapY++){
					initTile(mapLevel,mapX,mapY);
				}	
			}	
		}	
	}

	private void initTile(int mapLevel, int mapX, int mapY) {
	
		Image[] globalTileImages = Global.getGlobals().tileImages;
		Image[] globalTriangleTileImages = Global.getGlobals().triangleTileImages;
		
		String tileString = mapString[mapLevel][mapX][mapY];

		try {
			//Tile image index can go from 1 to globalTileImages.lenght, index 0 = no texture
			if (tileString.matches("^[0-9-<>]+$"))// a-zA-Z([0-9\,\.\+\-]+)
			{
				int negativeFlag = 1;
				int tileImageIndex = 0;
				boolean tileIsTriangleTile = false;


				//Tile does not contain < or > and can be negative
				if (!(tileString.contains("<") || tileString.contains(">"))) {
					//Convert String to int
					tileImageIndex = Integer.parseInt(tileString);
					//tileImageIndex is negative
					if (tileImageIndex < 0){
						//Convert to positive and store negative flag
						tileImageIndex*=-1;
						negativeFlag=-1;
					}

					//Make sure there are tiles and that the index does not exceed that number
					if(globalTileImages!=null){
						if (tileImageIndex > globalTileImages.length) {
							tileImageIndex=globalTileImages.length-1;
						}
					}  

				}else{
					//Tile contains < or > and must not have -
					//Grab the number after the first char
					tileImageIndex = Integer.parseInt(tileString.substring(1));
					tileIsTriangleTile = true;
					//tile is an inverted slope.
					if (tileString.contains(">")) {
						negativeFlag=-1;
					}
				}

				if (tileImageIndex>0){
					//Convert to array. image index 0 before this if is to have a blank tile
					tileImageIndex--;

					if(globalTileImages!=null){
						if (globalTileImages[tileImageIndex] != null) {
							if (tileIsTriangleTile == false) {
								//Normal tile
								mapTiles[mapLevel][mapX][mapY] = new Tile(globalTileImages[tileImageIndex]);
								mapTiles[mapLevel][mapX][mapY].setInvertedXFlag(negativeFlag);
								mapTiles[mapLevel][mapX][mapY].setWorldPosition(mapX*TILE_SPACING,-mapY*TILE_SPACING);

							} else {
								//Triangle tile
								mapTiles[mapLevel][mapX][mapY] = new Tile(globalTriangleTileImages[tileImageIndex]);
								mapTiles[mapLevel][mapX][mapY].setInvertedXFlag(negativeFlag);
								mapTiles[mapLevel][mapX][mapY].setWorldPosition(mapX*TILE_SPACING,-mapY*TILE_SPACING);

							}
						}
					}
				}
			}
			//TODO load animated tiles
			Image[][] globalAnimatedTileImages = Global.getGlobals().animatedTileImages;

			if(tileString.length()==3){
				if(tileString.charAt(0)=='A'){
					String numberAnimatedEffectString=tileString.substring(1);
					if(numberAnimatedEffectString.matches("^[0-9-]+$")){
						int numberAnimatedEffect=Integer.parseInt(numberAnimatedEffectString);
						if(globalAnimatedTileImages!=null) {
							if(numberAnimatedEffect<globalAnimatedTileImages.length)
							{
								if(globalAnimatedTileImages[numberAnimatedEffect][0]!=null)
								{

									//Normal tile
									mapTiles[mapLevel][mapX][mapY] = new Tile(globalAnimatedTileImages[numberAnimatedEffect]);
									//mapTiles[mapLevel][mapY][mapX].setInvertedXFlag(negativeFlag);
									mapTiles[mapLevel][mapX][mapY].setWorldPosition(mapX*TILE_SPACING,-mapY*TILE_SPACING);

								}
							}
						}
						else
						{
							mapTiles[mapLevel][mapX][mapY] = new Tile();
						}
					}
				}

				if(tileString.charAt(0)=='a'){
					String numberAnimatedEffectString=tileString.substring(1);
					if(numberAnimatedEffectString.matches("^[0-9-]+$")){
						int numberAnimatedEffect=Integer.parseInt(numberAnimatedEffectString);
						//logger.debug("animatedTileImages == null :"+ Global.getGlobals().animatedTileImages == null);
						if(globalAnimatedTileImages!=null) {
							if(numberAnimatedEffect< globalAnimatedTileImages.length)
							{
								if(globalAnimatedTileImages[numberAnimatedEffect][0]!=null)
								{
									//Normal tile
									mapTiles[mapLevel][mapX][mapY] = new Tile(globalAnimatedTileImages[numberAnimatedEffect]);
									mapTiles[mapLevel][mapX][mapY].setWorldPosition(mapX*TILE_SPACING,-mapY*TILE_SPACING);
								}
							}
						}
						else
						{
							mapTiles[mapLevel][mapX][mapY] = new Tile();
						}
					}
				}
			}
		}catch(Exception e) {logger.error("map apears to be corrupt at position "+mapLevel+","+mapX+","+mapY);}

		//if tile has not yet been created, create a blank tile.
		if(mapTiles[mapLevel][mapX][mapY]==null) {
			mapTiles[mapLevel][mapX][mapY] = new Tile();
			mapTiles[mapLevel][mapX][mapY].setWorldPosition(mapX*TILE_SPACING,-mapY*TILE_SPACING);
		}
	}

	public int getMapLenght() {
		return mapLenght;
	}

	private void setMapLenght(int mapLenght) {
		this.mapLenght = mapLenght;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	private void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}
	
	public String getMapString(int level,int x,int y) {
		return mapString[level][x][y];
	}
	
	public Tile getMapTile(int level,int x,int y) {
		return mapTiles[level][x][y];
	}
	
	public String getCollisionMapString(int level,int x,int y) {
		return collisionMapString[x][y];
	}
}
