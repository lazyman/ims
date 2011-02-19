package org.tingoo.client.datapick;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.BorderUIResource;


//========================================================

public final class DatePicker extends JPanel {

	private static final long serialVersionUID = 1L;

	// private static final int startX = 10;
	private static final int startX = 10;

	private static final int startY = 60;

	private static final Font smallFont = new Font("Dialog", Font.PLAIN, 10);

	private static final Font largeFont = new Font("Dialog", Font.PLAIN, 12);

	private static final Insets insets = new Insets(2, 2, 2, 2);

	private static final Color selectedBg = new Color(255, 255, 204);

	private static final Color cellColor = Color.black;
	private static final Color cellBg = Color.white;
	private static final Color extraCellBg = Color.white;
	private static final Color extraCellColor = Color.LIGHT_GRAY;
	private static final Color headerBg = Color.GRAY;
	private static final Color headerText = Color.white;
	private static final Color wenkendColor = new Color(210, 72, 89);

	private Point beginP = new Point();
	private Point endP = new Point();
	private Point selectedP = new Point();

	private Component selectedDay = null;

	private GregorianCalendar selectedDate = null;

	private GregorianCalendar originalDate = null;

	private boolean hideOnSelect = true;

	private final JButton backButton = new JButton();

	private final JLabel monthAndYear = new JLabel();

	private final JButton forwardButton = new JButton();

	private final JLabel[] dayHeader = new JLabel[] { new JLabel("日"),
			new JLabel("一"), new JLabel("二"), new JLabel("三"), new JLabel("四"),
			new JLabel("五"), new JLabel("六") };

	// daysInMonth[y][x]
	private JLabel[][] daysInMonth = new JLabel[6][7];

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
		originalDate = new GregorianCalendar(selectedDate.get(Calendar.YEAR),
				selectedDate.get(Calendar.MONTH), selectedDate
						.get(Calendar.DATE));
		init();
	}

	public boolean isHideOnSelect() {
		return hideOnSelect;
	}

	public void setHideOnSelect(boolean hideOnSelect) {
		this.hideOnSelect = hideOnSelect;
	}

	public Date getDate() {
		if (null != selectedDate)
			return selectedDate.getTime();
		return null;
	}

	private void init() {
		setLayout(new AbsoluteLayout());
		// this.setMinimumSize(new Dimension(161, 226));
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
		// add(forwardButton, new AbsoluteConstraints(130, 10, 20, 20));
		add(forwardButton, new AbsoluteConstraints(240, 10, 20, 20));

		int x = startX;
		for (int ii = 0; ii < dayHeader.length; ii++) {
			dayHeader[ii].setOpaque(true);
			dayHeader[ii].setBackground(headerBg);
			dayHeader[ii].setForeground(headerText);
			dayHeader[ii].setHorizontalAlignment(JLabel.CENTER);
			// add(dayHeadings[ii], new AbsoluteConstraints(x, 40, 21, 21));
			add(dayHeader[ii], new AbsoluteConstraints(x, 40, 40, 21));
			// x += 20;
			x += 40;
		}

		x = startX;
		int y = startY;
		DayLabel label;
		for (int ii = 0; ii < daysInMonth.length; ii++) {
			for (int jj = 0; jj < daysInMonth[ii].length; jj++) {
				label = new DayLabel();
				label.getGridLocation().x = jj;
				label.getGridLocation().y = ii;

				label.setBackground(cellBg);
				label.setFont(smallFont);
				// daysInMonth[ii][jj].setText("");
				label.addMouseListener(new MouseAdapter() {
					public void mouseClicked(final MouseEvent evt) {
						onDayClicked(evt);
					}
				});
				// add(daysInMonth[ii][jj], new AbsoluteConstraints(x, y, 21,
				// 21));
				add(label, new AbsoluteConstraints(x, y, 40, 21));
				// x += 20;
				x += 40;

				daysInMonth[ii][jj] = label;
			}
			x = startX;
			y += 20;
		}

		initButtons();

		calculateCalendar();
		activeDate();
	}

	private void initButtons() {
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
		add(todayButton, new AbsoluteConstraints(57, 190, 52, -1));
		add(cancelButton, new AbsoluteConstraints(187, 190, 52, -1));

		// if (hideOnSelect) {
		// // add(todayButton, new AbsoluteConstraints(25, 190, 52, -1));
		// add(todayButton, new AbsoluteConstraints(57, 190, 52, -1));
		// // add(cancelButton, new AbsoluteConstraints(87, 190, 52, -1));
		// add(cancelButton, new AbsoluteConstraints(187, 190, 52, -1));
		// } else {
		// add(todayButton, new AbsoluteConstraints(55, 240, 52, -1));
		// }
	}

	// 今天
	private void onToday(final java.awt.event.ActionEvent evt) {
		selectedDate = getToday();
		setVisible(!hideOnSelect);
		if (isVisible()) {
			activeDate();
		}
	}

	private void onCancel(final ActionEvent evt) {
		selectedDate = originalDate;
		setVisible(false);
	}

	// 下一个月
	private void onForwardClicked(final java.awt.event.ActionEvent evt) {
		final int day = selectedDate.get(Calendar.DATE);
		selectedDate.add(Calendar.MONTH, 1);
		selectedDate.set(Calendar.DATE, Math.min(day,
				calculateDaysInMonth(selectedDate)));

		activeDate();
	}

	// 前一个月
	private void onBackClicked(final java.awt.event.ActionEvent evt) {
		final int day = selectedDate.get(Calendar.DATE);
		selectedDate.add(Calendar.MONTH, -1);
		selectedDate.set(Calendar.DATE, Math.min(day,
				calculateDaysInMonth(selectedDate)));

		activeDate();
	}

	private void onDayClicked(final java.awt.event.MouseEvent evt) {
		final DayLabel label = (DayLabel) evt.getSource();
		// 取得当前点击的日期位置
		Point p = label.getGridLocation();

		// 与上一次的日期比较位移，得出点击的日期
		int days = (p.y - selectedP.y) * 7 + p.x - selectedP.x;
		selectedDate.add(Calendar.DATE, days);

		activeDate();
		setVisible(!hideOnSelect);
	}

	/**
	 * 计算当前选择的日历，并将光标定到选择的日期上
	 */
	private void activeDate() {
		calculateCalendar();

		selectedDay = daysInMonth[selectedP.y][selectedP.x];
		selectedDay.setBackground(selectedBg);
		monthAndYear.setText(formatDateText(selectedDate.getTime()));
	}

	private static GregorianCalendar getToday() {
		final GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc;
	}

	/**
	 * 绘制日历表，其他附加信息不绘制
	 */
	private void calculateCalendar() {
		final GregorianCalendar c = new GregorianCalendar(selectedDate
				.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), 1);

		int maxDay = calculateDaysInMonth(c);
		int dow = c.get(Calendar.DAY_OF_WEEK);
		int week;
		// 取得月第一天坐标
		beginP.x = dow - 1;
		beginP.y = 0;
		// 取得月最后一天的坐标
		c.set(Calendar.DATE, maxDay);
		week = c.get(Calendar.WEEK_OF_MONTH);
		dow = c.get(Calendar.DAY_OF_WEEK);
		endP.x = dow - 1;
		endP.y = week - 1;
		// 取得指定天的坐标
		week = selectedDate.get(Calendar.WEEK_OF_MONTH);
		dow = selectedDate.get(Calendar.DAY_OF_WEEK);
		selectedP.x = dow - 1;
		selectedP.y = week - 1;

		paintCurCalendar();

		c.add(Calendar.MONTH, -1);
		maxDay = calculateDaysInMonth(c);
		paintExtraCell(maxDay);
	}

	/**
	 * 绘制当月日历
	 */
	private void paintCurCalendar() {
		int dd = 0;

		// 日期正文
		int day = 1;
		for (dd = beginP.x; dd < 7; dd++) {
			JLabel fld = this.daysInMonth[0][dd];
			fld.setText(Integer.toString(day++));
			fld.setBackground(cellBg);
			fld.setForeground(cellColor);
		}
		// 多绘制的日期将在后续部分被覆盖，无需担心
		for (int w = 1; w <= endP.y; w++) {
			for (int d = 0; d < 7; d++) {
				JLabel fld = this.daysInMonth[w][d];
				fld.setText(Integer.toString(day++));
				fld.setBackground(cellBg);
				fld.setForeground(cellColor);
			}
		}

		// weekend
		for (int i = 0; i < daysInMonth.length; i++) {
			daysInMonth[i][0].setForeground(wenkendColor);
			daysInMonth[i][6].setForeground(wenkendColor);
		}
	}

	/**
	 * 绘制前后月的日历，并指定背景色
	 * 
	 * @param lastMonthDay
	 *            上个月的最后一天
	 */
	private void paintExtraCell(int lastMonthDay) {
		int lastday = lastMonthDay;

		// 将日期起始部分设成空白
		int dd = 0;
		for (dd = beginP.x - 1; dd >= 0; dd--) {
			daysInMonth[0][dd].setText(Integer.toString(lastday--));
			daysInMonth[0][dd].setForeground(extraCellColor);
			daysInMonth[0][dd].setBackground(cellBg);
		}

		int nextday = 1;
		// 将日期结束部分设成空白
		for (dd = endP.x + 1; dd < daysInMonth[endP.y].length; dd++) {
			daysInMonth[endP.y][dd].setText(Integer.toString(nextday++));
			daysInMonth[endP.y][dd].setForeground(extraCellColor);
			daysInMonth[endP.y][dd].setBackground(cellBg);
		}
		for (int ww = endP.y + 1; ww < daysInMonth.length; ww++) {
			for (dd = 0; dd < daysInMonth[ww].length; dd++) {
				daysInMonth[ww][dd].setText(Integer.toString(nextday++));
				daysInMonth[ww][dd].setForeground(extraCellColor);
				daysInMonth[ww][dd].setBackground(cellBg);
			}
		}
	}

	/**
	 * 计算每月的天数
	 * 
	 * @param c
	 *            需要计算的月份
	 * @return 计算出来的天数
	 */
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
		return (

		yy.toString().substring(yyfp.getBeginIndex(), yyfp.getEndIndex()) + "-"
		// + " Year -"
		+ mm.toString().substring(mmfp.getBeginIndex(), mmfp.getEndIndex())
		// + " Month"
		);
	}
	
	private JDialog dlg;
	
	public JDialog getDateDialog() {
		addComponentListener(new Listener());

		final Point origin = new Point();
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

		dlg.setResizable(false);
		dlg.getContentPane().add(this);
		dlg.pack();

		return dlg;
	}

	class Listener extends ComponentAdapter {

		public void componentHidden(ComponentEvent evt) {
//			dlg.setVisible(false);
			dlg.dispose();
		}

	}

}
