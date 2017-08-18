package com.wxq.apsv.controller;

import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.model.Constants;
import com.wxq.apsv.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController extends JPanel implements TabController {
    private final static Logger logger = LoggerFactory.getLogger(TaskDetailController.class);

    private JLabel logoLabel;

    private JLabel titleLabel;

    private JLabel copyrightLabel;

    private JLabel versionLabel;

    private JLabel linkLabel;

    @Override
    public WinTab getTabType() {
        return WinTab.ABOUT;
    }

    public AboutController() {
        this.InitViews();
        this.InitListeners();
    }

    private void InitViews() {
        this.setLayout(new GridLayoutManager(8, 6, new Insets(10, 10, 10, 10), -1, -1));

        this.add(new JLabel(""), new GridConstraints(0, 0, 1, 6, 0, 0, 1|2, 0, new Dimension(-1, 30), null, new Dimension(-1, 30), 0, false));

        logoLabel = new JLabel(new ImageIcon(this.getClass().getResource("/images/logo.png")));
        this.add(logoLabel, new GridConstraints(1, 0, 1, 6, 0, 0, 1|2, 0, null, null, new Dimension(-1, 175), 0, false));

        titleLabel = new JLabel(Constants.APP_NAME);
        titleLabel.setFont(new Font(Settings.getInstance().getFont(), Font.PLAIN, 32));
        this.add(titleLabel, new GridConstraints(2, 0, 1, 6, 0, 0, 1|2, 0, new Dimension(-1, 100), null, new Dimension(-1, 100), 0, false));

        this.add(new JLabel(""), new GridConstraints(3, 0, 1, 6, 0, 0, 1|2, 1|2, null, null, null, 0, false));

        copyrightLabel = new JLabel("<html>Copyright (c) 2017. " + "<a href=\"" + Constants.APP_AUTHOR_GITHUB + "\">" + Constants.APP_AUTHOR + "</a></html>");
        this.add(copyrightLabel, new GridConstraints(5, 0, 1, 6, 0, 0, 1|2, 0, null, null, new Dimension(-1, 30), 0, false));

        versionLabel = new JLabel("Version " + Constants.APP_VERSION + "_b170818");
        this.add(versionLabel, new GridConstraints(6, 0, 1, 6, 0, 0, 1|2, 0, null, null, new Dimension(-1, 30), 0, false));

        linkLabel = new JLabel("<html>GitHub: <a href='" + Constants.APP_REPO + "'>" + Constants.APP_REPO + "</a></html>");
        this.add(linkLabel, new GridConstraints(7, 0, 1, 6, 0, 0, 1|2, 0, new Dimension(-1, 50), null, new Dimension(-1, 50), 0, false));

    }

    private void InitListeners() {
        this.copyrightLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(Constants.APP_AUTHOR_GITHUB));
                } catch (IOException e1) {
                    logger.error(e1.getMessage());
                } catch (URISyntaxException e1) {
                    logger.error(e1.getMessage());
                }
            }
        });

        this.linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(Constants.APP_REPO));
                } catch (IOException e1) {
                    logger.error(e1.getMessage());
                } catch (URISyntaxException e1) {
                    logger.error(e1.getMessage());
                }
            }
        });
    }
}
