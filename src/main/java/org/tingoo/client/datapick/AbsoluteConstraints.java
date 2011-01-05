package org.tingoo.client.datapick;

import java.awt.Dimension;
import java.awt.Point;


//====================================================================


public class AbsoluteConstraints implements java.io.Serializable {
 static final long serialVersionUID = 5261460716622152494L;
 public int x;
 public int y;
 public int width = -1;
 public int height = -1;

 public AbsoluteConstraints(Point pos) {
  this(pos.x, pos.y);
 }

 public AbsoluteConstraints(int x, int y) {
  this.x = x;
  this.y = y;
 }

 public AbsoluteConstraints(Point pos, Dimension size) {
  this.x = pos.x;
  this.y = pos.y;
  if (size != null) {
   this.width = size.width;
   this.height = size.height;
  }
 }

 public AbsoluteConstraints(int x, int y, int width, int height) {
  this.x = x;
  this.y = y;
  this.width = width;
  this.height = height;
 }

 public int getX() {
  return x;
 }

 public int getY() {
  return y;
 }

 public int getWidth() {
  return width;
 }

 public int getHeight() {
  return height;
 }

 public String toString() {
  return super.toString() + " [x=" + x + ", y=" + y + ", width=" + width
    + ", height=" + height + "]";
 }
}
