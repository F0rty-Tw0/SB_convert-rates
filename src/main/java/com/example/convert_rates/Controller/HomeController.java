package com.example.convert_rates.Controller;

import com.example.convert_rates.Model.Rates.RatesModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.io.IOException;
import java.net.URL;
import org.json.*;
import java.util.*;

@Controller
public class HomeController {
    private JSONArray currencyArray;
    private List<String> codeList;

    public void scanWeb() throws IOException {
        // Instantiating the URL class
        URL url = new URL("https://www.nationalbanken.dk/_vti_bin/DN/DataService.svc/CurrencyRatesXML?lang=da");
        // Retrieving the contents of the specified page
        Scanner sc = new Scanner(url.openStream());
        // Instantiating the StringBuffer class to hold the result
        StringBuffer sb = new StringBuffer();

        while (sc.hasNext()) {
            sb.append(sc.next());
            sb.append(" ");
            try {
            } catch (Exception e) {
            }
        }
        sc.close();

        // Converting the XML to JSON object
        JSONObject json = XML.toJSONObject(sb.toString()).getJSONObject("exchangerates").getJSONObject("dailyrates");
        // Adding the missing DKK code for flexibilty conversion
        JSONObject dkk = new JSONObject("{\"code\":\"DKK\",\"rate\":\"100\",\"desc\":\"Danske Krona\"}");
        // Setting our array from the JSON by currency
        currencyArray = json.getJSONArray("currency");
        // Adding DKK to the array
        currencyArray.put(dkk);

        // Making a list of codes
        codeList = new ArrayList<>();

        for (int i = 0; i < currencyArray.length(); i++) {
            codeList.add(currencyArray.getJSONObject(i).getString("code") + " - "
                    + currencyArray.getJSONObject(i).getString("desc"));
        }
    }

    public RatesModel returnConversion(JSONArray array, String selectedFirstCode, String selectedSecondCode,
            RatesModel myRates, double amount) {
        // Looping through the array
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = null;
            try {
                obj = array.getJSONObject(i);
                if (obj.getString("code").equals(selectedFirstCode)) {
                    // Selected rate with dot instead of comma
                    String selectedRate = obj.getString("rate").replaceAll(",", ".");
                    String selectedFirstDescription = obj.getString("desc");
                    // Converting string to double
                    double firstRate = Double.parseDouble(selectedRate);

                    // Setting the object
                    myRates.setFirstCode(selectedFirstCode);
                    myRates.setFirstRate(firstRate);
                    myRates.setFirstDescription(selectedFirstDescription);
                    myRates.setAmount(amount);
                }
                if (obj.getString("code").equals(selectedSecondCode)) {
                    // Selected rate with dot instead of comma
                    String selectedRate = obj.getString("rate").replaceAll(",", ".");
                    String selectedSecondDescription = obj.getString("desc");
                    // Converting string to double
                    double secondRate = Double.parseDouble(selectedRate);

                    // Setting the object
                    myRates.setSecondCode(selectedSecondCode);
                    myRates.setSecondDescription(selectedSecondDescription);
                    myRates.setSecondRate(secondRate);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return myRates;
    }

    @GetMapping("/")
    public String Index(Model model) throws IOException {
        scanWeb();
        RatesModel myRates = new RatesModel();
        model.addAttribute("codeList", codeList);
        model.addAttribute("myRates", myRates);
        return "home/index";
    }

    @PostMapping("/")
    public String submitForm(@ModelAttribute("myRates") RatesModel myRates) throws IOException {
        scanWeb();
        // Formating the input
        String selectedFirstCode = myRates.getFirstCode().toUpperCase().replaceAll(",", "").substring(0,
                myRates.getFirstCode().indexOf(' '));
        // Formating the input
        String selectedSecondCode = myRates.getSecondCode().toUpperCase().replaceAll(",", "").substring(0,
                myRates.getSecondCode().indexOf(' '));
        double amount = myRates.getAmount();
        returnConversion(currencyArray, selectedFirstCode, selectedSecondCode, myRates, amount);
        return "data/index";
    }
}
