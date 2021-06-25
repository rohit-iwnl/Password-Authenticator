package passwordauthen;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class Fast2sms {

    public static int sendSms(String number)
    {
        Random rand = new Random();
        int otp = 0;
        try
        {
              otp = rand.nextInt(999999);
            String message = "Your OTP for the current session of Password Authenticator is "+String.valueOf(otp);
            String apiKey="WjRIKla06VM3SuGqCbk8otBwfcLvPH7NhxmiFn5AzryQT41dEOHvJ3K7WXpVzEjR9NTI2ihcUyw6kBCQ";
            String sendId="FSTSMS";
            message= URLEncoder.encode(message, "UTF-8");
            String language="english";

            String route="p";


            String myUrl="https://www.fast2sms.com/dev/bulk?authorization="+apiKey+"&sender_id="+sendId+"&message="+message+"&language="+language+"&route="+route+"&numbers="+number;
            URL url=new URL(myUrl);

            HttpsURLConnection con=(HttpsURLConnection)url.openConnection();


            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("cache-control", "no-cache");
            int code=con.getResponseCode();

            StringBuffer response=new StringBuffer();

            BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));

            while(true)
            {
                String line=br.readLine();
                if(line==null)
                {
                    break;
                }
                response.append(line);
            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return otp;
    }

}