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
            String urlStr = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                URLEncoder.encode(lat, "UTF-8"),
                URLEncoder.encode(lon, "UTF-8"));
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String temperature = parseTemperature(response.toString());
            resultLabel.setText("Temperature: " + temperature + " °C");
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
