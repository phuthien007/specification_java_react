package com.example.testjson;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonTransform {

    public static String toSQL(String s) {
        String key = s.substring(s.indexOf("\"") + 1, s.indexOf("\"", s.indexOf("\"") + 1));
        return _toSQL(s, key);
    }

    /*
    * hoat dong voi cac toan tu: !, !=, >, <, <=, >=
    * voi cac dieu kien: and, or, not
    * bat dau phai co dang object json: {...}
    * format thong thuong: {
                              "!": {
                                "and": [
                                  {
                                    "!=": [
                                      {
                                        "var": "slider"
                                      },
                                      35
                                    ]
                                  }
                                ]
                              }
                            }
    * */
    private static String _toSQL(String s, String key) {
        String result = "";
        String k = key;
        if (key.equals("!")) {
            s = (new JSONObject(s)).get("!").toString();
            System.out.println(s);
            k = s.substring(s.indexOf("\"") + 1, s.indexOf("\"", s.indexOf("\"") + 1));
            result += " not " + _toSQL(s, k);
            return result;
        }
        JSONArray jsonArray = new JSONObject(s).getJSONArray(key);
        result += " ( ";
        for (int i = 0; i < jsonArray.length(); i++) {
            String obj = jsonArray.get(i).toString();
            k = obj.substring(obj.indexOf("\"") + 1, obj.indexOf("\"", obj.indexOf("\"") + 1));
            JSONArray arr = null;
            switch (k) {
                case "!":
                    System.out.println("PROCESS IN !");
                    obj = (new JSONObject(obj)).get("!").toString();
                    System.out.println("Here: " + obj);
                    k = obj.substring(obj.indexOf("\"") + 1, obj.indexOf("\"", obj.indexOf("\"") + 1));
                    System.out.println(k);
                    result += " not " + _toSQL(obj, k) + " " + key + " ";
                    break;

                case "==":
                    System.out.println("PROCESS IN ==");
                    arr = new JSONObject(obj).getJSONArray(k);
                    for (int j = 0; j < arr.length(); j += 2) {
                        result += new JSONObject(arr.get(j).toString()).get("var") + " = '" + arr.get(j + 1) + "' ";
                        if (j != arr.length()) {
                            result += key + " ";
                        }
                    }
                    break;

                case ">=":
                    System.out.println("PROCESS IN >=");
                    arr = new JSONObject(obj).getJSONArray(k);
                    for (int j = 0; j < arr.length(); j += 2) {
                        result += "" + new JSONObject(arr.get(j).toString()).get("var") + " >= '" + arr.get(j + 1) + "' ";
                        if (j != arr.length()) {
                            result += key + " ";
                        }
                    }
                    break;

                case ">":
                    System.out.println("PROCESS IN >");
                    arr = new JSONObject(obj).getJSONArray(k);
                    for (int j = 0; j < arr.length(); j += 2) {
                        result += "" + new JSONObject(arr.get(j).toString()).get("var") + " > '" + arr.get(j + 1) + "' ";
                        if (j != arr.length()) {
                            result += key + " ";
                        }
                    }
                    break;

                case "<=":
                    System.out.println("PROCESS IN <=");
                    arr = new JSONObject(obj).getJSONArray(k);
                    for (int j = 0; j < arr.length(); j += 2) {
                        result += "" + new JSONObject(arr.get(j).toString()).get("var") + " <= '" + arr.get(j + 1) + "' ";
                        if (j != arr.length()) {
                            result += key + " ";
                        }
                    }
                    break;

                case "<":
                    System.out.println("PROCESS IN <");
                    arr = new JSONObject(obj).getJSONArray(k);
                    for (int j = 0; j < arr.length(); j += 2) {
                        result += "" + new JSONObject(arr.get(j).toString()).get("var") + " < '" + arr.get(j + 1) + "' ";
                        if (j != arr.length()) {
                            result += key + " ";
                        }
                    }
                    break;

                case "!=":
                    System.out.println("PROCESS IN !=");
                    arr = new JSONObject(obj).getJSONArray(k);
                    for (int j = 0; j < arr.length(); j += 2) {
                        result += "" + new JSONObject(arr.get(j).toString()).get("var") + " != '" + arr.get(j + 1) + "' ";
                        if (j != arr.length()) {
                            result += key + " ";
                        }
                    }
                    break;

                case "in":
                    System.out.println("PROCESS IN in");
                    arr = new JSONObject(obj).getJSONArray(k);
                    try {
                        result += "" + new JSONObject(arr.get(0).toString()).get("var") + " in (";
                        for (int j = 0; j < new JSONArray(arr.get(1).toString()).length(); j++) {
                            result += "'" + new JSONArray(arr.get(1).toString()).get(j) + "' ";
                            if (j + 1 == new JSONArray(arr.get(1).toString()).length()) {
                                result += " ) " + key;
                            } else result += ", ";
//                        System.out.println(new JSONArray(arr.get(1).toString()).get(j) + " " + result);
                        }
                    } catch (Exception e) {
                        System.out.println("PROCESS IN Exception in like");
                        result += "" + new JSONObject(arr.get(1).toString()).get("var")
                                + " like (" + "'%" + arr.get(0) + "%' ) " + key + " ";
                    }

                    break;

                case "and":
                    System.out.println("PROCESS IN and ");
                    result += _toSQL(obj, "and") + " " + key + " ";
                    break;

                case "or":
                    System.out.println("PROCESS IN or");
                    result += _toSQL(obj, "or") + " " + key + " ";
                    break;

                default:
                    System.out.println("errro");
            }
        }
        return result.substring(0, result.lastIndexOf(key)) + " ) ";

    }


}
