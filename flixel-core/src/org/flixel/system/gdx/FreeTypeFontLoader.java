package org.flixel.system.gdx;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.utils.Array;

/**
 * <code>AssetLoader</code> to create a <code>BitmapFont</code> from a ttf file. Passing a <code>BitmapFontParameter</code> to
 * <code>AssetManager::load(String, Class, AssetLoaderParameters)</code> allows to specify whether the font
 * should be flipped on the y-axis or not.
 * 
 * @author Thomas Weston
 */
public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, FreeTypeFontLoader.FreeTypeFontParameter> 
{	
	private BitmapFontLoader _bitmapFontLoader;
	
	public FreeTypeFontLoader (FileHandleResolver resolver)
	{
		super(resolver);
		_bitmapFontLoader = new BitmapFontLoader(resolver);
	}

	@Override
	public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, FreeTypeFontParameter parameter)
	{
		BitmapFont font = null;
		
		String[] split = fileName.split(":");
		if(split[0].endsWith(".ttf"))
		{
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(resolve(split[0]));
			FreeTypeBitmapFontData data = generator.generateData(Integer.parseInt(split[1]), parameter != null ? parameter.characters : FreeTypeFontGenerator.DEFAULT_CHARS, parameter != null ? parameter.flip : true);
			generator.dispose();
			font = new BitmapFont(data, data.getTextureRegion(), true);
		}
		else
		{
			font = _bitmapFontLoader.loadSync(manager, fileName, file, parameter);
			if(parameter == null)
				font.getData().flipped = true;
		}
		
		return font;
	}
	
	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, FreeTypeFontParameter parameter)
	{
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FreeTypeFontParameter parameter)
	{
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
	
		if(fileName.endsWith(".fnt"))
			deps = _bitmapFontLoader.getDependencies(fileName, file, parameter);

		return deps;
	}

	static public class FreeTypeFontParameter extends BitmapFontParameter 
	{
        /** Which characters to create in the font **/
        public String characters = null;
	}
}