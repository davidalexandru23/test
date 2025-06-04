import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * Simple Java Swing application that retrieves current weather data
 * from the Open-Meteo API using latitude and longitude coordinates.
 */
public class WeatherApp {
    private JFrame frame;
    private JTextField latField;
    private JTextField lonField;
    private JLabel resultLabel;

    public WeatherApp() {
        frame = new JFrame("Weather App");
        latField = new JTextField(8);
        lonField = new JTextField(8);
        JButton button = new JButton("Get Weather");
        resultLabel = new JLabel("Enter coordinates and press the button.");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Latitude:"));
        inputPanel.add(latField);
        inputPanel.add(new JLabel("Longitude:"));
        inputPanel.add(lonField);
        inputPanel.add(button);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(resultLabel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });
    }

    private void fetchWeather() {
        String lat = latField.getText().trim();
        String lon = lonField.getText().trim();
        if (lat.isEmpty() || lon.isEmpty()) {
            resultLabel.setText("Please enter both latitude and longitude.");
            return;
        }

        try {
            double latVal = Double.parseDouble(lat);
            double lonVal = Double.parseDouble(lon);

            String urlStr = String.format(
                java.util.Locale.US,
                "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true",
                latVal,
                lonVal);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "WeatherApp/1.0");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                resultLabel.setText("HTTP error: " + conn.getResponseCode());
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String temperature = parseTemperature(response.toString());
            resultLabel.setText("Temperature: " + temperature + " °C");
        } catch (NumberFormatException nfe) {
            resultLabel.setText("Invalid coordinate format.");
        } catch (Exception ex) {
            resultLabel.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Extracts the temperature value from a JSON response string.
     */
    private String parseTemperature(String json) {
        String token = "\"temperature\":";
        int index = json.indexOf(token);
        if (index == -1) {
            return "N/A";
        }
        int start = index + token.length();
        int end = json.indexOf(',', start);
        if (end == -1) {
            end = json.indexOf('}', start);
        }
        if (end == -1 || start >= end) {
            return "N/A";
        }
        return json.substring(start, end).trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherApp();
            }
        });
    }
}
