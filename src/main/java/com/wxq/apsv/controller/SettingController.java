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
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
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

    public SettingController() {
        this.InitViews();
        this.InitListeners();
    }

    private void InitViews() {
        this.setLayout(new GridLayoutManager(10, 10, new Insets(10, 10, 10, 10), -1, 10));

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
        add(topWrapperPanel, new GridConstraints(0, 0, 1, 1, 5, 3, 0, 1|2, null, new Dimension(-1, 100), null, 0, false));
        add(botWrapperPanel, new GridConstraints(1, 0, 1, 1, 5, 3, 0, 1|2, null, null, null, 0, false));

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

        topWrapperPanel.add(new JLabel("主题选择"), new GridConstraints(0, 0, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));
        topWrapperPanel.add(darculaThemeRadio, new GridConstraints(0, 1, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));
        topWrapperPanel.add(beautyEyeThemeRadio, new GridConstraints(0, 2, 1, 1, 8, 1, 0, 0, null, null, null, 1, false));
        topWrapperPanel.add(new JLabel(""), new GridConstraints(0, 3, 1, 1, 8, 1, 1|2, 0, null, null, null, 0, false));
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
            ScheduledExecutorService schedulerExecutor = Executors.newScheduledThreadPool(2);
            try {
                System.out.println(App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            } catch (URISyntaxException e1) {

            }
            Callable<Process> callable = () -> {
                Process p = Runtime.getRuntime().exec("cmd /c start /b java -jar " + App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                return p;
            };
            FutureTask<Process> futureTask = new FutureTask<>(callable);
            schedulerExecutor.submit(futureTask);

            System.exit(0);
        });
    }
}
