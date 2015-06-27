package de.sfnkassel.javaBeam.server.draw;

import static de.sfnkassel.javaBeam.server.Main.*;
import static de.sfnkassel.javaBeam.server.util.ByteConversions.*;

import de.sfnkassel.javaBeam.server.util.ArrayUtil;
import de.sfnkassel.javaBeam.server.util.SpriteType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Drawer {
	
	GraphicsContext graphics;
	
	public Drawer(Canvas canvas){
		graphics = canvas.getGraphicsContext2D();
		
		BoxBlur blur = new BoxBlur();
		blur.setWidth(2);
		blur.setHeight(2);
		blur.setIterations(100);
		graphics.setEffect(blur);
	}
	
	public void drawCommand(Byte[] command) throws UnsupportedOperationException{
		switch(command[0]){
			case SpriteType.CMD_DRAW_PIXEL:
				graphics.setFill(new Color(((double)command[1])/255, ((double)command[2])/255, ((double)command[3])/255, 1));
				graphics.fillRect(intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 4, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 8, 4)), 1, 1);
				break;
			case SpriteType.CMD_DRAW_RECTANGLE:
				graphics.setFill(new Color(((double)command[1])/255, ((double)command[2])/255, ((double)command[3])/255, 1));
				graphics.fillRect(intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 4, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 8, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 12, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 16, 4)));
				break;
			case SpriteType.CMD_DRAW_LINE:
				graphics.setStroke(new Color(((double)command[1])/255, ((double)command[2])/255, ((double)command[3])/255, 1));
				graphics.setLineWidth(intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 20, 4)));
				graphics.strokeLine(intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 4, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 8, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 12, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 16, 4)));
				break;
			case SpriteType.CMD_DRAW_CIRCLE:
				graphics.setFill(new Color(((double)command[1])/255, ((double)command[2])/255, ((double)command[3])/255, 1));
				graphics.fillOval(intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 4, 4)) - intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 12, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 8, 4)) - intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 12, 4)), 2 * intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 12, 4)), 2 * intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 12, 4)));
				break;
			case SpriteType.CMD_DRAW_TEXT:
				graphics.setStroke(new Color(((double)command[1])/255, ((double)command[2])/255, ((double)command[3])/255, 1));
				graphics.setFont(Font.font ("Calibri", command[12]));
				String str = "";
				for(int i = 13; i < command.length; i += 2){
					str += charFromByteArray(ArrayUtil.<Byte>getSubarray(command, i, 2));
				}
				graphics.fillText(str, intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 4, 4)), intFromByteArray(ArrayUtil.<Byte>getSubarray(command, 8, 4)));
				break;
			case SpriteType.INVALID:
				info("Draw command recieved that was marked Invalid.");
				break;
			default:
				throw new UnsupportedOperationException("Unknown draw command recieved.");
		}
	}
}
