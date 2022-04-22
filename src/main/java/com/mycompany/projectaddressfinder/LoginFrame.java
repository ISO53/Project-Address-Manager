/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projectaddressfinder;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author termi
 */
public class LoginFrame extends javax.swing.JFrame {

    Timer timer;

    Point pressedLocation;

    String adminMail = "ADMIN_MAIL";

    Graphics2D paintGraphic;

    int second;
    int minute;
    int randomCode;
    int CURRENT_ID = 0; //The id of the logged account.
    int selectedColorNumber = 0;

    DefaultListModel personsListModel;
    DefaultTableModel addressesTableModel;

    Color errorColor;
    Color defaultBackground;
    Color pictureAdjusterSelectedColor = Color.BLACK;

    BufferedImage profileImageScaled = null;
    BufferedImage scaledDatabaseProfilePic = null;
    BufferedImage lineImage = null;
    BufferedImage worldMapDark = null;

    ImageIcon iconDefaultMan;
    ImageIcon iconDefaultWoman;
    ImageIcon iconLine;
    ImageIcon iconWorldMapDark;

    /**
     * Creates new form LoginFrame
     */
    public LoginFrame() {
        initComponents();

        this.paintGraphic = (Graphics2D) pnl_pictureFramePicPanel.getGraphics();

        this.setLocationRelativeTo(null);

        personsListModel = new DefaultListModel();
        addressesTableModel = new DefaultTableModel(new String[]{"ID", "COUNTRY", "CITY", "FULLADDRESS"}, 0);

        try {
            lineImage = ImageIO.read(this.getClass().getClassLoader().getResource("curvedlinenew.png"));
            worldMapDark = ImageIO.read(this.getClass().getClassLoader().getResource("worldMapDark.png"));
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        ImageIcon iconFrame = new ImageIcon(this.getClass().getClassLoader().getResource("icon.png"));
        ImageIcon iconLogo = new ImageIcon(this.getClass().getClassLoader().getResource("newworldmap.png"));
        ImageIcon iconWarning = new ImageIcon(this.getClass().getClassLoader().getResource("warning.png"));
        ImageIcon iconCheck = new ImageIcon(this.getClass().getClassLoader().getResource("check.png"));
        ImageIcon iconPassword = new ImageIcon(this.getClass().getClassLoader().getResource("password.png"));
        ImageIcon iconSearch = new ImageIcon(this.getClass().getClassLoader().getResource("search.png"));
        ImageIcon iconPassChange = new ImageIcon(this.getClass().getClassLoader().getResource("changePass.png"));
        ImageIcon iconBackup = new ImageIcon(this.getClass().getClassLoader().getResource("backup.png"));
        iconDefaultMan = new ImageIcon(this.getClass().getClassLoader().getResource("male.png"));
        iconDefaultWoman = new ImageIcon(this.getClass().getClassLoader().getResource("female.png"));
        iconWorldMapDark = new ImageIcon(worldMapDark);
        iconLine = new ImageIcon(lineImage);

        this.setIconImage(iconFrame.getImage());
        jfrm_userFrame.setIconImage(iconFrame.getImage());
        jfrm_adminFrame.setIconImage(iconFrame.getImage());
        jfrm_profilePicAdjustment.setIconImage(iconFrame.getImage());
        dlg_areasNotFilled.setIconImage(iconFrame.getImage());
        dlg_mailNotUnique.setIconImage(iconFrame.getImage());
        dlg_accountRegistered.setIconImage(iconFrame.getImage());
        dlg_forgotPassword.setIconImage(iconFrame.getImage());
        dlg_mailNotExist.setIconImage(iconFrame.getImage());
        dlg_wrongPassword.setIconImage(iconFrame.getImage());
        dlg_passwordChanged.setIconImage(iconFrame.getImage());
        dlg_changePassword.setIconImage(iconFrame.getImage());
        dlg_backupToComputer.setIconImage(iconFrame.getImage());
        dlg_savedToDatabase.setIconImage(iconFrame.getImage());
        dlg_updatedToDatabase.setIconImage(iconFrame.getImage());

        lbl_map.setIcon(iconLogo);
        lbl_registerNotFilled.setIcon(iconWarning);
        lbl_mailWarning.setIcon(iconWarning);
        lbl_accountRegistered.setIcon(iconCheck);
        lbl_forgotPassLabel1.setIcon(iconPassword);
        lbl_passChangedLabel.setIcon(iconCheck);
        lbl_mailNotExistWarning.setIcon(iconWarning);
        lbl_wrongPassword.setIcon(iconWarning);
        lbl_adminFrameAddressSearchIcon.setIcon(iconSearch);
        lbl_adminFramePersonSearchIcon.setIcon(iconSearch);
        lbl_guideMap.setIcon(iconWorldMapDark);
        lbl_pictureFrameCurvedLine.setIcon(iconLine);
        lbl_changePasswordPic.setIcon(iconPassChange);
        lbl_backup.setIcon(iconBackup);
        lbl_savedToDatabase.setIcon(iconCheck);
        lbl_updatedToDatabase.setIcon(iconCheck);

        jScrollPane1.setBorder(null);

        errorColor = new Color(237, 67, 55);
        defaultBackground = new Color(255, 255, 255);
        
        jfrm_adminFrame.setVisible(true);
        jfrm_profilePicAdjustment.setVisible(true);
        jfrm_userFrame.setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        dlg_areasNotFilled = new javax.swing.JDialog();
        pnl_registerNotFilledBackground = new javax.swing.JPanel();
        lbl_registerNotFilled = new javax.swing.JLabel();
        btn_registerNotFilledButton = new java.awt.Button();
        dlg_mailNotUnique = new javax.swing.JDialog();
        pnl_mailWarningBackground = new javax.swing.JPanel();
        lbl_mailWarning = new javax.swing.JLabel();
        btn_mailWarningButton = new java.awt.Button();
        dlg_accountRegistered = new javax.swing.JDialog();
        pnl_accountRegisteredBackground = new javax.swing.JPanel();
        lbl_accountRegistered = new javax.swing.JLabel();
        btn_accountRegisteredButton = new java.awt.Button();
        dlg_mailNotExist = new javax.swing.JDialog();
        pnl_mailNotExistWarningBackground = new javax.swing.JPanel();
        lbl_mailNotExistWarning = new javax.swing.JLabel();
        btn_mailNotExistWarningButton = new java.awt.Button();
        dlg_wrongPassword = new javax.swing.JDialog();
        pnl_wrongPasswordBackground = new javax.swing.JPanel();
        lbl_wrongPassword = new javax.swing.JLabel();
        btn_wrongPasswordButton = new java.awt.Button();
        dlg_forgotPassword = new javax.swing.JDialog();
        pnl_forgotPassBackground = new javax.swing.JPanel();
        lbl_forgotPassLabel1 = new javax.swing.JLabel();
        txtfld_forgotPassMail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        frmtd_forgotPassCode = new javax.swing.JFormattedTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        btn_forgotPassCodeButton = new java.awt.Button();
        lbl_timer = new javax.swing.JLabel();
        dlg_passwordChanged = new javax.swing.JDialog();
        pnl_passChangedBackground = new javax.swing.JPanel();
        lbl_passChangedLabel = new javax.swing.JLabel();
        btn_passChangedButton = new java.awt.Button();
        jfrm_userFrame = new javax.swing.JFrame();
        spltpn_userFrameSplitPane = new javax.swing.JSplitPane();
        pnl_userFrameLeftPanel = new javax.swing.JPanel();
        cmbbox_userCountries = new javax.swing.JComboBox<>();
        cmbbox_userCities = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtarea_userFullAddress = new javax.swing.JTextArea();
        lbl_userAddressInformation = new javax.swing.JLabel();
        btn_userLogOut = new java.awt.Button();
        btn_userSaveToDatabase = new java.awt.Button();
        btn_userUpdateToDatabase = new java.awt.Button();
        sprtr_userFrameSeperator = new javax.swing.JSeparator();
        lbl_userFrameProfilePic = new javax.swing.JLabel();
        btn_userFrameUploadPic = new java.awt.Button();
        btn_userFrameAdjustPicture = new java.awt.Button();
        btn_userFrameChangePassword = new java.awt.Button();
        pnl_userFrameRightPanel = new javax.swing.JPanel();
        scrlpn_map = new javax.swing.JScrollPane();
        lbl_guideMap = new javax.swing.JLabel();
        lbl_mapLabel = new javax.swing.JLabel();
        prgrsbr_userFrameMapLoading = new javax.swing.JProgressBar();
        mnbr_userFrameMenuBar = new javax.swing.JMenuBar();
        mn_userFrameApplication = new javax.swing.JMenu();
        mnTm_clearFullAddress = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mnTm_uploadPic = new javax.swing.JMenuItem();
        mnTm_adjustPic = new javax.swing.JMenuItem();
        mnTm_changePass = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mnTm_userFrameLogOut = new javax.swing.JMenuItem();
        mn_userFrameMap = new javax.swing.JMenu();
        mnTm_dontDisplayMap = new javax.swing.JCheckBoxMenuItem();
        mnTm_displayWorldMap = new javax.swing.JMenuItem();
        jfrm_adminFrame = new javax.swing.JFrame();
        spltpn_adminFrame = new javax.swing.JSplitPane();
        pnl_userFrameLeftPanel1 = new javax.swing.JPanel();
        lbl_adminPersonelInformations = new javax.swing.JLabel();
        btn_adminFrameLogOut = new java.awt.Button();
        btn_adminFrameDeleteUsersAddress = new java.awt.Button();
        btn_updateTheTable = new java.awt.Button();
        sprtr_userFrameSeperator1 = new javax.swing.JSeparator();
        lbl_adminFrameNameSurname = new javax.swing.JLabel();
        lbl_adminFrameMail = new javax.swing.JLabel();
        lbl_adminFrameDate = new javax.swing.JLabel();
        lbl_adminFrameGender = new javax.swing.JLabel();
        lbl_adminFrameID = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lst_adminFramePersonList = new javax.swing.JList<>();
        btn_applyChangesToDatabase = new java.awt.Button();
        btn_adminFrameDeleteUser = new java.awt.Button();
        jSeparator15 = new javax.swing.JSeparator();
        txtfld_adminFramePersonSearchBox = new javax.swing.JTextField();
        lbl_adminFramePersonSearchIcon = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        lbl_adminFrameProfilePic = new javax.swing.JLabel();
        pnl_userFrameRightPanel1 = new javax.swing.JPanel();
        lbl_adminAddressInformations = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_adminFrameTable = new javax.swing.JTable();
        txtfld_adminFrameAddressSearchBox = new javax.swing.JTextField();
        lbl_adminFrameAddressSearchIcon = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtpn_adminFrameLog = new javax.swing.JTextPane();
        mnbr_adminFrameMenuBar = new javax.swing.JMenuBar();
        mn_adminFrameApplication = new javax.swing.JMenu();
        mnTm_updateTable = new javax.swing.JMenuItem();
        mnTm_applyChangesDatabase = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        mnTm_deleteUser = new javax.swing.JMenuItem();
        mnTm_deleteUsersAddressInformations = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        mnTm_adminFrameLogOut = new javax.swing.JMenuItem();
        mn_adminFrameFile = new javax.swing.JMenu();
        mnTm_clearLogArea = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        mnTm_backupToComputer = new javax.swing.JMenuItem();
        jfrm_profilePicAdjustment = new javax.swing.JFrame();
        pnl_pictureFrameBackground = new javax.swing.JPanel();
        pnl_pictureFramePicPanel = new javax.swing.JPanel();
        pnl_pictureFrameColorsPanel = new javax.swing.JPanel();
        pnl_color1 = new javax.swing.JPanel();
        pnl_color2 = new javax.swing.JPanel();
        pnl_color3 = new javax.swing.JPanel();
        pnl_color4 = new javax.swing.JPanel();
        pnl_color5 = new javax.swing.JPanel();
        btn_pictureFrameOpenColorChooser = new java.awt.Button();
        sldr_pictureFrameThicknessSlider = new javax.swing.JSlider();
        btn_pictureFrameBackToUserFrame = new java.awt.Button();
        btn_pictureFrameSetPicToDatabase = new java.awt.Button();
        btn_pictureFrameGetPicFromDatabase = new java.awt.Button();
        btn_pictureFrameOpenNewImage = new java.awt.Button();
        lbl_pictureFrameCurvedLine = new javax.swing.JLabel();
        pnl_pictureFrameToolsPanel = new javax.swing.JPanel();
        flchsr_fileChooser = new javax.swing.JFileChooser();
        dlg_changePassword = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        lbl_enterOldPassword = new javax.swing.JLabel();
        pswrdfld_oldPassword = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_enterNewPassword = new javax.swing.JLabel();
        lbl_errorChangePassword = new javax.swing.JLabel();
        pswrdfld_newPassword = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
        btn_changeThePassword = new java.awt.Button();
        jLabel2 = new javax.swing.JLabel();
        lbl_changePasswordPic = new javax.swing.JLabel();
        dlg_backupToComputer = new javax.swing.JDialog();
        pnl_backupComputerBackground = new javax.swing.JPanel();
        lbl_backup = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btn_backupToComputer = new java.awt.Button();
        prgrsbr_backupProgress = new javax.swing.JProgressBar();
        lbl_downloadInformator = new javax.swing.JLabel();
        pppmn_userFrameMapPopUp = new javax.swing.JPopupMenu();
        mnTm_changeColorWorldMap = new javax.swing.JMenuItem();
        mnTm_displayWordlMapPopUp = new javax.swing.JMenuItem();
        dlg_savedToDatabase = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        lbl_savedToDatabase = new javax.swing.JLabel();
        btn_savedToDatabaseOK = new java.awt.Button();
        dlg_updatedToDatabase = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        lbl_updatedToDatabase = new javax.swing.JLabel();
        btn_updatedToDatabaseOK = new java.awt.Button();
        jSeparator20 = new javax.swing.JSeparator();
        pnl_loginFrameBackPanel = new javax.swing.JPanel();
        scrlpn_leftPanel = new javax.swing.JScrollPane();
        lbl_map = new javax.swing.JLabel();
        pnl_rightPanel = new javax.swing.JPanel();
        tbdpn_tabbedPane = new javax.swing.JTabbedPane();
        pnl_login = new javax.swing.JPanel();
        lbl_loginMail = new javax.swing.JLabel();
        txtfld_loginMail = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        lbl_loginPassword = new javax.swing.JLabel();
        btn_login = new java.awt.Button();
        lbl_forgottenPassword = new javax.swing.JLabel();
        lbl_dontHaveAccount = new javax.swing.JLabel();
        chcbx_rememberMe = new javax.swing.JCheckBox();
        pswrdfld_loginPassword = new javax.swing.JPasswordField();
        pnl_register = new javax.swing.JPanel();
        lbl_adSoyad = new javax.swing.JLabel();
        lbl_mail = new javax.swing.JLabel();
        txtfld_registerNameSurname = new javax.swing.JTextField();
        btn_register = new java.awt.Button();
        sprtr_loginFrameNameSurname = new javax.swing.JSeparator();
        sprtr_loginFrameMail = new javax.swing.JSeparator();
        txtfld_registerMail = new javax.swing.JTextField();
        lbl_password = new javax.swing.JLabel();
        pswrdfld_registerPassword = new javax.swing.JPasswordField();
        sprtr_loginFramePassword = new javax.swing.JSeparator();
        lbl_date = new javax.swing.JLabel();
        frmttxtfld_registerDate = new javax.swing.JFormattedTextField();
        sprtr_loginFrameDate = new javax.swing.JSeparator();
        lbl_gender = new javax.swing.JLabel();
        rdbtn_female = new javax.swing.JRadioButton();
        rdbtn_male = new javax.swing.JRadioButton();
        lbl_loginFrameNameSurnameError = new javax.swing.JLabel();
        lbl_loginFrameMailError = new javax.swing.JLabel();
        lbl_loginFramePasswordError = new javax.swing.JLabel();
        lbl_loginFrameDateError = new javax.swing.JLabel();

        dlg_areasNotFilled.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_areasNotFilled.setTitle("Warning");
        dlg_areasNotFilled.setAlwaysOnTop(true);
        dlg_areasNotFilled.setMinimumSize(new java.awt.Dimension(460, 160));
        dlg_areasNotFilled.setResizable(false);
        dlg_areasNotFilled.getContentPane().setLayout(new javax.swing.BoxLayout(dlg_areasNotFilled.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl_registerNotFilledBackground.setBackground(new java.awt.Color(44, 47, 51));
        pnl_registerNotFilledBackground.setForeground(new java.awt.Color(255, 255, 255));
        pnl_registerNotFilledBackground.setLayout(new java.awt.GridBagLayout());

        lbl_registerNotFilled.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_registerNotFilled.setForeground(new java.awt.Color(255, 255, 255));
        lbl_registerNotFilled.setText("Please make sure you have entered all fields completely!");
        pnl_registerNotFilledBackground.add(lbl_registerNotFilled, new java.awt.GridBagConstraints());

        btn_registerNotFilledButton.setBackground(new java.awt.Color(132, 100, 200));
        btn_registerNotFilledButton.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_registerNotFilledButton.setForeground(new java.awt.Color(255, 255, 255));
        btn_registerNotFilledButton.setLabel("OK");
        btn_registerNotFilledButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_areasNotFilledButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pnl_registerNotFilledBackground.add(btn_registerNotFilledButton, gridBagConstraints);

        dlg_areasNotFilled.getContentPane().add(pnl_registerNotFilledBackground);

        dlg_mailNotUnique.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_mailNotUnique.setTitle("Mail Warning");
        dlg_mailNotUnique.setAlwaysOnTop(true);
        dlg_mailNotUnique.setMinimumSize(new java.awt.Dimension(420, 160));
        dlg_mailNotUnique.setResizable(false);
        dlg_mailNotUnique.getContentPane().setLayout(new javax.swing.BoxLayout(dlg_mailNotUnique.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl_mailWarningBackground.setBackground(new java.awt.Color(44, 47, 51));
        pnl_mailWarningBackground.setForeground(new java.awt.Color(255, 255, 255));
        pnl_mailWarningBackground.setLayout(new java.awt.GridBagLayout());

        lbl_mailWarning.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_mailWarning.setForeground(new java.awt.Color(255, 255, 255));
        lbl_mailWarning.setText("This e-mail address already exists in the database!");
        pnl_mailWarningBackground.add(lbl_mailWarning, new java.awt.GridBagConstraints());

        btn_mailWarningButton.setBackground(new java.awt.Color(132, 100, 200));
        btn_mailWarningButton.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_mailWarningButton.setForeground(new java.awt.Color(255, 255, 255));
        btn_mailWarningButton.setLabel("OK");
        btn_mailWarningButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_mailWarningButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        pnl_mailWarningBackground.add(btn_mailWarningButton, gridBagConstraints);

        dlg_mailNotUnique.getContentPane().add(pnl_mailWarningBackground);

        dlg_accountRegistered.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_accountRegistered.setTitle("Account Registered");
        dlg_accountRegistered.setAlwaysOnTop(true);
        dlg_accountRegistered.setMinimumSize(new java.awt.Dimension(490, 170));
        dlg_accountRegistered.setResizable(false);
        dlg_accountRegistered.getContentPane().setLayout(new javax.swing.BoxLayout(dlg_accountRegistered.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl_accountRegisteredBackground.setBackground(new java.awt.Color(44, 47, 51));
        pnl_accountRegisteredBackground.setForeground(new java.awt.Color(255, 255, 255));
        pnl_accountRegisteredBackground.setMinimumSize(new java.awt.Dimension(380, 150));
        pnl_accountRegisteredBackground.setPreferredSize(new java.awt.Dimension(380, 150));
        pnl_accountRegisteredBackground.setLayout(new java.awt.GridBagLayout());

        lbl_accountRegistered.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_accountRegistered.setForeground(new java.awt.Color(255, 255, 255));
        lbl_accountRegistered.setText("Your account has been successfully registered. You may login.");
        pnl_accountRegisteredBackground.add(lbl_accountRegistered, new java.awt.GridBagConstraints());

        btn_accountRegisteredButton.setBackground(new java.awt.Color(132, 100, 200));
        btn_accountRegisteredButton.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_accountRegisteredButton.setForeground(new java.awt.Color(255, 255, 255));
        btn_accountRegisteredButton.setLabel("OK");
        btn_accountRegisteredButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_accountRegisteredButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        pnl_accountRegisteredBackground.add(btn_accountRegisteredButton, gridBagConstraints);

        dlg_accountRegistered.getContentPane().add(pnl_accountRegisteredBackground);

        dlg_mailNotExist.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_mailNotExist.setTitle("Mail Warning");
        dlg_mailNotExist.setAlwaysOnTop(true);
        dlg_mailNotExist.setMinimumSize(new java.awt.Dimension(470, 160));
        dlg_mailNotExist.setResizable(false);
        dlg_mailNotExist.getContentPane().setLayout(new javax.swing.BoxLayout(dlg_mailNotExist.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl_mailNotExistWarningBackground.setBackground(new java.awt.Color(44, 47, 51));
        pnl_mailNotExistWarningBackground.setForeground(new java.awt.Color(255, 255, 255));
        pnl_mailNotExistWarningBackground.setLayout(new java.awt.GridBagLayout());

        lbl_mailNotExistWarning.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_mailNotExistWarning.setForeground(new java.awt.Color(255, 255, 255));
        lbl_mailNotExistWarning.setText("This e-mail address is not in the database. Please register.");
        pnl_mailNotExistWarningBackground.add(lbl_mailNotExistWarning, new java.awt.GridBagConstraints());

        btn_mailNotExistWarningButton.setBackground(new java.awt.Color(132, 100, 200));
        btn_mailNotExistWarningButton.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_mailNotExistWarningButton.setForeground(new java.awt.Color(255, 255, 255));
        btn_mailNotExistWarningButton.setLabel("OK");
        btn_mailNotExistWarningButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_mailNotExistWarningButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        pnl_mailNotExistWarningBackground.add(btn_mailNotExistWarningButton, gridBagConstraints);

        dlg_mailNotExist.getContentPane().add(pnl_mailNotExistWarningBackground);

        dlg_wrongPassword.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_wrongPassword.setTitle("Register Warning");
        dlg_wrongPassword.setAlwaysOnTop(true);
        dlg_wrongPassword.setMinimumSize(new java.awt.Dimension(450, 160));
        dlg_wrongPassword.setResizable(false);
        dlg_wrongPassword.getContentPane().setLayout(new javax.swing.BoxLayout(dlg_wrongPassword.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl_wrongPasswordBackground.setBackground(new java.awt.Color(44, 47, 51));
        pnl_wrongPasswordBackground.setForeground(new java.awt.Color(255, 255, 255));
        pnl_wrongPasswordBackground.setLayout(new java.awt.GridBagLayout());

        lbl_wrongPassword.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_wrongPassword.setForeground(new java.awt.Color(255, 255, 255));
        lbl_wrongPassword.setText("The password you entered is incorrect. Please try again.");
        pnl_wrongPasswordBackground.add(lbl_wrongPassword, new java.awt.GridBagConstraints());

        btn_wrongPasswordButton.setBackground(new java.awt.Color(132, 100, 200));
        btn_wrongPasswordButton.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_wrongPasswordButton.setForeground(new java.awt.Color(255, 255, 255));
        btn_wrongPasswordButton.setLabel("OK");
        btn_wrongPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_wrongPasswordButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pnl_wrongPasswordBackground.add(btn_wrongPasswordButton, gridBagConstraints);

        dlg_wrongPassword.getContentPane().add(pnl_wrongPasswordBackground);

        dlg_forgotPassword.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_forgotPassword.setTitle("Forgotten Password");
        dlg_forgotPassword.setAlwaysOnTop(true);
        dlg_forgotPassword.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        dlg_forgotPassword.setLocationByPlatform(true);
        dlg_forgotPassword.setMinimumSize(new java.awt.Dimension(750, 360));
        dlg_forgotPassword.setName("Forgot Password"); // NOI18N
        dlg_forgotPassword.setResizable(false);

        pnl_forgotPassBackground.setBackground(new java.awt.Color(44, 47, 51));

        lbl_forgotPassLabel1.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_forgotPassLabel1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_forgotPassLabel1.setText("Send code to your e-mail address registered in the database. Enter the code below within 5 minutes.");

        txtfld_forgotPassMail.setBackground(new java.awt.Color(44, 47, 51));
        txtfld_forgotPassMail.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtfld_forgotPassMail.setForeground(new java.awt.Color(255, 255, 255));
        txtfld_forgotPassMail.setBorder(null);
        txtfld_forgotPassMail.setPreferredSize(new java.awt.Dimension(53, 19));

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(132, 100, 200));
        jLabel3.setText("Mail");

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(132, 100, 200));
        jLabel4.setText("Kod");

        frmtd_forgotPassCode.setBackground(new java.awt.Color(44, 47, 51));
        frmtd_forgotPassCode.setBorder(null);
        frmtd_forgotPassCode.setForeground(new java.awt.Color(255, 255, 255));
        try {
            frmtd_forgotPassCode.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter(" #      #      #      #      #      #")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        frmtd_forgotPassCode.setFont(new java.awt.Font("Calibri", 0, 20)); // NOI18N
        frmtd_forgotPassCode.setMinimumSize(new java.awt.Dimension(20, 20));
        frmtd_forgotPassCode.setPreferredSize(new java.awt.Dimension(103, 30));
        frmtd_forgotPassCode.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                frmtd_forgotPassCodeCaretUpdate(evt);
            }
        });

        btn_forgotPassCodeButton.setBackground(new java.awt.Color(132, 100, 200));
        btn_forgotPassCodeButton.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_forgotPassCodeButton.setForeground(new java.awt.Color(255, 255, 255));
        btn_forgotPassCodeButton.setLabel("Kod Gonder");
        btn_forgotPassCodeButton.setName(""); // NOI18N
        btn_forgotPassCodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_forgotPassCodeButtonActionPerformed(evt);
            }
        });

        lbl_timer.setFont(new java.awt.Font("Calibri", 1, 35)); // NOI18N
        lbl_timer.setForeground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout pnl_forgotPassBackgroundLayout = new javax.swing.GroupLayout(pnl_forgotPassBackground);
        pnl_forgotPassBackground.setLayout(pnl_forgotPassBackgroundLayout);
        pnl_forgotPassBackgroundLayout.setHorizontalGroup(
            pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_forgotPassBackgroundLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_forgotPassLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                    .addGroup(pnl_forgotPassBackgroundLayout.createSequentialGroup()
                        .addGroup(pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtfld_forgotPassMail, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frmtd_forgotPassCode, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnl_forgotPassBackgroundLayout.createSequentialGroup()
                                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(44, 44, 44)
                        .addGroup(pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_forgotPassCodeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnl_forgotPassBackgroundLayout.createSequentialGroup()
                                .addComponent(lbl_timer, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        pnl_forgotPassBackgroundLayout.setVerticalGroup(
            pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_forgotPassBackgroundLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lbl_forgotPassLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(10, 10, 10)
                .addGroup(pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_forgotPassBackgroundLayout.createSequentialGroup()
                        .addComponent(txtfld_forgotPassMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_forgotPassCodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_forgotPassBackgroundLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(6, 6, 6)
                        .addComponent(frmtd_forgotPassCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(pnl_forgotPassBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lbl_timer, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(147, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlg_forgotPasswordLayout = new javax.swing.GroupLayout(dlg_forgotPassword.getContentPane());
        dlg_forgotPassword.getContentPane().setLayout(dlg_forgotPasswordLayout);
        dlg_forgotPasswordLayout.setHorizontalGroup(
            dlg_forgotPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_forgotPassBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlg_forgotPasswordLayout.setVerticalGroup(
            dlg_forgotPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_forgotPassBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dlg_passwordChanged.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_passwordChanged.setTitle("Password Change");
        dlg_passwordChanged.setAlwaysOnTop(true);
        dlg_passwordChanged.setLocationByPlatform(true);
        dlg_passwordChanged.setMinimumSize(new java.awt.Dimension(750, 220));
        dlg_passwordChanged.setResizable(false);

        pnl_passChangedBackground.setBackground(new java.awt.Color(44, 47, 51));

        lbl_passChangedLabel.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_passChangedLabel.setForeground(new java.awt.Color(255, 255, 255));
        lbl_passChangedLabel.setText("Your password has been successfully changed. You can log in to the application with your new password.");

        btn_passChangedButton.setBackground(new java.awt.Color(132, 100, 200));
        btn_passChangedButton.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_passChangedButton.setForeground(new java.awt.Color(255, 255, 255));
        btn_passChangedButton.setLabel("OK");
        btn_passChangedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_passChangedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_passChangedBackgroundLayout = new javax.swing.GroupLayout(pnl_passChangedBackground);
        pnl_passChangedBackground.setLayout(pnl_passChangedBackgroundLayout);
        pnl_passChangedBackgroundLayout.setHorizontalGroup(
            pnl_passChangedBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_passChangedBackgroundLayout.createSequentialGroup()
                .addGroup(pnl_passChangedBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_passChangedBackgroundLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_passChangedLabel))
                    .addGroup(pnl_passChangedBackgroundLayout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(btn_passChangedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_passChangedBackgroundLayout.setVerticalGroup(
            pnl_passChangedBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_passChangedBackgroundLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lbl_passChangedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_passChangedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlg_passwordChangedLayout = new javax.swing.GroupLayout(dlg_passwordChanged.getContentPane());
        dlg_passwordChanged.getContentPane().setLayout(dlg_passwordChangedLayout);
        dlg_passwordChangedLayout.setHorizontalGroup(
            dlg_passwordChangedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_passChangedBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlg_passwordChangedLayout.setVerticalGroup(
            dlg_passwordChangedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_passChangedBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jfrm_userFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jfrm_userFrame.setTitle("Address Manager User Frame");
        jfrm_userFrame.setAlwaysOnTop(true);
        jfrm_userFrame.setLocationByPlatform(true);
        jfrm_userFrame.setMinimumSize(new java.awt.Dimension(987, 585));
        jfrm_userFrame.getContentPane().setLayout(new javax.swing.BoxLayout(jfrm_userFrame.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        spltpn_userFrameSplitPane.setBackground(new java.awt.Color(132, 100, 200));
        spltpn_userFrameSplitPane.setDividerLocation(300);
        spltpn_userFrameSplitPane.setDividerSize(3);

        pnl_userFrameLeftPanel.setBackground(new java.awt.Color(44, 47, 51));
        pnl_userFrameLeftPanel.setPreferredSize(new java.awt.Dimension(300, 454));

        cmbbox_userCountries.setBackground(new java.awt.Color(132, 100, 200));
        cmbbox_userCountries.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        cmbbox_userCountries.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- CHOOSE A COUNTRY -", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Antigua And Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas, The", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia And Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burma", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo (Brazzaville)", "Congo (Kinshasa)", "Cook Islands", "Costa Rica", "Croatia", "Cuba", "CuraÃ§ao", "Cyprus", "Czechia", "CÃ´te Dâ€™Ivoire", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Islas Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "Gabon", "Gambia, The", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle Of Man", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States Of", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Namibia", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Helena, Ascension, And Tristan Da Cunha", "Saint Kitts And Nevis", "Saint Lucia", "Saint Vincent And The Grenadines", "Samoa", "San Marino", "Sao Tome And Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia And South Sandwich Islands", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad And Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks And Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis And Futuna", "West Bank", "Yemen", "Zambia", "Zimbabwe" }));
        cmbbox_userCountries.setBorder(null);
        cmbbox_userCountries.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbbox_userCountriesİtemStateChanged(evt);
            }
        });

        cmbbox_userCities.setBackground(new java.awt.Color(132, 100, 200));
        cmbbox_userCities.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        cmbbox_userCities.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- CHOOSE A CITY -" }));
        cmbbox_userCities.setEnabled(false);
        cmbbox_userCities.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbbox_userCitiesİtemStateChanged(evt);
            }
        });

        txtarea_userFullAddress.setBackground(new java.awt.Color(44, 47, 51));
        txtarea_userFullAddress.setColumns(20);
        txtarea_userFullAddress.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtarea_userFullAddress.setForeground(new java.awt.Color(255, 255, 255));
        txtarea_userFullAddress.setLineWrap(true);
        txtarea_userFullAddress.setRows(5);
        txtarea_userFullAddress.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Full Address", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jScrollPane1.setViewportView(txtarea_userFullAddress);

        lbl_userAddressInformation.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_userAddressInformation.setForeground(new java.awt.Color(132, 100, 200));
        lbl_userAddressInformation.setText("Address Information");

        btn_userLogOut.setBackground(new java.awt.Color(132, 100, 200));
        btn_userLogOut.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_userLogOut.setForeground(new java.awt.Color(255, 255, 255));
        btn_userLogOut.setLabel("Log Out");
        btn_userLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userLogOutActionPerformed(evt);
            }
        });

        btn_userSaveToDatabase.setBackground(new java.awt.Color(132, 100, 200));
        btn_userSaveToDatabase.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_userSaveToDatabase.setForeground(new java.awt.Color(255, 255, 255));
        btn_userSaveToDatabase.setLabel("Save to Database");
        btn_userSaveToDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userSaveToDatabaseActionPerformed(evt);
            }
        });

        btn_userUpdateToDatabase.setBackground(new java.awt.Color(132, 100, 200));
        btn_userUpdateToDatabase.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_userUpdateToDatabase.setForeground(new java.awt.Color(255, 255, 255));
        btn_userUpdateToDatabase.setLabel("Update the Database");
        btn_userUpdateToDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userUpdateToDatabaseActionPerformed(evt);
            }
        });

        sprtr_userFrameSeperator.setForeground(new java.awt.Color(255, 255, 255));

        btn_userFrameUploadPic.setBackground(new java.awt.Color(132, 100, 200));
        btn_userFrameUploadPic.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_userFrameUploadPic.setForeground(new java.awt.Color(255, 255, 255));
        btn_userFrameUploadPic.setLabel("Upload Picture");
        btn_userFrameUploadPic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userFrameUploadPicActionPerformed(evt);
            }
        });

        btn_userFrameAdjustPicture.setBackground(new java.awt.Color(132, 100, 200));
        btn_userFrameAdjustPicture.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_userFrameAdjustPicture.setForeground(new java.awt.Color(255, 255, 255));
        btn_userFrameAdjustPicture.setLabel("Adjust Picture");
        btn_userFrameAdjustPicture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userFrameAdjustPictureActionPerformed(evt);
            }
        });

        btn_userFrameChangePassword.setBackground(new java.awt.Color(132, 100, 200));
        btn_userFrameChangePassword.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_userFrameChangePassword.setForeground(new java.awt.Color(255, 255, 255));
        btn_userFrameChangePassword.setLabel("Change Password");
        btn_userFrameChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userFrameChangePasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_userFrameLeftPanelLayout = new javax.swing.GroupLayout(pnl_userFrameLeftPanel);
        pnl_userFrameLeftPanel.setLayout(pnl_userFrameLeftPanelLayout);
        pnl_userFrameLeftPanelLayout.setHorizontalGroup(
            pnl_userFrameLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_userFrameLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_userFrameLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(cmbbox_userCountries, 0, 0, Short.MAX_VALUE)
                    .addComponent(cmbbox_userCities, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_userLogOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_userSaveToDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_userUpdateToDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sprtr_userFrameSeperator)
                    .addGroup(pnl_userFrameLeftPanelLayout.createSequentialGroup()
                        .addComponent(lbl_userAddressInformation)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnl_userFrameLeftPanelLayout.createSequentialGroup()
                        .addComponent(lbl_userFrameProfilePic, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_userFrameLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_userFrameUploadPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_userFrameAdjustPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_userFrameChangePassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnl_userFrameLeftPanelLayout.setVerticalGroup(
            pnl_userFrameLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_userFrameLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_userAddressInformation)
                .addGap(18, 18, 18)
                .addComponent(cmbbox_userCountries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbbox_userCities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_userFrameLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_userFrameLeftPanelLayout.createSequentialGroup()
                        .addComponent(lbl_userFrameProfilePic, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(btn_userUpdateToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_userSaveToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sprtr_userFrameSeperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_userLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_userFrameLeftPanelLayout.createSequentialGroup()
                        .addComponent(btn_userFrameUploadPic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_userFrameAdjustPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_userFrameChangePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        spltpn_userFrameSplitPane.setLeftComponent(pnl_userFrameLeftPanel);

        pnl_userFrameRightPanel.setBackground(new java.awt.Color(44, 47, 51));

        scrlpn_map.setBorder(null);
        scrlpn_map.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrlpn_map.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrlpn_map.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                scrlpn_mapMouseDragged(evt);
            }
        });
        scrlpn_map.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrlpn_mapMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                MousePressed(evt);
            }
        });
        scrlpn_map.setViewportView(lbl_guideMap);

        lbl_mapLabel.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_mapLabel.setForeground(new java.awt.Color(132, 100, 200));
        lbl_mapLabel.setText("Map");

        prgrsbr_userFrameMapLoading.setBackground(new java.awt.Color(44, 47, 51));
        prgrsbr_userFrameMapLoading.setForeground(new java.awt.Color(132, 100, 200));

        javax.swing.GroupLayout pnl_userFrameRightPanelLayout = new javax.swing.GroupLayout(pnl_userFrameRightPanel);
        pnl_userFrameRightPanel.setLayout(pnl_userFrameRightPanelLayout);
        pnl_userFrameRightPanelLayout.setHorizontalGroup(
            pnl_userFrameRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_userFrameRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_userFrameRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrlpn_map, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                    .addComponent(prgrsbr_userFrameMapLoading, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_userFrameRightPanelLayout.createSequentialGroup()
                        .addComponent(lbl_mapLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnl_userFrameRightPanelLayout.setVerticalGroup(
            pnl_userFrameRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_userFrameRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_mapLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrlpn_map, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prgrsbr_userFrameMapLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        spltpn_userFrameSplitPane.setRightComponent(pnl_userFrameRightPanel);

        jfrm_userFrame.getContentPane().add(spltpn_userFrameSplitPane);

        mnbr_userFrameMenuBar.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N

        mn_userFrameApplication.setText("Application");

        mnTm_clearFullAddress.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnTm_clearFullAddress.setText("Clear Full Address");
        mnTm_clearFullAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTm_clearFullAddressActionPerformed(evt);
            }
        });
        mn_userFrameApplication.add(mnTm_clearFullAddress);
        mn_userFrameApplication.add(jSeparator3);

        mnTm_uploadPic.setText("Upload a Picture");
        mnTm_uploadPic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userFrameUploadPicActionPerformed(evt);
            }
        });
        mn_userFrameApplication.add(mnTm_uploadPic);

        mnTm_adjustPic.setText("Adjust Picture");
        mnTm_adjustPic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userFrameAdjustPictureActionPerformed(evt);
            }
        });
        mn_userFrameApplication.add(mnTm_adjustPic);

        mnTm_changePass.setText("Change Password");
        mnTm_changePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userFrameChangePasswordActionPerformed(evt);
            }
        });
        mn_userFrameApplication.add(mnTm_changePass);
        mn_userFrameApplication.add(jSeparator4);

        mnTm_userFrameLogOut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnTm_userFrameLogOut.setText("Log Out");
        mnTm_userFrameLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_userLogOutActionPerformed(evt);
            }
        });
        mn_userFrameApplication.add(mnTm_userFrameLogOut);

        mnbr_userFrameMenuBar.add(mn_userFrameApplication);

        mn_userFrameMap.setText("Map");

        mnTm_dontDisplayMap.setText("Do Not Display Map");
        mn_userFrameMap.add(mnTm_dontDisplayMap);

        mnTm_displayWorldMap.setText("Display World Map");
        mnTm_displayWorldMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTm_displayWorldMapActionPerformed(evt);
            }
        });
        mn_userFrameMap.add(mnTm_displayWorldMap);

        mnbr_userFrameMenuBar.add(mn_userFrameMap);

        jfrm_userFrame.setJMenuBar(mnbr_userFrameMenuBar);

        jfrm_adminFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jfrm_adminFrame.setTitle("Address Manager User Frame");
        jfrm_adminFrame.setLocationByPlatform(true);
        jfrm_adminFrame.setMinimumSize(new java.awt.Dimension(954, 610));
        jfrm_adminFrame.getContentPane().setLayout(new javax.swing.BoxLayout(jfrm_adminFrame.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        spltpn_adminFrame.setBackground(new java.awt.Color(132, 100, 200));
        spltpn_adminFrame.setDividerLocation(380);
        spltpn_adminFrame.setDividerSize(3);
        spltpn_adminFrame.setPreferredSize(new java.awt.Dimension(954, 550));

        pnl_userFrameLeftPanel1.setBackground(new java.awt.Color(44, 47, 51));
        pnl_userFrameLeftPanel1.setPreferredSize(new java.awt.Dimension(300, 454));

        lbl_adminPersonelInformations.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_adminPersonelInformations.setForeground(new java.awt.Color(132, 100, 200));
        lbl_adminPersonelInformations.setText("Personel Informations");

        btn_adminFrameLogOut.setBackground(new java.awt.Color(132, 100, 200));
        btn_adminFrameLogOut.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_adminFrameLogOut.setForeground(new java.awt.Color(255, 255, 255));
        btn_adminFrameLogOut.setLabel("Log Out");
        btn_adminFrameLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adminFrameLogOutActionPerformed(evt);
            }
        });

        btn_adminFrameDeleteUsersAddress.setActionCommand("Delete Users Address Informations");
        btn_adminFrameDeleteUsersAddress.setBackground(new java.awt.Color(132, 100, 200));
        btn_adminFrameDeleteUsersAddress.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_adminFrameDeleteUsersAddress.setForeground(new java.awt.Color(255, 255, 255));
        btn_adminFrameDeleteUsersAddress.setLabel("Delete Users Address Informations");
        btn_adminFrameDeleteUsersAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adminFrameDeleteUsersAddressActionPerformed(evt);
            }
        });

        btn_updateTheTable.setBackground(new java.awt.Color(132, 100, 200));
        btn_updateTheTable.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_updateTheTable.setForeground(new java.awt.Color(255, 255, 255));
        btn_updateTheTable.setLabel("Update the Table");
        btn_updateTheTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateTheTableActionPerformed(evt);
            }
        });

        sprtr_userFrameSeperator1.setForeground(new java.awt.Color(255, 255, 255));

        lbl_adminFrameNameSurname.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_adminFrameNameSurname.setForeground(new java.awt.Color(255, 255, 255));
        lbl_adminFrameNameSurname.setText("Person Name / Surname");

        lbl_adminFrameMail.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_adminFrameMail.setForeground(new java.awt.Color(255, 255, 255));
        lbl_adminFrameMail.setText("Person Mail Address");

        lbl_adminFrameDate.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_adminFrameDate.setForeground(new java.awt.Color(255, 255, 255));
        lbl_adminFrameDate.setText("Person Birth Date");

        lbl_adminFrameGender.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_adminFrameGender.setForeground(new java.awt.Color(255, 255, 255));
        lbl_adminFrameGender.setText("Gender");

        lbl_adminFrameID.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_adminFrameID.setForeground(new java.awt.Color(255, 255, 255));
        lbl_adminFrameID.setText("ID");

        jScrollPane2.setBackground(new java.awt.Color(255, 0, 153));

        lst_adminFramePersonList.setBackground(new java.awt.Color(44, 47, 51));
        lst_adminFramePersonList.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lst_adminFramePersonList.setForeground(new java.awt.Color(255, 255, 255));
        lst_adminFramePersonList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lst_adminFramePersonList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lst_adminFramePersonListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lst_adminFramePersonList);

        btn_applyChangesToDatabase.setBackground(new java.awt.Color(132, 100, 200));
        btn_applyChangesToDatabase.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_applyChangesToDatabase.setForeground(new java.awt.Color(255, 255, 255));
        btn_applyChangesToDatabase.setLabel("Apply the Changes to Database");
        btn_applyChangesToDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_applyChangesToDatabaseActionPerformed(evt);
            }
        });

        btn_adminFrameDeleteUser.setBackground(new java.awt.Color(132, 100, 200));
        btn_adminFrameDeleteUser.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_adminFrameDeleteUser.setForeground(new java.awt.Color(255, 255, 255));
        btn_adminFrameDeleteUser.setLabel("Delete the User");
        btn_adminFrameDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adminFrameDeleteUserActionPerformed(evt);
            }
        });

        jSeparator15.setForeground(new java.awt.Color(255, 255, 255));

        txtfld_adminFramePersonSearchBox.setBackground(new java.awt.Color(44, 47, 51));
        txtfld_adminFramePersonSearchBox.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        txtfld_adminFramePersonSearchBox.setForeground(new java.awt.Color(102, 102, 102));
        txtfld_adminFramePersonSearchBox.setText("Search Here!");
        txtfld_adminFramePersonSearchBox.setBorder(null);
        txtfld_adminFramePersonSearchBox.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfld_adminFramePersonSearchBoxCaretUpdate(evt);
            }
        });
        txtfld_adminFramePersonSearchBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtfld_adminFramePersonSearchBoxFocusLost(evt);
            }
        });
        txtfld_adminFramePersonSearchBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtfld_adminFramePersonSearchBoxMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnl_userFrameLeftPanel1Layout = new javax.swing.GroupLayout(pnl_userFrameLeftPanel1);
        pnl_userFrameLeftPanel1.setLayout(pnl_userFrameLeftPanel1Layout);
        pnl_userFrameLeftPanel1Layout.setHorizontalGroup(
            pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_userFrameLeftPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sprtr_userFrameSeperator1)
                    .addComponent(btn_updateTheTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_adminFrameDeleteUsersAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(btn_adminFrameLogOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_applyChangesToDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_adminFrameDeleteUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator15)
                    .addGroup(pnl_userFrameLeftPanel1Layout.createSequentialGroup()
                        .addGroup(pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_adminPersonelInformations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_adminFrameNameSurname)
                            .addComponent(lbl_adminFrameMail)
                            .addComponent(lbl_adminFrameDate)
                            .addComponent(lbl_adminFrameGender)
                            .addComponent(lbl_adminFrameID)
                            .addComponent(lbl_adminFrameProfilePic, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnl_userFrameLeftPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_adminFramePersonSearchIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator16)
                            .addComponent(txtfld_adminFramePersonSearchBox))))
                .addContainerGap())
        );
        pnl_userFrameLeftPanel1Layout.setVerticalGroup(
            pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_userFrameLeftPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_adminPersonelInformations)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_userFrameLeftPanel1Layout.createSequentialGroup()
                        .addComponent(txtfld_adminFramePersonSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_adminFramePersonSearchIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_userFrameLeftPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_userFrameLeftPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_adminFrameProfilePic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_adminFrameID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_adminFrameNameSurname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_adminFrameMail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_adminFrameDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_adminFrameGender)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_updateTheTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_applyChangesToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_adminFrameDeleteUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_adminFrameDeleteUsersAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sprtr_userFrameSeperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_adminFrameLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        spltpn_adminFrame.setLeftComponent(pnl_userFrameLeftPanel1);

        pnl_userFrameRightPanel1.setBackground(new java.awt.Color(44, 47, 51));

        lbl_adminAddressInformations.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_adminAddressInformations.setForeground(new java.awt.Color(132, 100, 200));
        lbl_adminAddressInformations.setText("Address Informations");

        tbl_adminFrameTable.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        tbl_adminFrameTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "COUNTRY", "CITY", "FULLADDRESS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbl_adminFrameTable);

        txtfld_adminFrameAddressSearchBox.setBackground(new java.awt.Color(41, 47, 51));
        txtfld_adminFrameAddressSearchBox.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        txtfld_adminFrameAddressSearchBox.setForeground(new java.awt.Color(102, 102, 102));
        txtfld_adminFrameAddressSearchBox.setText("Search Here!");
        txtfld_adminFrameAddressSearchBox.setBorder(null);
        txtfld_adminFrameAddressSearchBox.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfld_adminFrameAddressSearchBoxCaretUpdate(evt);
            }
        });
        txtfld_adminFrameAddressSearchBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtfld_adminFrameAddressSearchBoxFocusLost(evt);
            }
        });
        txtfld_adminFrameAddressSearchBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtfld_adminFrameAddressSearchBoxMouseClicked(evt);
            }
        });

        lbl_adminFrameAddressSearchIcon.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_adminFrameAddressSearchIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txtpn_adminFrameLog.setBackground(new java.awt.Color(44, 47, 51));
        txtpn_adminFrameLog.setBorder(null);
        jScrollPane5.setViewportView(txtpn_adminFrameLog);

        javax.swing.GroupLayout pnl_userFrameRightPanel1Layout = new javax.swing.GroupLayout(pnl_userFrameRightPanel1);
        pnl_userFrameRightPanel1.setLayout(pnl_userFrameRightPanel1Layout);
        pnl_userFrameRightPanel1Layout.setHorizontalGroup(
            pnl_userFrameRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_userFrameRightPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_userFrameRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                    .addGroup(pnl_userFrameRightPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_adminAddressInformations)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_adminFrameAddressSearchIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_userFrameRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtfld_adminFrameAddressSearchBox, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                            .addComponent(jSeparator14))))
                .addContainerGap())
        );
        pnl_userFrameRightPanel1Layout.setVerticalGroup(
            pnl_userFrameRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_userFrameRightPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_userFrameRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_userFrameRightPanel1Layout.createSequentialGroup()
                        .addGroup(pnl_userFrameRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_adminAddressInformations)
                            .addComponent(txtfld_adminFrameAddressSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_adminFrameAddressSearchIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        spltpn_adminFrame.setRightComponent(pnl_userFrameRightPanel1);

        jfrm_adminFrame.getContentPane().add(spltpn_adminFrame);

        mn_adminFrameApplication.setText("Application");

        mnTm_updateTable.setText("Update the Table");
        mnTm_updateTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateTheTableActionPerformed(evt);
            }
        });
        mn_adminFrameApplication.add(mnTm_updateTable);

        mnTm_applyChangesDatabase.setText("Apply Changes to Database");
        mnTm_applyChangesDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_applyChangesToDatabaseActionPerformed(evt);
            }
        });
        mn_adminFrameApplication.add(mnTm_applyChangesDatabase);
        mn_adminFrameApplication.add(jSeparator17);

        mnTm_deleteUser.setText("Delete the User");
        mnTm_deleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adminFrameDeleteUserActionPerformed(evt);
            }
        });
        mn_adminFrameApplication.add(mnTm_deleteUser);

        mnTm_deleteUsersAddressInformations.setText("Delete Users Address Informations");
        mnTm_deleteUsersAddressInformations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adminFrameDeleteUsersAddressActionPerformed(evt);
            }
        });
        mn_adminFrameApplication.add(mnTm_deleteUsersAddressInformations);
        mn_adminFrameApplication.add(jSeparator18);

        mnTm_adminFrameLogOut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnTm_adminFrameLogOut.setText("Log Out");
        mnTm_adminFrameLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adminFrameLogOutActionPerformed(evt);
            }
        });
        mn_adminFrameApplication.add(mnTm_adminFrameLogOut);

        mnbr_adminFrameMenuBar.add(mn_adminFrameApplication);

        mn_adminFrameFile.setText("File");

        mnTm_clearLogArea.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnTm_clearLogArea.setText("Clear Log Area");
        mnTm_clearLogArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTm_clearLogAreaActionPerformed(evt);
            }
        });
        mn_adminFrameFile.add(mnTm_clearLogArea);
        mn_adminFrameFile.add(jSeparator19);

        mnTm_backupToComputer.setText("Backup to Computer");
        mnTm_backupToComputer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTm_backupToComputerActionPerformed(evt);
            }
        });
        mn_adminFrameFile.add(mnTm_backupToComputer);

        mnbr_adminFrameMenuBar.add(mn_adminFrameFile);

        jfrm_adminFrame.setJMenuBar(mnbr_adminFrameMenuBar);

        jfrm_profilePicAdjustment.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jfrm_profilePicAdjustment.setTitle("Profile Picture Adjuster");
        jfrm_profilePicAdjustment.setMinimumSize(new java.awt.Dimension(890, 530));
        jfrm_profilePicAdjustment.setResizable(false);
        jfrm_profilePicAdjustment.getContentPane().setLayout(new javax.swing.BoxLayout(jfrm_profilePicAdjustment.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl_pictureFrameBackground.setBackground(new java.awt.Color(44, 47, 51));

        pnl_pictureFramePicPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnl_pictureFramePicPanelMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout pnl_pictureFramePicPanelLayout = new javax.swing.GroupLayout(pnl_pictureFramePicPanel);
        pnl_pictureFramePicPanel.setLayout(pnl_pictureFramePicPanelLayout);
        pnl_pictureFramePicPanelLayout.setHorizontalGroup(
            pnl_pictureFramePicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        pnl_pictureFramePicPanelLayout.setVerticalGroup(
            pnl_pictureFramePicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnl_pictureFrameColorsPanel.setBackground(new java.awt.Color(44, 47, 51));
        pnl_pictureFrameColorsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnl_color1.setBackground(new java.awt.Color(255, 0, 0));
        pnl_color1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_color1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_colorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnl_color1Layout = new javax.swing.GroupLayout(pnl_color1);
        pnl_color1.setLayout(pnl_color1Layout);
        pnl_color1Layout.setHorizontalGroup(
            pnl_color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        pnl_color1Layout.setVerticalGroup(
            pnl_color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        pnl_color2.setBackground(new java.awt.Color(255, 255, 0));
        pnl_color2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_color2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_colorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnl_color2Layout = new javax.swing.GroupLayout(pnl_color2);
        pnl_color2.setLayout(pnl_color2Layout);
        pnl_color2Layout.setHorizontalGroup(
            pnl_color2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        pnl_color2Layout.setVerticalGroup(
            pnl_color2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        pnl_color3.setBackground(java.awt.Color.green);
        pnl_color3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_color3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_colorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnl_color3Layout = new javax.swing.GroupLayout(pnl_color3);
        pnl_color3.setLayout(pnl_color3Layout);
        pnl_color3Layout.setHorizontalGroup(
            pnl_color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        pnl_color3Layout.setVerticalGroup(
            pnl_color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        pnl_color4.setBackground(java.awt.Color.magenta);
        pnl_color4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_color4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_colorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnl_color4Layout = new javax.swing.GroupLayout(pnl_color4);
        pnl_color4.setLayout(pnl_color4Layout);
        pnl_color4Layout.setHorizontalGroup(
            pnl_color4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        pnl_color4Layout.setVerticalGroup(
            pnl_color4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        pnl_color5.setBackground(java.awt.Color.cyan);
        pnl_color5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_color5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_colorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnl_color5Layout = new javax.swing.GroupLayout(pnl_color5);
        pnl_color5.setLayout(pnl_color5Layout);
        pnl_color5Layout.setHorizontalGroup(
            pnl_color5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        pnl_color5Layout.setVerticalGroup(
            pnl_color5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        btn_pictureFrameOpenColorChooser.setBackground(new java.awt.Color(132, 100, 200));
        btn_pictureFrameOpenColorChooser.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_pictureFrameOpenColorChooser.setForeground(new java.awt.Color(255, 255, 255));
        btn_pictureFrameOpenColorChooser.setLabel("Open Color Chooser");
        btn_pictureFrameOpenColorChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pictureFrameOpenColorChooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_pictureFrameColorsPanelLayout = new javax.swing.GroupLayout(pnl_pictureFrameColorsPanel);
        pnl_pictureFrameColorsPanel.setLayout(pnl_pictureFrameColorsPanelLayout);
        pnl_pictureFrameColorsPanelLayout.setHorizontalGroup(
            pnl_pictureFrameColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_pictureFrameColorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_pictureFrameColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pictureFrameOpenColorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_pictureFrameColorsPanelLayout.createSequentialGroup()
                        .addComponent(pnl_color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnl_color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnl_color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnl_color4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnl_color5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnl_pictureFrameColorsPanelLayout.setVerticalGroup(
            pnl_pictureFrameColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_pictureFrameColorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_pictureFrameColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_color2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_color5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_color4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_pictureFrameOpenColorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sldr_pictureFrameThicknessSlider.setBackground(new java.awt.Color(44, 47, 51));
        sldr_pictureFrameThicknessSlider.setForeground(new java.awt.Color(132, 100, 200));
        sldr_pictureFrameThicknessSlider.setMaximum(38);
        sldr_pictureFrameThicknessSlider.setMinimum(2);
        sldr_pictureFrameThicknessSlider.setValue(19);
        sldr_pictureFrameThicknessSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldr_pictureFrameThicknessSliderStateChanged(evt);
            }
        });

        btn_pictureFrameBackToUserFrame.setBackground(new java.awt.Color(132, 100, 200));
        btn_pictureFrameBackToUserFrame.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_pictureFrameBackToUserFrame.setForeground(new java.awt.Color(255, 255, 255));
        btn_pictureFrameBackToUserFrame.setLabel("Go Back to User Frame");
        btn_pictureFrameBackToUserFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pictureFrameBackToUserFrameActionPerformed(evt);
            }
        });

        btn_pictureFrameSetPicToDatabase.setBackground(new java.awt.Color(132, 100, 200));
        btn_pictureFrameSetPicToDatabase.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_pictureFrameSetPicToDatabase.setForeground(new java.awt.Color(255, 255, 255));
        btn_pictureFrameSetPicToDatabase.setLabel("Set As a Profile Picture in Database");
        btn_pictureFrameSetPicToDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pictureFrameSetPicToDatabaseActionPerformed(evt);
            }
        });

        btn_pictureFrameGetPicFromDatabase.setBackground(new java.awt.Color(132, 100, 200));
        btn_pictureFrameGetPicFromDatabase.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_pictureFrameGetPicFromDatabase.setForeground(new java.awt.Color(255, 255, 255));
        btn_pictureFrameGetPicFromDatabase.setLabel("Get the Profile Picture in Database");
        btn_pictureFrameGetPicFromDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pictureFrameGetPicFromDatabaseActionPerformed(evt);
            }
        });

        btn_pictureFrameOpenNewImage.setBackground(new java.awt.Color(132, 100, 200));
        btn_pictureFrameOpenNewImage.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_pictureFrameOpenNewImage.setForeground(new java.awt.Color(255, 255, 255));
        btn_pictureFrameOpenNewImage.setLabel("Open New Image");
        btn_pictureFrameOpenNewImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pictureFrameOpenNewImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_pictureFrameBackgroundLayout = new javax.swing.GroupLayout(pnl_pictureFrameBackground);
        pnl_pictureFrameBackground.setLayout(pnl_pictureFrameBackgroundLayout);
        pnl_pictureFrameBackgroundLayout.setHorizontalGroup(
            pnl_pictureFrameBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_pictureFrameBackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_pictureFramePicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_pictureFrameBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_pictureFrameSetPicToDatabase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_pictureFrameGetPicFromDatabase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_pictureFrameOpenNewImage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_pictureFrameCurvedLine, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldr_pictureFrameThicknessSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_pictureFrameColorsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_pictureFrameBackToUserFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_pictureFrameBackgroundLayout.setVerticalGroup(
            pnl_pictureFrameBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_pictureFrameBackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_pictureFrameBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_pictureFrameBackgroundLayout.createSequentialGroup()
                        .addComponent(pnl_pictureFrameColorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sldr_pictureFrameThicknessSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_pictureFrameCurvedLine, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(btn_pictureFrameOpenNewImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_pictureFrameGetPicFromDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_pictureFrameSetPicToDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_pictureFrameBackToUserFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnl_pictureFramePicPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jfrm_profilePicAdjustment.getContentPane().add(pnl_pictureFrameBackground);

        pnl_pictureFrameToolsPanel.setBackground(new java.awt.Color(44, 47, 51));

        javax.swing.GroupLayout pnl_pictureFrameToolsPanelLayout = new javax.swing.GroupLayout(pnl_pictureFrameToolsPanel);
        pnl_pictureFrameToolsPanel.setLayout(pnl_pictureFrameToolsPanelLayout);
        pnl_pictureFrameToolsPanelLayout.setHorizontalGroup(
            pnl_pictureFrameToolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        pnl_pictureFrameToolsPanelLayout.setVerticalGroup(
            pnl_pictureFrameToolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 527, Short.MAX_VALUE)
        );

        jfrm_profilePicAdjustment.getContentPane().add(pnl_pictureFrameToolsPanel);

        dlg_changePassword.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_changePassword.setTitle("Change Password");
        dlg_changePassword.setAlwaysOnTop(true);
        dlg_changePassword.setMinimumSize(new java.awt.Dimension(520, 270));
        dlg_changePassword.getContentPane().setLayout(new javax.swing.BoxLayout(dlg_changePassword.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(44, 47, 51));

        lbl_enterOldPassword.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_enterOldPassword.setForeground(new java.awt.Color(255, 255, 255));
        lbl_enterOldPassword.setText("Please enter your old password.");

        pswrdfld_oldPassword.setBackground(new java.awt.Color(44, 47, 51));
        pswrdfld_oldPassword.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        pswrdfld_oldPassword.setForeground(new java.awt.Color(255, 255, 255));
        pswrdfld_oldPassword.setBorder(null);
        pswrdfld_oldPassword.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                pswrdfld_oldPasswordCaretUpdate(evt);
            }
        });

        lbl_enterNewPassword.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        lbl_enterNewPassword.setForeground(new java.awt.Color(255, 255, 255));
        lbl_enterNewPassword.setText("Now enter your new password.");

        lbl_errorChangePassword.setForeground(new java.awt.Color(237, 67, 55));
        lbl_errorChangePassword.setText(" ");

        pswrdfld_newPassword.setBackground(new java.awt.Color(44, 47, 51));
        pswrdfld_newPassword.setForeground(new java.awt.Color(255, 255, 255));
        pswrdfld_newPassword.setBorder(null);

        btn_changeThePassword.setBackground(new java.awt.Color(132, 100, 200));
        btn_changeThePassword.setEnabled(false);
        btn_changeThePassword.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_changeThePassword.setForeground(new java.awt.Color(255, 255, 255));
        btn_changeThePassword.setLabel("Change Password");
        btn_changeThePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_changeThePasswordActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("This window allow you to change your password. Please read the following.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_changePasswordPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pswrdfld_newPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_enterNewPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_enterOldPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .addComponent(pswrdfld_oldPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2)
                            .addComponent(btn_changeThePassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_errorChangePassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_enterOldPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pswrdfld_oldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_errorChangePassword)
                        .addGap(23, 23, 23)
                        .addComponent(lbl_enterNewPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pswrdfld_newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_changeThePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lbl_changePasswordPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        dlg_changePassword.getContentPane().add(jPanel1);

        dlg_backupToComputer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_backupToComputer.setTitle("Backup to Computer");
        dlg_backupToComputer.setMinimumSize(new java.awt.Dimension(450, 200));
        dlg_backupToComputer.setResizable(false);

        pnl_backupComputerBackground.setBackground(new java.awt.Color(44, 47, 51));

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Please select the file you want to download the backups to.");

        btn_backupToComputer.setBackground(new java.awt.Color(132, 100, 200));
        btn_backupToComputer.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_backupToComputer.setForeground(new java.awt.Color(255, 255, 255));
        btn_backupToComputer.setLabel("Choose File");
        btn_backupToComputer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backupToComputerActionPerformed(evt);
            }
        });

        prgrsbr_backupProgress.setForeground(new java.awt.Color(132, 100, 200));

        lbl_downloadInformator.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_downloadInformator.setForeground(new java.awt.Color(255, 255, 255));
        lbl_downloadInformator.setText("Looking for Backup Folder...");

        javax.swing.GroupLayout pnl_backupComputerBackgroundLayout = new javax.swing.GroupLayout(pnl_backupComputerBackground);
        pnl_backupComputerBackground.setLayout(pnl_backupComputerBackgroundLayout);
        pnl_backupComputerBackgroundLayout.setHorizontalGroup(
            pnl_backupComputerBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_backupComputerBackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_backupComputerBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(prgrsbr_backupProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_backupComputerBackgroundLayout.createSequentialGroup()
                        .addComponent(lbl_backup, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnl_backupComputerBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_backupComputerBackgroundLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1))
                            .addGroup(pnl_backupComputerBackgroundLayout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(btn_backupToComputer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lbl_downloadInformator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_backupComputerBackgroundLayout.setVerticalGroup(
            pnl_backupComputerBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_backupComputerBackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_backupComputerBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_backupComputerBackgroundLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_backupToComputer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_backup, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_downloadInformator)
                .addGap(18, 18, 18)
                .addComponent(prgrsbr_backupProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout dlg_backupToComputerLayout = new javax.swing.GroupLayout(dlg_backupToComputer.getContentPane());
        dlg_backupToComputer.getContentPane().setLayout(dlg_backupToComputerLayout);
        dlg_backupToComputerLayout.setHorizontalGroup(
            dlg_backupToComputerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_backupComputerBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlg_backupToComputerLayout.setVerticalGroup(
            dlg_backupToComputerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_backupComputerBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mnTm_changeColorWorldMap.setText("Change the Color");
        mnTm_changeColorWorldMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTm_changeColorWorldMapActionPerformed(evt);
            }
        });
        pppmn_userFrameMapPopUp.add(mnTm_changeColorWorldMap);

        mnTm_displayWordlMapPopUp.setText("Display World Map");
        mnTm_displayWordlMapPopUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTm_displayWorldMapActionPerformed(evt);
            }
        });
        pppmn_userFrameMapPopUp.add(mnTm_displayWordlMapPopUp);

        dlg_savedToDatabase.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_savedToDatabase.setTitle("Saved to Database");
        dlg_savedToDatabase.setAlwaysOnTop(true);
        dlg_savedToDatabase.setMinimumSize(new java.awt.Dimension(415, 170));
        dlg_savedToDatabase.setResizable(false);

        jPanel2.setBackground(new java.awt.Color(44, 47, 51));

        lbl_savedToDatabase.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_savedToDatabase.setForeground(new java.awt.Color(255, 255, 255));
        lbl_savedToDatabase.setText("Your address informations has been saved to database.");

        btn_savedToDatabaseOK.setBackground(new java.awt.Color(132, 100, 200));
        btn_savedToDatabaseOK.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_savedToDatabaseOK.setForeground(new java.awt.Color(255, 255, 255));
        btn_savedToDatabaseOK.setLabel("OK");
        btn_savedToDatabaseOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_savedToDatabaseOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_savedToDatabase))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(btn_savedToDatabaseOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_savedToDatabase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_savedToDatabaseOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlg_savedToDatabaseLayout = new javax.swing.GroupLayout(dlg_savedToDatabase.getContentPane());
        dlg_savedToDatabase.getContentPane().setLayout(dlg_savedToDatabaseLayout);
        dlg_savedToDatabaseLayout.setHorizontalGroup(
            dlg_savedToDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlg_savedToDatabaseLayout.setVerticalGroup(
            dlg_savedToDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dlg_updatedToDatabase.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg_updatedToDatabase.setTitle("Saved to Database");
        dlg_updatedToDatabase.setAlwaysOnTop(true);
        dlg_updatedToDatabase.setMinimumSize(new java.awt.Dimension(425, 170));
        dlg_updatedToDatabase.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(44, 47, 51));

        lbl_updatedToDatabase.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_updatedToDatabase.setForeground(new java.awt.Color(255, 255, 255));
        lbl_updatedToDatabase.setText("Your address informations has been updated to database.");

        btn_updatedToDatabaseOK.setBackground(new java.awt.Color(132, 100, 200));
        btn_updatedToDatabaseOK.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_updatedToDatabaseOK.setForeground(new java.awt.Color(255, 255, 255));
        btn_updatedToDatabaseOK.setLabel("OK");
        btn_updatedToDatabaseOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updatedToDatabaseOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_updatedToDatabase))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(btn_updatedToDatabaseOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_updatedToDatabase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_updatedToDatabaseOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dlg_updatedToDatabaseLayout = new javax.swing.GroupLayout(dlg_updatedToDatabase.getContentPane());
        dlg_updatedToDatabase.getContentPane().setLayout(dlg_updatedToDatabaseLayout);
        dlg_updatedToDatabaseLayout.setHorizontalGroup(
            dlg_updatedToDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlg_updatedToDatabaseLayout.setVerticalGroup(
            dlg_updatedToDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Address Manager");
        setBackground(new java.awt.Color(44, 47, 51));
        setFont(new java.awt.Font("Calibri", 0, 10)); // NOI18N
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(945, 585));
        setMinimumSize(new java.awt.Dimension(945, 585));
        setPreferredSize(new java.awt.Dimension(945, 585));
        setResizable(false);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnl_loginFrameBackPanel.setLayout(new javax.swing.BoxLayout(pnl_loginFrameBackPanel, javax.swing.BoxLayout.LINE_AXIS));

        scrlpn_leftPanel.setBorder(null);
        scrlpn_leftPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrlpn_leftPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrlpn_leftPanel.setMinimumSize(new java.awt.Dimension(460, 23));
        scrlpn_leftPanel.setPreferredSize(new java.awt.Dimension(460, 100));
        scrlpn_leftPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                LoginFrame.this.mouseMoved(evt);
            }
        });
        scrlpn_leftPanel.setViewportView(lbl_map);

        pnl_loginFrameBackPanel.add(scrlpn_leftPanel);

        pnl_rightPanel.setBackground(new java.awt.Color(35, 39, 42));
        pnl_rightPanel.setPreferredSize(new java.awt.Dimension(432, 655));
        pnl_rightPanel.setLayout(new javax.swing.BoxLayout(pnl_rightPanel, javax.swing.BoxLayout.LINE_AXIS));

        tbdpn_tabbedPane.setBackground(new java.awt.Color(44, 47, 51));
        tbdpn_tabbedPane.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        pnl_login.setBackground(new java.awt.Color(44, 47, 51));
        pnl_login.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                LoginFrame.this.mouseMoved(evt);
            }
        });

        lbl_loginMail.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_loginMail.setForeground(new java.awt.Color(132, 100, 200));
        lbl_loginMail.setText("Mail");

        txtfld_loginMail.setBackground(new java.awt.Color(44, 47, 51));
        txtfld_loginMail.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtfld_loginMail.setForeground(new java.awt.Color(255, 255, 255));
        txtfld_loginMail.setBorder(null);
        txtfld_loginMail.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfld_loginMailCaretUpdate(evt);
            }
        });

        jSeparator5.setForeground(new java.awt.Color(44, 47, 51));

        jSeparator6.setForeground(new java.awt.Color(44, 47, 51));

        lbl_loginPassword.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_loginPassword.setForeground(new java.awt.Color(132, 100, 200));
        lbl_loginPassword.setText("Password");

        btn_login.setBackground(new java.awt.Color(132, 100, 200));
        btn_login.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_login.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_login.setForeground(new java.awt.Color(255, 255, 255));
        btn_login.setLabel("Login");
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        lbl_forgottenPassword.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_forgottenPassword.setForeground(new java.awt.Color(102, 102, 102));
        lbl_forgottenPassword.setText("I forgot password");
        lbl_forgottenPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_forgottenPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_forgottenPasswordMouseClicked(evt);
            }
        });

        lbl_dontHaveAccount.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        lbl_dontHaveAccount.setForeground(new java.awt.Color(102, 102, 102));
        lbl_dontHaveAccount.setText("Don't have an account? Register now");
        lbl_dontHaveAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_dontHaveAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_dontHaveAccountMouseClicked(evt);
            }
        });

        chcbx_rememberMe.setBackground(new java.awt.Color(44, 47, 51));
        chcbx_rememberMe.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        chcbx_rememberMe.setForeground(new java.awt.Color(102, 102, 102));
        chcbx_rememberMe.setText("Remember Me");

        pswrdfld_loginPassword.setBackground(new java.awt.Color(44, 47, 51));
        pswrdfld_loginPassword.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        pswrdfld_loginPassword.setForeground(new java.awt.Color(255, 255, 255));
        pswrdfld_loginPassword.setBorder(null);

        javax.swing.GroupLayout pnl_loginLayout = new javax.swing.GroupLayout(pnl_login);
        pnl_login.setLayout(pnl_loginLayout);
        pnl_loginLayout.setHorizontalGroup(
            pnl_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_loginLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(pnl_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_loginMail)
                    .addComponent(txtfld_loginMail)
                    .addComponent(jSeparator5)
                    .addComponent(lbl_loginPassword)
                    .addComponent(pswrdfld_loginPassword)
                    .addComponent(jSeparator6)
                    .addComponent(chcbx_rememberMe)
                    .addGroup(pnl_loginLayout.createSequentialGroup()
                        .addComponent(lbl_forgottenPassword)
                        .addGap(23, 23, 23)
                        .addComponent(lbl_dontHaveAccount))
                    .addComponent(btn_login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        pnl_loginLayout.setVerticalGroup(
            pnl_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_loginLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(lbl_loginMail)
                .addGap(6, 6, 6)
                .addComponent(txtfld_loginMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbl_loginPassword)
                .addGap(5, 5, 5)
                .addComponent(pswrdfld_loginPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chcbx_rememberMe)
                .addGap(33, 33, 33)
                .addGroup(pnl_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_forgottenPassword)
                    .addComponent(lbl_dontHaveAccount))
                .addGap(10, 10, 10)
                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(185, Short.MAX_VALUE))
        );

        tbdpn_tabbedPane.addTab("Login", pnl_login);

        pnl_register.setBackground(new java.awt.Color(44, 47, 51));
        pnl_register.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                LoginFrame.this.mouseMoved(evt);
            }
        });

        lbl_adSoyad.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_adSoyad.setForeground(new java.awt.Color(132, 100, 200));
        lbl_adSoyad.setText("Name / Surname");

        lbl_mail.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_mail.setForeground(new java.awt.Color(132, 100, 200));
        lbl_mail.setText("Mail");

        txtfld_registerNameSurname.setBackground(new java.awt.Color(44, 47, 51));
        txtfld_registerNameSurname.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtfld_registerNameSurname.setForeground(new java.awt.Color(255, 255, 255));
        txtfld_registerNameSurname.setBorder(null);

        btn_register.setBackground(new java.awt.Color(132, 100, 200));
        btn_register.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_register.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        btn_register.setForeground(new java.awt.Color(255, 255, 255));
        btn_register.setLabel("Register");
        btn_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registerActionPerformed(evt);
            }
        });

        sprtr_loginFrameNameSurname.setForeground(new java.awt.Color(44, 47, 51));
        sprtr_loginFrameNameSurname.setOpaque(true);

        sprtr_loginFrameMail.setForeground(new java.awt.Color(44, 47, 51));
        sprtr_loginFrameMail.setOpaque(true);

        txtfld_registerMail.setBackground(new java.awt.Color(44, 47, 51));
        txtfld_registerMail.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        txtfld_registerMail.setForeground(new java.awt.Color(255, 255, 255));
        txtfld_registerMail.setBorder(null);
        txtfld_registerMail.setPreferredSize(new java.awt.Dimension(6, 19));

        lbl_password.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_password.setForeground(new java.awt.Color(132, 100, 200));
        lbl_password.setText("Password");

        pswrdfld_registerPassword.setBackground(new java.awt.Color(44, 47, 51));
        pswrdfld_registerPassword.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        pswrdfld_registerPassword.setForeground(new java.awt.Color(255, 255, 255));
        pswrdfld_registerPassword.setBorder(null);
        pswrdfld_registerPassword.setPreferredSize(new java.awt.Dimension(6, 19));

        sprtr_loginFramePassword.setForeground(new java.awt.Color(44, 47, 51));
        sprtr_loginFramePassword.setOpaque(true);

        lbl_date.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(132, 100, 200));
        lbl_date.setText("Birth Date");

        frmttxtfld_registerDate.setBackground(new java.awt.Color(44, 47, 51));
        frmttxtfld_registerDate.setBorder(null);
        frmttxtfld_registerDate.setForeground(new java.awt.Color(255, 255, 255));
        try {
            frmttxtfld_registerDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.##.####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        frmttxtfld_registerDate.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        frmttxtfld_registerDate.setPreferredSize(new java.awt.Dimension(56, 19));

        sprtr_loginFrameDate.setForeground(new java.awt.Color(44, 47, 51));
        sprtr_loginFrameDate.setOpaque(true);

        lbl_gender.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        lbl_gender.setForeground(new java.awt.Color(132, 100, 200));
        lbl_gender.setText("Gender");

        rdbtn_female.setBackground(new java.awt.Color(44, 47, 51));
        buttonGroup1.add(rdbtn_female);
        rdbtn_female.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        rdbtn_female.setForeground(new java.awt.Color(132, 100, 200));
        rdbtn_female.setText("Female");

        rdbtn_male.setBackground(new java.awt.Color(44, 47, 51));
        buttonGroup1.add(rdbtn_male);
        rdbtn_male.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        rdbtn_male.setForeground(new java.awt.Color(132, 100, 200));
        rdbtn_male.setSelected(true);
        rdbtn_male.setText("Male");

        lbl_loginFrameNameSurnameError.setForeground(new java.awt.Color(237, 67, 55));
        lbl_loginFrameNameSurnameError.setText(" ");

        lbl_loginFrameMailError.setForeground(new java.awt.Color(237, 67, 55));
        lbl_loginFrameMailError.setText(" ");

        lbl_loginFramePasswordError.setForeground(new java.awt.Color(237, 67, 55));
        lbl_loginFramePasswordError.setText(" ");

        lbl_loginFrameDateError.setForeground(new java.awt.Color(237, 67, 55));
        lbl_loginFrameDateError.setText(" ");
        lbl_loginFrameDateError.setMaximumSize(new java.awt.Dimension(14, 14));
        lbl_loginFrameDateError.setMinimumSize(new java.awt.Dimension(14, 14));
        lbl_loginFrameDateError.setPreferredSize(new java.awt.Dimension(14, 14));

        javax.swing.GroupLayout pnl_registerLayout = new javax.swing.GroupLayout(pnl_register);
        pnl_register.setLayout(pnl_registerLayout);
        pnl_registerLayout.setHorizontalGroup(
            pnl_registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_registerLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(pnl_registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_adSoyad)
                    .addComponent(txtfld_registerNameSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sprtr_loginFrameNameSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_loginFrameNameSurnameError, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_mail)
                    .addComponent(txtfld_registerMail, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sprtr_loginFrameMail, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_loginFrameMailError, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_password)
                    .addComponent(pswrdfld_registerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sprtr_loginFramePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_loginFramePasswordError, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_date)
                    .addComponent(frmttxtfld_registerDate, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sprtr_loginFrameDate, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_loginFrameDateError, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_gender)
                    .addGroup(pnl_registerLayout.createSequentialGroup()
                        .addComponent(rdbtn_female)
                        .addGap(11, 11, 11)
                        .addComponent(rdbtn_male))
                    .addComponent(btn_register, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnl_registerLayout.setVerticalGroup(
            pnl_registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_registerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lbl_adSoyad)
                .addGap(8, 8, 8)
                .addComponent(txtfld_registerNameSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(sprtr_loginFrameNameSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lbl_loginFrameNameSurnameError)
                .addGap(11, 11, 11)
                .addComponent(lbl_mail)
                .addGap(6, 6, 6)
                .addComponent(txtfld_registerMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(sprtr_loginFrameMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lbl_loginFrameMailError)
                .addGap(6, 6, 6)
                .addComponent(lbl_password)
                .addGap(6, 6, 6)
                .addComponent(pswrdfld_registerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(sprtr_loginFramePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lbl_loginFramePasswordError)
                .addGap(6, 6, 6)
                .addComponent(lbl_date)
                .addGap(6, 6, 6)
                .addComponent(frmttxtfld_registerDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(sprtr_loginFrameDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lbl_loginFrameDateError, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lbl_gender)
                .addGap(18, 18, 18)
                .addGroup(pnl_registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdbtn_female)
                    .addComponent(rdbtn_male))
                .addGap(36, 36, 36)
                .addComponent(btn_register, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tbdpn_tabbedPane.addTab("Register", pnl_register);

        pnl_rightPanel.add(tbdpn_tabbedPane);
        tbdpn_tabbedPane.getAccessibleContext().setAccessibleName("Login");
        tbdpn_tabbedPane.getAccessibleContext().setAccessibleDescription("");

        pnl_loginFrameBackPanel.add(pnl_rightPanel);

        getContentPane().add(pnl_loginFrameBackPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_dontHaveAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dontHaveAccountMouseClicked
        // TODO add your handling code here:
        tbdpn_tabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_lbl_dontHaveAccountMouseClicked

    private void btn_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registerActionPerformed
        // TODO add your handling code here:
        boolean isNameSurname = false;
        boolean isMail = false;
        boolean isPass = false;
        boolean isDate = false;

        Matcher matcher;

        //----------------------   NAME SURNAME CONTROL   ----------------------
        if (txtfld_registerNameSurname.getText().isEmpty()) {
            lbl_loginFrameNameSurnameError.setText("Name Surname field cannot be empty.");
            sprtr_loginFrameNameSurname.setBackground(errorColor);
        } else {
            matcher = Pattern.compile("^[a-zA-ZçğıİöşüÇĞÖŞÜ\\s]*$").matcher(txtfld_registerNameSurname.getText());
            if (!matcher.find()) {
                lbl_loginFrameNameSurnameError.setText("Name Surname can't contain chars other than alphabets.");
                sprtr_loginFrameNameSurname.setBackground(errorColor);
            } else {
                matcher = Pattern.compile("^[\\S]{2,} [\\S]{2,}( [\\S]{2,})*$").matcher(txtfld_registerNameSurname.getText());
                if (!matcher.find()) {
                    lbl_loginFrameNameSurnameError.setText("Name Surname must be atleast 5 chars and 2 words long.");
                    sprtr_loginFrameNameSurname.setBackground(errorColor);
                } else {
                    lbl_loginFrameNameSurnameError.setText(" ");
                    sprtr_loginFrameNameSurname.setBackground(defaultBackground);
                    isNameSurname = true;
                }
            }
        }
        //----------------------------------------------------------------------

        //--------------------------   MAIL CONTROL   --------------------------
        if (txtfld_registerMail.getText().isEmpty()) {
            lbl_loginFrameMailError.setText("Mail field cannot be empty.");
            sprtr_loginFrameMail.setBackground(errorColor);
        } else {
            matcher = Pattern.compile("^[a-zA-Z0-9@.]*$").matcher(txtfld_registerMail.getText());
            if (!matcher.find()) {
                lbl_loginFrameMailError.setText("Mail cannot contain chars other than English alphabets.");
                sprtr_loginFrameMail.setBackground(errorColor);
            } else {
                matcher = Pattern.compile("^[A-Za-z0-9]+@(gmail|hotmail|yandexmail|yahoomail)(.com)$").matcher(txtfld_registerMail.getText());
                if (!matcher.find()) {
                    lbl_loginFrameMailError.setText("Mail format should be like example@gmail.com");
                    sprtr_loginFrameMail.setBackground(errorColor);
                } else {
                    lbl_loginFrameMailError.setText(" ");
                    sprtr_loginFrameMail.setBackground(defaultBackground);
                    isMail = true;
                }
            }
        }
        //----------------------------------------------------------------------

        //-----------------------    PASSWORD CONTROL    -----------------------
        if (pswrdfld_registerPassword.getText().isEmpty()) {
            lbl_loginFramePasswordError.setText("Password field cannot be empty.");
            sprtr_loginFramePassword.setBackground(errorColor);
        } else {
            matcher = Pattern.compile("^[a-zA-Z0-9çğıİöşüÇĞÖŞÜ]*$").matcher(pswrdfld_registerPassword.getText());
            if (!matcher.find()) {
                lbl_loginFramePasswordError.setText("Password can only contain alphabet chars and numbers.");
                sprtr_loginFramePassword.setBackground(errorColor);
            } else {
                lbl_loginFramePasswordError.setText(" ");
                sprtr_loginFramePassword.setBackground(defaultBackground);
                isPass = true;
            }
        }
        //----------------------------------------------------------------------

        //-------------------------    DATE CONTROL    -------------------------
        if (frmttxtfld_registerDate.getText().contains(" ")) {
            lbl_loginFrameDateError.setText("Date field cannot contain space character.");
            sprtr_loginFrameDate.setBackground(errorColor);
        } else {
            int day = Integer.parseInt(frmttxtfld_registerDate.getText().substring(0, 2));
            int month = Integer.parseInt(frmttxtfld_registerDate.getText().substring(3, 5));
            int year = Integer.parseInt(frmttxtfld_registerDate.getText().substring(6, 10));

            if (day <= 31) {
                if (month <= 12) {
                    if (year >= 1800) {
                        lbl_loginFrameDateError.setText(" ");
                        sprtr_loginFrameDate.setBackground(defaultBackground);
                        isDate = true;
                    } else {
                        lbl_loginFrameDateError.setText("Year value cannot be lower than 1800");
                        sprtr_loginFrameDate.setBackground(errorColor);
                    }
                } else {
                    lbl_loginFrameDateError.setText("Month value cannot be greater than 12");
                    sprtr_loginFrameDate.setBackground(errorColor);
                }
            } else {
                lbl_loginFrameDateError.setText("Day value cannot be greater than 31");
                sprtr_loginFrameDate.setBackground(errorColor);
            }
        }
        //----------------------------------------------------------------------

        if (isNameSurname && isMail && isPass & isDate) {

            //-----------------------------------------------------------------
            String name = txtfld_registerNameSurname.getText().split(" ", 2)[0];
            String surName = txtfld_registerNameSurname.getText().split(" ", 2)[1];
            String mail = txtfld_registerMail.getText();
            String password = pswrdfld_registerPassword.getText();
            String date = frmttxtfld_registerDate.getText();
            String gender = "";
            if (rdbtn_male.isSelected()) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            //-----------------------------------------------------------------

            if (!isMailExist(mail)) {
                if (register(name, surName, mail, password, date, gender)) {
                    dlg_accountRegistered.setVisible(true);
                    dlg_accountRegistered.setLocationRelativeTo(null);
                } else {
                    System.out.println("Bir hata olustu.");
                }
            } else {
                dlg_mailNotUnique.setVisible(true);
                dlg_mailNotUnique.setLocationRelativeTo(null);
            }
        }
    }//GEN-LAST:event_btn_registerActionPerformed

    private void btn_areasNotFilledButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_areasNotFilledButtonActionPerformed
        // TODO add your handling code here:
        dlg_areasNotFilled.dispose();
    }//GEN-LAST:event_btn_areasNotFilledButtonActionPerformed

    private void btn_mailWarningButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mailWarningButtonActionPerformed
        // TODO add your handling code here:
        dlg_mailNotUnique.dispose();
        tbdpn_tabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_btn_mailWarningButtonActionPerformed

    private void btn_accountRegisteredButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_accountRegisteredButtonActionPerformed
        // TODO add your handling code here:
        dlg_accountRegistered.dispose();
        tbdpn_tabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_btn_accountRegisteredButtonActionPerformed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        // TODO add your handling code here:

        if (login(txtfld_loginMail.getText(), pswrdfld_loginPassword.getText())) {
            setRememberMeToDatabase(txtfld_loginMail.getText());

            //#################      START THE PROGRAM       ###################
            
            if (txtfld_loginMail.getText().equals(adminMail)) {
                //Admin frame opens.
                
                CURRENT_ID = getCurrentID(txtfld_loginMail.getText());
                this.setVisible(false);
                jfrm_adminFrame.setVisible(true);
                jfrm_adminFrame.setLocationRelativeTo(null);
                jfrm_adminFrame.setExtendedState(jfrm_userFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                updateJTable();
                updateJList();
                appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - The account has been logged in.\n", Color.WHITE);
            } else {
                //User frame opens.
                
                cmbbox_userCountries.setSelectedIndex(0);
                cmbbox_userCities.setSelectedIndex(0);
                cmbbox_userCities.setEnabled(false);
                txtarea_userFullAddress.setText("");
                lbl_guideMap.setIcon(iconWorldMapDark);
                prgrsbr_userFrameMapLoading.setValue(0);

                CURRENT_ID = getCurrentID(txtfld_loginMail.getText());
                this.setVisible(false);
                jfrm_userFrame.setVisible(true);
                jfrm_userFrame.setLocationRelativeTo(null);
                jfrm_userFrame.setExtendedState(jfrm_userFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                btn_userSaveToDatabase.setEnabled(true);

                if (isIdExistInTBL_ADDRESSES(CURRENT_ID)) {
                    btn_userSaveToDatabase.setEnabled(false);
                }
                updateProfilePicOnUserFrame();
            }
        }
    }//GEN-LAST:event_btn_loginActionPerformed

    private void btn_mailNotExistWarningButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mailNotExistWarningButtonActionPerformed
        // TODO add your handling code here:
        dlg_mailNotExist.dispose();
        tbdpn_tabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_btn_mailNotExistWarningButtonActionPerformed

    private void txtfld_loginMailCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfld_loginMailCaretUpdate
        // TODO add your handling code here:
        if (isMailExist(txtfld_loginMail.getText()) && isRememberMeSelected(txtfld_loginMail.getText())) {

            String databasePassword = "";
            Connection conn = null;

            try {
                conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
                Statement statement = conn.createStatement();
                String query = "SELECT MAIL, PASSWORD, REMEMBERME FROM TBL_PERSONELINFO";
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String tableEmail = resultSet.getString("MAIL");
                    if (tableEmail.equals(txtfld_loginMail.getText())) {
                        databasePassword = resultSet.getString("PASSWORD");
                        pswrdfld_loginPassword.setText(databasePassword);
                        chcbx_rememberMe.setSelected(true);
                    }
                }

                conn.close();

                try {
                    if (conn != null && !conn.isClosed()) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtfld_loginMailCaretUpdate

    private void btn_wrongPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_wrongPasswordButtonActionPerformed
        // TODO add your handling code here:
        dlg_wrongPassword.dispose();
    }//GEN-LAST:event_btn_wrongPasswordButtonActionPerformed

    private void lbl_forgottenPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_forgottenPasswordMouseClicked
        // TODO add your handling code here:
        dlg_forgotPassword.setVisible(true);
        dlg_forgotPassword.setLocationRelativeTo(null);
    }//GEN-LAST:event_lbl_forgottenPasswordMouseClicked

    private void btn_forgotPassCodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_forgotPassCodeButtonActionPerformed
        // TODO add your handling code here:
        if (!txtfld_forgotPassMail.getText().equals("")) {
            randomCode = 100000 + (int) (Math.random() * 900000); //random code is generated.
            if (lbl_timer.getText().equals("") || lbl_timer.getText().equals("0:00")) {
                second = 59;
                minute = 4;
                countdownTimer();
                timer.start(); // 5 minute countdown starts.
                sendMail(txtfld_forgotPassMail.getText(), randomCode); //sends random code to the chosen mail.
            }
        }
    }//GEN-LAST:event_btn_forgotPassCodeButtonActionPerformed

    private void frmtd_forgotPassCodeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_frmtd_forgotPassCodeCaretUpdate
        // TODO add your handling code here:
        String userCode = frmtd_forgotPassCode.getText();
        userCode = userCode.replaceAll(" ", "");
        boolean isPassChanged = false;

        if (userCode.equals(Integer.toString(randomCode))) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
                Statement stmnt = conn.createStatement();
                String query = "UPDATE TBL_PERSONELINFO SET PASSWORD = '" + randomCode + "' WHERE MAIL = '" + txtfld_forgotPassMail.getText() + "'";
                stmnt.executeUpdate(query);
                System.out.println("password changed");
                isPassChanged = true;
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (isPassChanged) {
            dlg_passwordChanged.setVisible(true);
            dlg_passwordChanged.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_frmtd_forgotPassCodeCaretUpdate

    private void btn_passChangedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_passChangedButtonActionPerformed
        // TODO add your handling code here:
        dlg_passwordChanged.dispose();
        dlg_forgotPassword.dispose();
    }//GEN-LAST:event_btn_passChangedButtonActionPerformed

    private void scrlpn_mapMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrlpn_mapMouseDragged
        // TODO add your handling code here:

        int mouseX = pressedLocation.x;
        int mouseY = pressedLocation.y;

        int deltaX = mouseX - evt.getX();
        int deltaY = mouseY - evt.getY();

        //Move the scrollPane with mouse.
        scrlpn_map.getVerticalScrollBar().setValue(scrlpn_map.getVerticalScrollBar().getValue() + (int) (deltaY / 70));
        scrlpn_map.getHorizontalScrollBar().setValue(scrlpn_map.getHorizontalScrollBar().getValue() + (int) (deltaX / 70));
    }//GEN-LAST:event_scrlpn_mapMouseDragged

    private void MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MousePressed
        // TODO add your handling code here:
        pressedLocation = evt.getPoint();
    }//GEN-LAST:event_MousePressed


    private void mouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseMoved
        // TODO add your handling code here:
        
        //Moves the scrollpane on the login frame based on mouse movement.
        scrlpn_leftPanel.getVerticalScrollBar().setValue((int) (MouseInfo.getPointerInfo().getLocation().x / 40));
        scrlpn_leftPanel.getHorizontalScrollBar().setValue((int) (MouseInfo.getPointerInfo().getLocation().y / 40));
    }//GEN-LAST:event_mouseMoved

    private void cmbbox_userCountriesİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbbox_userCountriesİtemStateChanged
        // TODO add your handling code here:
        if (!mnTm_dontDisplayMap.isSelected()) {
            prgrsbr_userFrameMapLoading.setValue(5);
            if (evt.getStateChange() == ItemEvent.SELECTED && cmbbox_userCountries.getSelectedIndex() != 0) {
                prgrsbr_userFrameMapLoading.setValue(15);
                String place = cmbbox_userCountries.getSelectedItem().toString();
                place = place.replaceAll(" ", "_");
                System.out.println(place);
                File file = takeScreenShot(place);
                ImageIcon iconPlace = new ImageIcon(scale(cropImage(file), 1401, 981));
                prgrsbr_userFrameMapLoading.setValue(90);
                lbl_guideMap.setIcon(iconPlace);
                prgrsbr_userFrameMapLoading.setValue(100);

                cmbbox_userCities.setEnabled(true);

                ArrayList<String> citiesList = new ArrayList<>();
                citiesList.add("- CHOOSE A CITY -");

                try {
                    File citiesFile = new File("cities.txt");
                    Scanner scanner = new Scanner(citiesFile);

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.contains(cmbbox_userCountries.getSelectedItem().toString())) {
                            line = line.split("\t")[0];
                            citiesList.add(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println("An error ocurred!");
                    ex.printStackTrace();
                }

                cmbbox_userCities.setModel(new DefaultComboBoxModel<>(citiesList.toArray(new String[0])));
            }
        } else {
            if (evt.getStateChange() == ItemEvent.SELECTED && cmbbox_userCountries.getSelectedIndex() != 0) {

                cmbbox_userCities.setEnabled(true);
                ArrayList<String> citiesList = new ArrayList<>();
                citiesList.add("- CHOOSE A CITY -");

                try {
                    File citiesFile = new File("cities.txt");
                    Scanner scanner = new Scanner(citiesFile);

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.contains(cmbbox_userCountries.getSelectedItem().toString())) {
                            line = line.split("\t")[0];
                            citiesList.add(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println("An error ocurred!");
                    ex.printStackTrace();
                }
                cmbbox_userCities.setModel(new DefaultComboBoxModel<>(citiesList.toArray(new String[0])));
            }
        }
    }//GEN-LAST:event_cmbbox_userCountriesİtemStateChanged

    private void cmbbox_userCitiesİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbbox_userCitiesİtemStateChanged
        // TODO add your handling code here:
        if (!mnTm_dontDisplayMap.isSelected()) {
            prgrsbr_userFrameMapLoading.setValue(0);
            if (evt.getStateChange() == ItemEvent.SELECTED && cmbbox_userCities.getSelectedIndex() != 0) {
                prgrsbr_userFrameMapLoading.setValue(5);
                String place = cmbbox_userCities.getSelectedItem().toString();
                prgrsbr_userFrameMapLoading.setValue(10);
                place = place.replaceAll(" ", "_");
                System.out.println(place);
                
                File file = takeScreenShot(place); 
                //takes screenshot of the selected place on google maps.
                
                ImageIcon iconPlace = new ImageIcon(scale(cropImage(file), 1401, 981)); 
                //crops and scales the taken screenshot and converts to imageIcon
                
                prgrsbr_userFrameMapLoading.setValue(90);
                lbl_guideMap.setIcon(iconPlace);
                prgrsbr_userFrameMapLoading.setValue(100);
            }
        }
    }//GEN-LAST:event_cmbbox_userCitiesİtemStateChanged

    private void btn_userLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_userLogOutActionPerformed
        // TODO add your handling code here:
        CURRENT_ID = 0;

        jfrm_userFrame.setVisible(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        tbdpn_tabbedPane.setSelectedIndex(0);

        txtfld_loginMail.setText("");
        pswrdfld_loginPassword.setText("");
    }//GEN-LAST:event_btn_userLogOutActionPerformed

    private void btn_userSaveToDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_userSaveToDatabaseActionPerformed
        // TODO add your handling code here:
        System.out.println("Saving...");
        if (cmbbox_userCountries.getSelectedIndex() != 0 && cmbbox_userCities.getSelectedIndex() != 0 && !txtarea_userFullAddress.getText().equals("")) {
            saveAddressToDatabase(cmbbox_userCountries.getSelectedItem().toString(), cmbbox_userCities.getSelectedItem().toString(), txtarea_userFullAddress.getText());
            System.out.println("Saved.");
            btn_userSaveToDatabase.setEnabled(false);
            dlg_savedToDatabase.setVisible(true);
            dlg_savedToDatabase.setLocationRelativeTo(null);
        } else {
            dlg_areasNotFilled.setVisible(true);
            dlg_areasNotFilled.setLocationRelativeTo(null);
            System.out.println("Could not save!");
        }

    }//GEN-LAST:event_btn_userSaveToDatabaseActionPerformed

    private void btn_userUpdateToDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_userUpdateToDatabaseActionPerformed
        // TODO add your handling code here:
        System.out.println("Updating...");
        if (cmbbox_userCountries.getSelectedIndex() != 0 && cmbbox_userCities.getSelectedIndex() != 0 && !txtarea_userFullAddress.getText().equals("")) {
            updateAddressInDatabase(cmbbox_userCountries.getSelectedItem().toString(), cmbbox_userCities.getSelectedItem().toString(), txtarea_userFullAddress.getText());
            System.out.println("Updated.");
            dlg_updatedToDatabase.setVisible(true);
            dlg_updatedToDatabase.setLocationRelativeTo(null);
        } else {
            dlg_areasNotFilled.setVisible(true);
            dlg_areasNotFilled.setLocationRelativeTo(null);
            System.out.println("Could not updated!");
        }
    }//GEN-LAST:event_btn_userUpdateToDatabaseActionPerformed

    private void btn_adminFrameLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adminFrameLogOutActionPerformed
        // TODO add your handling code here:
        CURRENT_ID = 0;

        jfrm_adminFrame.setVisible(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        tbdpn_tabbedPane.setSelectedIndex(0);

        txtfld_loginMail.setText("");
        pswrdfld_loginPassword.setText("");

        appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - The account has been logged out.\n", Color.RED);
    }//GEN-LAST:event_btn_adminFrameLogOutActionPerformed

    private void btn_adminFrameDeleteUsersAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adminFrameDeleteUsersAddressActionPerformed
        // TODO add your handling code here:
        if (!lst_adminFramePersonList.isSelectionEmpty()) {
            int ID = Integer.parseInt(lbl_adminFrameID.getText());
            deleteInformations(ID, false);
        }
    }//GEN-LAST:event_btn_adminFrameDeleteUsersAddressActionPerformed

    private void btn_updateTheTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateTheTableActionPerformed
        // TODO add your handling code here:

        updateJList();

        updateJTable();

        updateProfilePicOnAdminFrame();
    }//GEN-LAST:event_btn_updateTheTableActionPerformed

    private void btn_applyChangesToDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_applyChangesToDatabaseActionPerformed
        // TODO add your handling code here:
        if (tbl_adminFrameTable.getSelectedRow() != -1 && tbl_adminFrameTable.getSelectedRows().length == 1) {
            int selectedRow = tbl_adminFrameTable.getSelectedRow();
            int ID = Integer.parseInt(tbl_adminFrameTable.getValueAt(selectedRow, 0).toString());
            String country = tbl_adminFrameTable.getValueAt(selectedRow, 1).toString();
            String city = tbl_adminFrameTable.getValueAt(selectedRow, 2).toString();
            String fullAddress = tbl_adminFrameTable.getValueAt(selectedRow, 3).toString();

            updateAddressInDatabaseAccToID(ID, country, city, fullAddress);
        }
    }//GEN-LAST:event_btn_applyChangesToDatabaseActionPerformed

    private void txtfld_adminFrameAddressSearchBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtfld_adminFrameAddressSearchBoxMouseClicked
        // TODO add your handling code here:
        txtfld_adminFrameAddressSearchBox.setForeground(Color.WHITE);
        txtfld_adminFrameAddressSearchBox.setText("");
    }//GEN-LAST:event_txtfld_adminFrameAddressSearchBoxMouseClicked

    private void lst_adminFramePersonListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lst_adminFramePersonListValueChanged
        // TODO add your handling code here:
        //Update the personel informations
        if (!lst_adminFramePersonList.isSelectionEmpty() && lst_adminFramePersonList.getSelectedValuesList().size() == 1) {
            lbl_adminFrameID.setText(getPersonelInformations(lst_adminFramePersonList.getSelectedValue())[0]);
            lbl_adminFrameNameSurname.setText(getPersonelInformations(lst_adminFramePersonList.getSelectedValue())[1]);
            lbl_adminFrameMail.setText(getPersonelInformations(lst_adminFramePersonList.getSelectedValue())[2]);
            lbl_adminFrameDate.setText(getPersonelInformations(lst_adminFramePersonList.getSelectedValue())[3]);
            lbl_adminFrameGender.setText(getPersonelInformations(lst_adminFramePersonList.getSelectedValue())[4]);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Personel informations has been updated.\n", Color.WHITE);
        }

        updateProfilePicOnAdminFrame();
    }//GEN-LAST:event_lst_adminFramePersonListValueChanged

    private void btn_adminFrameDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adminFrameDeleteUserActionPerformed
        // TODO add your handling code here:
        if (!lst_adminFramePersonList.isSelectionEmpty()) {
            int ID = Integer.parseInt(lbl_adminFrameID.getText());
            deleteInformations(ID, true);
        }
    }//GEN-LAST:event_btn_adminFrameDeleteUserActionPerformed

    private void txtfld_adminFrameAddressSearchBoxCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfld_adminFrameAddressSearchBoxCaretUpdate
        // TODO add your handling code here:
        if(!txtfld_adminFrameAddressSearchBox.getText().equals("Search Here!")) {
            searchAddress(txtfld_adminFrameAddressSearchBox.getText());
        }
    }//GEN-LAST:event_txtfld_adminFrameAddressSearchBoxCaretUpdate

    private void txtfld_adminFramePersonSearchBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtfld_adminFramePersonSearchBoxMouseClicked
        // TODO add your handling code here:
        txtfld_adminFramePersonSearchBox.setForeground(Color.WHITE);
        txtfld_adminFramePersonSearchBox.setText("");
    }//GEN-LAST:event_txtfld_adminFramePersonSearchBoxMouseClicked

    private void txtfld_adminFramePersonSearchBoxCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfld_adminFramePersonSearchBoxCaretUpdate
        // TODO add your handling code here:
        if(!txtfld_adminFramePersonSearchBox.getText().equals("Search Here!")) {
            searchPerson(txtfld_adminFramePersonSearchBox.getText());
        }
    }//GEN-LAST:event_txtfld_adminFramePersonSearchBoxCaretUpdate

    private void btn_userFrameUploadPicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_userFrameUploadPicActionPerformed
        // TODO add your handling code here:
        jfrm_userFrame.setAlwaysOnTop(false);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        flchsr_fileChooser.setFileFilter(filter);
        flchsr_fileChooser.setVisible(true);
        int value = flchsr_fileChooser.showOpenDialog(this);
        BufferedImage profileImageUnscaled = null;
        profileImageScaled = null;

        if (value == JFileChooser.APPROVE_OPTION) {
            try {
                profileImageUnscaled = ImageIO.read(flchsr_fileChooser.getSelectedFile());
                // Retrieves the photo selected by the user.
                
                String filePath = flchsr_fileChooser.getSelectedFile().getAbsolutePath();
                // Saves the path of the photo it receives.

                updateProfilePicToDatabase(filePath);
                // Saves that photo to the database.
                
                profileImageScaled = scale(profileImageUnscaled, 150, 150);
                // Scales the photo.
            } catch (IOException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            ImageIcon profilePicIcon = new ImageIcon(profileImageScaled);
            lbl_userFrameProfilePic.setIcon(profilePicIcon);
            // Gets the scaled photo and cast it to imageIcon and set as a profile pic.

            jfrm_userFrame.setAlwaysOnTop(true);
        }
    }//GEN-LAST:event_btn_userFrameUploadPicActionPerformed

    private void btn_userFrameAdjustPictureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_userFrameAdjustPictureActionPerformed
        // TODO add your handling code here:
        jfrm_userFrame.setVisible(false);
        jfrm_profilePicAdjustment.setVisible(true);
        jfrm_profilePicAdjustment.setLocationRelativeTo(null);
    }//GEN-LAST:event_btn_userFrameAdjustPictureActionPerformed

    private void sldr_pictureFrameThicknessSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldr_pictureFrameThicknessSliderStateChanged
        // TODO add your handling code here:
        
        BufferedImage scaledLineImage = scale(lineImage, lineImage.getWidth(), (int) (sldr_pictureFrameThicknessSlider.getValue() * 3.5));
        // Adjusts the curvedLine thickness based on sliders value.
        
        ImageIcon scaledCurvedLineIcon = new ImageIcon(scaledLineImage);
        lbl_pictureFrameCurvedLine.setIcon(scaledCurvedLineIcon);
    }//GEN-LAST:event_sldr_pictureFrameThicknessSliderStateChanged

    private void btn_pictureFrameBackToUserFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pictureFrameBackToUserFrameActionPerformed
        // TODO add your handling code here:
        jfrm_profilePicAdjustment.setVisible(false);
        jfrm_userFrame.setVisible(true);
        jfrm_userFrame.setLocationRelativeTo(null);
        jfrm_userFrame.setExtendedState(jfrm_userFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        updateProfilePicOnUserFrame();
    }//GEN-LAST:event_btn_pictureFrameBackToUserFrameActionPerformed

    private void btn_pictureFrameOpenNewImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pictureFrameOpenNewImageActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        flchsr_fileChooser.setFileFilter(filter);
        flchsr_fileChooser.setVisible(true);
        int value = flchsr_fileChooser.showOpenDialog(this);
        BufferedImage profileImageUnscaled = null;

        if (value == JFileChooser.APPROVE_OPTION) {
            try {
                profileImageUnscaled = ImageIO.read(flchsr_fileChooser.getSelectedFile());
                // Retrieves the photo selected by the user.
                
                String filePath = flchsr_fileChooser.getSelectedFile().getAbsolutePath();
                // Saves the path of the photo it receives.

                profileImageScaled = scale(profileImageUnscaled, 500, 500);
                // Scales the photo.
            } catch (IOException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            paintGraphic = (Graphics2D) pnl_pictureFramePicPanel.getGraphics();
            paintGraphic.drawImage(profileImageScaled, 0, 0, this);
            // Paints selected photo to the JPanel.
        }
    }//GEN-LAST:event_btn_pictureFrameOpenNewImageActionPerformed

    private void pnl_pictureFramePicPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_pictureFramePicPanelMouseDragged
        // TODO add your handling code here:
        paintGraphic = (Graphics2D) pnl_pictureFramePicPanel.getGraphics();
        // Create new graphics2D
        
        paintGraphic.setColor(pictureAdjusterSelectedColor);
        // Sets to color based on users choice.
        
        paintGraphic.fillOval(evt.getX(), evt.getY(), sldr_pictureFrameThicknessSlider.getValue(), sldr_pictureFrameThicknessSlider.getValue());
        // Draws the paintGraphic.
    }//GEN-LAST:event_pnl_pictureFramePicPanelMouseDragged

    private void btn_pictureFrameOpenColorChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pictureFrameOpenColorChooserActionPerformed
        // TODO add your handling code here:
        JPanel[] selectedColorList = {pnl_color1, pnl_color2, pnl_color3, pnl_color4, pnl_color5};
        // Last used color list.

        pictureAdjusterSelectedColor = JColorChooser.showDialog(this, "Color Chooser", Color.RED);
        Color darkModeColor = new Color(44, 47, 51);

        if (selectedColorNumber != 4) {
            selectedColorList[selectedColorNumber].setBackground(pictureAdjusterSelectedColor);
            selectedColorNumber++;
        } else {
            selectedColorList[selectedColorNumber].setBackground(pictureAdjusterSelectedColor);
            selectedColorNumber = 0;
        }

        for (int y = 0; y < 93; y++) { //8676552 16777215
            for (int x = 0; x < 349; x++) {
                if (lineImage.getRGB(x, y) != new Color(44, 47, 51).getRGB()) {
                    lineImage.setRGB(x, y, pictureAdjusterSelectedColor.getRGB());
                    // If lineImage's seleced RGB doesnt matches regular background color's (dark grey) RGB
                    // it will repaint the selected RGB to selected colors RGB.
                }
            }
        }

        ImageIcon lineImageColored = new ImageIcon(lineImage);
        lbl_pictureFrameCurvedLine.setIcon(lineImageColored);

        reScaleSlider();
    }//GEN-LAST:event_btn_pictureFrameOpenColorChooserActionPerformed

    private void pnl_colorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_colorMouseClicked
        // TODO add your handling code here:
        pictureAdjusterSelectedColor = evt.getComponent().getBackground();

        for (int y = 0; y < 93; y++) { //8676552 16777215
            for (int x = 0; x < 349; x++) {
                if (lineImage.getRGB(x, y) != new Color(44, 47, 51).getRGB()) {
                    lineImage.setRGB(x, y, pictureAdjusterSelectedColor.getRGB());
                    // If lineImage's seleced RGB doesnt matches regular background color's (dark grey) RGB
                    // it will repaint the selected RGB to selected colors RGB.
                }
            }
        }

        ImageIcon lineImageColored = new ImageIcon(lineImage);
        lbl_pictureFrameCurvedLine.setIcon(lineImageColored);

        reScaleSlider();
    }//GEN-LAST:event_pnl_colorMouseClicked

    private void btn_userFrameChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_userFrameChangePasswordActionPerformed
        // TODO add your handling code here:
        dlg_changePassword.setVisible(true);
        dlg_changePassword.setLocationRelativeTo(null);
    }//GEN-LAST:event_btn_userFrameChangePasswordActionPerformed

    private void pswrdfld_oldPasswordCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_pswrdfld_oldPasswordCaretUpdate
        // TODO add your handling code here:
        if (!pswrdfld_oldPassword.getText().equals(getPasswordByID(CURRENT_ID))) {
            lbl_errorChangePassword.setText("This password does not match with your old password.");
            btn_changeThePassword.setEnabled(false);
        } else {
            lbl_errorChangePassword.setText(" ");
            btn_changeThePassword.setEnabled(true);
        }
    }//GEN-LAST:event_pswrdfld_oldPasswordCaretUpdate

    private void btn_changeThePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_changeThePasswordActionPerformed
        // TODO add your handling code here:
        if (setPasswordToDatabase(CURRENT_ID)) {
            dlg_passwordChanged.setVisible(true);
            dlg_passwordChanged.setAlwaysOnTop(true);
            dlg_passwordChanged.setLocationRelativeTo(null);
            dlg_changePassword.dispose();
        } else {
            System.out.println("Something went wrong.");
        }
    }//GEN-LAST:event_btn_changeThePasswordActionPerformed

    private void btn_pictureFrameGetPicFromDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pictureFrameGetPicFromDatabaseActionPerformed
        // TODO add your handling code here:
        String path = getProfileImagePathByID(CURRENT_ID);
        BufferedImage databaseProfilePic = null;
        try {
            databaseProfilePic = ImageIO.read(new File(path));
        } catch (IOException ex) {
            try {
                if (getGenderByID(CURRENT_ID).equals("Male")) {
                    databaseProfilePic = ImageIO.read(this.getClass().getClassLoader().getResource("male.png"));
                } else {
                    databaseProfilePic = ImageIO.read(this.getClass().getClassLoader().getResource("female.png"));
                }
                // Sets default male or female image
            } catch (IOException exc) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
        scaledDatabaseProfilePic = scale(databaseProfilePic, 500, 500);
        paintGraphic = (Graphics2D) pnl_pictureFramePicPanel.getGraphics();
        paintGraphic.drawImage(scaledDatabaseProfilePic, 0, 0, this);
        // Paints the default image to the JPanel.
    }//GEN-LAST:event_btn_pictureFrameGetPicFromDatabaseActionPerformed

    private void btn_pictureFrameSetPicToDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pictureFrameSetPicToDatabaseActionPerformed
        // TODO add your handling code here:
        String fileName = getNameSurnameByID(CURRENT_ID);

        int w = pnl_pictureFramePicPanel.getWidth();
        int h = pnl_pictureFramePicPanel.getHeight();

        Point panelPoint = pnl_pictureFramePicPanel.getLocationOnScreen();
        Dimension dimensionOfPanel = pnl_pictureFramePicPanel.getSize();
        Rectangle rectangle = new Rectangle(panelPoint, dimensionOfPanel);
        // Creates new rectangle on the JPanel.

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage panelImage = robot.createScreenCapture(rectangle);
        // Takes the screenshot of the rectangle.

        File imageFile = new File(fileName + ".jpg");
        try {
            ImageIO.write(panelImage, "jpg", imageFile);
            // Saves the screenshot to computer.
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        setProfilePicPathToDatabase(imageFile.getAbsolutePath());
    }//GEN-LAST:event_btn_pictureFrameSetPicToDatabaseActionPerformed

    private void mnTm_clearFullAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTm_clearFullAddressActionPerformed
        // TODO add your handling code here:
        txtarea_userFullAddress.setText("");
    }//GEN-LAST:event_mnTm_clearFullAddressActionPerformed

    private void mnTm_displayWorldMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTm_displayWorldMapActionPerformed
        // TODO add your handling code here:
        lbl_guideMap.setIcon(iconWorldMapDark);
    }//GEN-LAST:event_mnTm_displayWorldMapActionPerformed

    private void mnTm_clearLogAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTm_clearLogAreaActionPerformed
        // TODO add your handling code here:
        txtpn_adminFrameLog.setText("");
    }//GEN-LAST:event_mnTm_clearLogAreaActionPerformed

    private void mnTm_backupToComputerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTm_backupToComputerActionPerformed
        // TODO add your handling code here:
        dlg_backupToComputer.setVisible(true);
        dlg_backupToComputer.setLocationRelativeTo(null);
    }//GEN-LAST:event_mnTm_backupToComputerActionPerformed

    private void btn_backupToComputerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backupToComputerActionPerformed
        // TODO add your handling code here:
        flchsr_fileChooser.setVisible(true);
        int value = flchsr_fileChooser.showOpenDialog(this);

        if (value == JFileChooser.APPROVE_OPTION) {
            if (flchsr_fileChooser.getSelectedFile().getName().endsWith(".txt")) {
                backupFromDatabase(flchsr_fileChooser.getSelectedFile().getAbsolutePath());
                lbl_downloadInformator.setText("All the database informations has been downloaded!");
            }
        }
    }//GEN-LAST:event_btn_backupToComputerActionPerformed

    private void scrlpn_mapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrlpn_mapMouseClicked
        // TODO add your handling code here:
        if (SwingUtilities.isRightMouseButton(evt)) {
            pppmn_userFrameMapPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_scrlpn_mapMouseClicked

    private void mnTm_changeColorWorldMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTm_changeColorWorldMapActionPerformed
        // TODO add your handling code here:
        jfrm_userFrame.setAlwaysOnTop(false);
        
        Color chosenColor = JColorChooser.showDialog(this, "Color Chooser", Color.RED);
        
        for (int y = 0; y < worldMapDark.getHeight(); y++) { //8676552 16777215
            for (int x = 0; x < worldMapDark.getWidth(); x++) {
                if (worldMapDark.getRGB(x, y) != new Color(44, 47, 51).getRGB()) {
                    worldMapDark.setRGB(x, y, chosenColor.getRGB());
                    // If worldMapDark's seleced RGB doesnt matches regular background color's (dark grey) RGB
                    // it will repaint the selected RGB to selected colors RGB.
                }
            }
        }
        ImageIcon coloredWorldMapDark = new ImageIcon(worldMapDark);
        lbl_guideMap.setIcon(coloredWorldMapDark);
        
        jfrm_userFrame.setAlwaysOnTop(true);
    }//GEN-LAST:event_mnTm_changeColorWorldMapActionPerformed

    private void btn_savedToDatabaseOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_savedToDatabaseOKActionPerformed
        // TODO add your handling code here:
        dlg_savedToDatabase.dispose();
    }//GEN-LAST:event_btn_savedToDatabaseOKActionPerformed

    private void btn_updatedToDatabaseOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updatedToDatabaseOKActionPerformed
        // TODO add your handling code here:
        dlg_updatedToDatabase.dispose();
    }//GEN-LAST:event_btn_updatedToDatabaseOKActionPerformed

    private void txtfld_adminFramePersonSearchBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtfld_adminFramePersonSearchBoxFocusLost
        // TODO add your handling code here:
        txtfld_adminFramePersonSearchBox.setForeground(new Color(102, 102, 102));
        txtfld_adminFramePersonSearchBox.setText("Search Here!");
    }//GEN-LAST:event_txtfld_adminFramePersonSearchBoxFocusLost

    private void txtfld_adminFrameAddressSearchBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtfld_adminFrameAddressSearchBoxFocusLost
        // TODO add your handling code here:
        txtfld_adminFrameAddressSearchBox.setForeground(new Color(102, 102, 102));
        txtfld_adminFrameAddressSearchBox.setText("Search Here!");
    }//GEN-LAST:event_txtfld_adminFrameAddressSearchBoxFocusLost
 
    //##########################################################################
    //                                METHODS (39) STARTS
    //##########################################################################
    public boolean register(String name, String surname, String mail, String password, String date, String gender) {
        boolean bl = false;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT COUNT (*) FROM TBL_PERSONELINFO";
            ResultSet rs = stmnt.executeQuery(query);
            query = "INSERT INTO TBL_PERSONELINFO VALUES (" + newID() + ", '" + name + "', '" + surname + "', '" + mail + "', '" + password + "', '" + date + "', '" + gender + "', " + false + ", 'default')";
            stmnt.executeUpdate(query);
            bl = true;
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bl;
    }
 
    public boolean login(String mail, String password) {
        boolean bl = false;

        if (isMailExist(mail)) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
                Statement stmnt = conn.createStatement();
                String query = "SELECT PASSWORD FROM TBL_PERSONELINFO WHERE MAIL = '" + mail + "'";
                ResultSet rs = stmnt.executeQuery(query);

                while (rs.next()) {
                    String databasePassword = rs.getString("PASSWORD");
                    if (password.equals(databasePassword)) {
                        bl = true;
                        System.out.println("program acilir");
                    } else {
                        dlg_wrongPassword.setVisible(true);
                        dlg_wrongPassword.setLocationRelativeTo(null);
                    }
                }

                conn.close();

            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            dlg_mailNotExist.setVisible(true);
            dlg_mailNotExist.setLocationRelativeTo(null);
        }
        return bl;
    }

    public void searchAddress(String searchWord) {

        for (int i = addressesTableModel.getRowCount() - 1; i >= 0; i--) {
            addressesTableModel.removeRow(i);
        }
        appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Address table has been cleared.\n", Color.WHITE);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Connected the database.\n", Color.WHITE);
            Statement stmnt = conn.createStatement();
            String query = "SELECT * FROM TBL_ADDRESSES WHERE "
                    + "COUNTRY LIKE '%" + searchWord
                    + "%' OR CITY LIKE '%" + searchWord
                    + "%' OR FULLADDRESS LIKE '%" + searchWord + "%'";
            ResultSet rs = stmnt.executeQuery(query);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - The word '" + searchWord + "'has been searched in the database.\n", Color.WHITE);
            while (rs.next()) {
                String ID = rs.getInt("ID") + "";
                String country = rs.getString("COUNTRY");
                String city = rs.getString("CITY");
                String fullAddress = rs.getString("FULLADDRESS");
                addressesTableModel.addRow(new Object[]{ID, country, city, fullAddress});
            }
            tbl_adminFrameTable.setModel(addressesTableModel);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Address table has been updated.\n", Color.WHITE);

            conn.close();
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Disonnected from the database.\n", Color.WHITE);

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }
    }

    public void searchPerson(String searchWord) {

        personsListModel.clear();
        appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Persons list cleared.\n", Color.RED);
        ArrayList<String> personsList = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Connected the database.\n", Color.WHITE);
            Statement stmnt = conn.createStatement();
            String query = "SELECT NAME, SURNAME FROM TBL_PERSONELINFO WHERE "
                    + "NAME LIKE '%" + searchWord
                    + "%' OR SURNAME LIKE '%" + searchWord + "%'";
            ResultSet rs = stmnt.executeQuery(query);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - The word '" + searchWord + "'has been searched in the database.\n", Color.WHITE);
            while (rs.next()) {
                String nameSurname = rs.getString("NAME") + " ";
                nameSurname += rs.getString("SURNAME");
                personsList.add(nameSurname);
            }
            for (int i = 0; i < personsList.size(); i++) {
                personsListModel.addElement(personsList.get(i));
            }
            lst_adminFramePersonList.setModel(personsListModel);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Persons list has been updated.\n", Color.WHITE);

            conn.close();
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Disconnected from the database.\n", Color.WHITE);

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }
    }

    public void deleteInformations(int ID, boolean isBoth) { //If true: delete users informations and address informations, if false: delete only address informations
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Connected the database.\n", Color.WHITE);
            Statement stmnt = conn.createStatement();
            String query = "DELETE FROM TBL_ADDRESSES WHERE ID = " + ID;
            stmnt.executeUpdate(query);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " The users address informations whose id is " + ID + " has been deleted.\n", Color.WHITE);
            if (isBoth) {
                query = "DELETE FROM TBL_PERSONELINFO WHERE ID = " + ID;
                stmnt.executeUpdate(query);
                appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " The users informations whose id is " + ID + " has been deleted.\n", Color.WHITE);
            }

            conn.close();
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Disonnected from the database.\n", Color.WHITE);

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }
    }

    public void saveAddressToDatabase(String country, String city, String fullAddress) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT * FROM TBL_ADDRESSES";
            ResultSet rs = stmnt.executeQuery(query);
            query = "INSERT INTO TBL_ADDRESSES VALUES (" + CURRENT_ID + ", '" + country + "', '" + city + "', '" + fullAddress + "')";
            stmnt.executeUpdate(query);
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateProfilePicToDatabase(String filePath) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT ID, PROFILEPIC FROM TBL_PERSONELINFO";
            ResultSet rs = stmnt.executeQuery(query);
            query = "UPDATE TBL_PERSONELINFO SET PROFILEPIC = '" + filePath + "' WHERE ID = " + CURRENT_ID;
            stmnt.executeUpdate(query);
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateAddressInDatabase(String country, String city, String fullAddress) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT * FROM TBL_ADDRESSES";
            ResultSet rs = stmnt.executeQuery(query);
            query = "UPDATE TBL_ADDRESSES "
                    + "SET COUNTRY = '" + country + "'"
                    + ", CITY = '" + city + "'"
                    + ", FULLADDRESS = '" + fullAddress + "'"
                    + "WHERE ID = " + CURRENT_ID;
            stmnt.executeUpdate(query);
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateAddressInDatabaseAccToID(int ID, String country, String city, String fullAddress) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Connected the database.\n", Color.WHITE);
            Statement stmnt = conn.createStatement();
            String query = "SELECT * FROM TBL_ADDRESSES";
            ResultSet rs = stmnt.executeQuery(query);
            query = "UPDATE TBL_ADDRESSES "
                    + "SET COUNTRY = '" + country + "'"
                    + ", CITY = '" + city + "'"
                    + ", FULLADDRESS = '" + fullAddress + "'"
                    + "WHERE ID = " + ID;
            stmnt.executeUpdate(query);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - The users address informations whose id " + ID + " has been updated.\n", Color.WHITE);
            conn.close();
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Disonnected from the database.\n", Color.WHITE);

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateJTable() {

        for (int i = addressesTableModel.getRowCount() - 1; i >= 0; i--) {
            addressesTableModel.removeRow(i);
        }
        // Clear the address table.
        
        appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Address table has been cleared.\n", Color.WHITE);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Connected the database.\n", Color.WHITE);
            Statement stmnt = conn.createStatement();
            String query = "SELECT * FROM TBL_ADDRESSES";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                String ID = rs.getInt("ID") + "";
                String COUNTRY = rs.getString("COUNTRY");
                String CITY = rs.getString("CITY");
                String FULLADDRESS = rs.getString("FULLADDRESS");
                addressesTableModel.addRow(new Object[]{ID, COUNTRY, CITY, FULLADDRESS});
            }
            tbl_adminFrameTable.setModel(addressesTableModel);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Address table has been updated.\n", Color.WHITE);
            conn.close();
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Disonnected from the database.\n", Color.WHITE);

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - An SQL Exception occured!\n", Color.RED);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateJList() {
        personsListModel.clear();
        appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Persons list has been cleared.\n", Color.WHITE);
        for (int i = 0; i < getPersonsArrayList().size(); i++) {
            personsListModel.addElement(getPersonsArrayList().get(i));
        }
        lst_adminFramePersonList.setModel(personsListModel);
        appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Persons list has been updated.\n", Color.RED);
    }

    public void updateProfilePicOnAdminFrame() {
        int ID = Integer.parseInt(lbl_adminFrameID.getText());

        if (getProfileImagePathByID(ID).equals("default")) {
            if (getGenderByID(ID).equals("Male")) {
                lbl_adminFrameProfilePic.setIcon(iconDefaultMan);
            } else {
                lbl_adminFrameProfilePic.setIcon(iconDefaultWoman);
            }
        } else {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(getProfileImagePathByID(ID)));
            } catch (IOException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedImage newImg = scale(img, 150, 150);
            lbl_adminFrameProfilePic.setIcon(new ImageIcon(newImg));
        }
    }

    public void updateProfilePicOnUserFrame() {
        if (getProfileImagePathByID(CURRENT_ID).equals("default")) {
            if (getGenderByID(CURRENT_ID).equals("Male")) {
                lbl_userFrameProfilePic.setIcon(iconDefaultMan);
            } else {
                lbl_userFrameProfilePic.setIcon(iconDefaultWoman);
            }
        } else {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(getProfileImagePathByID(CURRENT_ID)));
            } catch (IOException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedImage newImg = scale(img, 127, 127);
            lbl_userFrameProfilePic.setIcon(new ImageIcon(newImg));
        }
    }

    public boolean isIdExistInTBL_ADDRESSES(int ID) {
        boolean isExist = false;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement statement = conn.createStatement();
            String query = "SELECT ID FROM TBL_ADDRESSES";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int tableID = resultSet.getInt("ID");
                if (tableID == ID) {
                    isExist = true;
                    break;
                }
            }

            conn.close();

            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    public ArrayList<Integer> getAllIDs() {
        ArrayList<Integer> IDlist = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT ID FROM TBL_PERSONELINFO ORDER BY ID";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                IDlist.add(rs.getInt("ID"));
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return IDlist;
    }

    public int getCurrentID(String mail) {
        int currentID = 0;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT ID FROM TBL_PERSONELINFO WHERE MAIL = '" + mail + "'";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                currentID = rs.getInt("ID");
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return currentID;
    }

    public int getIDByName(String nameSurname) {
        int ID = 0;
        String databaseNameSurname;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT ID, NAME, SURNAME FROM TBL_PERSONELINFO";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                databaseNameSurname = "";
                databaseNameSurname += rs.getString("NAME");
                databaseNameSurname += " " + rs.getString("SURNAME");

                if (databaseNameSurname.equals(nameSurname)) {
                    ID = rs.getInt("ID");
                }
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ID;
    }

    public String getProfileImagePathByID(int ID) {
        String filePath = "";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT PROFILEPIC FROM TBL_PERSONELINFO WHERE ID = " + ID;
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                filePath = rs.getString("PROFILEPIC");
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return filePath;
    }

    public String getGenderByID(int ID) {
        String gender = "";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT GENDER FROM TBL_PERSONELINFO WHERE ID = " + ID;
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                gender = rs.getString("GENDER");
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return gender;
    }

    public String getPasswordByID(int ID) {
        String password = "";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT PASSWORD FROM TBL_PERSONELINFO WHERE ID = " + ID;
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                password = rs.getString("PASSWORD");
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return password;
    }

    public String getNameSurnameByID(int ID) {
        String nameSurname = "";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT NAME, SURNAME FROM TBL_PERSONELINFO WHERE ID = " + ID;
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                nameSurname = rs.getString("NAME");
                nameSurname += " " + rs.getString("SURNAME");
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nameSurname;
    }

    public String[] getPersonelInformations(String nameSurname) {//ORDER: {ID, NAMESURNAME, MAIL, DATE, GENDER}

        String[] personelInformations = new String[5]; //ORDER: {ID, NAMESURNAME, MAIL, DATE, GENDER}
        int ID = getIDByName(nameSurname);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Connected the database.\n", Color.WHITE);
            Statement stmnt = conn.createStatement();
            String query = "SELECT NAME, SURNAME, MAIL, DATE, GENDER FROM TBL_PERSONELINFO WHERE ID = " + ID + "";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                personelInformations[0] = ID + "";
                personelInformations[1] = rs.getString("NAME") + " " + rs.getString("SURNAME");
                personelInformations[2] = rs.getString("MAIL");
                personelInformations[3] = rs.getString("DATE");
                personelInformations[4] = rs.getString("GENDER");
            }
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Personel informations has been colected from the database.\n", Color.WHITE);

            conn.close();
            appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Disonnected from the database.\n", Color.WHITE);

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return personelInformations;
    }

    public ArrayList<String> getPersonsArrayList() {

        ArrayList<String> personsList = new ArrayList<>();
        String nameSurname;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT NAME, SURNAME FROM TBL_PERSONELINFO";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                nameSurname = "";
                nameSurname += rs.getString("NAME");
                nameSurname += " " + rs.getString("SURNAME");
                personsList.add(nameSurname);
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personsList;
    }

    public int newID() {
        int newID = 0;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "SELECT ID FROM TBL_PERSONELINFO";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
                int biggestID = 0;
                if (biggestID < rs.getInt("ID")) {
                    biggestID = rs.getInt("ID") + 1;
                }
                newID = biggestID;
            }

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newID;
    }

    public boolean isMailExist(String mail) { //true gonderirse email veritabaninda var
        Connection conn = null;
        boolean isExist = false;

        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement statement = conn.createStatement();
            String query = "SELECT MAIL FROM TBL_PERSONELINFO";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String tableEmail = resultSet.getString("MAIL");
                if (tableEmail.equals(mail)) {
                    isExist = true;
                }
            }

            conn.close();

            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    public boolean isRememberMeSelected(String mail) { //kullanici giris yaparken rememberme sectiyse true gonderir
        boolean isSelected = false;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement statement = conn.createStatement();
            String query = "SELECT MAIL, REMEMBERME FROM TBL_PERSONELINFO";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String tableEmail = resultSet.getString("MAIL");
                if (tableEmail.equals(mail)) {
                    isSelected = resultSet.getBoolean("REMEMBERME");
                }
            }

            conn.close();

            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isSelected;
    }

    public void setRememberMeToDatabase(String mail) {

        boolean isRememberMeSelected = chcbx_rememberMe.isSelected();

        if (isMailExist(mail)) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
                Statement stmnt = conn.createStatement();
                String query = "UPDATE TBL_PERSONELINFO SET REMEMBERME = " + isRememberMeSelected + " WHERE MAIL = '" + mail + "'";
                stmnt.executeUpdate(query);
                conn.close();

            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            dlg_mailNotExist.setVisible(true);
            dlg_mailNotExist.setLocationRelativeTo(null);
        }
    }

    public boolean setPasswordToDatabase(int ID) {
        boolean isPassChanged = false;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "UPDATE TBL_PERSONELINFO SET PASSWORD = '" + pswrdfld_newPassword.getText() + "' WHERE ID = " + ID;
            stmnt.executeUpdate(query);
            isPassChanged = true;
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isPassChanged;
    }

    public void setProfilePicPathToDatabase(String imagePath) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
            Statement stmnt = conn.createStatement();
            String query = "UPDATE TBL_PERSONELINFO SET PROFILEPIC = '" + imagePath + "' WHERE ID = " + CURRENT_ID;
            stmnt.executeUpdate(query);
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void countdownTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                if (second > 0) {
                    second--;
                } else {
                    minute--;
                    second = 59;
                }

                //--------------------------------------------
                if (second / 10 < 1) {
                    lbl_timer.setText(minute + ":0" + second);
                } else {
                    lbl_timer.setText(minute + ":" + second);
                }
                //--------------------------------------------

                if (second == 0 && minute == 0) {
                    timer.stop();
                }
            }
        });
    }

    public void sendMail(String targetMail, int code) {
        
        String to = targetMail;
        // Recipient's email ID needs to be mentioned.

        String from = "WRITE_YOUR_MAIL_HERE";
        // Sender's email ID needs to be mentioned

        String host = "smtp.gmail.com";
        // Sending email from through gmails smtp

        Properties properties = System.getProperties();
        // Get system properties

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Setup mail server

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("WRITE_YOUR_MAIL_HERE", "WRITE_YOUR_PASSWORD_HERE");
                // Get the Session object and pass username and password
            }
        });
        
        session.setDebug(true);
        // Used to debug SMTP issues

        try {
            MimeMessage message = new MimeMessage(session);
            // Create a default MimeMessage object.

            message.setFrom(new InternetAddress(from));
            // Set From: header field of the header.

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set To: header field of the header.

            message.setSubject("Sifre Sifirlama Talebi");
            // Set Subject: header field

            message.setText("Address Manager uygulamasi icin sifre sifirlama talebi olusturdunuz. "
                    + "Asagidaki kodu kullanarak uygulamaya giris yapabilirsiniz. Giris yaptıktan sonra "
                    + "sifrenizi degistirmenizi tavsiye ederiz.\n\n\n" + code);
            // Set the actual message

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public File takeScreenShot(String place) {
        WebDriver driver = new FirefoxDriver();
        // Create new firefox driver.
        
        prgrsbr_userFrameMapLoading.setValue(25);
        
        driver.get("https://www.google.com/maps/place/" + place);
        // Set the drivers link to google map
        
        prgrsbr_userFrameMapLoading.setValue(45);
        
        waitForLoad(driver);
        
        prgrsbr_userFrameMapLoading.setValue(60);
        
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // Take screenshot of the google maps page.
        
        prgrsbr_userFrameMapLoading.setValue(75);
        try {
            FileHandler.copy(scrFile, new File("place.jpg"));
            // Save the screenshot to computer.
            
            prgrsbr_userFrameMapLoading.setValue(80);
        } catch (IOException ex) {
            System.out.println("Error! File is not saved.");
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        driver.quit();
        // Close the driver.
        
        return scrFile;
    }

    public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

    public BufferedImage cropImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        image = image.getSubimage(432, 0, 934, 654);
        // Crops the taken screenshots unnecessary parts.
        
        return image;
    }

    public BufferedImage scale(BufferedImage image, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = image.getWidth();
        int hh = image.getHeight();
        int[] ys = new int[height];
        for (y = 0; y < height; y++) {
            ys[y] = y * hh / height;
        }
        for (x = 0; x < width; x++) {
            int newX = x * ww / width;
            for (y = 0; y < height; y++) {
                int col = image.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

    public void reScaleSlider() {
        BufferedImage scaledLineImage = scale(lineImage, lineImage.getWidth(), (int) (sldr_pictureFrameThicknessSlider.getValue() * 3.5));
        ImageIcon scaledCurvedLineIcon = new ImageIcon(scaledLineImage);
        lbl_pictureFrameCurvedLine.setIcon(scaledCurvedLineIcon);
    }

    public String getCurrentDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, Color.BLACK, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Calibri");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    public void backupFromDatabase(String filePath) {
        lbl_downloadInformator.setText("All the database informations are downloading...");
        String info;
        Connection conn = null;
        ArrayList<Integer> IDlist = getAllIDs();
        
        prgrsbr_backupProgress.setMaximum(IDlist.size() * 2);
        
        File file = new File(filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter output = new BufferedWriter(writer);

        for (int i = 0; i < IDlist.size(); i++) {
            info = "";
            try {
                conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DB_PersonelInformations", "sa", "as");
                appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Connected the database.\n", Color.WHITE);
                Statement stmnt = conn.createStatement();
                String query = "SELECT * FROM TBL_PERSONELINFO WHERE ID = " + IDlist.get(i);
                ResultSet rs = stmnt.executeQuery(query);

                while (rs.next()) {
                    info += "\nUSER_ID: " + rs.getInt("ID") + "\n";
                    info += "\tNAME_SURNAME: " + rs.getString("NAME") + " " + rs.getString("SURNAME") + "\n";
                    info += "\tMAIL: " + rs.getString("MAIL") + "\n";
                    info += "\tPASSWORD: " + rs.getString("PASSWORD") + "\n";
                    info += "\tDATE: " + rs.getString("DATE") + "\n";
                    info += "\tGENDER: " + rs.getString("GENDER") + "\n";
                    info += "\tREMEMBER_ME_STATUE: " + rs.getBoolean("REMEMBERME") + "\n";
                    info += "\tPROFILE_PIC_PATH: " + rs.getString("PROFILEPIC") + "\n";
                }
                prgrsbr_backupProgress.setValue(prgrsbr_backupProgress.getValue() + 1);

                query = "SELECT * FROM TBL_ADDRESSES WHERE ID = " + IDlist.get(i);
                rs = stmnt.executeQuery(query);

                while (rs.next()) {
                    info += "\tCOUNTRY: " + rs.getString("COUNTRY") + "\n";
                    info += "\tCITY: " + rs.getString("CITY") + "\n";
                    info += "\tFULL_ADDRESS: " + rs.getString("FULLADDRESS") + "\n";
                }
                prgrsbr_backupProgress.setValue(prgrsbr_backupProgress.getValue() + 1);
                
                try {
                    output.write(info);
                } catch (IOException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

                appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Database informations are collecting...\n", Color.WHITE);
                conn.close();
                appendToPane(txtpn_adminFrameLog, getCurrentDateAndTime() + " - Disonnected from the database.\n", Color.WHITE);

            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (conn != null && !conn.isClosed()) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //##########################################################################
    //                                METHODS ENDS
    //##########################################################################
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button btn_accountRegisteredButton;
    private java.awt.Button btn_adminFrameDeleteUser;
    private java.awt.Button btn_adminFrameDeleteUsersAddress;
    private java.awt.Button btn_adminFrameLogOut;
    private java.awt.Button btn_applyChangesToDatabase;
    private java.awt.Button btn_backupToComputer;
    private java.awt.Button btn_changeThePassword;
    private java.awt.Button btn_forgotPassCodeButton;
    private java.awt.Button btn_login;
    private java.awt.Button btn_mailNotExistWarningButton;
    private java.awt.Button btn_mailWarningButton;
    private java.awt.Button btn_passChangedButton;
    private java.awt.Button btn_pictureFrameBackToUserFrame;
    private java.awt.Button btn_pictureFrameGetPicFromDatabase;
    private java.awt.Button btn_pictureFrameOpenColorChooser;
    private java.awt.Button btn_pictureFrameOpenNewImage;
    private java.awt.Button btn_pictureFrameSetPicToDatabase;
    private java.awt.Button btn_register;
    private java.awt.Button btn_registerNotFilledButton;
    private java.awt.Button btn_savedToDatabaseOK;
    private java.awt.Button btn_updateTheTable;
    private java.awt.Button btn_updatedToDatabaseOK;
    private java.awt.Button btn_userFrameAdjustPicture;
    private java.awt.Button btn_userFrameChangePassword;
    private java.awt.Button btn_userFrameUploadPic;
    private java.awt.Button btn_userLogOut;
    private java.awt.Button btn_userSaveToDatabase;
    private java.awt.Button btn_userUpdateToDatabase;
    private java.awt.Button btn_wrongPasswordButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chcbx_rememberMe;
    private javax.swing.JComboBox<String> cmbbox_userCities;
    private javax.swing.JComboBox<String> cmbbox_userCountries;
    private javax.swing.JDialog dlg_accountRegistered;
    private javax.swing.JDialog dlg_areasNotFilled;
    private javax.swing.JDialog dlg_backupToComputer;
    private javax.swing.JDialog dlg_changePassword;
    private javax.swing.JDialog dlg_forgotPassword;
    private javax.swing.JDialog dlg_mailNotExist;
    private javax.swing.JDialog dlg_mailNotUnique;
    private javax.swing.JDialog dlg_passwordChanged;
    private javax.swing.JDialog dlg_savedToDatabase;
    private javax.swing.JDialog dlg_updatedToDatabase;
    private javax.swing.JDialog dlg_wrongPassword;
    private javax.swing.JFileChooser flchsr_fileChooser;
    private javax.swing.JFormattedTextField frmtd_forgotPassCode;
    private javax.swing.JFormattedTextField frmttxtfld_registerDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JFrame jfrm_adminFrame;
    private javax.swing.JFrame jfrm_profilePicAdjustment;
    private javax.swing.JFrame jfrm_userFrame;
    private javax.swing.JLabel lbl_accountRegistered;
    private javax.swing.JLabel lbl_adSoyad;
    private javax.swing.JLabel lbl_adminAddressInformations;
    private javax.swing.JLabel lbl_adminFrameAddressSearchIcon;
    private javax.swing.JLabel lbl_adminFrameDate;
    private javax.swing.JLabel lbl_adminFrameGender;
    private javax.swing.JLabel lbl_adminFrameID;
    private javax.swing.JLabel lbl_adminFrameMail;
    private javax.swing.JLabel lbl_adminFrameNameSurname;
    private javax.swing.JLabel lbl_adminFramePersonSearchIcon;
    private javax.swing.JLabel lbl_adminFrameProfilePic;
    private javax.swing.JLabel lbl_adminPersonelInformations;
    private javax.swing.JLabel lbl_backup;
    private javax.swing.JLabel lbl_changePasswordPic;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_dontHaveAccount;
    private javax.swing.JLabel lbl_downloadInformator;
    private javax.swing.JLabel lbl_enterNewPassword;
    private javax.swing.JLabel lbl_enterOldPassword;
    private javax.swing.JLabel lbl_errorChangePassword;
    private javax.swing.JLabel lbl_forgotPassLabel1;
    private javax.swing.JLabel lbl_forgottenPassword;
    private javax.swing.JLabel lbl_gender;
    private javax.swing.JLabel lbl_guideMap;
    private javax.swing.JLabel lbl_loginFrameDateError;
    private javax.swing.JLabel lbl_loginFrameMailError;
    private javax.swing.JLabel lbl_loginFrameNameSurnameError;
    private javax.swing.JLabel lbl_loginFramePasswordError;
    private javax.swing.JLabel lbl_loginMail;
    private javax.swing.JLabel lbl_loginPassword;
    private javax.swing.JLabel lbl_mail;
    private javax.swing.JLabel lbl_mailNotExistWarning;
    private javax.swing.JLabel lbl_mailWarning;
    private javax.swing.JLabel lbl_map;
    private javax.swing.JLabel lbl_mapLabel;
    private javax.swing.JLabel lbl_passChangedLabel;
    private javax.swing.JLabel lbl_password;
    private javax.swing.JLabel lbl_pictureFrameCurvedLine;
    private javax.swing.JLabel lbl_registerNotFilled;
    private javax.swing.JLabel lbl_savedToDatabase;
    private javax.swing.JLabel lbl_timer;
    private javax.swing.JLabel lbl_updatedToDatabase;
    private javax.swing.JLabel lbl_userAddressInformation;
    private javax.swing.JLabel lbl_userFrameProfilePic;
    private javax.swing.JLabel lbl_wrongPassword;
    private javax.swing.JList<String> lst_adminFramePersonList;
    private javax.swing.JMenuItem mnTm_adjustPic;
    private javax.swing.JMenuItem mnTm_adminFrameLogOut;
    private javax.swing.JMenuItem mnTm_applyChangesDatabase;
    private javax.swing.JMenuItem mnTm_backupToComputer;
    private javax.swing.JMenuItem mnTm_changeColorWorldMap;
    private javax.swing.JMenuItem mnTm_changePass;
    private javax.swing.JMenuItem mnTm_clearFullAddress;
    private javax.swing.JMenuItem mnTm_clearLogArea;
    private javax.swing.JMenuItem mnTm_deleteUser;
    private javax.swing.JMenuItem mnTm_deleteUsersAddressInformations;
    private javax.swing.JMenuItem mnTm_displayWordlMapPopUp;
    private javax.swing.JMenuItem mnTm_displayWorldMap;
    private javax.swing.JCheckBoxMenuItem mnTm_dontDisplayMap;
    private javax.swing.JMenuItem mnTm_updateTable;
    private javax.swing.JMenuItem mnTm_uploadPic;
    private javax.swing.JMenuItem mnTm_userFrameLogOut;
    private javax.swing.JMenu mn_adminFrameApplication;
    private javax.swing.JMenu mn_adminFrameFile;
    private javax.swing.JMenu mn_userFrameApplication;
    private javax.swing.JMenu mn_userFrameMap;
    private javax.swing.JMenuBar mnbr_adminFrameMenuBar;
    private javax.swing.JMenuBar mnbr_userFrameMenuBar;
    private javax.swing.JPanel pnl_accountRegisteredBackground;
    private javax.swing.JPanel pnl_backupComputerBackground;
    private javax.swing.JPanel pnl_color1;
    private javax.swing.JPanel pnl_color2;
    private javax.swing.JPanel pnl_color3;
    private javax.swing.JPanel pnl_color4;
    private javax.swing.JPanel pnl_color5;
    private javax.swing.JPanel pnl_forgotPassBackground;
    private javax.swing.JPanel pnl_login;
    private javax.swing.JPanel pnl_loginFrameBackPanel;
    private javax.swing.JPanel pnl_mailNotExistWarningBackground;
    private javax.swing.JPanel pnl_mailWarningBackground;
    private javax.swing.JPanel pnl_passChangedBackground;
    private javax.swing.JPanel pnl_pictureFrameBackground;
    private javax.swing.JPanel pnl_pictureFrameColorsPanel;
    private javax.swing.JPanel pnl_pictureFramePicPanel;
    private javax.swing.JPanel pnl_pictureFrameToolsPanel;
    private javax.swing.JPanel pnl_register;
    private javax.swing.JPanel pnl_registerNotFilledBackground;
    private javax.swing.JPanel pnl_rightPanel;
    private javax.swing.JPanel pnl_userFrameLeftPanel;
    private javax.swing.JPanel pnl_userFrameLeftPanel1;
    private javax.swing.JPanel pnl_userFrameRightPanel;
    private javax.swing.JPanel pnl_userFrameRightPanel1;
    private javax.swing.JPanel pnl_wrongPasswordBackground;
    private javax.swing.JPopupMenu pppmn_userFrameMapPopUp;
    private javax.swing.JProgressBar prgrsbr_backupProgress;
    private javax.swing.JProgressBar prgrsbr_userFrameMapLoading;
    private javax.swing.JPasswordField pswrdfld_loginPassword;
    private javax.swing.JPasswordField pswrdfld_newPassword;
    private javax.swing.JPasswordField pswrdfld_oldPassword;
    private javax.swing.JPasswordField pswrdfld_registerPassword;
    private javax.swing.JRadioButton rdbtn_female;
    private javax.swing.JRadioButton rdbtn_male;
    private javax.swing.JScrollPane scrlpn_leftPanel;
    private javax.swing.JScrollPane scrlpn_map;
    private javax.swing.JSlider sldr_pictureFrameThicknessSlider;
    private javax.swing.JSplitPane spltpn_adminFrame;
    private javax.swing.JSplitPane spltpn_userFrameSplitPane;
    private javax.swing.JSeparator sprtr_loginFrameDate;
    private javax.swing.JSeparator sprtr_loginFrameMail;
    private javax.swing.JSeparator sprtr_loginFrameNameSurname;
    private javax.swing.JSeparator sprtr_loginFramePassword;
    private javax.swing.JSeparator sprtr_userFrameSeperator;
    private javax.swing.JSeparator sprtr_userFrameSeperator1;
    private javax.swing.JTabbedPane tbdpn_tabbedPane;
    private javax.swing.JTable tbl_adminFrameTable;
    private javax.swing.JTextArea txtarea_userFullAddress;
    private javax.swing.JTextField txtfld_adminFrameAddressSearchBox;
    private javax.swing.JTextField txtfld_adminFramePersonSearchBox;
    private javax.swing.JTextField txtfld_forgotPassMail;
    private javax.swing.JTextField txtfld_loginMail;
    private javax.swing.JTextField txtfld_registerMail;
    private javax.swing.JTextField txtfld_registerNameSurname;
    private javax.swing.JTextPane txtpn_adminFrameLog;
    // End of variables declaration//GEN-END:variables
}
