package com.wxq.apsv.controller;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.App;
import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.utils.Initialize;
import com.wxq.apsv.utils.Settings;
import org.apache.commons.lang.StringUtils;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;

public class SettingController extends JPanel implements TabController {
    @Override
    public WinTab getTabType() {
        return WinTab.SETTINGS;
    }

    private JPanel topWrapperPanel;

    private JPanel botWrapperPanel;

    private JRadioButton beautyEyeThemeRadio;

    private JRadioButton darculaThemeRadio;

    private JRadioButton _12pxRadio;

    private JRadioButton _13pxRadio;

    private JRadioButton _14pxRadio;

    private JRadioButton _15pxRadio;

    private JRadioButton _16pxRadio;

    private ArrayList<JRadioButton> fontsizeRadios = new ArrayList<>();

    private JRadioButton _30SRadio;

    private JRadioButton _40SRadio;

    private JRadioButton _60SRadio;

    private JRadioButton _80SRadio;

    private JRadioButton _100SRadio;

    private JRadioButton _120SRadio;

    private ArrayList<JRadioButton> grapIntervalRadios = new ArrayList<>();

    private JTextField pushSuccessTextField;

    private JButton confirmSuccessTextInputBtn;

    public SettingController() {
        this.InitViews();
        this.InitListeners();
    }

    private void InitViews() {
        this.setLayout(new GridLayoutManager(10, 10, new Insets(10, 10, 10, 10), -1, 20));

        topWrapperPanel = new JPanel();
        botWrapperPanel = new JPanel();
        // Border
        EtchedBorder line = new EtchedBorder();
        Border border1 = BorderFactory.createTitledBorder(line, "外观设置");
        Border border2 = BorderFactory.createTitledBorder(line, "任务设置");
        topWrapperPanel.setBorder(border1);
        botWrapperPanel.setBorder(border2);
        topWrapperPanel.setLayout(new GridLayoutManager(5, 12, new Insets(5, 5, 20, 5), -1, 5));
        botWrapperPanel.setLayout(new GridLayoutManager(5, 12, new Insets(10, 5, 10, 5), -1, 5));
        add(topWrapperPanel, new GridConstraints(0, 0, 1, 1, 5, 3, 0, 1|2, null, null, new Dimension(-1, 200), 0, false));
        add(botWrapperPanel, new GridConstraints(1, 0, 1, 1, 5, 3, 0, 1|2|4, null, null, null, 0, false));

        // theme options radio
        beautyEyeThemeRadio = new JRadioButton("BeautyEye");
        darculaThemeRadio = new JRadioButton("Darcula (Default)");
        String theme = Settings.getInstance().getTheme();
        switch (theme) {
            case "BeautyEye":
                beautyEyeThemeRadio.setSelected(true);
                darculaThemeRadio.setSelected(false);
                break;
            default:
                beautyEyeThemeRadio.setSelected(false);
                darculaThemeRadio.setSelected(true);
        }

        JPanel themeRow = new JPanel();
        themeRow.setLayout(new GridLayoutManager(1, 12, new Insets(5, 5, 10, 5), -1, 5));
        topWrapperPanel.add(themeRow, new GridConstraints(0, 0, 1, 12, 8, 1, 0, 0, null, null, new Dimension(-1, 45), 0, false));

        themeRow.add(new JLabel("主题选择"), new GridConstraints(0, 0, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));
        themeRow.add(darculaThemeRadio, new GridConstraints(0, 1, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));
        themeRow.add(beautyEyeThemeRadio, new GridConstraints(0, 2, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));
        themeRow.add(new JLabel(""), new GridConstraints(0, 3, 1, 1, 8, 1, 1|2, 0, null, null, null, 0, false));

        // 字体大小
        _12pxRadio = new JRadioButton("12px");
        fontsizeRadios.add(_12pxRadio);
        _13pxRadio = new JRadioButton("13px");
        fontsizeRadios.add(_13pxRadio);
        _14pxRadio = new JRadioButton("14px");
        fontsizeRadios.add(_14pxRadio);
        _15pxRadio = new JRadioButton("15px");
        fontsizeRadios.add(_15pxRadio);
        _16pxRadio = new JRadioButton("16px");
        fontsizeRadios.add(_16pxRadio);

        JPanel fontSizeRow = new JPanel();
        fontSizeRow.setLayout(new GridLayoutManager(1, 12, new Insets(5, 5, 10, 5), -1, 5));
        topWrapperPanel.add(fontSizeRow, new GridConstraints(1, 0, 1, 12, 8, 1, 0, 0, null, null, new Dimension(-1, 45), 0, false));

        fontSizeRow.add(new JLabel("字体大小"), new GridConstraints(0, 0, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));

        int[] fontSizes = new int[]{12, 13, 14, 15, 16};
        int currentFontSize = Settings.getInstance().getFontSize();
        for(int i=0; i<fontsizeRadios.size(); i++){
            JRadioButton radio = fontsizeRadios.get(i);
            radio.setSelected(fontSizes[i] == currentFontSize);
            fontSizeRow.add(radio, new GridConstraints(0, i+1, 1, 1, 8, 1, 0, 0, null, null, new Dimension(100, -1), 1, false));
        }
        fontSizeRow.add(new JLabel(""), new GridConstraints(0, 11, 1, 1, 0, 1, 1|2, 0, null, null, null, 0, false));

        // space row
        JPanel spaceRow = new JPanel();
        spaceRow.setLayout(new GridLayoutManager(1, 12, new Insets(5, 5, 10, 5), -1, 5));
        topWrapperPanel.add(spaceRow, new GridConstraints(4, 0, 1, 12, 8, 3, 1|2, 1|2, null, null, null, 0, false));

        // 定时抓取任务间隔
        _30SRadio = new JRadioButton("30s");
        grapIntervalRadios.add(_30SRadio);
        _40SRadio = new JRadioButton("40s");
        grapIntervalRadios.add(_40SRadio);
        _60SRadio = new JRadioButton("60s");
        grapIntervalRadios.add(_60SRadio);
        _80SRadio = new JRadioButton("80s");
        grapIntervalRadios.add(_80SRadio);
        _100SRadio = new JRadioButton("100s");
        grapIntervalRadios.add(_100SRadio);
        _120SRadio = new JRadioButton("120s");
        grapIntervalRadios.add(_120SRadio);

        JPanel grapIntervalRow = new JPanel();
        grapIntervalRow.setLayout(new GridLayoutManager(1, 12, new Insets(5, 5, 10, 5), -1, 5));
        botWrapperPanel.add(grapIntervalRow, new GridConstraints(0, 0, 1, 12, 8, 1, 0, 0, null, null, new Dimension(-1, 45), 0, false));

        grapIntervalRow.add(new JLabel("爬取时间间隔"), new GridConstraints(0, 0, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));

        int[] grapIntervals = new int[]{30, 40, 60, 80, 100, 120};
        int currentGrapInterval = Settings.getInstance().getGrapInterval();
        for(int i=0; i<grapIntervalRadios.size(); i++){
            JRadioButton radio = grapIntervalRadios.get(i);
            radio.setSelected(grapIntervals[i] == currentGrapInterval);
            grapIntervalRow.add(radio, new GridConstraints(0, i+1, 1, 1, 8, 1, 0, 0, null, null, new Dimension(100, -1), 1, false));
        }
        grapIntervalRow.add(new JLabel("注意：任务需重启方可生效"), new GridConstraints(0, 11, 1, 1, 0, 1, 1|2, 0, null, null, null, 0, false));

        // 推送服务器成功处理的响应文本
        JPanel successTextRow = new JPanel();
        successTextRow.setLayout(new GridLayoutManager(1, 12, new Insets(5, 5, 10, 5), -1, 5));
        botWrapperPanel.add(successTextRow, new GridConstraints(1, 0, 1, 12, 8, 1, 0, 0, null, null, new Dimension(-1, 45), 0, false));

        successTextRow.add(new JLabel("推送成功的响应文本"), new GridConstraints(0, 0, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));
        pushSuccessTextField = new JTextField(Settings.getInstance().getPushSuccessBody());
        successTextRow.add(pushSuccessTextField, new GridConstraints(0, 1, 1, 1, 0, 1, 0, 0, new Dimension(160, -1), null, null, 0, false));
        confirmSuccessTextInputBtn = new JButton("确定");
        successTextRow.add(confirmSuccessTextInputBtn, new GridConstraints(0, 2, 1, 1, 0, 1, 0, 0, new Dimension(50, -1), null, null, 0, false));
        successTextRow.add(new JLabel("注意：由接收推送的服务器响应文本决定"), new GridConstraints(0, 11, 1, 1, 0, 1, 1|2, 0, null, null, null, 0, false));

        // space row
        JPanel spaceRow2 = new JPanel();
        spaceRow2.setLayout(new GridLayoutManager(1, 12, new Insets(5, 5, 10, 5), -1, 5));
        botWrapperPanel.add(spaceRow2, new GridConstraints(4, 0, 1, 12, 8, 3, 1|2, 1|2|4, null, null, null, 0, false));
    }

    private void InitListeners() {
        beautyEyeThemeRadio.addActionListener(e -> {
            Settings.getInstance().setTheme("BeautyEye");
            beautyEyeThemeRadio.setSelected(true);
            darculaThemeRadio.setSelected(false);
            Initialize.InitTheme();
            SwingUtilities.updateComponentTreeUI( getRootPane() );
        });

        darculaThemeRadio.addActionListener(e -> {
            Settings.getInstance().setTheme("Darcula");
            beautyEyeThemeRadio.setSelected(false);
            darculaThemeRadio.setSelected(true);
//            Initialize.InitTheme();
//            SwingUtilities.updateComponentTreeUI( getRootPane() );
            JOptionPane.showMessageDialog(Frame.getFrames()[0], "需要重启生效", "提示", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        });

        int[] fontSizes = new int[]{12, 13, 14, 15, 16};
        for(int i=0; i<fontsizeRadios.size(); i++){
            JRadioButton radio = fontsizeRadios.get(i);
            int fontSize = fontSizes[i];
            radio.addActionListener(e -> {
                Settings.getInstance().setFontSize(fontSize);
                fontsizeRadios.forEach(r -> {
                    if (r == radio) {
                        r.setSelected(true);
                    } else {
                        r.setSelected(false);
                    }
                });
                Initialize.InitFont();
                SwingUtilities.updateComponentTreeUI( getRootPane() );
            });
        }

        int[] grapIntervals = new int[]{30, 40, 60, 80, 100, 120};
        for(int i=0; i<grapIntervalRadios.size(); i++){
            JRadioButton radio = grapIntervalRadios.get(i);
            int interval = grapIntervals[i];
            radio.addActionListener(e -> {
                Settings.getInstance().setGrapInterval(interval);
                grapIntervalRadios.forEach(r -> {
                    if (r == radio) {
                        r.setSelected(true);
                    } else {
                        r.setSelected(false);
                    }
                });
            });
        }

        confirmSuccessTextInputBtn.addActionListener(e -> {
            String input = pushSuccessTextField.getText();
            if (StringUtils.isEmpty(input)) {
                JOptionPane.showMessageDialog(Frame.getFrames()[0], "该文本不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                Settings.getInstance().setPushSuccessBody(input);
                JOptionPane.showMessageDialog(Frame.getFrames()[0], "设置保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
