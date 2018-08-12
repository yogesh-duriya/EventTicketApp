package com.endive.eventplanner.util;

import com.endive.eventplanner.pojo.ColorSizeListPojo;
import com.endive.eventplanner.pojo.PackageMerchandiseDetailPojo;
import com.endive.eventplanner.pojo.PersonTicketDetailPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 12/15/2017.
 */

public class GenerateJsonObject {

    public static JSONObject getJsonObject(ArrayList<PersonTicketDetailPojo> arr) {
        JSONArray userArr = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject user_obj = new JSONObject();
            try {
                user_obj.put("name", arr.get(i).getName());
                user_obj.put("gender", arr.get(i).getGender());
                user_obj.put("age", arr.get(i).getAge());
//                user_obj.put("dob_on_ticket", "1989-01-26");
                user_obj.put("seat_number", arr.get(i).getSeat());
                if(arr.get(i).getPackageData() != null) {
                    JSONArray productArr = new JSONArray();
                    ArrayList<PackageMerchandiseDetailPojo> arrPackage = arr.get(i).getPackageData().getPackage_detail().getPackage_merchandise_detail();
                    for (int j = 0; j < arrPackage.size(); j++) {
                        JSONObject product_obj = new JSONObject();
                        product_obj.put("name", arrPackage.get(j).getMerchandise_detail().getName());
                        product_obj.put("package_merchandise_id", arrPackage.get(j).getPackage_merchandise_id());
                        ArrayList<ColorSizeListPojo> color = arrPackage.get(j).getMerchandise_detail().getMerchandise_property_detail().getColor();
                        for (int k = 0; k < color.size(); k++) {
                            if (color.get(k).isSelected()) {
                                product_obj.put("color", color.get(k).getColor_name());
                                product_obj.put("color_code", "#"+color.get(k).getColor());
                                break;
                            }
                        }

                        ArrayList<ColorSizeListPojo> size = arrPackage.get(j).getMerchandise_detail().getMerchandise_property_detail().getSize();
                        for (int k = 0; k < size.size(); k++) {
                            if (size.get(k).isSelected()) {
                                product_obj.put("size", size.get(k).getSize());
                            }
                        }

                        productArr.put(product_obj);
                    }
                    user_obj.put("product_array", productArr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            userArr.put(user_obj);
        }
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("user_array", userArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userObj;
    }
}
