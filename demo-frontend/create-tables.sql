delimiter $$

CREATE TABLE `data-pstadio` (
  `receive_date` varchar(255) NOT NULL,
  `receive_timestamp` varchar(255) DEFAULT NULL,
  `entity_id` varchar(255) DEFAULT NULL,
  `entity_type` varchar(255) DEFAULT NULL,
  `attr_name` varchar(255) DEFAULT NULL,
  `attr_type` varchar(255) DEFAULT NULL,
  `attr_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`receive_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$