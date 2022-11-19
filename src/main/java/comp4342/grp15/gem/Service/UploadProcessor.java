package comp4342.grp15.gem.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UploadProcessor {

    // return json
    public static String getProcess(String jsonString){

        Type listType = new TypeToken<List<PostMeta>>(){}.getType();
        Gson gson = new Gson();
        List<PostMeta> lPostMetas = gson.fromJson(json, listType);
        ArrayList<PostMeta> aPostMetas = new ArrayList<>(lPostMetas);

    }

}
