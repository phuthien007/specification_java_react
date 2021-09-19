package com.example.testjson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/search-test")
public class SearchTest {

    @Autowired
    private TestRepository testRepository;

    /* test check loi : s= {
  "and": [
    {
      ">": [
        {
          "var": "id"
        },
        1
      ]
    },
    {
      "or": [
        {
          "<": [
            {
              "var": "id"
            },
            2
          ]
        },
        {
          ">=": [
            {
              "var": "slider"
            },
            1
          ]
        }
      ]
    }
  ]
} => (1) đúng, (2) sai
 explain: ( id > '1' and  ( id < '2' or slider >= '1'  )   )  (1)
            ( id > '1' and  id < '2' or slider >= '1'    ) (2) sẽ đưa kết quả khác nhau
 */

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
    @PostMapping
    public List<TestEntity> testEntityList(@RequestBody String s){
        GenericSpecification4<TestEntity> testSpec = new GenericSpecification4<>();
        GenericSpecification4.s = s;
        System.out.println(GenericSpecification4.s);
        return testRepository.findAll(testSpec);
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
    @PostMapping("/sql")
    public String testSQL(@RequestBody String s){
        return JsonTransform.toSQL(s);
    }

    @GetMapping
    public List<TestEntity> getAll(){
        return testRepository.findAll();
    }
}
