INSERT INTO application (id, `name`, description, contact) VALUES('168a1a96337c41e6b3a61e8c43cdf863', 'applicationA', 'applicationDescriptionA', 'contactA')
INSERT INTO application (id, `name`, description, contact) VALUES('46a9df3fc37a499ea40714c5158d2ac5', 'applicationB', 'applicationDescriptionB', 'contactB')
INSERT INTO application (id, `name`, description, contact) VALUES('ad374016d15d47af8d8296fceb6d51fa', 'applicationC', 'applicationDescriptionC', 'contactC')

INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('40d2370748e8442a87f0a3f15278b745', 'interfaceA', 'interfaceDescriptionA', 0, '168a1a96337c41e6b3a61e8c43cdf863')
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('a134c5b50f354897925ab9c04cd5373b', 'interfaceB', NULL, 0, '168a1a96337c41e6b3a61e8c43cdf863')
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('72a60b0420aa4f6d94fd00c582b5ee19', 'interfaceC', 'interfaceDescriptionC', 1, '168a1a96337c41e6b3a61e8c43cdf863')
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('c071fa568143477c8efa1cbcda42bd04', 'interfaceD', NULL, 1, '46a9df3fc37a499ea40714c5158d2ac5')
INSERT INTO interface (id, `name`, description, `type`, application_id) VALUES('1660d4df25e34aed8b1483842c9f3f1f', 'interfaceE', 'interfaceDescriptionE', 0, '46a9df3fc37a499ea40714c5158d2ac5')
