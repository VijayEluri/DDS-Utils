package de.danielsenff.de.madds.models;

import java.awt.Color;
import java.awt.Paint;

import net.bouthier.treemapSwing.fileViewer.TMFileModelDraw;
import de.danielsenff.de.madds.util.ByteConverter;
import de.danielsenff.de.madds.view.ColorPalette;

public class TMTextureModelDraw extends TMFileModelDraw {

	@Override
	public Paint getFillingOfObject(Object node) {
		
		TextureFile textureNode = TextureHashMap.getTextureHashMap().get(node);
		if(textureNode instanceof TextureFile) {
			switch (textureNode.getMaterial()) {
			case Normal:	return ColorPalette.colorNormal;
			case Diffuse:	return ColorPalette.colorDiffuse;
			case Specular:	return ColorPalette.colorSpecular;
			case Other:	return ColorPalette.colorOther;
			default:
				break;
			}
		}
		
		return ColorPalette.colorDefault; 
	}
	
	@Override
	public String getTooltipOfObject(Object node) {
		String tooltip = super.getTooltipOfObject(node);
		
		TextureFile textureNode = TextureHashMap.getTextureHashMap().get(node);
		if(textureNode instanceof TextureFile) {
			long size = textureNode.getSize();
			tooltip += "<p>"+ByteConverter.bit2MibiByte(size) + " MByte";
		}
		
		return tooltip;
	}
	
}