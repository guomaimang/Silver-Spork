package comp4342.grp15.gem.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import comp4342.grp15.gem.Model.UploadMeta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UploadProcessor {

    // return json
    public static String getProcess(String jsonString){

        try {
            Gson gson = new Gson();
            UploadMeta uploadMeta = gson.fromJson(jsonString, UploadMeta.class);
            // DBController.savePost(uploadMeta);
        }catch (Exception e){
            return """
                    {"statue":"Fail", "commit":"Unknown Error", "code":"-1",
                    }
                    """;
        }
        return """
                    {"statue":"Success", "commit":"No Error", "code":"0",
                    }
                    """;
    }

}
