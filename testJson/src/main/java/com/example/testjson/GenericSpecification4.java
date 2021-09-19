package com.example.testjson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericSpecification4<T> implements Specification<T> {

    static String s = null;

    private Object castToRequiredType(Class fieldType, String value) {
        if(fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if(fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if(Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }

    private List<Object> stringToList(String s){
        List<Object> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(s);

        for (int i = 0; i< jsonArray.length(); i++){
            System.out.println(jsonArray.get(i));
            list.add(jsonArray.get(i));
        }
        System.out.println(list);
        return list;
    }

    private Predicate createPredicates(Root<T> root, CriteriaBuilder criteriaBuilder, String s){
        System.out.println("PROCESS IN createPredicates");
        String key = s.substring(s.indexOf("\"") + 1, s.indexOf("\"", s.indexOf("\"") + 1));
        System.out.println("key: " + key);
        return _buildPredicates(root, criteriaBuilder, s, key);
    }

    private Predicate _buildPredicates(Root<T> root, CriteriaBuilder criteriaBuilder, String s, String key){
        System.out.println("PROCESS IN _buildPredicates");
        Predicate predicate1 = null;
        Predicate predicate2 = null;
        String k = key;
        JSONArray jsonArray = new JSONObject(s).getJSONArray(key);
        System.out.println("jsonArray has length: " + jsonArray.length());
        for (int i=0; i< jsonArray.length(); i++){
            String obj = jsonArray.get(i).toString();
            System.out.println("obj " + i + " " + obj);
            k = obj.substring(obj.indexOf("\"") + 1, obj.indexOf("\"", obj.indexOf("\"") + 1));
            System.out.println("k : " + k);
            JSONArray array = null;
            switch (k){
                case "==":
                    System.out.println("PROCESS IN '=='");
                    array = new JSONObject(obj).getJSONArray(k);
                    for (int j=0; j<array.length(); j+=2){
                        predicate2 = criteriaBuilder.equal(
                                root.get((String) (new JSONObject(array.get(j).toString())).get("var")), array.get(j+1).toString()  );
                    }
                    System.out.println("DONE PROCESS IN '=='");
                    break;

                case "!=":
                    System.out.println("PROCESS IN '!='");
                    array = new JSONObject(obj).getJSONArray(k);
                    for (int j=0; j<array.length(); j+=2){
                        predicate2 = criteriaBuilder.notEqual(
                                root.get((String) (new JSONObject(array.get(j).toString())).get("var")), array.get(j+1).toString()  );
                    }
                    System.out.println("DONE PROCESS IN '!='");
                    break;

                case ">":
                    System.out.println("PROCESS IN '>'");
                    array = new JSONObject(obj).getJSONArray(k);
                    for (int j=0; j<array.length(); j+=2){
                        predicate2 = criteriaBuilder.greaterThan(
                                root.get((String) (new JSONObject(array.get(j).toString())).get("var")), array.get(j+1).toString()  );
                    }
                    System.out.println("DONE PROCESS IN '>'");
                    break;

                case ">=":
                    System.out.println("PROCESS IN '>='");
                    array = new JSONObject(obj).getJSONArray(k);
                    for (int j=0; j<array.length(); j+=2){
                        predicate2 = criteriaBuilder.greaterThanOrEqualTo(
                                root.get((String) (new JSONObject(array.get(j).toString())).get("var")), array.get(j+1).toString()  );
                    }
                    System.out.println("DONE PROCESS IN '>='");
                    break;

                case "<=":
                    System.out.println("PROCESS IN '<='");
                    array = new JSONObject(obj).getJSONArray(k);
                    for (int j=0; j<array.length(); j+=2){
                        predicate2 = criteriaBuilder.lessThanOrEqualTo(
                                root.get((String) (new JSONObject(array.get(j).toString())).get("var")), array.get(j+1).toString()  );
                    }
                    System.out.println("DONE PROCESS IN '<='");
                    break;

                case "<":
                    System.out.println("PROCESS IN '<'");
                    array = new JSONObject(obj).getJSONArray(k);
                    for (int j=0; j<array.length(); j+=2){
                        predicate2 = criteriaBuilder.lessThan(
                                root.get((String) (new JSONObject(array.get(j).toString())).get("var")), array.get(j+1).toString()  );
                    }
                    System.out.println("DONE PROCESS IN '<'");
                    break;

                case "and":
                    System.out.println("PROCESS IN 'and'");
                    predicate2 = _buildPredicates(root, criteriaBuilder, obj , k);
                    System.out.println("DONE PROCESS IN 'and'");
                    break;

                case "or":
                    System.out.println("PROCESS IN 'or'");
                    predicate2 = _buildPredicates(root, criteriaBuilder, obj , k);
                    System.out.println("DONE PROCESS IN 'or'");
                    break;

                case "in":
                    array = new JSONObject(obj).getJSONArray(k);
                    try {
                        System.out.println("PROCESS IN 'in'");
                        System.out.println(root.get((String) (new JSONObject(array.get(0).toString())).get("var")).getJavaType().getName());
                        for (int j=0; j<array.length(); j+=2){
                            predicate2 = criteriaBuilder.in(
                                    root.get((String) (new JSONObject(array.get(j).toString())).get("var")))
                                    .value( stringToList(array.get(j+1).toString()));
                        }
                        System.out.println("DONE PROCESS IN 'in'");

                    } catch (Exception e) {
                        System.out.println("PROCESS IN in 'like'");
                        for (int j=0; j<array.length(); j+=2){
                            predicate2 = criteriaBuilder.like(
                                    root.get((String) (new JSONObject(array.get(j+1).toString())).get("var")),"%"+array.get(j).toString() +"%" );
                        }
                        System.out.println("DONE PROCESS IN 'like'");
                    }

                    break;

            }

            if (predicate1 == null){
                predicate1 = predicate2;
            } else {
                if (key.equals("and")){
                    predicate1 = criteriaBuilder.and(predicate1, predicate2);
                } else if (key.equals("or")){
                    predicate1 = criteriaBuilder.or(predicate1, predicate2);
                }
            }
        }
        return predicate1;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        System.out.println("Start");
        return createPredicates(root, criteriaBuilder, s);
    }

}
