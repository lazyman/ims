package org.tingoo.client.datapick;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.Enumeration;


//==================================================


public class AbsoluteLayout implements LayoutManager2, java.io.Serializable {
 static final long serialVersionUID = -1919857869177070440L;
 protected java.util.Hashtable constraints = new java.util.Hashtable();

 public void addLayoutComponent(String name, Component comp) {
  throw new IllegalArgumentException();
 }

 public void removeLayoutComponent(Component comp) {
  constraints.remove(comp);
 }

 public Dimension preferredLayoutSize(Container parent) {
  int maxWidth = 0;
  int maxHeight = 0;
  for (java.util.Enumeration e = constraints.keys(); e.hasMoreElements();) {
   Component comp = (Component) e.nextElement();
   AbsoluteConstraints ac = (AbsoluteConstraints) constraints
     .get(comp);
   Dimension size = comp.getPreferredSize();
   int width = ac.getWidth();
   if (width == -1)
    width = size.width;
   int height = ac.getHeight();
   if (height == -1)
    height = size.height;
   if (ac.x + width > maxWidth)
    maxWidth = ac.x + width;
   if (ac.y + height > maxHeight)
    maxHeight = ac.y + height;
  }
  return new Dimension(maxWidth, maxHeight);
 }

 public Dimension minimumLayoutSize(Container parent) {
  int maxWidth = 0;
  int maxHeight = 0;
  for (java.util.Enumeration e = constraints.keys(); e.hasMoreElements();) {
   Component comp = (Component) e.nextElement();
   AbsoluteConstraints ac = (AbsoluteConstraints) constraints
     .get(comp);
   Dimension size = comp.getMinimumSize();
   int width = ac.getWidth();
   if (width == -1)
    width = size.width;
   int height = ac.getHeight();
   if (height == -1)
    height = size.height;
   if (ac.x + width > maxWidth)
    maxWidth = ac.x + width;
   if (ac.y + height > maxHeight)
    maxHeight = ac.y + height;
  }
  return new Dimension(maxWidth, maxHeight);
 }

 public void layoutContainer(Container parent) {
  for (Enumeration e = constraints.keys(); e.hasMoreElements();) {
   Component comp = (Component) e.nextElement();
   AbsoluteConstraints ac = (AbsoluteConstraints) constraints
     .get(comp);
   Dimension size = comp.getPreferredSize();
   int width = ac.getWidth();
   if (width == -1)
    width = size.width;
   int height = ac.getHeight();
   if (height == -1)
    height = size.height;
   comp.setBounds(ac.x, ac.y, width, height);
  }
 }

 public void addLayoutComponent(Component comp, Object constr) {
  if (!(constr instanceof AbsoluteConstraints))
   throw new IllegalArgumentException();
  constraints.put(comp, constr);
 }

 public Dimension maximumLayoutSize(Container target) {
  return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
 }

 public float getLayoutAlignmentX(Container target) {
  return 0;
 }

 public float getLayoutAlignmentY(Container target) {
  return 0;
 }

 public void invalidateLayout(Container target) {
 }
}
