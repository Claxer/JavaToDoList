import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        // Start the application on the Swing event thread
        SwingUtilities.invokeLater(() -> {

            try {
                // Use the system's default appearance
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );

            } catch (Exception e) {
                System.out.println("Unable to set application theme.");
            }

            // Launch TaskFlow
            TaskFlowGUI application = new TaskFlowGUI();
            application.setVisible(true);
        });
    }
}
