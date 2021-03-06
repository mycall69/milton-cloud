package org.spliffy.sync.app;

import io.milton.event.Event;
import io.milton.event.EventListener;
import io.milton.sync.event.DownloadSyncEvent;
import io.milton.sync.event.FinishedSyncEvent;
import io.milton.sync.event.TransferProgressEvent;
import io.milton.sync.event.UploadSyncEvent;
import javax.swing.SwingUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//com.ettrema.cloudsync//main//EN", autostore = false)
@TopComponent.Description(preferredID = "mainTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "org.spliffy.sync.MainTopComponent2")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "SpliffySync", preferredID = "mainTopComponent")
@Messages({
    "CTL_mainAction=main",
    "CTL_mainTopComponent=main Window",
    "HINT_mainTopComponent=This is a main window"
})
public final class MainTopComponent2 extends TopComponent {

    private final StatusUpdater statusUpdater = new StatusUpdater();
    private String currentFile;
    private int currentProgress;

    public MainTopComponent2() {
        System.out.println("--- STARTING SPLIFFY SYNC ---");

        try {
            initComponents();
            setName("SpliffySync");
            setToolTipText("SpliffySync");
            putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
            putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

            SyncFactory moduleFactory = SyncFactory.get();
            StatusEventListener eventListener = new StatusEventListener();
            moduleFactory.getEventManager().registerEventListener(eventListener, DownloadSyncEvent.class);
            moduleFactory.getEventManager().registerEventListener(eventListener, UploadSyncEvent.class);
            moduleFactory.getEventManager().registerEventListener(eventListener, FinishedSyncEvent.class);
            moduleFactory.getEventManager().registerEventListener(eventListener, TransferProgressEvent.class);

            System.out.println("initialied: " + moduleFactory);
            moduleFactory.startAll();
        } catch (Exception e) {
//            JOptionPane.showInputDialog("Couldnt start: " + e.toString());
//            System.exit(9);
            e.printStackTrace();
        }


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        pnlOuter = new javax.swing.JPanel();
        pnlInner = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        lblComment = new javax.swing.JLabel();
        lblHeading2 = new javax.swing.JLabel();
        lblCurrentFile = new javax.swing.JLabel();
        lblHeading5 = new javax.swing.JLabel();
        progCurrent = new javax.swing.JProgressBar();
        jSeparator1 = new javax.swing.JSeparator();

        setLayout(new java.awt.BorderLayout());

        pnlOuter.setBackground(new java.awt.Color(255, 255, 255));

        pnlInner.setBackground(new java.awt.Color(255, 255, 255));

        lblStatus.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/spliffy/sync/app/tick.jpg"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(lblStatus, org.openide.util.NbBundle.getMessage(MainTopComponent2.class, "MainTopComponent2.lblStatus.text_1")); // NOI18N

        lblComment.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(lblComment, org.openide.util.NbBundle.getMessage(MainTopComponent2.class, "MainTopComponent2.lblComment.text_1")); // NOI18N

        lblHeading2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblHeading2.setForeground(new java.awt.Color(127, 176, 50));
        lblHeading2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(lblHeading2, org.openide.util.NbBundle.getMessage(MainTopComponent2.class, "MainTopComponent2.lblHeading2.text_1")); // NOI18N

        lblCurrentFile.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(lblCurrentFile, org.openide.util.NbBundle.getMessage(MainTopComponent2.class, "MainTopComponent2.lblCurrentFile.text_1")); // NOI18N

        lblHeading5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblHeading5.setForeground(new java.awt.Color(127, 176, 50));
        lblHeading5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(lblHeading5, org.openide.util.NbBundle.getMessage(MainTopComponent2.class, "MainTopComponent2.lblHeading5.text_1")); // NOI18N

        javax.swing.GroupLayout pnlInnerLayout = new javax.swing.GroupLayout(pnlInner);
        pnlInner.setLayout(pnlInnerLayout);
        pnlInnerLayout.setHorizontalGroup(
            pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblComment, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
            .addGroup(pnlInnerLayout.createSequentialGroup()
                .addGroup(pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlInnerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblHeading5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblHeading2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progCurrent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCurrentFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE))
        );
        pnlInnerLayout.setVerticalGroup(
            pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInnerLayout.createSequentialGroup()
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblComment, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHeading2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCurrentFile, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInnerLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHeading5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInnerLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(progCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnlInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlInnerLayout.createSequentialGroup()
                    .addGap(141, 141, 141)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(143, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlOuterLayout = new javax.swing.GroupLayout(pnlOuter);
        pnlOuter.setLayout(pnlOuterLayout);
        pnlOuterLayout.setHorizontalGroup(
            pnlOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOuterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlOuterLayout.setVerticalGroup(
            pnlOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOuterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(pnlOuter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblComment;
    private javax.swing.JLabel lblCurrentFile;
    private javax.swing.JLabel lblHeading2;
    private javax.swing.JLabel lblHeading5;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel pnlInner;
    private javax.swing.JPanel pnlOuter;
    private javax.swing.JProgressBar progCurrent;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    @Override
    public boolean canClose() {
        return false;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private class StatusEventListener implements EventListener {

        @Override
        public void onEvent(Event e) {
            if (e instanceof DownloadSyncEvent) {
                DownloadSyncEvent de = (DownloadSyncEvent) e;
                currentFile = de.getLocalFile().getAbsolutePath();
                SwingUtilities.invokeLater(statusUpdater);
            }
            if (e instanceof UploadSyncEvent) {
                UploadSyncEvent ue = (UploadSyncEvent) e;
                currentFile = ue.getLocalFile().getAbsolutePath();
                SwingUtilities.invokeLater(statusUpdater);
            }
            if (e instanceof TransferProgressEvent) {
                TransferProgressEvent te = (TransferProgressEvent) e;
                currentProgress = te.getPercent();
                SwingUtilities.invokeLater(statusUpdater);
            }
            if (e instanceof FinishedSyncEvent) {
                currentFile = null;
                currentProgress = 0;
                SwingUtilities.invokeLater(statusUpdater);
            }
        }
    }

    private class StatusUpdater implements Runnable {

        @Override
        public void run() {
            lblCurrentFile.setText(currentFile);
            progCurrent.setValue(currentProgress);
        }
    }
}
