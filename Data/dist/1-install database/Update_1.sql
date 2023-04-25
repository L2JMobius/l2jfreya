DROP TABLE IF EXISTS `crests`;
CREATE TABLE `crests` (
  `crest_id` int(11) NOT NULL DEFAULT '0',
  `data` varbinary(2176) NOT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`crest_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;