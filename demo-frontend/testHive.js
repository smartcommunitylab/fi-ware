hive = require('node-hive').for({ server:"cosmos.lab.fi-ware.org" });

hive.fetch("SELECT entity_f,AVG(value_f) as media FROM sc_data WHERE entity_f  NOT LIKE 'street@%%' AND entity_f NOT LIKE 'parking@%%' GROUP BY entity_f", function(err, data) {
  console.log(err);
  data.each(function(record) {
    console.log(record);
  });
});

