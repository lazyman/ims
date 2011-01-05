package org.tingoo.client.datapick;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.BorderUIResource;


//========================================================

public final class DatePicker extends JPanel {

 private static final long serialVersionUID = 1L;

// private static final int startX = 10;
 private static final int startX =10;

 private static final int startY = 60;

 private static final Font smallFont = new Font("Dialog", Font.PLAIN, 10);

 private static final Font largeFont = new Font("Dialog", Font.PLAIN, 12);

 private static final Insets insets = new Insets(2, 2, 2, 2);

 private static final Color highlight = new Color(255, 255, 204);

 private static final Color white = new Color(255, 255, 255);

 private static final Color gray = new Color(204, 204, 204);

 private Component selectedDay = null;

 private GregorianCalendar selectedDate = null;

 private GregorianCalendar originalDate = null;

 private boolean hideOnSelect = true;

 private final JButton backButton = new JButton();

 private final JLabel monthAndYear = new JLabel();

 private final JButton forwardButton = new JButton();

 private final JLabel[] dayHeadings = new JLabel[] { new JLabel("Sun"),
   new JLabel("Mon"), new JLabel("Tus"), new JLabel("Wed"), new JLabel("Thu"),
   new JLabel("Fri"), new JLabel("Sat") };

 private final JLabel[][] daysInMonth = new JLabel[][] {
   { new JLabel(), new JLabel(), new JLabel(), new JLabel(),
     new JLabel(), new JLabel(), new JLabel() },
   { new JLabel(), new JLabel(), new JLabel(), new JLabel(),
     new JLabel(), new JLabel(), new JLabel() },
   { new JLabel(), new JLabel(), new JLabel(), new JLabel(),
     new JLabel(), new JLabel(), new JLabel() },
   { new JLabel(), new JLabel(), new JLabel(), new JLabel(),
     new JLabel(), new JLabel(), new JLabel() },
   { new JLabel(), new JLabel(), new JLabel(), new JLabel(),
     new JLabel(), new JLabel(), new JLabel() },
   { new JLabel(), new JLabel(), new JLabel(), new JLabel(),
     new JLabel(), new JLabel(), new JLabel() } };

 private final JButton todayButton = new JButton();

 private final JButton cancelButton = new JButton();

 public DatePicker() {
  super();
  selectedDate = getToday();
  init();
 }

 public DatePicker(final Date initialDate) {
  super();
  if (null == initialDate)
   selectedDate = getToday();
  else
   (selectedDate = new GregorianCalendar()).setTime(initialDate);
  originalDate = new GregorianCalendar(
    selectedDate.get(Calendar.YEAR),
    selectedDate.get(Calendar.MONTH),
    selectedDate.get(Calendar.DATE));
  init();
 }

 public boolean isHideOnSelect() {
  return hideOnSelect;
 }

 public void setHideOnSelect(final boolean hideOnSelect) {
  if (this.hideOnSelect != hideOnSelect) {
   this.hideOnSelect = hideOnSelect;
   initButtons(false);
  }
 }

 public Date getDate() {
  if (null != selectedDate)
   return selectedDate.getTime();
  return null;
 }

 private void init() {
  setLayout(new AbsoluteLayout());
//  this.setMinimumSize(new Dimension(161, 226));
  this.setMinimumSize(new Dimension(300, 226));
  this.setMaximumSize(getMinimumSize());
  this.setPreferredSize(getMinimumSize());
  this.setBorder(new BorderUIResource.EtchedBorderUIResource());

  backButton.setFont(smallFont);
  backButton.setText("<");
  backButton.setMargin(insets);
  backButton.setDefaultCapable(false);
  backButton.addActionListener(new ActionListener() {
   public void actionPerformed(final ActionEvent evt) {
    onBackClicked(evt);
   }
  });
  add(backButton, new AbsoluteConstraints(40, 10, 20, 20));

  monthAndYear.setFont(largeFont);
  monthAndYear.setHorizontalAlignment(JTextField.CENTER);
  monthAndYear.setText(formatDateText(selectedDate.getTime()));
  add(monthAndYear, new AbsoluteConstraints(50, 10, 200, 20));

  forwardButton.setFont(smallFont);
  forwardButton.setText(">");
  forwardButton.setMargin(insets);
  forwardButton.setDefaultCapable(false);
  forwardButton.addActionListener(new ActionListener() {
   public void actionPerformed(final ActionEvent evt) {
    onForwardClicked(evt);
   }
  });
//  add(forwardButton, new AbsoluteConstraints(130, 10, 20, 20));
  add(forwardButton, new AbsoluteConstraints(240, 10, 20, 20));

  int x = startX;
  for (int ii = 0; ii < dayHeadings.length; ii++) {
   dayHeadings[ii].setOpaque(true);
   dayHeadings[ii].setBackground(Color.LIGHT_GRAY);
   dayHeadings[ii].setForeground(Color.WHITE);
   dayHeadings[ii].setHorizontalAlignment(JLabel.CENTER);
//   add(dayHeadings[ii], new AbsoluteConstraints(x, 40, 21, 21));
   add(dayHeadings[ii], new AbsoluteConstraints(x, 40, 40, 21));
//   x += 20;
   x += 40;
  }

  x = startX;
  int y = startY;
  for (int ii = 0; ii < daysInMonth.length; ii++) {
   for (int jj = 0; jj < daysInMonth[ii].length; jj++) {
    daysInMonth[ii][jj].setOpaque(true);
    daysInMonth[ii][jj].setBackground(white);
    daysInMonth[ii][jj].setFont(smallFont);
    daysInMonth[ii][jj].setHorizontalAlignment(JLabel.CENTER);
    daysInMonth[ii][jj].setText("");
    daysInMonth[ii][jj].addMouseListener(new MouseAdapter() {
     public void mouseClicked(final MouseEvent evt) {
      onDayClicked(evt);
     }
    });
//    add(daysInMonth[ii][jj], new AbsoluteConstraints(x, y, 21, 21));
    add(daysInMonth[ii][jj], new AbsoluteConstraints(x, y, 40, 21));
//    x += 20;
    x += 40;
   }
   x = startX;
   y += 20;
  }

  initButtons(true);

  calculateCalendar();
 }

 private void initButtons(final boolean firstTime) {
  if (firstTime) {
   final Dimension buttonSize = new Dimension(68, 24);
   todayButton.setText("Today");
   todayButton.setMargin(insets);
   todayButton.setMaximumSize(buttonSize);
   todayButton.setMinimumSize(buttonSize);
   todayButton.setPreferredSize(buttonSize);
   todayButton.setDefaultCapable(true);
   todayButton.setSelected(true);
   todayButton.addActionListener(new ActionListener() {
    public void actionPerformed(final ActionEvent evt) {
     onToday(evt);
    }
   });

   cancelButton.setText("Cancel");
   cancelButton.setMargin(insets);
   cancelButton.setMaximumSize(buttonSize);
   cancelButton.setMinimumSize(buttonSize);
   cancelButton.setPreferredSize(buttonSize);
   cancelButton.addActionListener(new ActionListener() {
    public void actionPerformed(final ActionEvent evt) {
     onCancel(evt);
    }
   });
  } else {
   this.remove(todayButton);
   this.remove(cancelButton);
  }

  if (hideOnSelect) {
//   add(todayButton, new AbsoluteConstraints(25, 190, 52, -1));
   add(todayButton, new AbsoluteConstraints(57, 190, 52, -1));
//   add(cancelButton, new AbsoluteConstraints(87, 190, 52, -1));
   add(cancelButton, new AbsoluteConstraints(187, 190, 52, -1));
  } else {
   add(todayButton, new AbsoluteConstraints(55, 240, 52, -1));
  }
 }

 private void onToday(final java.awt.event.ActionEvent evt) {
  selectedDate = getToday();
  setVisible(!hideOnSelect);
  if (isVisible()) {
   monthAndYear.setText(formatDateText(selectedDate.getTime()));
   calculateCalendar();
  }
 }

 private void onCancel(final ActionEvent evt) {
  selectedDate = originalDate;
  setVisible(!hideOnSelect);
 }

 private void onForwardClicked(final java.awt.event.ActionEvent evt) {
  final int day = selectedDate.get(Calendar.DATE);
  selectedDate.set(Calendar.DATE, 1);
  selectedDate.add(Calendar.MONTH, 1);
  selectedDate.set(Calendar.DATE, Math.min(day,
    calculateDaysInMonth(selectedDate)));
  monthAndYear.setText(formatDateText(selectedDate.getTime()));
  calculateCalendar();
 }

 private void onBackClicked(final java.awt.event.ActionEvent evt) {
  final int day = selectedDate.get(Calendar.DATE);
  selectedDate.set(Calendar.DATE, 1);
  selectedDate.add(Calendar.MONTH, -1);
  selectedDate.set(Calendar.DATE, Math.min(day,
    calculateDaysInMonth(selectedDate)));
  monthAndYear.setText(formatDateText(selectedDate.getTime()));
  calculateCalendar();
 }

 private void onDayClicked(final java.awt.event.MouseEvent evt) {
  final javax.swing.JLabel fld = (javax.swing.JLabel) evt.getSource();
  if (!"".equals(fld.getText())) {
   fld.setBackground(highlight);
   selectedDay = fld;
   selectedDate.set(Calendar.DATE, Integer.parseInt(fld.getText()));
   setVisible(!hideOnSelect);
  }
 }

 private static GregorianCalendar getToday() {
  final GregorianCalendar gc = new GregorianCalendar();
  gc.set(Calendar.HOUR_OF_DAY, 0);
  gc.set(Calendar.MINUTE, 0);
  gc.set(Calendar.SECOND, 0);
  gc.set(Calendar.MILLISECOND, 0);
  return gc;
 }

 private void calculateCalendar() {
  if (null != selectedDay) {
   selectedDay.setBackground(white);
   selectedDay = null;
  }

  final GregorianCalendar c = new GregorianCalendar(selectedDate
    .get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), 1);

  final int maxDay = calculateDaysInMonth(c);

  final int selectedDay = Math.min(maxDay, selectedDate
    .get(Calendar.DATE));

  int dow = c.get(Calendar.DAY_OF_WEEK);
  for (int dd = 0; dd < dow; dd++) {
   daysInMonth[0][dd].setText("");
  }

  int week;
  do {
   week = c.get(Calendar.WEEK_OF_MONTH);
   dow = c.get(Calendar.DAY_OF_WEEK);
   final JLabel fld = this.daysInMonth[week - 1][dow - 1];
   fld.setText(Integer.toString(c.get(Calendar.DATE)));
   if (selectedDay == c.get(Calendar.DATE)) {
    fld.setBackground(highlight);
    this.selectedDay = fld;
   }
   if (c.get(Calendar.DATE) >= maxDay)
    break;
   c.add(Calendar.DATE, 1);
  } while (c.get(Calendar.DATE) <= maxDay);

  week--;
  for (int ww = week; ww < daysInMonth.length; ww++) {
   for (int dd = dow; dd < daysInMonth[ww].length; dd++) {
    daysInMonth[ww][dd].setText("");
   }
   dow = 0;
  }

  c.set(Calendar.DATE, selectedDay);
  selectedDate = c;
 }

 private static int calculateDaysInMonth(final Calendar c) {
  int daysInMonth = 0;
  switch (c.get(Calendar.MONTH)) {
  case 0:
  case 2:
  case 4:
  case 6:
  case 7:
  case 9:
  case 11:
   daysInMonth = 31;
   break;
  case 3:
  case 5:
  case 8:
  case 10:
   daysInMonth = 30;
   break;
  case 1:
   final int year = c.get(Calendar.YEAR);
   daysInMonth = (0 == year % 1000) ? 29 : (0 == year % 100) ? 28
     : (0 == year % 4) ? 29 : 28;
   break;
  }
  return daysInMonth;
 }

 private static String formatDateText(final Date dt) {
  final DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
  final StringBuffer mm = new StringBuffer();
  final StringBuffer yy = new StringBuffer();
  final FieldPosition mmfp = new FieldPosition(DateFormat.MONTH_FIELD);
  final FieldPosition yyfp = new FieldPosition(DateFormat.YEAR_FIELD);
  df.format(dt, mm, mmfp);
  df.format(dt, yy, yyfp);
  return
   (

    yy.toString().substring(yyfp.getBeginIndex(), yyfp.getEndIndex())
    + "-"
//    + " Year -"
    +mm.toString().substring(mmfp.getBeginIndex(),mmfp.getEndIndex())
//    + " Month"
   );
 }

}

