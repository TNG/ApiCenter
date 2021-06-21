SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE application;
TRUNCATE TABLE interface;
TRUNCATE TABLE version;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO application (id, `name`, description, contact) VALUES('168a1a96337c41e6b3a61e8c43cdf863', 'applicationA', 'applicationDescriptionA', 'contactA');
INSERT INTO application (id, `name`, description, contact) VALUES('46a9df3fc37a499ea40714c5158d2ac5', 'applicationB', 'applicationDescriptionB', 'contactB');
INSERT INTO application (id, `name`, description, contact) VALUES('ad374016d15d47af8d8296fceb6d51fa', 'applicationC', 'applicationDescriptionC', 'contactC');

INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('40d2370748e8442a87f0a3f15278b745', 'interfaceA', 'interfaceDescriptionA', 0, '168a1a96337c41e6b3a61e8c43cdf863');
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('a134c5b50f354897925ab9c04cd5373b', 'interfaceB', NULL, 0, '168a1a96337c41e6b3a61e8c43cdf863');
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('72a60b0420aa4f6d94fd00c582b5ee19', 'interfaceC', 'interfaceDescriptionC', 1, '168a1a96337c41e6b3a61e8c43cdf863');
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('c071fa568143477c8efa1cbcda42bd04', 'interfaceD', NULL, 1, '46a9df3fc37a499ea40714c5158d2ac5');
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('1660d4df25e34aed8b1483842c9f3f1f', 'interfaceE', 'interfaceDescriptionE', 0, '46a9df3fc37a499ea40714c5158d2ac5');

INSERT INTO version (id, title, version, description, content, interface_id) VALUES('243a7fb09c3e4b788fce5425e43b9cbe', 'versionA', '1.0.0', 'descriptionA', '{\n  \"swagger\": \"2.0\",\n  \"info\": {\n    \"version\": \"1.0.0\",\n    \"title\": \"versionA\",\n    \"description\": \"descriptionA\",\n}")', '40d2370748e8442a87f0a3f15278b745');
INSERT INTO version (id, title, version, description, content, interface_id) VALUES('92e8ebe4c9d846b5b54ea69c025c35e5', 'versionB', '2.3.0', NULL, '{\n  \"openapi\": \"3.0\",\n  \"info\": {\n    \"version\": \"2.3.0\",\n    \"title\": \"versionB\",\n}")', '40d2370748e8442a87f0a3f15278b745');
INSERT INTO version (id, title, version, description, content, interface_id) VALUES('b344904c63fa444d9cb85d4fd804af36', 'versionC', '7.0.0', 'descriptionC', '{\n  \"swagger\": \"2.0\",\n  \"info\": {\n    \"version\": \"7.0.0\",\n    \"title\": \"versionC\",\n    \"description\": \"descriptionC\",\n}")', 'a134c5b50f354897925ab9c04cd5373b');
INSERT INTO version (id, title, version, description, content, interface_id) VALUES('c1f1c7e30aa44e339cc028933163f399', 'versionD', '2.3.4', NULL, '{\n  \"openapi\": \"3.0\",\n  \"info\": {\n    \"version\": \"2.3.4\",\n    \"title\": \"versionD\",\n}")', 'c071fa568143477c8efa1cbcda42bd04');
