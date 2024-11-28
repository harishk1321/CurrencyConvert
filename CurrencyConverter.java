   import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get base currency and amount from user
        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();
        System.out.print("Enter amount in " + baseCurrency + ": ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        // Get target currency from user
        System.out.print("Enter target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        try {
            // Fetch exchange rate and perform conversion
            double rate = getExchangeRate(baseCurrency, targetCurrency);
            if (rate != -1) {
                double convertedAmount = amount * rate;
                System.out.printf("%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
            } else {
                System.out.println("Conversion failed. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    // Method to get the exchange rate from the API
    public static double getExchangeRate(String baseCurrency, String targetCurrency) {
        double exchangeRate = -1;

        try {
            // Build the URL for the API request
            String urlStr = API_URL + baseCurrency;
            URL url = new URL(urlStr);

            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Check if the connection is successful
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) { // HTTP OK
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                // Close connections
                in.close();
                conn.disconnect();

                // Parse JSON response
                 JSONObject jsonResponse = new JSONObject(content.toString());
                JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");
                exchangeRate = conversionRates.getDouble(targetCurrency);

            } else {
                System.out.println("Error: Unable to fetch exchange rate. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
        }

        return exchangeRate;
    }
}


