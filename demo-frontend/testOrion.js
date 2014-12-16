 var restler = require('restler');

/*

JSON REQUEST

{ "contextElements": [
        {
            "type": "Room",
            "isPattern": "false",
            "id": "Room1",
            "attributes": [
            {
                "name": "temperature",
                "type": "centigrade",
                "value": "26.5"
            },
            {
                "name": "pressure",
                "type": "mmHg",
                "value": "763"
            }
            ]
        }
    ],
    "updateAction": "UPDATE" 
}*/

/*

RESPONSE
 
 {
  "contextResponses" : [
    {
      "contextElement" : {
        "type" : "Room",
        "isPattern" : "false",
        "id" : "Room1000",
        "attributes" : [
          {
            "name" : "temperature",
            "type" : "centigrade",
            "value" : ""
          }
        ]
      },
      "statusCode" : {
        "code" : "200",
        "reasonPhrase" : "OK"
      }
    }
  ]
}

*/

  var url = 'http://orion:1026/NGSI10/updateContext';
  var options = {
    'headers' : {'User-Agent': 'demo-frontend', 'Content-type': 'application/json', 'Accept' : 'application/json'},
     'timeout' : 60000
  }

 
  var jsonData = {};
  jsonData['contextElements']=[];
  var ctxEl = {};
  ctxEl['type']='Room';
  ctxEl['isPattern']='false';
  ctxEl['id']='Room1002';
  ctxEl['attributes']=[{'name':'temperature','type':'centigrade','value':'21'}];
  jsonData['contextElements'].push(ctxEl);
  jsonData['updateAction'] = 'UPDATE';


  restler.postJson(url, jsonData,options).on('timeout', function(ms){
  console.log('timeout request in sec' + ms/1000);
  }).on('complete', function(data, response) {
  	 if(JSON.parse(response.rawEncoded).contextResponses[0].statusCode.code == 404){
  	 	console.log('orion update fail, trying insert...');
  	 	jsonData['updateAction'] = 'APPEND';
  	 	restler.postJson(url, jsonData,options).on('timeout', function(ms){
  			console.log('timeout request in sec' + ms/1000);
 		 }).on('complete', function(data, response) {
  	 		
  	 		if(JSON.parse(response.rawEncoded).contextResponses[0].statusCode.code == 200){
  	 			console.log('orion insert OK');
  	 		} else{
  	 			console.log('orion insert fail');
  	 		}
  	 	});
  	 }else{
  	 	console.log('orion update OK');
  	 }
  }); 
