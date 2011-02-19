package org.tingoo.client.datapick;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sun.util.calendar.ZoneInfo;

public class MyCalen extends JFrame {

 private static final long serialVersionUID = 1L;
 private JPanel jContentPane = null;
 private JButton jButton = null;
 private DatePicker dp;
 private JTextField dateText = null;
 private JDialog dlg;
 private Point origin = new Point();

 /**
  * This method initializes jButton
  *
  * @return javax.swing.JButton
  */
 private JButton getJButton() {
  if (jButton == null) {
   jButton = new JButton();
   jButton.setBounds(new Rectangle(74, 68, 34, 10));
   jButton.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent e) {
     onButtonClick(e);
    }
   });
  }
  return jButton;
 }

 private static String dateToString(final Date dt) {
  if (null != dt) {
   String DATE_FORMAT_SHORT = "yyyy-MM-dd";
   SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);
   TimeZone zone = new ZoneInfo("GMT", 0);
   sdf.setTimeZone(zone);
   return sdf.format(dt);
   //                return DateFormat.getDateInstance(DateFormat.LONG).format(dt);
  }
  return null;
 }

 final class Listener extends ComponentAdapter {

  public void componentHidden(final ComponentEvent evt) {
   final Date dt = ((DatePicker) evt.getSource()).getDate();
   if (null != dt)
    dateText.setText(dateToString(dt));
   dlg.dispose();
  }

 }

 private static Date stringToDate(final String s) {
  try {
   return DateFormat.getDateInstance(DateFormat.LONG).parse(s);
  } catch (ParseException e) {
   return null;
  }
 }

 private void onButtonClick(final java.awt.event.ActionEvent evt) {
  if ("".equals(dateText.getText()))
   dp = new DatePicker();
  else {
   dp = new DatePicker(stringToDate(dateText.getText()));
   dp.setHideOnSelect(false);
     }
  dp.addComponentListener(new Listener());

  final Point p = dateText.getLocationOnScreen();
  p.setLocation(p.getX(), p.getY() - 1 + dateText.getSize().getHeight());

  dlg = new JDialog(new JFrame(), true);

  dlg.addMouseListener(new MouseAdapter() {
   public void mousePressed(MouseEvent e) {
    origin.x = e.getX();
    origin.y = e.getY();
   }
  });
  dlg.addMouseMotionListener(new MouseMotionAdapter() {
   public void mouseDragged(MouseEvent e) {
    Point p = dlg.getLocation();
    dlg.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()
      - origin.y);
   }
  });

  dlg.setLocation(p);
  dlg.setResizable(false);
  dlg.setUndecorated(true);
  dlg.getContentPane().add(dp);
  dlg.pack();
  dlg.setVisible(true);
 }

 /**
  * This method initializes date
  *
  * @return javax.swing.JTextField
  */
 private JTextField getDate() {
  if (dateText == null) {
   dateText = new JTextField();
   dateText.setBounds(new Rectangle(68, 24, 115, 22));
  }
  return dateText;
 }

 /**
  * @param args
  */
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  SwingUtilities.invokeLater(new Runnable() {
   public void run() {
    MyCalen thisClass = new MyCalen();
    thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    thisClass.setVisible(true);
   }
  });
 }

 /**
  * This is the default constructor
  */
 public MyCalen() {
  super();
  initialize();
 }

 /**
  * This method initializes this
  *
  * @return void
  */
 private void initialize() {
  this.setSize(300, 200);
  this.setContentPane(getJContentPane());
  this.setTitle("JFrame");
 }

 /**
  * This method initializes jContentPane
  *
  * @return javax.swing.JPanel
  */
 private JPanel getJContentPane() {
  if (jContentPane == null) {
   jContentPane = new JPanel();
   jContentPane.setLayout(null);
   jContentPane.add(getJButton(), null);
   jContentPane.add(getDate(), null);
  }
  return jContentPane;
 }

}